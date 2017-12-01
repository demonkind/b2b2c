package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.constant.BrandApplyState;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class BrandDao extends BaseDaoHibernate4<Brand> {
    /**
     * 根据分类编号查询品牌
     * @param categoryId
     * @return
     */
    public List<Brand> findByCategoryId(int categoryId) {
        String hql = "select b from Brand b,CategoryBrand cb where b.brandId = cb.brandId and cb.categoryId = :categoryId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("categoryId", categoryId);
        return super.find(hql, map);
    }

    /**
     * 根据分类编号查询品牌 带排序
     * @param categoryId
     * @param order
     * @return
     */
    public List<Brand> findByCategoryId(int categoryId, String order) {
        String hql = "select b from Brand b,CategoryBrand cb where b.brandId = cb.brandId and cb.categoryId = :categoryId order by b.brandInitial " + order;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("categoryId", categoryId);
        return super.find(hql, map);
    }

    /**
     * 被推荐的品牌列表<br/>
     * 显示推荐并且带图的商品
     * @return
     */
    public List<Brand> findRecommendForHome() {
        int limit = 48;
        String hql = "from Brand where isRecommend = :isRecommend and showType = :showType order by brandSort asc";
        HashMap<String, Object> map = new HashMap<>();
        map.put("isRecommend", State.YES);
        map.put("showType", State.YES);
        return findByPage(hql, 1, limit, map);
    }

    /**
     * 根据品牌标签编号查询品牌
     * @param brandLabelId
     * @return
     */
    public List<Brand> findByBrandLabelId(int brandLabelId) {
        String hql = "select b from Brand b, BrandBrandLabel bbl where b.brandId = bbl.brandId and bbl.brandLabelId = :brandLabelId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("brandLabelId", brandLabelId);
        return find(hql, map);
    }

    /**
     * 根据品牌标签编号查询品牌
     * @param brandLabelId
     * @return
     */
    public List<Brand> findByBrandLabelId(int brandLabelId, int limit) {
        String hql = "select b from Brand b, BrandBrandLabel bbl where b.brandId = bbl.brandId and bbl.brandLabelId = :brandLabelId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("brandLabelId", brandLabelId);
        return findByPage(hql, 1, limit, map);
    }

    /**
     * 根据店铺编号查询品牌
     * @param storeId
     * @return
     */
    public List<Brand> findByStoreId(int storeId, int pageNo, int pageSize) {
        String hql = "from Brand where storeId = :storeId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        return findByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 根据店铺标号查询品牌数量
     * @param storeId
     * @return
     */
    public long findCountByStoreId(int storeId) {
        String hql = "select count(*) from Brand where storeId = :storeId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        return findCount(hql, map);
    }

    /**
     * 查询等待审核
     * @return
     */
    public long findWaitCount() {
        String hql = "select count(*) from Brand where applyState = :applyState";
        HashMap<String, Object> map = new HashMap<>();
        map.put("applyState", BrandApplyState.WAIT);
        return findCount(hql, map);
    }
}
