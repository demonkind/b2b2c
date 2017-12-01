package net.shopnc.b2b2c.constant;

/**
 * Created by hbj on 2016/1/27.
 */
public class OrdersOrdersStateName {
    /**
     * 订单被取消
     */
    public static final String CANCEL = "已取消";
    /**
     * 生成新订单
     */
    public static final String NEW = "待付款";
    /**
     * 已支付
     */
    public static final String PAY = "待发货";
    /**
     * 已发货
     */
    public static final String SEND = "已发货";
    /**
     * 已收货，订单完成
     */
    public static final String FINISH = "交易完成";
}
