package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.service.SiteService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginAction {
    @Autowired
    SiteService siteService;
    @RequestMapping("/login")
    public String login(ModelMap modelMap) {
        //如果已经登录跳转到首页
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/";
        }
        modelMap.put("config", siteService.getSiteInfo());
        return "login";
    }
}