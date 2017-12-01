package net.shopnc.b2b2c.wap.action.member;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.vo.orders.OrdersPayVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.util.UtilsHelper;

/**
 * 买家我的订单
 * Created by hbj on 2015/12/30.
 */
@Controller
@RequestMapping("orders")
public class MemberOrdersAction extends MemberBaseAction {
    private static int PAGESIZE = 20;
    @Autowired
    private MemberOrdersService memberOrdersService;

    /**
     * 我的订单列表
     * @param page
     * @param ordersState
     * @param createTimeStart
     * @param createTimeEnd
     * @param keyword
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                       @RequestParam(value = "ordersState",required = false) String ordersState,
                       @RequestParam(value = "createTimeStart",required = false) Timestamp createTimeStart,
                       @RequestParam(value = "createTimeEnd",required = false) Timestamp createTimeEnd,
                       @RequestParam(value = "keyword",required = false) String keyword,
                       ModelMap modelMap) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        if(SessionEntity.getMemberId() > 0){
        	params.put("memberId", SessionEntity.getMemberId());
        	String returnOrdersState = "";
        	if (ordersState != null && !ordersState.equals("")) {
        		params.put("ordersState",ordersState);
        		returnOrdersState = ordersState;
        	}
        	if (createTimeStart != null) {
        		params.put("createTimeStart",createTimeStart);
        	}
        	if (createTimeEnd != null) {
        		params.put("createTimeEnd",createTimeEnd);
        	}
        	if (keyword != null && !keyword.equals("")) {
        		params.put("keyword",keyword);
        	}
        	PageEntity pageEntity = new PageEntity();
        	pageEntity.setTotal(memberOrdersService.getMemberOrdersCount(params));
        	pageEntity.setPageNo(page);
        	pageEntity.setPageSize(PAGESIZE);
        	List<OrdersPayVo> ordersPayVoList = memberOrdersService.getOrdersPayVoList(params, page, pageEntity.getPageSize());
        	modelMap.put("ordersPayVoList",ordersPayVoList);
        	modelMap.put("showPage", pageEntity.getPageHtml());
        	//menuKey
        	modelMap.put("menuKey", "orders");
        	modelMap.put("ordersState", returnOrdersState);
        	modelMap.put("keyword", UtilsHelper.getString(keyword));
        	return getMemberTemplate("order_list");
        }else{
        	return getMemberTemplate("login");
        }
    }

    /**
     * 订单详情
     * @param ordersId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(@RequestParam(value = "keyword") Integer ordersId , ModelMap modelMap) {
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        params.put("memberId", SessionEntity.getMemberId());
        OrdersVo ordersVo = memberOrdersService.getOrdersVoInfo(params);
        if (ordersVo == null) {
            return "redirect:/404";
        }
        modelMap.put("ordersVo",ordersVo);
        //menuKey
        modelMap.put("menuKey", "orders");
        return getMemberTemplate("/order_detail");
    }

}
