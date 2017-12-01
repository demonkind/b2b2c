package net.shopnc.b2b2c.vo.statistical;

import net.shopnc.b2b2c.domain.orders.OrdersGoods;

import java.math.BigDecimal;

/**
 * Created by zxy on 2016-02-05
 */
public class StatOrdersGoodsVo {
    /**
     * 订单商品
     */
    private OrdersGoods ordersGoods;
    /**
     * 订单商品总金额
     */
    private BigDecimal ordersGoodsPriceSum;
    /**
     * 订单商品购买量
     */
    private Long ordersGoodsBuyNumSum;
    /**
     * 订单商品一级分类名称
     */
    private String ordersGoodsCategoryName1;
    /**
     * 订单商品创建时间精确到天
     */
    private String createTimeShort;

    public StatOrdersGoodsVo(OrdersGoods ordersGoods, BigDecimal ordersGoodsPriceSum, Long ordersGoodsBuyNumSum) {
        this.ordersGoods = ordersGoods;
        this.ordersGoodsPriceSum = ordersGoodsPriceSum;
        this.ordersGoodsBuyNumSum = ordersGoodsBuyNumSum;
    }

    public StatOrdersGoodsVo(OrdersGoods ordersGoods, BigDecimal ordersGoodsPriceSum, Long ordersGoodsBuyNumSum, String createTimeShort) {
        this.ordersGoods = ordersGoods;
        this.ordersGoodsPriceSum = ordersGoodsPriceSum;
        this.ordersGoodsBuyNumSum = ordersGoodsBuyNumSum;
        this.createTimeShort = createTimeShort;
    }

    public OrdersGoods getOrdersGoods() {
        return ordersGoods;
    }

    public void setOrdersGoods(OrdersGoods ordersGoods) {
        this.ordersGoods = ordersGoods;
    }

    public BigDecimal getOrdersGoodsPriceSum() {
        return ordersGoodsPriceSum;
    }

    public void setOrdersGoodsPriceSum(BigDecimal ordersGoodsPriceSum) {
        this.ordersGoodsPriceSum = ordersGoodsPriceSum;
    }

    public Long getOrdersGoodsBuyNumSum() {
        return ordersGoodsBuyNumSum;
    }

    public void setOrdersGoodsBuyNumSum(Long ordersGoodsBuyNumSum) {
        this.ordersGoodsBuyNumSum = ordersGoodsBuyNumSum;
    }

    public String getOrdersGoodsCategoryName1() {
        return ordersGoodsCategoryName1;
    }

    public void setOrdersGoodsCategoryName1(String ordersGoodsCategoryName1) {
        this.ordersGoodsCategoryName1 = ordersGoodsCategoryName1;
    }

    public String getCreateTimeShort() {
        return createTimeShort;
    }

    public void setCreateTimeShort(String createTimeShort) {
        this.createTimeShort = createTimeShort;
    }
}
