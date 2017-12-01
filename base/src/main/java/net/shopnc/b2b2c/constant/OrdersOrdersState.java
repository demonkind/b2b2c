package net.shopnc.b2b2c.constant;

/**
 * 订单状态<br/>
 * Created by shopnc on 2015/10/22.
 */
public class OrdersOrdersState {
    /**
     * 订单被取消
     */
    public static final int CANCEL = 0;
    /**
     * 生成新订单
     */
    public static final int NEW = 10;
    /**
     * 已支付
     */
    public static final int PAY = 20;
    /**
     * 已发货
     */
    public static final int SEND = 30;
    /**
     * 已收货，订单完成
     */
    public static final int FINISH = 40;
    
    /**
     * 订单被取消
     */
    public static final String CANCEL_STR = "cancel";
    /**
     * 生成新订单
     */
    public static final String NEW_STR = "new";
    /**
     * 已支付
     */
    public static final String PAY_STR = "pay";
    /**
     * 已发货
     */
    public static final String SEND_STR = "send";
    /**
     * 已收货，订单完成
     */
    public static final String FINISH_STR = "finish";
}
