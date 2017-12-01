package net.shopnc.b2b2c.dao.orders;

import net.shopnc.b2b2c.domain.FreightTemplate;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 运费模板
 * Created by hbj on 2016/1/21.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class FreightTemplateDao extends BaseDaoHibernate4<FreightTemplate> {
    /**
     * 根据店铺Id取得订单列表
     * @param storeId
     * @return
     */
    public List<FreightTemplate> getFreightTemplateListByStoreId(int storeId) {
        String hql = "from FreightTemplate where storeId = :storeId order by freightId desc";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("storeId",storeId);
        return super.find(hql, hashMap);
    }

    /**
     * 根据店铺Id取得订单列表
     * @param storeId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<FreightTemplate> getFreightTemplateListByStoreId(int storeId, int pageNo, int pageSize) {
        String hql = "from FreightTemplate where storeId = :storeId order by freightId desc";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("storeId",storeId);
        return super.findByPage(hql, pageNo, pageSize, hashMap);
    }

    /**
     * 取得店铺运费模板数量
     * @param storeId
     * @return
     */
    public long getFreightTemplateCountByStoreId(int storeId) {
        String hql = "select count(*) from FreightTemplate where storeId = :storeId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("storeId",storeId);
        return super.findCount(hql, hashMap);
    }

    /**
     * 运费模板详情
     * @param freightId
     * @param storeId
     * @return
     */
    public FreightTemplate getFreightTemplateInfo(int freightId, int storeId) {
        String hql = "from FreightTemplate where freightId = :freightId and storeId = :storeId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("storeId",storeId);
        hashMap.put("freightId",freightId);
        List<FreightTemplate> freightTemplateList = super.find(hql,hashMap);
        return freightTemplateList.size()>0 ? freightTemplateList.get(0) : null;
    }
}
