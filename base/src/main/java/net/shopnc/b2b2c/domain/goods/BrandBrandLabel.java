package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 品牌与品牌标签关系实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "brand_brand_label")
public class BrandBrandLabel implements Serializable {
    /**
     * 品牌编号
     */
    @Id
    @Column(name = "brand_id")
    private int brandId;
    /**
     * 品牌标签编号
     */
    @Id
    @Column(name = "brand_label_id")
    private int brandLabelId;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getBrandLabelId() {
        return brandLabelId;
    }

    public void setBrandLabelId(int brandLabelId) {
        this.brandLabelId = brandLabelId;
    }

    @Override
    public String toString() {
        return "BrandBrandLabel{" +
                "brandId=" + brandId +
                ", brandLabelId=" + brandLabelId +
                '}';
    }
}
