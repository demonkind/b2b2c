package net.shopnc.b2b2c.admin.action;

import com.google.code.kaptcha.Producer;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * Created by dqw on 2015/11/20.
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaAction {
    @Autowired
    private Producer captchaProducer = null;

    @RequestMapping(value = "captcha")
    public ModelAndView getKaptchaImage(
            @RequestParam(value = "t", required = false) String t,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();
        session.setAttribute("captcha", capText);

        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "check")
    public String check(
            @RequestParam(value = "captcha", required = false) String captcha) {

        String sessionCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");

        if(captcha == null || sessionCaptcha == null) {
            return "true";
        }

        if(captcha.equalsIgnoreCase(sessionCaptcha)) {
            return "true";
        }

        return "false";
    }
}
