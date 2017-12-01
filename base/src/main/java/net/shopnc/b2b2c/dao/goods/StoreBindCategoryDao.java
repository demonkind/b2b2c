package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.SpecValue;
import net.shopnc.b2b2c.domain.goods.StoreBindCategory;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by dqw on 2015/12/21.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreBindCategoryDao extends BaseDaoHibernate4<StoreBindCategory> {

    /**
     * 根据店铺编号删除绑定分类
     * @param storeId
     * @return
     */
    @SuppressWarnings("unchecked")
    public void deleteByStoreId(int storeId) {
        String hql = "delete StoreBindCategory where storeId = :storeId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("storeId", storeId);
        query.executeUpdate();
    }

    /**
     * 根据店铺编号读取绑定分类列表
     * @param storeId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<StoreBindCategory> getListByStoreId(int storeId) {
        String hql = "from StoreBindCategory where storeId = :storeId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("storeId", storeId);
        return query.list();
    }

    /**
     * 根据店铺Id、商品分类编号取得绑定信息
     * @param storeId
     * @param categoryId
     * @return
     */
    public StoreBindCategory getCommissionRateByStoreIdAndCategoryId(int storeId, int categoryId) {
        String hql = "from StoreBindCategory where storeId = :storeId and (categoryId1 = :categoryId or categoryId2 = :categoryId or categoryId3 =:categoryId)";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("storeId", storeId);
        query.setParameter("categoryId", categoryId);
        List<StoreBindCategory> storeBindCategoryList = query.list();
        return storeBindCategoryList.size() > 0 ? storeBindCategoryList.get(0) : null;
    }
}
