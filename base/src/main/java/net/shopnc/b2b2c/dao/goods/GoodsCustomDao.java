package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.GoodsCustom;
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
public class GoodsCustomDao extends BaseDaoHibernate4<GoodsCustom> {
    /**
     * 根据SPU编号查询商品与自定义属性关系
     * @param commonId
     * @return
     */
    public List<GoodsCustom> findByCommonId(int commonId) {
        String hql = "from GoodsCustom where commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return find(hql, map);
    }

    /**
     * 根据SPU编号查询GoodsAttrVo列表
     * @param commonId
     * @return
     */
    public List<Object> findGoodsAttrVoByCommonId(int commonId) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsAttrVo(c, gc) from GoodsCustom gc, Custom c where gc.customId = c.customId and gc.commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return findObject(hql, map);
    }

    /**
     * 根据SPU编号删除商品与自定义属性关系
     * @param commonId
     */
    public void deleteByCommonId(int commonId) {
        List<GoodsCustom> goodsCustomList = this.findByCommonId(commonId);
        super.deleteAll(goodsCustomList);
    }
}
