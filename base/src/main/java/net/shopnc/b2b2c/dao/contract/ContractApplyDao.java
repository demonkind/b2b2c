package net.shopnc.b2b2c.dao.contract;

import java.util.HashMap;
import java.util.List;

import net.shopnc.b2b2c.domain.contract.ContractApply;
import net.shopnc.common.dao.BaseDaoHibernate4;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ContractApplyDao extends BaseDaoHibernate4<ContractApply> {

	/**
	 * 查询服务保障申请
	 */
	public ContractApply getContractApply(List<String> condition,HashMap<String,Object> params){
		StringBuffer hql=new StringBuffer("from ContractApply where 1=1 ");
		for(String str : condition){
			hql.append(" and "+str);
		}
		hql.append(" order by ctaAddtime desc ");
		List<ContractApply> list=super.find(hql.toString(), params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据状态查询申请数
	 */
	public long countApply(int ctaAuditstate){
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("ctaAuditstate", ctaAuditstate);
		String hql="select count(*) from ContractApply where ctaAuditstate=:ctaAuditstate";
		long count=super.findCount(hql, params);
		return count;
	}
}
