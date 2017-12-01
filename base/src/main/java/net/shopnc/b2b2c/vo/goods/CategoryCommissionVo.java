package net.shopnc.b2b2c.vo.goods;

import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.domain.goods.CategoryCommission;

import javax.persistence.Column;

/**
 * Created by hbj on 2015/12/14.
 */
public class CategoryCommissionVo {
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
    @Column(name = "deep")
    private int deep = 0;
    /**
     * 佣金
     */
    private int commissionRate;

    public CategoryCommissionVo(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.categorySort = category.getCategorySort();
        this.parentId = category.getParentId();
        this.deep = category.getDeep();
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

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Override
    public String toString() {
        return "CategoryCommissionVo{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", categorySort=" + categorySort +
                ", deep=" + deep +
                ", commissionRate=" + commissionRate +
                '}';
    }
}
