package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 会员令牌实体<br>
 * Created by zxy on 2015-12-09.
 */
@Entity
@Table(name = "member_token")
public class MemberToken implements Serializable {
    /**
     * 令牌自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="token_id")
    private int tokenId;
    /**
     * 会员编码
     */
    @Column(name="member_id")
    private int memberId = 0;
    /**
     * 登录令牌
     */
    @Column(name="token",length = 100,unique = true)
    private String token = "";
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="add_time")
    private Timestamp addTime;
    /**
     * 客户端类型
     */
    @Column(name="client_type",length = 50)
    private String clientType = "";

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
