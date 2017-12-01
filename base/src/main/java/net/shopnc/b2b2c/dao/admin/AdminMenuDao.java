package net.shopnc.b2b2c.dao.admin;

import net.shopnc.b2b2c.domain.admin.AdminMenu;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 管理员菜单DAO
 * Created by dqw on 2015/11/26.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
@SuppressWarnings("unchecked")
public class AdminMenuDao extends BaseDaoHibernate4<AdminMenu> {

    /**
     * 查找所有管理员菜单
     * @return
     */
    public List<AdminMenu> findAdminMenuList() {
        String hql = "from AdminMenu order by id asc";

        List<AdminMenu> adminMenuList = sessionFactory.getCurrentSession().createQuery(hql).list();

        return adminMenuList;
    }

    /**
     * 根据父级编号查找管理员菜单
     * @param parentId
     * @return
     */
    public List<AdminMenu> findAdminMenuListByParentId(int parentId) {
        String hql = "from AdminMenu where parentId = :parentId order by id asc";

        List<AdminMenu> menuList = sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("parentId", parentId)
                .list();

        return menuList;
    }

    /**
     * 根据组编号查找管理员权限
     * @param groupId
     * @return
     */
    public Set<String> findAdminPermissionByGroupId(int groupId) {
        List<String> menuList;
        if (groupId == 0) {
            String hql = "select m.permission from AdminMenu m where m.permission != ''";
            menuList = sessionFactory.getCurrentSession().createQuery(hql).list();
        } else {
            String hql = "select m.permission from AdminGroupPermission p,AdminMenu m where p.menuId = m.id and p.groupId = :groupId";
            menuList = sessionFactory.getCurrentSession().createQuery(hql)
                    .setParameter("groupId", groupId)
                    .list();
        }

        Set<String> permissionSet = new HashSet<String>(menuList);

        return permissionSet;
    }

}
