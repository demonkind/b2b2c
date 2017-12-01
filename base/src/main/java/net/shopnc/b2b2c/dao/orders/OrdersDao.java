package net.shopnc.b2b2c.dao.orders;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 订单
 * Created by shopnc on 2015/10/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class OrdersDao extends BaseDaoHibernate4<Orders> {

    /**
     * 订单列表
     * @param payId
     * @return
     */
    public List<Orders> getOrdersList(int payId) {
        String hql = "from Orders where payId = :payId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("payId",payId);
        return super.find(hql, hashMap);
    }

    /**
     * 订单数量
     * @param condition
     * @param params
     * @return
     */
    public long getOrdersCount(List<Object> condition, HashMap<String, Object> params) {
        String hql = "select count(*) from Orders where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        return super.findCount(hql, params);
    }

    /**
     * 订单列表
     * @param condition
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> getOrdersVoList(List<Object> condition, HashMap<String,Object> params, int pageNo, int pageSize) {
        String hql = "select new net.shopnc.b2b2c.vo.orders.OrdersVo(o) from Orders o where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by ordersId desc";
        return super.findObjectByPage(hql, pageNo, pageSize, params);
    }

    /**
     * 订单列表
     * @param condition
     * @param params
     * @param pageNo
     * @param pageSize
     * @param order
     * @return
     */
    public List<Object> getOrdersVoList(List<Object> condition, HashMap<String,Object> params, int pageNo, int pageSize,String order) {
        String hql = "select new net.shopnc.b2b2c.vo.orders.OrdersVo(o) from Orders o where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by " + order;
        return super.findObjectByPage(hql, pageNo, pageSize, params);
    }

    /**
     * 订单列表
     * @param condition
     * @param params
     * @return
     */
    public List<Object> getOrdersVoList(List<Object> condition, HashMap<String,Object> params) {
        String hql = "select new net.shopnc.b2b2c.vo.orders.OrdersVo(o) from Orders o where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by ordersId desc";
        return super.findObject(hql, params);
    }

    /**
     * 商品列表
     * @param ordersIdList
     * @return
     */
    public List<Object> getOrdersGoodsVoList(List<Integer> ordersIdList) {
        if (ordersIdList.size() > 0) {
            String hql = "select new net.shopnc.b2b2c.vo.orders.OrdersGoodsVo(o) from OrdersGoods o where ordersId in (:orderIdList)";
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("orderIdList",ordersIdList);
            return super.findObject(hql,hashMap);
        } else {
            return null;
        }
    }

    /**
     * 订单表详情
     * @param whereItems
     * @param params
     * @return
     */
    public Orders getOrdersInfo(List<Object> whereItems, HashMap<String,Object> params) {
        String hql = "from Orders where 1=1";
        for (int i=0; i<whereItems.size(); i++) {
            hql += (" and " + whereItems.get(i));
        }
        List<Orders> ordersList = super.find(hql,params);
        return ordersList.size()>0 ? ordersList.get(0) : null;
    }

    /**
     * 更新订单
     * @param setItems
     * @param whereItems
     * @param params
     */
    public void editOrders(List<Object> setItems, List<Object> whereItems, HashMap<String,Object> params) {
        String setHql = "";
        String whereHql = "";
        String hql = "";
        for (int i=0; i<setItems.size(); i++) {
            setHql += (setItems.get(i) + ",");
        }
        Pattern pattern = Pattern.compile("^,+|,+$");
        setHql = pattern.matcher(setHql).replaceAll("");

        for (int i=0; i<whereItems.size(); i++) {
            whereHql += (" and " + whereItems.get(i));
        }

        hql = "update Orders set " + setHql + " where 1=1 " + whereHql;
        super.update(hql,params);
    }

    /**
     * 订单Vo详情
     * @param condition
     * @param params
     * @return
     */
    public OrdersVo getOrdersVoInfo(List<Object> condition, HashMap<String,Object> params) {
        String hql = "select new net.shopnc.b2b2c.vo.orders.OrdersVo(o) from Orders o where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        List<Object> list = super.findObject(hql,params);
        return list.size()>0 ? (OrdersVo)list.get(0) : null;
    }

    /**
     * 订单列表
     * @param condition
     * @param params
     * @return
     */
    public List<Orders> getOrdersList(List<Object> condition, HashMap<String,Object> params) {
        String hql = "from Orders where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by ordersId desc";
        return super.find(hql, params);
    }

    /**
     * 取带锁的订单信息
     * @param ordersId
     * @return
     */
    public Orders getLockModelOrdersInfo(int ordersId) {
        String hql = "from Orders Lorders where ordersId = " + ordersId;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setLockMode("Lorders", LockMode.PESSIMISTIC_WRITE);
        List<Orders> ordersList = query.list();
        return ordersList.size()>0 ? ordersList.get(0) : null;
    }

    /**
     * 取得店铺最早的订单信息
     * @param storeId
     * @return
     */
    public Orders getFirstOrdersInfo(int storeId) {
        String hql = "from Orders where storeId = :storeId order by ordersId asc";
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("storeId", storeId);
        List<Orders> ordersList = super.find(hql, hashMap);
        return ordersList.size()>0 ? ordersList.get(0) : null;
    }

    /**
     * 订单列表
     * @param condition
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Object> getOrdersList(List<Object> condition, HashMap<String,Object> params, int pageNo, int pageSize) {
        String hql = "from Orders where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by ordersId desc";
        return super.findObjectByPage(hql, pageNo, pageSize, params);
    }

    /**
     * 交易中的订单总数
     * @param whereList
     * @param params
     * @return
     */
    public long getProgressingCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from Orders where ordersState in (:ordersState) ";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("ordersState", new ArrayList<Integer>(){{add(OrdersOrdersState.NEW);add(OrdersOrdersState.PAY);add(OrdersOrdersState.SEND);}});
        return findCount(hql, params);
    }

    /**
     * 待付款订单总数
     * @param whereList
     * @param params
     * @return
     */
    public long getOrdersNewCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from Orders where ordersState = :ordersState ";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("ordersState", OrdersOrdersState.NEW);
        return findCount(hql, params);
    }

    /**
     * 待发货订单总数
     * @param whereList
     * @param params
     * @return
     */
    public long getOrdersPayCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from Orders where ordersState = :ordersState ";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("ordersState", OrdersOrdersState.PAY);
        return findCount(hql, params);
    }
    
    public List<Map<String, Object>> findTotalPurchasesOfGoods(int commonId,int memberId){
    	String hql="select sum(og.buyNum) from OrdersGoods og,Orders o where o.ordersId=og.ordersId and og.commonId=:commonId and og.memberId=:memberId "
    			+  " and o.ordersState not in (:ids) ";
    	List<Integer> list=new ArrayList<Integer>();
    	list.add(0);
    	list.add(10);
    	Query query = sessionFactory.getCurrentSession().createQuery(hql);
    	query.setInteger("commonId", commonId);
    	query.setInteger("memberId", memberId);
    	query.setParameterList("ids", list);
    	query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	return query.list();
    	
    }
}
