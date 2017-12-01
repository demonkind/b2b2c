package net.shopnc.b2b2c.wap.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/session")
public class SessionAction {
    final static Logger logger = Logger.getLogger(SessionAction.class);

    @RequestMapping(value = "set", method = RequestMethod.GET)
    @ResponseBody
    public String set(HttpSession session, @RequestParam(value = "name", required = false, defaultValue = "world") String name) {
        session.setAttribute("name", name);
        return "set";
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public String get(HttpSession session) {
        String name = (String) session.getAttribute("name");
        return "session" + name;
    }
}