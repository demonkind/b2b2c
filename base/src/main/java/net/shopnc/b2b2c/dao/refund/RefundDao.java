package net.shopnc.b2b2c.dao.refund;

import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 退款原因dao
 * Created by cj on 2016/2/1.
 */

@Repository
@Transactional(rollbackFor = {Exception.class})
public class RefundDao extends BaseDaoHibernate4<Refund> {

    /**
     * 根据条件获取 头一条 RefundVo 信息
     * @param condition
     * @param params
     * @return
     */
    public RefundItemVo getRefundVoByParams(List<Object> condition, HashMap<String,Object> params) {
        String hql = "select new net.shopnc.b2b2c.vo.refund.RefundItemVo(o) from Refund o where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        List<Object> list = super.findObject(hql,params);
        return list.size()>0 ? (RefundItemVo)list.get(0) : null;
    }
    
    /**
     * 根据条件获取 最新头一条 RefundVo 信息
     * @param condition
     * @param params
     * @return
     */
    public RefundItemVo getRefundVoByParamsLatest(List<Object> condition, HashMap<String,Object> params, List<Object> sorts) {
        String hql = "select new net.shopnc.b2b2c.vo.refund.RefundItemVo(o) from Refund o where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        
        for (int i=0; i<sorts.size(); i++) {
            hql += (" " + sorts.get(i));
        }
        List<Object> list = super.findObject(hql,params);
        return list.size()>0 ? (RefundItemVo)list.get(0) : null;
    }

    /**
     *  根据条件获取 头一条 Refund 信息
     * @param condition
     * @param params
     * @return
     */
    public Refund getRefundByParams(List<Object> condition, HashMap<String,Object> params){
        String hql = "from Refund  where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        List<Object> list = super.findObject(hql,params);
        return list.size()>0 ? (Refund)list.get(0) : null;
    }
    /**
     * 退款退货数量
     * @param condition
     * @param params
     * @return
     */
    public long getRefundListCount(List<Object> condition, HashMap<String,Object> params) {
        String hql = "select count(*) from Refund where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        return super.findCount(hql, params);
    }

    public List<Object> getRefundVoList(List<Object> condition, HashMap<String,Object> params, int pageNo, int pageSize) {
        String hql = "select new net.shopnc.b2b2c.vo.refund.RefundItemVo(o) from Refund o where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by refundId desc";
        return super.findObjectByPage(hql, pageNo, pageSize, params);
    }

    /**
     * 根据条件获取 RefundVo 列表
     * @param condition
     * @param params
     * @return
     */
    public List<Object> getRefundVoList(List<Object> condition, HashMap<String,Object> params){
        String hql = "select new net.shopnc.b2b2c.vo.refund.RefundItemVo(o) from Refund o where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by addTime desc";
        return super.findObject(hql , params);
    }
    /**
     * 根据条件获取退换货列表
     *
     * @param condition
     * @param params
     * @return
     */
    public List<Refund> getRefundList(List<Object> condition, HashMap<String, Object> params) {
        String hql = "from Refund where 1=1";
        for (int i = 0; i < condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by ordersId desc";
        return super.find(hql, params);
    }

    /**
     * 根据订单id 获取未同意的整单退款
     * @param ordersId
     * @return
     */
    public int getRefundDisagreeCount(int ordersId) {
        String hql = "select count(*) from Refund where " +
                "ordersId = :ordersId and " +
                "goodsId =0 and " +
                "sellerState < :sellerState"
                ;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("ordersId", ordersId);
        hashMap.put("sellerState", RefundState.SELLER_STATE_DISAGREE);
        return (int) super.findCount(hql, hashMap);
    }

    /**
     * 获取正在等待卖家审核的退款申请数量
     * @param ordersId
     * @return
     */
    public int getRefundWaitingCount(int ordersId,int ordersGoodsId,int memberId){
        String hql = "select count(*) from Refund where " +
                "ordersId = :ordersId and " +
                "ordersGoodsId = :ordersGoodsId and " +
                "memberId = :memberId and " +
                "refundState != :refundState ";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("ordersId", ordersId);
        hashMap.put("ordersGoodsId", ordersGoodsId);
        hashMap.put("memberId", memberId);
        hashMap.put("refundState", RefundState.REFUND_STATE_FINISH);
        return (int) super.findCount(hql, hashMap);

    }
    public RefundItemVo getRefundWaitingRefundVo(int ordersId){
        String hql =  "select new net.shopnc.b2b2c.vo.refund.RefundItemVo(o) from Refund o where " +
                "ordersId=:ordersId and " +
                "refundState !=:refundState";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("ordersId", ordersId);
        hashMap.put("refundState", RefundState.REFUND_STATE_FINISH);
        List<Object> products = super.findObject(hql, hashMap);
        return products.size()>0 ? (RefundItemVo)products.get(0) : null;
    }

    /**
     * 获取处理中退款总数
     * @return
     */
    public long getRefundWaitingCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from Refund where refundType = :refundType and refundState = :refundState ";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("refundType", RefundState.REFUND_TYPE_REFUND);
        params.put("refundState", RefundState.REFUND_STATE_WAITING);
        return super.findCount(hql, params);
    }
    /**
     * 获取处理中退货总数
     * @return
     */
    public long getReturnWaitingCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from Refund where refundType = :refundType and refundState = :refundState ";
        for (String whereKey : whereList) {
            hql +=  " and " + whereKey;
        }
        params.put("refundType", RefundState.REFUND_TYPE_RETURN);
        params.put("refundState", RefundState.REFUND_STATE_WAITING);
        return super.findCount(hql, params);
    }

}
