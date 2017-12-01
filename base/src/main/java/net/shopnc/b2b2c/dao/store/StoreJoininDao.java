package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.constant.StoreJoininState;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.StoreJoinin;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.hibernate.engine.transaction.spi.JoinStatus;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dqw on 2015/12/18.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreJoininDao extends BaseDaoHibernate4<StoreJoinin> {

    /**
     * 更具店铺名称查询申请是否存在，判断店铺名是否可用
     * @param storeName
     * @return
     */
    public StoreJoinin findByStoreName(String storeName) {
        String hql = "from StoreJoinin where storeName = :storeName";

        StoreJoinin storeJoinin = (StoreJoinin) sessionFactory.getCurrentSession().createQuery(hql)
                .setString("storeName", storeName)
                .uniqueResult();

        return storeJoinin;
    }

    /**
     * 查询需要管理员审核的开店申请数量
     * @return
     */
    public Long getAdminCheckCount() {
        String hql = "select count(*) from StoreJoinin where state = " + StoreJoininState.PAYED + " OR state = " + StoreJoininState.SUBMIT;

        Long count = (Long) sessionFactory.getCurrentSession().createQuery(hql)
                .uniqueResult();

        return count;
    }
}
