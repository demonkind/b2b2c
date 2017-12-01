package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.GoodsSale;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.util.ShopHelper;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class GoodsSaleDao extends BaseDaoHibernate4<GoodsSale> {
    /**
     * 根据SKU编号删除GoodsSale
     * @param goodsIds
     */
    public void deleteByInGoodsIds(List<Integer> goodsIds) {
        String hql = "from GoodsSale where goodsId in (:goodsIds)";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsIds", goodsIds);
        List<GoodsSale> goodsSaleList = super.find(hql, map);
        super.deleteAll(goodsSaleList);
    }

    /**
     * 取带锁的库存信息
     * @param goodsId
     * @return
     */
    public GoodsSale getLockModelGoodsSaleInfo(int goodsId) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        String hql = "from GoodsSale LGoodsSale where goodsId = " + goodsId;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setLockMode("LGoodsSale", LockMode.PESSIMISTIC_WRITE);
        List<GoodsSale> goodsSaleList = query.list();
        return goodsSaleList.size()>0 ? goodsSaleList.get(0) : null;
    }

    /**
     * 下单更新库存销量
     * @param goodsId
     * @param commonId
     * @param buyNum
     */
    public void updateFromCreateOrders(int goodsId, int commonId, int buyNum) {
        String hql = "update GoodsSale set goodsSaleNum = goodsSaleNum + :buyNum, goodsStorage = goodsStorage - :buyNum where goodsId = :goodsId and goodsStorage >= :buyNum";
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("goodsId",goodsId);
        hashMap.put("buyNum",buyNum);
        super.update(hql, hashMap);

        //更新商品最后编辑时间
        hql = "update GoodsCommon set updateTime = :updateTime where commonId = :commonId";
        hashMap = new HashMap<>();
        hashMap.put("commonId",commonId);
        hashMap.put("updateTime", ShopHelper.getCurrentTimestamp());
        super.update(hql, hashMap);
    }

    /**
     * 取消更新库存销量
     * @param goodsId
     * @param commonId
     * @param buyNum
     */
    public void updateFromCancelOrders(int goodsId, int commonId, int buyNum) {
        String hql = "update GoodsSale set goodsSaleNum = goodsSaleNum - :buyNum, goodsStorage = goodsStorage + :buyNum where goodsId = :goodsId and goodsSaleNum >= :buyNum";
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("goodsId",goodsId);
        hashMap.put("buyNum",buyNum);
        super.update(hql,hashMap);

        //更新商品最后编辑时间
        hql = "update GoodsCommon set updateTime = :updateTime where commonId = :commonId";
        hashMap = new HashMap<>();
        hashMap.put("commonId",commonId);
        hashMap.put("updateTime", ShopHelper.getCurrentTimestamp());
        super.update(hql, hashMap);
    }
}
