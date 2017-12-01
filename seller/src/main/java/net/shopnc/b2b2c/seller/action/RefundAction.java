package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.service.refund.SellerRefundService;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.entity.PageEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
public class RefundAction extends BaseAction {

    @Autowired
    private SellerRefundService sellerRefundService;

    @Autowired
    private MemberOrdersService memberOrdersService;

    private static int PAGESIZE = 20;

    public RefundAction() {
        setMenuPath("refund/list");
    }

    /**
     * 商家退款列表
     * @param page
     * @param addTimeStart
     * @param addTimeEnd
     * @param keyword
     * @param stateType
     * @param searchType
     * @param modelMap
     * @return
     */
    @RequiresPermissions("refund/list")
    @RequestMapping(value = "refund/list", method = RequestMethod.GET)
    public String index(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                        @RequestParam(value = "addTimeStart", required = false) Timestamp addTimeStart,
                        @RequestParam(value = "addTimeEnd", required = false) Timestamp addTimeEnd,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "stateType", required = false) String stateType,
                        @RequestParam(value = "searchType", required = false) String searchType,
                        ModelMap modelMap) {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("refund/list", "退款记录");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", SellerSessionHelper.getStoreId());
        params.put("page",page);
        if (addTimeStart != null) {
            params.put("addTimeStart", addTimeStart);
        }
        if (addTimeEnd != null) {
            params.put("addTimeEnd", addTimeEnd);
        }
        if (keyword != null && !keyword.equals("") && searchType != null) {
            if (searchType.equals("ordersSn")) {
                params.put("ordersSn", keyword);
            }
            if (searchType.equals("refundSn")) {
                params.put("refundSn", keyword);
            }
            if (searchType.equals("memberName")) {
                params.put("memberName", keyword);
            }
        }
        if (stateType != null && !stateType.equals("all")) {
            params.put("stateType", stateType);
        }
        params.put("refundType", RefundState.REFUND_TYPE_REFUND);
        List<RefundItemVo> refundItemVoList = sellerRefundService.getRefundVoList(params, page, PAGESIZE);
        //输出搜索条件
        modelMap.putAll(params);
        modelMap.put("refundItemVoList", refundItemVoList);
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(sellerRefundService.getRefundListCount(params));
        modelMap.put("showPage", pageEntity.getPageHtml());
        modelMap.put("keyword", keyword);
        modelMap.put("stateType", stateType);
        modelMap.put("searchType", searchType);
        return getSellerTemplate("refund/list");
    }

    /**
     * 退款详情
     *
     * @param refundId
     * @param modelMap
     * @return
     */
    @RequiresPermissions(value = "refund/list")
    @RequestMapping(value = "refund/info/{refundId}", method = RequestMethod.GET)
    public String info(@PathVariable(value = "refundId") int refundId, ModelMap modelMap) {
        if (refundId <= 0) {
            return "redirect:/seller/refund/list";
        }
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("refund/list", "退款记录");
        tabMenuMap.put("refund/info/"+refundId, "退款详情");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        //获取退款详情
        RefundItemVo refundItemVo = sellerRefundService.getRefundVoByIds(refundId, SellerSessionHelper.getStoreId(),RefundState.REFUND_TYPE_REFUND);
        //订单详情
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", refundItemVo.getStoreId());
        params.put("ordersId", refundItemVo.getOrdersId());
        OrdersVo ordersVo = sellerRefundService.getOrdersVoInfo(params,refundItemVo.getOrdersGoodsId());
        //买家详情
        Member memberInfo =  sellerRefundService.getMemberInfo(ordersVo.getMemberId());
        modelMap.put("memberInfo" , memberInfo);

        modelMap.put("ordersVo", ordersVo);
        modelMap.put("refundItemVo", refundItemVo);
        return getSellerTemplate("refund/info");
    }


}