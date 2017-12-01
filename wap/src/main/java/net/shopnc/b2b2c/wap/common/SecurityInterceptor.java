package net.shopnc.b2b2c.wap.common;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.util.CookieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityInterceptor implements HandlerInterceptor {
    @Autowired
    private MemberService memberService;

    /**
     * 在处理器调用之前被调用
     */
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        boolean result = false;
        //如果session存在返回成功
        if (SessionEntity.getIsLogin() == true) {
            result = true;
        }
        //自动登录
        if (result == false) {
            String token = CookieHelper.getCookie("autologin");
            if (token != null) {
                Member member = memberService.getMemberInfoByToken(token);
                if (member != null) {
                    //保存session
                    SessionEntity.setIsLogin(true);
                    SessionEntity.setMemberId(member.getMemberId());
                    SessionEntity.setMemberName(member.getMemberName());
                    SessionEntity.setMemberEmail(member.getEmail());
                    SessionEntity.setMemberMobile(member.getMobile());
                    SessionEntity.setAllowBuy(member.getAllowBuy());
                    result = true;
                }
            }
        }
        if (result == false) {
            res.sendRedirect(ShopConfig.getWebRoot() + "login");
        }
        return result;
    }
    /**
     * 在处理器调用之后执行
     */
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object arg2, ModelAndView arg3) throws Exception {
    }
    /**
     * 在请求结束之后调用
     */
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2, Exception arg3) throws Exception {
    }

}