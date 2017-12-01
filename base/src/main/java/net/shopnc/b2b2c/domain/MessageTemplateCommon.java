package net.shopnc.b2b2c.domain;

import net.shopnc.b2b2c.constant.State;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 普通消息模板实体
 * Created by zxy on 2016/01/07.
 */
@Entity
@Table(name = "message_template_common")
public class MessageTemplateCommon {
    /**
     * 消息模板编码、主键
     */
    @Id
    @Column(name = "tpl_code")
    private String tplCode;
    /**
     * 消息模板名称
     */
    @Column(name = "tpl_name")
    private String tplName = "";
    /**
     * 消息模板类型 1-用户消息模板 2-商家消息模板（MessageTemplateCommonTplType）
     */
    @Column(name = "tpl_type")
    private Integer tplType;
     /**
     * 站内信内容
     */
    @Size(max = 5000, message = "站内信内容应小于5000个字")
    @Lob
    @Column(name = "notice_content")
    private String noticeContent = "";
    /**
     * 短信开关 0-关闭 1-开启
     */
    @Column(name = "sms_state")
    private Integer smsState;
    /**
     * 短信内容
     */
    @Size(max = 1000, message = "短信内容应小于1000个字")
    @Column(name = "sms_content", length = 1000)
    private String smsContent = "";
    /**
     * 邮件开关  0-关闭 1-开启
     */
    @Column(name = "email_state")
    private Integer emailState;
    /**
     * 邮件标题
     */
    @Size(max = 500, message = "邮件标题应小于500个字")
    @Column(name = "email_title", length = 500)
    private String emailTitle = "";
    /**
     * 邮件内容
     */
    @Size(max = 10000, message = "邮件内容应小于10000个字")
    @Lob
    @Column(name = "email_content")
    private String emailContent = "";
    /**
     * 消息模板分类<br/>
     * 会员    交易-1001 退换货-1002 物流-1003 资产-1004<br/>
     * 商家    交易-2001 退换货-2002 商品-2003 运营-2004
     */
    @Column(name = "tpl_class")
    private Integer tplClass;
    /**
     * 已开启的消息
     */
    @Transient
    private String opened = "";
    /**
     * 是否接收
     */
    @Transient
    private int isReceive = 0;

    /**
     * 消息分类名称
     */
    @Transient
    private String tplClassName;

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public Integer getTplType() {
        return tplType;
    }

    public void setTplType(Integer tplType) {
        this.tplType = tplType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Integer getSmsState() {
        return smsState;
    }

    public void setSmsState(Integer smsState) {
        this.smsState = smsState;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Integer getEmailState() {
        return emailState;
    }

    public void setEmailState(Integer emailState) {
        this.emailState = emailState;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public Integer getTplClass() {
        return tplClass;
    }

    public void setTplClass(Integer tplClass) {
        this.tplClass = tplClass;
    }

    public String getOpened() {
        String string = "站内信";
        if (smsState == State.YES) {
            string += "/短信";
        }
        if (emailState == State.YES) {
            string += "/邮件";
        }
        return string;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    public String getTplClassName() {
        return tplClassName;
    }

    public void setTplClassName(String tplClassName) {
        this.tplClassName = tplClassName;
    }

    @Override
    public String toString() {
        return "MessageTemplateCommon{" +
                "tplCode='" + tplCode + '\'' +
                ", tplName='" + tplName + '\'' +
                ", tplType=" + tplType +
                ", noticeContent='" + noticeContent + '\'' +
                ", smsState=" + smsState +
                ", smsContent='" + smsContent + '\'' +
                ", emailState=" + emailState +
                ", emailTitle='" + emailTitle + '\'' +
                ", emailContent='" + emailContent + '\'' +
                ", tplClass=" + tplClass +
                ", opened='" + opened + '\'' +
                ", isReceive=" + isReceive +
                ", tplClassName='" + tplClassName + '\'' +
                '}';
    }
}
