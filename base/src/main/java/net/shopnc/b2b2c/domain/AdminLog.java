package net.shopnc.b2b2c.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 管理员操作日志实体
 * Created by shopnc on 2015/11/2.
 */
@Entity
@Table(name = "admin_log")
public class AdminLog implements Serializable {
    /**
     * 主键、自增
     *
     */
    @Id
    @GeneratedValue
    @Column(name = "log_id")
    private int logId;

    /**
     * 日志内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "create_time")
    private Timestamp createTime;
    /**
     * 操作人
     */
    @Column(name = "admin_name")
    private String adminName;

    /**
     * 管理员ID
     */
    @Column(name = "admin_id")
    private int adminId;
    /**
     * 操作人IP
     */
    @Column(name = "ip")
    private String ip;
    /**
     * 操作action
     */
    @Column(name = "action")
    private String action;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "AdminLog{" +
                "logId=" + logId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", adminName='" + adminName + '\'' +
                ", adminId=" + adminId +
                ", ip='" + ip + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
