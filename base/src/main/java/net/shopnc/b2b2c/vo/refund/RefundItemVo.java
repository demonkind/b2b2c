package net.shopnc.b2b2c.vo.refund;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 退款信息 
 * Created by cj on 2016/2/3.
 */
public class RefundItemVo {
    /**
     * 退款id
     */
    private int refundId;

    /**
     * 订单id
     */

    private int ordersId;

    /**
     * 订单编号
     */

    private long ordersSn;
    /**
     * 申请编号
     */


    private long refundSn;

    /**
     * 店铺编号
     */

    private int storeId;

    /**
     * 店铺名称
     */


    private String storeName;

    /**
     * 会员id
     */


    private int memberId;

    /**
     * 会员名称
     */

    private String memberName;

    /**
     * 商品SKU编号
     */

    private int goodsId;

    /**
     * 订单商品编号</br>
     * 主键、自增
     */

    private int ordersGoodsId;

    /**
     * 商品名称
     */

    private String goodsName;

    /**
     * 购买数量
     */

    private int goodsNum;

    /**
     * 退款金额
     */

    private BigDecimal refundAmount;

    /**
     * 商品图片
     */

    private String goodsImage;

    /**
     * 订单类型类型</br>
     * 类型:1默认2团购商品3限时折扣商品4组合套装
     */

    private int orderGoodsType = 1;

    /**
     * 申请类型</br>
     * 申请类型:1为退款,2为退货,默认为1
     */

    private int refundType = 1;

    /**
     * 卖家处理状态:1为待审核,2为同意,3为不同意,默认为1
     */

    private int sellerState = 1;
    /**
     * 申请状态:1为处理中,2为待管理员处理,3为已完成,默认为1
     */

    private int refundState = 1;

    /**
     * 申请状态:1为处理中,2为待管理员处理,3为已完成,默认为1
     */

    private int returnType = 1;

    /**
     * 订单锁定类型:1为不用锁定,2为需要锁定,默认为1
     */

    private int orderLock = 1;

    /**
     * 物流状态:1为待发货,2为待收货,3为未收到,4为已收货,默认为1
     */

    private int goodsState = 1;

    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp addTime;
    /**
     * 卖家处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp sellerTime;

    /**
     * 卖家处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp adminTime;


    /**
     * 退款原因id
     */

    private int reasonId;

    /**
     * 原因内容
     */

    private String reasonInfo;

    /**
     * 图片
     */
    private String picJson;
    /**
     * 买家备注
     */
    private String buyerMessage;
    /**
     * 卖家备注
     */
    private String sellerMessage;
    /**
     * 管理员备注
     */
    private String adminMessage;
    /**
     * 快递公司编号</br>
     * 主键、自增
     */
    private int shipId;
    /**
     * 发货单号
     */

    private String shipSn;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp shipTime;

    /**
     * 收货延时时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp delayTime;
    /**
     * 收货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Timestamp receiveTime;
    /**
     * 收货备注
     */
    private String receiveMessage;
    /**
     * 佣金比例
     */
    private int commissionRate;


    //新增
    /**
     * 卖家状态文字
     */
    private String sellerStateText;
    /**
     * 退款状态文字
     */
    private String refundStateText;

    /**
     * 卖家不处理退款退货申请时按同意处理
     */
    private int maxDayRefundConfirm = 7;
    /**
     * 卖家不处理收货时按弃货处理
     */
    private int maxDayReturnConfirm = 7;
    /**
     * 退货的商品发货多少天以后才可以选择没收到
     */
    private int maxDayReturnDelay = 5;


    /**
     * 会员中心-退货-发货按钮是否显示
     */
    private int showMemberReturnShip;

    /**
     * 会员中心-退货-延迟按钮是否显示
     */
    private int showMemberReturnDelay;

    /**
     * 店铺-退货-收货按钮是否显示
     */
    private int showStoreReturnReceive;

    /**
     * 店铺-退货 -是否显示未收到货
     */
    private int showStoreReturnUnreceived;

