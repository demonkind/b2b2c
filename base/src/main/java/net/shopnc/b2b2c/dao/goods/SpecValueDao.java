package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.SpecValue;
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
public class SpecValueDao extends BaseDaoHibernate4<SpecValue> {
    /**
     * 根据规格编号查询规格值
     * @param specId
     * @return
     */
    public List<SpecValue> findBySpecId(int specId) {
        String hql = "from SpecValue where specId = :specId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("specId", specId);
        return super.find(hql, map);
    }

    /**
     * 根据规格编号查询总数
     * @param specId
     * @return
     */
    public long findCountBySpecId(int specId) {
        String hql = "select count(*) from SpecValue where specId = :specId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("specId", specId);
        return super.findCount(hql, map);
    }

    /**
     * 根据规格编号删除规格值
     * @param specId
     * @return
     */
    public void deleteBySpecId(int specId) {
        List<SpecValue> specValues = findBySpecId(specId);
        super.deleteAll(specValues);
    }
}
