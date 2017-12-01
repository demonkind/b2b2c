package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.AttributeValue;
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
public class AttributeValueDao extends BaseDaoHibernate4<AttributeValue> {
    /**
     * 根据属性编号查询属性值列表
     * @param attributeId
     * @return
     */
    public List<AttributeValue> findByAttributeId(int attributeId) {
        String hql = "from AttributeValue where attributeId = :attributeId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("attributeId", attributeId);
        return super.find(hql, map);
    }

    /**
     * 根据属性编号删除属性值表
     * @param attributeId
     * @return
     */
    public void deleteByAttributeId(int attributeId) {
        List<AttributeValue> attributeValues = findByAttributeId(attributeId);
        super.deleteAll(attributeValues);
    }
}
