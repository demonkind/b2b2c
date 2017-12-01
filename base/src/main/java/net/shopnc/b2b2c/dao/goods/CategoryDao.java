package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.Category;
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
public class CategoryDao extends BaseDaoHibernate4<Category> {
    /**
     * 根据上级分类编号查询分类
     * @param parentId
     * @return
     */
    public List<Category> findByParentId(int parentId) {
        String hql = "from Category where parentId = :parentId order by categorySort asc";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", parentId);
        return super.find(hql, map);
    }

    /**
     * 根据上级分类编号查询分类
     * @param parentId
     * @return
     */
    public List<Object> findObjectByParentId(int parentId) {
        String hql = "from Category where parentId = :parentId order by categorySort asc";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", parentId);
        return super.findObject(hql, map);
    }

    /**
     * 根据上级分类编号查新分类导航
     * @param parentId
     * @return
     */
    public List<Object> findCategoryNavVoByParentId(int parentId) {
        String hql = "select new net.shopnc.b2b2c.vo.CategoryNavVo(c) from Category c where parentId = :parentId order by categorySort asc";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", parentId);
        return super.findObject(hql, map);
    }

    /**
     * 根据上级分类编号查询分类
     * @param storeId
     * @param parentId
     * @return
     */
    public List<Object> findStoreBindCategoryByParentId(int storeId, int parentId) {
        String hql = "select distinct c from Category c,StoreBindCategory sbc where (c.categoryId = sbc.categoryId1 or c.categoryId = sbc.categoryId2 or c.categoryId = sbc.categoryId3) and sbc.storeId = :storeId and c.parentId = :parentId order by c.categorySort asc";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        map.put("parentId", parentId);
        return super.findObject(hql, map);
    }
}
