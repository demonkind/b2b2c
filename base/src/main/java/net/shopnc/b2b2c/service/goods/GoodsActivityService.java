package net.shopnc.b2b2c.service.goods;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.dao.goods.GoodsActivityDao;
import net.shopnc.b2b2c.dao.goods.GoodsActivityHistoryDao;
import net.shopnc.b2b2c.domain.goods.Activity;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.goods.GoodsActivityHistory;
import net.shopnc.b2b2c.service.BaseService;

@Service
@Transactional(rollbackFor = { Exception.class })
public class GoodsActivityService extends BaseService {

	@Autowired
	GoodsActivityDao goodsActivityDao;
	
	@Autowired
	GoodsActivityHistoryDao goodsActivityHistoryDao;
	


	/***
	 * .保存绑定活动的商品
	 * 
	 */
	public int saveGoodsActivitys(GoodsActivity activity) {
		int isSave=0;
		GoodsActivity goodsActivity=new GoodsActivity();
		goodsActivity=(GoodsActivity) goodsActivityDao.checkBount(activity.getCommonId());
		if(goodsActivity==null){
			goodsActivityDao.saveGoodsActivity(activity);
		}
		return isSave;
	}
	

	/***
	 * .保存绑定活动的商品到历史记录表
	 * 
	 */
	public void saveGoodsActivityHistorys(GoodsActivityHistory activityHistory) {
		goodsActivityHistoryDao.saveGoodsActivityHistory(activityHistory);
	}
	

	/***
	 * 设置活动
	 * 
	 */
	public int goodsActivityUpdate(GoodsActivity goodsActivity) {
		int i = 0;
		try {
			goodsActivityDao.update(goodsActivity);
			i = 1;
		} catch (Exception e) {
			i = 0;
			e.printStackTrace();
		} finally {
			return i;
		}
	}
	
	
	/***
	 * 查询已经绑定活动的商品
	 * 
	 */
	public Object checkBound(int goodsId){
		return goodsActivityDao.checkBount(goodsId);
	}

	
	
	/***
	 * .解除已经绑定活动的商品
	 * 
	 */
	public void deleteGoodsActivitys(GoodsActivity activity){
		goodsActivityDao.deleteGoodsActivity(activity);
		
	}
	
	/***
	 * .查询活动信息
	 * 
	 */
	public List<Activity> findActivitys(Activity activity){
		
		return goodsActivityDao.findActivity(activity);
		
	}
	

	/***
	 * 查询活动商品信息
	 * 
	 * */
	public List<GoodsActivity> findGoodsActivityById(int commonId) {
		return goodsActivityDao.findGoodsActivityById(commonId);
	}


}
