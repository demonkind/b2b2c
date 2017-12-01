package net.shopnc.b2b2c.domain.orders;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.BillState;
import net.shopnc.b2b2c.constant.BillStateName;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.common.util.ShopHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


/**
 * 结算实体</br>
 * Created by shopnc on 2015/10/21.
 */
@Entity
@Table(name="bill")
public class Bill implements Serializable {
    /**
     * 结算单号</br>
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "bill_id")
    private int billId;
    /**
     * 结算单号
     */
    @Column(name = "bill_sn")
    private int billSn;
    /**
     * (结算周期)开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @Column(name = "state_time")
    private Timestamp startTime;
    /**
     * (结算周期)截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @Column(name = "end_time")
    private Timestamp endTime;
    /**
     * 订单金额
     */
    @Column(name = "orders_amount")
    private BigDecimal ordersAmount = new BigDecimal(0);
    /**
     * 运费金额(仅页面显示时备用，结算时该字段不参与结算，因为订单金额里已经包含运费了)
     */
    @Column(name = "freight_amount")
    private BigDecimal freightAmount = new BigDecimal(0);
    /**
     * 平台收取佣金金额
     */
    @Column(name = "commission_amount")
    private BigDecimal commissionAmount = new BigDecimal(0);
    /**
     * 退款金额
     */
    @Column(name = "refund_amount")
    private BigDecimal refundAmount = new BigDecimal(0);
    /**
     * 退还佣金金额
     */
    @Column(name = "refund_commission_amount")
    private BigDecimal refundCommissionAmount = new BigDecimal(0);
    /**
     * 平台应(与商家)结算金额
     */
    @Column(name = "bill_amount")
    private BigDecimal billAmount = new BigDecimal(0);
    /**
     * 出账时间(生成结算单的时间)
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @Column(name = "create_time")
    private Timestamp createTime = ShopHelper.getCurrentTimestamp();
    /**
     * 账单状态</br>
     * 10默认(已出账单)、20店家已确认、30平台已审核、40结算完成
     */
    @Column(name = "bill_state")
    private int billState = BillState.NEW;
    /**
     * (平台给商家)付款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "payment_time")
    private Timestamp paymentTime;
    /**
     * (平台给商家)付款备注
     */
    @Column(name = "payment_note")
    private String paymentNote;
    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private int storeId;
    /**
     * 店铺名称
     */
    @Column(name = "store_name")
    private String storeName;


    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getBillSn() {
        return billSn;
    }

    public void setBillSn(int billSn) {
        this.billSn = billSn;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getRefundCommissionAmount() {
        return refundCommissionAmount;
    }

    public void setRefundCommissionAmount(BigDecimal refundCommissionAmount) {
        this.refundCommissionAmount = refundCommissionAmount;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getBillState() {
        return billState;
    }

    public void setBillState(int billState) {
        this.billState = billState;
    }

    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * 自定义函数，返回结算状态标志
     * @return
     */
    public int getBillStateNew() {
        return billState == BillState.NEW ? State.YES : State.NO;
    }
    public int getBillStateConfirm() {
        return billState == BillState.CONFIRM ? State.YES : State.NO;
    }
    public int getBillStateAccess() {
        return billState == BillState.ACCESS ? State.YES : State.NO;
    }
    public int getBillStatePay() {
        return billState == BillState.PAY ? State.YES : State.NO;
    }

    public String getStartDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(startTime);
    }

    public String getEndDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(endTime);
    }

    public String getCreateDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(createTime);
    }

    public String getBillStateName() {
        if (billState == BillState.NEW) {
            return BillStateName.NEW;
        } else if (billState == BillState.CONFIRM) {
            return BillStateName.CONFIRM;
        } else if (billState == BillState.ACCESS) {
            return BillStateName.ACCESS;
        } else if (billState == BillState.PAY) {
            return BillStateName.PAY;
        }
        return "";
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", billSn=" + billSn +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", ordersAmount=" + ordersAmount +
                ", freightAmount=" + freightAmount +
                ", commissionAmount=" + commissionAmount +
                ", refundAmount=" + refundAmount +
                ", refundCommissionAmount=" + refundCommissionAmount +
                ", billAmount=" + billAmount +
                ", createTime=" + createTime +
                ", billState=" + billState +
                ", paymentTime=" + paymentTime +
                ", paymentNote='" + paymentNote + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
