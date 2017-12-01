package net.shopnc.b2b2c.dao.statistical;

import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.b2b2c.vo.statistical.StatOrdersGoodsVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-02-05
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StatOrdersGoodsDao extends BaseDaoHibernate4<OrdersGoods> {

    /**
     * 订单商品销售统计
     * @param num 查询条数
     * @param where 查询条件
     * @param params HQL参数值
     * @param groupBy 分组条件
     * @param orderBy 排序条件
     * @return 订单商品销售统计列表
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsList(int num, HashMap<String,String> where, HashMap<String,Object> params, String groupBy, String orderBy)
    {
        String hql = "select new net.shopnc.b2b2c.vo.statistical.StatOrdersGoodsVo(ordersGoods, SUM(ordersGoods.goodsPrice) as ordersGoodsPriceSum, SUM(ordersGoods.buyNum) as ordersGoodsBuyNumSum) from OrdersGoods ordersGoods, Orders orders where ordersGoods.ordersId=orders.ordersId ";

        if (where!=null && where.size()>0) {
            for (String key : where.keySet()) {
                hql += (" and " + where.get(key));
            }
        }
        if (groupBy!=null && groupBy.length()>0) {
            hql += (" group by " + groupBy);
        }
        if (orderBy!=null && orderBy.length()>0) {
            hql += (" order by " + orderBy);
        }else{
            hql += (" order by ordersGoods.ordersId");
        }
        if (params==null || params.size()<=0) {
            params = new HashMap<>();
        }
        List<Object> statList = super.findObjectByPage(hql, 1, num, params);
        List<StatOrdersGoodsVo> statListNew = new ArrayList<>();
        if (statList!=null && statList.size()>0) {
            for (int j = 0; j < statList.size(); j++) {
                statListNew.add((StatOrdersGoodsVo)statList.get(j));
            }
        }
        return statListNew;
    }

    /**
     * 查询商品分类某天销售情况
     * @param where 查询条件
     * @param params HQL参数值
     * @return
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsListByDay(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select new net.shopnc.b2b2c.vo.statistical.StatOrdersGoodsVo(ordersGoods, SUM(ordersGoods.goodsPrice) as ordersGoodsPriceSum, SUM(ordersGoods.buyNum) as ordersGoodsBuyNumSum, substring(str(orders.createTime),1,10) as createTimeShort) from OrdersGoods ordersGoods, Orders orders where ordersGoods.ordersId=orders.ordersId ";
        if (where!=null && where.size()>0) {
            for (String key : where.keySet()) {
                hql += (" and " + where.get(key));
            }
        }
        hql += " group by substring(str(orders.createTime),1,10) ";
        hql += " order by ordersGoods.ordersId";

        if (params==null || params.size()<=0) {
            params = new HashMap<>();
        }
        List<Object> statList = super.findObject(hql, params);
        List<StatOrdersGoodsVo> statListNew = new ArrayList<>();
        if (statList!=null && statList.size()>0) {
            for (int j = 0; j < statList.size(); j++) {
                statListNew.add((StatOrdersGoodsVo)statList.get(j));
            }
        }
        return statListNew;
    }
}