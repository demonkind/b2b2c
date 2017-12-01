package net.shopnc.b2b2c.vo.goodsbrowse;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.member.GoodsBrowse;
import net.shopnc.common.util.ShopHelper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by zxy on 2016-01-28
 */
public class GoodsBrowseVo {
    /**
     * 自增编码
     */
    private int browseId;
    /**
     * 商品ID
     */
    private int goodsId = 0;
    /**
     * 商品SPU编号
     */
    private int commonId = 0;
    /**
     * 会员ID
     */
    private int memberId = 0;
    /**
     * 浏览时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp addTime;
    /**
     * 浏览时间文字
     */
    private String addTimeText;
    /**
     * 商品分类ID
     */
    private int goodsCategoryId = 0;
    /**
     * 商品一级分类
     */
    private int goodsCategoryId1 = 0;
    /**
     * 商品二级分类
     */
    private int goodsCategoryId2 = 0;
    /**
     * 商品三级分类
     */
    private int goodsCategoryId3 = 0;
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

    public GoodsBrowseVo() {}

    public GoodsBrowseVo(Goods g, GoodsCommon gc) {
        this.goods = g;
        this.goodsCommon = gc;
    }

    public GoodsBrowseVo(GoodsBrowse gBrowse, Goods g, GoodsCommon gc) {
        this.browseId = gBrowse.getBrowseId();
        this.goodsId = gBrowse.getGoodsId();
        this.commonId = gBrowse.getCommonId();
        this.memberId = gBrowse.getMemberId();
        this.addTime = gBrowse.getAddTime();
        this.goodsCategoryId = gBrowse.getGoodsCategoryId();
        this.goodsCategoryId1 = gBrowse.getGoodsCategoryId1();
        this.goodsCategoryId2 = gBrowse.getGoodsCategoryId2();
        this.goodsCategoryId3 = gBrowse.getGoodsCategoryId3();
        this.goods = g;
        this.goodsCommon = gc;
    }

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

    public String getAddTimeText() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String timeStr = sdf.format(this.addTime);
        if (ShopHelper.getTimestampOfDayStart(null).equals(ShopHelper.getTimestampOfDayStart(this.addTime))) {
            this.addTimeText = "今天"+timeStr;
        }else if(ShopHelper.getTimestampOfDayStart(ShopHelper.getFutureTimestamp(Calendar.DATE, -1)).equals(ShopHelper.getTimestampOfDayStart(this.addTime))){
            this.addTimeText = "昨天"+timeStr;
        }else{
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
            this.addTimeText = sdf2.format(this.addTime)+timeStr;
        }
        return addTimeText;
    }

    public void setAddTimeText(String addTimeText) {
        this.addTimeText = addTimeText;
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

    public String getGoodsName() {
        this.goodsName = this.getGoodsCommon().getGoodsName() + " " + this.getGoods().getGoodsSerial() + " " + this.getGoods().getGoodsSpecs();
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
