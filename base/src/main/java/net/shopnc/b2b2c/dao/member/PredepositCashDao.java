package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.PredepositCash;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2015-12-24.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class PredepositCashDao extends BaseDaoHibernate4<PredepositCash> {

    /**
     * 通过提现单号查询提现单详情
     * @param cashSn
     * @return 提现单实体
     */
    public PredepositCash getCashInfoByCashSn(String cashSn){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("cashSn", "cashSn = :cashSn");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("cashSn", cashSn);
        return this.getCashInfo(where, params);
    }

    /**
     * 查询提现单详情
     * @param where 查询条件
     * @param params HQL参数值
     * @return 提现单实体
     */
    public PredepositCash getCashInfo(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "from PredepositCash where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        List<PredepositCash> list = super.find(hql, params);
        PredepositCash info = null;
        if (list.size() > 0) {
            info = list.get(0);
        }
        return info;
    }

    /**
     * 提现记录总数
     * @param where 查询条件
     * @param params HQL参数值
     * @return 提现记录总数
     */
    public Long findCashCount(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select count(*) from PredepositCash where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findCount(hql, params);
    }

    /**
     * 提现日志列表
     * @param where 查询条件
     * @param params HQL参数值
     * @param pageNo 当前页数
     * @param pageSize 分页条数
     * @return 提现日志列表
     */
    public List<PredepositCash> getCashListByPage(HashMap<String,String> where, HashMap<String,Object> params,int pageNo, int pageSize) {
        String hql = "from PredepositCash where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        hql += " order by cashId desc";
        return super.findByPage(hql, pageNo, pageSize, params);
    }

    /**
     * 更新提现记录
     * @param where 查询条件
     * @param update 更新条件
     * @param params HQL参数值
     * @return 更新成功或者失败
     */
    public Boolean updateByWhere(HashMap<String,String> where, HashMap<String,String> update, HashMap<String,Object> params){
        String hql = "update PredepositCash set ";
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

    /**
     * 删除提现记录
     * @param where 查询条件
     * @param params HQL参数值
     */
    public void deleteCash(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "delete PredepositCash where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        super.delete(hql, params);
    }
}