package net.shopnc.b2b2c.domain.goods;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 品牌标签实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "brand_label")
public class BrandLabel implements Serializable {
    /**
     * 品牌标签编号
     */
    @Id
    @GeneratedValue
    @Column(name = "brand_label_id")
    private int brandLabelId;
    /**
     * 品牌标签名称
     */
    @NotNull
    @Length(min = 2, max = 12)
    @Column(name = "brand_label_name")
    private String brandLabelName;
    /**
     * 品牌标签排序
     */
    @Min(0)
    @Max(999)
    @Column(name = "brand_label_sort")
    private int brandLabelSort = 0;

    public int getBrandLabelId() {
        return brandLabelId;
    }

    public void setBrandLabelId(int brandLabelId) {
        this.brandLabelId = brandLabelId;
    }

    public String getBrandLabelName() {
        return brandLabelName;
    }

    public void setBrandLabelName(String brandLabelName) {
        this.brandLabelName = brandLabelName;
    }

    public int getBrandLabelSort() {
        return brandLabelSort;
    }

    public void setBrandLabelSort(int brandLabelSort) {
        this.brandLabelSort = brandLabelSort;
    }

    @Override
    public String toString() {
        return "BrandLabel{" +
                "brandLabelId=" + brandLabelId +
                ", brandLabelName='" + brandLabelName + '\'' +
                ", brandLabelSort=" + brandLabelSort +
                '}';
    }
}
