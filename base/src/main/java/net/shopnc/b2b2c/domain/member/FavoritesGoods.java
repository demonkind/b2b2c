package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品关注<br>
 * Created by zxy on 2016-01-18
 */
@Entity
@Table(name = "favorites_goods")
public class FavoritesGoods implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="favorites_id")
    private int favoritesId;
    /**
     * 会员编码
     */
    @Column(name="member_id")
    private int memberId = 0;
    /**
     * 商品编码
     */
    @Column(name="goods_id")
    private int goodsId = 0;
    /**
     * 店铺编码
     */
    @Column(name="store_id")
    private int storeId = 0;
    /**
     * 商品分类ID
     */
    @Column(name="category_id")
    private int categoryId = 0;
    /**
     * 关注时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 商品关注时价格
     */
    @Column(name="fav_goods_price", length = 500)
    private BigDecimal favGoodsPrice = new BigDecimal(0);

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
}
