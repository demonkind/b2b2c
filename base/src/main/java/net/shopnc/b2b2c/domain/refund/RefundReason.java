package net.shopnc.b2b2c.domain.refund;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 退款原因实体
 * Created by cj on 2016/2/1.
 */

@Entity
@Table(name = "refund_reason")
public class RefundReason implements Serializable {
    /**
     * 退款原因id
     */
    @Id
    @GeneratedValue
    @Column(name = "reason_id")
    private int reasonId;

    /**
     * 原因内容
     */
    @Size(min = 1, max = 100, message = "原因长度为1-100个字")
    @Column(name = "reason_info")
    private String reasonInfo;

    /**
     * 属性排序
     */
    @Min(0)
    @Max(255)
    @Column(name = "reason_sort")
    private int reasonSort;

    /**
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "update_time")
    private Timestamp updateTime;

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonInfo() {
        return reasonInfo;
    }

    public void setReasonInfo(String reasonInfo) {
        this.reasonInfo = reasonInfo;
    }

    public int getReasonSort() {
        return reasonSort;
    }

    public void setReasonSort(int reasonSort) {
        this.reasonSort = reasonSort;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AdminLog{" +
                "reasonId=" + reasonId +
                ", reasonInfo='" + reasonInfo + '\'' +
                ", reasonSort=" + reasonSort +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
