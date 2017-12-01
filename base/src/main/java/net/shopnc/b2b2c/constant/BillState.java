package net.shopnc.b2b2c.constant;

/**
 * Created by shopnc on 2015/10/22.
 */
public class BillState {
    /**
     * 生成账单，商家未确认
     */
    public static final int NEW = 10;
    /**
     * 商家已确认，待平台确认
     */
    public static final int CONFIRM = 20;
    /**
     * 平台已确认，未付款
     */
    public static final int ACCESS = 30;
    /**
     * 平台已向商家付款，结算流程完成
     */
    public static final int PAY = 40;
}
