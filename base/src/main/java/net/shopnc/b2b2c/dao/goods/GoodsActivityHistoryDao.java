package net.shopnc.b2b2c.dao.goods;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.domain.goods.GoodsActivityHistory;
import net.shopnc.common.dao.BaseDaoHibernate4;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class GoodsActivityHistoryDao extends BaseDaoHibernate4<GoodsActivityHistory> {
	
	 /***
	 * 保存或更新绑定活动的商品到历史记录表
	 * @return 
	 * 
	 * */
	public  void saveGoodsActivityHistory(GoodsActivityHistory activityHistory){
		super.save(activityHistory);
	}

}
