package net.shopnc.b2b2c.dao.orders;

import net.shopnc.b2b2c.domain.orders.OrdersPay;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 订单支付
 * Created by shopnc on 2015/10/23.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class OrdersPayDao extends BaseDaoHibernate4<OrdersPay> {
    /**
     * 取得支付单信息
     * @param paySn
     * @return
     */
    public OrdersPay getOrdersPayInfo(long paySn) {
        String hql = "from OrdersPay where paySn= :paySn";
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("paySn",paySn);
        List<OrdersPay> ordersPayList = super.find(hql,hashMap);
        return ordersPayList.size()>0 ? ordersPayList.get(0) : null;
    }
}
