package net.shopnc.b2b2c.service.orders;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.constant.OrdersPaymentCode;
import net.shopnc.b2b2c.constant.OrdersPaymentTypeCode;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.orders.OrdersPayDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersPay;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.b2b2c.vo.orders.OrdersPayVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.common.entity.buy.OrdersPayEntity;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 支付单
 * Created by hbj on 2015/12/22.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class PayService extends BaseService {
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PredepositService predepositService;
    @Autowired
    private OrdersService ordersService;


    /**
     * 取得支付单信息
     * @param payId
     * @param memberId
     * @return
     */
    public OrdersPayEntity getOrdersPayEntityInfo(int payId,int memberId) throws Exception {
        OrdersPayEntity ordersPayEntity = getOrdersPayEntityInfo(payId);
        if (ordersPayEntity != null) {
            for(int i=0; i<ordersPayEntity.getOrdersList().size(); i++) {
                if (ordersPayEntity.getOrdersList().get(i).getMemberId() != memberId) {
                    ordersPayEntity.getOrdersList().remove(i);
                }
            }
        }
        return ordersPayEntity;
    }

    /**
     * 取得支付单信息
     * @param payId
     * @return
     * @throws Exception
     */
    public OrdersPayEntity getOrdersPayEntityInfo(int payId) throws Exception {
        //订单列表
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("payId = :payId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("payId",payId);
        List<Orders> ordersList = ordersDao.getOrdersList(whereItems, params);
        if (ordersList.size() == 0) {
            throw new ShopException("订单不存在");
        }
        //锁表查询最新信息
        List<Orders> ordersList1 = new ArrayList<>();
        for (int i=0; i<ordersList.size(); i++) {
            Orders orders = ordersDao.getLockModelOrdersInfo(ordersList.get(i).getOrdersId());
            ordersList1.add(orders);
        }
        if (ordersList1.size() == 0) {
            throw new ShopException("订单不存在");
        }
        OrdersPayEntity ordersPayEntity = new OrdersPayEntity(ordersList1);
        return ordersPayEntity;
    }
    
    /**
     * 取得支付单信息
     * @param payId
     * @return
     * @throws Exception
     */
    public List<Orders> getOrdersByPauId(int payId) throws Exception {
        //订单列表
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("payId = :payId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("payId",payId);
        List<Orders> ordersList = ordersDao.getOrdersList(whereItems, params);
        return ordersList;
    }

    /**
     * 预存款支付
     * @param ordersPayEntity
     * @param payPwd
     * @return
     * @throws Exception
     */
    public OrdersPayEntity predepositPay(OrdersPayEntity ordersPayEntity, String payPwd) throws Exception {
        List<Orders> ordersList = ordersPayEntity.getOrdersList();
        Member member = ordersPayEntity.getMember();
        memberService.validatePayPwd(payPwd, member.getMemberId());
        BigDecimal predepositAvailableAmount = member.getPredepositAvailable();
        //应支付金额
        BigDecimal payAmount;
        if (PriceHelper.isGreaterThan(predepositAvailableAmount,new BigDecimal(0)) == false) {
            throw new ShopException("预存款不足");
        }
        for(int i=0; i<ordersList.size(); i++) {
            if (ordersList.get(i).getOrdersState() != OrdersOrdersState.NEW
                    || (ordersList.get(i).getPaymentTypeCode() != null
                    && ordersList.get(i).getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE))
                    || PriceHelper.isLessThanOrEquals(predepositAvailableAmount,new BigDecimal(0)) == true) {
                continue;
            }
            payAmount = ordersList.get(i).getOrdersAmount();
            if (PriceHelper.isGreaterThanOrEquals(predepositAvailableAmount, payAmount)) {
                logger.info("预存款可完全支付");
                //预存款足够独立支付，直接扣可用预存款
                predepositService.addLogOrdersFullPay(payAmount, member.getMemberId(), ordersList.get(i).getOrdersSn());

                //更改订单支付状态、支付时间、支付方式[完全预存款支付时不记predepositAmount]
                ordersList.get(i).setPaymentCode(OrdersPaymentCode.PREDEPOSIT);
                ordersList.get(i).setPaymentTime(ShopHelper.getCurrentTimestamp());
                ordersService.payOrders(ordersList.get(i));

                //可用预存款变量值变更
                predepositAvailableAmount = PriceHelper.sub(predepositAvailableAmount,payAmount);

                //更新订单实体[用于返回]
                ordersList.get(i).setOrdersState(OrdersOrdersState.PAY);

                //更新member实体预存款[用于返回]
                member.setPredepositAvailable(predepositAvailableAmount);

            } else {
                logger.info("预存款支付部分金额");
                //预存款不足以完全支付，可用金额减少，冻结金额增加
                predepositService.addLogOrdersPartPay(predepositAvailableAmount, member.getMemberId(), ordersList.get(i).getOrdersSn());

                //更新member实体预存款[用于返回]
                member.setPredepositAvailable(new BigDecimal(0));
                member.setPredepositFreeze(PriceHelper.add(member.getPredepositFreeze(), predepositAvailableAmount));

                //更新订单实体[用于返回]
                ordersList.get(i).setPredepositAmount(predepositAvailableAmount);

                //更改订单预存款支付金额
                List<Object> setItems = new ArrayList<Object>();
                setItems.add("predepositAmount = :predepositAmount");

                List<Object> whereItems = new ArrayList<Object>();
                whereItems.add("ordersId = :ordersId");

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("predepositAmount", predepositAvailableAmount);
                params.put("ordersId", ordersList.get(i).getOrdersId());
                ordersDao.editOrders(setItems, whereItems, params);

                //可用预存款变量值变更
                predepositAvailableAmount = new BigDecimal(0);
            }
        }
        ordersPayEntity = new OrdersPayEntity(ordersList);
        ordersPayEntity.setMember(member);
        return ordersPayEntity;
    }

}
