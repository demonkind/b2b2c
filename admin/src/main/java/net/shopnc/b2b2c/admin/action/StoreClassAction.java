package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 店铺主营行业管理
 * Created by dqw on 2015-12-11.
 */
@Controller
public class StoreClassAction extends BaseAction {

    /**
     * 主营行业列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store_class/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        return getAdminTemplate("store/class/list");
    }

}
