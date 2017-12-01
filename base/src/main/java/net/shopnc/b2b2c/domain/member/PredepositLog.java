package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 预存款日志<br>
 * Created by zxy on 2015-12-24
 */
@Entity
@Table(name = "predeposit_log")
public class PredepositLog implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="log_id")
    private int logId;
    /**
     * 会员编号
     */
    @NotNull
    @Column(name="member_id")
    private int memberId = 0;
    /**
     * 管理员编号
     */
    @Column(name="admin_id")
    private int adminId = 0;
    /**
     * 管理员名称
     */
    @Column(name="admin_name")
    private String adminName = "";
    /**
     * 操作阶段（PredepositLogOperationStage状态）
     */
    @NotNull
    @Column(name="operation_stage", length = 50)
    private String operationStage = "";
    /**
     * 可用金额变更
     */
    @Column(name="available_amount")
    private BigDecimal availableAmount = new BigDecimal(0);
    /**
     * 冻结金额变更
     */
    @Column(name="freeze_amount")
    private BigDecimal freezeAmount = new BigDecimal(0);
    /**
     * 添加时间
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 描述
     */
    @Column(name="description")
    private String description = "";

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

    public String getOperationStage() {
        return operationStage;
    }

    public void setOperationStage(String operationStage) {
        this.operationStage = operationStage;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
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
}
