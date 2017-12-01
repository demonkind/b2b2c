package net.shopnc.b2b2c.vo;

import net.shopnc.b2b2c.domain.goods.SpecValue;

import java.util.List;

/**
 * 后台规格JSON数据实体
 * Created by shopnc.feng on 2015-11-09.
 */
public class SpecAndValueVo {
    /**
     * 规格编号
     */
    private int specId;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 规格排序
     */
    private int specSort;
    /**
     * 店铺编号<br>
     * 0为平台设置
     */
    private int storeId;
    /**
     * 所有规格值，英文“,”隔开
     */
    private String specValueNames;
    /**
     * 规格值
     */
    private List<SpecValue> specValueList;

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

    public int getSpecSort() {
        return specSort;
    }

    public void setSpecSort(int specSort) {
        this.specSort = specSort;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getSpecValueNames() {
        return specValueNames;
    }

    public void setSpecValueNames(String specValueNames) {
        this.specValueNames = specValueNames;
    }

    public List<SpecValue> getSpecValueList() {
        return specValueList;
    }

    public void setSpecValueList(List<SpecValue> specValueList) {
        this.specValueList = specValueList;
    }

    @Override
    public String toString() {
        return "SpecAndValueVo{" +
                "specId=" + specId +
                ", specName='" + specName + '\'' +
                ", specSort=" + specSort +
                ", storeId=" + storeId +
                ", specValueNames='" + specValueNames + '\'' +
                ", specValueList=" + specValueList +
                '}';
    }
}
