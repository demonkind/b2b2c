package net.shopnc.b2b2c.seller.action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.buy.BuyService;
import net.shopnc.b2b2c.service.orders.OrdersService;

/**
 * 评价action
 * @author sjz
 *
 */
@Controller
@RequestMapping("/evaluate")
public class EvaluateAction extends BaseAction {
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private BuyService buyService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private OrdersService ordersService;
    
    EvaluateAction(){
    	setMenuPath("evaluate");
    }
    
    /**
     * seller评价列表
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String initEvaluate(ModelMap modelMap,@RequestParam(value = "page", required = false, defaultValue = "1") int page,String goodsName,String memberName){
    	HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("evaluate", "评价管理");
        modelMap.put("sellerTabMenuMap", tabMenuMap);
    	
    	int storeId=SellerSessionHelper.getStoreId();
    	Map<String,Object> result=evaluateService.queryPageSellerEvaluate(page, storeId, goodsName, memberName);
    	modelMap.put("evaluateGoodsVoList", result.get("list"));
    	modelMap.put("showPage", result.get("showPage"));
    	if(goodsName==null){
    		goodsName="";
    	}
    	if(memberName==null){
    		memberName="";
    	}
    	modelMap.put("goodsName", goodsName);
    	modelMap.put("memberName", memberName);
    	return getSellerTemplate("evaluate/evaluate");
    }
    
}
