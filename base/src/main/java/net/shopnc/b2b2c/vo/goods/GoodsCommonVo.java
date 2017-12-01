package net.shopnc.b2b2c.vo.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.common.util.JsonHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

/**
 * Created by shopnc.feng on 2015-12-22.
 */
public class GoodsCommonVo {
    /**
     * 商品SPU编号
     */
    private int commonId;
    /**
     * 商品SKU编号
     */
    private int goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Timestamp createTime;
    /**
     * 是否推荐
     */
    private int isCommend;
    /**
     * 商品状态<br>
     * 0下架，1正常，10违规禁售
     */
    private int goodsState;
    /**
     * 违规禁售原因
     */
    private String stateRemark;
    /**
     * 审核状态<br>
     * 0未通过，1已通过，10审核中
     */
    private int goodsVerify;
    /**
     * 审核失败原因
     */
    private String verifyRemark;
    /**
     * 图片名称
     */
    private String imageSrc;
    /**
     * 价格区间
     */
    private String priceRange;
    /**
     * 库存
     */
    private long storage;
    /**
     * 规格JSON
     */
    private List<SpecJsonVo> specJson = new ArrayList<SpecJsonVo>();
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 是否自营店铺</br>
     * 0-否 1-是
     */
    private int isOwnShop;
    
    private int isBoundActivity;
    
	/***
	 * 判断是哪种活动 1:0元购 ，2：预售 ，3：0元购+预售
	 * 
	 * */
	@Column(name = "activity_type")
	private String activityType;
	
	
	private int goodsActivityId;

    public int getGoodsActivityId() {
		return goodsActivityId;
	}

	public void setGoodsActivityId(int goodsActivityId) {
		this.goodsActivityId = goodsActivityId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public int getIsBoundActivity() {
		return isBoundActivity;
	}

	public void setIsBoundActivity(int isBoundActivity) {
		this.isBoundActivity = isBoundActivity;
	}

	public GoodsCommonVo(GoodsCommon goodsCommon, Goods goods, BigDecimal goodsPriceMax, BigDecimal goodsPriceMin, long storage) {
        this.commonId = goodsCommon.getCommonId();
        this.goodsId = goods.getGoodsId();
        this.goodsName = goodsCommon.getGoodsName();
        this.createTime = goodsCommon.getCreateTime();
        this.isCommend = goodsCommon.getIsCommend();
        this.goodsState = goodsCommon.getGoodsState();
        this.stateRemark = goodsCommon.getStateRemark();
        this.goodsVerify  = goodsCommon.getGoodsVerify();
        this.verifyRemark = goodsCommon.getVerifyRemark();
        this.imageSrc = goods.getImageSrc();
        this.priceRange = (goodsPriceMin.equals(goodsPriceMax) ? goodsPriceMax.toString() : goodsPriceMin.toString() + '~' + goodsPriceMax.toString());
        this.storage = storage;
        if (!goodsCommon.getSpecJson().equals("")) {
            List<SpecJsonVo> specJsonVoList = JsonHelper.toGenericObject(goodsCommon.getSpecJson(), new TypeReference<List<SpecJsonVo>>() {
            });
            this.specJson = specJsonVoList;
        }
    }

    public GoodsCommonVo(GoodsCommon goodsCommon, Goods goods, Store store, BigDecimal goodsPriceMax, BigDecimal goodsPriceMin, long storage) {
        this.commonId = goodsCommon.getCommonId();
        this.goodsId = goods.getGoodsId();
        this.goodsName = goodsCommon.getGoodsName();
        this.createTime = goodsCommon.getCreateTime();
        this.isCommend = goodsCommon.getIsCommend();
        this.goodsState = goodsCommon.getGoodsState();
        this.stateRemark = goodsCommon.getStateRemark();
        this.goodsVerify  = goodsCommon.getGoodsVerify();
        this.verifyRemark = goodsCommon.getVerifyRemark();
        this.imageSrc = goods.getImageSrc();
        this.priceRange = (goodsPriceMin.equals(goodsPriceMax) ? goodsPriceMax.toString() : goodsPriceMin.toString() + '~' + goodsPriceMax.toString());
        this.storage = storage;
        if (!goodsCommon.getSpecJson().equals("")) {
            List<SpecJsonVo> specJsonVoList = JsonHelper.toGenericObject(goodsCommon.getSpecJson(), new TypeReference<List<SpecJsonVo>>() {
            });
            this.specJson = specJsonVoList;
        }
        this.setStoreId(store.getStoreId());
        this.setStoreName(store.getStoreName());
        this.setIsOwnShop(store.getIsOwnShop());
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getIsCommend() {
        return isCommend;
    }

    public void setIsCommend(int isCommend) {
        this.isCommend = isCommend;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
    }

    public String getStateRemark() {
        return stateRemark;
    }

    public void setStateRemark(String stateRemark) {
        this.stateRemark = stateRemark;
    }

    public int getGoodsVerify() {
        return goodsVerify;
    }

    public void setGoodsVerify(int goodsVerify) {
        this.goodsVerify = goodsVerify;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public long getStorage() {
        return storage;
    }

    public void setStorage(long storage) {
        this.storage = storage;
    }

    public List<SpecJsonVo> getSpecJson() {
        return specJson;
    }

    public void setSpecJson(List<SpecJsonVo> specJson) {
        this.specJson = specJson;
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

    @Override
    public String toString() {
        return "GoodsCommonVo{" +
                "commonId=" + commonId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", createTime=" + createTime +
                ", isCommend=" + isCommend +
                ", goodsState=" + goodsState +
                ", stateRemark='" + stateRemark + '\'' +
                ", goodsVerify=" + goodsVerify +
                ", verifyRemark='" + verifyRemark + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", priceRange='" + priceRange + '\'' +
                ", storage=" + storage +
                ", specJson=" + specJson +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", isOwnShop=" + isOwnShop +
                 ", isBoundActivity=" + isBoundActivity +
                 ", activityType=" + activityType +
                 ", goodsActivityId=" + goodsActivityId +
                '}';
    }
}
