package net.shopnc.b2b2c.domain;

import net.shopnc.common.validator.EmptyRange;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 行政区域实体</br>
 * Created by shopnc on 2015/10/21.
 *
 */
@Entity
@Table(name = "area")
public class Area implements Serializable {
    /**
     * 地区编号</br>
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "area_id")
    private int areaId;
    /**
     * 地区名称
     */
    @Size(min = 2, max = 30, message = "地区长度为2-30个字")
    @Column(name = "area_name")
    private String areaName;
    /**
     * 上级地区ID
     */
    @Column(name = "area_parent_id")
    private int areaParentId;
    /**
     * 深度<br/>
     * 一级地区深度为1，二级深度为2，依次类推。。。
     */
    @Min(1)
    @Column(name = "area_deep")
    private int areaDeep;
    /**
     * 所在大区</br>
     * 如华北、华中等
     */
    @EmptyRange(min = 2, max = 30, message = "大区长度为2-30个字")
    @Column(name = "area_region")
    private String areaRegion;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAreaParentId() {
        return areaParentId;
    }

    public void setAreaParentId(int areaParentId) {
        this.areaParentId = areaParentId;
    }

    public int getAreaDeep() {
        return areaDeep;
    }

    public void setAreaDeep(int areaDeep) {
        this.areaDeep = areaDeep;
    }

    public String getAreaRegion() {
        return areaRegion;
    }

    public void setAreaRegion(String areaRegion) {
        this.areaRegion = areaRegion;
    }

    @Override
    public String toString() {
        return "Area{" +
                "areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", areaParentId=" + areaParentId +
                ", areaDeep=" + areaDeep +
                ", areaRegion='" + areaRegion + '\'' +
                '}';
    }
}
