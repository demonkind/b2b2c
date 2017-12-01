package net.shopnc.b2b2c.seller.action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.domain.contract.ContractApply;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.vo.contract.ContractVo;

/**
 * 保障服务action
 * @author sjz
 *
 */
@Controller
@RequestMapping("contract")
public class ContractAction extends BaseAction {

    @Autowired
    private ContractService contractService;


    public ContractAction() {
    	setMenuPath("contract/list");
    }

    /**
     * 进入保障服务页面
     */
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String initContract(ModelMap modelMap) {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("contract/list", "消费者保障服务");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);
        
        
        int storeId=SellerSessionHelper.getStoreId();
        List<ContractVo> list=contractService.getSellerContractList(storeId);
        modelMap.put("contractVoList", list);
    	
        return getSellerTemplate("store/contract/contract");
    }
    
    /**
     * 保障服务详情页面
     */
    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String initContractLog(ModelMap modelMap,@RequestParam(value = "page", required = false, defaultValue = "1") int page,String itemid){
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("contract/list", "消费者保障服务");
        tabMenuMap.put("contract/detail", "保障服务详情");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);
        
        int storeId=SellerSessionHelper.getStoreId();
        ContractVo contractVo=contractService.getContract(storeId,Integer.parseInt(itemid));
        modelMap.put("contractVo", contractVo);
        
        Map<String,Object> result=contractService.getPageSellerContractLog(page, storeId, Integer.parseInt(itemid));
        modelMap.put("contractLogList", result.get("list"));
    	modelMap.put("showPage", result.get("showPage"));
    	
    	return getSellerTemplate("store/contract/contract_log");
    }

    /**
     * 保证金日志页面
     */
    @RequestMapping(value="/cost",method = RequestMethod.GET)
    public String initContractCostlog(ModelMap modelMap,@RequestParam(value = "page", required = false, defaultValue = "1") int page,String itemid){
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("contract/list", "消费者保障服务");
        tabMenuMap.put("contract/cost", "保证金日志");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);
        
        int storeId=SellerSessionHelper.getStoreId();
        ContractVo contractVo=contractService.getContract(storeId,Integer.parseInt(itemid));
        modelMap.put("contractVo", contractVo);
        
        Map<String,Object> result=contractService.getPageSellerContractCostlog(page, storeId, Integer.parseInt(itemid));
        modelMap.put("contractCostlogList", result.get("list"));
    	modelMap.put("showPage", result.get("showPage"));
    	
    	return getSellerTemplate("store/contract/contract_costlog");
    }
    
    
    /**
     * 进入申请支付保证金页面
     */
    @RequestMapping(value="/paycost",method = RequestMethod.GET)
    public String initCost(ModelMap modelMap,String itemid){
    	HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("contract/list", "消费者保障服务");
        tabMenuMap.put("contract/paycost", "支付保证金");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);
    	
    	int storeId=SellerSessionHelper.getStoreId();
        ContractVo contractVo=contractService.getContract(storeId,Integer.parseInt(itemid));
        modelMap.put("contractVo", contractVo);
        
        ContractApply contractApply=contractService.getContractApply(storeId, Integer.parseInt(itemid));
        modelMap.put("contractApply", contractApply);
        
        return getSellerTemplate("store/contract/paycost");
    }
    
 }
