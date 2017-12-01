package net.shopnc.b2b2c.wap.common.entity;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.common.util.SessionHelper;

/**
 * Created by zxy on 2015-12-04.
 */
public class SessionEntity {
    /**
     * session过期时间，默认为1小时
     */
    private final static int EXPIRED_TIME = 3600;
    /**
     * 获取session ID
     * @return
     */
    public static String getSessionId() {
        return SessionHelper.getSessionId();
    }
    /**
     * 获取session路径
     * @return
     */
    public static String getSessionPath() {
        return SessionHelper.getSessionPath();
    }
    /**
     * 创建session
     * @param name
     * @param value
     */
    public static void createSession(String name, Object value){
        SessionHelper.setSession(name, value, EXPIRED_TIME);
    }
    /**
     * 获取是否已登录
     * @return
     */
    public static boolean getIsLogin() {
        Object isLogin = SessionHelper.getSession("isLogin");
        if (isLogin == null) {
            return false;
        }else {
            if ((Boolean)isLogin == true) {
                return true;
            }else{
                return false;
            }
        }
    }
    /**
     * 设置是否已登录
     * @param isLogin
     */
    public static void setIsLogin(boolean isLogin) {
        createSession("isLogin", isLogin);
    }
    /**
     * 销毁是否已登录
     */
    public static void destroyIsLogin(){
        SessionHelper.destroySession("isLogin");
    }
    /**
     * 获得会员ID
     * @return
     */
    public static int getMemberId() {
        Object memberId = SessionHelper.getSession("memberId");
        if (memberId == null) {
            return 0;
        }else{
            return (Integer)memberId;
        }
    }
    /**
     * 设置会员ID
     * @param memberId
     */
    public static void setMemberId(int memberId) {
        createSession("memberId", memberId);
    }
    /**
     * 销毁会员ID
     */
    public static void destroyMemberId(){
        SessionHelper.destroySession("memberId");
    }
    /**
     * 获得会员名称
     */
    public static String getMemberName() {
        Object memberName = SessionHelper.getSession("memberName");
        if (memberName == null) {
            return "";
        }else{
            return (String)memberName;
        }
    }
    /**
     * 设置会员名称
     * @param memberName
     */
    public static void setMemberName(String memberName) {
        createSession("memberName", memberName);
    }
    /**
     * 销毁会员名称
     */
    public static void destroyMemberName(){
        SessionHelper.destroySession("memberName");
    }
    /**
     * 获得会员邮箱
     */
    public static String getMemberEmail() {
        Object memberEmail = SessionHelper.getSession("memberEmail");
        if (memberEmail == null) {
            return "";
        }else{
            return (String)memberEmail;
        }
    }
    /**
     * 设置会员邮箱
     * @param memberEmail
     */
    public static void setMemberEmail(String memberEmail) {
        createSession("memberEmail", memberEmail);
    }
    /**
     * 销毁会员邮箱
     */
    public static void destroyMemberEmail(){
        SessionHelper.destroySession("memberEmail");
    }
    /**
     * 获得会员手机号
     */
    public static String getMemberMobile() {
        Object memberMobile = SessionHelper.getSession("memberMobile");
        if (memberMobile == null) {
            return "";
        }else{
            return (String)memberMobile;
        }
    }
    /**
     * 设置会员手机号
     * @param memberMobile
     */
    public static void setMemberMobile(String memberMobile) {
        createSession("memberMobile", memberMobile);
    }
    /**
     * 销毁会员手机号
     */
    public static void destroyMemberMobile(){
        SessionHelper.destroySession("memberMobile");
    }
    /**
     * 获得账户安全认证的状态
     */
    public static boolean getSecurityAuthState() {
        Object securityAuthState = SessionHelper.getSession("securityAuthState");
        if (securityAuthState == null) {
            return false;
        }else {
            if ((Boolean)securityAuthState == true) {
                return true;
            }else{
                return false;
            }
        }
    }
    /**
     * 设置账户安全认证的状态
     * @param securityAuthState
     */
    public static void setSecurityAuthState(boolean securityAuthState) {
        createSession("securityAuthState", securityAuthState);
    }
    /**
     * 销毁账户安全认证的状态
     */
    public static void destroySecurityAuthState(){
        SessionHelper.destroySession("securityAuthState");
    }
    /**
     * 获得会员是否有购买权限
     */
    public static int getAllowBuy() {
        Object allowBuy = SessionHelper.getSession("allowBuy");
        if (allowBuy == null) {
            return State.NO;
        }else{
            return (Integer)allowBuy;
        }
    }
    /**
     * 设置会员是否有购买权限
     * @param allowBuy
     */
    public static void setAllowBuy(int allowBuy) {
        createSession("allowBuy", allowBuy);
    }
    /**
     * 销毁会员是否有购买权限
     */
    public static void destroyAllowBuy(){
        SessionHelper.destroySession("allowBuy");
    }
}