package net.shopnc.b2b2c.vo.orders;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.util.OrdersHelper;
import net.shopnc.common.util.PriceHelper;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * 订单Vo
 * Created by hbj on 2015/12/30.
 */
public class OrdersVo {

    private List<OrdersGoodsVo> ordersGoodsVoList;
    /**
     * 订单主键ID
     */
    private int ordersId;
    /**
     * 订单编号
     */
    private long ordersSn;
    /**
     * 订单状态
     */
    private int ordersState;
    /**
     * 订单状态
     */
    private String ordersStateName;
    /**
     * 订单来源
     */
    private String ordersFrom;
    /**
     * 支付单ID
     */
    private int payId;
    /**
     * 支付单号
     */
    private long paySn;
    /**
     * 店铺ID
     */
    private int storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺电话
     */
    private String storePhone;
    /**
     * 店铺地址
     */
    private String storeAddress;
    /**
     * QQ
     */
    private String storeQq;
    /**
     * 旺旺
     */
    private String storeWw;
    /**
     * 卖家账号
     */
    private String sellerName;
    /**
     * 会员ID
     */
    private int memberId;
    /**
     * 会员名
     */
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
    private String receiverAddress;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 买家留言
     */
    private String receiverMessage;
    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp createTime;
    /**
     * 线上线下支付代码online/offline
     */
    private String paymentTypeCode;
    /**
     * 线上线下支付中文名
     */
    private String paymentTypeName;
    /**
     * 支付方式代码
     */
    private String paymentCode;
    /**
     * 外部交易号
     */
    private String outOrdersSn;
    /**
     * 支付方式名称
     */
    private String paymentName;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp paymentTime;
    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp sendTime;
    /**
     * 订单完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp finishTime;
    /**
     * 订单金额
     */
    private BigDecimal ordersAmount;
    /**
     * 预存款支付金额
     */
    private BigDecimal predepositAmount;
    /**
     * 运费
     */
    private BigDecimal freightAmount;
    /**
     * 评价状态
     */
    private int evaluationState;
    /**
     * 追加评价状态
     */
    private int evaluationAppendState;
    /**
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp evaluationTime;
    /**
     * 发货单号
     */
    private String shipSn;
    /**
     * 快递公司
     */
    private String shipName;
    /**
     * 快递公司编码
     */
    private String shipCode;
    /**
     * 快递公司网址
     */
    private String shipUrl;
    /**
     * 发货备注
     */
    private String shipNote;
    /**
     * 自动收货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp autoReceiveTime;
    /**
     * 发票内容
     */
    private String invoiceInfo;
    /**
     * 订单类型
     */
    private int ordersType;
    /**
     * 订单取消原因
     */
    private Integer cancelReason;
    /**
     * 订单取消原因
     */
    private String cancelReasonContent;
    /**
     * 关闭时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp cancelTime;
    /**
     * 自动关闭时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp autoCancelTime;
    /**
     * 自动取消周期
     */
    private int autoCancelCycle;
    /**
     * 订单关闭操作主体
     */
    private String cancelRole;
    /**
     * 订单收取佣金
     */
    private BigDecimal commissionAmount = new BigDecimal(0);
    /**
     * 订单列表是否显示操作
     */
    private int showMemberCancel = 0;
    private int showMemberPay = 0;
    private int showShipSearch = 0;
    private int showMemberReceive = 0;
    private int showStoreCancel = 0;
    private int showStoreModifyFreight = 0;
    private int showStoreSend = 0;
    private int showStoreSendModify = 0;
    private int showEvaluation = 0;
    private int showEvaluationAppend = 0;
    private int showStoreModifyReceiver = 0;
    private int showAdminCancel = 0;
    private int showAdminPay = 0;
    private int showMemberDelayReceive = 0;
    // bycj [ 订单退款，全退 ]
    private int showMemberRefundAll = 0;

    /**
     * bycj [ 退款金额 ]
     */
    private BigDecimal refundAmount;

    /**
     * bycj [ 退款状态:0是无退款,1是部分退款,2是全部退款 ]
     */
    private int refundState;

    // bycj [ 是否显示退款中 ]
    private int showRefundWaiting = 0 ;

