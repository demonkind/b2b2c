package net.shopnc.b2b2c.vo;

/**
 * Created by shopnc.feng on 2015-11-13.
 */
public class CategoryCommissionAdminListVo {
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
    private int parentId;
    /**
     * 父级分类名称
     */
    private String parentName;
    /**
     * 排序
     */
    private int categorySort;
    /**
     * 深度
     */
    private int deep;

    /**
     * 佣金比例id
     */
    private int ratesId;
    /**
     * 佣金比例
     */
    private int commisRate;


    /**
     * 是否关联到下级
     */
//    private int isRel;

//    public int getIsRel() {
//        return isRel;
//    }
//
//    public void setIsRel(int isRel) {
//        this.isRel = isRel;
//    }

    public int getRatesId() {
        return ratesId;
    }

    public void setRatesId(int ratesId) {
        this.ratesId = ratesId;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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

    public int getCommisRate() {
        return commisRate;
    }

    public void setCommisRate(int commisRate) {
        this.commisRate = commisRate;
    }

    @Override
    public String toString() {
        return "CategoryCommisRatesAdminListVo{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", deep=" + deep +
                ", ratesId='" + ratesId + '\'' +
                ", commisRate='" + commisRate + '\'' +
//                ", isRel='" + isRel + '\'' +
                '}';
    }
}
