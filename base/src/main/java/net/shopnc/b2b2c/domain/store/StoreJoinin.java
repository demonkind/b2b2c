package net.shopnc.b2b2c.domain.store;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 店铺开店实体
 * Created by dqw on 2015/12/10.
 */
@Entity
@Table(name = "store_joinin")
public class StoreJoinin {
    /**
     * 商家编号
     */
    @Id
    @Column(name = "seller_id")
    private int sellerId;

    /**
     * 商家用户名
     */
    @Column(name = "seller_name")
    private String sellerName;

    /**
     * 申请的店铺名
     */
    @Column(name = "store_name")
    private String storeName;

    /**
     * 申请状态</br>
     * 10-新申请 15-初审失败 20-初审成功 30-缴费完成 35-缴费审核失败 90-审核成功
     */
    @Column(name = "state")
    private int state;

    /**
     * 管理员审核信息
     */
    @Column(name = "joinin_message")
    private String joininMessage = "";

    /**
     * 申请年限
     */
    @Column(name = "joinin_year")
    private int joininYear;

    /**
     * 店铺等级编号
     */
    @Column(name = "grade_id")
    private int gradeId;

    /**
     * 店铺分类编号
     */
    @Column(name = "class_id")
    private int classId;

    /**
     * 付款总额
     */
    @Column(name = "paying_amount")
    private int payingAmount = 0;

    /**
     * 付款凭证
     */
    @Column(name = "paying_certificate")
    private String payingCertificate = "";

    /**
     * 付款凭证说明
     */
    @Column(name = "paying_CertificateExp")
    private String payingCertificateExp = "";

    /**
     * 申请提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "joinin_submit_time")
    private Timestamp joininSubmitTime;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getJoininMessage() {
        return joininMessage;
    }

    public void setJoininMessage(String joininMessage) {
        this.joininMessage = joininMessage;
    }

    public int getJoininYear() {
        return joininYear;
    }

    public void setJoininYear(int joininYear) {
        this.joininYear = joininYear;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getPayingAmount() {
        return payingAmount;
    }

    public void setPayingAmount(int payingAmount) {
        this.payingAmount = payingAmount;
    }

    public String getPayingCertificate() {
        return payingCertificate;
    }

    public void setPayingCertificate(String payingCertificate) {
        this.payingCertificate = payingCertificate;
    }

    public String getPayingCertificateExp() {
        return payingCertificateExp;
    }

    public void setPayingCertificateExp(String payingCertificateExp) {
        this.payingCertificateExp = payingCertificateExp;
    }

    public Timestamp getJoininSubmitTime() {
        return joininSubmitTime;
    }

    public void setJoininSubmitTime(Timestamp joininSubmitTime) {
        this.joininSubmitTime = joininSubmitTime;
    }

    @Override
    public String toString() {
        return "StoreJoinin{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", state=" + state +
                ", joininMessage='" + joininMessage + '\'' +
                ", joininYear=" + joininYear +
                ", gradeId=" + gradeId +
                ", classId=" + classId +
                ", payingAmount=" + payingAmount +
                ", payingCertificate='" + payingCertificate + '\'' +
                ", payingCertificateExp='" + payingCertificateExp + '\'' +
                ", joininSubmitTime=" + joininSubmitTime +
                '}';
    }
}

