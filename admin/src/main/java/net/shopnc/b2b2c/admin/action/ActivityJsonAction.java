package net.shopnc.b2b2c.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.shopnc.b2b2c.domain.goods.Activity;
import net.shopnc.b2b2c.service.goods.ActivityService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;

@Controller
public class ActivityJsonAction extends BaseJsonAction {
	
	@Autowired
	ActivityService activityService;
	
	
	
	/**
     * 订单列表
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "findActivityList.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager,HttpServletRequest request){
        DtGrid dtGrid = new DtGrid();
        try {
            ObjectMapper mapper = new ObjectMapper();
            dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
            String memberId="123456";
            dtGrid=activityService.findActivityList(memberId, dtGrid);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("whereHql:" + dtGrid.getWhereHql());
            logger.error("sortHql:" + dtGrid.getSortHql());
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

	 /**
     * 新增活动
     */
	
    @RequestMapping(value = "createActivity", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity addActivity(HttpServletRequest request) {
    	ResultEntity resultEntity =new ResultEntity();
    	String memberId="123456";
    	String activityId=request.getParameter("activityId");
    	String activityName=request.getParameter("activityName");
    	Activity activity=new Activity();
    	activity.setActivityId(activityId);
    	activity.setActivityName(activityName);
    	activity.setMemberId(memberId);
    	activity.setIsFlag("0");
    	try{
	    	int count=activityService.getCount("", activityId, "");
	    	if(count==0){
	    		int i=activityService.addActivity(activity);
		    	if(i>0){
		    		resultEntity.setMessage("新增成功!");
		    		resultEntity.setCode(200);
		    	}else{
		    		
		    	}
		    	
	    	}else{
	    		resultEntity.setMessage("新增失败,活动ID已存在!");
	    	}
    	}catch(Exception  e){
    		e.printStackTrace();
    		resultEntity.setMessage("新增失败!");
    	}finally{
    		return resultEntity;
    	}
    }
    
    /**
     * 修改活动信息
     */
    @RequestMapping(value = "/updateActivity", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity updateActivity(HttpServletRequest request,ModelMap modelMap) {
    	String activityId=request.getParameter("activityId");
    	String activityName=request.getParameter("activityName");
    	ResultEntity resultEntity =new ResultEntity();
    	try{
    		activityService.updateActivity(activityId, activityName);
    		resultEntity.setCode(200);
    	}catch(Exception e){
    		e.printStackTrace();
    		resultEntity.setCode(201);
    	}finally {
    		return resultEntity;
		}
    }
    
    /**
     * 删除活动信息
     */
    @RequestMapping(value = "/delActivity", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity delActivity(HttpServletRequest request,ModelMap modelMap) {
    	String activityId=request.getParameter("activityId");
    	ResultEntity resultEntity =new ResultEntity();
    	try{
    		activityService.delActivity(activityId);
    		resultEntity.setCode(200);
    	}catch(Exception e){
    		e.printStackTrace();
    		resultEntity.setCode(201);
    	}finally {
    		return resultEntity;
		}
    }
}
