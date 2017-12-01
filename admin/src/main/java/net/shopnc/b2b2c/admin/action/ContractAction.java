package net.shopnc.b2b2c.admin.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.vo.contract.ContractVo;

/**
 * 保障服务 action
 * @author sjz
 *
 */
@Controller
@RequestMapping("contract")
public class ContractAction extends BaseAction {


    @Autowired
    private ContractService contractService;

    /**
     * 进入服务保障管理页面
     */
    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public String initItem(ModelMap modelMap) {
        return getAdminTemplate("contract/item");
    }


    
    
    
    /**
     * admin服务加入申请页面
     */
    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    public String initApply(ModelMap modelMap) {
        return getAdminTemplate("contract/apply");
    }
    
    
    
    
    
    
    /**
     * admin服务退出申请页面
     */
    @RequestMapping(value = "/quit_apply", method = RequestMethod.GET)
    public String initQuitApply(ModelMap modelMap) {
        return getAdminTemplate("contract/quit_apply");
    }
    
    /**
     * 进入店铺服务保障页面
     */
    @RequestMapping(value = "/contract", method = RequestMethod.GET)
    public String initContract(ModelMap modelMap) {
        return getAdminTemplate("contract/contract");
    }
    
    
    /**
     * 店铺保障服务-查看详情
     */
    @RequestMapping(value = "/contract_detail", method = RequestMethod.GET)
    public String initContractDetail(ModelMap modelMap,int ctid){
    	ContractVo contractVo=contractService.getContract(ctid);
    	modelMap.put("contractVo", contractVo);
    	return getAdminTemplate("contract/contract_detail");
    }
    
    /**
     * 店铺保障服务-保证金日志
     */
    @RequestMapping(value = "/contract_cost", method = RequestMethod.GET)
    public String initContractCost(ModelMap modelMap,int ctid){
    	ContractVo contractVo=contractService.getContract(ctid);
    	modelMap.put("contractVo", contractVo);
    	return getAdminTemplate("contract/contract_cost");
    }
    
}
