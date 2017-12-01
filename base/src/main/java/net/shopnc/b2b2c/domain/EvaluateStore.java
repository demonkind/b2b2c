package net.shopnc.b2b2c.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 店铺评价<br>
 * Created by zxy on 2015-10-26
 */
@Entity
@Table(name = "evaluate_store")
public class EvaluateStore implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name = "evaluate_id")
    private int evaluateId;
    /**
     * 订单编码
     */
    @Column(name = "order_id")
    private int orderId;
    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private int storeId;
    /**
     * 评价时间
     */
    @Column(name = "evaluate_time")
    private Timestamp evaluateTime;
    /**
     * 评价人编号
     */
    @Column(name = "from_member_id")
    private int fromMemberId;
    /**
     * 描述相符评分
     */
    @Min(1)
    @Max(5)
    @Column(name = "description_credit")
    private int descriptionCredit;
    /**
     * 服务态度评分
     */
    @Min(1)
    @Max(5)
    @Column(name = "service_credit")
    private int serviceCredit;
    /**
     * 发货速度评分
     */
    @Min(1)
    @Max(5)
    @Column(name = "delivery_credit")
    private int deliveryCredit;

    public int getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Timestamp evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public int getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(int fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public int getDescriptionCredit() {
        return descriptionCredit;
    }

    public void setDescriptionCredit(int descriptionCredit) {
        this.descriptionCredit = descriptionCredit;
    }

    public int getServiceCredit() {
        return serviceCredit;
    }

    public void setServiceCredit(int serviceCredit) {
        this.serviceCredit = serviceCredit;
    }

    public int getDeliveryCredit() {
        return deliveryCredit;
    }

    public void setDeliveryCredit(int deliveryCredit) {
        this.deliveryCredit = deliveryCredit;
    }
}
