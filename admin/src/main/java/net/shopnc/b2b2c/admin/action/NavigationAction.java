package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 导航
 * Created by hbj on 2015/12/7.
 */
@Controller
public class NavigationAction extends BaseAction {
    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "navigation/list", method = RequestMethod.GET)
    public String list() {
        return getAdminTemplate("setting/navigation/list");
    }
}
