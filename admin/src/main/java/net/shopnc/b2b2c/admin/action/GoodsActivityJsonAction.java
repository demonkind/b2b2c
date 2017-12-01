package net.shopnc.b2b2c.admin.action;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.dao.goods.ActivityDao;
import net.shopnc.b2b2c.domain.goods.Activity;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.goods.GoodsActivityHistory;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.common.entity.ResultEntity;


@Controller
public class GoodsActivityJsonAction extends BaseJsonAction {

	@Autowired
	GoodsActivityService goodsActivityService;
	
	@Autowired
	ActivityDao activityDao;

	
	/***
	 * .保存商品绑定活动信息
	 * 
	 */
	@RequestMapping(value = "goods/saveGoodsActivity.json", method = RequestMethod.POST)
	@ResponseBody
	public ResultEntity saveGoodsActivity(HttpServletRequest request) {
		
		ResultEntity resultEntity = new ResultEntity();
		try {
			
			String goodsId=request.getParameter("commonId");
			//String activityType=request.getParameter("activityType");
			String storeId=request.getParameter("storeId");
			
			String activityId=request.getParameter("activityId");

			GoodsActivity  activity = new GoodsActivity();
			//activity.setActivityType(activityType);
			activity.setCommonId(Integer.valueOf(goodsId));
			activity.setStoreId(Integer.valueOf(storeId));
			
			activity.setActivityId(activityId);
			
			GoodsActivityHistory activityHistory = new GoodsActivityHistory();
			//activityHistory.setActivityType(activityType);
			activityHistory.setCommonId(Integer.valueOf(goodsId));
			activityHistory.setStoreId(Integer.valueOf(storeId));
			
			activityHistory.setActivityId(activityId);
			activity.setActivityType(activityId);
			goodsActivityService.saveGoodsActivitys(activity);
			activityHistory.setActivityType(activityId);
			goodsActivityService.saveGoodsActivityHistorys(activityHistory);
			
			resultEntity.setCode(ResultEntity.SUCCESS);
			resultEntity.setMessage("操作成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("数据库保存失败");
		}

		return resultEntity;

	}
	
	/***
	 * .解除商品绑定活动的信息
	 * 
	 */
	@RequestMapping(value = "goods/deleteGoodsActivity.json", method = RequestMethod.POST)
	@ResponseBody
	public ResultEntity deleteGoodsActivity(HttpServletRequest request) {
		ResultEntity resultEntity = new ResultEntity();
		try {
			String commonId =request.getParameter("commonId");
			List<GoodsActivity> activityList = goodsActivityService.findGoodsActivityById(Integer.parseInt(commonId));//查询到的结果集
			if(activityList!=null && !activityList.isEmpty()){
				for (GoodsActivity activity : activityList){
					goodsActivityService.deleteGoodsActivitys(activity);
				}
			}
			resultEntity.setCode(ResultEntity.SUCCESS);
			resultEntity.setMessage("解除成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("解除失败");
		}
		return resultEntity;
	}
	
	/***
	 * .获取数据库中的活动信息
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 * 
	 */
	@RequestMapping(value = "goods/findActivityLists.json", method = RequestMethod.GET)
	@ResponseBody
	public List<Activity> findActivityList(Activity activity,HttpServletRequest request) throws NumberFormatException, SQLException{
		
		List<Activity> list = goodsActivityService.findActivitys(activity);//查询到的结果集

		return list;
	}
	
	
	

}
