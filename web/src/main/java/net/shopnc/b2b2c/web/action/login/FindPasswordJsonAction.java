package net.shopnc.b2b2c.web.action.login;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.EmailCodeSendType;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.SmsCodeSendType;
import net.shopnc.b2b2c.dao.member.EmailCodeDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.SmsCodeDao;
import net.shopnc.b2b2c.domain.member.EmailCode;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.SmsCode;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.member.EmailCodeService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.member.SmsCodeService;
import net.shopnc.b2b2c.web.common.util.CaptchaHelper;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */
@Controller
@RequestMapping("findpwd")
public class FindPasswordJsonAction extends LoginBaseJsonAction {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private EmailCodeService emailCodeService;
    @Autowired
    private EmailCodeDao emailCodeDao;
    @Autowired
    private SmsCodeService smsCodeService;
    @Autowired
    private SmsCodeDao smsCodeDao;

    /**
     * 邮箱找回密码第一步
     * @param memberName
     * @param email
     * @param captcha
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "email", method = RequestMethod.POST)
    public ResultEntity findByEmailFirst(@RequestParam("memberName") String memberName,
                                         @RequestParam("email") String email,
                                         @RequestParam("captcha") String captcha) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //验证码验证
        if (new CaptchaHelper().checkCaptcha(captcha) == false) {
            logger.info("验证码错误");
            resultEntity.setMessage("验证码错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        return this.findByEmailSendCode(memberName, email);
    }

    /**
     * 邮箱找回密码发送动态码
     * @param memberName
     * @param email
     * @return
     */
    private ResultEntity findByEmailSendCode(String memberName, String email) {
        ResultEntity resultEntity = new ResultEntity();
        if (memberName==null || memberName.length()<=0 || email==null || email.length()<=0) {
            logger.info("参数错误");
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //查询会员信息
        Member memberInfo = memberDao.getMemberInfoByMemberName(memberName);
        if (memberInfo==null || memberInfo.getEmail()==null || memberInfo.getEmail().length()<=0 || !email.equals(memberInfo.getEmail())) {
            logger.info("会员信息错误");
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //发送动态码
        EmailCode emailCode = new EmailCode();
        emailCode.setMemberId(memberInfo.getMemberId());
        emailCode.setEmail(memberInfo.getEmail());
        emailCode.setSendType(EmailCodeSendType.FINDPASSWORD);
        try{
            Serializable logId = emailCodeService.send(emailCode);
            HashMap<String, Object> returnMap = new HashMap<>();
            //------------------------------方便测试暂时输出code，后期删除----------------
            /*EmailCode emailCodeInfo = emailCodeDao.get(EmailCode.class, logId);
            returnMap.put("authCode", emailCodeInfo.getAuthCode());*/
            //返回值
            returnMap.put("authCodeValidTime", Common.EMAIL_AUTHCODE_VALID_TIME/60);
            returnMap.put("authCodeResendTime", Common.EMAIL_AUTHCODE_RESEND_TIME);
            returnMap.put("memberId", memberInfo.getMemberId());
            resultEntity.setData(returnMap);
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
     * 邮箱找回密码重新发送动态码
     * @param memberName
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "email/second/again", method = RequestMethod.POST)
    public ResultEntity findByEmailSecondAgain(@RequestParam("memberName") String memberName,
                                              @RequestParam("email") String email) {
        return this.findByEmailSendCode(memberName, email);
    }

    /**
     * 邮箱找回密码第二步
     * @param memberId
     * @param email
     * @param memberPwd
     * @param repeatMemberPwd
     * @param authCode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "email/second", method = RequestMethod.POST)
    public ResultEntity findByEmailSecond(@RequestParam("memberId") Integer memberId,
                                         @RequestParam("email") String email,
                                         @RequestParam("memberPwd") String memberPwd,
                                         @RequestParam("repeatMemberPwd") String repeatMemberPwd,
                                         @RequestParam("authCode") String authCode) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //查询会员信息
        Member memberInfo = memberDao.get(Member.class, memberId);
        if (memberInfo==null || memberInfo.getEmail()==null || memberInfo.getEmail().length()<=0 || !email.equals(memberInfo.getEmail())) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证动态码
        EmailCode emailCode = new EmailCode();
        emailCode.setMemberId(memberId);
        emailCode.setSendType(EmailCodeSendType.FINDPASSWORD);
        emailCode.setEmail(email);
        emailCode.setAuthCode(authCode);
        if (emailCodeDao.checkCode(emailCode).equals(false)) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("动态码错误或已过期，请重新输入");
            return resultEntity;
        }
        try {
            //修改密码
            memberService.modifyMemberPwd(memberPwd, repeatMemberPwd, memberId);
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
     * 手机找回密码第一步
     * @param mobile
     * @param captcha
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "mobile", method = RequestMethod.POST)
    public ResultEntity findByMobileFirst(@RequestParam("mobile") String mobile,
                                          @RequestParam("captcha") String captcha) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //验证码验证
        if (new CaptchaHelper().checkCaptcha(captcha) == false) {
            logger.info("验证码错误");
            resultEntity.setMessage("验证码错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        return this.findByMobileSendCode(mobile);
    }

    /**
     * 手机找回密码发送动态码
     * @param mobile
     * @return
     */
    private ResultEntity findByMobileSendCode(String mobile) {
        ResultEntity resultEntity = new ResultEntity();
        //系统未开启手机注册功能
        if (!siteService.getSiteInfo().get(SiteTitle.SMSPASSWORD).equals("1")) {
            logger.info("系统未开启手机找回密码功能");
            resultEntity.setMessage("系统未开启手机找回密码功能");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        if (mobile==null || mobile.length()<=0) {
            logger.info("参数错误");
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //查询会员信息
        Member memberInfo = memberDao.getMemberInfoByMobile(mobile);
        if (memberInfo==null) {
            logger.info("会员信息错误");
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //发送动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setMemberId(memberInfo.getMemberId());
        smsCode.setMobilePhone(memberInfo.getMobile());
        smsCode.setSendType(SmsCodeSendType.FINDPASSWORD);
        try{
            Serializable logId = smsCodeService.send(smsCode);
            HashMap<String, Object> returnMap = new HashMap<>();
            //返回值
            returnMap.put("authCodeValidTime", Common.SMS_AUTHCODE_VALID_TIME/60);
            returnMap.put("authCodeResendTime", Common.SMS_AUTHCODE_RESEND_TIME);
            returnMap.put("memberId", memberInfo.getMemberId());
            resultEntity.setData(returnMap);
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
     * 手机找回密码重新发送动态码
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mobile/second/again", method = RequestMethod.POST)
    public ResultEntity findByMobileSecondAgain(@RequestParam("mobile") String mobile) {
        return this.findByMobileSendCode(mobile);
    }

    /**
     * 手机找回密码第二步
     * @param memberId
     * @param mobile
     * @param memberPwd
     * @param repeatMemberPwd
     * @param authCode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "mobile/second", method = RequestMethod.POST)
    public ResultEntity findByMobileSecond(@RequestParam("memberId") Integer memberId,
                                           @RequestParam("mobile") String mobile,
                                           @RequestParam("memberPwd") String memberPwd,
                                           @RequestParam("repeatMemberPwd") String repeatMemberPwd,
                                           @RequestParam("authCode") String authCode) throws Exception{
        ResultEntity resultEntity = new ResultEntity();
        //查询会员信息
        Member memberInfo = memberDao.get(Member.class, memberId);
        if (memberInfo==null || memberInfo.getMobile()==null || memberInfo.getMobile().length()<=0 || !mobile.equals(memberInfo.getMobile())) {
            resultEntity.setMessage("会员信息错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setMemberId(memberId);
        smsCode.setSendType(SmsCodeSendType.FINDPASSWORD);
        smsCode.setMobilePhone(mobile);
        smsCode.setAuthCode(authCode);
        if (smsCodeDao.checkCode(smsCode).equals(false)) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("动态码错误或已过期，请重新输入");
            return resultEntity;
        }
        try {
            //修改密码
            memberService.modifyMemberPwd(memberPwd, repeatMemberPwd, memberId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            return resultEntity;
        }catch (ShopException e){
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
}