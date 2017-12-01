package net.shopnc.b2b2c.dao.orders;

import net.shopnc.b2b2c.domain.FreightArea;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 运费模板地区
 * Created by hbj on 2016/1/21.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class FreightAreaDao extends BaseDaoHibernate4<FreightArea> {
    /**
     * 删除运费模板详细地区设置
     * @param freightId
     */
    public void delFreightArea(int freightId) {
        String hql = "delete from FreightArea where freightId = :freightId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("freightId",freightId);
        super.delete(hql,hashMap);
    }

    /**
     * 根据freightIdList取得列表
     * @param freightIdList
     * @return
     */
    public List<FreightArea> getFreightAreaByFreightIdList(List<Integer> freightIdList) {
        String hql = "from FreightArea where freightId in (:freightIdList)";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("freightIdList", freightIdList);
        return super.find(hql,hashMap);
    }

    /**
     * 取得运费模板地区列表
     * @param freightId
     * @return
     */
    public List<FreightArea> getFreightAreaByFreightId(int freightId) {
        String hql = "from FreightArea where freightId = :freightId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("freightId",freightId);
        return super.find(hql,hashMap);
    }
}
