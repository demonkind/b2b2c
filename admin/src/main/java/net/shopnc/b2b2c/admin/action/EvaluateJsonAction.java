package net.shopnc.b2b2c.admin.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;

/**
 * 评价action
 * @author sjz
 *
 */
@Controller
@RequestMapping("/evaluate")
public class EvaluateJsonAction extends BaseJsonAction {
    @Autowired
    private EvaluateService evaluateService;
    
    /**
     * 来自买家的评价列表
     */
    @RequestMapping(value="list",method=RequestMethod.POST)
    @ResponseBody
    public DtGrid queryPageEvaluate(String dtGridPager) throws Exception{
    	DtGrid dtGrid=null;
    	try{
    		dtGrid=evaluateService.queryPageAdminEvaluate(dtGridPager);
    		return dtGrid;
    	}catch(Exception ex){
    		ex.printStackTrace();
    		dtGrid=new DtGrid();
    	}
    	return dtGrid;
    }
    
    /**
     * 删除买家评价
     */
    @RequestMapping(value="delEval",method=RequestMethod.POST)
    @ResponseBody
    public ResultEntity deleteEvaluate(int evaluateId){
    	ResultEntity re=new ResultEntity();
    	try{
    		evaluateService.deleteEvaluate(evaluateId);
    		re.setCode(ResultEntity.SUCCESS);
    		re.setMessage("删除成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		re.setCode(ResultEntity.FAIL);
    		re.setMessage("服务器异常，删除失败");
    	}
    	return re;
    }
    
    /**
     * 店铺评分列表
     */
    @RequestMapping(value="list_store",method=RequestMethod.POST)
    @ResponseBody
    public DtGrid queryPageEvaluateStore(String dtGridPager) throws Exception{
    	DtGrid dtGrid=null;
    	try{
    		dtGrid=evaluateService.queryPageAdminStoreEvaluate(dtGridPager);
    		return dtGrid;
    	}catch(Exception ex){
    		ex.printStackTrace();
    		dtGrid=new DtGrid();
    	}
    	return dtGrid;
    }
    
    /**
     * 删除店铺评价
     */
    @RequestMapping(value="delEvalStore",method=RequestMethod.POST)
    @ResponseBody
    public ResultEntity deleteStoreEvaluate(int evaluateId){
    	ResultEntity re=new ResultEntity();
    	try{
    		evaluateService.deleteStoreEvaluate(evaluateId);
    		re.setCode(ResultEntity.SUCCESS);
    		re.setMessage("删除成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		re.setCode(ResultEntity.FAIL);
    		re.setMessage("服务器异常，删除失败");
    	}
    	return re;
    }
}