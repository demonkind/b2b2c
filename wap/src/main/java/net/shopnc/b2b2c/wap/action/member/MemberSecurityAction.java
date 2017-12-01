package net.shopnc.b2b2c.wap.action.member;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.util.ShopHelper;

/**
 * Created by zxy on 2016-03-14.
 * 
 * ---------页面跳转中转---------
 * 
 * 
 */

@Controller
@RequestMapping("security")
public class MemberSecurityAction extends MemberBaseAction {

    /**
     * 用户设置的跳转页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //menuKey
        modelMap.put("menuKey", "security");
        if(SessionEntity.getIsLogin()){
        	return getMemberTemplate("member_account");
    	}else{
    		return getMemberTemplate("login");
    	}
    }
    
    /**
     * 设置中“登陆密码”的跳转页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/changeMob", method = RequestMethod.GET)
    public String chuangeMob(ModelMap modelMap) {
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        modelMap.put("menuKey", "security");
        if(member.getMobile().isEmpty() || member.getMobile() == null || "".equals(member.getMobile())){
        	return getMemberTemplate("member_mobile_bind");
        }else{
        	return getMemberTemplate("member_password_step1");
        }
    }
    
    /**
     * 验证验证码成功后，跳转到修改密码页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/changePwd", method = RequestMethod.GET)
    public String chuangePwd() {
        return getMemberTemplate("member_password_step2");
    }
    
    /**
     * 个人设置中跳转到“手机验证”页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/mobVerification", method = RequestMethod.GET)
    public String mobVerification(ModelMap map) {
    	Member member = memberDao.getLockModelMemberInfo(SessionEntity.getMemberId());
    	if(member.getMobile().isEmpty() || member.getMobile() == null || "".equals(member.getMobile())){
    		return getMemberTemplate("member_mobile_bind");
    	}else{
    		map.put("member", member);
    		return getMemberTemplate("member_mobile_modify");
    	}
    }
    /**
     * 个人设置中跳转到“手机验证”的修改页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/mobChangePage", method = RequestMethod.GET)
    public String mobChangePage() throws Exception {
        return getMemberTemplate("member_mobile_bind");
    }
    /**
     * 个人设置中跳转到支付密码页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/paymentPwd", method = RequestMethod.GET)
    public String paymentPwd(ModelMap map) throws Exception{
    	Member member = memberDao.getLockModelMemberInfo(SessionEntity.getMemberId());
    	map.put("member", member);
    	//需要进行身份认证才可修改
    	if(member.getMobile().isEmpty() || member.getMobile() == null || "".equals(member.getMobile())){
//    		HashMap<String, String> hashMapMessage = new HashMap<>();
//            hashMapMessage.put("message", "请先绑定手机再设置支付密码");
//            hashMapMessage.put("url", ShopConfig.getMemberRoot()+"security");
//            map.put("menuKey", "security");
//            this.mobVerification(map);
    		return getMemberTemplate("member_mobile_bind");
    	}else{
    		return getMemberTemplate("member_paypwd_step1");
    	}
    }
    /**
     * 手机验证成功后，跳转到修改密码页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/paymentPwd2", method = RequestMethod.GET)
    public String paymentPwd2(ModelMap map) throws Exception{
		return getMemberTemplate("member_paypwd_step2");

    }

    /**
     * 个人设置中跳转到用户反馈页面   TODO
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/feedBack", method = RequestMethod.GET)
    public String feedBack() {
        return getMemberTemplate("");
    }
    /**
     * 账户安全验证页面
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
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
    @RequestMapping(value = "/email", method = RequestMethod.GET)
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
    @RequestMapping(value = "/mobile", method = RequestMethod.GET)
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
    @RequestMapping(value = "/pwd", method = RequestMethod.GET)
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
    @RequestMapping(value = "/paypwd", method = RequestMethod.GET)
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
        return getMemberTemplate("");
    }
}