package net.shopnc.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session存取<br/>
 * Created by zxy on 2015-12-02.
 */
public class SessionHelper {
    /**
     * session前缀
     */
    private static String prefix = "ncSession_";

    /**
     * 设置Session
     * @param name session名称
     * @param value session值
     * @param expiredTime 过期时间
     */
    public static void setSession(String name, Object value, int expiredTime) {
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(expiredTime);
        session.setAttribute(prefix + name, value);
    }

    /**
     * 获取Session
     * @param name session名称
     * @return session值
     */
    public static Object getSession(String name) {
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        return session.getAttribute(prefix + name);
    }

    /**
     * 销毁Session
     * @param name session名称
     */
    public static void destroySession(String name) {
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute(prefix + name);
    }

    /**
     * 获取session ID
     * @return sessionID
     */
    public static String getSessionId() {
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        return session.getId();
    }

    /**
     * 获取session路径
     * @return session路径
     */
    public static String getSessionPath() {
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getContextPath()+"/";
    }
}
