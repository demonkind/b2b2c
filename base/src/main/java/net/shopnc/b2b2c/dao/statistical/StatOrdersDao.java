package net.shopnc.b2b2c.dao.statistical;

import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.vo.statistical.StatOrdersVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-02-03
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StatOrdersDao extends BaseDaoHibernate4<Orders> {

    /**
     * 查询订单某小时销售情况
     * @param where 查询条件
     * @param params HQL参数
     * @return 查询订单某小时销售情况统计列表
     */
    public List<StatOrdersVo> getOrdersListByHours(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select new net.shopnc.b2b2c.vo.statistical.StatOrdersVo(orders, SUM(orders.ordersAmount) as ordersAmountSum, Count(orders.ordersId) as ordersCount, hour(orders.createTime) as createTimeHour) from Orders orders where 1=1 ";
        if (where!=null && where.size()>0) {
            for (String key : where.keySet()) {
                hql += (" and " + where.get(key));
            }
        }
        hql += " group by hour(orders.createTime) ";
        hql += " order by createTimeHour asc";

        if (params==null || params.size()<=0) {
            params = new HashMap<>();
        }
        List<Object> statList = super.findObject(hql, params);
        List<StatOrdersVo> statListNew = new ArrayList<>();
        if (statList!=null && statList.size()>0) {
            for (int j = 0; j < statList.size(); j++) {
                statListNew.add((StatOrdersVo)statList.get(j));
            }
        }
        return statListNew;
    }
}