package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 经验值日志<br>
 * Created by zxy on 2015-10-23
 */
@Entity
@Table(name = "exp_points_log")
public class ExpPointsLog implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="log_id")
    private int logId;
    /**
     * 会员编码
     */
    @Column(name="member_id")
    private int memberId = 0;
    /**
     * 经验值负数表示扣除，正数表示增加
     */
    @Column(name="points")
    private int points = 0;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 操作描述
     */
    @Column(name="description", length = 500)
    private String description = "";
    /**
     * 操作阶段
     */
    @Column(name="operation_stage")
    private String operationStage = "";
    /**
     * 操作阶段文本
     */
    @Transient
    private String operationStageText = "";

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
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

    public String getOperationStageText() {
        if (operationStage.equals("login")) {
            operationStageText = "会员登录";
        }else if(operationStage.equals("comments")){
            operationStageText = "商品评论";
        }else if(operationStage.equals("orders")){
            operationStageText = "订单消费";
        }else if(operationStage.equals("register")){
            operationStageText = "会员注册";
        }
        return operationStageText;
    }
}
