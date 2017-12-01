package net.shopnc.b2b2c.domain.store;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Null;

/**
 * 主营行业实体
 * Created by dqw on 2015/12/14.
 */
@Entity
@Table(name="store_class")
public class StoreClass {
    /**
     * 店铺分类ID
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    /**
     * 分类名称
     */
    @Length(min = 1, max = 20)
    @Column(name = "name")
    private String name;
    /**
     * 保证金数额
     */
    @Min(0)
    @Max(999999)
    @Column(name = "bail")
    private int bail;
    /**
     * 排序
     */
    @Min(0)
    @Max(999)
    @Column(name = "sort")
    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBail() {
        return bail;
    }

    public void setBail(int bail) {
        this.bail = bail;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "StoreClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bail=" + bail +
                ", sort=" + sort +
                '}';
    }
}

