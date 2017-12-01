package net.shopnc.b2b2c.domain.orders;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;
import net.shopnc.common.util.PriceHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品实体</br>
 * Created by shopnc on 2015/10/21.
 */
@Entity
@Table(name = "orders_goods")
public class OrdersGoods implements Serializable {
    /**
     * 订单商品编号</br>
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "orders_goods_id")
    private int ordersGoodsId;
    /**
     * 订单ID
     */
    @Column(name = "orders_id")
    private int ordersId;
    /**
     * 商品Id
     */
    @Column(name = "goods_id")
    private int goodsId;
    /**
     * 商品SPU
     */
    @Column(name = "common_id")
    private int commonId;
    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;
    /**
     * 商品(购买时的)单价
     */
    @Column(name = "goods_price")
    private BigDecimal goodsPrice = new BigDecimal(0);
    /**
     * (该商品)实付支付总金额
     */
    @Column(name = "goods_pay_amount")
    private BigDecimal goodsPayAmount = new BigDecimal(0);
    /**
     * 购买数量
     */
    @Column(name = "buy_num")
    private int buyNum;
    /**
     * 商品图片
     */
    @Column(name = "goods_image")
    private String goodsImage;
    /**
     * 商品类型</br>
     * 0-普通商品
     */
    @Column(name = "goods_type")
    private int goodsType = 0;
    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private int storeId;
    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private int memberId;
    /**
     * 佣金比例
     */
    @Column(name = "commission_rate")
    private int commissionRate;
    /**
     * 商品分类ID
     */
    @Column(name = "cartgory_id")
    private int categoryId;
    /**
     * 一级分类
     */
    @Column(name = "category_id_1")
    private int categoryId1;
    /**
     * 二级分类
     */
    @Column(name = "category_id_2")
    private int categoryId2;
    /**
     * 三级分类
     */
    @Column(name = "category_id_3")
    private int categoryId3;
    /**
     * 完整规格<br>
     * 例“颜色：红色，尺码：L”
     */
    @Column(name = "goods_full_specs")
    private String goodsFullSpecs;
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

    /**
     *
     * @return
     */
    public String getImageSrc() {
        if (goodsImage == null || goodsImage.equals("")) {
            return ShopConfig.getPublicRoot() + Common.DEFAULT_GOODS_IMG;
        } else {
            return ShopConfig.getUploadRoot() + goodsImage;
        }
    }

    /**
     * 收取佣金
     * @return
     */
    public BigDecimal getCommissionAmount() {
        return PriceHelper.div(PriceHelper.mul(goodsPayAmount,commissionRate),100);
    }

    @Override
    public String toString() {
        return "OrdersGoods{" +
                "ordersGoodsId=" + ordersGoodsId +
                ", ordersId=" + ordersId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsPayAmount=" + goodsPayAmount +
                ", buyNum=" + buyNum +
                ", goodsImage='" + goodsImage + '\'' +
                ", goodsType=" + goodsType +
                ", storeId=" + storeId +
                ", memberId=" + memberId +
                ", commissionRate=" + commissionRate +
                ", categoryId=" + categoryId +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                '}';
    }
}
