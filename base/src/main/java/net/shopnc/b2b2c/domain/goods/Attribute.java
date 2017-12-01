package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 属性实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "attribute")
public class Attribute implements Serializable {
    /**
     * 属性编号
     */
    @Id
    @GeneratedValue
    @Column(name = "attribute_id")
    private int attributeId;
    /**
     * 属性名称
     */
    @Column(name = "attribute_name")
    private String attributeName;
    /**
     * 分类编号
     */
    @Column(name = "category_id")
    private int categoryId;
    /**
     * 属性排序
     */
    @Column(name = "attribute_sort")
    private int attributeSort;
    /**
     * 是否显示
     */
    @Column(name = "is_show")
    private int isShow;

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAttributeSort() {
        return attributeSort;
    }

    public void setAttributeSort(int attributeSort) {
        this.attributeSort = attributeSort;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "attributeId=" + attributeId +
                ", attributeName='" + attributeName + '\'' +
                ", categoryId=" + categoryId +
                ", attributeSort=" + attributeSort +
                ", isShow=" + isShow +
                '}';
    }
}
