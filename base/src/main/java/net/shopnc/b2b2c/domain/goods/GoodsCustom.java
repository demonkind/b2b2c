package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品与自定义属性关系实体
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "goods_custom")
public class GoodsCustom implements Serializable {
    /**
     * 商品SPU编号
     */
    @Id
    @Column(name = "common_id")
    private int commonId;
    /**
     * 自定义属性编号
     */
    @Id
    @Column(name = "custom_id")
    private int customId;
    /**
     * 自定义属性名称
     */
    @Column(name = "custom_value")
    private String customValue;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    @Override
    public String toString() {
        return "GoodsCustom{" +
                "commonId=" + commonId +
                ", customId=" + customId +
                ", customValue='" + customValue + '\'' +
                '}';
    }
}
