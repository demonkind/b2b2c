package net.shopnc.b2b2c.domain.store;

import javax.persistence.*;
import javax.validation.constraints.Null;

/**
 * 商家权限组实体
 * Created by dqw on 2015/11/20.
 */
@Entity
@Table(name="seller_group")
public class SellerGroup {
    /**
     * 权限组编号
     */
    @Id
    @GeneratedValue
    @Column(name = "group_id")
    private int groupId;
    /**
     * 权限组名称
     */
    @Column(name = "group_name")
    private String groupName;
    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private int storeId;

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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "SellerGroup{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
