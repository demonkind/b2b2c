package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.orders.StoreOrdersService;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.common.entity.PageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 订单
 * Created by hbj on 2016/1/7.
 */
@Controller
public class OrdersAction extends BaseAction {
    private static int PAGESIZE = 20;
    @Autowired
    private StoreOrdersService storeOrdersService;

    public OrdersAction() {
        setMenuPath("orders/list/default");
    }

    /**
     * 子导航
     * @param modelMap
     */
    private void subNav(ModelMap modelMap) {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("orders/list/default", "所有订单");
        tabMenuMap.put("orders/list/new", "待付款");
        tabMenuMap.put("orders/list/pay", "待发货");
        tabMenuMap.put("orders/list/send", "已发货");
        tabMenuMap.put("orders/list/finish", "已完成");
        tabMenuMap.put("orders/list/cancel", "已取消");
        modelMap.put("sellerTabMenuMap", tabMenuMap);
    }

    /**
     * 商家订单列表
     * @param page
     * @param ordersState
     * @param createTimeStart
     * @param createTimeEnd
     * @param keyword
     * @param searchType
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "orders/list/{ordersState:\\w+}", method = RequestMethod.GET)
    public String list(@PathVariable String ordersState,
                       @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value = "createTimeStart",required = false) Timestamp createTimeStart,
                       @RequestParam(value = "createTimeEnd",required = false) Timestamp createTimeEnd,
                       @RequestParam(value = "keyword",required = false) String keyword,
                       @RequestParam(value = "searchType",required = false) String searchType,
                       ModelMap modelMap) {
        subNav(modelMap);
        List<String> ordersStateList = Arrays.asList("default","new","pay","send","finish","cancel");
        if (ordersStateList.contains(ordersState) == false) {
            return "redirect:/orders/list/default";
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", SellerSessionHelper.getStoreId());
        if (ordersState != null && !ordersState.equals("")) {
            params.put("ordersState",ordersState);
        }
        if (createTimeStart != null) {
            params.put("createTimeStart",createTimeStart);
        }
        if (createTimeEnd != null) {
            params.put("createTimeEnd",createTimeEnd);
        }
        if (keyword != null && !keyword.equals("") && searchType != null) {
            if (searchType.equals("memberName")) {
                params.put("memberName", keyword);
            }
            if (searchType.equals("goodsName")) {
                params.put("goodsName",keyword);
            }
            if (searchType.equals("ordersSn")) {
                params.put("ordersSn",keyword);
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(storeOrdersService.getMemberOrdersCount(params));
        pageEntity.setPageNo(page);
        pageEntity.setPageSize(PAGESIZE);
        List<OrdersVo> ordersVoList = storeOrdersService.getOrdersVoList(params, page, PAGESIZE);
        modelMap.put("ordersVoList",ordersVoList);
        modelMap.put("showPage", pageEntity.getPageHtml());
        return getSellerTemplate("orders/list");
    }

    /**
     * 订单详情
     * @param ordersId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "orders/info/{ordersId}", method = RequestMethod.GET)
    public String info(@PathVariable Integer ordersId, ModelMap modelMap) {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("orders/list/default", "商品订单");
        tabMenuMap.put("orders/info/" + ordersId, "订单详情");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        params.put("storeId", SellerSessionHelper.getStoreId());
        OrdersVo ordersVo = storeOrdersService.getOrdersVoInfo(params);
        if (ordersVo == null) {
            return "redirect:/404";
        }
        modelMap.put("ordersVo", ordersVo);
        return getSellerTemplate("orders/info");
    }
}
