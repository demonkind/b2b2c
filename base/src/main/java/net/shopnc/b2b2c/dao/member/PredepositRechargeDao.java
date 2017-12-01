package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.PredepositRecharge;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2015-12-24.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class PredepositRechargeDao extends BaseDaoHibernate4<PredepositRecharge> {

    /**
     * 通过充值单号查询充值单详情
     * @param rechargeSn
     * @return 充值单实体
     */
    public PredepositRecharge getRechargeInfoByRechargeSn(String rechargeSn){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("rechargeSn", "rechargeSn = :rechargeSn");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("rechargeSn", rechargeSn);
        return this.getRechargeInfo(where, params);
    }

    /**
     * 查询充值单详情
     * @param where 查询条件
     * @param params HQL参数值
     * @return 充值单实体
     */
    public PredepositRecharge getRechargeInfo(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "from PredepositRecharge where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        List<PredepositRecharge> list = super.find(hql, params);
        PredepositRecharge info = null;
        if (list.size() > 0) {
            info = list.get(0);
        }
        return info;
    }
    /**
     * 通过充值ID查询充值单详情并加锁
     * @param rechargeId 充值单编号
     * @return 充值单实体
     */
    public PredepositRecharge getRechargeInfoByRechargeIdAndLock(int rechargeId)
    {
        //加锁必须写别名
        String hql = "from PredepositRecharge LPredepositRecharge where rechargeId = :rechargeId ";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("rechargeId", rechargeId);
        query.setLockMode("LPredepositRecharge", LockMode.PESSIMISTIC_WRITE);
        List<PredepositRecharge> list = query.list();
        PredepositRecharge info = null;
        if (list.size() > 0) {
            info = list.get(0);
        }
        return info;
    }

    /**
     * 充值单记录总数
     * @param where 查询条件
     * @param params HQL参数值
     * @return 充值单记录总数
     */
    public Long findRechargeCount(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select count(*) from PredepositRecharge where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findCount(hql, params);
    }

    /**
     * 充值单分页列表
     * @param where 查询条件
     * @param params HQL参数值
     * @param pageNo 当前页数
     * @param pageSize 分页条数
     * @return 充值单分页列表
     */
    public List<PredepositRecharge> getRechargeListByPage(HashMap<String,String> where, HashMap<String,Object> params,int pageNo, int pageSize) {
        String hql = "from PredepositRecharge where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        hql += " order by rechargeId desc";
        return super.findByPage(hql, pageNo, pageSize, params);
    }
    /**
     * 删除充值单记录
     * @param where 查询条件
     * @param params HQL参数值
     */
    public void deleteRecharge(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "delete PredepositRecharge where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        super.delete(hql, params);
    }

    /**
     * 更新充值单记录
     * @param where 查询条件
     * @param update 更新条件
     * @param params HQL参数值
     * @return 更新成功或者失败
     */
    public Boolean updateByWhere(HashMap<String,String> where, HashMap<String,String> update, HashMap<String,Object> params){
        String hql = "update PredepositRecharge set ";
        //update
        int i=0;
        for (String key : update.keySet()) {
            if (i > 0) {
                hql += (", " + key + "=:" + update.get(key));
            }else{
                hql += (key + "=:" + update.get(key));
            }
            i++;
        }
        //where
        hql +=" where 1=1 ";
        for (String key : where.keySet()) {
            hql += " and " + key + "=:" + where.get(key);
        }
        super.update(hql,params);
        return true;
    }
}