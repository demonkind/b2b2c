package net.shopnc.b2b2c.dao.admin;

import net.shopnc.b2b2c.domain.admin.AdminGroupPermission;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员权限组DAO
 * Created by dqw on 2015/12/29.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class AdminGroupPermissionDao extends BaseDaoHibernate4<AdminGroupPermission> {

    /**
     * 根据组编号查找权限
     * @param groupId
     * @return
     */
    public List<Integer> findMenuIdListByGroupId(int groupId) {
        String hql = "select menuId from AdminGroupPermission where groupId = :groupId";
        return sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("groupId", groupId)
                .list();
    }

    /**
     * 根据组编号删除权限
     * @param groupId
     */
    public void delByGroupId(int groupId) {
        String hql = "delete from AdminGroupPermission where groupId = :groupId";
        sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }
}

