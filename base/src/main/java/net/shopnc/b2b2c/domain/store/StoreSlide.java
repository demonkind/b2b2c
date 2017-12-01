package net.shopnc.b2b2c.domain.store;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;

import javax.persistence.*;

/**
 * 店铺幻灯实体
 * Created by dqw on 2016/01/19.
 */
@Entity
@Table(name = "store_slide")
public class StoreSlide {
    /**
     * 店铺编号
     */
    @Id
    @GeneratedValue
    @Column(name = "slide_id")
    private int slideId;

    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private int storeId;

    /**
     * 幻灯图片
     */
    @Column(name = "image")
    private String image = "";

    /**
     * 幻灯URL
     */
    @Column(name = "url")
    private String url = "";

    /**
     * 幻灯片图片URL
     */
    @Transient
    private String imageUrl;

    public int getSlideId() {
        return slideId;
    }

    public void setSlideId(int slideId) {
        this.slideId = slideId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        if(imageUrl == null) {
            if(image == null) {
                return ShopConfig.getPublicRoot() + Common.DEFAULT_STORE_SLIDE1;
            } else {
                return ShopConfig.getUploadRoot() + image;
            }
        } else {
            return ShopConfig.getPublicRoot() + imageUrl;
        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "StoreSlide{" +
                "slideId=" + slideId +
                ", storeId=" + storeId +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

