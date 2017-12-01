package net.shopnc.b2b2c.service.cart.impl;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.dao.orders.CartDao;
import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.cart.CartService;
import net.shopnc.b2b2c.vo.cart.CartItemVo;
import net.shopnc.b2b2c.vo.cart.CartStoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 购物车存入数据表
 * Created by hbj on 2015/11/30.
 */
@Service(value = "CartServiceDbImpl")
@Transactional(rollbackFor = {Exception.class})
public class CartServiceDbImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    /**
     * 加入购物车
     * @param cart
     * @return
     * @throws ShopException
     */
    public List<CartStoreVo> addCart(Cart cart) throws ShopException {
        if (cart.getMemberId() <= 0) {
            throw new ShopException("会员账号错误");
        }
        long cartCount = cartDao.getCartlistCountByMemberId(cart.getMemberId());
        if (cartCount >= Common.CART_DB_COUNT) {
            throw new ShopException(String.format("购物车最多允许加入%d个商品",Common.CART_DB_COUNT));
        }
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
        Cart cart1 = cartDao.getCartInfo(cartItemVo.getGoodsId(), cart.getMemberId());
        if (cart1 == null) {
            cartDao.save(cart);
        }
        return getCartStoreVoList(cart.getMemberId());
    }

    /**
     * 取得购物车列表
     * @param memberId
     * @return
     */
    public List<CartStoreVo> getCartStoreVoList(int memberId) {
        List<CartStoreVo> cartStoreVoList = new ArrayList<CartStoreVo>();
        HashMap<Integer, List<CartItemVo>> hashMapCartItemVoList = this.getCartItemListGroupbyStoreIdByMemberId(memberId);
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
     * 取得购物车商品列表[以店铺ID分组]
     * @param memberId
     * @return HashMap<storeId, List<CartItemVo>
     */
    private HashMap<Integer, List<CartItemVo>> getCartItemListGroupbyStoreIdByMemberId(int memberId) {
        HashMap<Integer, List<CartItemVo>> hashMap = new HashMap<Integer, List<CartItemVo>>();
        List<Object> cartItemVoList = cartDao.getCartItemVoList(memberId);
        int storeId;
        for (int i = 0; i < cartItemVoList.size(); i++) {
            List<CartItemVo> cartItemVoList1 = new ArrayList<CartItemVo>();
            CartItemVo cartItemVo = (CartItemVo)cartItemVoList.get(i);
            storeId = cartItemVo.getStoreId();
            if (hashMap.containsKey(storeId)) {
                cartItemVoList1 = hashMap.get(storeId);
            }
            cartItemVoList1.add(cartItemVo);
            hashMap.put(storeId, cartItemVoList1);
        }
        return hashMap;
    }

    /**
     * 更新购物车数量
     * @param cartId
     * @param buyNum
     * @param memberId
     * @throws ShopException
     */
    public void editCart(int cartId, int buyNum, int memberId) throws ShopException {
        if (cartId <= 0 || buyNum <= 0 || memberId <= 0) {
            throw new ShopException("参数错误");
        }
        Cart cart = cartDao.get(Cart.class,cartId);
        if (cart == null) {
            throw new ShopException("该购物车信息不存在");
        }
        if (memberId != cart.getMemberId()) {
            throw new ShopException("该购物车信息不存在");
        }
        CartItemVo cartItemVo = cartDao.getCartItemVoInfoByGoodsId(cart.getGoodsId());
        if (cartItemVo.getGoodsStatus() == 0) {
            throw new ShopException("商品未上架");
        }
        if (buyNum > cartItemVo.getGoodsStorage()) {
            HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
            hashMap.put("goodsStorage",cartItemVo.getGoodsStorage());
            throw new ShopException("商品库存不足",hashMap);
        }
        cart.setBuyNum(buyNum);
        cartDao.update(cart);
    }

    /**
     * 删除购物车
     * @param cartId
     * @param memberId
     * @throws ShopException
     */
    public void delCart(int cartId, int memberId) throws ShopException {
        if (cartId <= 0) {
            throw new ShopException("参数错误");
        }
        Cart cart = cartDao.get(Cart.class, cartId);
        if (cart == null) {
            throw new ShopException("删除失败");
        }
        if (memberId != cart.getMemberId()) {
            throw new ShopException("删除失败");
        }
        cartDao.delete(Cart.class, cartId);
    }

    /**
     * 清空购物车
     */
    public void clearCart(int memberId) {
        cartDao.delCart(memberId);
    }

	@Override
	public List<CartStoreVo> getCartStoreVoListForWap(String goodsInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
