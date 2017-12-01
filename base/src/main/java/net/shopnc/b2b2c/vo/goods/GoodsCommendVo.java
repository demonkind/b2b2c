package net.shopnc.b2b2c.vo.goods;

import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.goods.GoodsImage;

import java.math.BigDecimal;

/**
 * Created by shopnc.feng on 2016-01-15.
 */
public class GoodsCommendVo {
    /**
     * 商品SKU编号
     */
    private int goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 商品图
     */
    private String imageSrc;
    /**
     * 查询名称、价格和图片
     * @param goods
     * @param goodsCommon
     */
    public GoodsCommendVo(Goods goods, GoodsCommon goodsCommon) {
        this.goodsId = goods.getGoodsId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.goodsPrice = goods.getGoodsPrice();
        this.imageSrc = goods.getImageSrc();
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public String toString() {
        return "GoodsCommendVo{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
