package net.shopnc.b2b2c.domain.refund;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 退款实体
 * Created by cj on 2016/2/1.
 */

@Entity
@Table(name = "refund")
public class Refund implements Serializable {
    /**
     * 退款id
     */
    @Id
    @GeneratedValue
    @Column(name = "refund_id")
    private int refundId;

    /**
     * 订单id
     */
    @Column(name = "orders_id")
    private int ordersId;

    /**
     * 订单编号
     */
    @Column(name = "orders_sn")
    private long ordersSn;
    /**
     * 申请编号
     */

    @Column(name = "refund_sn")
    private long refundSn;

    /**
     * 店铺编号
     */

    @Column(name = "store_id")
    private int storeId;

    /**
     * 店铺名称
     */

    @Size(min = 2, max = 50)
    @Column(name = "store_name")
    private String storeName;

    /**
     * 会员id
     */

    @Column(name = "member_id")
    private int memberId;

    /**
     * 会员名称
     */
    @Column(name = "member_name", length = 100)
    private String memberName = "";

    /**
     * 商品SKU编号
     */
    @Column(name = "goods_id")
    private int goodsId;

    /**
     * 订单商品编号</br>
     * 主键、自增
     */
    @Column(name = "orders_goods_id")
    private int ordersGoodsId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 购买数量
     */
    @Column(name = "goods_num")
    private int goodsNum;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount")
    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * 商品图片
     */
    @Column(name = "goods_image")
    private String goodsImage;

    /**
     * 订单类型类型</br>
     * 类型:1默认2团购商品3限时折扣商品4组合套装
     */
    @Column(name = "order_goods_type")
    private int orderGoodsType = 1;

    /**
     * 申请类型</br>
     * 申请类型:1为退款,2为退货,默认为1
     */
    @Column(name = "refund_type")
    private int refundType = 1;

    /**
     * 卖家处理状态:1为待审核,2为同意,3为不同意,默认为1
     */
    @Column(name = "seller_state")
    private int sellerState = 1;
    /**
     * 申请状态:1为处理中,2为待管理员处理,3为已完成,默认为1
     */
    @Column(name = "refund_state")
    private int refundState = 1;

    /**
     * 退货类型:1为不用退货,2为需要退货,默认为1
     */
    @Column(name = "return_type")
    private int returnType = 1;

    /**
     * 订单锁定类型:1为不用锁定,2为需要锁定,默认为1
     */
    @Column(name = "order_lock")
    private int orderLock = 1;

    /**
     * 物流状态:1为待发货,2为待收货,3为未收到,4为已收货,默认为1
     */
    @Column(name = "goods_state")
    private int goodsState = 1;

    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "add_time")
    private Timestamp addTime;
    /**
     * 卖家处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "seller_time")
    private Timestamp sellerTime;

    /**
     * 平台处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "admin_time")
    private Timestamp adminTime;


    /**
     * 退款原因id
     */
    @Column(name = "reason_id")
    private int reasonId;

    /**
     * 原因内容
     */
    @Column(name = "reason_info")
    private String reasonInfo;

    /**
     * 图片
     */
    @Lob
    @Column(name = "pic_json")
    private String picJson;

    /**
     * 买家备注
     */
    @Column(name = "buyer_message")
    private String buyerMessage;

    /**
     * 卖家备注
     */
    @Column(name = "seller_message")
    private String sellerMessage;

    /**
     * 管理员备注
     */
    @Column(name = "admin_message")
    private String adminMessage;

    /**
     * 快递公司编号</br>
     * 主键、自增
     */
    @Column(name = "ship_id")
    private int shipId;

    /**
     * 发货单号
     */
    @Column(name = "ship_sn")
    private String shipSn;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "ship_time")
    private Timestamp shipTime;

    /**
     * 收货延时时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name="delay_time")
    private Timestamp delayTime;
    /**
     * 收货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "receive_time")
    private Timestamp receiveTime;

    /**
     * 收货备注
     */
    @Column(name = "receive_message")
    private String receiveMessage;

    /**
     * 佣金比例
     */
    @Column(name = "commission_rate")
    private int commissionRate;

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

    public Timestamp getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Timestamp delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public String toString() {
        return "Refund{" +
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
                '}';
    }
}
