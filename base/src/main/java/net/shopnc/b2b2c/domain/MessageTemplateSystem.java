package net.shopnc.b2b2c.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 系统默认消息模板实体
 * Created by zxy on 2016/01/07.
 */
@Entity
@Table(name = "message_template_system")
public class MessageTemplateSystem {
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
     * 标题
     */
    @Size(max = 500, message = "模板标题应小于500个字")
    @Column(name = "tpl_title", length = 500)
    private String tplTitle = "";
    /**
     * 内容
     */
    @Size(max = 10000, message = "模板内容应小于10000个字")
    @Column(name = "tpl_content", length = 10000)
    private String tplContent = "";
    /**
     * 模板类型<br>
     *     1 短信，2 邮件
     */
    @Column(name = "send_type")
    private int sendType;

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public String getTplTitle() {
        return tplTitle;
    }

    public void setTplTitle(String tplTitle) {
        this.tplTitle = tplTitle;
    }

    public String getTplContent() {
        return tplContent;
    }

    public void setTplContent(String tplContent) {
        this.tplContent = tplContent;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    @Override
        public String toString() {
        return "MessageTemplateSystem{" +
                "tplCode='" + tplCode + '\'' +
                ", tplName='" + tplName + '\'' +
                ", tplTitle='" + tplTitle + '\'' +
                ", tplContent='" + tplContent + '\'' +
                ", sendType='" + sendType + '\'' +
                '}';
    }
}
