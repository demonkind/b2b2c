package net.shopnc.b2b2c.web.action.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by hbj on 2016/2/3.
 */
@Controller
public class HomeMessageAction extends HomeBaseAction {

    /**
     * 提示消息页
     * @param message
     * @param url
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "message",method = RequestMethod.GET)
    public String message(@RequestParam(value = "message",required = false) String message,
                          @RequestParam(value = "url",required = false) String url,
                          ModelMap modelMap) {
        modelMap.put("message",message);
        modelMap.put("url",url);
        return getHomeTemplate("message");
    }
}
