package net.shopnc.b2b2c.constant;

/**
 * 预存款日志操作阶段
 * Created by zxy on 2015-12-24.
 */
public class PredepositLogOperationStage {
    /**
     * 充值成功，增加预存款
     */
    public static final String RECHARGE = "recharge";
    /**
     * 申请提现，冻结预存款
     */
    public static final String CASHAPPLY = "cashApply";
    /**
     * 提现成功，减少冻结预存款
     */
    public static final String CASHPAY = "cashPay";
    /**
     * 取消提现申请，解冻预存款
     */
    public static final String CASHDEL = "cashDel";
    /**
     * 拒绝提现申请，解冻预存款
     */
    public static final String CASHREFUSE = "cashRefuse";
    /**
     * 下单支付预存款
     */
    public static final String ORDERPAY = "orderPay";
    /**
     * 下单冻结预存款
     */
    public static final String ORDERFREEZE = "orderFreeze";
    /**
     * 取消订单解冻预存款
     */
    public static final String ORDERCANCEL = "orderCancel";
    /**
     * 订单完成减少被冻结的预存款
     */
    public static final String ORDERCOMPLETEPAY = "orderCompletePay";
    /**
     * 退款
     */
    public static final String REFUND = "refund";
}