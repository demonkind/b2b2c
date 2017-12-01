package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.domain.Area;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 地区
 * Created by shopnc on 2015/10/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class AreaDao extends BaseDaoHibernate4<Area> {

    /**
     * 取得子级地区列表
     * @param parentId
     * @return
     */
    public List<Area> findByAreaParentId(int parentId) {
        String hql = "from Area where areaParentId = :areaParentId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("areaParentId",parentId);
        return super.find(hql, hashMap);
    }

    /**
     * 删除地区及所有子地区
     * @param areaIdList
     */
    public void delArea(List<Integer> areaIdList) {
        String hql = "delete from Area where areaId in(:areaIdList)";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("areaIdList",areaIdList);
        super.delete(hql, hashMap);
    }

    /**
     * 取得一二级地区
     * @return
     */
    public List<Area> getAreaListByDeep2() {
        String hql = "from Area where areaDeep = 1 or areaDeep = 2 order by areaId asc";
        return super.find(hql);
    }
}
