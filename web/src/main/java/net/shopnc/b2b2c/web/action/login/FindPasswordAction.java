package net.shopnc.b2b2c.web.action.login;

import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
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
        return getLoginTemplate("findpwd");
    }
}