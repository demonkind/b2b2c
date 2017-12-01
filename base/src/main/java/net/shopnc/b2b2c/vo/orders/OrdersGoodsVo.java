package net.shopnc.b2b2c.vo.orders;

import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 订单商品
 * Created by hbj on 2015/12/30.
 */
public class OrdersGoodsVo {
    /**
     * 订单商品编号
     */
    private int ordersGoodsId;
    /**
     * 订单ID
     */
    private int ordersId;
    /**
     * 商品Id
     */
    private int goodsId;
    /**
     * 商品SPU
     */
    private int commonId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品(购买时的)单价
     */
    private BigDecimal goodsPrice;
    /**
     * (该商品)实付支付总金额
     */
    private BigDecimal goodsPayAmount;
    /**
     * 购买数量
     */
    private int buyNum;
    /**
     * 商品图片
     */
    private String goodsImage;
    /**
     * 商品图片带URL
     */
    private String imageSrc;
    /**
     * 商品类型</br>
     */
    private int goodsType = 0;
    /**
     * 店铺ID
     */
    private int storeId;
    /**
     * 会员ID
     */
    private int memberId;
    /**
     * 佣金比例
     */
    private int commissionRate;
    /**
     * 商品分类ID
     */
    private int categoryId;
    /**
     * 完整规格
     */
    private String goodsFullSpecs;
    /**
     * 佣金金额
     */
    private BigDecimal commissionAmount;
    /**
     * 会员名称
     */
    private String memberName;
    /**
     * 下单时间
     */
    private Timestamp createTime;
    /**
     * 一级分类
     */
    private int categoryId1;
    /**
     * 二级分类
     */
    private int categoryId2;
    /**
     * 三级分类
     */
    private int categoryId3;

    /**
     * cj[ 商品是否显示退款]
     * 1.显示 ， 2 不显示
     */
    private int showRefund = 0 ;

    /**
     * 已有退款记录时显示退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 已有退款记录时显示是否显示"查看退款详情"按钮
     */
    private int showRefundInfo= 0;
    /**
     *  已有退款记录时显示显示"查看退款还是退货详情"按钮
     */
    private int refundType;

    /**
     * 退款id
     */
    private int refundId ;

    /**
     * 退款单号
     */
    private long refundSn;

    public OrdersGoodsVo() {}
    public OrdersGoodsVo(OrdersGoods ordersGoods) {
        this.ordersGoodsId = ordersGoods.getOrdersGoodsId();
        this.ordersId = ordersGoods.getOrdersId();
        this.goodsId = ordersGoods.getGoodsId();
        this.commonId = ordersGoods.getCommonId();
        this.goodsName = ordersGoods.getGoodsName();
        this.goodsPrice = ordersGoods.getGoodsPrice();
        this.goodsPayAmount = ordersGoods.getGoodsPayAmount();
        this.buyNum = ordersGoods.getBuyNum();
        this.goodsImage = ordersGoods.getGoodsImage();
        this.imageSrc = ordersGoods.getImageSrc();
        this.goodsType = ordersGoods.getGoodsType();
        this.storeId = ordersGoods.getStoreId();
        this.memberId = ordersGoods.getMemberId();
        this.commissionRate = ordersGoods.getCommissionRate();
        this.categoryId = ordersGoods.getCategoryId();
        this.goodsFullSpecs = ordersGoods.getGoodsFullSpecs();
        this.commissionAmount = ordersGoods.getCommissionAmount();
        this.categoryId1 = ordersGoods.getCategoryId1();
        this.categoryId2 = ordersGoods.getCategoryId2();
        this.categoryId3 = ordersGoods.getCategoryId3();


    }
    public OrdersGoodsVo(OrdersGoods ordersGoods, Orders orders) {
        this.ordersGoodsId = ordersGoods.getOrdersGoodsId();
        this.ordersId = ordersGoods.getOrdersId();
        this.goodsId = ordersGoods.getGoodsId();
        this.commonId = ordersGoods.getCommonId();
        this.goodsName = ordersGoods.getGoodsName();
        this.goodsPrice = ordersGoods.getGoodsPrice();
        this.goodsPayAmount = ordersGoods.getGoodsPayAmount();
        this.buyNum = ordersGoods.getBuyNum();
        this.goodsImage = ordersGoods.getGoodsImage();
        this.imageSrc = ordersGoods.getImageSrc();
        this.goodsType = ordersGoods.getGoodsType();
        this.storeId = ordersGoods.getStoreId();
        this.memberId = ordersGoods.getMemberId();
        this.commissionRate = ordersGoods.getCommissionRate();
        this.categoryId = ordersGoods.getCategoryId();
        this.goodsFullSpecs = ordersGoods.getGoodsFullSpecs();
        this.commissionAmount = ordersGoods.getCommissionAmount();
        this.memberName = orders.getMemberName();
        this.createTime = orders.getCreateTime();
        this.categoryId1 = ordersGoods.getCategoryId1();
        this.categoryId2 = ordersGoods.getCategoryId2();
        this.categoryId3 = ordersGoods.getCategoryId3();
    }

    public int getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(int ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getGoodsPayAmount() {
        return goodsPayAmount;
    }

    public void setGoodsPayAmount(BigDecimal goodsPayAmount) {
        this.goodsPayAmount = goodsPayAmount;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public int getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(int categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public int getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(int categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public int getShowRefund() {
        return showRefund;
    }

    public void setShowRefund(int showRefund) {
        this.showRefund = showRefund;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getShowRefundInfo() {
        return showRefundInfo;
    }

    public void setShowRefundInfo(int showRefundInfo) {
        this.showRefundInfo = showRefundInfo;
    }

    public int getRefundType() {
        return refundType;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
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

    @Override
    public String toString() {
        return "OrdersGoodsVo{" +
                "ordersGoodsId=" + ordersGoodsId +
                ", ordersId=" + ordersId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsPayAmount=" + goodsPayAmount +
                ", buyNum=" + buyNum +
                ", goodsImage='" + goodsImage + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsType=" + goodsType +
                ", storeId=" + storeId +
                ", memberId=" + memberId +
                ", commissionRate=" + commissionRate +
                ", categoryId=" + categoryId +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", commissionAmount=" + commissionAmount +
                ", memberName='" + memberName + '\'' +
                ", createTime=" + createTime +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", showRefund=" + showRefund +
                ", refundAmount=" + refundAmount +
                ", showRefundInfo=" + showRefundInfo +
                ", refundType=" + refundType +
                ", refundId=" + refundId +
                ", refundSn=" + refundSn +
                '}';
    }
}
