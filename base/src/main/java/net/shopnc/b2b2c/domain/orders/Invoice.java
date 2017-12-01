package net.shopnc.b2b2c.domain.orders;

import net.shopnc.b2b2c.constant.InvoiceInvoiceType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 发票实体</br>
 * Created by shopnc on 2015/10/21.
 */
@Entity
@Table(name="invoice")
public class Invoice implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue
    @Column(name = "invoice_id")
    private Integer invoiceId;
    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private Integer memberId;
    /**
     * 发票类型1普通发票2增值税发票
     */
    @Column(name = "invoice_type")
    private short invoiceType = InvoiceInvoiceType.NORMAL;
    /**
     * 发票抬头[普通发票]
     */
    @Column(name = "title")
    private String title;
    /**
     * 发票内容[普通发票]
     */
    @Column(name = "content")
    private String content;
    /**
     * 单位名称
     */
    @Column(name = "company")
    private String company;
    /**
     * 纳税人识别号
     */
    @Column(name = "code_sn")
    private String codeSn;
    /**
     * 注册地址
     */
    @Column(name = "register_address")
    private String registerAddress;
    /**
     * 注册电话
     */
    @Column(name = "register_phone")
    private String registerPhone;
    /**
     * 开户银行
     */
    @Column(name = "bank_name")
    private String bankName;
    /**
     * 银行帐户
     */
    @Column(name = "bank_accunt")
    private String bankAccount;
    /**
     * 收票人姓名
     */
    @Column(name = "receiver_name")
    private String receiverName;
    /**
     * 收票人手机号
     */
    @Column(name = "receiver_phone")
    private String receiverPhone;
    /**
     * 收票人省市县
     */
    @Column(name = "receiver_area")
    private String receiverArea;
    /**
     * 收票人街道
     */
    @Column(name = "receiver_address")
    private String receiverAddress;

    /**
     * 输票发出内容
     */
    @Transient
    private String invoiceContent;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public short getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(short invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCodeSn() {
        return codeSn;
    }

    public void setCodeSn(String codeSn) {
        this.codeSn = codeSn;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverArea() {
        return receiverArea;
    }

    public void setReceiverArea(String receiverArea) {
        this.receiverArea = receiverArea;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getInvoiceContent() {
        if (invoiceType == InvoiceInvoiceType.NORMAL) {
            return String.format("普通发票 %s %s",title,content);
        } else {
            return String.format("增值税发票 %s %s",company,codeSn,registerAddress);
        }
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", memberId=" + memberId +
                ", invoiceType=" + invoiceType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", company='" + company + '\'' +
                ", codeSn='" + codeSn + '\'' +
                ", registerAddress='" + registerAddress + '\'' +
                ", registerPhone='" + registerPhone + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverArea='" + receiverArea + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", invoiceContent='" + invoiceContent + '\'' +
                '}';
    }
}
