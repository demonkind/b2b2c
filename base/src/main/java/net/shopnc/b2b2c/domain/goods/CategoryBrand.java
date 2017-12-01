package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品分类与品牌关系实体
 * Created by shopnc.feng on 2015-11-13.
 */
@Entity
@Table(name = "category_brand")
public class CategoryBrand implements Serializable {
    /**
     * 商品分类编号
     */
    @Id
    @Column(name = "category_id")
    private int categoryId;
    /**
     * 品牌编号
     */
    @Id
    @Column(name = "brand_id")
    private int brandId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "CategoryBrand{" +
                "categoryId=" + categoryId +
                ", brandId=" + brandId +
                '}';
    }
}
