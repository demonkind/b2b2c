package net.shopnc.b2b2c.vo.favorites;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.member.FavoritesGoods;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by zxy on 2016-01-19
 */
public class FavoritesGoodsVo {
    /**
     * 自增编码
     */
    private int favoritesId;
    /**
     * 会员编码
     */
    private int memberId = 0;
    /**
     * 商品编码
     */
    private int goodsId = 0;
    /**
     * 店铺编码
     */
    private int storeId = 0;
    /**
     * 关注时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp addTime;
    /**
     * 商品关注时价格
     */
    private BigDecimal favGoodsPrice = new BigDecimal(0);
    /**
     * 商品名称
     */
    private String goodsName = "";
    /**
     * 商品SKU
     */
    private Goods goods;
    /**
     * 商品SPU
     */
    private GoodsCommon goodsCommon;


    public FavoritesGoodsVo(FavoritesGoods fg, Goods g, GoodsCommon gc) {
        this.favoritesId = fg.getFavoritesId();
        this.memberId = fg.getMemberId();
        this.goodsId = fg.getGoodsId();
        this.storeId = fg.getStoreId();
        this.addTime = fg.getAddTime();
        this.favGoodsPrice = fg.getFavGoodsPrice();
        this.goodsName = gc.getGoodsName() + " " + g.getGoodsSerial() + " " + g.getGoodsSpecs();
        this.goods = g;
        this.goodsCommon = gc;
    }

    public int getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(int favoritesId) {
        this.favoritesId = favoritesId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public BigDecimal getFavGoodsPrice() {
        return favGoodsPrice;
    }

    public void setFavGoodsPrice(BigDecimal favGoodsPrice) {
        this.favGoodsPrice = favGoodsPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsCommon getGoodsCommon() {
        return goodsCommon;
    }

    public void setGoodsCommon(GoodsCommon goodsCommon) {
        this.goodsCommon = goodsCommon;
    }
}
