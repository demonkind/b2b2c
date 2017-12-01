package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.BrandBrandLabel;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class BrandBrandLabelDao extends BaseDaoHibernate4<BrandBrandLabel> {
    /**
     * 根据品牌编号查询品牌与品牌标签关系列表
     * @param brandId
     * @return
     */
    public List<BrandBrandLabel> findByBrandId(int brandId) {
        String hql = "from BrandBrandLabel where brandId = :brandId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("brandId", brandId);
        return super.find(hql, map);
    }

    /**
     * 根据品牌编号删除品牌与品牌标签关系表
     * @param brandId
     * @return
     */
    public void deleteByBrandId(int brandId) {
        List<BrandBrandLabel> brandBrandLabels = findByBrandId(brandId);
        super.deleteAll(brandBrandLabels);
    }

    /**
     * 根据品牌标签编号查询品牌与品牌标签列表
     * @param brandLabelId
     * @return
     */
    public List<BrandBrandLabel> findByBrandLabelId(int brandLabelId) {
        String hql = "from BrandBrandLabel where brandLabelId = :brandLabelId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("brandLabelId", brandLabelId);
        return super.find(hql, map);
    }

    /**
     * 根据品牌标签编号删除品牌与品牌标签
     * @param brandLabelId
     * @return
     */
    public void deleteByBrandLabelId(int brandLabelId) {
        List<BrandBrandLabel> brandBrandLabels = findByBrandLabelId(brandLabelId);
        super.deleteAll(brandBrandLabels);
    }
}
