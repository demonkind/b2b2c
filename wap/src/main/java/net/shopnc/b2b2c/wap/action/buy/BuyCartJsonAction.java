package net.shopnc.b2b2c.wap.action.buy;

import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.cart.CartService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.vo.cart.CartItemVo;
import net.shopnc.b2b2c.vo.cart.CartStoreVo;
import net.shopnc.b2b2c.vo.cart.CartVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * 购物车
 * Created by hou on 2016/3/14.
 */
@Controller
public class BuyCartJsonAction extends BuyBaseJsonAction {
    @Autowired
    @Qualifier("CartServiceDbImpl")
    private CartService cartServiceDbImpl;
    @Autowired
    private GoodsActivityService goodsActivityService;

    @Autowired
    @Qualifier("CartServiceCookieImpl")
    private CartService cartServiceCookieImpl;
    private CartService getCartService() {
        return SessionEntity.getIsLogin() ? cartServiceDbImpl : cartServiceCookieImpl;
    }

    /**
     * 获取购物车json数据
     * by cj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cart/jsonlist", method =  RequestMethod.GET)
    public ResultEntity getCartListJson(){
        ResultEntity resultEntity = new ResultEntity();
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("login", SessionEntity.getIsLogin());
            CartService cartService = getCartService();
            List<CartStoreVo> cartStoreVoList = cartService.getCartStoreVoList(SessionEntity.getMemberId());
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
            map.put("cart_list", cartStoreVoList);
            resultEntity.setData(map);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
    
    /**
     * 加入购物车
     * @param cart
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cart/add", method =  RequestMethod.POST)
    public ResultEntity add(HttpServletRequest request) {
    	Cart cart=new Cart();
    	String key=request.getParameter("key");
    	String goodsId=request.getParameter("goodsId");
    	String buyNum=request.getParameter("buyNum");
    	cart.setBuyNum(Integer.valueOf(buyNum));
    	cart.setMemberId(Integer.valueOf(key));
    	cart.setGoodsId(Integer.valueOf(goodsId));
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(getMemberBuyRoot() + "cart/list");
        resultEntity.setCode(ResultEntity.FAIL);
        try {
        	if(key != null && !"".equals(key)){
        		cart.setMemberId(Integer.valueOf(key));
                List<CartStoreVo> cartStoreVoList = cartServiceDbImpl.addCart(cart);
                CartVo cartVo = new CartVo(cartStoreVoList);
                resultEntity.setData(cartVo);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("添加成功");
        	} else{
        		resultEntity.setCode(ResultEntity.FAIL);
        		resultEntity.setMessage("未登录");
        	}
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setMessage("添加失败");
        }
        return resultEntity;
    }

    /**
     * 更新购物车数量
     * @param cartId
     * @param buyNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cart/edit", method = RequestMethod.POST)
    public ResultEntity edit(@RequestParam(value = "cartId" ,required = true) int cartId,
                             @RequestParam(value = "buyNum", required = true) int buyNum) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(getMemberBuyRoot() + "cart/list");
        try {
        	HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("login", SessionEntity.getIsLogin());
            CartService cartService = getCartService();
            cartService.editCart(cartId,buyNum,SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("更新成功");
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            if (e.getExtendData() != null) {
                resultEntity.setData(e.getExtendData());
            }
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
        }

        return resultEntity;
    }

    /**
     * 删除
     * @param cartId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cart/del", method = RequestMethod.POST)
    public ResultEntity del(String cartId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(getMemberBuyRoot() + "cart/list");
        try {
            CartService cartService = getCartService();
            cartService.delCart(Integer.parseInt(cartId), SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
        }
        return resultEntity;
    }

    /**
     * 清空
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "cart/clear", method = RequestMethod.GET)
    public ResultEntity clear() {
        ResultEntity resultEntity = new ResultEntity();
        try {
            CartService cartService = getCartService();
            cartService.clearCart(SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
        }
        return resultEntity;
    }
}