    // bycj [ 退款信息 ]
    private RefundItemVo refundItemVo;
    /**
     * 是否进行过延迟收货操作，买家只能点击一次 0-否/1-是
     */
    private int delayReceiveState = State.NO;

    /**
     * 是否是管理员点击的收款
     */
    private int adminReceivePayState = State.NO;
    
    /*******************************退款，退货状态追加start ***********************************/
    
    /**
     * 退款退货状态(0：未发生退款退货，11:退款中, 18:退款结束,19:退款拒绝,21:退货中, 22:同意退货,23:同意退货并收货中,24:退货结束, 25:同意退货并弃货, 29:退货拒绝)
     */
    private int refundReturnStatus = 0;
    
    /*******************************退款，退货状态追加end ***********************************/
    
    ///
    public OrdersVo() {}

    public int getShowMemberRefundAll() {
        return showMemberRefundAll;
    }

    public void setShowMemberRefundAll(int showMemberRefundAll) {
        this.showMemberRefundAll = showMemberRefundAll;
    }

    public OrdersVo(Orders orders) {
        this.ordersId = orders.getOrdersId();
        this.ordersSn = orders.getOrdersSn();
        this.ordersState = orders.getOrdersState();
        this.ordersStateName = orders.getOrdersStateName();
        this.ordersFrom = orders.getOrdersFrom();
        this.payId = orders.getPayId();
        this.paySn = orders.getPaySn();
        this.storeId = orders.getStoreId();
        this.storeName = orders.getStoreName();
        this.memberId = orders.getMemberId();
        this.memberName = orders.getMemberName();
        this.receiverAreaId1 = orders.getReceiverAreaId1();
        this.receiverAreaId2 = orders.getReceiverAreaId2();
        this.receiverAreaId3 = orders.getReceiverAreaId3();
        this.receiverAreaId4 = orders.getReceiverAreaId4();
        this.receiverAreaInfo = orders.getReceiverAreaInfo();
        this.receiverAddress = orders.getReceiverAddress();
        this.receiverPhone = orders.getReceiverPhone();
        this.receiverName = orders.getReceiverName();
        this.receiverMessage = orders.getReceiverMessage();
        this.createTime = orders.getCreateTime();
        this.paymentTypeCode = orders.getPaymentTypeCode();
        this.paymentTypeName = orders.getPaymentTypeName();
        this.paymentCode = orders.getPaymentCode();
        this.paymentName = orders.getPaymentCode() != null ? orders.getPaymentName() : orders.getPaymentTypeName();
        this.outOrdersSn = orders.getOutOrdersSn();
        this.paymentTime = orders.getPaymentTime();
        this.sendTime = orders.getSendTime();
        this.finishTime = orders.getFinishTime();
        this.ordersAmount = orders.getOrdersAmount();
        this.predepositAmount = orders.getPredepositAmount();
        this.freightAmount = orders.getFreightAmount();
        this.evaluationState = orders.getEvaluationState();
        this.evaluationAppendState = orders.getEvaluationAppendState();
        this.evaluationTime = orders.getEvaluationTime();
        this.shipSn = orders.getShipSn();
        this.shipName = orders.getShipName();
        this.shipCode = orders.getShipCode();
        this.autoReceiveTime = orders.getAutoReceiveTime();
        this.invoiceInfo = orders.getInvoiceInfo();
        this.ordersType = orders.getOrdersType();
        this.cancelReason = orders.getCancelReason();
        this.cancelReasonContent = orders.getCancelReasonContent();
        this.cancelTime = orders.getCancelTime();
        this.cancelRole = orders.getCancelRole();
        this.autoCancelCycle = Common.ORDER_AUTO_CANCEL_TIME;
        this.commissionAmount = orders.getCommissionAmount();
        this.shipNote = orders.getShipNote();
        this.adminReceivePayState = orders.getAdminReceivePayState();
        this.refundItemVo = null;

        //操作权限设置
        this.showMemberCancel = OrdersHelper.operationValidate(orders, OrdersOperationType.MEMBER_CANCEL);
        this.showMemberPay = OrdersHelper.operationValidate(orders, OrdersOperationType.MEMBER_PAY);
        this.showMemberReceive = OrdersHelper.operationValidate(orders,OrdersOperationType.MEMBER_RECEIVE);
        this.showStoreCancel = OrdersHelper.operationValidate(orders,OrdersOperationType.STORE_CANCEL);
        this.showStoreModifyFreight = OrdersHelper.operationValidate(orders,OrdersOperationType.STORE_MODIFY_FREIGHT);
        this.showStoreSend = OrdersHelper.operationValidate(orders,OrdersOperationType.STORE_SEND);
        this.showStoreSendModify = OrdersHelper.operationValidate(orders,OrdersOperationType.STORE_SEND_MODIFY);
        this.showStoreModifyReceiver = OrdersHelper.operationValidate(orders,OrdersOperationType.STORE_MODIFY_RECEIVER);
        this.showEvaluation = OrdersHelper.operationValidate(orders,OrdersOperationType.MEMBER_EVALUATION);
        this.showEvaluationAppend = OrdersHelper.operationValidate(orders,OrdersOperationType.MEMBER_EVALUATION_APPEND);
        this.showShipSearch = OrdersHelper.operationValidate(orders,OrdersOperationType.SHIP_SEARCH);
        this.showAdminCancel = OrdersHelper.operationValidate(orders,OrdersOperationType.ADMIN_CANCEL);
        this.showAdminPay = OrdersHelper.operationValidate(orders,OrdersOperationType.ADMIN_PAY);
        this.showMemberDelayReceive = OrdersHelper.operationValidate(orders,OrdersOperationType.MEMBER_DELAY_RECEIVE);

    }

