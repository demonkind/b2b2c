package net.shopnc.b2b2c.domain.admin;

import javax.persistence.*;
import javax.validation.constraints.Null;

/**
 * 后台管理员权限组和权限关系实体
 * Created by dqw on 2015/12/29.
 */
@Entity
@Table(name = "admin_group_permission")
public class AdminGroupPermission {
    /**
     * 编号
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    /**
     * 组编号
     */
    @Column(name = "group_id")
    private int groupId;
    /**
     * 权限编号
     */
    @Column(name = "menu_id")
    private int menuId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "AdminGroupPermission{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", menuId=" + menuId +
                '}';
    }
}
