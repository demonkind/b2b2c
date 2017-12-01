package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 属性值实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "attribute_value")
public class AttributeValue implements Serializable {
    /**
     * 属性值编号
     */
    @Id
    @GeneratedValue
    @Column(name = "attribute_value_id")
    private int attributeValueId;
    /**
     * 属性值名称
     */
    @Column(name = "attribute_value_name")
    private String attributeValueName;
    /**
     * 属性编号
     */
    @Column(name = "attribute_id")
    private int attributeId;

    public int getAttributeValueId() {
        return attributeValueId;
    }

    public void setAttributeValueId(int attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    public String getAttributeValueName() {
        return attributeValueName;
    }

    public void setAttributeValueName(String attributeValueName) {
        this.attributeValueName = attributeValueName;
    }

    public int getAttributeId(int attributeId) {
        return this.attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
                "attributeValueId=" + attributeValueId +
                ", attributeValueName='" + attributeValueName + '\'' +
                ", attributeId=" + attributeId +
                '}';
    }
}
