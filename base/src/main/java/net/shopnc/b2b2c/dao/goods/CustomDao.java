package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.Custom;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class CustomDao extends BaseDaoHibernate4<Custom> {
    /**
     * 根据分类查询自定义属性
     * @param categoryId
     * @return
     */
    public List<Custom> findByCategoryId(int categoryId) {
        String hql = "from Custom where categoryId = :categoryId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("categoryId", categoryId);
        return super.find(hql, map);
    }

    /**
     * 根据分类删除自定义属性
     * @param categoryId
     * @return
     */
    public void deleteByCategoryId(int categoryId) {
        List<Custom> customs = findByCategoryId(categoryId);
        super.deleteAll(customs);
    }
}
