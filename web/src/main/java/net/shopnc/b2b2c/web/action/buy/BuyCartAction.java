package net.shopnc.b2b2c.web.action.buy;

import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.service.cart.CartService;
import net.shopnc.b2b2c.service.goods.ActivityService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.vo.cart.CartItemVo;
import net.shopnc.b2b2c.vo.cart.CartStoreVo;
import net.shopnc.b2b2c.vo.cart.CartVo;
import net.shopnc.b2b2c.vo.goods.GoodsDetailVo;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.awt.Dialog.ModalExclusionType;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private GoodsActivityService goodsActivityService;

    /**
     * 购物车列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "cart/list", method = RequestMethod.GET)
    public String getCartList(ModelMap modelMap) {
        try {
            CartService cartService = SessionEntity.getIsLogin() ? cartServiceDbImpl : cartServiceCookieImpl;
            List<CartStoreVo> cartStoreVoList = cartService.getCartStoreVoList(SessionEntity.getMemberId());
            CartVo cartVo = new CartVo(cartStoreVoList);
            //购买进度坐标
            modelMap.put("buyStep", "1");

            //获取购物车中商品的状态
            for(CartStoreVo c :cartStoreVoList){
            	for(CartItemVo s : c.getCartItemVoList()){
            		List<GoodsActivity> activityGoods = goodsActivityService.findGoodsActivityById(s.getCommonId());
            		if(activityGoods.isEmpty() || activityGoods.size()>0){
            			for(GoodsActivity a : activityGoods){
            				s.setMaxNun(a.getMaxNum());
            				if(a.getStartTime() !=null && a.getEndTime() != null){
            					s.setActivityType(2);//活动进行中
            					if((a.getStartTime()).after(new Timestamp(System.currentTimeMillis()))){
            						s.setActivityType(1);//活动未开始
                				}
            					if((new Timestamp(System.currentTimeMillis())).after(a.getEndTime())){
                					s.setActivityType(3);//活动已经结束
                				}
            				}
            				
            			}
            		}

            	}
            }
            modelMap.put("cartAmount", cartVo.getCartAmount());
            modelMap.put("cartStoreVoList", cartStoreVoList);
            return getBuyTemplate(cartStoreVoList.size() > 0 ? "cart/list" : "cart/empty");
        } catch (Exception e) {
            logger.error(e.toString());
            return "redirect:/error";
        }
    }
}
