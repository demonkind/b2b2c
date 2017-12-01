package net.shopnc.b2b2c.vo.cart;

import net.shopnc.common.util.PriceHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车整体统计信息
 * Created by hbj on 2015/12/15.
 */
public class CartVo {
    /**
     * 购买数量
     */
    private int buyNum;
    /**
     * 购物车总金额
     */
    private BigDecimal cartAmount;

    public CartVo(){}
    public CartVo(List<CartStoreVo> cartStoreVoList){
        for(int i=0; i<cartStoreVoList.size(); i++) {
            this.cartAmount = PriceHelper.add(this.cartAmount,cartStoreVoList.get(i).getCartAmount());
            this.buyNum += cartStoreVoList.get(i).getBuyNum();
        }
        this.cartAmount = PriceHelper.format(this.cartAmount);
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public BigDecimal getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(BigDecimal cartAmount) {
        this.cartAmount = cartAmount;
    }

    @Override
    public String toString() {
        return "CartVo{" +
                "buyNum=" + buyNum +
                ", cartAmount=" + cartAmount +
                '}';
    }
}
