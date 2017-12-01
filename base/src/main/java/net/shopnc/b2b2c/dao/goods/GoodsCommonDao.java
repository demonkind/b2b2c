package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.util.ShopHelper;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class GoodsCommonDao extends BaseDaoHibernate4<GoodsCommon> {
    private String hqlCommend = "update GoodsCommon set isCommend = :isCommend where commonId = :commonId";

    /**
     * 查询店铺SPU数量
     * @param storeId
     * @return
     */
    public long findCountByStoreId(int storeId) {
        String hql = "select count(*) from GoodsCommon where storeId = :storeId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        return findCount(hql, map);
    }
    /**
     * 出售中的商品SPU列表
     * @param list
     * @param map
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> findGoodsCommonVoList(List<String> list, HashMap<String, Object> map, int pageNo, int pageSize) {
        String where = "";
        for (String string : list) {
            where += " and " + string;
        }
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsCommonVo(gc, g, max(g.goodsPrice), min(g.goodsPrice), sum(gs.goodsStorage)) from GoodsCommon gc, Goods g, GoodsSale gs where gc.commonId = g.commonId and g.goodsId = gs.goodsId " + where +  " group by gc.commonId order by gc.commonId desc";
        return findObjectByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 商家商品列表SPU数量
     * @param list
     * @param map
     * @return
     */
    public long findCount(List<String> list, HashMap<String, Object> map) {
        String hql = "select count(distinct gc) from GoodsCommon gc, Goods g where gc.commonId = g.commonId";
        for (String string : list) {
            hql +=  " and " + string;
        }
        return findCount(hql, map);
    }

    /**
     * 根据店铺编号下架商品<br>
     *     后台关闭店铺使用
     * @param storeId
     */
    public void editOfflineByStoreId(int storeId) {
        String hql = "update GoodsCommon set goodsState = :goodsState where storeId = :storeId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsState", GoodsState.OFFLINE);
        map.put("storeId", storeId);
        update(hql, map);
    }

    /**
     * 查询商品列表<br>
     *     后台商品列表使用
     * @param commonId
     * @return
     */
    public Object getGoodsCommonVoByCommonId(int commonId) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsCommonVo(gc, g, s, max(g.goodsPrice), min(g.goodsPrice), sum(gs.goodsStorage)) from GoodsCommon gc, Store s, Goods g, GoodsSale gs where gc.storeId = s.storeId and gc.commonId = g.commonId and g.goodsId = gs.goodsId and gc.commonId = :commonId group by gc.commonId order by gc.commonId desc";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        List<Object> list = super.findObject(hql, map);
        return list.get(0);
    }

    /**
     * 根据商品SPU编号查询列表
     * @param commonId
     * @return
     */
    public List<GoodsCommon> findByCommonIdList(List<Integer> commonId) {
        String hql = "from GoodsCommon where commonId in (:commonId)";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return find(hql, map);
    }

    /**
     * 查询商品已推荐数量
     * @param storeId
     * @return
     */
    public long findCommendCountByStoreId(int storeId) {
        String hql = "select count(*) from GoodsCommon where storeId = :storeId and isCommend = :isCommend";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("isCommend", State.YES);
        map.put("storeId", storeId);
        return findCount(hql, map);
    }

    /**
     * 等待审核商品数量
     * @return
     */
    public long findWaitCount() {
        String hql = "select count(*) from GoodsCommon where goodsVerify = :goodsVerify";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsVerify", GoodsVerify.WAIT);
        return findCount(hql, map);
    }
    /**
     * 等待审核商品数量
     * @return
     */
    public long findWaitCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from GoodsCommon where goodsVerify = :goodsVerify";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("goodsVerify", GoodsVerify.WAIT);
        return findCount(hql, params);
    }
    /**
     * 出售中商品数量
     * @return
     */
    public long findOnSaleCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from GoodsCommon where goodsState = :goodsState and goodsVerify = :goodsVerify ";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("goodsState", GoodsState.ONLINE);
        params.put("goodsVerify", GoodsVerify.PASS);
        return findCount(hql, params);
    }
    /**
     * 审核失败商品数量
     * @return
     */
    public long findVerifyFailCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from GoodsCommon where goodsVerify = :goodsVerify";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("goodsVerify", GoodsVerify.FAIL);
        return findCount(hql, params);
    }
    /**
     * 仓库中已审核商品数量
     * @return
     */
    public long findOfflineAndPassCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from GoodsCommon where goodsState = :goodsState and goodsVerify = :goodsVerify";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("goodsState", GoodsState.OFFLINE);
        params.put("goodsVerify", GoodsVerify.PASS);
        return findCount(hql, params);
    }
    /**
     * 违规禁售商品数量
     * @return
     */
    public long findBanCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from GoodsCommon where goodsState = :goodsState ";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("goodsState", GoodsState.BAN);
        return findCount(hql, params);
    }
    /**
     * 查询GoodsVo列表，带翻页   查询Goods，GoodsCommon表
     * @param where
     * @param params
     * @param pageNo
     * @param pageSize
     * @param sort
     * @return
     */
    public List<Object> findGoodsVoGC(List<String> where, HashMap<String, Object> params, int pageNo, int pageSize, String sort) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc) from Goods g, GoodsCommon gc where g.commonId = gc.commonId";
        //where
        for (String string : where) {
            hql +=  " and " + string;
        }
        // group
        hql += " group by gc.commonId";
        //sort
        if (sort!=null && sort.length()>0) {
            hql += (" order by " + sort);
        } else {
            hql += " order by g.commonId desc";
        }
        if (pageSize == 0) {
            return findObject(hql, params);
        } else {
            return findObjectByPage(hql, pageNo, pageSize, params);
        }
    }

    /**
     * 查询GoodsVo列表，带翻页   只查询Goods，GoodsCommon，GoodsSale表
     * @param where
     * @param params
     * @param pageNo
     * @param pageSize
     * @param sort
     * @return
     */
    public List<Object> findGoodsVoGCS(List<String> where, HashMap<String, Object> params, int pageNo, int pageSize, String sort) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc, gs) from Goods g, GoodsCommon gc, GoodsSale gs where g.commonId = gc.commonId and g.goodsId = gs.goodsId";
        //where
        if(where!=null){
	        for (String string : where) {
	            hql +=  " and " + string;
	        }	
        }
        //group
        hql += " group by gc.commonId";
        //sort
        if (sort!=null && sort.length()>0) {
            hql += (" order by " + sort);
        } else {
            hql += " order by g.commonId desc";
        }
        if (pageSize == 0) {
            return findObject(hql, params);
        } else {
            return findObjectByPage(hql, pageNo, pageSize, params);
        }
    }
    /**
     * 查询商品SPU总数
     * @param whereList
     * @param params
     * @return
     */
    public long findGoodsCommonCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from GoodsCommon where 1=1 ";
        for (String item : whereList) {
            hql += (" and " + item);
        }
        return super.findCount(hql, params);
    }

    /**
     * 更新商品SPU数据
     * @param goodsCommon
     */
    public void update(GoodsCommon goodsCommon) {
        goodsCommon.setUpdateTime(ShopHelper.getCurrentTimestamp());
        super.update(goodsCommon);
    }

    /**
     * 批量更新商品SPU数据
     * @param goodsCommonList
     */
    public void updateAll(List<GoodsCommon> goodsCommonList) {
        for (GoodsCommon goodsCommon : goodsCommonList) {
            this.update(goodsCommon);
        }
    }
}
