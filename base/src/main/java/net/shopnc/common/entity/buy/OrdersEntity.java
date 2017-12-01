package net.shopnc.common.entity.buy;

import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.b2b2c.domain.orders.OrdersPay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 存放已经生成的订单数据，模板显示也使用这个VO<br/>
 * Created by hbj on 2015/12/17.
 */
public class OrdersEntity {
    private OrdersPay ordersPay;
    private List<Orders> ordersList;
    private HashMap<Integer,List<OrdersGoods>> ordersGoodsList = new HashMap<Integer, List<OrdersGoods>>();
    private BigDecimal ordersAmount;
    private BigDecimal ordersOnlineAmount;
    private BigDecimal ordersOfflineAmount;


    public OrdersPay getOrdersPay() {
        return ordersPay;
    }

    public void setOrdersPay(OrdersPay ordersPay) {
        this.ordersPay = ordersPay;
    }

    /**
     * ************************************************************************
     * Geter Seter toString 块
     * ************************************************************************
     */

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public HashMap<Integer, List<OrdersGoods>> getOrdersGoodsList() {
        return ordersGoodsList;
    }

    public void setOrdersGoodsList(HashMap<Integer, List<OrdersGoods>> ordersGoodsList) {
        this.ordersGoodsList = ordersGoodsList;
    }

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
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


    /**
     * ************************************************************************
     * 自定义类内使用函数 块
     * ************************************************************************
     */
    public void ncAddOrdersGoodsList(int orderId,List<OrdersGoods> ordersGoodsList) {
        this.ordersGoodsList.put(orderId,ordersGoodsList);
    }
}
