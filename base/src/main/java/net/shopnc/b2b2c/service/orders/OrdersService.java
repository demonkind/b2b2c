package net.shopnc.b2b2c.service.orders;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.goods.GoodsSaleDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.orders.OrdersGoodsDao;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.b2b2c.service.member.ExperienceService;
import net.shopnc.b2b2c.service.member.PointsService;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 订单处理公用
 * Created by hbj on 2015/12/22.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class OrdersService extends BaseService {
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private PredepositService predepositService;
    @Autowired
    private GoodsSaleDao goodsSaleDao;
    @Autowired
    private OrdersGoodsDao ordersGoodsDao;
    @Autowired
    private PointsService pointsService;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private SendMessageService sendMessageService;

    /**
     * 取消订单公用方法
     * @param orders
     * @throws ShopException
     */
    public void cancelOrders(Orders orders) throws ShopException {
        //更新订单状态
        List<Object> setItems = new ArrayList<Object>();
        setItems.add("ordersState = :ordersState");
        setItems.add("cancelReason = :cancelReason");
        setItems.add("cancelTime = :cancelTime");
        setItems.add("cancelRole = :cancelRole");
        setItems.add("predepositAmount = :predepositAmount");

        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ordersState",OrdersOrdersState.CANCEL);
        params.put("cancelReason", orders.getCancelReason());
        params.put("ordersId", orders.getOrdersId());
        params.put("cancelTime",orders.getCancelTime());
        params.put("cancelRole", orders.getCancelRole());
        params.put("predepositAmount",new BigDecimal(0));
        ordersDao.editOrders(setItems, whereItems, params);

        //恢复库存销量
        List<OrdersGoods> ordersGoodsList = ordersGoodsDao.getOrdersGoodsListByOrdersId(orders.getOrdersId());
        for (OrdersGoods ordersGoods : ordersGoodsList) {
            goodsSaleDao.updateFromCancelOrders(ordersGoods.getGoodsId(),ordersGoods.getCommonId(), ordersGoods.getBuyNum());
        }

        //解锁预存款
        if (PriceHelper.isGreaterThan(orders.getPredepositAmount(),new BigDecimal(0))) {
            //未付款成功时解锁，其它情况不解锁（如完全退款后取消订单）
            if (orders.getOrdersState() == OrdersOrdersState.NEW) {
                predepositService.addLogCancelOrders(orders.getPredepositAmount(), orders.getMemberId(), orders.getOrdersSn());
            }
        }

        logger.info("订单取消消息通知");
        HashMap<String, Object> hashMapMsg = new HashMap<>();
        hashMapMsg.put("ordersSn", Long.toString(orders.getOrdersSn()));
        hashMapMsg.put("cancelTime", (new SimpleDateFormat("yyyy-MM-dd").format(orders.getCancelTime())).toString());
        sendMessageService.sendStore("storeOrdersCancel", orders.getStoreId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
        sendMessageService.sendMember("memberOrdersCancel", orders.getMemberId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
    }

    /**
     * 修改运费
     * @param orders
     */
    public void modifyFreight(Orders orders) {
        //更新订单状态
        List<Object> setItems = new ArrayList<Object>();
        setItems.add("freightAmount = :freightAmount");
        setItems.add("ordersAmount = :ordersAmount");

        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ordersId",orders.getOrdersId());
        params.put("freightAmount", orders.getFreightAmount());
        params.put("ordersAmount", orders.getOrdersAmount());
        ordersDao.editOrders(setItems, whereItems, params);

        //消息通知
        logger.info("修改运费消息通知");
        HashMap<String, Object> hashMapMsg = new HashMap<>();
        hashMapMsg.put("ordersSn",Long.toString(orders.getOrdersSn()));
        hashMapMsg.put("freightAmount",orders.getFreightAmount().toString());
        hashMapMsg.put("storeName", orders.getStoreName());
        sendMessageService.sendMember("memberOrdersModifyFreight", orders.getMemberId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
    }

    /**
     * 订单付款
     * @param orders
     * @throws Exception
     */
    public void payOrders(Orders orders) throws Exception{
        List<Object> setItems = new ArrayList<Object>();
        setItems.add("ordersState = :ordersState");
        setItems.add("paymentTime = :paymentTime");
        setItems.add("paymentCode = :paymentCode");
        setItems.add("outOrdersSn = :outOrdersSn");

        //管理员收款
        if (orders.getAdminReceivePayState() == State.YES) {
            setItems.add("adminReceivePayState = 1");
        }

        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");

        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersState", OrdersOrdersState.PAY);
        params.put("paymentTime", orders.getPaymentTime());
        params.put("paymentCode", orders.getPaymentCode());
        params.put("ordersId",orders.getOrdersId());
        params.put("outOrdersSn",orders.getOutOrdersSn());
        ordersDao.editOrders(setItems, whereItems, params);

        //彻底扣除预存款
        if (PriceHelper.isGreaterThan(orders.getPredepositAmount(),new BigDecimal(0))) {
            predepositService.addLogPaySuccessOrders(orders.getPredepositAmount(), orders.getMemberId(), orders.getOrdersSn());
        }

        //消息通知
        logger.info("支付消息通知");
        HashMap<String, Object> hashMapMsg = new HashMap<>();
        hashMapMsg.put("ordersSn",Long.toString(orders.getOrdersSn()));
        hashMapMsg.put("paymentTime",(new SimpleDateFormat("yyyy-MM-dd").format(orders.getPaymentTime())).toString());
        hashMapMsg.put("memberName",orders.getMemberName());
        sendMessageService.sendStore("storeOrdersPay", orders.getStoreId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
        hashMapMsg = new HashMap<>();
        hashMapMsg.put("ordersSn",Long.toString(orders.getOrdersSn()));
        hashMapMsg.put("paymentTime",(new SimpleDateFormat("yyyy-MM-dd").format(orders.getPaymentTime())).toString());
        hashMapMsg.put("memberName",orders.getMemberName());
        sendMessageService.sendMember("memberOrdersPay", orders.getMemberId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
    }

    /**
     * 发货
     * @param orders
     */
    public void sendOrders(Orders orders) {
        //更新订单状态
        List<Object> setItems = new ArrayList<Object>();
        if (orders.getShipCode() != null && orders.getShipName() != null && orders.getShipSn() != null) {
            setItems.add("shipSn = :shipSn");
            setItems.add("shipName = :shipName");
            setItems.add("shipCode = :shipCode");
        }
        setItems.add("shipNote = :shipNote");
        setItems.add("receiverName = :receiverName");
        setItems.add("receiverPhone = :receiverPhone");
        setItems.add("receiverAddress = :receiverAddress");
        if (orders.getReceiverAreaId1() > 0) {
            setItems.add("receiverAreaId1 = :receiverAreaId1");
            setItems.add("receiverAreaId2 = :receiverAreaId2");
            setItems.add("receiverAreaId3 = :receiverAreaId3");
            setItems.add("receiverAreaId4 = :receiverAreaId4");
            setItems.add("receiverAreaInfo = :receiverAreaInfo");
        }

        if (orders.getOrdersState() == OrdersOrdersState.PAY) {
            setItems.add("ordersState = :ordersState");
            setItems.add("sendTime = :sendTime");
            setItems.add("autoReceiveTime = :autoReceiveTime");
        }

        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ordersId", orders.getOrdersId());
        if (orders.getShipCode() != null && orders.getShipName() != null && orders.getShipSn() != null) {
            params.put("shipSn", orders.getShipSn());
            params.put("shipName", orders.getShipName());
            params.put("shipCode", orders.getShipCode());
        }
        params.put("shipNote", orders.getShipNote());
        params.put("receiverName", orders.getReceiverName());
        params.put("receiverPhone", orders.getReceiverPhone());
        params.put("receiverAddress",orders.getReceiverAddress());
        if (orders.getReceiverAreaId1() > 0) {
            params.put("receiverAreaId1",orders.getReceiverAreaId1());
            params.put("receiverAreaId2",orders.getReceiverAreaId2());
            params.put("receiverAreaId3",orders.getReceiverAreaId3());
            params.put("receiverAreaId4",orders.getReceiverAreaId4());
            params.put("receiverAreaInfo",orders.getReceiverAreaInfo());
        }

        if (orders.getOrdersState() == OrdersOrdersState.PAY) {
            params.put("sendTime",orders.getSendTime());
            params.put("ordersState",OrdersOrdersState.SEND);
            params.put("autoReceiveTime",ShopHelper.getFutureTimestamp(orders.getSendTime(), Calendar.DATE, Common.ORDER_AUTO_RECEIVE_TIME));
        }

        ordersDao.editOrders(setItems, whereItems, params);
        if (orders.getOrdersState() == OrdersOrdersState.PAY) {
            //消息通知
            logger.info("发货消息通知");
            HashMap<String, Object> hashMapMsg = new HashMap<>();
            hashMapMsg.put("ordersSn",Long.toString(orders.getOrdersSn()));
            hashMapMsg.put("sendTime",(new SimpleDateFormat("yyyy-MM-dd").format(orders.getSendTime())).toString());
            hashMapMsg.put("storeName",orders.getStoreName());
            hashMapMsg.put("shipSn",params.containsKey("shipSn") ? params.get("shipSn") : "无");
            sendMessageService.sendMember("memberOrdersSend", orders.getMemberId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
        } else if (orders.getOrdersState() == OrdersOrdersState.SEND) {
            //修改发货信息
        }
    }

    /**
     * 订单列表
     * @param payId
     * @param memberId
     * @return
     */
    public List<Orders> getOrdersList(int payId, int memberId) {
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("payId = :payId");
        whereItems.add(("memberId = :memberId"));

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("payId", payId);
        params.put("memberId", memberId);
        List<Orders> ordersList = ordersDao.getOrdersList(whereItems, params);
       return ordersList;
    }

    /**
     * 订单详情
     * @param params
     * @return
     */
    public Orders getOrdersInfo(HashMap<String,Object> params) {
        //取得订单orders
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String,Object> values = new HashMap<String, Object>();
        for(String key : params.keySet()) {
            if (key.equals("ordersId")) {
                conditions.add("ordersId = :ordersId");
                values.put("ordersId",params.get(key));
            }
            if (key.equals("storeId")) {
                conditions.add("storeId = :storeId");
                values.put("storeId",params.get(key));
            }
        }
        Orders orders = ordersDao.getOrdersInfo(conditions, values);

        return orders;
    }

    /**
     * 收货
     * @param orders
     * @throws ShopException
     */
    public void receiveOrders(Orders orders) throws ShopException {

        //计算佣金
        List<OrdersGoods> ordersGoodsList = ordersGoodsDao.getOrdersGoodsListByOrdersId(orders.getOrdersId());
        BigDecimal commissionAmount = new BigDecimal(0);
        for (OrdersGoods ordersGoods : ordersGoodsList) {
            commissionAmount = PriceHelper.add(commissionAmount,ordersGoods.getCommissionAmount());
        }

        //更新订单状态
        List<Object> setItems = new ArrayList<Object>();
        setItems.add("ordersState = :ordersState");
        setItems.add("finishTime = :finishTime");
        setItems.add("commissionAmount = :commissionAmount");

        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ordersState", OrdersOrdersState.FINISH);
        params.put("ordersId", orders.getOrdersId());
        params.put("finishTime", orders.getFinishTime());
        params.put("commissionAmount", commissionAmount);
        ordersDao.editOrders(setItems, whereItems, params);
        //增加会员积分
        pointsService.addPointsOrders(orders.getMemberId(), orders.getOrdersAmount(), orders.getOrdersSn());
        //增加会员经验值
        experienceService.addExperienceOrders(orders.getMemberId(), orders.getOrdersAmount(), orders.getOrdersSn());

        //消息通知
        logger.info("收货消息通知");
        HashMap<String, Object> hashMapMsg = new HashMap<>();
        hashMapMsg.put("ordersSn", Long.toString(orders.getOrdersSn()));
        hashMapMsg.put("finishTime", (new SimpleDateFormat("yyyy-MM-dd").format(orders.getFinishTime())).toString());
        hashMapMsg.put("memberName", orders.getMemberName());
        sendMessageService.sendStore("storeOrdersReceive", orders.getStoreId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
        hashMapMsg = new HashMap<>();
        hashMapMsg.put("ordersSn",Long.toString(orders.getOrdersSn()));
        hashMapMsg.put("finishTime", (new SimpleDateFormat("yyyy-MM-dd").format(orders.getFinishTime())).toString());
        hashMapMsg.put("memberName", orders.getMemberName());
        sendMessageService.sendMember("memberOrdersReceive", orders.getMemberId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
    }

    /**
     * 延迟收货
     * @param orders
     */
    public void delayReceivOrders(Orders orders) throws Exception {
        //更新订单状态
        List<Object> setItems = new ArrayList<Object>();
        setItems.add("delayReceiveState = :delayReceiveState");
        setItems.add("autoReceiveTime = :autoReceiveTime");

        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("delayReceiveState", orders.getDelayReceiveState());
        params.put("autoReceiveTime", orders.getAutoReceiveTime());
        params.put("ordersId", orders.getOrdersId());
        ordersDao.editOrders(setItems, whereItems, params);

        //消息通知
        logger.info("买家延迟收货消息通知");
        HashMap<String, Object> hashMapMsg = new HashMap<>();
        hashMapMsg.put("ordersSn",Long.toString(orders.getOrdersSn()));
        hashMapMsg.put("autoReceiveTime", (new SimpleDateFormat("yyyy-MM-dd").format(orders.getAutoReceiveTime())).toString());
        hashMapMsg.put("memberName", orders.getMemberName());
        sendMessageService.sendStore("storeOrdersDelay", orders.getStoreId(), hashMapMsg, Integer.toString(orders.getOrdersId()));

    }

    /**
     * 快递跟踪
     * @param shipSn
     * @param shipCode
     * @return
     * @throws Exception
     */
    public String shipSearch(String shipSn, String shipCode) throws Exception {
        String url = String.format("http://www.kuaidi100.com/query?type=%s&postid=%s&id=1&valicode=&temp=%s",shipCode,shipSn, Double.toString(Math.random()));
        logger.info(url);
        return ShopHelper.httpGet(url);
    }

    /**
     * 店铺交易中的订单数
     * @param storeId
     * @return
     */
    public long getProgressingCountByStoreId(int storeId){
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return ordersDao.getProgressingCount(where, params);
    }

    /**
     * 店铺下单，待收款订单总数
     * @param storeId
     * @return
     */
    public long getOrdersNewCountByStoreId(int storeId){
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return ordersDao.getOrdersNewCount(where, params);
    }

    /**
     * 店铺付款，待发货订单总数
     * @param storeId
     * @return
     */
    public long getOrdersPayCountByStoreId(int storeId){
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return ordersDao.getOrdersPayCount(where, params);
    }
}
