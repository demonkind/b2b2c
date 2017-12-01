package net.shopnc.b2b2c.web.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cookie")
public class CookieAction {
    final static Logger logger = Logger.getLogger(CookieAction.class);

    @RequestMapping(value = "set", method = RequestMethod.GET)
    @ResponseBody
    public String set(HttpServletResponse response, @RequestParam(value = "name", required = false,defaultValue = "dqw") String name) {
        Cookie cookie = new Cookie("name", name);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "set";
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
        public String set(@CookieValue(value = "name", required = false, defaultValue = "world") String name) {
        return "cookie" + name;
    }
}