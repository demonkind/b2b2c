package net.shopnc.b2b2c.admin.action;

/**
 * Created by dqw on 2015/12/04.
 */

import net.shopnc.b2b2c.constant.UrlAdmin;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class DefaultExceptionHandler {

    protected final Logger logger = Logger.getLogger(getClass());

    /**
     * 没有权限异常处理
     */
//    @ExceptionHandler({Exception.class})
//    public ModelAndView exception(Exception e) {
//        logger.error(e.toString());
//        return new ModelAndView(UrlAdmin.HOME);
//    }

}
