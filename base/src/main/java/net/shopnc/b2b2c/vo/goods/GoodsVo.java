package net.shopnc.b2b2c.vo.goods;

import java.sql.Timestamp;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.goods.Activity;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.goods.GoodsImage;
import net.shopnc.b2b2c.domain.goods.GoodsSale;
import net.shopnc.b2b2c.domain.store.Store;

/**
 * Created by shopnc.feng on 2015-12-11.
 */
public class GoodsVo {
    /**
     * 商品SKU编号
     */
    @Field
    private int goodsId;
    /**
     * 商品SPU
     */
    @Field
    private int commonId;
    /**
     * 商品名称
     */
    @Field
    private String goodsName;
    /**
     * 商品买点
     */
    @Field
    private String jingle;
    /**
     * 商品价格
     */
    @Field
    private Double goodsPrice;

    /**
     * 市场价格
     */
    @Field
    private Double markerPrice;
    /**
     * 销售数量
     */
    @Field
    private int goodsSaleNum;
    /**
     * 库存
     */
    @Field
    private int goodsStorage;
    /**
     * 商品状态<br>
     * 可以购买1，不可购买0
     */
    @Field
    private int goodsStatus;
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    @Field
    private int colorId;
    /**
     * 所在地
     */
    @Field
    private String areaInfo;
    /**
     * 被关注数量
     */
    @Field
    private int goodsFavorite;
    /**
     * 店铺编号
     */
    @Field
    private int storeId;
    /**
     * 店铺名称
     */
    @Field
    private String storeName;
    /**
     * 是否自营店铺</br>
     * 0-否 1-是
     */
    @Field
    private int isOwnShop;
    /**
     * 商品图
     */
    private String imageSrc;
    /**
     * 商品图片列表
     */
    private List<GoodsImage> goodsImageList;
    
    
    /**
     * 规格JSON
     */
    @Field
    private String specJson;
    /**
     * 商品编号和规格值编号JSON
     */
    @Field
    private String goodsSpecValueJson;
    /**
     * 商品分类编号
     */
    @Field
    private int categoryId;
    /**
     * 商品分类名称
     */
    @Field
    private String categoryName;
    /**
     * 评价数量
     */
    @Field
    private Integer evaluateNum;

    /**
     * 好评率
     */
    @Field
    private Integer goodsRate;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp createTime;
    /**
     * 商品一级分类
     */
    private int categoryId1;
    /**
     * 商品二级分类
     */
    private int categoryId2;
    /**
     * 商品三级分类
     */
    private int categoryId3;
    
    /******************以下为商品活动start************************/
    //活动类型
    private String activityType;
    
    //活动类型  是否活动已经开始
    private int activityIsStart;
    
    //活动开始时间
    private Timestamp startTime;
    
    //活动结束时间
    private Timestamp endTime;
    
    //返回金额
    private String returnAmount;
    
    //活动最大限购数量
    private String maxWeight;
    
  //活动最大限购数量
    private String saleWeight;
    
    // 活动ID
    private String activityId;
    
    // 商品活动ID
    private int goodsActivityId;
    
    private String weight;
	/***
	 * 限购数量
	 * 
	 * */
	private String maxNum;
	/***
	 * 卡卷类型
	 * 
	 * */
	private String cartType;
	/***
	 * 发送K码时间 0：在线支付时 1：确定收货时
	 * 
	 * */
	private String sendKCodeType;
	/***
	 * 描述
	 * 
	 * */
	private String description;
	
	/**
     * commonId 销售总数量
     */
    private int commonSaleNum;
    /**
     * commonId 库存总数量
     */
    private int commonStorage;
    
    /******************以下为商品活动end************************/
    
