package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zxy on 2016-01-05.
 */

@Controller
public class PaymentAction extends BaseAction {
    /**
     * 支付方式列表
     * @return
     */
    @RequestMapping(value = "payment/list", method = RequestMethod.GET)
    public String paymentList() {
        return getAdminTemplate("payment/list");
    }
}