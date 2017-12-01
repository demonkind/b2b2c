package net.shopnc.common.entity.buy.postjson;

/**
 * Created by hbj on 2015/12/29.
 */
public class PostJsonGoodsEntity {
    private Integer cartId;
    private Integer goodsId;
    private Integer buyNum;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    @Override
    public String toString() {
        return "PostJsonGoodsEntity{" +
                "cartId=" + cartId +
                ", goodsId=" + goodsId +
                ", buyNum=" + buyNum +
                '}';
    }
}
