package net.shopnc.b2b2c.domain.store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 商家菜单实体
 * Created by dqw on 2015/11/25.
 */
@Entity
@Table(name = "seller_menu")
public class SellerMenu implements Serializable {
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
     * 父级编号
     */
    @Column(name="parent_id")
    private int parentId;

    /**
     * 权限组编号</br>
     * 如果几个菜单的权限有关联性需要同时被选中时需要设置相同的编号
     */
    @Column(name="group_id")
    private int groupId;

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

    @Override
    public String toString() {
        return "SellerMenu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", parentId=" + parentId +
                ", groupId=" + groupId +
                '}';
    }
}

