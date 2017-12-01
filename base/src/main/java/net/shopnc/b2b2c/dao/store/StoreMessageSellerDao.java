package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.StoreMessageSeller;
import net.shopnc.b2b2c.domain.store.StoreMessageSetting;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Created by shopnc.feng on 2016-02-05.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreMessageSellerDao extends BaseDaoHibernate4<StoreMessageSeller> {
    /**
     * 根据模板编号和店铺编号删除子账号接受设置
     * @param tplCode
     * @param storeId
     */
    public void deleteByTplCodeAndStoreId(String tplCode, int storeId) {
        String hql = "delete StoreMessageSeller where storeId = :storeId and tplCode = :tplCode";
        HashMap<String, Object> map = new HashMap<>();
        map.put("tplCode", tplCode);
        map.put("storeId", storeId);
        super.delete(hql, map);
    }

    /***
     * 根据模板编号和商户编号查询子账号接受设置
     * @param tplCode
     * @param sellerId
     * @return
     */
    public StoreMessageSeller getByTplCodeAndSellerId(String tplCode, int sellerId) {
        String hql = "from StoreMessageSeller where sellerId = :sellerId and tplCode = :tplCode";
        StoreMessageSeller StoreMessageSeller = (StoreMessageSeller) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("sellerId", sellerId)
                .setParameter("tplCode", tplCode)
                .uniqueResult();
        return StoreMessageSeller;
    }
}
