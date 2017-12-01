package net.shopnc.b2b2c.domain.orders;


import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单实体
 * Created by shopnc on 2015/10/21.
 */
@Entity
@Table(name = "orders")
public class Orders implements Serializable {
    /**
     * 订单主键ID、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "orders_id")
    private int ordersId;
    /**
     * 订单编号
     */
    @Column(name = "orders_sn")
    private long ordersSn;
    /**
     * 订单状态
     * 0-已取消 10-未支付 20-已支付 30-已发货 40-已收货 50-已评价
     */
    @Column(name = "orders_state")
    private int ordersState;
    /**
     * 订单来源
     */
    @Column(name = "orders_from")
    private String ordersFrom;
    /**
     * 支付单ID
     */
    @Column(name = "pay_id")
    private int payId;
    /**
     * 支付单号
     */
    @Column(name = "pay_sn")
    private long paySn;
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
    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private int memberId;
    /**
     * 会员名称
     */
    @Column(name = "member_name")
    private String memberName;
    /**
     * 一级地区ID
     */
    @Column(name = "receiver_area_id_1")
    private int receiverAreaId1;
    /**
     * 二级地区ID
     */
    @Column(name = "receiver_area_id_2")
    private int receiverAreaId2;
    /**
     * 三级地区Id
     */
    @Column(name = "receiver_area_id_3")
    private int receiverAreaId3;
    /**
     * 四级地区
     */
    @Column(name = "receiver_area_id_4")
    private int receiverAreaId4;
    /**
     * 省市县(区)内容
     */
    @Size(min = 1)
    @Column(name = "receiver_area_info")
    private String receiverAreaInfo;
    /**
     * 收货人地址
     */
    @Column(name = "reciver_address")
    private String receiverAddress;
    /**
     * 收货人电话
     */
    @Column(name = "receiver_phone")
    private String receiverPhone;
    /**
     * 收货人姓名
     */
    @Column(name = "receiver_name")
    private String receiverName;
    /**
     * 买家留言
     */
    @Column(name = "receiver_message")
    private String receiverMessage;
    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "create_time")
    private Timestamp createTime;
    /**
     * 线上线下支付代码online/offline
     */
    @Column(name = "payment_type_code")
    private String paymentTypeCode;
    /**
     * 支付方式代码
     */
    @Column(name = "payment_code")
    private String paymentCode;
    /**
     * 外部交易号
     */
    @Column(name = "out_orders_sn")
    private String outOrdersSn;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "payment_time")
    private Timestamp paymentTime;
    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "send_time")
    private Timestamp sendTime;
    /**
     * 订单完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "finish_time")
    private Timestamp finishTime;
    /**
     * 订单金额
     */
    @Column(name = "orders_amount")
    private BigDecimal ordersAmount = new BigDecimal(0);
    /**
     * 预存款支付金额
     */
    @Column(name = "predeposit_amount")
    private BigDecimal predepositAmount = new BigDecimal(0);
    /**
     * 运费
     */
    @Column(name = "freight_amount")
    private BigDecimal freightAmount = new BigDecimal(0);
    /**
     * 评价状态
     * 0-未评价 1-已评价
     */
    @Column(name = "evaluation_state")
    private int evaluationState = OrdersEvaluationState.NO;
    /**
     * 追加评价状态
     * 0-未评价 1-已评价
     */
    @Column(name = "evaluation_append_state")
    private int evaluationAppendState = OrdersEvaluationState.NO;
    /**
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "evaluation_time")
    private Timestamp evaluationTime;
    /**
     * 发货单号
     */
    @Column(name = "ship_sn")
    private String shipSn;
    /**
     * 快递公司
     */
    @Column(name = "ship_name")
    private String shipName;
    /**
     * 快递公司编码
     */
    @Column(name = "ship_code")
    private String shipCode;
    /**
     * 发货备注
     */
    private String shipNote;
    /**
     * 自动收货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "auto_receive_time")
    private Timestamp autoReceiveTime;
    /**
     * 发票内容
     */
    @Column(name = "invoice_info")
    private String invoiceInfo;
    /**
     * 订单类型
     */
    @Column(name = "orders_type")
    private int ordersType;
    /**
     * 订单取消原因
     */
    @Column(name = "cancel_reason")
    private Integer cancelReason;
    /**
     * 关闭时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "cancel_time")
    private Timestamp cancelTime;
    /**
     * 订单关闭操作主体
     */
    @Column(name = "cancel_role")
    private String cancelRole;
    /**
     * 订单收取佣金
     */
    @Column(name = "commission_amount")
    private BigDecimal commissionAmount = new BigDecimal(0);

    /**
     * bycj [ 退款金额 ]
     */
    @Column(name = "refund_amount")
    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * bycj [ 退款状态:0是无退款,1是部分退款,2是全部退款 ]
     */
    @Column(name = "refund_state")
    private int refundState = RefundState.NONE;

    /**
     *  bycj [ 订单锁定 ]
     *  0:是正常 ， 1：是锁定
     */
    @Column(name = "lock_state")
    private int lockState = 0;

    /**
     * 是否进行过延迟收货操作，买家只能点击一次 0-否/1-是
     */
    @Column(name = "delay_receive_state")
    private int delayReceiveState = State.NO;

    /**
     * 是否是管理员点击的收款
     */
    @Column(name = "admin_receive_pay_state")
    private int adminReceivePayState = State.NO;

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public long getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(long ordersSn) {
        this.ordersSn = ordersSn;
    }

    public int getOrdersState() {
        return ordersState;
    }

    public void setOrdersState(int ordersState) {
        this.ordersState = ordersState;
    }

    /**
     * 订单状态
     *
     * @return
     */
    public String getOrdersStateName() {
        String ordersStateName = "";
        switch (ordersState) {
            case OrdersOrdersState.CANCEL:
                ordersStateName = OrdersOrdersStateName.CANCEL;
                break;
            case OrdersOrdersState.NEW:
                ordersStateName = OrdersOrdersStateName.NEW;
                break;
            case OrdersOrdersState.PAY:
                ordersStateName = OrdersOrdersStateName.PAY;
                break;
            case OrdersOrdersState.SEND:
                ordersStateName = OrdersOrdersStateName.SEND;
                break;
            case OrdersOrdersState.FINISH:
                ordersStateName = OrdersOrdersStateName.FINISH;
                break;
        }
        return ordersStateName;
    }

    public String getOrdersFrom() {
        return ordersFrom;
    }

    public void setOrdersFrom(String ordersFrom) {
        this.ordersFrom = ordersFrom;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getReceiverAreaId1() {
        return receiverAreaId1;
    }

    public void setReceiverAreaId1(int receiverAreaId1) {
        this.receiverAreaId1 = receiverAreaId1;
    }

    public int getReceiverAreaId2() {
        return receiverAreaId2;
    }

    public void setReceiverAreaId2(int receiverAreaId2) {
        this.receiverAreaId2 = receiverAreaId2;
    }

    public int getReceiverAreaId3() {
        return receiverAreaId3;
    }

    public void setReceiverAreaId3(int receiverAreaId3) {
        this.receiverAreaId3 = receiverAreaId3;
    }

    public int getReceiverAreaId4() {
        return receiverAreaId4;
    }

    public void setReceiverAreaId4(int receiverAreaId4) {
        this.receiverAreaId4 = receiverAreaId4;
    }

    public String getReceiverAreaInfo() {
        return receiverAreaInfo;
    }

    public void setReceiverAreaInfo(String receiverAreaInfo) {
        this.receiverAreaInfo = receiverAreaInfo;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getOutOrdersSn() {
        return outOrdersSn;
    }

    public void setOutOrdersSn(String outOrdersSn) {
        this.outOrdersSn = outOrdersSn;
    }

    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getPredepositAmount() {
        return predepositAmount;
    }

    public void setPredepositAmount(BigDecimal predepositAmount) {
        this.predepositAmount = predepositAmount;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public int getEvaluationState() {
        return evaluationState;
    }

    public void setEvaluationState(int evaluationState) {
        this.evaluationState = evaluationState;
    }

    public int getEvaluationAppendState() {
        return evaluationAppendState;
    }

    public void setEvaluationAppendState(int evaluationAppendState) {
        this.evaluationAppendState = evaluationAppendState;
    }

    public Timestamp getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Timestamp evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipNote() {
        return shipNote;
    }

    public void setShipNote(String shipNote) {
        this.shipNote = shipNote;
    }

    public Timestamp getAutoReceiveTime() {
        return autoReceiveTime;
//        if (sendTime != null) {
//            return new Timestamp(sendTime.getTime() + 60 * 60 * 1000 * 24 * Common.ORDER_AUTO_RECEIVE_TIME);
//        } else {
//            return autoReceiveTime;
//        }
    }

    public void setAutoReceiveTime(Timestamp autoReceiveTime) {
        this.autoReceiveTime = autoReceiveTime;
    }

    public String getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(String invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    /**
     * @return
     */
    public String getPaymentTypeName() {
        return paymentTypeCode != null && paymentTypeCode.equals(OrdersPaymentTypeCode.ONLINE) ? OrdersPaymentTypeName.ONLINE : OrdersPaymentTypeName.OFFLINE;
    }

    public String getPaymentName() {
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

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public Integer getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Integer cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * @return
     */
    public String getCancelReasonContent() {
        if (cancelReason == null) {
            return null;
        }
        String cancelReasonContent;
        switch (cancelReason) {
            //买家取消
            case 1:
                cancelReasonContent = "我不想买了";
                break;
            case 2:
                cancelReasonContent = "信息填写错误";
                break;
            case 3:
                cancelReasonContent = "卖家缺货";
                break;
            case 4:
                cancelReasonContent = "付款遇到问题";
                break;
            case 5:
                cancelReasonContent = "买错了";
                break;
            case 6:
                cancelReasonContent = "其它原因";
                break;
            //商家取消
            case 10:
                cancelReasonContent = "无法备齐货物";
                break;
            case 11:
                cancelReasonContent = "不是有效的订单";
                break;
            case 12:
                cancelReasonContent = "买家主动要求";
                break;
            case 13:
                cancelReasonContent = "其它原因";
                break;
            //系统执行
            case 30:
                cancelReasonContent = "超时未支付订单自动关闭";
                break;
            default:
                cancelReasonContent = "其它原因";
                break;
        }
        return cancelReasonContent;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelRole() {
        return cancelRole;
    }

    public void setCancelRole(String cancelRole) {
        this.cancelRole = cancelRole;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    /**
     * 自动取消时间
     *
     * @return
     */
    public Timestamp getAutoCancelTime() {
        Date date = new Date(createTime.getTime() + 60 * 60 * 1000 * Common.ORDER_AUTO_CANCEL_TIME);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(simpleDateFormat.format(date));
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getDelayReceiveState() {
        return delayReceiveState;
    }

    public void setDelayReceiveState(int delayReceiveState) {
        this.delayReceiveState = delayReceiveState;
    }

    public int getAdminReceivePayState() {
        return adminReceivePayState;
    }

    public void setAdminReceivePayState(int adminReceivePayState) {
        this.adminReceivePayState = adminReceivePayState;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersState=" + ordersState +
                ", ordersFrom='" + ordersFrom + '\'' +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", receiverAreaId1=" + receiverAreaId1 +
                ", receiverAreaId2=" + receiverAreaId2 +
                ", receiverAreaId3=" + receiverAreaId3 +
                ", receiverAreaId4=" + receiverAreaId4 +
                ", receiverAreaInfo='" + receiverAreaInfo + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverMessage='" + receiverMessage + '\'' +
                ", createTime=" + createTime +
                ", paymentTypeCode='" + paymentTypeCode + '\'' +
                ", paymentCode='" + paymentCode + '\'' +
                ", outOrdersSn='" + outOrdersSn + '\'' +
                ", paymentTime=" + paymentTime +
                ", sendTime=" + sendTime +
                ", finishTime=" + finishTime +
                ", ordersAmount=" + ordersAmount +
                ", predepositAmount=" + predepositAmount +
                ", freightAmount=" + freightAmount +
                ", evaluationState=" + evaluationState +
                ", evaluationAppendState=" + evaluationAppendState +
                ", evaluationTime=" + evaluationTime +
                ", shipSn='" + shipSn + '\'' +
                ", shipName='" + shipName + '\'' +
                ", shipCode='" + shipCode + '\'' +
                ", shipNote='" + shipNote + '\'' +
                ", autoReceiveTime=" + autoReceiveTime +
                ", invoiceInfo='" + invoiceInfo + '\'' +
                ", ordersType=" + ordersType +
                ", cancelReason=" + cancelReason +
                ", cancelTime=" + cancelTime +
                ", cancelRole='" + cancelRole + '\'' +
                ", commissionAmount=" + commissionAmount +
                ", refundAmount=" + refundAmount +
                ", refundState=" + refundState +
                ", lockState=" + lockState +
                ", delayReceiveState=" + delayReceiveState +
                ", adminReceivePayState=" + adminReceivePayState +
                '}';
    }
}
