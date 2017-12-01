package net.shopnc.b2b2c.wap.action.login;

import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.util.CookieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Random;

/**
 * Created by zxy on 2015-11-3.
 */
@Controller
@RequestMapping("login")
public class LoginAction extends LoginBaseAction {

    @Autowired
    private SiteService siteService;

    /**
     * 登录页面
     * @param modelMap
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        if (SessionEntity.getIsLogin() == true) {
            return "redirect:/member";
        }
        modelMap.put("member",new Member());
        int index = new Random().nextInt(4)+1;
        modelMap.put("loginImage", siteService.getSiteInfo().get("loginImage"+index+"Url"));
        return getLoginTemplate("login");
    }

    /**
     * 弹出框登录页面
     * @return
     */
    @RequestMapping(value = "/popuplogin")
    public String popupLogin() {
        /*if (SessionEntity.getIsLogin() == true) {
            return "redirect:/member";
        }*/
        return getLoginTemplate("login");
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        //销毁session
        SessionEntity.destroyIsLogin();
        SessionEntity.destroyMemberId();
        SessionEntity.destroyMemberName();
        SessionEntity.destroyMemberEmail();
        SessionEntity.destroyMemberMobile();
        //销毁自动登录cookie
//        CookieHelper.removeCookie("autologin");
        return "redirect:/login";
    }
}