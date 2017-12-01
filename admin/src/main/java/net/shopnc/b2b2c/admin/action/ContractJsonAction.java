package net.shopnc.b2b2c.admin.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.admin.util.AdminSessionHelper;
import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.vo.contract.ContractApplyVo;
import net.shopnc.b2b2c.vo.contract.ContractItemVo;
import net.shopnc.b2b2c.vo.contract.ContractQuitapplyVo;
import net.shopnc.b2b2c.vo.contract.ContractVo;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;

/**
 * 保障服务action
 * @author sjz
 *
 */
@Controller
@RequestMapping("contract")
public class ContractJsonAction extends BaseJsonAction {


    @Autowired
    private ContractService contractService;


    /**
     * 服务保障列表
     */
    @RequestMapping(value = "/item_list", method = RequestMethod.POST)
    @ResponseBody
    public DtGrid itemList(String dtGridPager){
    	DtGrid dtGrid=null;
    	try{
    		dtGrid = contractService.getAdminContractItemList(dtGridPager);
    	}catch(Exception ex){
    		ex.printStackTrace();
    		dtGrid=new DtGrid();
    	}
    	return dtGrid;
    }
    
    /**
     * 修改服务保障
     */
    @RequestMapping(value = "/update_item", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity updateContractItem(ContractItemVo contractItemVo){
    	ResultEntity resultEntity = new ResultEntity();
    	try{
    		contractService.updateContractItem(contractItemVo);
    		resultEntity.setCode(ResultEntity.SUCCESS);
    		resultEntity.setMessage("修改成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		resultEntity.setCode(ResultEntity.FAIL);
    		resultEntity.setMessage("修改失败");
    	}
    	return resultEntity;
    }
    
    
    /**
     * admin服务加入申请列表
     */
    @RequestMapping(value = "/apply_list", method = RequestMethod.POST)
    @ResponseBody
    public DtGrid applyList(String dtGridPager){
    	DtGrid dtGrid=null;
    	try{
    		dtGrid = contractService.getAdminContractApplyList(dtGridPager);
    	}catch(Exception ex){
    		ex.printStackTrace();
    		dtGrid=new DtGrid();
    	}
    	return dtGrid;
    }
    
    /**
     * 审核apply
     */
    @RequestMapping(value = "/check_apply", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity checkApply(ContractApplyVo contractApplyVo){
    	ResultEntity resultEntity = new ResultEntity();
    	try{
    		int userId=AdminSessionHelper.getAdminId();
    		String userName=AdminSessionHelper.getAdminName();
    		contractService.checkApply(userId,userName,contractApplyVo);
    		resultEntity.setCode(ResultEntity.SUCCESS);
    		resultEntity.setMessage("审核成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		resultEntity.setCode(ResultEntity.FAIL);
    		resultEntity.setMessage("审核失败");
    	}
    	return resultEntity;
    }
    
    
    /**
     * admin服务退出申请列表
     */
    @RequestMapping(value = "/quitapply_list", method = RequestMethod.POST)
    @ResponseBody
    public DtGrid quitapplyList(String dtGridPager){
    	DtGrid dtGrid=null;
    	try{
    		dtGrid = contractService.getAdminContractQuitapplyList(dtGridPager);
    	}catch(Exception ex){
    		ex.printStackTrace();
    		dtGrid=new DtGrid();
    	}
    	return dtGrid;
    }
    
    /**
     * 审核quitapply
     */
    @RequestMapping(value = "/check_quitapply", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity checkQuitapply(ContractQuitapplyVo contractQuitapplyVo){
    	ResultEntity resultEntity = new ResultEntity();
    	try{
    		int userId=AdminSessionHelper.getAdminId();
    		String userName=AdminSessionHelper.getAdminName();
    		contractService.checkQuitapply(userId,userName,contractQuitapplyVo);
    		resultEntity.setCode(ResultEntity.SUCCESS);
    		resultEntity.setMessage("审核成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		resultEntity.setCode(ResultEntity.FAIL);
    		resultEntity.setMessage("审核失败");
    	}
    	return resultEntity;
    }
    
    /**
     * admin店铺服务保障列表
     */
    @RequestMapping(value = "/contract_list", method = RequestMethod.POST)
    @ResponseBody
    public DtGrid contractList(String dtGridPager){
    	DtGrid dtGrid=null;
    	try{
    		dtGrid = contractService.getAdminContractList(dtGridPager);
    	}catch(Exception ex){
    		ex.printStackTrace();
    		dtGrid=new DtGrid();
    	}
    	return dtGrid;
    }

    /**
     * admin开启关闭店铺保障服务contract
     */
    @RequestMapping(value = "/check_close", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity checkClose(ContractVo contractVo){
    	ResultEntity resultEntity = new ResultEntity();
    	try{
    		int userId=AdminSessionHelper.getAdminId();
    		String userName=AdminSessionHelper.getAdminName();
    		contractService.checkClose(userId,userName,contractVo);
    		resultEntity.setCode(ResultEntity.SUCCESS);
    		resultEntity.setMessage("保存成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		resultEntity.setCode(ResultEntity.FAIL);
    		resultEntity.setMessage("保存失败");
    	}
    	return resultEntity;
    }
    
    /**
     * 修改店铺保证金
     */
    @RequestMapping(value = "/update_cost", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity updateCost(int ctId,String zjType,double cost,String content){
    	ResultEntity resultEntity = new ResultEntity();
    	try{
    		int userId=AdminSessionHelper.getAdminId();
    		String userName=AdminSessionHelper.getAdminName();
    		contractService.updateCost(userId,userName,ctId, zjType, cost, content);
    		resultEntity.setCode(ResultEntity.SUCCESS);
    		resultEntity.setMessage("保存成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		resultEntity.setCode(ResultEntity.FAIL);
    		resultEntity.setMessage("保存失败");
    	}
    	return resultEntity;
    }
    
    
    /**
     * 保障服务日志列表
     */
    @RequestMapping(value = "/contract_detail_list", method = RequestMethod.POST)
    @ResponseBody
    public DtGrid contractDetailList(String dtGridPager){
    	DtGrid dtGrid=null;
    	try{
    		dtGrid = contractService.getAdminContractLogList(dtGridPager);
    	}catch(Exception ex){
    		ex.printStackTrace();
    		dtGrid=new DtGrid();
    	}
    	return dtGrid;
    }
    
    /**
     * 保证金日志列表
     */
    @RequestMapping(value = "/contract_cost_list", method = RequestMethod.POST)
    @ResponseBody
    public DtGrid contractCostList(String dtGridPager){
    	DtGrid dtGrid=null;
    	try{
    		dtGrid = contractService.getAdminContractCostlogList(dtGridPager);
    	}catch(Exception ex){
    		ex.printStackTrace();
    		dtGrid=new DtGrid();
    	}
    	return dtGrid;
    }
}
