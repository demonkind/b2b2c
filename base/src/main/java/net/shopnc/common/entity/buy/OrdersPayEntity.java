package net.shopnc.common.entity.buy;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.constant.OrdersPaymentTypeCode;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.common.util.PriceHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by hbj on 2016/2/1.
 */
public class OrdersPayEntity {
    private List<Orders> ordersList;
    /**
     * 支付单ID
     */
    private int payId;
    /**
     * 支付单号
     */
    private long paySn;
    /**
     * 在线待支付订单总金额(选择在线支付的订单且未支付状态的订单总金额)
     */
    private BigDecimal ordersOnlineAmount = new BigDecimal(0);
    /**
     * 货到付款待支付订单总金额(未取消、未收货)
     */
    private BigDecimal ordersOfflineAmount = new BigDecimal(0);
    /**
     * 还需要在线支付金额(选择在线支付的订单，使用站内余额方式支付后还在线支付的差额)
     */
    private BigDecimal ordersOnlineDiffAmount = new BigDecimal(0);
    /**
     * 使用预存款支付金额
     */
    private BigDecimal predepositAmount = new BigDecimal(0);
    /**
     * 选择在线支付方式且已经完成支付的订单总金额
     */
    private BigDecimal ordersOnlinePayAmount = new BigDecimal(0);
    /**
     * 是否存在在线支付且未支付的订单
     */
    private int isExistOnlineNoPay = State.NO;
    /**
     * 会员实体
     */
    private Member member;

    public OrdersPayEntity(List<Orders> ordersList) {
        BigDecimal diffAmount;
        //金额计算
        for (int i=0; i<ordersList.size(); i++) {
            //在线待支付金额
            if (ordersList.get(i).getOrdersState() == OrdersOrdersState.NEW) {
                ordersOnlineAmount = PriceHelper.add(ordersOnlineAmount, ordersList.get(i).getOrdersAmount());
                isExistOnlineNoPay = State.YES;
            }
            //货到付款待支付订单总金额
            if (ordersList.get(i).getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE) &&
                    (ordersList.get(i).getOrdersState() == OrdersOrdersState.PAY ||
                            ordersList.get(i).getOrdersState() == OrdersOrdersState.SEND)) {
                ordersOfflineAmount = PriceHelper.add(ordersOfflineAmount,ordersList.get(i).getOrdersAmount());
            }
            //还需要在线支付金额
            if (ordersList.get(i).getOrdersState() == OrdersOrdersState.NEW) {
                diffAmount = PriceHelper.sub(ordersList.get(i).getOrdersAmount(),ordersList.get(i).getPredepositAmount());
                ordersOnlineDiffAmount = PriceHelper.add(ordersOnlineDiffAmount,diffAmount);
            }
            //预存款支付金额
            predepositAmount = PriceHelper.add(predepositAmount,ordersList.get(i).getPredepositAmount());
            //使用在线支付方式且已经完成支付的订单总金额
            if (ordersList.get(i).getPaymentTypeCode().equals(OrdersPaymentTypeCode.ONLINE) && ordersList.get(i).getOrdersState() == OrdersOrdersState.PAY) {
                ordersOnlinePayAmount = PriceHelper.add(ordersOnlinePayAmount, ordersList.get(i).getOrdersAmount());
            }
        }
        this.payId = ordersList.get(0).getPayId();
        this.paySn = ordersList.get(0).getPaySn();
        this.ordersList = ordersList;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
    }

    public BigDecimal getOrdersOnlineAmount() {
        return ordersOnlineAmount;
    }

    public void setOrdersOnlineAmount(BigDecimal ordersOnlineAmount) {
        this.ordersOnlineAmount = ordersOnlineAmount;
    }

    public BigDecimal getOrdersOfflineAmount() {
        return ordersOfflineAmount;
    }

    public void setOrdersOfflineAmount(BigDecimal ordersOfflineAmount) {
        this.ordersOfflineAmount = ordersOfflineAmount;
    }

    public BigDecimal getOrdersOnlineDiffAmount() {
        return ordersOnlineDiffAmount;
    }

    public void setOrdersOnlineDiffAmount(BigDecimal ordersOnlineDiffAmount) {
        this.ordersOnlineDiffAmount = ordersOnlineDiffAmount;
    }

    public BigDecimal getPredepositAmount() {
        return predepositAmount;
    }

    public void setPredepositAmount(BigDecimal predepositAmount) {
        this.predepositAmount = predepositAmount;
    }

    public BigDecimal getOrdersOnlinePayAmount() {
        return ordersOnlinePayAmount;
    }

    public void setOrdersOnlinePayAmount(BigDecimal ordersOnlinePayAmount) {
        this.ordersOnlinePayAmount = ordersOnlinePayAmount;
    }

    public int getIsExistOnlineNoPay() {
        return isExistOnlineNoPay;
    }

    public void setIsExistOnlineNoPay(int isExistOnlineNoPay) {
        this.isExistOnlineNoPay = isExistOnlineNoPay;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "OrdersPayEntity{" +
                "ordersList=" + ordersList +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", ordersOnlineAmount=" + ordersOnlineAmount +
                ", ordersOfflineAmount=" + ordersOfflineAmount +
                ", ordersOnlineDiffAmount=" + ordersOnlineDiffAmount +
                ", predepositAmount=" + predepositAmount +
                ", ordersOnlinePayAmount=" + ordersOnlinePayAmount +
                ", isExistOnlineNoPay=" + isExistOnlineNoPay +
                ", member=" + member +
                '}';
    }
}
