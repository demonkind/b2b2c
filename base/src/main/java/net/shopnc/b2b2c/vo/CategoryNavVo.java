package net.shopnc.b2b2c.vo;

import net.shopnc.b2b2c.domain.goods.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类实体
 * Created by shopnc.feng on 2016-01-05.
 */
public class CategoryNavVo {
    /**
     * 商品分类编号
     */
    private int categoryId;
    /**
     * 商品分类名称
     */
    private String categoryName;
    /**
     * 父级分类编号
     */
    private int parentId = 0;
    /**
     * 排序
     */
    private int categorySort;
    /**
     * 深度
     */
    private int deep = 0;
    /**
     * 分类列表
     */
    private List<Object> categoryList = new ArrayList<Object>();
    /**
     * 分类
     */
    private Object category;

    public CategoryNavVo(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.parentId = category.getParentId();
        this.categorySort = category.getCategorySort();
        this.deep = category.getDeep();
    }

    public CategoryNavVo() {
    }

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

    public List<Object> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Object> categoryList) {
        this.categoryList = categoryList;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CategoryNavVo{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", categorySort=" + categorySort +
                ", deep=" + deep +
                ", categoryList=" + categoryList +
                ", category=" + category +
                '}';
    }
}