    public String getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(String maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

    

    public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public int getActivityIsStart() {
		return activityIsStart;
	}

	public void setActivityIsStart(int activityIsStart) {
		this.activityIsStart = activityIsStart;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public GoodsVo() {
    }
	/**
     * 商品列表页使用
     * @param goods
     * @param goodsCommon
     * @param goodsSale
     * @param store
     * @param category
     */
    public GoodsVo(Goods goods, GoodsCommon goodsCommon, GoodsSale goodsSale, Store store, Category category) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.jingle = goodsCommon.getJingle();
        this.goodsPrice = goods.getGoodsPrice().doubleValue();
        this.markerPrice = goods.getMarkerPrice().doubleValue();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.goodsSaleNum = goodsSale.getGoodsSaleNum();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.goodsFavorite = goods.getGoodsFavorite();
        this.colorId = goods.getColorId();
        this.areaInfo = goodsCommon.getAreaInfo();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.isOwnShop = store.getIsOwnShop();
        this.specJson = goodsCommon.getSpecJson();
        this.goodsSpecValueJson = goodsCommon.getGoodsSpecValueJson();
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.evaluateNum = goods.getEvaluateNum();
        this.goodsRate = goods.getGoodsRate();
        this.imageSrc = goods.getImageName();
    }
    
    /**
     * 商品活动列表页面
     * @param goods
     * @param goodsCommon
     * @param goodsSale
     * @param store
     * @param category
     */
    public GoodsVo(Goods goods, GoodsCommon goodsCommon, GoodsSale goodsSale, Store store, Category category,GoodsActivity goodsActivity) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.jingle = goodsCommon.getJingle();
        this.goodsPrice = goods.getGoodsPrice().doubleValue();
        this.markerPrice = goods.getMarkerPrice().doubleValue();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.goodsSaleNum = goodsSale.getGoodsSaleNum();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.goodsFavorite = goods.getGoodsFavorite();
        this.colorId = goods.getColorId();
        this.imageSrc = goods.getImageSrc();
        this.areaInfo = goodsCommon.getAreaInfo();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.isOwnShop = store.getIsOwnShop();
        this.specJson = goodsCommon.getSpecJson();
        this.goodsSpecValueJson = goodsCommon.getGoodsSpecValueJson();
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.evaluateNum = goods.getEvaluateNum();
        this.goodsRate = goods.getGoodsRate();
        this.startTime = goodsActivity.getStartTime();
        this.endTime = goodsActivity.getEndTime();
        this.activityType = goodsActivity.getActivityId();
        this.returnAmount = goodsActivity.getReturnAmount();
        this.saleWeight=goodsActivity.getSaleWeight();
        this.activityId = goodsActivity.getActivityId();
        this.goodsActivityId = goodsActivity.getGoodsActivityId();
        this.weight = goodsActivity.getWeight();
        this.maxNum = goodsActivity.getMaxNum();
        this.cartType = goodsActivity.getCartType();
        this.sendKCodeType = goodsActivity.getSendKCodeType();
        this.description = goodsActivity.getDescription();
    }
    
