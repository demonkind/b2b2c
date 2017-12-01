package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.common.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * Created by zxy on 2016-01-12
 */
@Controller
public class ConsultAction extends BaseAction {
    @Autowired
    private SiteService siteService;
    @Autowired
    SecurityHelper securityHelper;
    /**
     * 咨询提醒文字设置
     * @param model
     * @return
     */
    @RequestMapping(value = "consult/setting", method = RequestMethod.GET)
    public String setting(ModelMap model) {
        HashMap siteInfo = siteService.getSiteInfo();
        model.put("siteInfo", siteInfo);
        return getAdminTemplate("consult/setting");
    }
    /**
     * 类型列表
     * @return
     */
    @RequestMapping(value = "consult/class", method = RequestMethod.GET)
    public String classList() {
        return getAdminTemplate("consult/class_list");
    }
    /**
     * 咨询列表
     * @return
     */
    @RequestMapping(value = "consult/list", method = RequestMethod.GET)
    public String list() {
        return getAdminTemplate("consult/list");
    }
}
