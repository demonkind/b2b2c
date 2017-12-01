package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 商家管理
 * Created by dqw on 2015-12-01.
 */
@Controller
public class SellerAction extends BaseAction {

    /**
     * 商家会员列表页
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "seller/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        return getAdminTemplate("store/seller/list");
    }
}
