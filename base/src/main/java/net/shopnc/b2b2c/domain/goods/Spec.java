package net.shopnc.b2b2c.domain.goods;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品规格表<br>
 * Created by shopnc.feng on 2015-10-22.
 */
@Entity
@Table(name = "spec")
public class Spec implements Serializable {
    /**
     * 规格编号
     */
    @Id
    @GeneratedValue
    @Column(name = "spec_id")
    private int specId;
    /**
     * 规格名称
     */
    @NotNull
    @Length(min = 1, max = 12)
    @Column(name = "spec_name")
    private String specName;
    /**
     * 规格排序
     */
    @Min(0)
    @Max(999)
    @Column(name = "spec_sort")
    private int specSort = 0;
    /**
     * 店铺编号<br>
     * 0为平台设置
     */
    @Column(name = "store_id")
    private int storeId = 0;

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public void setSpecSort(int specSort) {
        this.specSort = specSort;
    }

    public int getSpecSort() {
        return specSort;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "Spec{" +
                "specId=" + specId +
                ", specName='" + specName + '\'' +
                ", specSort='" + specSort + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
