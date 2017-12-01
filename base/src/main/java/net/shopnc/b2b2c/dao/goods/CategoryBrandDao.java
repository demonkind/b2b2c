package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.CategoryBrand;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-13.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class CategoryBrandDao extends BaseDaoHibernate4<CategoryBrand> {
    /**
     * 根据品牌编号查询分类与品牌关系列表
     * @param brandId
     * @return
     */
    public List<CategoryBrand> findByBrandId(int brandId) {
        String hql = "from CategoryBrand where brandId = :brandId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("brandId", brandId);
        return super.find(hql, map);
    }

    /**
     * 根据品牌编号删除分类与品牌关系数据
     * @param brandId
     * @return
     */
    public void deleteByBrandId(int brandId) {
        List<CategoryBrand> categoryBrands = findByBrandId(brandId);
        super.deleteAll(categoryBrands);
    }

    /**
     * 根据分类编号查新分类与品牌关系表
     * @param categoryId
     * @return
     */
    public List<CategoryBrand> findByCategoryId(int categoryId) {
        String hql = "from CategoryBrand where categoryId = :categoryId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("categoryId", categoryId);
        return super.find(hql, map);
    }

    /**
     * 根据分类编号删除分类与品牌关系表
     * @param categoryId
     * @return
     */
    public void deleteByCategoryId(int categoryId) {
        List<CategoryBrand> categoryBrands = findByCategoryId(categoryId);
        super.deleteAll(categoryBrands);
    }
}
