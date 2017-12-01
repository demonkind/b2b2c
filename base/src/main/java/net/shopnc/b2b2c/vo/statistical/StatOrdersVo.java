package net.shopnc.b2b2c.vo.statistical;

import net.shopnc.b2b2c.domain.orders.Orders;

import java.math.BigDecimal;

/**
 * Created by zxy on 2016-02-03
 */
public class StatOrdersVo {
    /**
     * 订单
     */
    private Orders orders;
    /**
     * 订单总金额
     */
    private BigDecimal ordersAmountSum;
    /**
     * 订单总数
     */
    private Long ordersCount;
    /**
     * 订单创建时间的小时
     */
    private int createTimeHour;

    public StatOrdersVo(Orders orders, BigDecimal ordersAmountSum, Long ordersCount, int createTimeHour) {
        this.orders = orders;
        this.ordersAmountSum = ordersAmountSum;
        this.ordersCount = ordersCount;
        this.createTimeHour = createTimeHour;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public BigDecimal getOrdersAmountSum() {
        return ordersAmountSum;
    }

    public void setOrdersAmountSum(BigDecimal ordersAmountSum) {
        this.ordersAmountSum = ordersAmountSum;
    }

    public Long getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(Long ordersCount) {
        this.ordersCount = ordersCount;
    }

    public int getCreateTimeHour() {
        return createTimeHour;
    }

    public void setCreateTimeHour(int createTimeHour) {
        this.createTimeHour = createTimeHour;
    }
}
