package net.shopnc.b2b2c.seller.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.domain.EvaluateGoodsContent;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.buy.BuyService;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.common.entity.ResultEntity;

/**
 * 评价action
 * @author sjz
 *
 */
@Controller
@RequestMapping("/evaluate")
public class EvaluateJsonAction extends BaseJsonAction {
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private BuyService buyService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private OrdersService ordersService;
    
    
    
    /**
     * seller解释评价
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity saveExplain(int evaluateId,String explainContent,String type){
    	ResultEntity re=new ResultEntity();
    	try{
    		String sellerName=SellerSessionHelper.getSellerName();
    		EvaluateGoodsContent e=evaluateService.saveExplaint(evaluateId, explainContent, type,sellerName);
    		re.setCode(ResultEntity.SUCCESS);
    		re.setMessage("评价解释成功");
    		re.setData(e);
    	}catch(Exception ex){
    		ex.printStackTrace();
    		re.setCode(ResultEntity.FAIL);
    		re.setMessage("评价解释失败");
    	}
    	return re;
    }
    
}
