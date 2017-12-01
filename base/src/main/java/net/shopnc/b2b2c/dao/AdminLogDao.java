package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.domain.AdminLog;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;

/**
 * 操作日志
 * Created by shopnc on 2015/11/2.
*/
@Repository
@Transactional(rollbackFor = {Exception.class})
public class AdminLogDao extends BaseDaoHibernate4<AdminLog> {
    /**
     * 删除N久之前的记录
     * @param date
     * @return
     */
    public void delAdminLogByTime(Date date) {
        String hql = "delete from AdminLog where createTime < :createTime";
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("createTime", date);
        super.delete(hql,params);
    }
}