package net.shopnc.b2b2c.service.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.dao.goods.ActivityDao;
import net.shopnc.b2b2c.domain.goods.Activity;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.entity.dtgrid.QueryUtils;

@Service
@Transactional(rollbackFor = {Exception.class})
public class ActivityService  extends BaseService {
		@Autowired
		ActivityDao activityDao;
		
		
		/**
	     *	新增活动信息
	     * @param memberId
	     * @param acrivityId
	     * @param activityName
	     */
		public int addActivity(Activity activity){
			int i=(Integer)activityDao.save(activity);
			if(i>0){
				return 1;
			}else{
				return 0;
			}
		}
		
		//获取不同条件下的查询记录总数
		public int getCount(String activityName,String activityId,String memberId){
			HashMap<String, Object> map = new HashMap<String, Object>();
			StringBuffer str=new StringBuffer();
			if(!activityName.equals("")&&activityName!=null){
				str.append(" and activityName like :activityName");
				map.put("activityName", "%"+activityName+"%");
			}
			if(!activityId.equals("")&&activityId!=null){
				str.append(" and activityId = :activityId");
				map.put("activityId", activityId);
			}
			if(!memberId.equals("")&&memberId!=null){
				str.append(" and memberId = :memberId");
				map.put("memberId", memberId);
			}
			return activityDao.getCount(str.toString(), map);
		}
		
		/**
	     * 获取用户自己的活动信息列表
	     * @param memberId
	     * @param acrivityId
	     * @param activityName
	     */
		public DtGrid findActivityList(String memberId,DtGrid dtGrid)throws Exception{
	        if (dtGrid.getFastQueryParameters().containsKey("activityName")) {
	            dtGrid.getFastQueryParameters().put("lk_activityName",dtGrid.getFastQueryParameters().get("activityName"));
	            dtGrid.getFastQueryParameters().remove("activityName");
	        }
	        dtGrid.getFastQueryParameters().put("eq_memberId",memberId);
	        dtGrid.getFastQueryParameters().put("eq_isFlag",0);
	        HashMap<String, String> hashMap = new HashMap<String, String>();
	        if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
	            for (String key : dtGrid.getNcColumnsType().keySet()) {
	                for (int i = 0; i< dtGrid.getNcColumnsType().get(key).size(); i++) {
	                    hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
	                }
	                dtGrid.setNcColumnsTypeList(hashMap);
	            }
	        }
	        QueryUtils.parseDtGridHql(dtGrid);
	        dtGrid = activityDao.getDtGridList(dtGrid,Activity.class);
	        List<Object> exhibitDatas = dtGrid.getExhibitDatas();
	        List<Object> activityList  = new ArrayList<Object>();
	        for (int i = 0; i < exhibitDatas.size(); i++) {
	            Activity activ = (Activity)exhibitDatas.get(i);
	            activityList.add(activ);
	        }
	        dtGrid.setExhibitDatas(activityList);
	        return dtGrid;
	        
	      /*  if(!activityName.equals("")&&activityName!=null){
	        	 map.put("activityName", activityName);
	        	 hqlCondition="and activityName = :activityName";
	        }
	        return activityDao.findActivityList(map, Integer.valueOf(pageNo), Integer.valueOf(pageSize),hqlCondition);*/
		}
		
		/**
	     * 修改活动信息
	     * @param memberId
	     * @param acrivityId
	     * @param activityName
	     */
	    public void updateActivity(String activityId,String activityName) {
	    	HashMap<String,Object> hashMap = new HashMap<>();
	        hashMap.put("activityName",activityName);
	        hashMap.put("activityId",activityId);
	        activityDao.updateActivity(hashMap);
	    }
	    
	    /**
	     * 删除活动信息
	     * @param memberId
	     * @param acrivityId
	     * @param activityName
	     */
	    public void delActivity(String activityId) {
	        HashMap<String,Object> hashMap = new HashMap<>();
	        hashMap.put("activityId",activityId);
	        activityDao.delActivity(hashMap);
	    }
	    
}
