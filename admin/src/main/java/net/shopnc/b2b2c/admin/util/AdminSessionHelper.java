package net.shopnc.b2b2c.admin.util;

import net.shopnc.b2b2c.domain.admin.AdminMenu;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.util.List;

/**
 * Created by dqw on 2015/12/29.
 */
public class AdminSessionHelper {
    /**
     * session前缀
     */
    private static String prefix = "ncSession_";

    public static int getAdminId() {
        Session session = SecurityUtils.getSubject().getSession();
        return (Integer) session.getAttribute(prefix + "adminId");
    }

    public static void setAdminId(int adminId) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "adminId", adminId);
    }

    public static String getAdminName() {
        Session session = SecurityUtils.getSubject().getSession();
        return (String) session.getAttribute(prefix + "adminName");
    }

    public static void setAdminName(String adminName) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "adminName", adminName);
    }

    public static String getAdminGroup() {
        Session session = SecurityUtils.getSubject().getSession();
        return (String) session.getAttribute(prefix + "adminGroup");
    }

    public static void setAdminGroup(String adminGroup) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "adminGroup", adminGroup);
    }

    public static String getAdminAvatarUrl() {
        Session session = SecurityUtils.getSubject().getSession();
        return (String) session.getAttribute(prefix + "adminAvatarUrl");
    }

    public static void setAdminAvatarUrl(String adminAvatar) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "adminAvatarUrl", adminAvatar);
    }

    public static List<AdminMenu> getAdminMenu() {
        Session session = SecurityUtils.getSubject().getSession();
        return (List<AdminMenu>) session.getAttribute(prefix + "adminMenu");
    }

    public static void setAdminMenu(List<AdminMenu> adminMenu) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "adminMenu", adminMenu);
    }
}

