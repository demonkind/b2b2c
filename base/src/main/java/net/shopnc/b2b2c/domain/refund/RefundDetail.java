package net.shopnc.b2b2c.domain.refund;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 退款实体
 * Created by cj on 2016/2/1.
 */

@Entity
@Table(name = "refund_detail")
public class RefundDetail implements Serializable {
    /**
     * 退款单id
     */
    @Id
    @Column(name = "refund_id")
    private int refundId;

    /**
     * 订单id
     */
    @Column(name = "orders_id")
    private int ordersId;

    /**
     * 批次号
     */
    @Column(name = "batch_no")
    private String batchNo;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount")
    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * 在线退款金额
     */
    @Column(name = "pay_amount")
    private BigDecimal payAmount = new BigDecimal(0);

    /**
     * 预存款退款金额
     */
    @Column(name = "pd_amount")
    private BigDecimal pdAmount = new BigDecimal(0);

    /**
     * 充值卡退款金额
     */
    @Column(name = "rcb_amount")
    private BigDecimal rcbAmount = new BigDecimal(0);

    /**
     * 退款支付代码
     */
    @Column(name = "refund_code")
    private String refundCode = "predeposit";

    /**
     * 申请状态:1为处理中,2为已完成
     */
    @Column(name = "refund_state")
    private int refundState = 1;


    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "add_time")
    private Timestamp addTime;

    /**
     * 在线退款完成时间,默认为0
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "pay_time")
    private Timestamp payTime;

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getPdAmount() {
        return pdAmount;
    }

    public void setPdAmount(BigDecimal pdAmount) {
        this.pdAmount = pdAmount;
    }

    public BigDecimal getRcbAmount() {
        return rcbAmount;
    }

    public void setRcbAmount(BigDecimal rcbAmount) {
        this.rcbAmount = rcbAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundCode() {
        return refundCode;
    }

    public void setRefundCode(String refundCode) {
        this.refundCode = refundCode;
    }


    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    @Override
    public String toString() {
        return "RefundDetail{" +
                "addTime=" + addTime +
                ", refundId=" + refundId +
                ", ordersId=" + ordersId +
                ", batchNo='" + batchNo + '\'' +
                ", refundAmount=" + refundAmount +
                ", payAmount=" + payAmount +
                ", pdAmount=" + pdAmount +
                ", rcbAmount=" + rcbAmount +
                ", refundCode='" + refundCode + '\'' +
                ", refundState=" + refundState +
                ", payTime=" + payTime +
                '}';
    }
}
