package net.shopnc.b2b2c.dao.admin;

import net.shopnc.b2b2c.domain.admin.Admin;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.util.ShopHelper;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dqw on 2015/12/29.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class AdminDao extends BaseDaoHibernate4<Admin> {

    /**
     * 根据用户名查找管理员
     * @param name
     * @return
     */
    public Admin findByName(String name) {
        String hql = "from Admin where name = :name";

        return (Admin) sessionFactory.getCurrentSession().createQuery(hql)
                .setString("name", name)
                .uniqueResult();
    }

    /**
     * 根据用户名密码查找管理员
     * @param name
     * @param password
     * @return
     */
    public Admin findByNameAndPassword(String name, String password) {
        String hql = "from Admin where name = :name and password = :password";

        return (Admin) sessionFactory.getCurrentSession().createQuery(hql)
                .setString("name", name)
                .setString("password", ShopHelper.getMd5(password))
                .uniqueResult();
    }
}

