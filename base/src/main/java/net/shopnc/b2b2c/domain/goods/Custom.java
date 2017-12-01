package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 自定义属性实体
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "custom")
public class Custom implements Serializable {
    /**
     * 自定义属性编号
     */
    @Id
    @GeneratedValue
    @Column(name = "custom_id")
    private int customId;
    /**
     * 自定义属性名称
     */
    @Column(name = "custom_name")
    private String customName;
    /**
     * 分类编号
     */
    @Column(name = "category_id")
    private int categoryId;

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "customId=" + customId +
                ", customName='" + customName + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
