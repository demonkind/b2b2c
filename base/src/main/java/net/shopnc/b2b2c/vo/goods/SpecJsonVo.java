package net.shopnc.b2b2c.vo.goods;

import java.util.List;

/**
 * Created by shopnc.feng on 2015-12-02.
 */
public class SpecJsonVo {
    private int specId;
    private String specName;
    private List<SpecValueListVo> specValueList;

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

    public List<SpecValueListVo> getSpecValueList() {
        return specValueList;
    }

    public void setSpecValueList(List<SpecValueListVo> specValueList) {
        this.specValueList = specValueList;
    }

    @Override
    public String toString() {
        return "SpecJsonVo{" +
                "specId='" + specId + '\'' +
                ", specName='" + specName + '\'' +
                ", specValueList=" + specValueList +
                '}';
    }
}
