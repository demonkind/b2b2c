package net.shopnc.b2b2c.domain.store;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺等级实体
 * Created by dqw on 2015/12/11.
 */
@Entity
@Table(name="store_grade")
public class StoreGrade implements Serializable {
    /**
     * 等级编号
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    /**
     * 等级名称
     */
    @Length(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    /**
     * 商品限制
     */
    @Min(0)
    @Max(99999)
    @Column(name = "goods_limit")
    private int goodsLimit;
    /**
     * 图片限制
     */
    @Min(0)
    @Max(99999)
    @Column(name = "album_limit")
    private int albumLimit;
    /**
     * 推荐商品限制
     */
    @Min(0)
    @Max(99999)
    @Column(name = "recommend_limit")
    private int recommendLimit;
    /**
     * 店铺模板数量
     */
    @Min(0)
    @Max(999)
    @Column(name = "template_count")
    private int templateCount;
    /**
     * 店铺模板名称
     */
    @Column(name = "template")
    private String template;
    /**
     * 价格
     */
    @Min(0)
    @Max(999999)
    @Column(name = "price")
    private int price;
    /**
     * 申请说明
     */
    @Column(name = "description")
    private String description;
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

    public int getGoodsLimit() {
        return goodsLimit;
    }

    public void setGoodsLimit(int goodsLimit) {
        this.goodsLimit = goodsLimit;
    }

    public int getAlbumLimit() {
        return albumLimit;
    }

    public void setAlbumLimit(int albumLimit) {
        this.albumLimit = albumLimit;
    }

    public int getRecommendLimit() {
        return recommendLimit;
    }

    public void setRecommendLimit(int recommendLimit) {
        this.recommendLimit = recommendLimit;
    }

    public int getTemplateCount() {
        return templateCount;
    }

    public void setTemplateCount(int templateCount) {
        this.templateCount = templateCount;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "StoreGrade{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", goodsLimit=" + goodsLimit +
                ", albumLimit=" + albumLimit +
                ", recommendLimit=" + recommendLimit +
                ", templateCount=" + templateCount +
                ", template='" + template + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", sort=" + sort +
                '}';
    }
}

