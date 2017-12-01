package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.Attribute;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class AttributeDao extends BaseDaoHibernate4<Attribute> {
    /**
     * 根据商品分类编号查询属性
     * @param categoryId
     * @return
     */
    public List<Attribute> findByCategoryId(int categoryId) {
        String hql = "from Attribute where categoryId = :categoryId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("categoryId", categoryId);
        return super.find(hql, map);
    }

    /**
     * 根据商品分类编号查询属性
     * @param categoryId
     * @param order
     * @return
     */
    public List<Attribute> findByCategoryId(int categoryId, String order) {
        String hql = "from Attribute where categoryId = :categoryId order by " + order;
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("categoryId", categoryId);
        return super.find(hql, map);
    }
}
