package net.shopnc.b2b2c.vo.predeposit;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by zxy on 2015/12/28.
 */
public class PredepositRechargeMemberVo {
    /**
     * 自增编码
     */
    private int rechargeId;
    /**
     * 充值编号
     */
    private String rechargeSn;
    /**
     * 会员编号
     */
    private int memberId;
    /**
     * 充值金额
     */
    private BigDecimal amount;
    /**
     * 支付方式标识
     */
    private String paymentCode;
    /**
     * 支付方式名称
     */
    private String paymentName;
    /**
     * 第三方支付接口交易号
     */
    private String tradeSn;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp addTime;
    /**
     * 支付状态（PredepositRechargePayState状态）
     */
    private int payState;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp payTime;
    /**
     * 操作审核的管理员编号
     */
    private int adminId;
    /**
     * 操作审核的管理员
     */
    private String adminName;
    /**
     * 支付状态文本
     */
    private String payStateText;
    /**
     * 会员名称
     */
    private String memberName;

    public int getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(int rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getRechargeSn() {
        return rechargeSn;
    }

    public void setRechargeSn(String rechargeSn) {
        this.rechargeSn = rechargeSn;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getTradeSn() {
        return tradeSn;
    }

    public void setTradeSn(String tradeSn) {
        this.tradeSn = tradeSn;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPayStateText() {
        return payStateText;
    }

    public void setPayStateText(String payStateText) {
        this.payStateText = payStateText;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
