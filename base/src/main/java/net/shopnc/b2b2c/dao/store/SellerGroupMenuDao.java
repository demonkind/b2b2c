package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.SellerGroupMenu;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商家账号组菜单DAO
 * Created by dqw on 2015/11/20.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
@SuppressWarnings("unchecked")
public class SellerGroupMenuDao extends BaseDaoHibernate4<SellerGroupMenu> {

    /**
     * 根据组编号查找所有菜单编号
     * @param groupId
     * @return
     */
    public List<Integer> findMenuIdListByGroupId(int groupId) {
        String hql = "select menuId from SellerGroupMenu where groupId = :groupId";
        return sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("groupId", groupId)
                .list();
    }

    /**
     * 根据组编号删除
     * @param groupId
     */
    public void delByGroupId(int groupId) {
        String hql = "delete from SellerGroupMenu where groupId = :groupId";
        sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }
}
