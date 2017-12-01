package net.shopnc.b2b2c.constant;

/**
 * Created by hbj on 2016/1/4.
 */
public class OrdersOperationType {
    public final static int MEMBER_CANCEL = 100;
    public final static int MEMBER_PAY = 101;
    public final static int MEMBER_RECEIVE = 102;
    public final static int MEMBER_EVALUATION = 103;
    public final static int MEMBER_EVALUATION_APPEND = 104;
    public final static int MEMBER_DELAY_RECEIVE = 105;
    // bycj [ 买家订单退款，整单 ]
    public final static int MEMBER_REFUND_ALL = 105;

    public final static int STORE_CANCEL = 200;
    public final static int STORE_MODIFY_FREIGHT = 201;
    public final static int STORE_SEND = 202;
    public final static int STORE_SEND_MODIFY = 203;
    public final static int STORE_MODIFY_RECEIVER = 204;

    public final static int ADMIN_CANCEL = 300;
    public final static int ADMIN_PAY = 301;

    public final static int AUTO_CANCEL = 400;
    public final static int AUTO_FINISH = 401;
    public final static int SHIP_SEARCH = 403;

}
