package net.shopnc.b2b2c.vo.consult;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * Created by zxy on 2016-01-12
 */
public class ConsultVo {
    /**
     * 自增编码
     */
    private int consultId;
    /**
     * 商品编号
     */
    private int goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 会员ID
     */
    private int memberId;
    /**
     * 会员名称
     */
    private String memberName;
    /**
     * 店铺ID
     */
    private int storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 分类ID
     */
    private int classId;
    /**
     * 分类名称
     */
    private String className;
    /**
     * 咨询内容
     */
    private String consultContent;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp addTime;
    /**
     * 回复内容
     */
    private String consultReply;
    /**
     * 回复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp replyTime;
    /**
     * 匿名状态
     */
    private int anonymousState;
    /**
     * 会员已读回复
     */
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
