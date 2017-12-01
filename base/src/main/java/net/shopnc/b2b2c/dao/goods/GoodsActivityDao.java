package net.shopnc.b2b2c.dao.goods;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.domain.goods.Activity;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.common.dao.BaseDaoHibernate4;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class GoodsActivityDao extends BaseDaoHibernate4<GoodsActivity>{

	/***
	 * .保存或更新绑定活动的商品
	 * @return 
	 * 
	 * */
	public  void saveGoodsActivity(GoodsActivity goodsActivity){
		update(goodsActivity);
	}

	/***
	 * 查询已经绑定活动的商品
	 * @return 
	 * 
	 * */
	public Object checkBount(int commonId){
		String hql="from GoodsActivity where commonId = :goodsId";
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("goodsId", commonId);
		List<Object> list= super.findObject(hql, params);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	/**
     * 查询绑定商品详情
     * @param memberId
     * @param acrivityId
     * @param activityName
     */
	public List<GoodsActivity> findGoodsActivityById(int goodsId) {
		 String hql = "from GoodsActivity where commonId = :goodsId";
	     Query query = sessionFactory.getCurrentSession().createQuery(hql);
	     query.setInteger("goodsId", goodsId);
		return query.list();
	}

	
	/***
	 * .解除已经绑定活动的商品
	 * @return 
	 * 
	 * */
	public void deleteGoodsActivity(GoodsActivity activity) {
		delete(activity);
	}

	/***
	 * .查询活动信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Activity> findActivity(Activity activity) {
		String hql="from Activity WHERE isFlag = '0' ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

}
