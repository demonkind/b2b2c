package net.shopnc.b2b2c.constant;

/**
 * Created by hbj on 2016/6/2.
 */
public class BillStateName {
    /**
     * 生成账单，商家未确认
     */
    public static final String NEW = "账单已生成，等待商家确认";
    /**
     * 商家已确认，待平台确认
     */
    public static final String CONFIRM = "商家已确认，等待平台审核";
    /**
     * 平台已确认，未付款
     */
    public static final String ACCESS = "平台已审核，等待平台付款";
    /**
     * 平台已向商家付款，结算流程完成
     */
    public static final String PAY = "平台已付款，平台结算完成";
}
