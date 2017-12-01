package net.shopnc.b2b2c.dao.contract;

import java.util.HashMap;
import java.util.List;

import net.shopnc.b2b2c.domain.contract.ContractItem;
import net.shopnc.common.dao.BaseDaoHibernate4;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ContractItemDao extends BaseDaoHibernate4<ContractItem> {
	
	/**
	 * 查询服务保障列表
	 */
	public List<ContractItem> getAllContractItem(List<String> condition,HashMap<String,Object> params){
		StringBuffer hql=new StringBuffer("from ContractItem where 1=1 ");
		for (String con : condition) {
            hql.append(" and " + con);
        }
		hql.append(" order by ctiSort ");
		List<ContractItem> list=super.find(hql.toString(),params);
		return list;
	}
}
