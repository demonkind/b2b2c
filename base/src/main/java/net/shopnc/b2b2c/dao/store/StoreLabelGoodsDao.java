package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.StoreLabelGoods;
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
public class StoreLabelGoodsDao extends BaseDaoHibernate4<StoreLabelGoods> {
    /**
     * 根据SPU编号查询店内分类与商品关系
     * @param commonId
     * @return
     */
    public List<StoreLabelGoods> findByCommonId(int commonId) {
        String hql = "from StoreLabelGoods where commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return find(hql, map);
    }

    /**
     * 根据SPU删除店内分类与商品关系
     * @param commonId
     */
    public void deleteByCommonId(int commonId) {
        List<StoreLabelGoods> storeLabelGoodsList = findByCommonId(commonId);
        deleteAll(storeLabelGoodsList);
    }
}
