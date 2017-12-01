package net.shopnc.b2b2c.web.action.member;

import net.shopnc.common.util.MyWebBindingInitializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by hou on 2016/3/14.
 */
@Controller
@RequestMapping("/member")
public class MemberBaseJsonAction {

    @Autowired
    protected MyWebBindingInitializer myWebBindingInitializer;

    protected final Logger logger = Logger.getLogger(getClass());

    /**
     * 过滤表单
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        myWebBindingInitializer.initBinder(binder, request);
    }
}
