package net.shopnc.b2b2c.domain.store;

import net.shopnc.b2b2c.config.ShopConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 店内商品分类实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "store_label")
public class StoreLabel implements Serializable {
    /**
     * 店内商品分类编号
     */
    @Id
    @GeneratedValue
    @Column(name = "store_label_id")
    private int storeLabelId;
    /**
     * 店内商品分类名称
     */
    @Column(name = "store_label_name")
    private String storeLabelName;
    /**
     * 店内商品分类排序
     */
    @Column(name = "store_label_sort")
    private int storeLabelSort = 0;
    /**
     * 父级分类编号
     */
    @Column(name = "parent_id")
    private int parentId = 0;
    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private int storeId;
    /**
     * 是否折叠
     */
    @Column(name = "is_fold")
    private int isFold;
    /**
     * 图片
     */
    @Column(name = "image")
    private String image = "";
    /**
     * 图片路径
     */
    @Transient
    private String imageSrc;
    /**
     * 子集实体
     */
    @Transient
    private List<StoreLabel> storeLabelList;

    public int getStoreLabelId() {
        return storeLabelId;
    }

    public void setStoreLabelId(int storeLabelId) {
        this.storeLabelId = storeLabelId;
    }

    public String getStoreLabelName() {
        return storeLabelName;
    }

    public void setStoreLabelName(String storeLabelName) {
        this.storeLabelName = storeLabelName;
    }

    public int getStoreLabelSort() {
        return storeLabelSort;
    }

    public void setStoreLabelSort(int storeLabelSort) {
        this.storeLabelSort = storeLabelSort;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getIsFold() {
        return isFold;
    }

    public void setIsFold(int isFold) {
        this.isFold = isFold;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<StoreLabel> getStoreLabelList() {
        return storeLabelList;
    }

    public String getImageSrc() {
        if (image.equals("")) {
            return "";
        } else {
            return ShopConfig.getUploadRoot() + image;
        }
    }

    public void setStoreLabelList(List<StoreLabel> storeLabelList) {
        this.storeLabelList = storeLabelList;
    }

    @Override
    public String toString() {
        return "StoreLabel{" +
                "storeLabelId=" + storeLabelId +
                ", storeLabelName='" + storeLabelName + '\'' +
                ", storeLabelSort=" + storeLabelSort +
                ", parentId=" + parentId +
                ", storeId=" + storeId +
                ", isFold=" + isFold +
                ", image='" + image + '\'' +
                ", storeLabelList=" + storeLabelList +
                '}';
    }
}
