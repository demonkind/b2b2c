package net.shopnc.b2b2c.domain.store;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * 店铺导航
 * Created by dqw on 2016/01/22.
 */
@Entity
@Table(name = "store_navigation")
public class StoreNavigation {
    /**
     * 导航编号
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    /**
     * 导航标题
     */
    @Size(min = 2, max = 10)
    @Column(name = "title")
    private String title;
    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private int storeId;
    /**
     * 导航内容
     */
    @Lob
    @Column(name = "content")
    private String content;
    /**
     * 导航排序
     */
    @Column(name = "sort")
    private int sort;
    /**
     * 是否显示
     */
    @Column(name = "is_show")
    private int isShow;
    /**
     * 导航URL
     */
    @Column(name = "url")
    private String url;
    /**
     * 是否新页面打开导航URL
     */
    @Column(name = "is_new_page")
    private int isNewPage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsNewPage() {
        return isNewPage;
    }

    public void setIsNewPage(int isNewPage) {
        this.isNewPage = isNewPage;
    }

    @Override
    public String toString() {
        return "StoreNavigation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", storeId=" + storeId +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                ", isShow=" + isShow +
                ", url='" + url + '\'' +
                ", isNewPage='" + isNewPage + '\'' +
                '}';
    }
}

