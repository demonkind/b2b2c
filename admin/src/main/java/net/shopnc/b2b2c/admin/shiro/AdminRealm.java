package net.shopnc.b2b2c.admin.shiro;

import net.shopnc.b2b2c.dao.admin.AdminDao;
import net.shopnc.b2b2c.dao.admin.AdminMenuDao;
import net.shopnc.b2b2c.domain.admin.Admin;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dqw on 2015/11/3.
 */
public class AdminRealm extends AuthorizingRealm {

    protected final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AdminMenuDao adminMenuDao;

    public String getName() {
        return "AdminRealm";
    }

    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken类型的Token
        return token instanceof UsernamePasswordCaptchaToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authenticationToken;
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        String captcha = token.getCaptcha();

        String sessionCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
        if (null == captcha || !captcha.equalsIgnoreCase(sessionCaptcha)) {
            throw new CaptchaException();
        }

        Admin admin = adminDao.findByNameAndPassword(username, password);

        if (admin == null) {
            throw new AuthenticationException();
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                username,
                password,
                getName()
        );
        return authenticationInfo;

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();

        Admin admin = adminDao.findByName(username);

        Set<String> permissionsSet = adminMenuDao.findAdminPermissionByGroupId(admin.getGroupId());

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        authorizationInfo.setStringPermissions(permissionsSet);

        return authorizationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
