package net.shopnc.b2b2c.service.member;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.CustomSystemTime;
import net.shopnc.b2b2c.constant.EmailCodeSendType;
import net.shopnc.b2b2c.dao.member.EmailCodeDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.member.EmailCode;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by zxy on 2015/12/22.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class EmailCodeService extends BaseService {

    @Autowired
    private EmailCodeDao emailCodeDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SendMessageService sendMessageService;

    /**
     * 发送邮件动态码
     * @param emailCode
     * @return
     * @throws ShopException
     */
    public Serializable send(EmailCode emailCode) throws ShopException {
        //判断发送类型是否合法
        if (new EmailCodeSendType().isExistValue(emailCode.getSendType()) == false) {
            throw new ShopException("参数错误");
        }
        //同一类型同一IP[n]秒内只能发一封邮件
        if (this.repeatSendByIp(emailCode) == true) {
            throw new ShopException("同一IP地址" + Common.EMAIL_AUTHCODE_RESEND_TIME + "秒内，请勿多次获取动态码！");
        }
        //同一类型同一邮箱[n]秒内只能发一封邮件
        if (this.repeatSendByEmail(emailCode) == true) {
            throw new ShopException("同一邮箱" + Common.EMAIL_AUTHCODE_RESEND_TIME + "秒内，请勿多次获取动态码！");
        }
        //同一邮箱24小时内只能发[n]封邮件
        if (this.sendOverNumByEmail(emailCode) == true) {
            throw new ShopException("同一邮箱24小时内，发送动态码次数不能超过" + Common.EMAIL_AUTHCODE_SAMEPHONE_MAXNUM + "次！");
        }
        //同一IP24小时内只能发[n]封邮件
        if (this.sendOverNumByIp(emailCode) == true) {
            throw new ShopException("同一IP 24小时内，发送动态码次数不能超过" + Common.EMAIL_AUTHCODE_SAMEIP_MAXNUM + "次！");
        }
        //验证邮箱是否存在
        if (emailCode.getSendType() == EmailCodeSendType.EMAILBIND && memberDao.emailIsExist(emailCode.getEmail())) {
            throw new MemberExistingException("当前邮箱已被占用，请更换其他邮箱");
        }
        //动态码
        String authCode = emailCodeDao.getEmailAuthCode();
        //增加发送记录
        emailCode.setAuthCode(authCode);
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        emailCode.setAddTime(currTime);
        emailCode.setContent("");
        emailCode.setLogIp(ShopHelper.getAddressIP());
        Serializable logId = emailCodeDao.save(emailCode);
        //发送邮件
        if(emailCode.getSendType() == EmailCodeSendType.EMAILAUTH){
            sendMessageService.sendSystem("authEmail", emailCode.getEmail(), authCode);
        }else if(emailCode.getSendType() == EmailCodeSendType.EMAILBIND){
            sendMessageService.sendSystem("bindEmail", emailCode.getEmail(), authCode);
        }
        return logId;
    }
    /**
     * 同一IP是否重复发送动态码
     * @param emailCode
     * @return
     */
    public Boolean repeatSendByIp(EmailCode emailCode){
        HashMap where = new HashMap<String,String>();
        where.put("sendType", "sendType = :sendType");
        where.put("logIp", "logIp = :logIp");
        where.put("addTime", "addTime > :addTimeGt");

        HashMap params = new HashMap<String,Object>();
        params.put("sendType", emailCode.getSendType());
        params.put("logIp", emailCode.getLogIp());
        Timestamp currTime = new Timestamp(System.currentTimeMillis() - Common.EMAIL_AUTHCODE_RESEND_TIME*1000);
        params.put("addTimeGt", currTime);
        Long count = emailCodeDao.findEmailCodeCount(where, params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 同一邮箱是否重复发送动态码
     * @param emailCode
     * @return
     */
    public Boolean repeatSendByEmail(EmailCode emailCode){
        HashMap where = new HashMap<String,String>();
        where.put("sendType", "sendType = :sendType");
        where.put("email", "email = :email");
        where.put("addTime", "addTime > :addTimeGt");

        HashMap params = new HashMap<String,Object>();
        params.put("sendType", emailCode.getSendType());
        params.put("email", emailCode.getEmail());
        Timestamp currTime = new Timestamp(System.currentTimeMillis() - Common.EMAIL_AUTHCODE_RESEND_TIME*1000);
        params.put("addTimeGt", currTime);
        Long count = emailCodeDao.findEmailCodeCount(where, params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 同一邮箱24小时内只能发[n]封邮件
     * @param emailCode
     * @return
     */
    public Boolean sendOverNumByEmail(EmailCode emailCode){
        HashMap where = new HashMap<String,String>();
        where.put("email", "email = :email");
        where.put("addTime", "addTime > :addTimeGt");

        HashMap params = new HashMap<String,Object>();
        params.put("email", emailCode.getEmail());
        Timestamp currTime = new Timestamp(System.currentTimeMillis() - CustomSystemTime.DAYSECONDS*1000);
        params.put("addTimeGt", currTime);
        Long count = emailCodeDao.findEmailCodeCount(where, params);
        if (count >= Common.EMAIL_AUTHCODE_SAMEPHONE_MAXNUM) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 同一IP24小时内只能发[n]封邮件
     * @param emailCode
     * @return
     */
    public Boolean sendOverNumByIp(EmailCode emailCode){
        HashMap where = new HashMap<String,String>();
        where.put("logIp", "logIp = :logIp");
        where.put("addTime", "addTime > :addTimeGt");

        HashMap params = new HashMap<String,Object>();
        params.put("logIp", emailCode.getLogIp());
        Timestamp currTime = new Timestamp(System.currentTimeMillis() - CustomSystemTime.DAYSECONDS*1000);
        params.put("addTimeGt", currTime);
        Long count = emailCodeDao.findEmailCodeCount(where, params);
        if (count >= Common.EMAIL_AUTHCODE_SAMEIP_MAXNUM) {
            return true;
        } else {
            return false;
        }
    }
}