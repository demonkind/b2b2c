package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * 店铺绑定商品分类及佣金实体
 * Created by hbj on 2015/12/14.
 */
@Entity
@Table(name = "store_bind_category")
public class StoreBindCategory {
    /**
     * 主键 自增
     */
    @Id
    @GeneratedValue
    @Column(name = "bind_id")
    private int bindId;
    /**
     * 店铺Id
     */
    @Min(1)
    @Column(name = "store_id")
    private int storeId;
    /**
     * 一级商品分类编号
     */
    @Min(1)
    @Column(name = "category_id1")
    private int categoryId1 = 0;
    /**
     * 一级商品分类名称
     */
    @Column(name = "category_name1")
    private String categoryName1 = "";
    /**
     * 二级商品分类
     */
    @Column(name = "category_id2")
    private int categoryId2 = 0;
    /**
     * 二级商品分类名称
     */
    @Column(name = "category_name2")
    private String categoryName2 = "";
    /**
     * 三级商品分类
     */
    @Column(name = "category_id3")
    private int categoryId3 = 0;
    /**
     * 三级商品分类名称
     */
    @Column(name = "category_name3")
    private String categoryName3 = "";
    /**
     * 佣金比例
     */
    @Column(name = "commission_rate")
    private int commissionRate;

    public int getBindId() {
        return bindId;
    }

    public void setBindId(int bindId) {
        this.bindId = bindId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public String getCategoryName1() {
        return categoryName1;
    }

    public void setCategoryName1(String categoryName1) {
        this.categoryName1 = categoryName1;
    }

    public int getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(int categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public String getCategoryName2() {
        return categoryName2;
    }

    public void setCategoryName2(String categoryName2) {
        this.categoryName2 = categoryName2;
    }

    public int getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(int categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public String getCategoryName3() {
        return categoryName3;
    }

    public void setCategoryName3(String categoryName3) {
        this.categoryName3 = categoryName3;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Override
    public String toString() {
        return "StoreBindCategory{" +
                "bindId=" + bindId +
                ", storeId=" + storeId +
                ", categoryId1=" + categoryId1 +
                ", categoryName1='" + categoryName1 + '\'' +
                ", categoryId2=" + categoryId2 +
                ", categoryName2='" + categoryName2 + '\'' +
                ", categoryId3=" + categoryId3 +
                ", categoryName3='" + categoryName3 + '\'' +
                ", commissionRate=" + commissionRate +
                '}';
    }
}

