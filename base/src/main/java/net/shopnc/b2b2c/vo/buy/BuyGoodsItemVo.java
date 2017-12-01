package net.shopnc.b2b2c.vo.buy;

import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.goods.GoodsImage;
import net.shopnc.b2b2c.domain.goods.GoodsSale;
import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.common.util.PriceHelper;

import java.math.BigDecimal;

/**
 * 存放下单时单个商品信息<br/>
 * Created by hbj on 2015/11/27.
 */
public class BuyGoodsItemVo {
    /**
     * 购物车ID[如果直接购买此项不赋值]
     */
    private int cartId;
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
     * 商品规格
     */
    private String goodsFullSpecs;
    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;
    /**
     * 商品图
     */
    private String imageName;
    /**
     * 购买数量
     */
    private int buyNum;
    /**
     * 商品小计[单价 * 数量]
     */
    private BigDecimal itemAmount;
    /**
     * 商品运费
     */
    private BigDecimal goodsFreight;
    /**
     * 商品库存
     */
    private int goodsStorage;
    /**
     * 商品分类
     */
    private int categoryId;
    /**
     * 商品状态
     */
    private int goodsStatus;
    /**
     * 店铺ID
     */
    private int storeId;
    /**
     * 店铺名
     */
    private String storeName;
    /**
     * 商品库存是否足够
     */
    private int storageStatus;
    /**
     * 运费模板ID
     */
    private int freightTemplateId;
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 是否支持配送0否1是
     */
    private int allowSend = State.YES;
    /**
     * 商品重量
     */
    private BigDecimal freightWeight;
    /**
     * 商品体积
     */
    private BigDecimal freightVolume;

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
     * 是否使用增值税发票
     */
    private int allowVat;
    /**
     * 是否自营
     */
    private int isOwnShop;
    /**
     * 是否到达库存预警
     */
    private int isGoodsStorageAlarm = 0;

    public BuyGoodsItemVo(){}

    /**
     * 来源购物车
     * @param cart
     * @param goods
     * @param goodsCommon
     * @param goodsImage
     * @param goodsSale
     * @param store
     */
    public BuyGoodsItemVo(Cart cart, Goods goods, GoodsCommon goodsCommon, GoodsImage goodsImage, GoodsSale goodsSale, Store store) {
        this.cartId = cart.getCartId();
        this.goodsId = goods.getGoodsId();
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.goodsPrice = goods.getGoodsPrice();
        this.buyNum = cart.getBuyNum();
        this.imageName = goodsImage.getImageName();
        this.imageSrc = goodsImage.getImageSrc();
        this.goodsFreight = goodsCommon.getGoodsFreight();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.categoryId = goodsCommon.getCategoryId();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.freightTemplateId = goodsCommon.getFreightTemplateId();
        this.itemAmount = PriceHelper.mul(goodsPrice, buyNum);
        this.storageStatus = goodsStorage >= buyNum ? State.YES : State.NO;
        this.freightWeight = goodsCommon.getFreightWeight();
        this.freightVolume = goodsCommon.getFreightVolume();
        this.categoryId1 = goodsCommon.getCategoryId1();
        this.categoryId2 = goodsCommon.getCategoryId2();
        this.categoryId3 = goodsCommon.getCategoryId3();
        this.allowVat = goodsCommon.getAllowVat();
        this.isOwnShop = store.getIsOwnShop();
    }

    /**
     * 直接购买
     * @param goods
     * @param goodsCommon
     * @param goodsImage
     * @param goodsSale
     * @param store
     */
    public BuyGoodsItemVo(Goods goods, GoodsCommon goodsCommon, GoodsImage goodsImage, GoodsSale goodsSale, Store store) {
        this.cartId = goods.getGoodsId();
        this.goodsId = goods.getGoodsId();
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.goodsPrice = goods.getGoodsPrice();
        this.imageName = goodsImage.getImageName();
        this.imageSrc = goodsImage.getImageSrc();
        this.goodsFreight = goodsCommon.getGoodsFreight();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.categoryId = goodsCommon.getCategoryId();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.freightTemplateId = goodsCommon.getFreightTemplateId();
        this.itemAmount = PriceHelper.mul(goodsPrice, buyNum);
        this.freightWeight = goodsCommon.getFreightWeight();
        this.freightVolume = goodsCommon.getFreightVolume();
        this.categoryId1 = goodsCommon.getCategoryId1();
        this.categoryId2 = goodsCommon.getCategoryId2();
        this.categoryId3 = goodsCommon.getCategoryId3();
        this.allowVat = goodsCommon.getAllowVat();
        this.isOwnShop = store.getIsOwnShop();
        //[this.storageStatus]直接购买不在此处判断库存是否足够，设置购买量，得到buyGoodsItemVoList后，结合cartPostEntity记录的购买数量判断，设置购买数量
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }

    public BigDecimal getGoodsFreight() {
        return goodsFreight;
    }

    public void setGoodsFreight(BigDecimal goodsFreight) {
        this.goodsFreight = goodsFreight;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
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

    public int getStorageStatus() {
        return storageStatus;
    }

    public void setStorageStatus(int storageStatus) {
        this.storageStatus = storageStatus;
    }

    public int getFreightTemplateId() {
        return freightTemplateId;
    }

    public void setFreightTemplateId(int freightTemplateId) {
        this.freightTemplateId = freightTemplateId;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getAllowSend() {
        return allowSend;
    }

    public void setAllowSend(int allowSend) {
        this.allowSend = allowSend;
    }

    public BigDecimal getFreightWeight() {
        return freightWeight;
    }

    public void setFreightWeight(BigDecimal freightWeight) {
        this.freightWeight = freightWeight;
    }

    public BigDecimal getFreightVolume() {
        return freightVolume;
    }

    public void setFreightVolume(BigDecimal freightVolume) {
        this.freightVolume = freightVolume;
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

    public int getAllowVat() {
        return allowVat;
    }

    public void setAllowVat(int allowVat) {
        this.allowVat = allowVat;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public int getIsGoodsStorageAlarm() {
        return isGoodsStorageAlarm;
    }

    public void setIsGoodsStorageAlarm(int isGoodsStorageAlarm) {
        this.isGoodsStorageAlarm = isGoodsStorageAlarm;
    }

    @Override
    public String toString() {
        return "BuyGoodsItemVo{" +
                "cartId=" + cartId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", imageName='" + imageName + '\'' +
                ", buyNum=" + buyNum +
                ", itemAmount=" + itemAmount +
                ", goodsFreight=" + goodsFreight +
                ", goodsStorage=" + goodsStorage +
                ", categoryId=" + categoryId +
                ", goodsStatus=" + goodsStatus +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storageStatus=" + storageStatus +
                ", freightTemplateId=" + freightTemplateId +
                ", imageSrc='" + imageSrc + '\'' +
                ", allowSend=" + allowSend +
                ", freightWeight=" + freightWeight +
                ", freightVolume=" + freightVolume +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", allowVat=" + allowVat +
                ", isOwnShop=" + isOwnShop +
                ", isGoodsStorageAlarm=" + isGoodsStorageAlarm +
                '}';
    }
}
