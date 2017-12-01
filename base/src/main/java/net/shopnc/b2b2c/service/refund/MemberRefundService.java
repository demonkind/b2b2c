package net.shopnc.b2b2c.service.refund;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.constant.OrdersPaymentCode;
import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.dao.refund.RefundReasonDao;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.domain.refund.RefundReason;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 退款退货原因
 * Created by cj on 2016/2/1.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class MemberRefundService extends RefundService {

    @Autowired
    MemberOrdersService memberOrdersService;

    @Autowired
    RefundReasonDao refundReasonDao;

    @Autowired
    SendMessageService sendMessageService;

    /**
     * 新建整单退款申请（买家）
     *
     * @param refund
     */
    public int saveRefundAll(Refund refund) throws ShopException {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", refund.getMemberId());
        params.put("ordersId", refund.getOrdersId());
//        OrdersVo ordersVo = memberOrdersService.getOrdersVoInfo(params);
        Orders orders = ordersDao.get(Orders.class, refund.getOrdersId());
        if (allowRefundAll(orders.getOrdersId(), orders.getOrdersState(), orders.getPaymentTypeCode()) == false) {
            throw new ShopException("参数错误");
        }
        refund.setAddTime(ShopHelper.getCurrentTimestamp());
        refund.setMemberName(orders.getMemberName());
        refund.setOrdersSn(orders.getOrdersSn());
        refund.setRefundSn(makeRefundSn(orders.getStoreId()));
        refund.setRefundType(RefundState.REFUND_TYPE_REFUND);
        refund.setSellerState(RefundState.SELLER_STATE_WAITING);
        refund.setOrderLock(RefundState.ORDER_LOCK);
        refund.setGoodsId(RefundState.REFUND_NONE);
        refund.setOrdersGoodsId(RefundState.REFUND_NONE);
        refund.setReasonId(RefundState.REFUND_NONE);
        refund.setReasonInfo(RefundState.REFUND_ALL_REASON_INFO);
        refund.setGoodsName(RefundState.REFUND_ALL_GOODS_NAME);
        refund.setStoreId(orders.getStoreId());
        refund.setStoreName(orders.getStoreName());
        refund.setRefundAmount(orders.getOrdersAmount());
//        refund.setCommissionRate(orders.getCom);

        refundDao.save(refund);

        //订单锁定
        lockAndUnlockOrdersById(refund.getOrdersId(),ORDERS_LOCK);

        //发送卖家信息
        HashMap<String, Object> hashMapMsg = new HashMap<>();
        hashMapMsg.put("refundSn", Long.toString(refund.getRefundSn()));
        sendMessageService.sendStore("storeRefund", refund.getStoreId(), hashMapMsg, Integer.toString(refund.getRefundId()));
        return refund.getRefundId();

    }

    /**
     * 保存单品退款
     *
     * @param refund
     */
    public void saveGoodsRefund(Refund refund) throws ShopException {
        //订单详情
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", refund.getMemberId());
        params.put("ordersId", refund.getOrdersId());
        OrdersVo ordersVo = memberOrdersService.getOrdersVoInfo(params);

        //商品详情
        OrdersGoodsVo ordersGoodsVo = new OrdersGoodsVo();
        for (int i = 0; i < ordersVo.getOrdersGoodsVoList().size(); i++) {
            OrdersGoodsVo _ordersGoodsVo = (OrdersGoodsVo) ordersVo.getOrdersGoodsVoList().get(i);
            if (_ordersGoodsVo.getOrdersGoodsId() == refund.getOrdersGoodsId()) {
                ordersGoodsVo = _ordersGoodsVo;
                break;
            }
        }
        /*验证是否可以单品退款*/
        //是否有退款记录
        if (getRefundWaitingCount(refund.getOrdersId(), refund.getOrdersGoodsId(), refund.getMemberId()) > 0) {
            throw new ShopException("退款申请正在处理，请等待。");
        }
        //是否
        if (!allowRefundByOrders(ordersVo)) {
            throw new ShopException("该订单无法申请退款。");
        }
        //修正商品成交价格（可退金额）
        ordersGoodsVo.setGoodsPayAmount(memberReviseGoodsPayAmount(ordersVo.getOrdersAmount(), ordersGoodsVo.getGoodsPayAmount(), ordersVo.getRefundAmount()));

        //退款退货原因
        RefundReason refundReason = refundReasonDao.get(RefundReason.class, refund.getReasonId());
        /*修改数据*/
        //退款原因详情
        refund.setReasonInfo(refundReason.getReasonInfo());
        //退款金额修正,退款金额小于等于0，大于可退金额
        if (PriceHelper.isLessThanOrEquals(refund.getRefundAmount(), BigDecimal.ZERO) || PriceHelper.isGreaterThan(refund.getRefundAmount(), ordersGoodsVo.getGoodsPayAmount())) {
            refund.setRefundAmount(ordersGoodsVo.getGoodsPayAmount());
        }
        //锁定类型
        if (ordersVo.getOrdersState() == OrdersOrdersState.SEND) {
            refund.setOrderLock(RefundState.ORDER_LOCK);
            //锁定订单
            lockAndUnlockOrdersById(ordersVo.getOrdersId(), ORDERS_LOCK);
        }
//        refund.setRefundType(RefundState.REFUND_TYPE_REFUND);
        refund.setSellerState(RefundState.SELLER_STATE_WAITING);
        refund.setAddTime(ShopHelper.getCurrentTimestamp());
        refund.setStoreId(ordersVo.getStoreId());
        refund.setStoreName(ordersVo.getStoreName());
        refund.setGoodsId(ordersGoodsVo.getGoodsId());
        refund.setGoodsName(ordersGoodsVo.getGoodsName());
        refund.setOrdersSn(ordersVo.getOrdersSn());
        refund.setRefundSn(super.makeRefundSn(ordersVo.getStoreId()));
        refund.setGoodsImage(ordersGoodsVo.getGoodsImage());
        refund.setCommissionRate(ordersGoodsVo.getCommissionRate());
        refundDao.save(refund);
        //发送卖家信息
        HashMap<String, Object> hashMapMsg = new HashMap<>();
        hashMapMsg.put("refundSn", Long.toString(refund.getRefundSn()));
        if (refund.getRefundType() == RefundState.REFUND_TYPE_REFUND){
            sendMessageService.sendStore("storeRefund", refund.getStoreId(), hashMapMsg, Integer.toString(refund.getRefundId()));
        }else if(refund.getRefundType() == RefundState.REFUND_TYPE_RETURN){
            sendMessageService.sendStore("storeReturn", refund.getStoreId(), hashMapMsg, Integer.toString(refund.getRefundId()));
        }
    }

    /**
     * 校正可退金额
     * 买家单商品退款时用
     *
     * @param orderAmount    订单金额
     * @param goodsPayAmount 商品实际支付金额
     * @param refundAmount   退款金额
     * @return
     */
    public BigDecimal memberReviseGoodsPayAmount(BigDecimal orderAmount, BigDecimal goodsPayAmount, BigDecimal refundAmount) {
        if (PriceHelper.isLessThan(orderAmount, PriceHelper.add(goodsPayAmount, refundAmount))) {
            return PriceHelper.sub(orderAmount, refundAmount);
        }
        return goodsPayAmount;
    }

    /**
     * 退货发货设置
     *
     * @param refundId
     * @param shipId
     * @param shipSn
     * @param memberId
     */
    public void saveReturnShip(int refundId, int shipId, String shipSn, int memberId) throws ShopException {
        Refund refund = getRefund(refundId, memberId);
        if (refund == null) {
            throw new ShopException("发货失败[参数错误]");
        }
        //检测物流

        if (getShipCompany(shipId) == null) {
            throw new ShopException("发货失败[获取物流信息失败]");
        }
        //状态判断this.sellerState == 2 && this.returnType ==2 && this.goodsState ==1
        if (refund.getSellerState() == RefundState.SELLER_STATE_AGREE && refund.getReturnType() == RefundState.RETURN_TYPE_RETURNED_YES && refund.getGoodsState() == RefundState.REFUND_STATE_WAITING) {
            refund.setShipTime(ShopHelper.getCurrentTimestamp());
            refund.setDelayTime(ShopHelper.getCurrentTimestamp());
            refund.setShipId(shipId);
            refund.setShipSn(shipSn);
            refund.setGoodsState(RefundState.RETURN_SHIP_STATE_SEND);
            refundDao.update(refund);
        } else {
            throw new ShopException("发货失败[退款状态错误]");
        }
        //TODO:发送商家信息，告诉商家商品发货

    }

    /**
     * 退货延迟
     * 当发货后5天之后，商家点击未收到货的时候
     *
     * @param refundId
     * @param memberId
     */

    public void saveReturnDelay(int refundId, int memberId) throws ShopException {
        Refund refund = getRefund(refundId, memberId);
        if (refund == null) {
            throw new ShopException("延迟失败[参数错误]");
        }
        if (refund.getSellerState() != RefundState.SELLER_STATE_AGREE || refund.getGoodsState() != RefundState.RETURN_SHIP_STATE_UNRECEIVED) {
            throw new ShopException("延迟失败[退货单状态错误]");
        }
        refund.setDelayTime(ShopHelper.getCurrentTimestamp());
        refund.setGoodsState(RefundState.RETURN_SHIP_STATE_SEND);
        refundDao.update(refund);
        //TODO:发送商家消息
    }

    /**
     * 获取卖家同意的退款单
     * 用于显示退款的价格
     * @param refundItemVoList
     * @return
     * refundItemVo.getRefundState() == RefundState.REFUND_STATE_FINISH && refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE
     */
    public RefundItemVo getSellerAgreeRefund(List<RefundItemVo> refundItemVoList){
        if (refundItemVoList.size() == 0)
        {
            return null;
        }
        for (RefundItemVo refundItemVo:refundItemVoList){
            if (refundItemVo.getRefundState() == RefundState.REFUND_STATE_FINISH && refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE){
                return refundItemVo;
            }
        }
        return null;
    }


    /**
     * 获取正在处理的退款单
     * 商家同意，且退款单未完成
     * @param refundItemVoList
     * @return
     */
    public RefundItemVo getSellerDisagreeRefund(List<RefundItemVo> refundItemVoList){
        if (refundItemVoList.size() == 0 ){
            return null;
        }
        for (RefundItemVo refundItemVo:refundItemVoList){
            if ( refundItemVo.getRefundState() < RefundState.REFUND_STATE_FINISH){
                return refundItemVo;
            }
        }
        return null;
    }
    /**
     * 根据订单来判断是否能够退款退货
     * @param ordersVo
     * @return
     */
    public Boolean allowRefundByOrders(OrdersVo ordersVo){
        Boolean result = false;

        switch (ordersVo.getOrdersState()){
            case OrdersOrdersState.SEND:
                if (!ordersVo.getPaymentTypeCode().equals(OrdersPaymentCode.OFFLINE)){
                    result = true;
                }
                break;
            case OrdersOrdersState.FINISH:
                if(ordersVo.getFinishTime() == null ||ordersVo.getAutoReceiveTime() == null ){
                    break;
                }
                Timestamp delayTime = ordersVo.getAutoReceiveTime().before(ordersVo.getFinishTime()) ? ordersVo.getFinishTime(): ordersVo.getAutoReceiveTime();
                Timestamp future = ShopHelper.getFutureTimestamp(delayTime,Calendar.DATE,ORDER_REFUND);
                if (future.after(ShopHelper.getCurrentTimestamp())){
                    result = true;
                }
                break;
        }
        return result;
    }
}
