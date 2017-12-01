package net.shopnc.b2b2c.dao.orders;

import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 订单商品
 * Created by shopnc on 2015/10/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class OrdersGoodsDao extends BaseDaoHibernate4<OrdersGoods> {

    /**
     * 取前N条
     * @param goodsName
     * @param memberId
     * @param limit
     * @return
     */
    public List<OrdersGoods> getOrdersGoodsListForMemberLimit(String goodsName, int memberId, int limit) {
        String hql = "from OrdersGoods where memberId = :memberId and goodsName like :goodsName";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId",memberId);
        hashMap.put("goodsName","%" + goodsName + "%");
        return super.findByPage(hql,1,limit,hashMap);
    }

    /**
     * 取前N条
     * @param goodsName
     * @param storeId
     * @param limit
     * @return
     */
    public List<OrdersGoods> getOrdersGoodsListForStoreLimit(String goodsName, int storeId, int limit) {
        String hql = "from OrdersGoods where storeId = :storeId and goodsName like :goodsName";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("storeId",storeId);
        hashMap.put("goodsName","%" + goodsName + "%");
        return super.findByPage(hql,1,limit,hashMap);
    }

    /**
     * 根据商品SKU编号查询订单商品数量
     * @param goodsId
     * @return
     */
    public long findCountByGoodsId(int goodsId) {
        String hql = "select count(*) from OrdersGoods where goodsId = :goodsId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsId", goodsId);
        return findCount(hql, map);
    }

    /**
     * 根据商品SKU编号查询订单商品
     * @param goodsId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> findOrderGoodsVoByGoodsId(int goodsId, int pageNo, int pageSize) {
        String hql = "select new net.shopnc.b2b2c.vo.orders.OrdersGoodsVo(og, o) from OrdersGoods og, Orders o where og.ordersId = o.ordersId and og.goodsId = :goodsId order by o.createTime desc";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("goodsId", goodsId);
        return findObjectByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 取得订单商品列表
     * @param ordersId
     * @return
     */
    public List<OrdersGoods> getOrdersGoodsListByOrdersId(int ordersId) {
        String hql = "from OrdersGoods where ordersId = :ordersId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("ordersId",ordersId);
        return super.find(hql, hashMap);
    }
}
