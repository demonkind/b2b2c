package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.store.StoreNavigation;
import net.shopnc.b2b2c.domain.store.StoreSlide;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 店铺导航Dao
 * Created by dqw on 2016/01/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
@SuppressWarnings("unchecked")
public class StoreNavigationDao extends BaseDaoHibernate4<StoreNavigation> {

    /**
     * 根据店铺编号查询导航列表
     *
     * @param storeId
     * @return
     */
    public List<StoreNavigation> findByStoreId(int storeId) {
        String hql = "from StoreNavigation where storeId = :storeId";

        return sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("storeId", storeId)
                .list();
    }

    /**
     * 根据店铺编号查询可见的导航列表
     *
     * @param storeId
     * @return
     */
    public List<StoreNavigation> findShowByStoreId(int storeId) {
        String hql = "from StoreNavigation where storeId = :storeId and isShow = :isShow order by sort";

        return sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("storeId", storeId)
                .setParameter("isShow", State.YES)
                .list();
    }

}
