package net.shopnc.b2b2c.vo.predeposit;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by zxy on 2015/12/30.
 */
public class PredepositCashMemberVo {
    /**
     * 自增编码
     */
    private int cashId;
    /**
     * 提现编号
     */
    private String cashSn;
    /**
     * 会员编号
     */
    private int memberId;
    /**
     * 提现金额
     */
    private BigDecimal amount;
    /**
     * 收款公司比如支付宝、建行等
     */
    private String receiveCompany;
    /**
     * 收款账号
     */
    private String receiveAccount;
    /**
     * 开户人姓名
     */
    private String receiveUser;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp addTime;
    /**
     * 付款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp payTime;
    /**
     * 状态（PredepositCashState状态）
     */
    private int state;
    /**
     * 操作审核的管理员编号
     */
    private int adminId;
    /**
     * 操作审核的管理员
     */
    private String adminName;
    /**
     * 状态文本
     */
    private String stateText;
    /**
     * 会员名称
     */
    private String memberName;

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

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
