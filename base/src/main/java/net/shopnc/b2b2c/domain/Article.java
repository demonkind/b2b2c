package net.shopnc.b2b2c.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.ArticleAllowDelete;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * 文章实体<br/>
 * Created by shopnc on 2015/11/18.
 */
@Entity
@Table(name = "article")
public class Article {
    /**
     * 文章主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private int articleId;
    /**
     * 文章标题
     */
    @Size(min = 2, max = 30, message = "文章标题长度为2-30个字")
    @Column(name = "title")
    private String title;

    /**
     * 文章分类ID
     */
    @NotNull
    @Column(name = "category_id")
    private int categoryId;

    /**
     * 外链
     */
    @Column(name = "url")
    private String url;

    /**
     * 文章内容
     */
    @Lob
    @Column(name = "content")
    private String content = "";

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 是否推荐</br>
     * 1-是 0-否
     */
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "recommend_state")
    private int recommendState;
    /**
     * 是否允许删除<br/>
     * 1-是 0-否
     */
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "allow_delete")
    private int allowDelete = ArticleAllowDelete.YES;

    @Transient
    private String categoryTitle = "";

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getRecommendState() {
        return recommendState;
    }

    public void setRecommendState(int recommendState) {
        this.recommendState = recommendState;
    }

    public int getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(int allowDelete) {
        this.allowDelete = allowDelete;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", categoryId=" + categoryId +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", recommendState=" + recommendState +
                ", allowDelete=" + allowDelete +
                ", categoryTitle='" + categoryTitle + '\'' +
                '}';
    }
}
