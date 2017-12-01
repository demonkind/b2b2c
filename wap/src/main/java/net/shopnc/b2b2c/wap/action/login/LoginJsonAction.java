package net.shopnc.b2b2c.wap.action.login;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.constant.ClientType;
import net.shopnc.b2b2c.constant.SmsCodeSendType;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.MemberToken;
import net.shopnc.b2b2c.domain.member.SmsCode;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.GoodsBrowseService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.member.SmsCodeService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.CookieHelper;

/**
 * Created by zxy on 2016-03-14.
 */
@Controller
@RequestMapping("/login")
public class LoginJsonAction extends LoginBaseJsonAction {

    @Autowired
    private MemberService memberService;
    @Autowired
    private SmsCodeService smsCodeService;
    @Autowired
    private GoodsBrowseService goodsBrowseService;

    /**
     * 普通登录
     * @param loginName
     * @param memberPwd
     * @param captcha
     * @param autoLogin
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/common", method = RequestMethod.POST)
    public ResultEntity save(HttpServletRequest request) throws Exception{
    	String loginName = request.getParameter("username");//用户账号
    	String memberPwd = request.getParameter("password");//用户密码
    	String captcha = request.getParameter("client");//验证码
    	int autoLogin = Integer.parseInt(request.getParameter("autoLogin"));//
        ResultEntity resultEntity = new ResultEntity();
        //验证码验证
        /*if (new CaptchaHelper().checkCaptcha(captcha) == false) {
            logger.info("验证码错误");
            resultEntity.setMessage("验证码错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }*/
        try{
            //会员登录
            Member member = memberService.login(loginName, memberPwd);
            //保存session
            SessionEntity.setIsLogin(true);
            SessionEntity.setMemberId(member.getMemberId());
            SessionEntity.setMemberName(member.getMemberName());
            SessionEntity.setMemberEmail(member.getEmail());
            SessionEntity.setMemberMobile(member.getMobile());
            SessionEntity.setAllowBuy(member.getAllowBuy());
            //自动登录
            if (autoLogin == 1) {
                MemberToken memberToken = new MemberToken();
                memberToken.setMemberId(member.getMemberId());
                memberToken.setClientType(ClientType.WEB);
                String token = memberService.updateMemberToken(memberToken);
                CookieHelper.setCookie("autologin", token, 86400 * 7);
            }
            super.initCart(SessionEntity.getMemberId());
            //-------------------合并浏览历史（待加入队列等后台运行）-----------------
            goodsBrowseService.mergeGoodsBrowse(member.getMemberId());
            Map<String , Object> data = new HashMap<String, Object>();
            data.put("key", member.getMemberId());//TODO
            data.put("avatar", member.getAvatar());//头像
            data.put("avatarUrl", member.getAvatarUrl());//头像路径
            data.put("userid", member.getMemberId());//主键ID
            data.put("username", member.getMemberName());//账号
            data.put("level_name", member.getSecurityLevel());//等级
            resultEntity.setData(data);
            resultEntity.setMessage("登录成功");
            resultEntity.setCode(ResultEntity.SUCCESS);
        }catch (ShopException e){
            logger.info(e.getMessage());
            //抛出异常
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }
    /**
     * 手机登录
     * @param mobile
     * @param authCode
     * @return
     * @throws ShopException
     */
    @ResponseBody
    @RequestMapping(value = "mobile", method = RequestMethod.POST)
    public ResultEntity saveMobile(@RequestParam("mobile") String mobile,
                                   @RequestParam("authCode") String authCode) throws ShopException{
        ResultEntity resultEntity = new ResultEntity();
        //注册用户
        try {
            Member memberInfo = memberService.loginMobile(mobile, authCode);
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
            resultEntity.setMessage("登录成功");
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            //抛出异常
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 发送手机动态码
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sendsmscode", method = RequestMethod.POST)
    public ResultEntity loginSendSmsCode(@RequestParam("mobile") String mobile) {
        ResultEntity resultEntity = new ResultEntity();
        //发送动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setMobilePhone(mobile);
        smsCode.setSendType(SmsCodeSendType.LOGIN);
        try{
            Serializable logId = smsCodeService.send(smsCode);
            //返回值
            HashMap<String, Object> returnMap = new HashMap<String, Object>();
            //查询该记录详情-----方便测试暂时输出code，后期删除----------------
            /*SmsCode smsCodeInfo = smsCodeDao.get(SmsCode.class, logId);
            returnMap.put("authCode", smsCodeInfo.getAuthCode());*/
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
     * 返回登录状态
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "status", method = RequestMethod.GET)
    public ResultEntity isLogin() {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        HashMap<String,Boolean> hashMap = new HashMap<String, Boolean>();
        hashMap.put("status",SessionEntity.getIsLogin());
        resultEntity.setData(hashMap);
        return resultEntity;
    }
}