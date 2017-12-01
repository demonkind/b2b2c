package net.shopnc.b2b2c.domain.goods;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品图片实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "goods_image")
public class GoodsImage implements Serializable {
    /**
     * 商品图片编号
     */
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private int imageId;
    /**
     * 商品SPU编号
     */
    @Column(name = "common_id")
    private int commonId;
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    @Column(name = "color_id")
    private int colorId;
    /**
     * 图片名称
     */
    @Column(name = "image_name")
    private String imageName;
    /**
     * 图片排序
     */
    @Column(name = "image_sort")
    private int imageSort;
    /**
     * 默认主图<br>
     * 1是，0否
     */
    @Column(name = "is_default")
    private int isDefault;
    /**
     * 图片路径
     */
    @Transient
    private String imageSrc;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageSort() {
        return imageSort;
    }

    public void setImageSort(int imageSort) {
        this.imageSort = imageSort;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getImageSrc() {
        if ( imageName.equals("")) {
            return ShopConfig.getPublicRoot() + Common.DEFAULT_GOODS_IMG;
        } else {
            return ShopConfig.getUploadRoot() + imageName;
        }
    }

    @Override
    public String toString() {
        return "GoodsImage{" +
                "imageId=" + imageId +
                ", commonId=" + commonId +
                ", colorId=" + colorId +
                ", imageName='" + imageName + '\'' +
                ", imageSort=" + imageSort +
                ", isDefault=" + isDefault +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
