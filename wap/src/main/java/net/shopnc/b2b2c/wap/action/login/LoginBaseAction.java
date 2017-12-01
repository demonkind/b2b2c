package net.shopnc.b2b2c.wap.action.login;

import net.shopnc.b2b2c.constant.NavigationPosition;
import net.shopnc.b2b2c.dao.NavigationDao;
import net.shopnc.b2b2c.domain.Navigation;
import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.cart.CartService;
import net.shopnc.b2b2c.vo.cart.CartItemVo;
import net.shopnc.b2b2c.vo.cart.CartStoreVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxy on 2015-12-07.
 */
@Controller
public class LoginBaseAction {
    @Autowired
    private SiteService siteService;
    @Autowired
    private NavigationDao navigationDao;

    @Autowired
    @Qualifier("CartServiceDbImpl")
    private CartService cartServiceDbImpl;

    @Autowired
    @Qualifier("CartServiceCookieImpl")
    private CartService cartServiceCookieImpl;

    protected final Logger logger = Logger.getLogger(getClass());

    protected static final String LOGIN_TEMPLATE_ROOT = "tmpl/member/";

    protected String getLoginTemplate(String template) {
        return LOGIN_TEMPLATE_ROOT + template;
    }

    /**
     * freemarker 中获取全局配置
     * @return
     */
    @ModelAttribute("config")
    public Map<String, String> getConfig() {
        return siteService.getSiteInfo();
    }
    /**
     * 导航
     * @return
     */
    @ModelAttribute("navList")
    public HashMap<String,List<Navigation>> getNavigationList() {
        HashMap<String,List<Navigation>> navList = new HashMap<String, List<Navigation>>();
        List<Navigation> navigationList = navigationDao.getNavigationListByPositionId(NavigationPosition.TOP);
        navList.put(NavigationPosition.TOP_NAME,navigationList);
        navigationList = navigationDao.getNavigationListByPositionId(NavigationPosition.BODY);
        navList.put(NavigationPosition.BODY_NAME,navigationList);
        navigationList = navigationDao.getNavigationListByPositionId(NavigationPosition.FOOTER);
        navList.put(NavigationPosition.FOOTER_NAME,navigationList);
        return navList;
    }

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