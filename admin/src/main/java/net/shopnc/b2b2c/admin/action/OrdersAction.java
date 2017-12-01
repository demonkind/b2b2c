package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.OrdersPaymentCode;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.b2b2c.service.orders.AdminOrdersService;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * 订单列表
 * Created by hbj on 2016/1/25.
 */
@Controller
public class OrdersAction extends BaseAction {
    @Autowired
    private AdminOrdersService adminOrdersService;
    @Autowired
    private PaymentService paymentService;
    /**
     * 订单列表
     * @return
     */
    public OrdersAction() {
        setMenuPath("orders/list");
    }
    @RequestMapping(value = "orders/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        //支付方式列表
        HashMap<String,String> hashMap = paymentService.getAllPaymentList();
        modelMap.put("paymentListJson", JsonHelper.toJson(hashMap));

        hashMap.remove(OrdersPaymentCode.OFFLINE);
        hashMap.remove(OrdersPaymentCode.PREDEPOSIT);
        modelMap.put("paymentList", hashMap);

        //订单状态列表
        HashMap<Integer,String> hashMap1 = adminOrdersService.getOrdersStateList();
        modelMap.put("ordersStateListJson", JsonHelper.toJson(hashMap1));

        return getAdminTemplate("orders/list");
    }

    /**
     * 订单详情
     * @param ordersId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "orders/info/{ordersId}", method = RequestMethod.GET)
    public String info(@PathVariable Integer ordersId, ModelMap modelMap) {

        try {
            OrdersVo ordersVo = adminOrdersService.getOrdersVoInfo(ordersId);
            modelMap.put("ordersVo",ordersVo);
        } catch (ShopException e) {

        }
        return getAdminTemplate("orders/info");
    }
}
