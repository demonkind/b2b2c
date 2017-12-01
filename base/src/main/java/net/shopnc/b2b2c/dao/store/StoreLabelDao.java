package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.StoreLabel;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreLabelDao extends BaseDaoHibernate4<StoreLabel> {
    /**
     * 查询店内商品分类
     * @param where
     * @param params
     * @return
     */
    public List<StoreLabel> findStoreLabel(List<String> where, HashMap<String, Object> params) {
        String hql = "from StoreLabel where 1=1 ";
        for (String key : where) {
            hql += (" and " + key);
        }
        return super.find(hql, params);
    }

    /**
     * 根据父级编号查询店内商品分类
     * @param parentId
     * @return
     */
    public List<StoreLabel> findByParentId(int parentId) {
        List<String> where = new ArrayList<String>();
        where.add("parentId = :parentId");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("parentId", parentId);
        return this.findStoreLabel(where, params);
    }
}
