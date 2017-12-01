package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.StoreSlide;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dqw on 2016/01/19.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
@SuppressWarnings("unchecked")
public class StoreSlideDao extends BaseDaoHibernate4<StoreSlide> {

    public List<StoreSlide> findByStoreId(int storeId) {
        String hql = "from StoreSlide where storeId = :storeId";

        return sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("storeId", storeId)
                .list();
    }

    public void delByStoreId(int storeId) {
        String hql = "delete from StoreSlide where storeId = :storeId";

        sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("storeId", storeId)
                .executeUpdate();
    }

}
