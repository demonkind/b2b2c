package net.shopnc.b2b2c.vo.cart;

import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.goods.GoodsImage;
import net.shopnc.b2b2c.domain.goods.GoodsSale;
import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.common.util.PriceHelper;

import java.math.BigDecimal;

/**
 * 单条商品信息
 * Created by hbj on 2015/11/27.
 */
public class CartItemVo {

	/**
     * 购物车Id
     */
    private int cartId;
    /**
     * 商品Id
     */
    private int goodsId;
    /**
     * 规格ID
     */
    private int commonId;
    /**
     * 商品名
     */
    private String goodsName;
    /**
     * 规格
     */
    private String goodsFullSpecs;
    /**
     * 单价
     */
    private BigDecimal goodsPrice;
    /**
     * 商品图
     */
    private String imageName;
    /**
     * 购买数量
     */
    private int buyNum;
    /**
     * 商品小计[单价 * 数量]
     */
    private BigDecimal itemAmount;
    /**
     * 商品库存
     */
    private int goodsStorage;
    /**
     * 商品状态
     */
    private int goodsStatus;
    /**
     * 店铺ID
     */
    private int storeId;
    /**
     * 店铺名
     */
    private String storeName;
    /**
     * 商品库存是否足够
     */
    private int storageStatus;
    /**
     * 会员ID
     */
    private int memberId;
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 活动是否正在进行： 1为进行中;
     */
    private int activityType;
    /**
     * 限购数量
     */
    private String maxNun;
    
    
	public CartItemVo(){}
    public CartItemVo(Cart cart, Goods goods, GoodsCommon goodsCommon, GoodsImage goodsImage, GoodsSale goodsSale, Store store) {
        this.cartId = cart.getCartId();
        this.buyNum = cart.getBuyNum();
        this.goodsId = goods.getGoodsId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.goodsPrice = goods.getGoodsPrice();
        this.imageName = goodsImage.getImageName();
        this.imageSrc = goodsImage.getImageSrc();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.memberId = cart.getMemberId();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.itemAmount = PriceHelper.mul(this.goodsPrice, this.buyNum);
        this.storageStatus = this.goodsStorage >= this.buyNum ? 1 : 0;
        this.commonId = goods.getCommonId();
        this.activityType = 0;
        this.maxNun = "";
    }

    public CartItemVo( Goods goods, GoodsCommon goodsCommon, GoodsImage goodsImage, GoodsSale goodsSale, Store store) {
        this.cartId = goods.getGoodsId();
        this.goodsId = goods.getGoodsId();
        this.goodsName = goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.goodsPrice = goods.getGoodsPrice();
        this.imageName = goodsImage.getImageName();
        this.imageSrc = goodsImage.getImageSrc();
        this.goodsStorage = goodsSale.getGoodsStorage();
        this.goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.storageStatus = this.goodsStorage >= this.buyNum ? 1 : 0;
        this.commonId = goods.getCommonId();
        this.activityType = 0;
        this.maxNun = "";
    }

    public int getCommonId() {
		return commonId;
	}
	public void setCommonId(int commonId) {
		this.commonId = commonId;
	}
	public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
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

    public int getStorageStatus() {
        return storageStatus;
    }

    public void setStorageStatus(int storageStatus) {
        this.storageStatus = storageStatus;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getActivityType() {
		return activityType;
	}
	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}
	 public String getMaxNun() {
			return maxNun;
	}
	public void setMaxNun(String string) {
		this.maxNun = string;
	}

    @Override
    public String toString() {
        return "CartItemVo{" +
                "cartId=" + cartId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", imageName='" + imageName + '\'' +
                ", buyNum=" + buyNum +
                ", itemAmount=" + itemAmount +
                ", goodsStorage=" + goodsStorage +
                ", goodsStatus=" + goodsStatus +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storageStatus=" + storageStatus +
                ", memberId=" + memberId +
                ", imageSrc='" + imageSrc + '\'' +
                ", activityType='" + activityType +
                ", maxNun='" + maxNun + '\'' +
                '}';
    }
}
