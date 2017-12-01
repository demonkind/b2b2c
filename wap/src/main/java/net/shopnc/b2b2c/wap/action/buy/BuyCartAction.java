package net.shopnc.b2b2c.wap.action.buy;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.shopnc.b2b2c.service.cart.CartService;

/**
 * 购物车
 * Created by hbj on 2015/11/30.
 */
@Controller
public class BuyCartAction extends BuyBaseAction {
    @Autowired
    @Qualifier("CartServiceDbImpl")
    private CartService cartServiceDbImpl;

    @Autowired
    @Qualifier("CartServiceCookieImpl")
    private CartService cartServiceCookieImpl;

    /**
     * 购物车列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "cart/list", method = RequestMethod.GET)
    public String getCartList(ModelMap modelMap, HttpServletRequest request) {
    	return getBuyTemplate("cart_list");
    }
}
