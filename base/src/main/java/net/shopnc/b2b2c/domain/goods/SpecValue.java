package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 规格值实体<br>
 * Created by shopnc.feng on 2015-10-22.
 */
@Entity
@Table(name = "spec_value")
public class SpecValue implements Serializable {
    /**
     * 规格值编号
     */
    @Id
    @GeneratedValue
    @Column(name = "spec_value_id")
    private int specValueId;
    /**
     * 规格编号
     */
    @Column(name = "spec_id")
    private int specId;
    /**
     * 规格值名称
     */
    @NotNull
    @Column(name = "spec_value_name")
    private String specValueName;
    /**
     * 店铺编号<br>
     * 0为平台设置
     */
    @Column(name = "store_id")
    private int storeId = 0;

    public int getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(int specValueId) {
        this.specValueId = specValueId;
    }

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpecValueName() {
        return specValueName;
    }

    public void setSpecValueName(String specValueName) {
        this.specValueName = specValueName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "SpecValue{" +
                "specValueId=" + specValueId +
                ", specId=" + specId +
                ", specValueName='" + specValueName + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
