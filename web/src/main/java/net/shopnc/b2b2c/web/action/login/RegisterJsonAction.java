package net.shopnc.b2b2c.web.action.login;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.SmsCodeSendType;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.SmsCode;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.member.GoodsBrowseService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.member.SmsCodeService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.b2b2c.web.common.util.CaptchaHelper;
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
                       @RequestParam("captcha") String captcha,
                       @RequestParam("agreeClause") int agreeClause) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //数据验证
        if (!repeatMemberPwd.equals(member.getMemberPwd())) {
            logger.info("两次输入的密码不一致");
            resultEntity.setMessage("两次输入的密码不一致");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //注册协议
        if (agreeClause != 1) {
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
    public ResultEntity saveMobileFirst(Member member,
                                   @RequestParam("captcha") String captcha,
                                   @RequestParam("agreeClause") int agreeClause) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //注册协议
        if (agreeClause != 1) {
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
        return this.mobileRegisterSendCode(member);
    }
    /**
     * 手机注册发送动态码
     * @param member
     * @return
     */
    private ResultEntity mobileRegisterSendCode(Member member){
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
        smsCode.setMobilePhone(member.getMobile());
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
    public ResultEntity mobileSecondAgain(Member member) {
        return this.mobileRegisterSendCode(member);
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
    public ResultEntity saveMobileSecond(Member member,
                       @RequestParam("repeatMemberPwd") String repeatMemberPwd,
                       @RequestParam("authCode") String authCode) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //数据验证
        if (!repeatMemberPwd.equals(member.getMemberPwd())) {
            logger.info("两次输入的密码不一致");
            resultEntity.setMessage("两次输入的密码不一致");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //注册用户
        try {
            Member memberInfo = memberService.registerMobile(member, authCode);

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