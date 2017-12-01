package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.refund.RefundDao;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.service.refund.AdminRefundService;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.vo.refund.RefundDetailVo;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.util.PriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 退款退货原因放到
 * Created by cj on 2016/2/1.
 */
@Controller
public class RefundAction extends BaseAction {
    @Autowired
    AdminRefundService adminRefundService;
    @Autowired
    RefundDao refundDao;
    @Autowired
    MemberOrdersService memberOrdersService;

    /**
     * 退款管理列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "refund/list", method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.put("adminMenuState",adminMenuService.getAdminMenuState("refund/list"));
        //待审核的退款单
        model.put("refundHandleCount",adminRefundService.getHandleRefundCount());
        return getAdminTemplate("refund/list");
    }

    /**
     * 退款审核列表
     * @param model
     * @return
     */
    @RequestMapping(value = "refund/handle_list", method = RequestMethod.GET)
    public String handleList(ModelMap model) {
        model.put("adminMenuState",adminMenuService.getAdminMenuState("refund/handle_list"));
        return getAdminTemplate("refund/handle_list");
    }

    /**
     * 退款详情以及处理退款
     *
     * @param refundId
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "refund/info/{refundId}", method = RequestMethod.GET)
    public String info(@PathVariable(value = "refundId") int refundId,
                       @RequestParam(value = "type", defaultValue = "look") String type,
                       ModelMap modelMap) {
        if (refundId <= 0) {
            return "redirect:/admin/refund/list";
        }
        RefundItemVo refundItemVo = adminRefundService.getRefundInfo(refundId);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ordersId", refundItemVo.getOrdersId());
        OrdersVo ordersVo = memberOrdersService.getOrdersVoInfo(params);

        modelMap.put("ordersVo", ordersVo);
        modelMap.put("refundItemVo", refundItemVo);
        modelMap.put("type", type);
        modelMap.put("refundId", refundId);

        //计算可退金额
        BigDecimal paymentOnline = BigDecimal.ZERO;
        if (ordersVo.getPaymentTime()!= null){
            paymentOnline = PriceHelper.sub(ordersVo.getOrdersAmount() , ordersVo.getPredepositAmount());
        }
        modelMap.put("paymentOnline",paymentOnline);
        RefundDetailVo refundDetailVo = adminRefundService.getRefundDetailVoByRefundId(refundId);
        if (type.equals("handle")) {
            if (refundDetailVo == null){
            //添加退款详情表
                refundDetailVo = adminRefundService.saveOrUpdateRefundDetail(refundItemVo, ordersVo);
            }
        }
        modelMap.put("refundDetailVo", refundDetailVo);
//        setMenuPath("refund/info");
        modelMap.put("adminMenuState",adminMenuService.getAdminMenuState("refund/list"));
        return getAdminTemplate("refund/info");
    }


}
