package net.shopnc.b2b2c.seller.shiro;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.SellerGroup;
import net.shopnc.b2b2c.domain.store.SellerMenu;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.SellerMenuService;
import net.shopnc.b2b2c.service.store.SellerService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dqw on 2015/11/3.
 */
public class SellerRealm extends AuthorizingRealm {

    protected final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerMenuService sellerMenuService;

    public String getName() {
        return "SellerRealm";
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

        Seller seller = sellerService.findSellerByNameAndPassword(username, password);

        if (seller == null) {
            throw new AuthenticationException();
        }

        if (seller.getAllowLogin() == State.NO) {
            throw new NotAllowLoginException();
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

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        Seller seller = sellerService.findSellerByName(username);


        Set<String> roleSet = new HashSet<>();
        Set<String> permissionsSet;

        if(SellerSessionHelper.getStoreId() > 0) {
            //已开店
            roleSet.add("store");
            permissionsSet = sellerMenuService.findPremissionsByGroupId(seller.getGroupId());
        } else {
            //未开店
            roleSet.add("seller");
            permissionsSet = new HashSet<>();
            permissionsSet.add("open");
        }

        authorizationInfo.setRoles(roleSet);
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