    public int getShowRefundWaiting() {
        return showRefundWaiting;
    }

    public void setShowRefundWaiting(int showRefundWaiting) {
        this.showRefundWaiting = showRefundWaiting;
    }

    public List<OrdersGoodsVo> getOrdersGoodsVoList() {
        return ordersGoodsVoList;
    }

    public void setOrdersGoodsVoList(List<OrdersGoodsVo> ordersGoodsVoList) {
        this.ordersGoodsVoList = ordersGoodsVoList;
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

    public String getOrdersStateName() {
        return ordersStateName;
    }

    public void setOrdersStateName(String ordersStateName) {
        this.ordersStateName = ordersStateName;
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

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreQq() {
        return storeQq;
    }

    public void setStoreQq(String storeQq) {
        this.storeQq = storeQq;
    }

    public String getStoreWw() {
        return storeWw;
    }

    public void setStoreWw(String storeWw) {
        this.storeWw = storeWw;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
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

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
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

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
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

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
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

    public String getShipUrl() {
        return shipUrl;
    }

    public void setShipUrl(String shipUrl) {
        this.shipUrl = shipUrl;
    }

    public Timestamp getAutoReceiveTime() {
        return autoReceiveTime;
    }

    public void setAutoReceiveTime(Timestamp autoReceiveTime) {
        this.autoReceiveTime = autoReceiveTime;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getInvoiceInfo() {
        return invoiceInfo;
    }

    /**
     * 发票信息
     * @return
     */
    public HashMap<String,String> getInvoiceList() {
        HashMap<String,String> hashMap = new HashMap<String, String>();
        if (invoiceInfo != null && !invoiceInfo.equals("")) {
            String[] invoiceList = invoiceInfo.split(",");
            for (int i=0; i<invoiceList.length; i++) {
                String[] item = invoiceList[i].split(":");
                hashMap.put(item[0],item[1]);
            }
        }
        return hashMap;
    }

    public void setInvoiceInfo(String invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
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

    public String getCancelReasonContent() {
        return cancelReasonContent;
    }

    public void setCancelReasonContent(String cancelReasonContent) {
        this.cancelReasonContent = cancelReasonContent;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Timestamp getAutoCancelTime() {
        return autoCancelTime;
    }

    public void setAutoCancelTime(Timestamp autoCancelTime) {
        this.autoCancelTime = autoCancelTime;
    }

    public int getAutoCancelCycle() {
        return autoCancelCycle;
    }

    public void setAutoCancelCycle(int autoCancelCycle) {
        this.autoCancelCycle = autoCancelCycle;
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

    public int getShowMemberCancel() {
        return showMemberCancel;
    }

    public void setShowMemberCancel(int showMemberCancel) {
        this.showMemberCancel = showMemberCancel;
    }

    public int getShowMemberPay() {
        return showMemberPay;
    }

    public void setShowMemberPay(int showMemberPay) {
        this.showMemberPay = showMemberPay;
    }

    public int getShowMemberReceive() {
        return showMemberReceive;
    }

    public void setShowMemberReceive(int showMemberReceive) {
        this.showMemberReceive = showMemberReceive;
    }

    public int getShowStoreCancel() {
        return showStoreCancel;
    }

    public void setShowStoreCancel(int showStoreCancel) {
        this.showStoreCancel = showStoreCancel;
    }

    public int getShowStoreModifyFreight() {
        return showStoreModifyFreight;
    }

    public void setShowStoreModifyFreight(int showStoreModifyFreight) {
        this.showStoreModifyFreight = showStoreModifyFreight;
    }

    public int getShowStoreSend() {
        return showStoreSend;
    }

    public void setShowStoreSend(int showStoreSend) {
        this.showStoreSend = showStoreSend;
    }

    public String getShipNote() {
        return shipNote;
    }

    public void setShipNote(String shipNote) {
        this.shipNote = shipNote;
    }

    public int getShowStoreSendModify() {
        return showStoreSendModify;
    }

    public void setShowStoreSendModify(int showStoreSendModify) {
        this.showStoreSendModify = showStoreSendModify;
    }

    public int getShowStoreModifyReceiver() {
        return showStoreModifyReceiver;
    }

    public void setShowStoreModifyReceiver(int showStoreModifyReceiver) {
        this.showStoreModifyReceiver = showStoreModifyReceiver;
    }

    public int getShowEvaluation() {
        return showEvaluation;
    }

    public void setShowEvaluation(int showEvaluation) {
        this.showEvaluation = showEvaluation;
    }

    public int getShowEvaluationAppend() {
        return showEvaluationAppend;
    }

    public void setShowEvaluationAppend(int showEvaluationAppend) {
        this.showEvaluationAppend = showEvaluationAppend;
    }

    public int getShowShipSearch() {
        return showShipSearch;
    }

    public void setShowShipSearch(int showShipSearch) {
        this.showShipSearch = showShipSearch;
    }

    public int getShowAdminCancel() {
        return showAdminCancel;
    }

    public void setShowAdminCancel(int showAdminCancel) {
        this.showAdminCancel = showAdminCancel;
    }

    public int getShowAdminPay() {
        return showAdminPay;
    }

    public void setShowAdminPay(int showAdminPay) {
        this.showAdminPay = showAdminPay;
    }

    public int getShowMemberDelayReceive() {
        return showMemberDelayReceive;
    }

    public void setShowMemberDelayReceive(int showMemberDelayReceive) {
        this.showMemberDelayReceive = showMemberDelayReceive;
    }

    public int getAdminReceivePayState() {
        return adminReceivePayState;
    }

    public void setAdminReceivePayState(int adminReceivePayState) {
        this.adminReceivePayState = adminReceivePayState;
    }

    public RefundItemVo getRefundItemVo() {
        return refundItemVo;
    }

    public void setRefundItemVo(RefundItemVo refundItemVo) {
        this.refundItemVo = refundItemVo;
    }

    /**
     * 模板中读取订单状态
     * @return
     */
    public int getOrdersStateIsCancel() {
        return ordersState == OrdersOrdersState.CANCEL ? 1 : 0;
    }
    public int getOrdersStateIsNew() {
        return ordersState == OrdersOrdersState.NEW ? 1 : 0;
    }
    public int getOrdersStateIsPay() {
        return ordersState == OrdersOrdersState.PAY ? 1 : 0;
    }
    public int getOrdersStateIsSend() {
        return ordersState == OrdersOrdersState.SEND ? 1 : 0;
    }
    public int getOrdersStateIsFinish() {
        return ordersState == OrdersOrdersState.FINISH ? 1 : 0;
    }
    public int getOrdersStateIsEvaluation() {
        return ordersState == OrdersEvaluationState.NO ? 1 : 0;
    }

    /**
     * 是否是货到付款
     * @return
     */
    public int getIsOffLineOrders() {
        return paymentTypeCode == OrdersPaymentTypeCode.OFFLINE ? 1 : 0;
    }

    /**
     * 是否可以物流跟踪
     * @return
     */
    public int getIsShipTrack() {
        return shipSn != null && (getOrdersStateIsSend() == 1 || getOrdersStateIsFinish() == 1) ? 1 : 0;
    }

    /**
     * long型的数据输出json后，过长会被截断，所以需要返回string型
     * @return
     */
    public String getOrdersSnStr() {
        return Long.toString(ordersSn);
    }
    public String getPaySnStr() {
        return Long.toString(paySn);
    }

    public int getDelayReceiveState() {
        return delayReceiveState;
    }

    public void setDelayReceiveState(int delayReceiveState) {
        this.delayReceiveState = delayReceiveState;
    }

    /**
     * 组合支付时，通过在线API支付多少钱
     * @return
     */
    public BigDecimal getApiPayDiffAmount() {
        return PriceHelper.sub(ordersAmount,predepositAmount);
    }
    

	public int getRefundReturnStatus() {
		return refundReturnStatus;
	}

	public void setRefundReturnStatus(int refundReturnStatus) {
		this.refundReturnStatus = refundReturnStatus;
	}

	@Override
    public String toString() {
        return "OrdersVo{" +
                "ordersGoodsVoList=" + ordersGoodsVoList +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersState=" + ordersState +
                ", ordersStateName='" + ordersStateName + '\'' +
                ", ordersFrom='" + ordersFrom + '\'' +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeQq='" + storeQq + '\'' +
                ", storeWw='" + storeWw + '\'' +
                ", sellerName='" + sellerName + '\'' +
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
                ", paymentTypeName='" + paymentTypeName + '\'' +
                ", paymentCode='" + paymentCode + '\'' +
                ", outOrdersSn='" + outOrdersSn + '\'' +
                ", paymentName='" + paymentName + '\'' +
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
                ", shipUrl='" + shipUrl + '\'' +
                ", shipNote='" + shipNote + '\'' +
                ", autoReceiveTime=" + autoReceiveTime +
                ", invoiceInfo='" + invoiceInfo + '\'' +
                ", ordersType=" + ordersType +
                ", cancelReason=" + cancelReason +
                ", cancelReasonContent='" + cancelReasonContent + '\'' +
                ", cancelTime=" + cancelTime +
                ", autoCancelTime=" + autoCancelTime +
                ", autoCancelCycle=" + autoCancelCycle +
                ", cancelRole='" + cancelRole + '\'' +
                ", commissionAmount=" + commissionAmount +
                ", showMemberCancel=" + showMemberCancel +
                ", showMemberPay=" + showMemberPay +
                ", showShipSearch=" + showShipSearch +
                ", showMemberReceive=" + showMemberReceive +
                ", showStoreCancel=" + showStoreCancel +
                ", showStoreModifyFreight=" + showStoreModifyFreight +
                ", showStoreSend=" + showStoreSend +
                ", showStoreSendModify=" + showStoreSendModify +
                ", showEvaluation=" + showEvaluation +
                ", showEvaluationAppend=" + showEvaluationAppend +
                ", showStoreModifyReceiver=" + showStoreModifyReceiver +
                ", showAdminCancel=" + showAdminCancel +
                ", showAdminPay=" + showAdminPay +
                ", showMemberDelayReceive=" + showMemberDelayReceive +
                ", showMemberRefundAll=" + showMemberRefundAll +
                ", refundAmount=" + refundAmount +
                ", refundState=" + refundState +
                ", showRefundWaiting=" + showRefundWaiting +
                ", refundItemVo=" + refundItemVo +
                ", delayReceiveState=" + delayReceiveState +
                ", adminReceivePayState=" + adminReceivePayState +
                ", refundReturnStatus=" + refundReturnStatus +
                '}';
    }
}
