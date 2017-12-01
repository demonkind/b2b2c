package net.shopnc.b2b2c.wap.action.member;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.buy.BuyService;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.b2b2c.vo.member.EvaluateGoodsOrderVo;
import net.shopnc.b2b2c.vo.member.EvaluateStoreVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

/**
 * 评价action
 * @author sjz
 *
 */
@Controller
@RequestMapping("/member/evaluate")
public class EvaluateAction extends MemberBaseAction {
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
     * 第一次添加评价页面
     * @param modelMap
     * @param orderId
     * @return
     */
    @RequestMapping(value="evaluateAdd",method = RequestMethod.GET)
    public String initEvaluateAdd(ModelMap modelMap, String orderId) throws ShopException{
    	int memberId=SessionEntity.getMemberId();
    	Map<String,Object> map=evaluateService.getEvaluateOrder(memberId,Integer.parseInt(orderId));
    	Orders orders=(Orders)map.get("orders");
    	List<Object> list=(List)map.get("ordersGoodsList");
    	Store store=(Store)map.get("store");
    	EvaluateStoreVo evaluateStoreVo=(EvaluateStoreVo)map.get("evaluateStoreVo");
    	modelMap.put("orders", orders);
    	modelMap.put("ordersGoodsList", list);
    	modelMap.put("store",store);
    	modelMap.put("evaluateStoreVo", evaluateStoreVo);
        //menuKey
        modelMap.put("menuKey", "evaluate");
    	return getMemberTemplate("member_evaluation");
    }
    
    

    /**
     * 追加评价页面
     * @param modelMap
     * @param orderId
     * @return
     */
    @RequestMapping(value="evaluateAddAain",method = RequestMethod.GET)
    public String initEvaluateAddAgain(ModelMap modelMap,String orderId) throws ShopException{
    	int memberId=SessionEntity.getMemberId();
    	List<EvaluateGoodsOrderVo> voList=evaluateService.queryEvaluateByOrderId(memberId, Integer.parseInt(orderId));
    	modelMap.put("evaluateGoodsOrderVoList", voList);
    	modelMap.put("orderId", orderId);
    	int storeId=voList.get(0).getStoreId();
    	EvaluateStoreVo evaluateStoreVo=evaluateService.getEvalStoreClass(storeId);
    	modelMap.put("evaluateStoreVo", evaluateStoreVo);
    	Store store=storeDao.get(Store.class, storeId);
    	modelMap.put("store", store);
        //menuKey
        modelMap.put("menuKey", "evaluate");
        return getMemberTemplate("member_evaluation_again");
    }


    /**
     * 买家评价列表
     * @param page
     * @param modelMap
     * @return
     * @throws ShopException
     */
    @RequestMapping(value="",method = RequestMethod.GET)
    public String initEvaluate(@RequestParam(value = "page", required = false, defaultValue="1") int page,ModelMap modelMap) throws ShopException{
    	int memberId=SessionEntity.getMemberId();
    	Map<String,Object> result=evaluateService.queryPageEvaluate(page, memberId);
    	modelMap.put("evaluateList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //menuKey
        modelMap.put("menuKey", "evaluate");
        return getMemberTemplate("evaluate/evaluate");
    }

}
