package net.shopnc.b2b2c.vo.goods;

import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsSale;

import java.math.BigDecimal;

/**
 * Created by shopnc.feng on 2015-12-03.
 */
public class GoodsJsonVo {
    private int goodsId;
    private String goodsSpecs;
    private String goodsFullSpecs;
    private String specValueIds;
    private BigDecimal markerPrice;
    private BigDecimal goodsPrice;
    private String goodsSerial;
    private int goodsStorage;
    private int goodsStorageAlarm;
    private String goodsBarcode;
    private int colorId;

    public GoodsJsonVo() {
    }

    public GoodsJsonVo(Goods goods, GoodsSale goodsSale) {
        this.goodsId = goods.getGoodsId();
        this.goodsSpecs = goods.getGoodsSpecs();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.specValueIds = goods.getSpecValueIds();
        this.markerPrice = goods.getMarkerPrice();
        this.goodsPrice = goods.getGoodsPrice();
        this.goodsSerial = goods.getGoodsSerial();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.goodsStorageAlarm = goodsSale.getGoodsStorageAlarm();
        this.goodsBarcode = goods.getGoodsBarcode();
        this.colorId = goods.getColorId();
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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

    public String getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }

    public BigDecimal getMarkerPrice() {
        return markerPrice;
    }

    public void setMarkerPrice(BigDecimal markerPrice) {
        this.markerPrice = markerPrice;
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

    @Override
    public String toString() {
        return "GoodsJsonVo{" +
                "goodsId=" + goodsId +
                ", goodsSpecs='" + goodsSpecs + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", specValueIds='" + specValueIds + '\'' +
                ", markerPrice=" + markerPrice +
                ", goodsPrice=" + goodsPrice +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", goodsStorage=" + goodsStorage +
                ", goodsStorageAlarm=" + goodsStorageAlarm +
                ", goodsBarcode='" + goodsBarcode + '\'' +
                ", colorId=" + colorId +
                '}';
    }
}
