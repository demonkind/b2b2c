package net.shopnc.b2b2c.seller.action;

/**
 * Created by dqw on 2015/12/04.
 */

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class DefaultExceptionHandler {

    protected final Logger logger = Logger.getLogger(getClass());

    /**
     * 全局默认异常处理
     */
//    @ExceptionHandler({Exception.class})
//    public ModelAndView exception(Exception e) {
//        logger.error(e.toString());
//        return new ModelAndView(UrlSeller.HOME);
//    }

}
