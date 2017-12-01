package net.shopnc.b2b2c.web.action.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeErrorAction extends HomeBaseAction {

    @RequestMapping("/404")
    public String error() {
        return "404";
    }
}