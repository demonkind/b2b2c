package net.shopnc.b2b2c.dao.contract;

import java.util.HashMap;
import java.util.List;

import net.shopnc.b2b2c.domain.contract.ContractLog;
import net.shopnc.common.dao.BaseDaoHibernate4;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ContractLogDao extends BaseDaoHibernate4<ContractLog> {
	/**
	 * 卖家保障日志列表
	 */
	public List<ContractLog> getContractCostlog(int pageNo,int pageSize,List<String> condition, HashMap<String,Object> params){
		StringBuffer hql=new StringBuffer("from ContractLog where 1=1 ");
		for (String con : condition) {
            hql.append(" and " + con);
        }
		hql.append(" order by logAddtime desc ");
		List<ContractLog> list=super.findByPage(hql.toString(), pageNo, pageSize, params);;
		return list;
	}
	
	/**
	 * 卖家保障日志数据总条数
	 */
	public long getContractCostlogCount(List<String> condition, HashMap<String,Object> params){
		StringBuffer hql=new StringBuffer("select count(*) from ContractLog where 1=1 ");
		for (String con : condition) {
            hql.append(" and " + con);
        }
		long count=super.findCount(hql.toString(), params);
		return count;
	}
}
