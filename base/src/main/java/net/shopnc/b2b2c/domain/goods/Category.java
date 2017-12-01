package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品分类实体
 * Created by shopnc.feng on 2015-10-22.
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {
    /**
     * 商品分类编号
     */
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private int categoryId;
    /**
     * 商品分类名称
     */
    @Column(name = "category_name")
    private String categoryName;
    /**
     * 父级分类编号
     */
    @Column(name = "parent_id")
    private int parentId = 0;
    /**
     * 排序
     */
    @Column(name = "category_sort")
    private int categorySort;

    /**
     * 深度
     */
    @Column(name = "deep")
    private int deep = 0;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(int categorySort) {
        this.categorySort = categorySort;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", categorySort=" + categorySort +
                ", deep=" + deep +
                '}';
    }
}
