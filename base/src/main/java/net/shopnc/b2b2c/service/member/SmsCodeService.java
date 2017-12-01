package net.shopnc.b2b2c.service.member;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.CustomSystemTime;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.SmsCodeSendType;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.SmsCodeDao;
import net.shopnc.b2b2c.domain.member.SmsCode;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by zxy on 2015-11-03.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SmsCodeService extends BaseService {

    @Autowired
    private SmsCodeDao smsCodeDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SiteService siteService;
    @Autowired
    private SendMessageService sendMessageService;

    /**
     * 发送动态码
     * @param smsCode
     * @return
     * @throws ShopException
     */
    public Serializable send(SmsCode smsCode) throws ShopException {
        HashMap<String, String> siteConfig = siteService.getSiteInfo();

        //判断发送类型是否合法
        if (new SmsCodeSendType().isExistValue(smsCode.getSendType()) == false) {
            throw new ShopException("参数错误");
        }
        //判断功能是否开启
        if (smsCode.getSendType() == SmsCodeSendType.REGISTER && !siteConfig.get(SiteTitle.SMSREGISTER).equals("1")) {
            throw new ShopException("系统没有开启手机注册功能");
        }
        if (smsCode.getSendType() == SmsCodeSendType.LOGIN && !siteConfig.get(SiteTitle.SMSLOGIN).equals("1")) {
            throw new ShopException("系统没有开启手机登录功能");
        }
        if (smsCode.getSendType() == SmsCodeSendType.FINDPASSWORD && !siteConfig.get(SiteTitle.SMSPASSWORD).equals("1")) {
            throw new ShopException("系统没有开启手机找回密码功能");
        }
        //同一类型同一IP[n]秒内只能发一条短信
        if (this.repeatSendByIp(smsCode) == true) {
            throw new ShopException(Common.SMS_AUTHCODE_RESEND_TIME + "秒内，请勿多次获取动态码！");
        }
        //同一类型同一手机号[n]秒内只能发一条短信
        if (this.repeatSendByMobile(smsCode) == true) {
            throw new ShopException(Common.SMS_AUTHCODE_RESEND_TIME + "秒内，请勿多次获取动态码！");
        }
        //同一手机号24小时内只能发[n]条短信
        if (this.sendOverNumByMobile(smsCode) == true) {
            throw new ShopException("同一手机号24小时内，发送动态码次数不能超过" + Common.SMS_AUTHCODE_SAMEPHONE_MAXNUM + "次！");
        }
        //同一手机号24小时内只能发[n]条短信
        if (this.sendOverNumByIp(smsCode) == true) {
            throw new ShopException("同一IP 24小时内，发送动态码次数不能超过" + Common.SMS_AUTHCODE_SAMEIP_MAXNUM + "次！");
        }
        //注册验证手机号是否存在
    	if ((smsCode.getSendType() == SmsCodeSendType.REGISTER || smsCode.getSendType() == SmsCodeSendType.MOBILEBIND) && memberDao.mobileIsExist(smsCode.getMobilePhone())) {
    		throw new MemberExistingException("当前手机号已被注册，请更换其他号码");
    	}
        //动态码
        String authCode = smsCodeDao.getSmsAuthCode();
        logger.error("手机短信in验证码：" + authCode);
        System.out.println("---------------authCode="+authCode+"-------------------");//-----------------打印动态验证码------------------
        //增加发送记录
        smsCode.setAuthCode(authCode);
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        smsCode.setAddTime(currTime);
        smsCode.setContent("");
        smsCode.setLogIp(ShopHelper.getAddressIP());
        Serializable logId = smsCodeDao.save(smsCode);
        //发送短信
        if (smsCode.getSendType() == SmsCodeSendType.REGISTER) {
            sendMessageService.sendSystem("registerMobile", smsCode.getMobilePhone(), authCode);
        }else if(smsCode.getSendType() == SmsCodeSendType.LOGIN){
            sendMessageService.sendSystem("loginMobile", smsCode.getMobilePhone(), authCode);
        }else if(smsCode.getSendType() == SmsCodeSendType.MOBILEAUTH){
            sendMessageService.sendSystem("authMobile", smsCode.getMobilePhone(), authCode);
        }else if(smsCode.getSendType() == SmsCodeSendType.MOBILEBIND){
            sendMessageService.sendSystem("bindMobile", smsCode.getMobilePhone(), authCode);
        }
        return logId;
    }
    /**
     * 同一IP是否重复发送动态码
     * @param smsCode
     * @return
     */
    public Boolean repeatSendByIp(SmsCode smsCode){
        HashMap where = new HashMap<String,String>();
        where.put("sendType", "sendType = :sendType");
        where.put("logIp", "logIp = :logIp");
        where.put("addTime", "addTime > :addTimeGt");

        HashMap params = new HashMap<String,Object>();
        params.put("sendType", smsCode.getSendType());
        params.put("logIp", smsCode.getLogIp());

        Timestamp currTime = new Timestamp(System.currentTimeMillis() - Common.SMS_AUTHCODE_RESEND_TIME*1000);
        params.put("addTimeGt", currTime);

        Long count = smsCodeDao.findSmsCodeCount(where, params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 同一手机号是否重复发送动态码
     * @param smsCode
     * @return
     */
    public Boolean repeatSendByMobile(SmsCode smsCode){
        HashMap where = new HashMap<String,String>();
        where.put("sendType", "sendType = :sendType");
        where.put("mobilePhone", "mobilePhone = :mobilePhone");
        where.put("addTime", "addTime > :addTimeGt");

        HashMap params = new HashMap<String,Object>();
        params.put("sendType", smsCode.getSendType());
        params.put("mobilePhone", smsCode.getMobilePhone());

        Timestamp currTime = new Timestamp(System.currentTimeMillis() - Common.SMS_AUTHCODE_RESEND_TIME*1000);
        params.put("addTimeGt", currTime);
        Long count = smsCodeDao.findSmsCodeCount(where, params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 同一手机号24小时内只能发[n]条短信
     * @param smsCode
     * @return
     */
    public Boolean sendOverNumByMobile(SmsCode smsCode){
        HashMap where = new HashMap<String,String>();
        where.put("mobilePhone", "mobilePhone = :mobilePhone");
        where.put("addTime", "addTime > :addTimeGt");

        HashMap params = new HashMap<String,Object>();
        params.put("mobilePhone", smsCode.getMobilePhone());
        Timestamp currTime = new Timestamp(System.currentTimeMillis() - CustomSystemTime.DAYSECONDS*1000);
        params.put("addTimeGt", currTime);
        Long count = smsCodeDao.findSmsCodeCount(where, params);
        if (count >= Common.SMS_AUTHCODE_SAMEPHONE_MAXNUM) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 同一IP24小时内只能发[n]条短信
     * @param smsCode
     * @return
     */
    public Boolean sendOverNumByIp(SmsCode smsCode){
        HashMap where = new HashMap<String,String>();
        where.put("logIp", "logIp = :logIp");
        where.put("addTime", "addTime > :addTimeGt");

        HashMap params = new HashMap<String,Object>();
        params.put("logIp", smsCode.getLogIp());
        Timestamp currTime = new Timestamp(System.currentTimeMillis() - CustomSystemTime.DAYSECONDS*1000);
        params.put("addTimeGt", currTime);
        Long count = smsCodeDao.findSmsCodeCount(where, params);
        if (count >= Common.SMS_AUTHCODE_SAMEIP_MAXNUM) {
            return true;
        } else {
            return false;
        }
    }
}