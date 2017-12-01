package net.shopnc.b2b2c.dao.member;

import java.util.HashMap;
import java.util.List;

import net.shopnc.b2b2c.domain.EvaluateStore;
import net.shopnc.common.dao.BaseDaoHibernate4;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(rollbackFor = {Exception.class})
public class EvaluateStoreDao extends BaseDaoHibernate4<EvaluateStore> {

	/**
	 * 计算评分的平均值
	 * @param storeId
	 * @return
	 */
	public List<Object> getAvgScore(int storeId){
		String hql="select avg(a.descriptionCredit),avg(a.serviceCredit),avg(a.deliveryCredit) from EvaluateStore a where a.storeId=:storeId";
		HashMap<String,Object> params=new HashMap<String, Object>();
		params.put("storeId", storeId);
		List<Object> list=super.findObject(hql, params);
		return list;
	}
}