    /**
     * 大后台-退货- 是否显示处理按钮
     */
    private int showAdminReturnHandle;
    ////
    public RefundItemVo(Refund refund) {
        this.refundId = refund.getRefundId();
        this.ordersId = refund.getOrdersId();
        this.ordersSn = refund.getOrdersSn();
        this.refundSn = refund.getRefundSn();
        this.storeId = refund.getStoreId();
        this.storeName = refund.getStoreName();
        this.memberId = refund.getMemberId();
        this.memberName = refund.getMemberName();
        this.goodsId = refund.getGoodsId();
        this.ordersGoodsId = refund.getOrdersGoodsId();
        this.goodsName = refund.getGoodsName();
        this.goodsNum = refund.getGoodsNum();
        this.refundAmount = refund.getRefundAmount();
        this.goodsImage = refund.getGoodsImage();
        this.orderGoodsType = refund.getOrderGoodsType();
        this.refundType = refund.getRefundType();
        this.sellerState = refund.getSellerState();
        this.refundState = refund.getRefundState();
        this.returnType = refund.getReturnType();
        this.orderLock = refund.getOrderLock();
        this.goodsState = refund.getGoodsState();
        this.addTime = refund.getAddTime();
        this.sellerTime = refund.getSellerTime();
        this.adminTime = refund.getAdminTime();
        this.reasonId = refund.getReasonId();
        this.reasonInfo = refund.getReasonInfo();
        this.picJson = refund.getPicJson();
        this.buyerMessage = refund.getBuyerMessage();
        this.sellerMessage = refund.getSellerMessage();
        this.adminMessage = refund.getAdminMessage();
        this.shipId = refund.getShipId();
        this.shipSn = refund.getShipSn();
        this.shipTime = refund.getShipTime();
        this.delayTime = refund.getDelayTime();
        this.receiveTime = refund.getReceiveTime();
        this.receiveMessage = refund.getReceiveMessage();
        this.commissionRate = refund.getCommissionRate();
        this.sellerStateText = this.getSellerStateTextForSelf();
        this.refundStateText = this.getRefundStateTextForSelf();

        this.showMemberReturnShip = this.isShowMemberReturnShip();
        this.showMemberReturnDelay = this.isShowMemberReturnDelay();
        this.showStoreReturnReceive = this.isShowStoreReturnReceive();
        this.showStoreReturnUnreceived = this.isShowStoreReturnUnreceived();
        this.showAdminReturnHandle = this.isShowAdminReturnHandle();
    }
    ////

    /**
     * 大后台-退货-判断是否显示处理按钮
     * @return
     */
    public int isShowAdminReturnHandle(){
        return this.refundState == RefundState.REFUND_STATE_CHECK ? 1:0;
    }
    /**
     * 店铺退款中是否显示未收到货选项
     * @return
     */
    public int isShowStoreReturnUnreceived(){
        if (this.delayTime == null){
            return 0;
        }
        Timestamp a = ShopHelper.getFutureTimestamp(this.delayTime, Calendar.DATE ,this.maxDayReturnDelay);
        return a.before(ShopHelper.getCurrentTimestamp()) ? 1 : 0;
    }
    /**
     * 店铺退货列表中是否显示收货
     * @return
     */
    public int isShowStoreReturnReceive(){
        return (this.sellerState == RefundState.SELLER_STATE_AGREE && this.returnType == RefundState.REFUND_TYPE_RETURN && this.goodsState == RefundState.RETURN_SHIP_STATE_SEND) ? 1:0;
    }
    /**
     * 判断会员 退货列表中是否显示退货发货按钮
     *
     * @return
     */
    public int isShowMemberReturnShip() {
        return (this.sellerState == 2 && this.returnType == 2 && this.goodsState == 1) ? 1 : 0;
    }

    /**
     * 判断会员退货是否显示延迟按钮
     *
     * @return
     */
    public int isShowMemberReturnDelay() {
        return (this.sellerState == RefundState.SELLER_STATE_AGREE && this.returnType == RefundState.RETURN_TYPE_RETURNED_YES && this.goodsState == RefundState.RETURN_SHIP_STATE_UNRECEIVED) ? 1 : 0;
    }

