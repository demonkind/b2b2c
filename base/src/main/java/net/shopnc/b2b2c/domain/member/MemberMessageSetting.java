package net.shopnc.b2b2c.domain.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by shopnc.feng on 2016-02-18.
 */
@Entity
@Table(name = "member_message_setting")
public class MemberMessageSetting implements Serializable {
    /**
     * 会员编号
     */
    @Id
    @Column(name = "member_id")
    private int memberId;
    /**
     * 消息模板编号
     */
    @Id
    @Column(name = "tpl_code")
    private String tplCode;
    /**
     * 是否接收
     */
    @Column(name = "is_receive")
    private int isReceive;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    @Override
    public String toString() {
        return "MemberMessageSetting{" +
                "memberId=" + memberId +
                ", tplCode='" + tplCode + '\'' +
                ", isReceive=" + isReceive +
                '}';
    }
}
