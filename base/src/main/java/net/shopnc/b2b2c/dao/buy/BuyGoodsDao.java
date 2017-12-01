package net.shopnc.b2b2c.dao.buy;

import net.shopnc.b2b2c.constant.GoodsImageDefault;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 下单过程中查询商品信息
 * Created by hbj on 2015/12/15.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class BuyGoodsDao extends BaseDaoHibernate4<Orders> {
    /**
     * 取得多条购买商品信息[联查购物车]
     * @param cartIdList 购物车ID构成的list
     * @param memberId 会员Id
     * @return
     */
    public List<Object> getBuyGoodsItemVoList(List<Integer> cartIdList, int memberId) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        if (cartIdList.size() > 0) {
            String hql = "select new net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo(c, g, gc, gi, gs, s) from Cart c, Goods g, GoodsCommon gc, GoodsImage gi, GoodsSale gs, Store s where c.goodsId = g.goodsId and g.commonId = gc.commonId and gi.commonId = gc.commonId and gi.colorId = g.colorId and gi.isDefault = " + GoodsImageDefault.YES + " and gc.storeId = s.storeId and g.goodsId = gs.goodsId and c.cartId in (:cartIdList) and c.memberId = :memberId";
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("cartIdList",cartIdList);
            map.put("memberId", memberId);
            return super.findObject(hql, map);
        } else {
            return new ArrayList<Object>();
        }
    }

    /**
     * 取得多条购买商品信息[不联查购物车]
     * @param goodsIdList goodsId构成的list
     * @return
     */
    public List<Object> getBuyGoodsItemVoList(List<Integer> goodsIdList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        if (goodsIdList.size() > 0) {
            String hql = "select new net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo(g, gc, gi, gs, s) from Goods g, GoodsCommon gc, GoodsImage gi, GoodsSale gs, Store s where g.commonId = gc.commonId and gi.commonId = gc.commonId and gi.colorId = g.colorId and gi.isDefault = " + GoodsImageDefault.YES + " and gc.storeId = s.storeId and g.goodsId = gs.goodsId and g.goodsId in (:goodsIdList)";
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("goodsIdList", goodsIdList);
            return super.findObject(hql, map);
        } else {
            return new ArrayList<Object>();
        }

    }
}
