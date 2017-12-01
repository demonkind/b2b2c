package net.shopnc.b2b2c.domain.admin;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 后台管理员实体
 * Created by dqw on 2015/12/29.
 */
@Entity
@Table(name = "admin")
public class Admin {
    /**
     * 编号
     */
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    private int adminId;
    /**
     * 用户名
     */
    @Size(min = 3, max = 20, message = "用户名长度6-20个字")
    @Column(name = "name", unique = true)
    private String name;
    /**
     * 密码
     */
    @Column(name = "password")
    private String password;
    /**
     * 是否超级管理员</br>
     * 1-是 0-否
     */
    @Column(name = "isSuper")
    private int isSuper;
    /**
     * 组编号
     */
    @Column(name = "groupId")
    private int groupId;
    /**
     * 组名称
     */
    @Column(name = "groupName")
    private String groupName;

    /**
     * 管理员头像
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * 管理员头像Url
     */
    @Transient
    private String avatarUrl;

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(int isSuper) {
        this.isSuper = isSuper;
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

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", isSuper=" + isSuper +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}

