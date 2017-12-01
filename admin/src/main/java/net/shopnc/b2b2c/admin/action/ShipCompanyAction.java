package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 快递公司
 * Created by shopnc on 2015/11/9.
 */
@Controller
public class ShipCompanyAction extends BaseAction {

    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "ship_company/list", method = RequestMethod.GET)
    public String list() {
        return getAdminTemplate("setting/ship_company/list");
    }
}
