package net.shopnc.b2b2c.dao.member;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.domain.member.Coupons;
import net.shopnc.common.dao.BaseDaoHibernate4;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class CardCouponsDao extends BaseDaoHibernate4<Coupons>  {
	
	
	/**
	 * 查询用户拥有卡券
	 */
	public List<Coupons> getCouponsList(int memberId) {
		if(memberId > 0){
			String hql = "from Coupons where memberId = :memberId order by createTime desc";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setInteger("memberId", memberId);
	        return query.list();
        }else{
        	return null;
        }
    }
	
	/**
	 * 查询用户拥有卡券
	 */
	public List<Coupons> getCoupon(HashMap<String, Integer> param) {
		Query query;
		String hql;
		if(param.get("goodsId") >0){
			hql = "from Coupons where memberId = :memberId and ordersId = :ordersId and storeId = :storeId and goodsId = :goodsId order by createTime desc";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setInteger("goodsId", param.get("goodsId"));
		}else{
			hql = "from Coupons where memberId = :memberId and ordersId = :ordersId and storeId = :storeId order by createTime desc";
			query = sessionFactory.getCurrentSession().createQuery(hql);
		}
		query.setInteger("memberId", param.get("memberId"));
		query.setInteger("ordersId", param.get("ordersId"));
		query.setInteger("storeId", param.get("storeId"));
		return query.list();
    }
    
}
