package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.StoreMessageSetting;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.apache.shiro.session.mgt.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Created by shopnc.feng on 2016-02-05.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreMessageSettingDao extends BaseDaoHibernate4<StoreMessageSetting> {
    /**
     * 根据模板编号和店铺编号，删除消息接收设置
     * @param tplCode
     * @param storeId
     */
    public void deleteByTplCodeAndStoreId(String tplCode, int storeId) {
        String hql = "delete StoreMessageSetting where storeId = :storeId and tplCode = :tplCode";
        HashMap<String, Object> map = new HashMap<>();
        map.put("tplCode", tplCode);
        map.put("storeId", storeId);
        super.delete(hql, map);
    }

    /**
     * 根据模板编号和店铺编号，查询接收设置
     * @param tplCode
     * @param storeId
     * @return
     */
    public StoreMessageSetting getByTplCodeAndStoreId(String tplCode, int storeId) {
        String hql = "from StoreMessageSetting where storeId = :storeId and tplCode = :tplCode";
        StoreMessageSetting storeMessageSetting = (StoreMessageSetting) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("storeId", storeId)
                .setParameter("tplCode", tplCode)
                .uniqueResult();
        return storeMessageSetting;
    }
}
