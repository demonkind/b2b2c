package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 分类与标签关系表
 * Created by shopnc.feng on 2015-10-22.
 */
@Entity
@Table(name = "category_label")
public class CategoryLabel implements Serializable {
    /**
     * 商品分类编号
     */
    @Id
    @Column(name = "category_id")
    private int categoryId;
    /**
     * 商品标签编号
     */
    @Id
    @Column(name = "label_id")
    private int labelId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    @Override
    public String toString() {
        return "CategoryLabel{" +
                "categoryId=" + categoryId +
                ", labelId=" + labelId +
                '}';
    }
}
