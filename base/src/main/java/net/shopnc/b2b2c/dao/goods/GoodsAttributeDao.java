package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.GoodsAttribute;
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
public class GoodsAttributeDao extends BaseDaoHibernate4<GoodsAttribute> {
    /**
     * 根据SPU查询商品与属性关系
     * @param commonId
     * @return
     */
    public List<GoodsAttribute> findByCommonId(int commonId) {
        String hql = "from GoodsAttribute where commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return find(hql, map);
    }

    /**
     * 根据SPU查询GoodsAttrVo列表
     * @param commonId
     * @return
     */
    public List<Object> findGoodsAttrVoByCommonId(int commonId) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsAttrVo(a, av) from GoodsAttribute ga, Attribute a, AttributeValue av where ga.attributeValueId = av.attributeValueId and a.attributeId = av.attributeId and ga.commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return findObject(hql, map);
    }

    /**
     * 根据SPU删除分类与属性关系
     * @param commonId
     */
    public void deleteByCommonId(int commonId) {
        List<GoodsAttribute> goodsAttributeList = this.findByCommonId(commonId);
        deleteAll(goodsAttributeList);
    }
}
