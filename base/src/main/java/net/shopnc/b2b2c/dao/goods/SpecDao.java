package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.Spec;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.entity.dtgrid.QueryUtils;
import javax.persistence.Query;
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
public class SpecDao extends BaseDaoHibernate4<Spec> {
    /**
     * 根据店铺编号查询规格
     * @param storeId
     * @param order
     * @return
     */
    public List<Spec> findByStoreId(Object storeId, String order) {
        String hql = "from Spec where storeId in (:storeId) order by " + order;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        return super.find(hql, map);
    }

    /**
     * 根据店铺编号查询规格数量
     * @param storeId
     * @return
     */
    public long findCountByStoreId(Object storeId){
        String hql = "select count(*) from Spec where storeId in (:storeId)";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        return super.findCount(hql, map);
    }
}
