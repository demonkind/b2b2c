package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.PredepositRechargePayState;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 预存款充值<br>
 * Created by zxy on 2015-12-24
 */
@Entity
@Table(name = "predeposit_recharge")
public class PredepositRecharge implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="recharge_id")
    private int rechargeId;
    /**
     * 充值编号
     */
    @Column(name="recharge_sn", length = 100)
    private String rechargeSn = "";
    /**
     * 会员编号
     */
    @Column(name="member_id")
    private int memberId = 0;
    /**
     * 充值金额
     */
    @Column(name="amount")
    private BigDecimal amount = new BigDecimal(0);
    /**
     * 支付方式标识
     */
    @Column(name="payment_code", length = 50)
    private String paymentCode = "";
    /**
     * 支付方式名称
     */
    @Column(name = "payment_name")
    private String paymentName = "";
    /**
     * 第三方支付接口交易号
     */
    @Column(name="trade_sn", length = 100)
    private String tradeSn = "";
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 支付状态（PredepositRechargePayState状态）
     */
    @Column(name="pay_state")
    private int payState = 0;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="pay_time")
    private Timestamp payTime;
    /**
     * 操作审核的管理员编号
     */
    @Column(name="admin_id")
    private int adminId = 0;
    /**
     * 操作审核的管理员
     */
    @Column(name="admin_name")
    private String adminName = "";
    /**
     * 支付状态文本
     */
    @Transient
    private String payStateText = "";

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
        if (payState == PredepositRechargePayState.PAID) {
            payStateText = "已支付";
        }else {
            payStateText = "未支付";
        }
        return payStateText;
    }
}
