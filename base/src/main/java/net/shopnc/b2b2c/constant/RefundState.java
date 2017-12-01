package net.shopnc.b2b2c.constant;

/**
 * 退款状态
 * Created by cj on 2015/12/17.
 */
public class RefundState {


    public final static int REFUND_NONE = 0;
    /**
     * 无退款
     */
    public final static int NONE = 0;
    /**
     * 1 是部分退款
     */
    public final static int PART = 1;
    /**
     * 2 是全部退款
     */
    public final static int ALL = 2;
    /**
     * 卖家待审核
     */
    public final static int SELLER_STATE_WAITING = 1;
    /**
     * 卖家同意
     */
    public final static int SELLER_STATE_AGREE = 2;
    /**
     * 卖家不同意
     */
    public final static int SELLER_STATE_DISAGREE = 3;

    /**
     * 订单状态锁定
     */
    public final static int ORDER_LOCK = 2;
    /**
     * 订单状态不锁定
     */
    public final static int ORDER_UNLOCK = 1;

    /**
     * 订单全部付款的时候
     */
    public final static String REFUND_ALL_REASON_INFO = "取消订单，全部退款";
    public final static String REFUND_ALL_GOODS_NAME = "订单商品全部退款";
    /**
     * 退款类型，退款
     */
    public final static int REFUND_TYPE_REFUND = 1;
    /**
     * 退款类型，退货
     */
    public final static int REFUND_TYPE_RETURN = 2;

    /**
     * 退款单状态，待审核
     */
    public final static int REFUND_STATE_WAITING = 1;
    /**
     * 退款单状态，等待管理员审核
     */
    public final static int REFUND_STATE_CHECK = 2;
    /**
     * 退款单状态，完成
     */
    public final static int REFUND_STATE_FINISH = 3;

    /**
     * 退款想请表中的退款状态
     * 1为处理中
     */
    public final static int REFUND_DETAIL_WAITING = 1;
    /**
     * 退款想请表中的退款状态
     * 2为已完成
     */
    public final static int REFUND_DETAIL_FINISH = 2;

    /**
     * 退货类型:1为不用退货,2为需要退货,默认为1
     */
    public final static int RETURN_TYPE_RETURNED_NO = 1;
    /**
     * 退货类型:1为不用退货,2为需要退货,默认为1
     */
    public final static int RETURN_TYPE_RETURNED_YES = 2;

    /**
     * 退款物流状态:1为待发货,2为待收货,3为未收到,4为已收货,默认为1
     */
    public final static int RETURN_SHIP_STATE_WAITING = 1;
    /**
     * 退款物流状态:1为待发货,2为待收货,3为未收到,4为已收货,默认为1
     */
    public final static int RETURN_SHIP_STATE_SEND = 2;
    /**
     * 退款物流状态:1为待发货,2为待收货,3为未收到,4为已收货,默认为1
     */
    public final static int RETURN_SHIP_STATE_UNRECEIVED = 3;
    /**
     * 退款物流状态:1为待发货,2为待收货,3为未收到,4为已收货,默认为1
     */
    public final static int RETURN_SHIP_STATE_FINISH = 4;

    /**
     * 确认收货完成的文字
     */
    public final static String RECEIVE_MESSAGE ="确认收货完成";
    
    
    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_0 = 0;
    
    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_REFUNDING = 11;
    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝, 21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_REFUND_END = 18;

    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_REFUND_REFUSE = 19;
    

    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_RETURNING = 21;

    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_RETURN_AGREE = 22;
    
    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_RETURN_SEND = 23;
    
    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_RETURN_END = 24;
    
    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_RETURN_NO_GOOD = 25;
    
    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    public final static int REFUND_RETURN_STATUS_RETURN_REFUSE = 29;


}
