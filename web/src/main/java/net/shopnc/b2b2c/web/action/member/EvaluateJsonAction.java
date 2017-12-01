package net.shopnc.b2b2c.web.action.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.buy.BuyService;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.b2b2c.vo.member.EvaluateVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;

/**
 * 评价action
 * @author sjz
 *
 */
@Controller
@RequestMapping("/member/evaluate")
public class EvaluateJsonAction extends MemberBaseJsonAction {
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private BuyService buyService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private StoreDao storeDao;
    

    
    
    /**
     * 第一次添加评价
     * @param evaluateVo
     * @return
     * @throws ShopException
     */
    @ResponseBody
    @RequestMapping(value = "insertEvaluate", method = RequestMethod.POST)
    public ResultEntity save(EvaluateVo evaluateVo) throws ShopException {
    	ResultEntity resultEntity = new ResultEntity();
        try{
        	String memberId=String.valueOf(SessionEntity.getMemberId());
        	String memberName=SessionEntity.getMemberName();
        	evaluateVo.setMemberId(memberId);
        	evaluateVo.setMemberName(memberName);
        	evaluateService.saveEvaluate(evaluateVo);
        	
        	resultEntity.setUrl(ShopConfig.getWebRoot() + "member/evaluate");
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("评价成功");
        }catch(Exception ex){
        	ex.printStackTrace();
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("服务器异常 评价失败");
        }
        return resultEntity;
    }

    

    /**
     * 追加评价
     * @param evaluateVo
     * @return
     * @throws ShopException
     */
    @ResponseBody
    @RequestMapping(value = "addEvaluate", method = RequestMethod.POST)
    public ResultEntity saveAgain(EvaluateVo evaluateVo) throws ShopException{
    	ResultEntity resultEntity = new ResultEntity();
    	try{
    		String memberName=SessionEntity.getMemberName();
    		evaluateVo.setMemberName(memberName);
    		evaluateService.saveEvaluateAgain(evaluateVo);
    		
    		resultEntity.setUrl(ShopConfig.getWebRoot() + "member/evaluate");
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("评价成功");
    	}catch(Exception ex){
    		ex.printStackTrace();
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("服务器异常 评价失败");
    	}
    	return resultEntity;
    }


}
