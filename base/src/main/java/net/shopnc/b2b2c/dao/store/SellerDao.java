package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.util.ShopHelper;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 商家账号DAO
 * Created by dqw on 2015/11/20.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
@SuppressWarnings("unchecked")
public class SellerDao extends BaseDaoHibernate4<Seller> {

    /**
     * 使用商家用户名查找商家
     *
     * @param sellerName
     * @return
     */
    public Seller findSellerByName(String sellerName) {
        String hql = "from Seller where sellerName = :sellerName";

        Seller seller = (Seller) sessionFactory.getCurrentSession().createQuery(hql)
                .setString("sellerName", sellerName)
                .uniqueResult();

        return seller;
    }

    /**
     * 使用Email查找商家
     *
     * @param sellerEmail
     * @return
     */
    public Seller findSellerByEmail(String sellerEmail) {
        String hql = "from Seller where sellerEmail = :sellerEmail";

        Seller seller = (Seller) sessionFactory.getCurrentSession().createQuery(hql)
                .setString("sellerEmail", sellerEmail)
                .uniqueResult();

        return seller;
    }

    /**
     * 使用用户名密码查找商家
     *
     * @param sellerName
     * @param sellerPassword
     * @return
     */
    public Seller findSellerByNameAndPassword(String sellerName, String sellerPassword) {
        String hql = "from Seller where sellerName = :sellerName and sellerPassword = :sellerPassword";

        Seller seller = (Seller) sessionFactory.getCurrentSession().createQuery(hql)
                .setString("sellerName", sellerName)
                .setString("sellerPassword", ShopHelper.getMd5(sellerPassword))
                .uniqueResult();

        return seller;
    }

    /**
     * 根据店铺编号查找商家列表
     *
     * @param storeId
     * @return
     */
    public List<Seller> findSellerListByStoreId(int storeId) {
        String hql = "from Seller where storeId = :storeId";

        List<Seller> sellerList = (List<Seller>) sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("storeId", storeId)
                .list();

        return sellerList;
    }

    /**
     * 根据消息接收设置查询Seller
     * @param tplCode
     * @param storeId
     * @return
     */
    public List<Seller> findSellerByTplCodeAndStoreId(String tplCode, int storeId) {
        String hql = "select s from Seller s,StoreMessageSeller sms where s.sellerId = sms.sellerId and sms.storeId = :storeId and sms.tplCode = :tplCode";
        List<Seller> sellerList = (List<Seller>) sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("tplCode", tplCode)
                .setParameter("storeId", storeId)
                .list();
        return sellerList;
    }

    public Seller getSellerOwnerByStoreId(int storeId) {
        String hql = "from Seller where isStoreOwner = 1 and storeId = :storeId";
        Seller seller = (Seller) sessionFactory.getCurrentSession().createQuery(hql)
                .setParameter("storeId", storeId)
                .uniqueResult();
        return seller;
    }
}
