package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 浏览商品记录<br>
 * Created by zxy on 2016-01-27
 */
@Entity
@Table(name = "goods_browse")
public class GoodsBrowse implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="browse_id")
    private int browseId;
    /**
     * 商品ID
     */
    @Column(name="goods_id")
    private int goodsId = 0;
    /**
     * 商品SPU编号
     */
    @Column(name="common_id")
    private int commonId = 0;
    /**
     * 会员ID
     */
    @Column(name="member_id")
    private int memberId = 0;
    /**
     * 浏览时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 商品分类ID
     */
    @Column(name="goods_categoryid")
    private int goodsCategoryId = 0;
    /**
     * 商品一级分类
     */
    @Column(name="goods_categoryid_1")
    private int goodsCategoryId1 = 0;
    /**
     * 商品二级分类
     */
    @Column(name="goods_categoryid_2")
    private int goodsCategoryId2 = 0;
    /**
     * 商品三级分类
     */
    @Column(name="goods_categoryid_3")
    private int goodsCategoryId3 = 0;

    public int getBrowseId() {
        return browseId;
    }

    public void setBrowseId(int browseId) {
        this.browseId = browseId;
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getGoodsCategoryId() {
        return goodsCategoryId;
    }

    public void setGoodsCategoryId(int goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }

    public int getGoodsCategoryId1() {
        return goodsCategoryId1;
    }

    public void setGoodsCategoryId1(int goodsCategoryId1) {
        this.goodsCategoryId1 = goodsCategoryId1;
    }

    public int getGoodsCategoryId2() {
        return goodsCategoryId2;
    }

    public void setGoodsCategoryId2(int goodsCategoryId2) {
        this.goodsCategoryId2 = goodsCategoryId2;
    }

    public int getGoodsCategoryId3() {
        return goodsCategoryId3;
    }

    public void setGoodsCategoryId3(int goodsCategoryId3) {
        this.goodsCategoryId3 = goodsCategoryId3;
    }
}
