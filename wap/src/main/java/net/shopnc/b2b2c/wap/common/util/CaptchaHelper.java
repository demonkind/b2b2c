package net.shopnc.b2b2c.wap.common.util;

import com.google.code.kaptcha.Producer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * Created by zxy on 2015-12-04.
 */
public class CaptchaHelper {

    /**
     * 生成验证码
     * @throws Exception
     */
    public void createCaptcha() throws Exception{
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        Producer captchaProducer = (Producer)context.getBean("captchaProducer");

        HttpSession session = request.getSession();

        HttpServletResponse response  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

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
    }
    /**
     * 验证验证码是否正确
     * @param captcha
     * @return
     * @throws Exception
     */
    public boolean checkCaptcha(String captcha) throws Exception{
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Object sessionCaptcha = session.getAttribute("captcha");
        if(sessionCaptcha == null) {
            return false;
        }
        if(captcha.equals(sessionCaptcha)) {
            return true;
        }
        return false;
    }
}
