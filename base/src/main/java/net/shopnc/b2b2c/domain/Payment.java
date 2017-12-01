package net.shopnc.b2b2c.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 支付方式实体</br>
 * Created by shopnc on 2015/10/21.
 */
@Entity
@Table(name="payment")
public class Payment implements Serializable {
    /**
     * 支付代码
     */
    @Id
    @Column(name = "payment_code")
    private String paymentCode;
    /**
     * 支付名称
     */
    @Column(name = "payment_name")
    private String paymentName = "";
    /**
     * 支付配置信息
     */
    @Column(name = "payment_info")
    private String paymentInfo = "";
    /**
     * 支付开关</br>
     * 0-关闭 1-开启
     */
    @Column(name = "payment_state")
    private Integer paymentState = 0;

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

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public Integer getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(Integer paymentState) {
        this.paymentState = paymentState;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentCode='" + paymentCode + '\'' +
                ", paymentName='" + paymentName + '\'' +
                ", paymentInfo='" + paymentInfo + '\'' +
                ", paymentState=" + paymentState +
                '}';
    }
}
