package net.shopnc.b2b2c.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * Created by zxy on 2015/12/01.
 */
public class PointsLogMemberVo {
    /**
     * 自增编码
     */
    private int logId;
    /**
     * 会员编码
     */
    private int memberId;
    /**
     * 积分负数表示扣除，正数表示增加
     */
    private int points;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp addTime;
    /**
     * 操作描述
     */
    private String description;
    /**
     * 操作阶段
     */
    private String operationStage;
    /**
     * 会员名称
     */
    private String memberName;
    /**
     * 管理员编号
     */
    private int adminId;
    /**
     * 管理员名称
     */
    private String adminName;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationStage() {
        return operationStage;
    }

    public void setOperationStage(String operationStage) {
        this.operationStage = operationStage;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
