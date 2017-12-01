package net.shopnc.b2b2c.vo.refund;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.OrdersPaymentCode;
import net.shopnc.b2b2c.constant.OrdersPaymentName;
import net.shopnc.b2b2c.domain.refund.RefundDetail;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 退款实体
 * Created by cj on 2016/2/1.
 */
public class RefundDetailVo {
    /**
     * 退款id
     */


    private int refundId;

    /**
     * 订单id
     */

    private int ordersId;

    /**
     * 批次号
     */

    private String batchNo;

    /**
     * 退款金额
     */

    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * 在线退款金额
     */

    private BigDecimal payAmount = new BigDecimal(0);

    /**
     * 预存款退款金额
     */

    private BigDecimal pdAmount = new BigDecimal(0);

    /**
     * 充值卡退款金额
     */

    private BigDecimal rcbAmount = new BigDecimal(0);

    /**
     * 退款支付代码
     */
    private String refundCode = "predeposit";

    /**
     * 申请状态:1为处理中,2为已完成
     */
    private int refundState = 1;


    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp addTime;

    /**
     * 在线退款完成时间,默认为0
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp payTime;

    /**
     * 支付名称
     * @param refundDetail
     */
    private String paymentName;

    /**
     * 状态文字
     */
    private String refundStateText;

    public RefundDetailVo(RefundDetail refundDetail) {
        this.refundId = refundDetail.getRefundId();
        this.ordersId = refundDetail.getOrdersId();
        this.batchNo = refundDetail.getBatchNo();
        this.refundAmount = refundDetail.getRefundAmount();
        this.payAmount = refundDetail.getPayAmount();
        this.pdAmount = refundDetail.getPdAmount();
        this.rcbAmount = refundDetail.getRcbAmount();
        this.refundCode = refundDetail.getRefundCode();
        this.refundState = refundDetail.getRefundState();
        this.addTime = refundDetail.getAddTime();
        this.payTime = refundDetail.getPayTime();
        this.paymentName = this.getPaymentNameForSelf(refundDetail.getRefundCode());
        this.refundStateText = refundDetail.getRefundState() == 1 ?"处理中":"已完成";
    }
    private String getPaymentNameForSelf(String paymentCode){
        String paymentName = null;
        if (paymentCode != null) {
            if (paymentCode.equals(OrdersPaymentCode.OFFLINE)) {
                paymentName = OrdersPaymentName.OFFLINE;
            } else if (paymentCode.equals(OrdersPaymentCode.WXPAY)) {
                paymentName = OrdersPaymentName.WXPAY;
            } else if (paymentCode.equals(OrdersPaymentCode.ALIPAY)) {
                paymentName = OrdersPaymentName.ALIPAY;
            } else if (paymentCode.equals(OrdersPaymentCode.PREDEPOSIT)) {
                paymentName = OrdersPaymentName.PREDEPOSIT;
            }
        }
        return paymentName;
    }

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
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

    public String getRefundCode() {
        return refundCode;
    }

    public void setRefundCode(String refundCode) {
        this.refundCode = refundCode;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
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

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getRefundStateText() {
        return refundStateText;
    }

    public void setRefundStateText(String refundStateText) {
        this.refundStateText = refundStateText;
    }

    @Override
    public String toString() {
        return "RefundDetailVo{" +
                "refundId=" + refundId +
                ", ordersId=" + ordersId +
                ", batchNo='" + batchNo + '\'' +
                ", refundAmount=" + refundAmount +
                ", payAmount=" + payAmount +
                ", pdAmount=" + pdAmount +
                ", rcbAmount=" + rcbAmount +
                ", refundCode='" + refundCode + '\'' +
                ", refundState=" + refundState +
                ", addTime=" + addTime +
                ", payTime=" + payTime +
                ", paymentName='" + paymentName + '\'' +
                ", refundStateText='" + refundStateText + '\'' +
                '}';
    }
}
