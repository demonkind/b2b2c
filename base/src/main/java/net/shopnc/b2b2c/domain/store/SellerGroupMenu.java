package net.shopnc.b2b2c.domain.store;

import javax.persistence.*;
import javax.validation.constraints.Null;

/**
 * 商家权限组和权限关系实体
 * Created by dqw on 2015/11/20.
 */
@Entity
@Table(name = "seller_group_menu")
public class SellerGroupMenu {
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
        return "SellerGroupMenu{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", menuId=" + menuId +
                '}';
    }
}
