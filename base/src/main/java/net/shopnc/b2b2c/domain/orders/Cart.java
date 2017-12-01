package net.shopnc.b2b2c.domain.orders;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 购物车实体</br>
 * Created by shopnc on 2015/10/20.
 */
@Entity
@Table(name="cart")
public class Cart implements Serializable {
    /**
     * 购物车编号</br>
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private int cartId;
    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private int memberId;
    /**
     * 商品(sku)ID
     */
    @Min(1)
    @Column(name = "goods_id")
    private int goodsId;
    /**
     * 购买数量
     */
    @Min(1)
    @Column(name = "buy_num")
    private int buyNum;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", memberId=" + memberId +
                ", goodsId=" + goodsId +
                ", buyNum=" + buyNum +
                '}';
    }
}
