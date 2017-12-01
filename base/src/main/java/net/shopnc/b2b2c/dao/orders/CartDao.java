package net.shopnc.b2b2c.dao.orders;

import net.shopnc.b2b2c.constant.GoodsImageDefault;
import net.shopnc.b2b2c.domain.orders.Cart;
import net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo;
import net.shopnc.b2b2c.vo.cart.CartItemVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.util.PriceHelper;
import org.hibernate.dialect.Ingres10Dialect;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 购物车
 * Created by shopnc on 2015/10/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class CartDao extends BaseDaoHibernate4<Cart> {

    /**
     * 取得购物车数量
     * @param memberId 会员ID
     * @return
     */
    public long getCartlistCountByMemberId(int memberId) {
        String hql = "select count(*) from Cart where memberId = :memberId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId",memberId);
        return super.findCount(hql,hashMap);
    }

    /**
     * 删除购物车
     * @param memberId 会员ID
     */
    public void delCart(int memberId) {
        String hql = "delete from Cart where memberId = :memberId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId",memberId);
        super.delete(hql, hashMap);
    }

    /**
     * 删除购物车
     * @param memberId 会员ID
     * @param cartIdList 购物车Id构成的列表
     */
    public void delCart(int memberId, List<Integer> cartIdList) {
        String hql = "delete from Cart where memberId = :memberId and cartId in (:cartIdList)";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId",memberId);
        hashMap.put("cartIdList",cartIdList);
        super.delete(hql, hashMap);
    }

    /**
     * 取得单条购物车信息[仅购物车表信息]
     * @param goodsId 商品ID
     * @param memberId 会员ID
     * @return
     */
    public Cart getCartInfo(int goodsId, int memberId) {
        String hql = "from Cart where memberId = :memberId and goodsId = :goodsId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId",memberId);
        hashMap.put("goodsId", goodsId);
        List<Cart> cartList = super.find(hql,hashMap);
        return cartList.size() > 0 ? cartList.get(0) : null;
    }

    /**
     * 取得单条商品信息[不联查购物车表]
     * @param goodsId 商品ID
     * @return
     */
    public CartItemVo getCartItemVoInfoByGoodsId(int goodsId) {
        String hql = "select new net.shopnc.b2b2c.vo.cart.CartItemVo(g, gc, gi, gs, s) from Goods g, GoodsCommon gc, GoodsImage gi, GoodsSale gs, Store s where g.commonId = gc.commonId and gi.commonId = gc.commonId and gi.colorId = g.colorId and gi.isDefault = " + GoodsImageDefault.YES + " and gc.storeId = s.storeId and g.goodsId = gs.goodsId and g.goodsId = :goodsId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsId", goodsId);
        List<Object> objectList = super.findObject(hql, map);
        return objectList.size() > 0 ? (CartItemVo)objectList.get(0) : null;
    }

    /**
     * 取得多条购物车信息[包含相关商品及店铺信息]
     * @param memberId 会员ID
     * @return
     */
    public List<Object> getCartItemVoList(int memberId) {
        String hql = "select new net.shopnc.b2b2c.vo.cart.CartItemVo(c, g, gc, gi, gs, s) from Cart c, Goods g, GoodsCommon gc, GoodsImage gi, GoodsSale gs, Store s where c.goodsId = g.goodsId and g.commonId = gc.commonId and gi.commonId = gc.commonId and gi.colorId = g.colorId and gi.isDefault = " + GoodsImageDefault.YES + " and gc.storeId = s.storeId and g.goodsId = gs.goodsId and c.memberId = :memberId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("memberId", memberId);
        return super.findObject(hql, map);
    }

    /**
     * 取得多条购物车信息[包含相关商品及店铺信息，使用cookie存放时不联查购物车表]
     * @param goodsIdList 商品ID构成的列表
     * @return
     */
    public List<Object> getCartItemVoCookieList(List<Integer> goodsIdList) {
        String hql = "select new net.shopnc.b2b2c.vo.cart.CartItemVo(g, gc, gi, gs, s) from Goods g, GoodsCommon gc, GoodsImage gi, GoodsSale gs, Store s where g.commonId = gc.commonId and gi.commonId = gc.commonId and gi.colorId = g.colorId and gi.isDefault = " + GoodsImageDefault.YES + " and gc.storeId = s.storeId and g.goodsId = gs.goodsId and g.goodsId in (:goodsIdList)";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsIdList", goodsIdList);
        return super.findObject(hql, map);
    }
}
