package net.shopnc.b2b2c.vo.goods;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.goods.*;
import net.shopnc.common.util.JsonHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-12-03.
 */
public class GoodsDetailVo {
    /**
     * 商品SKU编号
     */
    private int goodsId;
    /**
     * 商品SPU
     */
    private int commonId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品买点
     */
    private String jingle;
    /**
     * 商品分类编号
    */
    private int categoryId;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 市场价格
     */
    private BigDecimal markerPrice;
    /**
     * 二维码
     */
    private String goodsQRCode;
    /**
     * 销售数量
     */
    private int goodsSaleNum;
    /**
     * 库存
     */
    private int goodsStorage;
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 品牌编号
     */
    private int brandId;
    /**
     * 商品描述
     */
    private String goodsBody;
    /**
     * 手机端描述
     */
    private String mobileBody;
    /**
     * 商品货号
     */
    private String goodsSerial;
    /**
     * 商品条码
     */
    private String goodsBarcode;
    /**
     * 商品状态<br>
     * 可以购买1，不可购买0
     */
    private int goodsStatus;
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId;
    /**
     * 一级地区编号
     */
    private int areaId1;
    /**
     * 二级地区编号
     */
    private int areaId2;
    /**
     * 被关注数量
     */
    private int goodsFavorite;
    /**
     * 被点击数量
     */
    private int goodsClick;
    /**
     * 商品图片
     */
    private String imageSrc;
    /**
     * 所选规格值编号<br>
     * 列 “1,2,3,4”
     */
    private List<Integer> specValueIds;
    /**
     * 规格JSON
     */
    private List<SpecJsonVo> specJson = new ArrayList<SpecJsonVo>();
    /**
     * 商品编号和规格值编号JSON
     */
    private String goodsSpecValues;
    /**
     * 商品编号和规格值编号JSON
     */
    private List<GoodsSpecValueJsonVo> goodsSpecValueJson;
    /**
     * 商品图片列表
     */
    private List<GoodsImage> goodsImageList;
    /**
     * 商品参数
     */
    private List<GoodsAttrVo> goodsAttrList;
    /**
     * 顶部关联板式编号
     */
    private int formatTop;
    /**
     * 底部关联板式编号
     */
    private int formatBottom;
    
    //活动类型
    private int activityType;
    
    //活动类型  是否活动已经开始
    private int activityIsStart;
    
    //活动结束时间
    private Timestamp endTime;
    
  //活动开始时间
    private Timestamp startTime;
    //活动最大限购数量(动态数据)
    private String maxWeight;
      
    //活动最大限购数量(静态数据)
    private String maxBuyNum;
    
    //返现金额
    private String returnAmount;
    
    //

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

    public GoodsDetailVo(Goods goods, GoodsCommon goodsCommon, GoodsSale goodsSale) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.jingle = goodsCommon.getJingle();
        this.categoryId = goodsCommon.getCategoryId();
        this.goodsPrice = goods.getGoodsPrice();
        this.markerPrice = goods.getMarkerPrice();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.goodsSaleNum = goodsSale.getGoodsSaleNum();
        this.storeId = goodsCommon.getStoreId();
        this.brandId = goodsCommon.getBrandId();
        this.goodsBody = (goods.getGoodsBody() == null || goods.getGoodsBody().length() == 0) ? goodsCommon.getGoodsBody() : goods.getGoodsBody();
        this.mobileBody = (goods.getMobileBody() == null || goods.getMobileBody().length() == 0) ? goodsCommon.getMobileBody() : goods.getMobileBody();
        this.goodsSerial = goods.getGoodsSerial();
        this.goodsBarcode = goods.getGoodsBarcode();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.areaId1 = goodsCommon.getAreaId1();
        this.areaId2 = goodsCommon.getAreaId2();
        this.colorId = goods.getColorId();
        this.goodsFavorite = goods.getGoodsFavorite();
        this.goodsClick = goods.getGoodsClick();
        this.formatTop = goodsCommon.getFormatTop();
        this.formatBottom = goodsCommon.getFormatBottom();
        List<Integer> integers = new ArrayList<Integer>();
        if (!goods.getSpecValueIds().equals("")) {
            String[] specValueIds = goods.getSpecValueIds().split(",");
            for (String specValueId : specValueIds) {
                integers.add(Integer.parseInt(specValueId));
            }
        }
        this.specValueIds = integers;
        this.goodsSpecValues = goodsCommon.getGoodsSpecValueJson();
        if (!goodsCommon.getSpecJson().equals("")) {
            List<SpecJsonVo> specJsonVoList = JsonHelper.toGenericObject(goodsCommon.getSpecJson(), new TypeReference<List<SpecJsonVo>>() {
            });
            this.specJson = specJsonVoList;
        }
        List<GoodsSpecValueJsonVo> goodsSpecValueJsonVoList = new ArrayList<GoodsSpecValueJsonVo>();
        if (!goodsCommon.getSpecJson().equals("")) {
            List<GoodsValueJsonVo> goodsValueJsonVoList = JsonHelper.toGenericObject(goodsCommon.getGoodsSpecValueJson(), new TypeReference<List<GoodsValueJsonVo>>() {
            });
            if(goodsValueJsonVoList!=null){
	            for (GoodsValueJsonVo goodsValueJsonVo : goodsValueJsonVoList) {
	                GoodsSpecValueJsonVo goodsSpecValueJsonVo = new GoodsSpecValueJsonVo();
	                goodsSpecValueJsonVo.setGoodsId(goodsValueJsonVo.getGoodsId());
	                String[] ids = goodsValueJsonVo.getSpecValueIds().split(",");
	                List<Integer> integerList = new ArrayList<Integer>();
	                for (String specValueId : ids) {
	                    integerList.add(Integer.parseInt(specValueId));
	                }
	                Collections.sort(integerList);
	                goodsSpecValueJsonVo.setSpecValueIds(integerList);
	                goodsSpecValueJsonVoList.add(goodsSpecValueJsonVo);
	            }
            }
        this.goodsSpecValueJson = goodsSpecValueJsonVoList;
        }
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsQRCode() {
        return ShopConfig.getUploadRoot() + this.storeId + "/" + this.goodsId + ".gif";
    }

