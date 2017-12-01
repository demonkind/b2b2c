package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品属性对应关系表<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "goods_attribute")
public class GoodsAttribute implements Serializable {
    /**
     * 商品SPU编号
     */
    @Id
    @Column(name = "common_id")
    private int commonId;
    /**
     * 属性值编号
     */
    @Id
    @Column(name = "attribute_value_id")
    private int attributeValueId;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getAttributeValueId() {
        return attributeValueId;
    }

    public void setAttributeValueId(int attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    @Override
    public String toString() {
        return "GoodsAttribute{" +
                "commonId=" + commonId +
                ", attributeValueId=" + attributeValueId +
                '}';
    }
}
