package net.shopnc.b2b2c.dao.contract;

import java.util.HashMap;
import java.util.List;

import net.shopnc.b2b2c.domain.contract.Contract;
import net.shopnc.common.dao.BaseDaoHibernate4;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ContractDao extends BaseDaoHibernate4<Contract> {
	
	/**
	 * 查询店铺服务保障信息
	 */
	public Contract getContract(int storeId,int itemId){
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("storeId", storeId);
		params.put("itemId", itemId);
		String hql="from Contract where ctStoreid=:storeId and ctItemid=:itemId order by ctId";
		List<Contract> list=super.find(hql, params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
