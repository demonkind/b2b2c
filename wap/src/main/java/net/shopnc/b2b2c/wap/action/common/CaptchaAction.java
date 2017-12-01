package net.shopnc.b2b2c.wap.action.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.shopnc.b2b2c.wap.action.CaptchaHelper;

/**
 * Created by zxy on 2015-12-04
 */
@Controller
@RequestMapping("captcha")
public class CaptchaAction {

    @RequestMapping(value = "getcaptcha", method = RequestMethod.GET)
    public ModelAndView getCaptcha() throws Exception {
        new CaptchaHelper().createCaptcha();
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "check", method = RequestMethod.GET)
    public boolean check(
            @RequestParam(value = "captcha") String captcha) throws Exception {
        if (new CaptchaHelper().checkCaptcha(captcha) == true) {
            return true;
        }else{
            return false;
        }
    }
}
