package net.shopnc.b2b2c.web.action.login;

import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.cart.CartService;
import net.shopnc.b2b2c.vo.cart.CartItemVo;
import net.shopnc.b2b2c.vo.cart.CartStoreVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by zxy on 2016-03-14.
 */
@Controller
public class LoginBaseJsonAction {
    @Autowired
    @Qualifier("CartServiceDbImpl")
    private CartService cartServiceDbImpl;

    @Autowired
    @Qualifier("CartServiceCookieImpl")
    private CartService cartServiceCookieImpl;

    protected final Logger logger = Logger.getLogger(getClass());

    /**
     * 登录前加入到购物车的商品登录后存储到购物车表中
     * @param memberId
     * by hbj
     */
    protected void initCart(int memberId){
        try {
            List<CartStoreVo> cartStoreVoList = cartServiceCookieImpl.getCartStoreVoList(0);
            if (cartStoreVoList.size() > 0 ) {
                for (CartStoreVo cartStoreVo : cartStoreVoList) {
                    List<CartItemVo> cartItemVoList = cartStoreVo.getCartItemVoList();
                    for (CartItemVo cartItemVo : cartItemVoList) {
                        Cart cart = new Cart();
                        cart.setMemberId(memberId);
                        cart.setGoodsId(cartItemVo.getGoodsId());
                        cart.setBuyNum(cartItemVo.getBuyNum());
                        cartServiceDbImpl.addCart(cart);
                        cartServiceCookieImpl.clearCart(0);
                    }
                }
            }
        } catch (ShopException e) {
            logger.info(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}