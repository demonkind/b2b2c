package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 短信验证码发送记录<br>
 * Created by zxy on 2015-10-23
 */
@Entity
@Table(name = "sms_code_log")
public class SmsCode implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="log_id")
    private int logId;
    /**
     * 接收手机号
     */
    @Column(name="mobile_phone", length = 11)
    private String mobilePhone = "";
    /**
     * 短信动态码
     */
    @Column(name="auth_code", length = 50)
    private String authCode = "";
    /**
     * 请求IP
     */
    @Column(name="log_ip", length = 50)
    private String logIp = "";
    /**
     * 短信内容
     */
    @Column(name="content", length = 200)
    private String content = "";
    /**
     * 短信类型
     */
    @Column(name="send_type")
    private int sendType = 0;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 会员ID
     */
    @Column(name="member_id")
    private int memberId = 0;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getLogIp() {
        return logIp;
    }

    public void setLogIp(String logIp) {
        this.logIp = logIp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}
