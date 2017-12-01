package net.shopnc.b2b2c.wap.action.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.EmailCodeSendType;
import net.shopnc.b2b2c.constant.SmsCodeSendType;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.member.EmailCodeDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.SmsCodeDao;
import net.shopnc.b2b2c.domain.member.EmailCode;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.SmsCode;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.EmailCodeService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.member.SmsCodeService;
import net.shopnc.b2b2c.wap.action.CaptchaHelper;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.PriceHelper;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
@RequestMapping("security")
public class MemberSecurityJsonAction extends MemberBaseJsonAction {
    @Autowired
    private EmailCodeService emailCodeService;
    @Autowired
    private SmsCodeService smsCodeService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EmailCodeDao emailCodeDao;
    @Autowired
    private SmsCodeDao smsCodeDao;
    @Autowired
    private MemberDao memberDao;
 
    /**
     * 账户安全验证
     * @param type
     * @param authType
     * @param authCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/auth/{type}", method = RequestMethod.POST)
    public ResultEntity securityAuthSave(@PathVariable String type,
                                         @RequestParam("authType") String authType,
                                         @RequestParam("authCode") String authCode) {
        ResultEntity resultEntity = new ResultEntity();
        String[] typeArr = {"pwd","mobile","email","paypwd","predepositcash"};
        String currType = "";
        for(String v: typeArr){
            if(v.equals(type)){
                currType = v;
            }
        }
        if (currType.length()<=0) {
            resultEntity.setUrl(ShopConfig.getMemberRoot() +"security");
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        if (!(authType.equals("email") || authType.equals("mobile"))) {
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (authType.equals("email")) {
            if (member.getEmail()==null || member.getEmail().length()<=0) {
                resultEntity.setMessage("参数错误");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
            //验证动态码
            EmailCode emailCode = new EmailCode();
            emailCode.setSendType(EmailCodeSendType.EMAILAUTH);
            emailCode.setEmail(member.getEmail());
            emailCode.setAuthCode(authCode);
            if (emailCodeDao.checkCode(emailCode).equals(false)) {
                resultEntity.setMessage("动态码错误或已过期，重新输入");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }else{
                SessionEntity.setSecurityAuthState(true);
                if (type.equals("predepositcash")) {//预存款提现
                    resultEntity.setUrl(ShopConfig.getMemberRoot()+"predeposit/cash");
                }else{
                    resultEntity.setUrl(ShopConfig.getMemberRoot()+"security/"+type);
                }
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("验证成功");
                return resultEntity;
            }
        } else if (authType.equals("mobile")) {
            if (member.getMobile()==null || member.getMobile().length()<=0) {
                resultEntity.setMessage("参数错误");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
            //验证动态码
            SmsCode smsCode = new SmsCode();
            smsCode.setSendType(SmsCodeSendType.MOBILEAUTH);
            smsCode.setMobilePhone(member.getMobile());
            smsCode.setAuthCode(authCode);
            if (smsCodeDao.checkCode(smsCode).equals(false)) {
                resultEntity.setMessage("动态码错误或已过期，重新输入");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }else{
                SessionEntity.setSecurityAuthState(true);
                if (type.equals("predepositcash")) {//预存款提现
                    resultEntity.setUrl(ShopConfig.getMemberRoot()+"predeposit/cash");
                }else{
                    resultEntity.setUrl(ShopConfig.getMemberRoot()+"security/"+type);
                }
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("验证成功");
                return resultEntity;
            }
        }else{
            SessionEntity.setSecurityAuthState(false);
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("验证失败");
            return resultEntity;
        }
    }
    /**
     * 保存绑定邮箱
     * @param email
     * @param authCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public ResultEntity securityEmailSave(@RequestParam("email") String email,
                                          @RequestParam("authCode") String authCode) {
        ResultEntity resultEntity = new ResultEntity();
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (member == null) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //如果已绑定邮箱需要进行验证才可绑定
        if ((member.getMobileIsBind()== State.YES || member.getEmailIsBind()==State.YES) && SessionEntity.getSecurityAuthState()==false) {
            resultEntity.setUrl(ShopConfig.getMemberRoot() + "security/auth/email");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证邮箱
        Pattern patternEmail = Pattern.compile(".*@.*");
        Matcher matcherEmail = patternEmail.matcher(email);
        if (!matcherEmail.matches()) {
            resultEntity.setMessage("请填写有效邮箱");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        try {
            if (memberService.bindingEmail(authCode, email, member.getMemberId()) == true) {
                SessionEntity.destroySecurityAuthState();
                resultEntity.setMessage("邮箱绑定成功");
                resultEntity.setCode(ResultEntity.SUCCESS);
                return resultEntity;
            }else{
                resultEntity.setMessage("邮箱绑定失败");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
        } catch (MemberExistingException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        } catch (ShopException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
    }
    /**
     * 保存绑定手机
     * @param mobile
     * @param authCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mobile", method = RequestMethod.POST)
    public ResultEntity securityMobileSave(@RequestParam("mobile") String mobile,
                                           @RequestParam("authCode") String authCode) {
        ResultEntity resultEntity = new ResultEntity();
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (member == null) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //如果已绑定手机需要进行验证才可绑定
        if ((member.getMobileIsBind()== State.YES || member.getEmailIsBind()==State.YES) && SessionEntity.getSecurityAuthState()==false) {
            resultEntity.setUrl(ShopConfig.getMemberRoot() + "security/auth/mobile");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证手机号
        Pattern patternMobile = Pattern.compile("^[1][0-9]{10}$");
        Matcher matcherMobile = patternMobile.matcher(mobile);
        if (!matcherMobile.matches()) {
            resultEntity.setMessage("请填写有效手机号");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        try {
            if (memberService.bindingMobile(authCode, mobile, member.getMemberId()) == true) {
                SessionEntity.destroySecurityAuthState();
                resultEntity.setMessage("手机绑定成功");
                resultEntity.setCode(ResultEntity.SUCCESS);
                return resultEntity;
            }else{
                resultEntity.setMessage("手机绑定失败");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
        } catch (MemberExistingException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        } catch (ShopException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
    }
    /**
     * 保存会员密码
     * @param memberPwd
     * @param repeatMemberPwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pwd", method = RequestMethod.POST)
    public ResultEntity securityPwdSave(@RequestParam("memberPwd") String memberPwd,
                                        @RequestParam("repeatMemberPwd") String repeatMemberPwd) {
        ResultEntity resultEntity = new ResultEntity();
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (member == null) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //需要进行验证才可绑定
        //TODO
//        if (SessionEntity.getSecurityAuthState()==false) {
//            resultEntity.setUrl(ShopConfig.getMemberRoot() + "security/auth/pwd");
//            resultEntity.setCode(ResultEntity.FAIL);
//            return resultEntity;
//        }
        try {
            if (memberService.modifyMemberPwd(memberPwd, repeatMemberPwd, member.getMemberId()) == true) {
                SessionEntity.destroySecurityAuthState();
                resultEntity.setMessage("密码修改成功");
                resultEntity.setCode(ResultEntity.SUCCESS);
                return resultEntity;
            }else{
                resultEntity.setMessage("密码修改失败");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
        } catch (ShopException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
    }
    /**
     * 保存支付密码
     * @param payPwd
     * @param repeatPayPwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/paypwd", method = RequestMethod.POST)
    public ResultEntity securityPayPwdSave(@RequestParam("payPwd") String payPwd,
                                        @RequestParam("repeatPayPwd") String repeatPayPwd) {
        ResultEntity resultEntity = new ResultEntity();
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (member == null) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //需要进行验证才可修改 TODO
//        if (SessionEntity.getSecurityAuthState()==false) {
//            resultEntity.setUrl(ShopConfig.getMemberRoot() + "security/auth/paypwd");
//            resultEntity.setCode(ResultEntity.FAIL);
//            return resultEntity;
//        }
        try {
            if (memberService.modifyPayPwd(payPwd, repeatPayPwd, member.getMemberId()) == true) {
                SessionEntity.destroySecurityAuthState();
                resultEntity.setMessage("支付密码修改成功");
                resultEntity.setCode(ResultEntity.SUCCESS);
                return resultEntity;
            }else{
                resultEntity.setMessage("支付密码修改失败");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
        } catch (ShopException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
    }
    /**
     * 验证动态码正确后修改密码
     * @author luo
     * @param authType
     * @param captcha
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public ResultEntity securityUpdatePassword(@RequestParam("authCode") String authCode,@RequestParam("password") String password,@RequestParam("password1") String password1) throws Exception{
    	ResultEntity resultEntity = new ResultEntity();
        //查询会员信息
        Member memberInfo = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (memberInfo==null) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setSendType(SmsCodeSendType.MOBILEAUTH);
        smsCode.setAuthCode(authCode);
        smsCode.setMemberId(SessionEntity.getMemberId());
        smsCode.setMobilePhone(memberInfo.getMobile());
        if (smsCodeDao.checkCode(smsCode).equals(false)) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("动态码错误或已过期，请重新输入");
            return resultEntity;
        }
        try {
            //修改密码
            memberService.modifyMemberPwd(password, password1, memberInfo.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            return resultEntity;
        }catch (ShopException e){
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
    /**
     * 个人设置中验证当前绑定手机
     * @author luo
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/mobChange", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity mobChange(@RequestParam("authCode") String authCode,@RequestParam("mobile") String mobile) throws Exception {
    	 ResultEntity resultEntity = new ResultEntity();
    	 SmsCode smsCode = new SmsCode();
         smsCode.setSendType(SmsCodeSendType.MOBILEAUTH);
         smsCode.setAuthCode(authCode);
         smsCode.setMemberId(SessionEntity.getMemberId());
         smsCode.setMobilePhone(mobile);
         if (smsCodeDao.checkCode(smsCode).equals(false)) {
             resultEntity.setCode(ResultEntity.FAIL);
             resultEntity.setMessage("动态码错误或已过期，请重新输入");
             return resultEntity;
         }else{
        	 resultEntity.setCode(ResultEntity.SUCCESS);
             return resultEntity;
         }
    }
    /**
     * 验证码和手机动态码同时校验/绑定手机
     * @author luo
     * @param authType
     * @param captcha
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/boundPhone", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity boundPhone(@RequestParam("mobile") String mobile,@RequestParam("authCode") String authCode) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //验证码验证
        //查询会员信息
        Member memberInfo = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (memberInfo==null) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setSendType(SmsCodeSendType.MOBILEBIND);
        smsCode.setAuthCode(authCode);
        smsCode.setMemberId(SessionEntity.getMemberId());
        smsCode.setMobilePhone(mobile);
        if (smsCodeDao.checkCode(smsCode).equals(false)) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("动态码错误或已过期，请重新输入");
            return resultEntity;
        }
        //（重新）绑定手机
        Pattern patternMobile = Pattern.compile("^[1][0-9]{10}$");
        Matcher matcherMobile = patternMobile.matcher(mobile);
        if (!matcherMobile.matches()) {
            resultEntity.setMessage("请填写有效手机号");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        try {
            if (memberService.bindingMobile(authCode, mobile, SessionEntity.getMemberId()) == true) {
                SessionEntity.destroySecurityAuthState();
                resultEntity.setMessage("手机绑定成功");
                resultEntity.setCode(ResultEntity.SUCCESS);
                return resultEntity;
            }else{
                resultEntity.setMessage("手机绑定失败");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
        } catch (MemberExistingException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        } catch (ShopException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
    }
    /**
     * 认证发送动态码不需要验证码
     * @param authType
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/sendcode/auth/simple", method = RequestMethod.POST)
    public ResultEntity securitySendCodeAuthSimple(@RequestParam("authType") String authType) throws Exception{
        return this.securitySendCodeAuth(authType);
    }
    /**
     * 认证发送动态码(旧)
     * @param authType
     * @return
     */
    private ResultEntity securitySendCodeAuth(String authType) {
        ResultEntity resultEntity = new ResultEntity();
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (!(authType.equals("email") || authType.equals("mobile"))) {
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证码验证
        if (authType.equals("email")) {
            if (member.getEmail() == null || member.getEmail().length()<=0) {
                resultEntity.setMessage("邮箱错误");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
            //发送动态码
            EmailCode emailCode = new EmailCode();
            emailCode.setEmail(member.getEmail());
            emailCode.setSendType(EmailCodeSendType.EMAILAUTH);
            emailCode.setMemberId(member.getMemberId());
            try{
                Serializable logId = emailCodeService.send(emailCode);
                //返回值
                HashMap<String, Object> returnMap = new HashMap<String, Object>();
                //查询该记录详情-----方便测试暂时输出code，后期删除----------------
                /*EmailCode emailCodeInfo = emailCodeDao.get(EmailCode.class, logId);
                returnMap.put("authCode", emailCodeInfo.getAuthCode());*/
                returnMap.put("authCodeValidTime", Common.EMAIL_AUTHCODE_VALID_TIME/60);
                returnMap.put("authCodeResendTime", Common.EMAIL_AUTHCODE_RESEND_TIME);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setData(returnMap);
                return resultEntity;
            } catch (MemberExistingException e){
                logger.warn(e.getMessage());
                //抛出异常
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage(e.getMessage());
                return resultEntity;
            } catch (ShopException e){
                logger.warn(e.getMessage());
                //抛出异常
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage(e.getMessage());
                return resultEntity;
            }
        } else if (authType.equals("mobile")) {
            if (member.getMobile() == null || member.getMobile().length()<=0) {
                resultEntity.setMessage("手机号错误");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
            //发送动态码
            SmsCode smsCode = new SmsCode();
            smsCode.setMobilePhone(member.getMobile());
            smsCode.setSendType(SmsCodeSendType.MOBILEAUTH);
            smsCode.setMemberId(member.getMemberId());
            try{
                Serializable logId = smsCodeService.send(smsCode);
                //返回值
                HashMap<String, Object> returnMap = new HashMap<String, Object>();
                //查询该记录详情-----方便测试暂时输出code，后期删除----------------
                /*SmsCode smsCodeInfo = smsCodeDao.get(SmsCode.class, logId);
                returnMap.put("authCode", smsCodeInfo.getAuthCode());*/
                returnMap.put("authCodeValidTime", Common.SMS_AUTHCODE_VALID_TIME/60);
                returnMap.put("authCodeResendTime", Common.SMS_AUTHCODE_RESEND_TIME);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setData(returnMap);
                return resultEntity;
            } catch (MemberExistingException e){
                logger.warn(e.getMessage());
                //抛出异常
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage(e.getMessage());
                return resultEntity;
            } catch (ShopException e){
                logger.warn(e.getMessage());
                //抛出异常
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage(e.getMessage());
                return resultEntity;
            }
        }else{
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("发送失败");
            return resultEntity;
        }
    }
    /**
     * 绑定邮箱时发送动态码
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendcode/bind/email", method = RequestMethod.POST)
    public ResultEntity securitySendCodeBindEmail(@RequestParam("email") String email) {
        ResultEntity resultEntity = new ResultEntity();
        if (email == null || email.length()<=0) {
            resultEntity.setMessage("请填写邮箱");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证邮箱
        Pattern patternEmail = Pattern.compile(".*@.*");
        Matcher matcherEmail = patternEmail.matcher(email);
        if (!matcherEmail.matches()) {
            resultEntity.setMessage("请填写有效邮箱");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //发送动态码
        EmailCode emailCode = new EmailCode();
        emailCode.setEmail(email);
        emailCode.setSendType(EmailCodeSendType.EMAILBIND);
        emailCode.setMemberId(SessionEntity.getMemberId());
        try{
            Serializable logId = emailCodeService.send(emailCode);
            //返回值
            HashMap<String, Object> returnMap = new HashMap<String, Object>();
            //查询该记录详情-----方便测试暂时输出code，后期删除----------------
            /*EmailCode emailCodeInfo = emailCodeDao.get(EmailCode.class, logId);
            returnMap.put("authCode", emailCodeInfo.getAuthCode());*/
            returnMap.put("authCodeValidTime", Common.EMAIL_AUTHCODE_VALID_TIME/60);
            returnMap.put("authCodeResendTime", Common.EMAIL_AUTHCODE_RESEND_TIME);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(returnMap);
        } catch (MemberExistingException e){
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (ShopException e){
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
    /**
     * 绑定手机号时验证是否已绑定过手机并发送动态码
     * @author luo
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/verMobile", method = RequestMethod.POST)
    public ResultEntity verMobile(@RequestParam("mobile") String mobile,@RequestParam("captcha") String captcha) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        if (new CaptchaHelper().checkCaptcha(captcha) == false) {
            logger.info("验证码错误");
            resultEntity.setMessage("验证码错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        if (mobile == null || mobile.length()<=0) {
            resultEntity.setMessage("请填写手机号");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //发送动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setMobilePhone(mobile);
        smsCode.setSendType(SmsCodeSendType.MOBILEAUTH);
        smsCode.setMemberId(SessionEntity.getMemberId());
        try{
            Serializable logId = smsCodeService.send(smsCode);
            //返回值
            HashMap<String, Object> returnMap = new HashMap<String, Object>();
            //查询该记录详情-----方便测试暂时输出code，后期删除----------------
            /*SmsCode smsCodeInfo = smsCodeDao.get(SmsCode.class, logId);
            returnMap.put("authCode", smsCodeInfo.getAuthCode());*/
            returnMap.put("authCodeValidTime", Common.SMS_AUTHCODE_VALID_TIME/60);
            returnMap.put("authCodeResendTime", Common.SMS_AUTHCODE_RESEND_TIME);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(returnMap);
        } catch (MemberExistingException e){
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (ShopException e){
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
   
    /**
     * 绑定手机号时发送动态码
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bindMobile", method = RequestMethod.POST)
    public ResultEntity securitySendCodeBindMobile(@RequestParam("mobile") String mobile,@RequestParam("captcha") String captcha) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        if (new CaptchaHelper().checkCaptcha(captcha) == false) {
            logger.info("验证码错误");
            resultEntity.setMessage("验证码错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        if (mobile == null || mobile.length()<=0) {
            resultEntity.setMessage("请填写手机号");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证手机号
        Pattern patternMobile = Pattern.compile("^[1][0-9]{10}$");
        Matcher matcherMobile = patternMobile.matcher(mobile);
        if (!matcherMobile.matches()) {
            resultEntity.setMessage("请填写有效手机号");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //发送动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setMobilePhone(mobile);
        smsCode.setSendType(SmsCodeSendType.MOBILEBIND);
        smsCode.setMemberId(SessionEntity.getMemberId());
        try{
            Serializable logId = smsCodeService.send(smsCode);
            //返回值
            HashMap<String, Object> returnMap = new HashMap<String, Object>();
            //查询该记录详情-----方便测试暂时输出code，后期删除----------------
            /*SmsCode smsCodeInfo = smsCodeDao.get(SmsCode.class, logId);
            returnMap.put("authCode", smsCodeInfo.getAuthCode());*/
            returnMap.put("authCodeValidTime", Common.SMS_AUTHCODE_VALID_TIME/60);
            returnMap.put("authCodeResendTime", Common.SMS_AUTHCODE_RESEND_TIME);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(returnMap);
        } catch (MemberExistingException e){
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (ShopException e){
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
    
    /**
     * 支付密码页面验证手机动态码
     * @author 
     * @param authType
     * @param captcha
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mobAuthentication", method = RequestMethod.POST)
    @ResponseBody
    public ResultEntity mobAuthentication(@RequestParam("mobile") String mobile,@RequestParam("authCode") String authCode) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //验证码验证
        //查询会员信息
        Member memberInfo = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (memberInfo==null) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setSendType(SmsCodeSendType.MOBILEAUTH);
        smsCode.setAuthCode(authCode);
        smsCode.setMemberId(SessionEntity.getMemberId());
        smsCode.setMobilePhone(mobile);
        if (smsCodeDao.checkCode(smsCode).equals(false)) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("动态码错误或已过期，请重新输入");
            return resultEntity;
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
		return resultEntity;
    }
    
    /**
     * 支付密码验证
     * @param payPwd
     * @param ordersOnlineAmount
     * @param predepositAmount
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/validatePayPwd",method = RequestMethod.POST)
    public ResultEntity validatePayPwd(@RequestParam(value = "payPwd",required = true) String payPwd,
                                       @RequestParam(value = "ordersOnlineAmount",required = false) BigDecimal ordersOnlineAmount,
                                       @RequestParam(value = "predepositAmount",required = false) BigDecimal predepositAmount) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            memberService.validatePayPwd(payPwd,SessionEntity.getMemberId());
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            hashMap.put("payDiffAmount", PriceHelper.sub(ordersOnlineAmount,predepositAmount));
            resultEntity.setData(hashMap);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            resultEntity.setMessage("系统运行错误");
            logger.error(e.getMessage());
        }
        return resultEntity;
    }
    
}