package net.shopnc.b2b2c.seller.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.vo.contract.ContractApplyVo;
import net.shopnc.common.entity.ResultEntity;

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
     * 申请加入保障服务
     */
    @RequestMapping(value="/join",method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity joinContract(ModelMap modelMap,int itemId){
    	ResultEntity re=new ResultEntity();
    	try{
    		int storeId=SellerSessionHelper.getStoreId();
    		int userId=SellerSessionHelper.getSellerId();
    		String userName=SellerSessionHelper.getSellerName();
    		contractService.joinContract(userId,userName,storeId, itemId);
    		re.setCode(ResultEntity.SUCCESS);
    		re.setMessage("保障服务申请成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		re.setCode(ResultEntity.FAIL);
    		re.setMessage("保障服务申请失败");
    	}
    	return re;
    }
    
    
    /**
     * 申请支付保证金
     * @param contractApplyVo
     */
    @RequestMapping(value="/pay",method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity payCost(ContractApplyVo contractApplyVo){
    	ResultEntity re=new ResultEntity();
    	try{
    		int userId=SellerSessionHelper.getSellerId();
    		String userName=SellerSessionHelper.getSellerName();
    		contractService.payCost(userId,userName,contractApplyVo);
    		re.setCode(ResultEntity.SUCCESS);
    		re.setMessage("保证金申请成功");
    		re.setUrl(ShopConfig.getSellerRoot()+"contract/list");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		re.setCode(ResultEntity.FAIL);
    		re.setMessage("保证金申请失败");
    	}
    	return re;
    }
    
    /**
     * 店铺申请退出保障服务
     */
    @RequestMapping(value="/quit",method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity quitContract(ModelMap modelMap,int itemId){
    	ResultEntity re=new ResultEntity();
    	try{
    		int storeId=SellerSessionHelper.getStoreId();
    		int userId=SellerSessionHelper.getSellerId();
    		String userName=SellerSessionHelper.getSellerName();
    		contractService.quitContract(userId,userName,storeId, itemId);
    		re.setCode(ResultEntity.SUCCESS);
    		re.setMessage("退出服务申请成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
    		re.setCode(ResultEntity.FAIL);
    		re.setMessage("退出服务申请失败");
    	}
    	return re;
    }
}
