package net.shopnc.b2b2c.domain;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 导航实体<br/>
 * Created by hbj on 2015/12/7.
 */
@Entity
@Table(name = "navigation")
public class Navigation {
    /**
     * 主键
     */
    @Id
    @GeneratedValue
    @Column(name = "nav_id")
    private int navId;
    /**
     * 导航标题
     */
    @Column(name = "title")
    @NotNull
    @Size(min = 2, max = 20, message = "分类长度为2-30个字")
    private String title;
    /**
     * 导航跳转链接
     */
    @URL
    @Column(name = "url")
    @NotNull
    private String url;
    /**
     * 显示位置
     */
    @Min(1)
    @Max(3)
    @Column(name = "position_id")
    private int positionId = 2;
    /**
     * 是否新窗口打开 1-是，0-否
     */
    @Min(0)
    @Max(1)
    @Column(name = "")
    private int openType;
    /**
     * 分类排序
     */
    @Min(0)
    @Max(999)
    @Column(name = "sort")
    private int sort = 0;

    public int getNavId() {
        return navId;
    }

    public void setNavId(int navId) {
        this.navId = navId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Navigation{" +
                "navId=" + navId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", positionId=" + positionId +
                ", openType=" + openType +
                ", sort=" + sort +
                '}';
    }
}
