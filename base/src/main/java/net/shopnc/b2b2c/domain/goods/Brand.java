package net.shopnc.b2b2c.domain.goods;

import net.shopnc.b2b2c.config.ShopConfig;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 品牌实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "brand")
public class Brand implements Serializable {
    /**
     * 品牌编号
     */
    @Id
    @GeneratedValue
    @Column(name = "brand_id")
    private int brandId;
    /**
     * 品牌名称
     */
    @NotNull
    @Length(min = 1, max = 30)
    @Column(name = "brand_name")
    private String brandName;
    /**
     * 英文名称
     */
    @Length(min = 0, max = 30)
    @Column(name = "brand_english")
    private String brandEnglish;
    /**
     * 品牌首字母
     */
    @Column(name = "brand_initial")
    private String brandInitial;
    /**
     * 品牌图片名称
     */
    @Column(name = "brand_image")
    private String brandImage;
    /**
     * 品牌图片地址
     */
    @Transient
    private String brandImageSrc;
    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private int storeId = 0;
    /**
     * 品牌排序
     */
    @Min(0)
    @Max(999)
    @Column(name = "brand_sort")
    private int brandSort = 0;
    /**
     * 是否推荐<br>
     * 1是 0否
     */
    @Min(0)
    @Max(1)
    @Column(name = "is_recommend")
    private int isRecommend = 0;
    /**
     * 申请状态<br>
     * 店铺申请添加品牌使用，0为申请中，1为通过，10为申请失败<br>
     * 店铺添加品牌为0，平台添加品牌为1
     */
    @Column(name = "apply_state")
    private int applyState = 1;
    /**
     * 展示方式<br>
     * 1图片，0文字
     */
    @Min(0)
    @Max(1)
    @Column(name = "show_type")
    private int showType;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getBrandSort() {
        return brandSort;
    }

    public void setBrandSort(int brandSort) {
        this.brandSort = brandSort;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandEnglish() {
        return brandEnglish;
    }

    public void setBrandEnglish(String brandEnglish) {
        this.brandEnglish = brandEnglish;
    }

    public String getBrandInitial() {
        return brandInitial;
    }

    public void setBrandInitial(String brandInitial) {
        this.brandInitial = brandInitial;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getBrandImageSrc() {
        if (brandImage.equals("")) {
            return ShopConfig.getPublicRoot() + "img/default_image.gif";
        } else {
            return ShopConfig.getUploadRoot() + brandImage;
        }
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getApplyState() {
        return applyState;
    }

    public void setApplyState(int applyState) {
        this.applyState = applyState;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", brandEnglish='" + brandEnglish + '\'' +
                ", brandInitial='" + brandInitial + '\'' +
                ", brandImage='" + brandImage + '\'' +
                ", brandImageSrc='" + brandImageSrc + '\'' +
                ", storeId=" + storeId +
                ", brandSort=" + brandSort +
                ", isRecommend=" + isRecommend +
                ", applyState=" + applyState +
                ", showType=" + showType +
                '}';
    }
}
