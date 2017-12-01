package net.shopnc.b2b2c.vo.goods;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.goods.GoodsImage;
import net.shopnc.b2b2c.domain.goods.GoodsSale;

import java.math.BigDecimal;

/**
 * Created by shopnc.feng on 2015-12-22.
 */
public class GoodsSkuVo {
    /**
     * 商品SKU编号
     */
    private int goodsId;
    /**
     * 商品SPU
     */
    private int commonId;
    /**
     * 规格<br>
     * 例“红色 尺码”
     */
    private String goodsSpecs;
    /**
     * 完整规格<br>
     * 例“颜色：红色，尺码：L”
     */
    private String goodsFullSpecs;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 商品货号
     */
    private String goodsSerial;
    /**
     * 商品条码
     */
    private String goodsBarcode;
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId;
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 库存
     */
    private int goodsStorage;
    /**
     * 库存预警
     */
    private int goodsStorageAlarm;
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 二维码
     */
    private String goodsQRCode;

    public GoodsSkuVo(Goods goods, GoodsCommon goodsCommon, GoodsSale goodsSale) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsSpecs = goods.getGoodsSpecs();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.goodsPrice = goods.getGoodsPrice();
        this.goodsSerial = goods.getGoodsSerial();
        this.goodsBarcode = goods.getGoodsBarcode();
        this.colorId = goods.getColorId();
        this.imageSrc = goods.getImageSrc();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.storeId = goodsCommon.getStoreId();
        this.goodsStorageAlarm = goodsSale.getGoodsStorageAlarm();
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

    public String getGoodsSpecs() {
        return goodsSpecs;
    }

    public void setGoodsSpecs(String goodsSpecs) {
        this.goodsSpecs = goodsSpecs;
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

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    public String getGoodsBarcode() {
        return goodsBarcode;
    }

    public void setGoodsBarcode(String goodsBarcode) {
        this.goodsBarcode = goodsBarcode;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getGoodsStorageAlarm() {
        return goodsStorageAlarm;
    }

    public void setGoodsStorageAlarm(int goodsStorageAlarm) {
        this.goodsStorageAlarm = goodsStorageAlarm;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getGoodsQRCode() {
        return ShopConfig.getUploadRoot() + this.storeId + "/" + this.goodsId + ".gif";
    }

    public void setGoodsQRCode(String goodsQRCode) {
        this.goodsQRCode = goodsQRCode;
    }

    @Override
    public String toString() {
        return "GoodsSkuVo{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsSpecs='" + goodsSpecs + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", goodsBarcode='" + goodsBarcode + '\'' +
                ", colorId=" + colorId +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsStorage=" + goodsStorage +
                ", goodsStorageAlarm=" + goodsStorageAlarm +
                ", storeId=" + storeId +
                ", goodsQRCode='" + goodsQRCode + '\'' +
                '}';
    }
}