    public void setGoodsQRCode(String goodsQRCode) {
        this.goodsQRCode = goodsQRCode;
    }

    public BigDecimal getMarkerPrice() {
        return markerPrice;
    }

    public void setMarkerPrice(BigDecimal markerPrice) {
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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
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

    public int getAreaId1() {
        return areaId1;
    }

    public void setAreaId1(int areaId1) {
        this.areaId1 = areaId1;
    }

    public int getAreaId2() {
        return areaId2;
    }

    public void setAreaId2(int areaId2) {
        this.areaId2 = areaId2;
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

    public int getFormatTop() {
        return formatTop;
    }

    public void setFormatTop(int formatTop) {
        this.formatTop = formatTop;
    }

    public int getFormatBottom() {
        return formatBottom;
    }

    public void setFormatBottom(int formatBottom) {
        this.formatBottom = formatBottom;
    }

    public String getImageSrc() {
        if ( imageSrc==null) {
            return ShopConfig.getPublicRoot() + Common.DEFAULT_GOODS_IMG;
        } else {
            return imageSrc;
        }
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public List<Integer> getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(List<Integer> specValueIds) {
        this.specValueIds = specValueIds;
    }

    public List<SpecJsonVo> getSpecJson() {
        return specJson;
    }

    public void setSpecJson(List<SpecJsonVo> specJson) {
        this.specJson = specJson;
    }

    public String getGoodsSpecValues() {
        return goodsSpecValues;
    }

    public void setGoodsSpecValues(String goodsSpecValues) {
        this.goodsSpecValues = goodsSpecValues;
    }

    public List<GoodsSpecValueJsonVo> getGoodsSpecValueJson() {
        return goodsSpecValueJson;
    }

    public void setGoodsSpecValueJson(List<GoodsSpecValueJsonVo> goodsSpecValueJson) {
        this.goodsSpecValueJson = goodsSpecValueJson;
    }

    public List<GoodsImage> getGoodsImageList() {
        return goodsImageList;
    }

    public void setGoodsImageList(List<GoodsImage> goodsImageList) {
        this.goodsImageList = goodsImageList;
    }

    public List<GoodsAttrVo> getGoodsAttrList() {
        return goodsAttrList;
    }

    public void setGoodsAttrList(List<GoodsAttrVo> goodsAttrList) {
        this.goodsAttrList = goodsAttrList;
    }

    @Override
    public String toString() {
        return "GoodsDetailVo{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", jingle='" + jingle + '\'' +
                ", categoryId=" + categoryId +
                ", goodsPrice=" + goodsPrice +
                ", markerPrice=" + markerPrice +
                ", goodsQRCode='" + goodsQRCode + '\'' +
                ", goodsSaleNum=" + goodsSaleNum +
                ", goodsStorage=" + goodsStorage +
                ", storeId=" + storeId +
                ", brandId=" + brandId +
                ", goodsBody='" + goodsBody + '\'' +
                ", mobileBody='" + mobileBody + '\'' +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", goodsBarcode='" + goodsBarcode + '\'' +
                ", goodsStatus=" + goodsStatus +
                ", colorId=" + colorId +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", goodsFavorite=" + goodsFavorite +
                ", goodsClick=" + goodsClick +
                ", imageSrc='" + imageSrc + '\'' +
                ", specValueIds=" + specValueIds +
                ", specJson=" + specJson +
                ", goodsSpecValues='" + goodsSpecValues + '\'' +
                ", goodsSpecValueJson=" + goodsSpecValueJson +
                ", goodsImageList=" + goodsImageList +
                ", goodsAttrList=" + goodsAttrList +
                ", formatTop=" + formatTop +
                ", formatBottom=" + formatBottom +
                 ", activityType=" + activityType +
                ", activityIsStart=" + activityIsStart +
                ", endTime=" + endTime +
                 ", startTime=" + startTime +
                ", maxWeight=" + maxWeight +
                ", description=" + description +
                ", returnAmount=" + returnAmount +
                ", commonSaleNum=" + commonSaleNum +
                ", commonStorage=" + commonStorage +
                ", maxBuyNum=" + maxBuyNum +
            
                '}';
    }

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
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


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(String returnAmount) {
		this.returnAmount = returnAmount;
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

	public String getMaxBuyNum() {
		return maxBuyNum;
	}

	public void setMaxBuyNum(String maxBuyNum) {
		this.maxBuyNum = maxBuyNum;
	}
}
