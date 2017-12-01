package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.SellerMenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorAction extends BaseAction {
    @RequestMapping("error")
    public String error(ModelMap modelMap) {
        String errorMessage = SellerSessionHelper.getError();
        if(errorMessage == null || errorMessage.equals("")) {
            errorMessage = "参数错误";
        }
        modelMap.addAttribute("errorMessage", errorMessage);
        return getSellerTemplate("error");
    }
}