package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品咨询<br>
 * Created by zxy on 2016-01-12
 */
@Entity
@Table(name = "consult")
public class Consult implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="consult_id")
    private int consultId;
    /**
     * 商品编号
     */
    @Min(value=1, message = "商品信息错误")
    @Column(name="goods_id")
    private int goodsId = 0;
    /**
     * 会员ID
     */
    @Column(name="member_id")
    private int memberId = 0;
    /**
     * 店铺ID
     */
    @Column(name="store_id")
    private int storeId = 0;
    /**
     * 分类ID
     */
    @Min(value=1, message = "咨询类型错误")
    @Column(name="class_id")
    private int classId = 0;
    /**
     * 咨询内容
     */
    @NotEmpty
    @Size(max = 500, message = "咨询内容长度应小于500个字符")
    @Column(name="consult_content", length = 500)
    private String consultContent = "";
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 回复内容
     */
    @Size(max = 500, message = "回复内容长度应小于500个字符")
    @Column(name="consult_reply", length = 500)
    private String consultReply = "";
    /**
     * 回复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="reply_time")
    private Timestamp replyTime;
    /**
     * 匿名状态
     */
    @Column(name="anonymous_state")
    private int anonymousState = 0;
    /**
     * 会员已读回复
     * 0未读 1已读
     */
    @Column(name="read_state")
    private int readState = 0;

    public int getConsultId() {
        return consultId;
    }

    public void setConsultId(int consultId) {
        this.consultId = consultId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getConsultContent() {
        return consultContent;
    }

    public void setConsultContent(String consultContent) {
        this.consultContent = consultContent;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getConsultReply() {
        return consultReply;
    }

    public void setConsultReply(String consultReply) {
        this.consultReply = consultReply;
    }

    public Timestamp getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
    }

    public int getAnonymousState() {
        return anonymousState;
    }

    public void setAnonymousState(int anonymousState) {
        this.anonymousState = anonymousState;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }
}
