package net.shopnc.b2b2c.domain.goods;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品SKU实体<br>
 * Created by shopnc.feng on 2015-10-21.
 */
@Entity
@Table(name = "goods")
public class Goods implements Serializable {
    /**
     * 商品SKU编号
     */
    @Id
    @GeneratedValue
    @Column(name = "goods_id")
    private int goodsId;
    /**
     * 商品SPU
     */
    @Min(value = 1)
    @Column(name = "common_id")
    private int commonId;
    /**
     * 规格<br>
     * 例“红色 尺码”
     */
    @Column(name = "goods_specs")
    private String goodsSpecs;
    /**
     * 完整规格<br>
     * 例“颜色：红色，尺码：L”
     */
    @Column(name = "goods_full_specs")
    private String goodsFullSpecs;
    /**
     * 所选规格值编号<br>
     * 列 “1,2,3,4”
     */
    @Column(name = "spec_value_ids")
    private String specValueIds;
    /**
     * 商品价格
     */
    @Column(name = "goods_price")
    private BigDecimal goodsPrice;
    /**
     * 市场价格
     */
    @Column(name = "marker_price")
    private BigDecimal markerPrice;
    /**
     * 商品描述
     */
    @Lob
    @Column(name = "goods_body")
    private String goodsBody;
    /**
     * 手机端描述
     */
    @Lob
    @Column(name = "mobile_body")
    private String mobileBody;
    /**
     * 商品货号
     */
    @Column(name = "goods_serial")
    private String goodsSerial;
    /**
     * 商品条码
     */
    @Column(name = "goods_barcode")
    private String goodsBarcode;
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    @Column(name = "color_id")
    private int colorId;
    /**
     * 被关注数量
     */
    @Column(name = "goods_favorite")
    private int goodsFavorite = 0;
    /**
     * 被点击数量
     */
    @Column(name = "goods_click")
    private int goodsClick = 0;
    /**
     * 评价数量
     */
    @Column(name = "evaluate_num")
    private Integer evaluateNum = 0;
    /**
     * 好评率
     */
    @Max(100)
    @Column(name = "goods_rate")
    private Integer goodsRate = 100;
    /**
     * 图片名称
     */
    @Column(name = "image_name")
    private String imageName;
    /**
     * 图片路径
     */
    @Transient
    private String imageSrc;

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

    public String getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getMarkerPrice() {
        return markerPrice;
    }

    public void setMarkerPrice(BigDecimal markerPrice) {
        this.markerPrice = markerPrice;
    }

    public String getGoodsBody() {
        return goodsBody;
    }

    public void setGoodsBody(String goodsBody) {
        this.goodsBody = goodsBody;
    }

    public String getMobileBody() {
        return mobileBody;
    }

    public void setMobileBody(String mobileBody) {
        this.mobileBody = mobileBody;
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

    public int getGoodsFavorite() {
        return goodsFavorite;
    }

    public void setGoodsFavorite(int goodsFavorite) {
        this.goodsFavorite = goodsFavorite;
    }

    public int getGoodsClick() {
        return goodsClick;
    }

    public void setGoodsClick(int goodsClick) {
        this.goodsClick = goodsClick;
    }

    public Integer getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(Integer evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public Integer getGoodsRate() {
        return goodsRate;
    }

    public void setGoodsRate(Integer goodsRate) {
        this.goodsRate = goodsRate;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageSrc() {
        if (imageName.equals("")) {
            return ShopConfig.getPublicRoot() + Common.DEFAULT_GOODS_IMG;
        } else {
            return ShopConfig.getUploadRoot() + imageName;
        }
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsSpecs='" + goodsSpecs + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", specValueIds='" + specValueIds + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", markerPrice=" + markerPrice +
                ", goodsBody='" + goodsBody + '\'' +
                ", mobileBody='" + mobileBody + '\'' +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", goodsBarcode='" + goodsBarcode + '\'' +
                ", colorId=" + colorId +
                ", goodsFavorite=" + goodsFavorite +
                ", goodsClick=" + goodsClick +
                ", evaluateNum=" + evaluateNum +
                ", goodsRate=" + goodsRate +
                ", imageName='" + imageName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
