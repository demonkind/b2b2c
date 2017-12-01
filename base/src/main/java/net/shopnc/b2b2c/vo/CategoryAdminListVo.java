package net.shopnc.b2b2c.vo;

/**
 * Created by shopnc.feng on 2015-11-13.
 */
public class CategoryAdminListVo {
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
     * 品牌名称
     */
    private String brandIds;
    /**
     * 品牌名称
     */
    private String brandNames;
    /**
     * 属性名称
     */
    private String attributeNames;
    /**
     * 自定义属性名称
     */
    private String customsNames;

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

    public String getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String brandIds) {
        this.brandIds = brandIds;
    }

    public String getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(String brandNames) {
        this.brandNames = brandNames;
    }

    public String getAttributeNames() {
        return attributeNames;
    }

    public void setAttributeNames(String attributeNames) {
        this.attributeNames = attributeNames;
    }

    public String getCustomsNames() {
        return customsNames;
    }

    public void setCustomsNames(String customsNames) {
        this.customsNames = customsNames;
    }

    @Override
    public String toString() {
        return "CategoryAdminListVo{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", categorySort=" + categorySort +
                ", deep=" + deep +
                ", brandIds='" + brandIds + '\'' +
                ", brandNames='" + brandNames + '\'' +
                ", attributeNames='" + attributeNames + '\'' +
                ", customsNames='" + customsNames + '\'' +
                '}';
    }
}
