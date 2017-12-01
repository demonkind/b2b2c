package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.domain.member.SmsCode;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by zxy on 2015/11/10.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class SmsCodeDao extends BaseDaoHibernate4<SmsCode> {

    /**
     * 短信动态码日志总数
     * @param where 查询条件
     * @param params HQL参数值
     * @return 短信动态码日志总数
     */
    public Long findSmsCodeCount(HashMap<String,String> where,HashMap<String,Object> params) {
        String hql = "select count(*) from SmsCode where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findCount(hql, params);
    }

    /**
     * 获得短信动态码日志详情
     * @param where 查询条件
     * @param params HQL参数值
     * @return 短信动态码日志实体
     */
    public SmsCode getSmsCode(HashMap<String,String> where,HashMap<String,Object> params) {
        String hql = "from SmsCode where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        hql += " order by logId desc";
        List<SmsCode> smsCodeList = super.find(hql, params);
        if (smsCodeList.isEmpty()) {
            return null;
        }
        return smsCodeList.get(0);
    }

    /**
     * 获得短信动态码
     * @return 短信动态码
     */
    public String getSmsAuthCode() {
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
     * 验证短信动态码
     * @param smsCode 短信动态码日志实体
     * @return 验证成功或者失败
     */
    public Boolean checkCode(SmsCode smsCode){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("sendType", "sendType = :sendType");
        where.put("mobilePhone", "mobilePhone = :mobilePhone");
        //where.put("authCode", "authCode = :authCode");
        where.put("addTime", "addTime > :addTimeGt");
        if (smsCode.getMemberId()>0) {
            where.put("memberId", "memberId = :memberId");
        }

        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("sendType", smsCode.getSendType());
        params.put("mobilePhone", smsCode.getMobilePhone());
        //params.put("authCode", smsCode.getAuthCode());
        Timestamp currTime = new Timestamp(System.currentTimeMillis() - Long.valueOf(Common.SMS_AUTHCODE_VALID_TIME*1000));
        params.put("addTimeGt", currTime);
        if (smsCode.getMemberId()>0) {
            params.put("memberId", smsCode.getMemberId());
        }
        SmsCode smsCodeInfo = this.getSmsCode(where, params);
        if (smsCodeInfo == null) {
            return false;
        }
        if (smsCodeInfo.getAuthCode().equals(smsCode.getAuthCode())) {
            return true;
        }else{
            return false;
        }
    }
}
