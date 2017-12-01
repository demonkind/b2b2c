package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 地区
 * Created by shopnc on 2015/10/28.
 */
@Controller
public class AreaAction extends BaseAction {


    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "area/list", method = RequestMethod.GET)
    public String list() {
        return getAdminTemplate("setting/area/list");
    }


}