    /**
     * 获取卖家处理状态文字:1为待审核,2为同意,3为不同意,默认为1
     *
     * @return
     */
    public String getSellerStateTextForSelf() {
        String r = "";
        switch (sellerState) {
            case 1:
                r = "待审核";
                break;
            case 2:
                r = "同意";
                break;
            case 3:
                r = "不同意";
                break;
            default:
                break;
        }
        return r;
    }

    /**
     * 申请状态文字:1为处理中,2为待管理员处理,3为已完成,默认为1
     *
     * @return
     */
    public String getRefundStateTextForSelf() {
        String r = "";
        switch (refundState) {
            case 1:
                r = "处理中";
                break;
            case 2:
                r = "待管理员处理";
                break;
            case 3:
                r = "已完成";
                break;
            default:
                break;
        }
        return sellerState == 2 ? r : "无";
    }


    public String getRefundStateText() {
        return refundStateText;
    }

    public String getSellerStateText() {
        return sellerStateText;
    }

    public void setRefundStateText(String refundStateText) {
        this.refundStateText = refundStateText;
    }


    public void setSellerStateText(String sellerStateText) {
        this.sellerStateText = sellerStateText;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
    }

    public Timestamp getAdminTime() {
        return adminTime;
    }

    public void setAdminTime(Timestamp adminTime) {
        this.adminTime = adminTime;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }


    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
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

    public int getOrderGoodsType() {
        return orderGoodsType;
    }

    public void setOrderGoodsType(int orderGoodsType) {
        this.orderGoodsType = orderGoodsType;
    }

    public int getOrderLock() {
        return orderLock;
    }

    public void setOrderLock(int orderLock) {
        this.orderLock = orderLock;
    }

