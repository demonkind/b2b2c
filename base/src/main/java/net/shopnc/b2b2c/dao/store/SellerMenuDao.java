package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.SellerMenu;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 商家菜单
 * Created by dqw on 2015/11/26.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
@SuppressWarnings("unchecked")
public class SellerMenuDao extends BaseDaoHibernate4<SellerMenu> {

    /**
     * 获取商家所有菜单
     * @return
     */
    public List<SellerMenu> getSellerMenuList() {
        String hql = "from SellerMenu order by id asc";

        List<SellerMenu> sellerMenuList = sessionFactory.getCurrentSession().createQuery(hql).list();

        return sellerMenuList;
    }

    /**
     * 根据组编号查找商家权限
     * @param groupId
     * @return
     */
    public List<SellerMenu> findPremissionsByGroupId(int groupId) {
        String hql = "select m " +
                "from SellerGroupMenu g, SellerMenu m " +
                "where m.id = g.menuId and g.groupId = :groupId";

        List<SellerMenu> list = sessionFactory.getCurrentSession().createQuery(hql)
                .setInteger("groupId", groupId)
                .list();

        return list;
    }

}
