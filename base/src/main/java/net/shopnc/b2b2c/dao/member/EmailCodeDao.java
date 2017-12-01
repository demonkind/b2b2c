package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.domain.member.EmailCode;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by zxy on 2015/12/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class EmailCodeDao extends BaseDaoHibernate4<EmailCode> {
    /**
     * 邮件动态码日志总数
     * @param where 查询条件
     * @param params HQL参数值
     * @return 日志总数
     */
    public Long findEmailCodeCount(HashMap<String,String> where,HashMap<String,Object> params) {
        String hql = "select count(*) from EmailCode where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findCount(hql, params);
    }

    /**
     * 获得邮件动态码日志详情
     * @param where 查询条件
     * @param params HQL参数值
     * @return 邮件动态码日志详情
     */
    public EmailCode getEmailCode(HashMap<String,String> where,HashMap<String,Object> params) {
        String hql = "from EmailCode where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        hql += " order by logId desc";
        List<EmailCode> emailCodeList = super.find(hql, params);
        if (emailCodeList.isEmpty()) {
            return null;
        }
        return emailCodeList.get(0);
    }

    /**
     * 获得邮件动态码值
     * @return 动态码
     */
    public String getEmailAuthCode() {
        Random r = new Random();
        StringBuffer code = new StringBuffer();
        int i;
        for (i=0; i<6; i++) {
            if (i == 0) {
                code. append(r.nextInt(9) + 1);
            } else {
                code. append(r.nextInt(10));
            }
        }
        return code.toString();
    }

    /**
     * 验证动态码
     * @param emailCode 邮件动态码对象
     * @return 验证成功或者失败
     */
    public Boolean checkCode(EmailCode emailCode){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("sendType", "sendType = :sendType");
        where.put("email", "email = :email");
        //where.put("authCode", "authCode = :authCode");
        where.put("addTime", "addTime > :addTimeGt");
        if (emailCode.getMemberId()>0) {
            where.put("memberId", "memberId = :memberId");
        }

        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("sendType", emailCode.getSendType());
        params.put("email", emailCode.getEmail());
        //params.put("authCode", emailCode.getAuthCode());
        Timestamp currTime = new Timestamp(System.currentTimeMillis() - Long.valueOf(Common.EMAIL_AUTHCODE_VALID_TIME*1000));
        params.put("addTimeGt", currTime);
        if (emailCode.getMemberId()>0) {
            params.put("memberId", emailCode.getMemberId());
        }
        EmailCode emailCodeInfo = this.getEmailCode(where, params);
        if (emailCodeInfo == null) {
            return false;
        }
        if (emailCodeInfo.getAuthCode().equals(emailCode.getAuthCode())) {
            return true;
        }else{
            return false;
        }
    }
}
