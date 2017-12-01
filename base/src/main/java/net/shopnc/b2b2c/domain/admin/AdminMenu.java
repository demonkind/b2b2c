package net.shopnc.b2b2c.domain.admin;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 后台菜单实体
 * Created by dqw on 2015/12/10.
 */
@Entity
@Table(name = "admin_menu")
public class AdminMenu implements Serializable {
    /**
     * 菜单编号
     */
    @Id
    @Column(name = "id")
    private int id;

    /**
     * 菜单名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 菜单标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 菜单链接
     */
    @Column(name = "url")
    private String url;

    /**
     * 权限
     */
    @Column(name = "permission")
    private String permission;

    /**
     * 父级编号
     */
    @Column(name = "parent_id")
    private int parentId;

    /**
     * 权限组编号</br>
     * 如果几个菜单的权限有关联性需要同时被选中时需要设置相同的编号
     */
    @Column(name = "group_id")
    private int groupId;

    @Transient
    private List<AdminMenu> subMenu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<AdminMenu> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<AdminMenu> subMenu) {
        this.subMenu = subMenu;
    }

    @Override
    public String toString() {
        return "AdminMenu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", permission='" + permission + '\'' +
                ", parentId=" + parentId +
                ", groupId=" + groupId +
                ", subMenu=" + subMenu +
                '}';
    }
}

