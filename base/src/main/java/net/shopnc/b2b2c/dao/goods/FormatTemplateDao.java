package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.constant.FormatSite;
import net.shopnc.b2b2c.domain.goods.FormatTemplate;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 关联板式
 * Created by shopnc.feng on 2015-12-17.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class FormatTemplateDao extends BaseDaoHibernate4<FormatTemplate> {
    /**
     * 根据店铺查询关联板式
     * @param storeId
     * @return
     */
    public List<FormatTemplate> findByStoreId(int storeId) {
        String hql = "from FormatTemplate where storeId = :storeId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        return find(hql, map);
    }
    /**
     * 根据店铺查询关联板式
     * @param storeId
     * @return
     */
    public List<FormatTemplate> findByStoreId(int storeId, int pageNo, int pageSize) {
        String hql = "from FormatTemplate where storeId = :storeId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        return findByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 根据店铺编号查询数量
     *
     * @param list
     * @param map
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<FormatTemplate> findByStoreId(List<String> list, HashMap<String, Object> map, int pageNo, int pageSize) {
        String where = "";
        for (String string : list) {
            where += " and " + string;
        }
        String hql = "from FormatTemplate where 1=1" + where;
        return findByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 根据店铺编号查询数量
     * @param list
     * @param map
     * @return
     */
    public long findCountByStoreId(List<String> list, HashMap<String, Object> map) {
        String where = "";
        for (String string : list) {
            where += " and " + string;
        }
        String hql = "select count(*) from FormatTemplate where 1=1" + where;
        return findCount(hql, map);
    }
    /**
     * 根据店铺查询顶部关联板式
     * @param storeId
     * @return
     */
    public List<FormatTemplate> findTopByStoreId(int storeId) {
        String hql = "from FormatTemplate where storeId = :storeId and formatSite = :formatSite";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        map.put("formatSite", FormatSite.TOP);
        return find(hql, map);
    }

    /**
     * 根据店铺查询底部关联板式
     * @param storeId
     * @return
     */
    public List<FormatTemplate> findBottomByStoreId(int storeId) {
        String hql = "from FormatTemplate where storeId = :storeId and formatSite = :formatSite";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        map.put("formatSite", FormatSite.BOTTOM);
        return find(hql, map);
    }
}
