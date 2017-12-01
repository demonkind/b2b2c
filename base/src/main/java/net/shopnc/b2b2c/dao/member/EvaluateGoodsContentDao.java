package net.shopnc.b2b2c.dao.member;

import java.util.HashMap;
import java.util.List;

import net.shopnc.b2b2c.domain.EvaluateGoodsContent;
import net.shopnc.common.dao.BaseDaoHibernate4;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(rollbackFor = {Exception.class})
public class EvaluateGoodsContentDao extends BaseDaoHibernate4<EvaluateGoodsContent> {

	/**
	 * 根据评价ID查询评价商品信息
	 */
    public List<EvaluateGoodsContent> getEvaluateGoodsContentByEvaluateId(int evaluateId){
    	String hql="from EvaluateGoodsContent a where a.evaluateId=:evaluateId order by a.addTime";
    	HashMap<String,Object> params=new HashMap<String, Object>();
    	params.put("evaluateId", evaluateId);
    	List<EvaluateGoodsContent> list=super.find(hql, params);
    	return list;
    }
}
