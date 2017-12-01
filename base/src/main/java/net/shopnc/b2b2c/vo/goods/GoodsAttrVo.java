package net.shopnc.b2b2c.vo.goods;

import net.shopnc.b2b2c.domain.goods.Attribute;
import net.shopnc.b2b2c.domain.goods.AttributeValue;
import net.shopnc.b2b2c.domain.goods.Custom;
import net.shopnc.b2b2c.domain.goods.GoodsCustom;

/**
 * 商品属性实体，商品详情显示使用
 * Created by shopnc.feng on 2015-12-15.
 */
public class GoodsAttrVo {
    private String name;
    private String value;

    public GoodsAttrVo() {
    }

    public GoodsAttrVo(Attribute attribute, AttributeValue attributeValue) {
        this.name = attribute.getAttributeName();
        this.value = attributeValue.getAttributeValueName();
    }

    public GoodsAttrVo(Custom custom, GoodsCustom goodsCustom) {
        this.name = custom.getCustomName();
        this.value = goodsCustom.getCustomValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GoodsAttrVo{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