    public int getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(int ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
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

    public String getPicJson() {
        return picJson;
    }

    public void setPicJson(String picJson) {
        this.picJson = picJson;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonInfo() {
        return reasonInfo;
    }

    public void setReasonInfo(String reasonInfo) {
        this.reasonInfo = reasonInfo;
    }

    public String getReceiveMessage() {
        return receiveMessage;
    }

    public void setReceiveMessage(String receiveMessage) {
        this.receiveMessage = receiveMessage;
    }

    public Timestamp getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Timestamp receiveTime) {
        this.receiveTime = receiveTime;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public long getRefundSn() {
        return refundSn;
    }

    public void setRefundSn(long refundSn) {
        this.refundSn = refundSn;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getRefundType() {
        return refundType;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public String getSellerMessage() {
        return sellerMessage;
    }

    public void setSellerMessage(String sellerMessage) {
        this.sellerMessage = sellerMessage;
    }

    public int getSellerState() {
        return sellerState;
    }

    public void setSellerState(int sellerState) {
        this.sellerState = sellerState;
    }

    public Timestamp getSellerTime() {
        return sellerTime;
    }

    public void setSellerTime(Timestamp sellerTime) {
        this.sellerTime = sellerTime;
    }

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public Timestamp getShipTime() {
        return shipTime;
    }

    public void setShipTime(Timestamp shipTime) {
        this.shipTime = shipTime;
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

    public int getShowMemberReturnShip() {
        return showMemberReturnShip;
    }

    public void setShowMemberReturnShip(int showMemberReturnShip) {
        this.showMemberReturnShip = showMemberReturnShip;
    }

    public int getMaxDayRefundConfirm() {
        return maxDayRefundConfirm;
    }

    public void setMaxDayRefundConfirm(int maxDayRefundConfirm) {
        this.maxDayRefundConfirm = maxDayRefundConfirm;
    }

    public int getMaxDayReturnConfirm() {
        return maxDayReturnConfirm;
    }

    public void setMaxDayReturnConfirm(int maxDayReturnConfirm) {
        this.maxDayReturnConfirm = maxDayReturnConfirm;
    }

    public int getMaxDayReturnDelay() {
        return maxDayReturnDelay;
    }

    public void setMaxDayReturnDelay(int maxDayReturnDelay) {
        this.maxDayReturnDelay = maxDayReturnDelay;
    }

    public Timestamp getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Timestamp delayTime) {
        this.delayTime = delayTime;
    }

    public int getShowMemberReturnDelay() {
        return showMemberReturnDelay;
    }

    public void setShowMemberReturnDelay(int showMemberReturnDelay) {
        this.showMemberReturnDelay = showMemberReturnDelay;
    }

    public int getShowStoreReturnReceive() {
        return showStoreReturnReceive;
    }

    public void setShowStoreReturnReceive(int showStoreReturnReceive) {
        this.showStoreReturnReceive = showStoreReturnReceive;
    }

    public int getShowStoreReturnUnreceived() {
        return showStoreReturnUnreceived;
    }

    public void setShowStoreReturnUnreceived(int showStoreReturnUnreceived) {
        this.showStoreReturnUnreceived = showStoreReturnUnreceived;
    }

    public int getShowAdminReturnHandle() {
        return showAdminReturnHandle;
    }

    public void setShowAdminReturnHandle(int showAdminReturnHandle) {
        this.showAdminReturnHandle = showAdminReturnHandle;
    }
    ////


    /**
     * long型的数据输出json后，过长会被截断，所以需要返回string型
     * by hbj
     *
     * @return
     */
    public String getRefundSnStr() {
        return Long.toString(refundSn);
    }


    public String getOrdersSnStr(){
        return Long.toString(ordersSn);
    }
    /**
     * 返回退还佣金金额
     * by hbj
     *
     * @return
     */
    public BigDecimal getRefundCommissionAmount() {
        return PriceHelper.div(PriceHelper.mul(refundAmount, commissionRate), 100);
    }

    @Override
    public String toString() {
        return "RefundItemVo{" +
                "refundId=" + refundId +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", refundSn=" + refundSn +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", goodsId=" + goodsId +
                ", ordersGoodsId=" + ordersGoodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsNum=" + goodsNum +
                ", refundAmount=" + refundAmount +
                ", goodsImage='" + goodsImage + '\'' +
                ", orderGoodsType=" + orderGoodsType +
                ", refundType=" + refundType +
                ", sellerState=" + sellerState +
                ", refundState=" + refundState +
                ", returnType=" + returnType +
                ", orderLock=" + orderLock +
                ", goodsState=" + goodsState +
                ", addTime=" + addTime +
                ", sellerTime=" + sellerTime +
                ", adminTime=" + adminTime +
                ", reasonId=" + reasonId +
                ", reasonInfo='" + reasonInfo + '\'' +
                ", picJson='" + picJson + '\'' +
                ", buyerMessage='" + buyerMessage + '\'' +
                ", sellerMessage='" + sellerMessage + '\'' +
                ", adminMessage='" + adminMessage + '\'' +
                ", shipId=" + shipId +
                ", shipSn='" + shipSn + '\'' +
                ", shipTime=" + shipTime +
                ", delayTime=" + delayTime +
                ", receiveTime=" + receiveTime +
                ", receiveMessage='" + receiveMessage + '\'' +
                ", commissionRate=" + commissionRate +
                ", sellerStateText='" + sellerStateText + '\'' +
                ", refundStateText='" + refundStateText + '\'' +
                ", maxDayRefundConfirm=" + maxDayRefundConfirm +
                ", maxDayReturnConfirm=" + maxDayReturnConfirm +
                ", maxDayReturnDelay=" + maxDayReturnDelay +
                ", showMemberReturnShip=" + showMemberReturnShip +
                ", showMemberReturnDelay=" + showMemberReturnDelay +
                ", showStoreReturnReceive=" + showStoreReturnReceive +
                ", showStoreReturnUnreceived=" + showStoreReturnUnreceived +
                ", showAdminReturnHandle=" + showAdminReturnHandle +
                '}';
    }
}
