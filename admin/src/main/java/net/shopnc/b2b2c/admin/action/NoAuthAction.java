package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.service.store.SellerMenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NoAuthAction extends BaseAction {

    @RequestMapping("noauth")
    public String index() {
        return getAdminTemplate("noauth");
    }

}