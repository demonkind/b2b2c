package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.member.Consult;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-12
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class ConsultDao extends BaseDaoHibernate4<Consult> {
    /**
     * 咨询总数
     * @param where 查询条件
     * @param params 查询参数值
     * @return Long 咨询总数
     */
    public Long findConsultCount(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select count(*) from Consult where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findCount(hql, params);
    }
    /**
     * 查询待回复咨询总数
     * @param where 查询条件
     * @param params 查询参数值
     * @return Long 待回复咨询总数
     */
    public Long findNoReplyCount(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select count(*) from Consult where consultReply = :consultReply ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        params.put("consultReply", "");
        return super.findCount(hql, params);
    }
    /**
     * 查询会员未读回复数
     * @param where 查询条件
     * @param params 查询参数值
     * @return Long 会员未读咨询回复的总数
     */
    public Long findNoReadCount(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select count(*) from Consult where readState = :readState and consultReply != :consultReplyNeq ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        params.put("consultReplyNeq", "");
        params.put("readState", State.NO);
        return super.findCount(hql, params);
    }
    /**
     * 咨询分页列表
     * @param where 查询条件
     * @param params 查询参数值
     * @param pageNo 当前页数
     * @param pageSize 分页条数
     * @param orderBy 排序
     * @return 咨询列表
     */
    public List<Consult> getConsultListByPage(HashMap<String,String> where, HashMap<String,Object> params,int pageNo, int pageSize, String orderBy) {
        String hql = "from Consult where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        if (orderBy.length() > 0) {
            hql += " order by " + orderBy +",consultId desc";
        }else{
            hql += " order by consultId desc";
        }
        return super.findByPage(hql, pageNo, pageSize, params);
    }
    /**
     * 更新咨询
     * @param where 查询条件
     * @param update 更新字段
     * @param params HQL参数值
     * @return 更新成功或者失败
     */
    public Boolean updateByWhere(HashMap<String,String> where, HashMap<String,String> update, HashMap<String,Object> params){
        String hql = "update Consult set ";
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
     * 更新咨询回复为已读状态
     * @param where 查询条件
     * @param params HQL参数值
     * @return 更新成功或者失败
     */
    public Boolean updateToRead(HashMap<String,String> where, HashMap<String,Object> params){
        String hql = "update Consult set readState = " + State.YES + " where readState = :readState and consultReply != :consultReplyNeq ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        params.put("readState", State.NO);
        params.put("consultReplyNeq", "");
        super.update(hql,params);
        return true;
    }
    /**
     * 删除咨询
     * @param where 查询条件
     * @param params HQL参数值
     */
    public void deleteConsult(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "delete Consult where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        super.delete(hql, params);
    }
}