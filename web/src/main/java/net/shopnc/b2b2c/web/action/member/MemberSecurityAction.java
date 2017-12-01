package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.util.ShopHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberSecurityAction extends MemberBaseAction {

    /**
     * 账户安全页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "security", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //menuKey
        modelMap.put("menuKey", "security");
        return getMemberTemplate("information/security");
    }
    /**
     * 账户安全验证页面
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "security/auth/{type}", method = RequestMethod.GET)
    public String securityAuth(@PathVariable String type, ModelMap modelMap) {
        String[] typeArr = {"pwd","mobile","email","paypwd","predepositcash"};
        String currType = "";
        for(String v: typeArr){
            if(v.equals(type)){
                currType = v;
            }
        }
        if (currType.length()<=0) {
            return "redirect:/member/security";
        }
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        modelMap.put("type", type);
        //menuKey
        modelMap.put("menuKey", "security");
        return getMemberTemplate("information/security_auth");
    }
    /**
     * 绑定邮箱页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "security/email", method = RequestMethod.GET)
    public String securityEmail(ModelMap modelMap) {
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //如果已绑定邮箱或者手机需要进行身份认证才可进入邮箱绑定
        if ((member.getMobileIsBind()== State.YES || member.getEmailIsBind()==State.YES) && SessionEntity.getSecurityAuthState()==false) {
            return "redirect:/member/security/auth/email";
        }
        //menuKey
        modelMap.put("menuKey", "security");
        return getMemberTemplate("information/security_email");
    }
    /**
     * 绑定手机页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "security/mobile", method = RequestMethod.GET)
    public String securityMobile(ModelMap modelMap) {
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //如果已绑定手机需要进行身份认证才可绑定
        if ((member.getMobileIsBind()== State.YES || member.getEmailIsBind()==State.YES) && SessionEntity.getSecurityAuthState()==false) {
            return "redirect:/member/security/auth/mobile";
        }
        //menuKey
        modelMap.put("menuKey", "security");
        return getMemberTemplate("information/security_mobile");
    }
    /**
     * 修改密码页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "security/pwd", method = RequestMethod.GET)
    public String securityPwd(ModelMap modelMap){
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //需要进行身份认证才可修改
        if (member.getMobileIsBind() != State.YES && member.getEmailIsBind() != State.YES) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", "请先绑定邮箱或手机再修改密码");
            hashMapMessage.put("url", ShopConfig.getMemberRoot() + "security");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }
        //需要进行身份认证才可绑定
        if (SessionEntity.getSecurityAuthState()==false) {
            return "redirect:/member/security/auth/pwd";
        }
        //menuKey
        modelMap.put("menuKey", "security");
        return getMemberTemplate("information/security_pwd");
    }
    /**
     * 修改支付密码页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "security/paypwd", method = RequestMethod.GET)
    public String securityPayPwd(ModelMap modelMap){
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //需要进行身份认证才可修改
        if (member.getMobileIsBind() != State.YES && member.getEmailIsBind() != State.YES) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", "请先绑定邮箱或手机再设置支付密码");
            hashMapMessage.put("url", ShopConfig.getMemberRoot()+"security");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }
        if (SessionEntity.getSecurityAuthState()==false) {
            return "redirect:/member/security/auth/paypwd";
        }
        //menuKey
        modelMap.put("menuKey", "security");
        return getMemberTemplate("information/security_paypwd");
    }
}