package net.shopnc.b2b2c.dao.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.vo.goods.GoodsDetailVo;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.common.dao.BaseDaoHibernate4;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class GoodsDao extends BaseDaoHibernate4<Goods> {

	/**
	 * 活动商品查询
	 */
	public List<Object> getGoodsListByActivityType(HashMap<String, Object> param,List<String> whereList,int pageNo,int pageSize){
		StringBuffer hql=new StringBuffer();
		 hql.append("select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc, gs, s, c, at, a) from Goods g, GoodsCommon gc, GoodsSale gs, Store s, Category c,GoodsActivity at,Activity a " 
                + " where g.commonId = gc.commonId and g.goodsId = gs.goodsId and g.commonId = at.commonId and gc.storeId = s.storeId and gc.categoryId = c.categoryId "
                + " and a.activityId = at.activityId ");
		if(whereList!=null){
			 for (String whereKey : whereList) {
		            hql.append(" and " + whereKey);
			 }
		}
		hql.append(" group by gc.commonId");
		return super.findObjectByPage(hql.toString(), pageNo, pageSize, param);
	}
    /**
     * 商品详细信息<br>
     *     相片详情使用
     * @param goodsId
     * @return
     * @throws ShopException
     */
    public GoodsDetailVo getDetail(int goodsId) throws ShopException {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsDetailVo(g, gc, gs) from Goods g, GoodsCommon gc, GoodsSale gs where g.commonId = gc.commonId and g.goodsId = gs.goodsId and g.goodsId = :goodsId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsId", goodsId);
        List<Object> products = super.findObject(hql, map);
        if (products == null || products.isEmpty()) {
            throw new ShopException("参数错误");
        }
        return (GoodsDetailVo) products.get(0);
    }

    /**
     * GoodsVo列表
     * @param table
     * @param whereList
     * @param sort
     * @param map
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> findGoodsVo(String table, List<String> whereList, String sort, HashMap<String, Object> map, int pageNo, int pageSize) {
    	String where = "";
        for (String string : whereList) {
            where += " and " + string;
        }
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc, gs, s, c) from Goods g, GoodsCommon gc, GoodsSale gs, Store s, Category c " + table +
                " where g.commonId = gc.commonId and g.goodsId = gs.goodsId and gc.storeId = s.storeId and gc.categoryId = c.categoryId " + where +
                " group by gc.commonId order by " + sort;
        return super.findObjectByPage(hql, pageNo, pageSize, map);
    }
    
    /**
     * GoodsVo列表
     * @param table
     * @param whereList
     * @param sort
     * @param map
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> findGoodsVoActivity(String table, List<String> whereList, String sort, HashMap<String, Object> map, int pageNo, int pageSize) {
    	String where = "";
        for (String string : whereList) {
            where += " and " + string;
        }
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc, gs, s, c, at) from Goods g, GoodsCommon gc, GoodsSale gs, Store s, Category c, GoodsActivity at" + table +
                " where at.commonId=gc.commonId and   g.commonId = gc.commonId and g.goodsId = gs.goodsId and gc.storeId = s.storeId and gc.categoryId = c.categoryId " + where +
                " group by gc.commonId order by " + sort;
        return super.findObjectByPage(hql, pageNo, pageSize, map);
    }
    
    /**
     * GoodsVo列表
     * @param table
     * @param whereList
     * @param sort
     * @param map
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> findGoods(String table, List<String> whereList, String sort, HashMap<String, Object> map, int pageNo, int pageSize) {
        String where = "";
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc, gs, s, c) from Goods g, GoodsCommon gc, GoodsSale gs, Store s, Category c " + 
                " where g.commonId = gc.commonId and g.goodsId = gs.goodsId and gc.storeId = s.storeId and gc.categoryId = c.categoryId " + 
                " group by gc.commonId" ;
        return super.findObjectByPage(hql, pageNo, pageSize, map);
    }
    
    /**
     * 查询商品活动总数<br>
     *     商品列表使用
     * @param table
     * @param whereList
     * @param map
     * @return
     */
    public long findGoodsVoCountActivity(String table, List<String> whereList, HashMap<String, Object> map) {
        String where = "";
        for (String string : whereList) {
            where += " and " + string;
        }
        String hql = "select count(distinct g.commonId) from Goods g, GoodsCommon gc, GoodsSale gs,Store s, GoodsActivity at " + table +
                " where  g.commonId = gc.commonId and g.goodsId = gs.goodsId and gc.storeId = s.storeId " + where +
                " group by gc.commonId";
        return super.findCount(hql, map);
    }

    /**
     * 查询商品总数<br>
     *     商品列表使用
     * @param table
     * @param whereList
     * @param map
     * @return
     */
    public long findGoodsVoCount(String table, List<String> whereList, HashMap<String, Object> map) {
        String where = "";
        for (String string : whereList) {
            where += " and " + string;
        }
        String hql = "select count(distinct g.commonId) from Goods g, GoodsCommon gc, GoodsSale gs,Store s " + table +
                " where  g.commonId = gc.commonId and g.goodsId = gs.goodsId and gc.storeId = s.storeId " + where +
                " group by gc.commonId";
        return super.findCount(hql, map);
    }

    /**
     * 查询店铺GoodsVo列表
     * @param whereList
     * @param sort
     * @param map
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> findStoreGoodsVo(String table, List<String> whereList, String sort, HashMap<String, Object> map, int pageNo, int pageSize) {
        String where = "";
        for (String string : whereList) {
            where += " and " + string;
        }
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc, gs) from Goods g, GoodsCommon gc, GoodsSale gs" + table +
                " where g.commonId = gc.commonId and g.goodsId = gs.goodsId  " + where +
                " group by gc.commonId order by " + sort;
        return super.findObjectByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 查询店铺商品数<br>
     *     店铺商品列表使用
     * @param whereList
     * @param map
     * @return
     */
    public long findStoreGoodsVoCount(String table, List<String> whereList, HashMap<String, Object> map) {
        String where = "";
        for (String string : whereList) {
            where += " and " + string;
        }
        String hql = "select count(distinct g.commonId) from Goods g, GoodsCommon gc, GoodsSale gs " + table +
                " where g.commonId = gc.commonId and g.goodsId = gs.goodsId " + where +
                " group by gc.commonId";
        return super.findCount(hql, map);
    }

    /**
     * 根据commonId查询GoodsJsonVo
     * @param commonId
     * @return
     */
    public List<Object> findGoodsJsonVoByCommonId(int commonId) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsJsonVo(g, gs) from Goods g, GoodsSale gs where g.goodsId = gs.goodsId and g.commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return super.findObject(hql, map);
    }

    /**
     * 根据commonId查询GoodsSkuVo
     * @param commonId
     * @return
     */
    public List<Object> findGoodsSkuVoByCommonId(int commonId) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsSkuVo(g, gc, gs) from Goods g, GoodsCommon gc, GoodsSale gs where g.commonId = gc.commonId and g.goodsId = gs.goodsId and g.commonId = :commonId group by g.goodsId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return super.findObject(hql, map);
    }

    /**
     * 删除商品
     * @param goodsIds
     * @param commonId
     * @return
     */
    public List<Integer> deleteByNotInGoodsIds(List<Integer> goodsIds, int commonId) {
        String hql = "from Goods where commonId = :commonId and goodsId not in (:goodsIds)";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        map.put("goodsIds", goodsIds);
        List<Goods> goodsList = super.find(hql, map);
        List<Integer> integerList = new ArrayList<Integer>();
        for (Goods goods : goodsList) {
            integerList.add(goods.getGoodsId());
            super.delete(goods);
        }
        return integerList;
    }

    /**
     * 根据commonId查询商品
     * @param commonId
     * @return
     */
    public List<Goods> findByCommonId(int commonId) {
        String hql = "from Goods where commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return super.find(hql, map);
    }

    /**
     * 获得商品基础信息列表
     *
     * @param where
     * @param params
     * @return
     */
    public List<Object> findGoodsAndCommonList(HashMap<String,String> where, HashMap<String,Object> params) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc) from Goods g, GoodsCommon gc where g.commonId = gc.commonId ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findObject(hql, params);
    }

    /**
     * 获得商品基础信息
     * @param goodsId
     * @return
     */
    public GoodsVo findGoodsAndCommonByGoodsId(Integer goodsId) {
        GoodsVo goodsVo = null;
        if (goodsId==null || goodsId<=0) {
            return goodsVo;
        }
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc) from Goods g, GoodsCommon gc where g.commonId = gc.commonId and g.goodsId = :goodsId";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("goodsId", goodsId);
        List<Object> list = super.findObject(hql, params);
        if (list != null && list.size() > 0) {
            goodsVo = (GoodsVo) list.get(0);
        }
        return goodsVo;
    }

    /**
     * 根据一级分类编号查询GoodsVo列表，首页使用
     * @param categoryId
     * @param limit
     * @return
     */
    public List<Object> findGoodsVoByCategoryId1(int categoryId, int limit) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc) from Goods g, GoodsCommon gc where g.commonId = gc.commonId and gc.categoryId1 = :categoryId1 group by gc.commonId";
        HashMap<String, Object> map = new HashMap<>();
        map.put("categoryId1", categoryId);
        return findObjectByPage(hql, 1, limit, map);
    }

    /**
     * 根据二级分类编号查询GoodsVo列表，首页使用
     * @param categoryId
     * @param limit
     * @return
     */
    public List<Object> findGoodsVoByCategoryId2(int categoryId, int limit) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc) from Goods g, GoodsCommon gc where g.commonId = gc.commonId and gc.categoryId2 = :categoryId2 group by gc.commonId";
        HashMap<String, Object> map = new HashMap<>();
        map.put("categoryId2", categoryId);
        return findObjectByPage(hql, 1, limit, map);
    }

    /**
     * 查询商品信息<br>
     *     列表页选择规格使用
     * @param goodsId
     * @return
     * @throws ShopException
     */
    public Object getGoodsVoByGoodsId(int goodsId) throws ShopException {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc, gs) from Goods g, GoodsCommon gc, GoodsSale gs where g.commonId = gc.commonId and g.goodsId = gs.goodsId and g.goodsId = :goodsId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsId", goodsId);
        List<Object> list = super.findObject(hql, map);
        if (list == null) {
            throw new ShopException("参数错误");
        }
        return list.get(0);
    }

    /**
     * 更新商品主图
     *
     * @param commonId
     * @param colorId
     * @param imageName
     */
    public void updateGoodsImage(int commonId, int colorId, String imageName) {
        String hql = "update Goods set imageName = :imageName where commonId = :commonId and colorId = :colorId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        map.put("colorId", colorId);
        map.put("imageName", imageName);
        update(hql, map);
    }
    
    /**
     * 获取单个商品的总数以及剩余数量 ,坐标0是剩余数量，坐标1是商品总数 
     */
    public List<Map<String, Object>> getProductCountByCommonId(int commonId){
    	String hql2="select sum(gs.goodsSaleNum),sum(gs.goodsStorage) from GoodsCommon gc,Goods g,GoodsSale gs where gc.commonId=g.commonId and g.goodsId=gs.goodsId and gc.commonId=:commonId group by gc.commonId";
    	Query query = sessionFactory.getCurrentSession().createQuery(hql2);
    	query.setInteger("commonId", commonId);
    	query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	List<Map<String, Object>> list = query.list();
    	return list;
    }
    
    public List<Map<String,Object>> getProductSaleNumByCommonId(int commonId){
    	String hql="select ga.goodsStorage from Goods g,GoodsSale ga where g.goodsId=ga.goodsId and g.commonId=:commonId";
    	Query query = sessionFactory.getCurrentSession().createQuery(hql);
    	query.setInteger("commonId", commonId);
    	query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	List<Map<String, Object>> list = query.list();
    	return list;
    }
}

