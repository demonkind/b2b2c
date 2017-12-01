package net.shopnc.b2b2c.dao.contract;

import net.shopnc.b2b2c.domain.contract.ContractQuitapply;
import net.shopnc.common.dao.BaseDaoHibernate4;

import java.util.HashMap;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ContractQuitapplyDao extends BaseDaoHibernate4<ContractQuitapply> {

	/**
	 * 根据状态查询退出申请数
	 */
	public long countQuitApply(int ctqAuditstate){
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("ctqAuditstate", ctqAuditstate);
		String hql="select count(*) from ContractQuitapply where ctqAuditstate=:ctqAuditstate";
		long count=super.findCount(hql, params);
		return count;
	}
}
