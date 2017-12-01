package net.shopnc.b2b2c.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 文章分类实体<br/>
 * Created by shopnc on 2015/11/18.
 */
@Entity
@Table(name = "article_category")
public class ArticleCategory {
    /**
     * 分类主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private int categoryId;

    /**
     * 分类名称
     */
    @Size(min = 2, max = 20, message = "分类长度为2-30个字")
    @Column(name = "title")
    private String title;

    /**
     * 出现位置
     */
    @Column(name = "position_id")
    private int positionId = 1;

    /**
     * 分类排序
     */
    @Min(0)
    @Max(999)
    @Column(name = "sort")
    private int sort = 0;

    /**
     * 文章类型
     * 1-系统内置不可以删除，不可以发布文章
     * 2-系统内置不可以删除，可以发布文章
     * 3-系统内置可以删除，可以发布文章
     */
    @Min(value = 1)
    @Max(value = 3)
    @Column(name = "type")
    private int type = 3;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ArticleCategory{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", positionId=" + positionId +
                ", sort=" + sort +
                ", type=" + type +
                '}';
    }
}
