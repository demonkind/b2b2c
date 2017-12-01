package net.shopnc.b2b2c.service.cart;

import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.vo.cart.CartStoreVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车接口
 * Created by hbj on 2015/12/2.
 */
public interface CartService {
    List<CartStoreVo> addCart(Cart cart) throws ShopException;
    List<CartStoreVo> getCartStoreVoList(int memberId);
    void editCart(int cartId, int buyNum, int memberId) throws ShopException;
    void delCart(int cartId, int memberId) throws ShopException;
    void clearCart(int memberId);
    
    List<CartStoreVo> getCartStoreVoListForWap(String goodsInfo);
    
}
