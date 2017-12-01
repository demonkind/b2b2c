package net.shopnc.b2b2c.domain.admin;

import javax.persistence.*;

/**
 * 后台管理员组实体
 * Created by dqw on 2015/12/29.
 */
@Entity
@Table(name = "admin_group")
public class AdminGroup {
    /**
     * 编号
     */
    @Id
    @GeneratedValue
    @Column(name = "group_id")
    private int groupId;
    /**
     * 组名称
     */
    @Column(name = "group_name")
    private String groupName;

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

    @Override
    public String toString() {
        return "AdminGroup{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}

