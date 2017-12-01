package net.shopnc.b2b2c.service.cart.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.dao.orders.CartDao;
import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.cart.CartService;
import net.shopnc.b2b2c.vo.cart.CartItemVo;
import net.shopnc.b2b2c.vo.cart.CartStoreVo;
import net.shopnc.common.util.CookieHelper;
import net.shopnc.common.util.JsonHelper;
import net.shopnc.common.util.PriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 购物车存入Cookie<br/>
 * Created by hbj on 2015/11/30.
 */
@Service(value = "CartServiceCookieImpl")
@Transactional(rollbackFor = {Exception.class})
public class CartServiceCookieImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    /**
     * 加入购物车
     * @param cart
     * @return
     */
    public List<CartStoreVo> addCart(Cart cart) throws ShopException {
        String cookieValue = CookieHelper.getCookie("cart");
        HashMap<String, Object> hashMapCookie = new HashMap<String, Object>();
        if (cookieValue != null) {
            hashMapCookie = JsonHelper.toGenericObject(cookieValue, new TypeReference< HashMap<String, Object> >(){});
        }
        if (hashMapCookie == null) {
            hashMapCookie = new HashMap<String, Object>();
        }
        if (!hashMapCookie.containsKey(Integer.toString(cart.getGoodsId()))) {
            if (hashMapCookie.size() < Common.CART_COOKIE_COUNT) {
                CartItemVo cartItemVo = cartDao.getCartItemVoInfoByGoodsId(cart.getGoodsId());
                if (cartItemVo == null) {
                    throw new ShopException("商品不存在");
                }
                if (cartItemVo.getGoodsStatus() == 0) {
                    throw new ShopException("商品未上架");
                }
                cartItemVo.setBuyNum(cart.getBuyNum());
                if (cartItemVo.getStorageStatus() == 0) {
                    throw new ShopException("商品库存不足");
                }
                hashMapCookie.put(Integer.toString(cart.getGoodsId()), cart.getBuyNum());
                CookieHelper.setCookie("cart",hashMapCookie);
            } else {
                throw new ShopException(String.format("未登录购物车最多允许加入%d个商品",Common.CART_COOKIE_COUNT));
            }
        }
        //返回最新购物车总数量、总价格
        List<Cart> cartList = new ArrayList<Cart>();
        for(String goodsId: hashMapCookie.keySet()) {
            Cart cart1 = new Cart();
            cart1.setCartId(Integer.parseInt(goodsId));
            cart1.setGoodsId(Integer.parseInt(goodsId));
            cart1.setBuyNum(Integer.parseInt(hashMapCookie.get(goodsId).toString()));
            cartList.add(cart1);
        }
        HashMap<Integer, List<CartItemVo>> hashMapCartItemVoList = this.getCartItemListGroupbyStoreId(cartList);
        List<CartStoreVo> cartStoreVoList = new ArrayList<CartStoreVo>();
        for(Integer storeId : hashMapCartItemVoList.keySet()) {
            CartStoreVo cartStoreVo = new CartStoreVo();
            cartStoreVo.setStoreId(storeId);
            cartStoreVo.setCartItemVoList(hashMapCartItemVoList.get(storeId));
            cartStoreVo.setStoreName(hashMapCartItemVoList.get(storeId).get(0).getStoreName());
            cartStoreVo.ncSetCartAmount(hashMapCartItemVoList.get(storeId));
            cartStoreVo.setBuyNum(hashMapCartItemVoList.get(storeId).size());
            cartStoreVoList.add(cartStoreVo);
        }
        return cartStoreVoList;
    }

    /**
     * 取得购物车列表
     * @param memberId
     * @return
     */
    public List<CartStoreVo> getCartStoreVoList(int memberId) {
        List<Cart> cartList = this.getCartList();
        HashMap<Integer, List<CartItemVo>> hashMapCartItemVoList = this.getCartItemListGroupbyStoreId(cartList);
        List<CartStoreVo> cartStoreVoList = new ArrayList<CartStoreVo>();
        for(Integer storeId : hashMapCartItemVoList.keySet()) {
            CartStoreVo cartStoreVo = new CartStoreVo();
            cartStoreVo.setStoreId(storeId);
            cartStoreVo.setCartItemVoList(hashMapCartItemVoList.get(storeId));
            cartStoreVo.setStoreName(hashMapCartItemVoList.get(storeId).get(0).getStoreName());
            cartStoreVo.ncSetCartAmount(hashMapCartItemVoList.get(storeId));
            cartStoreVo.setBuyNum(hashMapCartItemVoList.get(storeId).size());
            cartStoreVoList.add(cartStoreVo);
        }
        return cartStoreVoList;
    }
    
    /**
     * 取得购物车列表(for wap)
     * @param memberId
     * @return
     */
    public List<CartStoreVo> getCartStoreVoListForWap(String goodsInfo) {
        List<Cart> cartList = this.getCartList(goodsInfo);
        HashMap<Integer, List<CartItemVo>> hashMapCartItemVoList = this.getCartItemListGroupbyStoreId(cartList);
        List<CartStoreVo> cartStoreVoList = new ArrayList<CartStoreVo>();
        for(Integer storeId : hashMapCartItemVoList.keySet()) {
            CartStoreVo cartStoreVo = new CartStoreVo();
            cartStoreVo.setStoreId(storeId);
            cartStoreVo.setCartItemVoList(hashMapCartItemVoList.get(storeId));
            cartStoreVo.setStoreName(hashMapCartItemVoList.get(storeId).get(0).getStoreName());
            cartStoreVo.ncSetCartAmount(hashMapCartItemVoList.get(storeId));
            cartStoreVo.setBuyNum(hashMapCartItemVoList.get(storeId).size());
            cartStoreVoList.add(cartStoreVo);
        }
        return cartStoreVoList;
    }
    
    /**
     * 购物车列表(for wap)
     * @return
     */
    private List<Cart> getCartList(String goodsInfo) {
    	List<Cart> cartList = new ArrayList<Cart>();
    	String[] goods = new String[]{goodsInfo};
    	if (goodsInfo.contains(";")){
    		goods = goodsInfo.split(";");
    	}
    	for(String good : goods){
    		String goodId = good.split(",")[0];
    		int buyNum = Integer.parseInt(good.split(",")[1]);
    		Cart cart = new Cart();
    		cart.setCartId(Integer.parseInt(goodId));
            cart.setGoodsId(Integer.parseInt(goodId));
            cart.setBuyNum(buyNum);
            cartList.add(cart);
    	}
        return cartList;
    }
    

    /**
     * 取得购物车商品列表[以店铺ID分组]
     * @param cartList
     * @return HashMap<storeId, List<CartItemVo>
     */
    private HashMap<Integer, List<CartItemVo>> getCartItemListGroupbyStoreId(List<Cart> cartList) {
        HashMap<Integer, List<CartItemVo>> hashMap = new HashMap<Integer, List<CartItemVo>>();
        if (cartList.size() > 0) {
            List<Integer> goodsIdList = new ArrayList<Integer>();
            for(int i=0; i<cartList.size(); i++) {
                goodsIdList.add(cartList.get(i).getGoodsId());
            }
            List<Object> cartItemVoList = cartDao.getCartItemVoCookieList(goodsIdList);
            int storeId;
            for (int i = 0; i < cartItemVoList.size(); i++) {
                List<CartItemVo> cartItemVoList1 = new ArrayList<CartItemVo>();
                CartItemVo cartItemVo = (CartItemVo)cartItemVoList.get(i);
                for(int a=0; a<cartList.size(); a++) {
                    if (cartList.get(a).getGoodsId() == cartItemVo.getGoodsId()) {
                        cartItemVo.setBuyNum(cartList.get(a).getBuyNum());
                        cartItemVo.setItemAmount(PriceHelper.mul(cartItemVo.getGoodsPrice(),cartList.get(a).getBuyNum()));
                    }
                }
                storeId = cartItemVo.getStoreId();
                if (hashMap.containsKey(storeId)) {
                    cartItemVoList1 = hashMap.get(storeId);
                }
                cartItemVoList1.add(cartItemVo);
                hashMap.put(storeId, cartItemVoList1);
            }
        }
        return hashMap;
    }

    /**
     * 购物车列表
     * @return
     */
    private List<Cart> getCartList() {
        String cookieValue = CookieHelper.getCookie("cart");
        HashMap<String, Object> hashMapCookie = new HashMap<String, Object>();;
        if (cookieValue != null) {
            hashMapCookie = JsonHelper.toGenericObject(cookieValue, new TypeReference< HashMap<String, Object> >(){});
        }
        if (hashMapCookie == null) {
            hashMapCookie = new HashMap<String, Object>();
        }

        List<Cart> cartList = new ArrayList<Cart>();
        for(String goodsId: hashMapCookie.keySet()) {
            Cart cart = new Cart();
            cart.setCartId(Integer.parseInt(goodsId));
            cart.setGoodsId(Integer.parseInt(goodsId));
            cart.setBuyNum(Integer.parseInt(hashMapCookie.get(goodsId).toString()));
            cartList.add(cart);
        }
        return cartList;
    }

    /**
     * 更新购物车数量
     * @param goodsId
     * @param buyNum
     * @param memberId
     * @throws ShopException
     */
    public void editCart(int goodsId, int buyNum, int memberId) throws ShopException {
        if (goodsId <= 0 || buyNum <= 0) {
            throw new ShopException("参数错误");
        }
        String cookieValue = CookieHelper.getCookie("cart");
        if (cookieValue == null) {
            throw new ShopException("该购物车信息不存在");
        }
        HashMap<String, Object> hashMapCookie = JsonHelper.toGenericObject(cookieValue, new TypeReference<HashMap<String, Object>>() {});
        if (hashMapCookie == null) {
            throw new ShopException("该购物车信息不存在");
        }
        if (hashMapCookie.containsKey(Integer.toString(goodsId))) {
            hashMapCookie.put(Integer.toString(goodsId), buyNum);
        } else {
            throw new ShopException("该购物车信息不存在");
        }
        CartItemVo cartItemVo = cartDao.getCartItemVoInfoByGoodsId(goodsId);
        if (cartItemVo == null) {
            throw new ShopException("商品不存在");
        }
        if (cartItemVo.getGoodsStatus() == 0) {
            throw new ShopException("商品未上架");
        }
        if (buyNum > cartItemVo.getGoodsStorage()) {
            HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("goodsStorage",cartItemVo.getGoodsStorage());
            throw new ShopException("商品库存不足",hashMap);
        }

        hashMapCookie.put(Integer.toString(goodsId),buyNum);
        CookieHelper.setCookie("cart",hashMapCookie);
    }

    /**
     * 删除购物车
     * @param goodsId
     * @param memberId
     * @throws ShopException
     */
    public void delCart(int goodsId, int memberId) throws ShopException {
        if (goodsId <= 0) {
            throw new ShopException("参数错误");
        }
        String cookieValue = CookieHelper.getCookie("cart");
        HashMap<String, Object> hashMapCookie = new HashMap<String, Object>();
        if (cookieValue != null) {
            hashMapCookie = JsonHelper.toGenericObject(cookieValue, new TypeReference< HashMap<String, Object> >(){});
            if (hashMapCookie == null) {
                hashMapCookie = new HashMap<String, Object>();
            }
            if (hashMapCookie.containsKey(Integer.toString(goodsId))) {
                hashMapCookie.remove(Integer.toString(goodsId));
            }
            CookieHelper.setCookie("cart",hashMapCookie);
        } else {
            throw new ShopException("该购物车信息不存在");
        }
    }

    /**
     * 清空购物车
     */
    public void clearCart(int memberId) {
        CookieHelper.removeCookie("cart");
    }
}
