package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.refund.RefundDao;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.orders.AdminOrdersService;
import net.shopnc.b2b2c.service.refund.AdminRefundService;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
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

/**
 * 退款退货原因放到
 * Created by cj on 2016/2/1.
 */
@Controller
public class ReturnAction extends BaseAction {
    @Autowired
    AdminRefundService adminRefundService;
    @Autowired
    RefundDao refundDao;
    @Autowired
    AdminOrdersService adminOrdersService;


    /**
     * 退货管理列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "return/list", method = RequestMethod.GET)
    public String list(ModelMap model) {
//        setMenuPath("return/list");
        model.put("adminMenuState",adminMenuService.getAdminMenuState("return/list"));
        //待审核退货
        model.put("returnHandleCount",adminRefundService.getHandleReturnCount());
        return getAdminTemplate("return/list");
    }

    /**
     * 退货待处理处理列表
     * @param model
     * @return
     */
    @RequestMapping(value = "return/handle_list", method = RequestMethod.GET)
    public String handleList(ModelMap model) {
//        setMenuPath("return/handle_list");
        model.put("adminMenuState",adminMenuService.getAdminMenuState("return/handle_list"));
        return getAdminTemplate("return/handle_list");
    }



    /**
     * 退货详情以及处理退款
     *
     * @param refundId
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "return/info/{refundId}", method = RequestMethod.GET)
    public String info(@PathVariable(value = "refundId") int refundId,
                       @RequestParam(value = "type", defaultValue = "look") String type,
                       ModelMap modelMap) {
        if (refundId <= 0) {
            return "redirect:/admin/return/list";
        }
        try{
            RefundItemVo refundItemVo = adminRefundService.getRefundInfo(refundId);
            OrdersVo ordersVo = adminOrdersService.getOrdersVoInfo(refundItemVo.getOrdersId());
            modelMap.put("ordersVo", ordersVo);
            modelMap.put("refundItemVo", refundItemVo);
            modelMap.put("type", type);
            modelMap.put("refundId", refundId);
//计算可退金额
            BigDecimal paymentOnline = BigDecimal.ZERO;
            if (ordersVo.getPaymentTime()!= null && PriceHelper.isGreaterThan(ordersVo.getPredepositAmount(), BigDecimal.ZERO)){
                paymentOnline = PriceHelper.sub(ordersVo.getOrdersAmount() , ordersVo.getPredepositAmount());
            }
            modelMap.put("paymentOnline",paymentOnline);
            modelMap.put("refundDetailVo", adminRefundService.getRefundDetailVoByRefundId(refundId));
            if (type.equals("handle")) {
                //添加退款详情表
                adminRefundService.saveOrUpdateRefundDetail(refundItemVo, ordersVo);
            }

        }catch (ShopException e){
            return "redirect:/admin/return/list";
        }
//        setMenuPath("return/info");
        modelMap.put("adminMenuState",adminMenuService.getAdminMenuState("return/list"));
        return getAdminTemplate("return/info");
    }
}
