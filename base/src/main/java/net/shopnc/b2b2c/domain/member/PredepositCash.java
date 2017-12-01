package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.PredepositCashState;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 预存款提现<br>
 * Created by zxy on 2015-12-24
 */
@Entity
@Table(name = "predeposit_cash")
public class PredepositCash implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="cash_id")
    private int cashId;
    /**
     * 提现编号
     */
    @NotNull
    @Column(name="cash_sn", length = 100)
    private String cashSn = "";
    /**
     * 会员编号
     */
    @NotNull
    @Column(name="member_id")
    private int memberId = 0;
    /**
     * 提现金额
     */
    @NotNull
    @DecimalMin("0.01")
    @Column(name="amount")
    private BigDecimal amount = new BigDecimal(0);
    /**
     * 收款公司比如支付宝、建行等
     */
    @NotBlank
    @Size(max = 100, message = "收款方式长度应小于100个字")
    @Column(name="receive_company", length = 100)
    private String receiveCompany = "";
    /**
     * 收款账号
     */
    @NotBlank
    @Size(max = 100, message = "收款账号长度应小于100个字")
    @Column(name="receive_account", length = 100)
    private String receiveAccount = "";
    /**
     * 开户人姓名
     */
    @NotBlank
    @Size(max = 100, message = "开户名长度应小于100个字")
    @Column(name="receive_user", length = 100)
    private String receiveUser = "";
    /**
     * 添加时间
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 付款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="pay_time")
    private Timestamp payTime;
    /**
     * 状态（PredepositCashState状态）
     */
    @NotNull
    @Column(name="state")
    private int state = 0;
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
     * 拒绝提现理由
     */
    @Size(max = 1000, message = "拒绝理由长度应小于1000个字")
    @Column(name="refuse_reason", length = 1000)
    private String refuseReason = "";
    /**
     * 状态文本
     */
    @Transient
    private String stateText = "";

    public int getCashId() {
        return cashId;
    }

    public void setCashId(int cashId) {
        this.cashId = cashId;
    }

    public String getCashSn() {
        return cashSn;
    }

    public void setCashSn(String cashSn) {
        this.cashSn = cashSn;
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

    public String getReceiveCompany() {
        return receiveCompany;
    }

    public void setReceiveCompany(String receiveCompany) {
        this.receiveCompany = receiveCompany;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getStateText() {
        if (state == PredepositCashState.FAIL) {
            stateText = "提现失败";
        }else if(state == PredepositCashState.SUCCESS) {
            stateText = "提现成功";
        }else{
            stateText = "未处理";
        }
        return stateText;
    }
}