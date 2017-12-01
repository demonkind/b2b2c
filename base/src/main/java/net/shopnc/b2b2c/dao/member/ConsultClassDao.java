package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.ConsultClass;
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
public class ConsultClassDao extends BaseDaoHibernate4<ConsultClass> {
    /**
     * 通过咨询类型ID删除咨询类型
     * @param classId 咨询类型编号
     * @return boolean 删除成功或者失败
     */
    public boolean deleteConsultById(int classId){
        String hql = "delete from ConsultClass where classId=:classId";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("classId", classId);
        super.delete(hql, params);
        return true;
    }
    /**
     * 获得咨询类型列表
     * @param orderBy 排序字段
     * @return 咨询类型列表
     */
    public List<ConsultClass> getConsultClassList(String orderBy)
    {
        String hql = "from ConsultClass order by ";
        if (orderBy.length() > 0) {
            hql += (orderBy + ",classSort asc");
        }else{
            hql += "classSort asc";
        }
        return super.find(hql);
    }
    /**
     * 获得咨询类型列表
     * @param where 查询条件
     * @param params 查询参数值
     * @return 咨询类型列表
     */
    public List<ConsultClass> getConsultClassList(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "from ConsultClass where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        hql += "order by classSort asc";
        return super.find(hql, params);
    }
}