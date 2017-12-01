package net.shopnc.b2b2c.wap.action.login;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.SmsCodeSendType;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.SmsCodeDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.SmsCode;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.member.GoodsBrowseService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.member.SmsCodeService;
import net.shopnc.b2b2c.wap.action.CaptchaHelper;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */
@Controller
@RequestMapping("register")
public class RegisterJsonAction extends LoginBaseJsonAction {

    @Autowired
    private MemberService memberService;
    @Autowired
    private SmsCodeService smsCodeService;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SiteService siteService;
    @Autowired
    private GoodsBrowseService goodsBrowseService;
    @Autowired
    private SmsCodeDao smsCodeDao;

    /**
     * 普通注册
     * @param member
     * @param bindingResult
     * @param repeatMemberPwd
     * @param captcha
     * @param agreeClause
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "common", method = RequestMethod.POST)
    public ResultEntity save(@Valid Member member,
                       BindingResult bindingResult,
                       @RequestParam("repeatMemberPwd") String repeatMemberPwd,
                       @RequestParam("agreeClause") Boolean agreeClause) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        
        //数据验证
        if (!repeatMemberPwd.equals(member.getMemberPwd())) {
            logger.info("两次输入的密码不一致");
            resultEntity.setMessage("两次输入的密码不一致");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
      //注册协议
        if (!agreeClause) {
            logger.info("请勾选服务协议");
            resultEntity.setMessage("请勾选服务协议");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        } 
        //验证码验证
        //TODO
//        if (new CaptchaHelper().checkCaptcha(captcha) == false) {
//            logger.info("验证码错误");
//            resultEntity.setMessage("验证码错误");
//            resultEntity.setCode(ResultEntity.FAIL);
//            return resultEntity; 
//        }

        //获取表单错误信息
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.info(error.getDefaultMessage());
            }
            resultEntity.setMessage("注册失败");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //注册用户
        try {
            Member memberInfo = memberService.register(member);
            //保存session
            SessionEntity.setIsLogin(true);
            SessionEntity.setMemberId(memberInfo.getMemberId());
            SessionEntity.setMemberName(memberInfo.getMemberName());
            SessionEntity.setMemberEmail(memberInfo.getEmail());
            SessionEntity.setMemberMobile("");
            SessionEntity.setAllowBuy(memberInfo.getAllowBuy());
            super.initCart(SessionEntity.getMemberId());
            //-------------------合并浏览历史（待加入队列等后台运行）-----------------
            goodsBrowseService.mergeGoodsBrowse(memberInfo.getMemberId());

            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("注册成功");
            
            return resultEntity;
        } catch (MemberExistingException e) {
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (ParameterErrorException e) {
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
    
    /**
     * 手机注册第一步
     * @param member
     * @param captcha
     * @param agreeClause
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "mobilefirst", method = RequestMethod.POST)
    public ResultEntity saveMobileFirst(@RequestParam("mobile") String mobile,
                                   @RequestParam("captcha") String captcha,
                                   @RequestParam("agreeClause") Boolean agreeClause) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //注册协议
        if (!agreeClause) {
            logger.info("请勾选服务协议");
            resultEntity.setMessage("请勾选服务协议");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        } 
        //验证码验证
        if (new CaptchaHelper().checkCaptcha(captcha) == false) {
            logger.info("验证码错误");
            resultEntity.setMessage("验证码错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        return this.mobileRegisterSendCode(mobile);
    }
    /**
     * 手机注册发送动态码
     * @param member
     * @return
     */
    private ResultEntity mobileRegisterSendCode(String mobile){
        ResultEntity resultEntity = new ResultEntity();
        //系统未开启手机注册功能
        if (!siteService.getSiteInfo().get(SiteTitle.SMSREGISTER).equals("1")) {
            logger.info("系统未开启手机注册功能");
            resultEntity.setMessage("系统未开启手机注册功能");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //发送动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setMobilePhone(mobile);
        smsCode.setSendType(SmsCodeSendType.REGISTER);
        try{
            Serializable logId = smsCodeService.send(smsCode);
            //返回值
            HashMap<String, Object> returnMap = new HashMap<String, Object>();
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
     * 手机注册重新发送动态码
     * @param member
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mobilesecond/again", method = RequestMethod.POST)
    public ResultEntity mobileSecondAgain(@RequestParam(value="mobile")String mobile) {
        return this.mobileRegisterSendCode(mobile);
    }
    
    /**
     * 验证手机注册验证码是否正确
     * @param mobile
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "codeCheck", method = RequestMethod.POST)
    public ResultEntity mobCodeCheck(@RequestParam("mobile") String mobile,
    		@RequestParam("captcha") String captcha,@RequestParam("mobilecode") String mobilecode) throws Exception {
    	ResultEntity resultEntity = new ResultEntity();
    	//验证码验证
        if (new CaptchaHelper().checkCaptcha(captcha) == false) {
            logger.info("验证码错误");
            resultEntity.setMessage("验证码错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
    	//验证手机动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setSendType(SmsCodeSendType.REGISTER);
        smsCode.setMobilePhone(mobile);
        smsCode.setAuthCode(mobilecode);
        if (smsCodeDao.checkCode(smsCode).equals(false)) {
        	logger.info("动态码错误或已过期");
            resultEntity.setMessage("动态码错误或已过期，重新输入");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }else{
        	logger.info("手机动态验证码验证成功");
            resultEntity.setCode(ResultEntity.SUCCESS);
            return resultEntity;
        }
    }
    
    
    /**
     * 手机注册
     * @param member
     * @param repeatMemberPwd
     * @param authCode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "mobilesecond", method = RequestMethod.POST)
    public ResultEntity saveMobileSecond(@RequestParam("mobile") String mobile,
    		@RequestParam("memberPwd") String memberPwd) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //数据验证
//        if (!repeatMemberPwd.equals(member.getMemberPwd())) {
//            logger.info("两次输入的密码不一致");
//            resultEntity.setMessage("两次输入的密码不一致");
//            resultEntity.setCode(ResultEntity.FAIL);
//            return resultEntity;
//        }
        //注册用户
        try {
        	Member member = new Member();
        	member.setMobile(mobile);
        	member.setMemberPwd(memberPwd);
            Member memberInfo = memberService.registerMobile(member,null);

            //保存session
            SessionEntity.setIsLogin(true);
            SessionEntity.setMemberId(memberInfo.getMemberId());
            SessionEntity.setMemberName(memberInfo.getMemberName());
            SessionEntity.setMemberEmail("");
            SessionEntity.setMemberMobile(memberInfo.getMobile());
            SessionEntity.setAllowBuy(memberInfo.getAllowBuy());
            super.initCart(SessionEntity.getMemberId());
            //-------------------合并浏览历史（待加入队列等后台运行）-----------------
            goodsBrowseService.mergeGoodsBrowse(memberInfo.getMemberId());
            resultEntity.setData(memberInfo);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("注册成功");
        } catch (MemberExistingException e) {
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (ParameterErrorException e) {
            logger.error(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (ShopException e){
            logger.error(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
    /**
     * 验证会员名是否存在
     * @param memberName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "membernameexist", method = RequestMethod.GET)
    public boolean memberNameExist(String memberName){
        if (memberDao.memberNameIsExist(memberName) == false) {
            return true;
        }else{
            return false;
        }
    }
    /**
     * 验证会员邮箱是否存在
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "emailexist", method = RequestMethod.GET)
    public boolean emailExist(String email) {
        if (memberDao.emailIsExist(email) == false) {
            return true;
        }else{
            return false;
        }
    }
    /**
     * 验证会员手机是否存在
     * @param mobile
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "mobileexist", method = RequestMethod.GET)
    public boolean mobileExist(String mobile) throws Exception {
        if (memberDao.mobileIsExist(mobile) == false) {
            return true;
        }else{
            return false;
        }
    }
}