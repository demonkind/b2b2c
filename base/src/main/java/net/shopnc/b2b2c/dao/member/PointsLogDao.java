package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.PointsLog;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2015-12-01.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class PointsLogDao extends BaseDaoHibernate4<PointsLog> {

    /**
     * 积分日志总数
     * @param where 查询条件
     * @param params HQL参数值
     * @return 积分日志数
     */
    public Long findPointsLogCount(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select count(*) from PointsLog where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findCount(hql, params);
    }

    /**
     * 日志列表
     * @param where 查询条件
     * @param params HQL参数值
     * @param pageNo 当前分页
     * @param pageSize 分页条数
     * @return 积分日志列表
     */
    public List<PointsLog> getPointsLogListByPage(HashMap<String,String> where, HashMap<String,Object> params,int pageNo, int pageSize) {
        String hql = "from PointsLog where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        hql += " order by logId desc";
        return super.findByPage(hql, pageNo, pageSize, params);
    }

}