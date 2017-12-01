package net.shopnc.b2b2c.vo.orders;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.constant.OrdersPaymentTypeCode;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.common.util.PriceHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单支付Vo
 * Created by hbj on 2015/12/30.
 */
public class OrdersPayVo {
    private List<OrdersVo> ordersVoList;
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
     * 是否存在在线支付且未支付的订单
     */
    private int isExistOnlineNoPay = 0;

    public OrdersPayVo() {}
    public OrdersPayVo(List<OrdersVo> ordersVoList) {
        BigDecimal diffAmount;

        //金额计算
        for (int i=0; i<ordersVoList.size(); i++) {
            //在线待支付金额
            if (ordersVoList.get(i).getOrdersState() == OrdersOrdersState.NEW) {
                ordersOnlineAmount = PriceHelper.add(ordersOnlineAmount, ordersVoList.get(i).getOrdersAmount());
                isExistOnlineNoPay = 1;
            }
            //货到付款待支付订单总金额
            if (ordersVoList.get(i).getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE) &&
                    (ordersVoList.get(i).getOrdersState() == OrdersOrdersState.PAY ||
                    ordersVoList.get(i).getOrdersState() == OrdersOrdersState.SEND)) {
                ordersOfflineAmount = PriceHelper.add(ordersOfflineAmount,ordersVoList.get(i).getOrdersAmount());
            }
            //还需要在线支付金额
            if (ordersVoList.get(i).getOrdersState() == OrdersOrdersState.NEW) {
                diffAmount = PriceHelper.sub(ordersVoList.get(i).getOrdersAmount(),ordersVoList.get(i).getPredepositAmount());
                ordersOnlineDiffAmount = PriceHelper.add(ordersOnlineDiffAmount,diffAmount);
            }
            //预存款支付金额
            predepositAmount = PriceHelper.add(predepositAmount,ordersVoList.get(i).getPredepositAmount());

        }
        this.payId = ordersVoList.get(0).getPayId();
        this.paySn = ordersVoList.get(0).getPaySn();
        this.ordersVoList = ordersVoList;
    }

    public List<OrdersVo> getOrdersVoList() {
        return ordersVoList;
    }

    public void setOrdersVoList(List<OrdersVo> ordersVoList) {
        this.ordersVoList = ordersVoList;
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

    public int getIsExistOnlineNoPay() {
        return isExistOnlineNoPay;
    }

    public void setIsExistOnlineNoPay(int isExistOnlineNoPay) {
        this.isExistOnlineNoPay = isExistOnlineNoPay;
    }

    @Override
    public String toString() {
        return "OrdersPayVo{" +
                "ordersVoList=" + ordersVoList +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", ordersOnlineAmount=" + ordersOnlineAmount +
                ", ordersOfflineAmount=" + ordersOfflineAmount +
                ", ordersOnlineDiffAmount=" + ordersOnlineDiffAmount +
                ", predepositAmount=" + predepositAmount +
                ", isExistOnlineNoPay=" + isExistOnlineNoPay +
                '}';
    }
}