    /**
     * 商品活动列表页面
     * @param goods
     * @param goodsCommon
     * @param goodsSale
     * @param store
     * @param category
     */
    public GoodsVo(Goods goods, GoodsCommon goodsCommon, GoodsSale goodsSale, Store store, Category category,GoodsActivity goodsActivity,Activity activity) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.jingle = goodsCommon.getJingle();
        this.goodsPrice = goods.getGoodsPrice().doubleValue();
        this.markerPrice = goods.getMarkerPrice().doubleValue();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.goodsSaleNum = goodsSale.getGoodsSaleNum();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.goodsFavorite = goods.getGoodsFavorite();
        this.colorId = goods.getColorId();
        this.imageSrc = goods.getImageSrc();
        this.areaInfo = goodsCommon.getAreaInfo();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.isOwnShop = store.getIsOwnShop();
        this.specJson = goodsCommon.getSpecJson();
        this.goodsSpecValueJson = goodsCommon.getGoodsSpecValueJson();
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.evaluateNum = goods.getEvaluateNum();
        this.goodsRate = goods.getGoodsRate();
        this.startTime = goodsActivity.getStartTime();
        this.endTime = goodsActivity.getEndTime();
        this.activityType = goodsActivity.getActivityId();
        this.returnAmount = goodsActivity.getReturnAmount();
        this.saleWeight=goodsActivity.getSaleWeight();
        this.activityId = goodsActivity.getActivityId();
        this.goodsActivityId = goodsActivity.getGoodsActivityId();
        this.weight = goodsActivity.getWeight();
        this.maxNum = goodsActivity.getMaxNum();
        this.cartType = goodsActivity.getCartType();
        this.sendKCodeType = goodsActivity.getSendKCodeType();
        this.description = goodsActivity.getDescription();
    }

    public String getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(String returnAmount) {
		this.returnAmount = returnAmount;
	}

	/**
     * 查询名称、库存、价格和图片
     * @param goods
     * @param goodsSale
     */
    public GoodsVo(Goods goods, GoodsCommon goodsCommon, GoodsSale goodsSale) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.jingle = goodsCommon.getJingle();
        this.goodsPrice = goods.getGoodsPrice().doubleValue();
        this.markerPrice = goods.getMarkerPrice().doubleValue();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.goodsFavorite = goods.getGoodsFavorite();
        this.colorId = goods.getColorId();
        this.areaInfo = goodsCommon.getAreaInfo();
        this.specJson = goodsCommon.getSpecJson();
        this.goodsSpecValueJson = goodsCommon.getGoodsSpecValueJson();
        this.categoryId = goodsCommon.getCategoryId();
        this.createTime = goodsCommon.getCreateTime();
        this.storeId=goodsCommon.getStoreId();
        this.imageSrc = goods.getImageSrc();
        this.goodsStorage = goodsSale.getGoodsStorage();
    }

    /**
     * 查询名称、价格和图片
     * @param goods
     * @param goodsCommon
     */
    public GoodsVo(Goods goods, GoodsCommon goodsCommon) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.jingle = goodsCommon.getJingle();
        this.goodsPrice = goods.getGoodsPrice().doubleValue();
        this.markerPrice = goods.getMarkerPrice().doubleValue();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.goodsFavorite = goods.getGoodsFavorite();
        this.colorId = goods.getColorId();
        this.areaInfo = goodsCommon.getAreaInfo();
        this.specJson = goodsCommon.getSpecJson();
        this.goodsSpecValueJson = goodsCommon.getGoodsSpecValueJson();
        this.categoryId = goodsCommon.getCategoryId();
        this.createTime = goodsCommon.getCreateTime();
        this.imageSrc = goods.getImageSrc();
        this.categoryId1 = goodsCommon.getCategoryId1();
        this.categoryId2 = goodsCommon.getCategoryId2();
        this.categoryId3 = goodsCommon.getCategoryId3();
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

    public String getJingle() {
        return jingle;
    }

    public void setJingle(String jingle) {
        this.jingle = jingle;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Double getMarkerPrice() {
        return markerPrice;
    }

    public void setMarkerPrice(Double markerPrice) {
        this.markerPrice = markerPrice;
    }

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public int getGoodsFavorite() {
        return goodsFavorite;
    }

    public void setGoodsFavorite(int goodsFavorite) {
        this.goodsFavorite = goodsFavorite;
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

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public List<GoodsImage> getGoodsImageList() {
        return goodsImageList;
    }

    public void setGoodsImageList(List<GoodsImage> goodsImageList) {
        this.goodsImageList = goodsImageList;
    }

    public String getSpecJson() {
        return specJson;
    }

    public void setSpecJson(String specJson) {
        this.specJson = specJson;
    }

    public String getGoodsSpecValueJson() {
        return goodsSpecValueJson;
    }

    public void setGoodsSpecValueJson(String goodsSpecValueJson) {
        this.goodsSpecValueJson = goodsSpecValueJson;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    @Override
    public String toString() {
        return "GoodsVo{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", jingle='" + jingle + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", markerPrice=" + markerPrice +
                ", goodsSaleNum=" + goodsSaleNum +
                ", goodsStorage=" + goodsStorage +
                ", goodsStatus=" + goodsStatus +
                ", colorId=" + colorId +
                ", areaInfo='" + areaInfo + '\'' +
                ", goodsFavorite=" + goodsFavorite +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", isOwnShop=" + isOwnShop +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsImageList=" + goodsImageList +
                ", specJson='" + specJson + '\'' +
                ", goodsSpecValueJson='" + goodsSpecValueJson + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", evaluateNum=" + evaluateNum +
                ", goodsRate=" + goodsRate +
                ", createTime=" + createTime +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", activityType=" + activityType +
                ", activityIsStart=" + activityIsStart +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", maxWeight=" + maxWeight +
                ", returnAmount=" + returnAmount +
                ", saleWeight=" + saleWeight +
                ", commonSaleNum=" + commonSaleNum +
                ", commonStorage=" + commonStorage +
                '}';
    }

	public String getSaleWeight() {
		return saleWeight;
	}

	public void setSaleWeight(String saleWeight) {
		this.saleWeight = saleWeight;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public int getGoodsActivityId() {
		return goodsActivityId;
	}

	public void setGoodsActivityId(int goodsActivityId) {
		this.goodsActivityId = goodsActivityId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(String maxNum) {
		this.maxNum = maxNum;
	}

	public String getCartType() {
		return cartType;
	}

	public void setCartType(String cartType) {
		this.cartType = cartType;
	}

	public String getSendKCodeType() {
		return sendKCodeType;
	}

	public void setSendKCodeType(String sendKCodeType) {
		this.sendKCodeType = sendKCodeType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCommonSaleNum() {
		return commonSaleNum;
	}

	public void setCommonSaleNum(int commonSaleNum) {
		this.commonSaleNum = commonSaleNum;
	}

	public int getCommonStorage() {
		return commonStorage;
	}

	public void setCommonStorage(int commonStorage) {
		this.commonStorage = commonStorage;
	}
}
