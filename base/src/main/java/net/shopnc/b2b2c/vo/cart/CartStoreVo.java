package net.shopnc.b2b2c.vo.cart;

import net.shopnc.common.util.PriceHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 单个店铺信息
 * Created by hbj on 2015/11/27.
 */
public class CartStoreVo {

    private List<CartItemVo> cartItemVoList;
    /**
     * 购物车总金额
     */
    private BigDecimal cartAmount;
    private String storeName;
    private int storeId;
    /**
     * 购物车商品种类数量
     */
    private int buyNum;

    public List<CartItemVo> getCartItemVoList() {
        return cartItemVoList;
    }

    public void setCartItemVoList(List<CartItemVo> cartItemVoList) {
        this.cartItemVoList = cartItemVoList;
    }

    public BigDecimal getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(BigDecimal cartAmount) {
        this.cartAmount = cartAmount;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    @Override
    public String toString() {
        return "CartStoreVo{" +
                "cartItemVoList=" + cartItemVoList +
                ", cartAmount=" + cartAmount +
                ", storeName='" + storeName + '\'' +
                ", storeId=" + storeId +
                ", buyNum=" + buyNum +
                '}';
    }


    /**
     * ************************************************************************
     * 自定义类外使用函数 块
     * ************************************************************************
     */
    public void ncSetCartAmount(List<CartItemVo> cartItemVoList) {
        if (cartItemVoList != null) {
            for (int i = 0; i < cartItemVoList.size(); i++) {
                cartAmount = PriceHelper.add(cartAmount,cartItemVoList.get(i).getItemAmount());
            }
        }
        this.cartAmount = PriceHelper.format(cartAmount);
    }
}
