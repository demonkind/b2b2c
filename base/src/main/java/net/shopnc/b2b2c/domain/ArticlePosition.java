package net.shopnc.b2b2c.domain;

import net.shopnc.b2b2c.constant.ArticlePositionsAllowAddCategory;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.Override;
import java.lang.String;

/**
 * 文章分类显示位置实体<br/>
 * Created by shopnc on 2015/11/25.
 */
@Entity
@Table(name = "article_position")
public class ArticlePosition {
    /**
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "position_id")
    private int positionId;

    /**
     * 位置名称
     */
    @Column(name = "position_title")
    private String positionTitle;
    /**
     * 是否可以新增下面的分类
     */
    @Column(name = "allow_add_category")
    private int allowAddCategory = ArticlePositionsAllowAddCategory.YES;
    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public int getAllowAddCategory() {
        return allowAddCategory;
    }

    public void setAllowAddCategory(int allowAddCategory) {
        this.allowAddCategory = allowAddCategory;
    }

    @Override
    public String toString() {
        return "ArticlePosition{" +
                "positionId=" + positionId +
                ", positionTitle='" + positionTitle + '\'' +
                ", allowAddCategory=" + allowAddCategory +
                '}';
    }
}
