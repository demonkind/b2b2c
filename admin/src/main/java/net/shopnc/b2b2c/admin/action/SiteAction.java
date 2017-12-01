package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

@Controller
public class SiteAction extends BaseAction {

    @Autowired
    private SiteService siteService;

    /**
     * 基本设置页面
     * @param model
     * @return
     */
    @RequestMapping(value = "site/edit", method = RequestMethod.GET)
    public String edit(ModelMap model) {
        HashMap siteInfo = siteService.getSiteInfo();
        model.put("siteInfo", siteInfo);
        return getAdminTemplate("setting/site/edit");
    }
    /**
     * 登录设置页面
     * @param model
     * @return
     */
    @RequestMapping(value = "site/login", method = RequestMethod.GET)
    public String sitelogin(ModelMap model) {
        HashMap siteInfo = siteService.getSiteInfo();
        model.put("siteInfo", siteInfo);
        return getAdminTemplate("setting/site/login");
    }
    /**
     * 修改汇款信息页面
     * @param model
     * @return
     */
    @RequestMapping(value = "site/pay_info", method = RequestMethod.GET)
    public String payInfo(ModelMap model) {
        HashMap siteInfo = siteService.getSiteInfo();
        model.put("siteInfo", siteInfo);
        return getAdminTemplate("setting/site/pay_info");
    }
    /**
     * 修改默认搜索页面
     * @param model
     * @return
     */
    @RequestMapping(value = "site/default_search", method = RequestMethod.GET)
    public String defaultSearch(ModelMap model) {
        HashMap siteInfo = siteService.getSiteInfo();
        model.put("siteInfo", siteInfo);
        return getAdminTemplate("setting/site/default_search");
    }
}