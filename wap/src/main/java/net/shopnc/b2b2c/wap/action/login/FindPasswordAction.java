package net.shopnc.b2b2c.wap.action.login;


import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.*;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Random;

/**
 * Created by zxy on 2016-02-17.
 */
@Controller
@RequestMapping("findpwd")
public class FindPasswordAction extends LoginBaseAction {

    @Autowired
    private SiteService siteService;

    /**
     * 找回密码页面
     * @param modelMap
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        if (SessionEntity.getIsLogin() == true) {
            return "redirect:/member";
        }
        modelMap.put("member", new Member());
        int index = new Random().nextInt(4)+1;
        modelMap.put("loginImage", siteService.getSiteInfo().get("loginImage" + index + "Url"));
        return getLoginTemplate("find_password");
    }
    
    /**
     * 输入短信验证码页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "findPasswordPassword",method = RequestMethod.GET)
    public String findPasswordCode(ModelMap modelMap) {
    	
        return getLoginTemplate("find_password_password");
    }
}