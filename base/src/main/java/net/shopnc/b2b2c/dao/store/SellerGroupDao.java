package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.SellerGroup;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 商家账号组DAO
 * Created by dqw on 2015/11/20.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
@SuppressWarnings("unchecked")
public class SellerGroupDao extends BaseDaoHibernate4<SellerGroup> {

    /**
     * 根据店铺编号查找账号组
     *
     * @param storeId
     * @return
     */
    public List<SellerGroup> findByStoreId(int storeId) {
        String hql = "from SellerGroup where storeId = :storeId";

        return sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("storeId", storeId)
                .list();
    }
}
