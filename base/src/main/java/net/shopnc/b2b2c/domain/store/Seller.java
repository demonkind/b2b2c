package net.shopnc.b2b2c.domain.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import java.sql.Timestamp;

/**
 * 商家实体
 * Created by dqw on 2015/11/20.
 */
@Entity
@Table(name = "seller")
public class Seller {
    /**
     * 卖家编号
     */
    @Id
    @GeneratedValue
    @Column(name = "seller_id")
    private int sellerId;
    /**
     * 卖家用户名
     */
    @Size(min = 3, max = 20, message = "用户名长度6-20个字")
    @Column(name = "seller_name", unique = true)
    private String sellerName;
    /**
     * 卖家密码
     */
    @JsonIgnore
    @Column(name = "seller_password")
    private String sellerPassword;
    /**
     * 卖家邮箱
     */
    @Email
    @Column(name = "seller_email", unique = true)
    private String sellerEmail;
    /**
     * 卖家手机
     */
    @Column(name = "seller_mobile")
    private String sellerMobile;
    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private int storeId = 0;
    /**
     * 店铺名称
     */
    @Column(name = "store_name")
    private String storeName = "";
    /**
     * 是否是管理员</br>
     * 0-不是 1-是
     */
    @Column(name = "is_store_owner")
    private int isStoreOwner = 0;
    /**
     * 卖家用户组ID
     */
    @JsonIgnore
    @Column(name = "group_id")
    private int groupId = 0;
    /**
     * 卖家用户组ID
     */
    @JsonIgnore
    @Column(name = "group_name")
    private String groupName;
    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "join_date")
    private Timestamp joinDate;
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Column(name = "last_login_time")
    private Timestamp lastLoginTime;
    /**
     * 是否可以登录 0-禁止登陆 1-可以登录
     */
    @Column(name = "allow_login")
    private int allowLogin = 1;

    /**
     * 商家头像
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * 商家头像URL
     */
    @Transient
    @Column(name = "avatar_url")
    private String avatarUrl;
    /**
     * 是否接收
     */
    @Transient
    private int isReceive = 0;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPassword() {
        return sellerPassword;
    }

    public void setSellerPassword(String sellerPassword) {
        this.sellerPassword = sellerPassword;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
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

    public int getIsStoreOwner() {
        return isStoreOwner;
    }

    public void setIsStoreOwner(int isStoreOwner) {
        this.isStoreOwner = isStoreOwner;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(int allowLogin) {
        this.allowLogin = allowLogin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        if (avatar == null || avatar.equals("")) {
            avatarUrl = ShopConfig.getPublicRoot() + Common.DEFAULT_AVATAR_URL;
        }else{
            avatarUrl = ShopConfig.getUploadRoot() + avatar;
        }
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", sellerPassword='" + sellerPassword + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", sellerMobile='" + sellerMobile + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", isStoreOwner=" + isStoreOwner +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", joinDate=" + joinDate +
                ", lastLoginTime=" + lastLoginTime +
                ", allowLogin=" + allowLogin +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", isReceive=" + isReceive +
                '}';
    }
}


