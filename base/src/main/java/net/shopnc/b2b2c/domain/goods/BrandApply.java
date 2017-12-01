package net.shopnc.b2b2c.domain.goods;

import net.shopnc.b2b2c.config.ShopConfig;

import javax.persistence.*;

/**
 * Created by shopnc.feng on 2016-01-26.
 */
@Entity
@Table(name = "brand_apply")
public class BrandApply {
    /**
     * 品牌编号
     */
    @Id
    @Column(name = "brand_id")
    private int brandId;
    /**
     * 注册号
     */
    @Column(name = "register_number")
    private String registerNumber;
    /**
     * 注册证、受理书1
     */
    @Column(name = "image_1")
    private String image1;
    /**
     * 注册证、受理书2
     */
    @Column(name = "image_2")
    private String image2;
    /**
     * 所有者
     */
    @Column(name = "owner")
    private String owner;
    /**
     * 审核失败原因
     */
    @Column(name = "state_remark")
    private String stateRemark;
    /**
     * 注册证、受理书1图片路径
     */
    @Transient
    private String image1Src;
    /**
     * 注册证、受理书2图片路径
     */
    @Transient
    private String image2Src;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStateRemark() {
        return stateRemark;
    }

    public void setStateRemark(String stateRemark) {
        this.stateRemark = stateRemark;
    }

    public String getImage1Src() {
        return ShopConfig.getUploadRoot() + image1;
    }

    public String getImage2Src() {
        return ShopConfig.getUploadRoot() + image2;
    }

    @Override
    public String toString() {
        return "BrandApply{" +
                "brandId=" + brandId +
                ", registerNumber='" + registerNumber + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", owner='" + owner + '\'' +
                ", stateRemark='" + stateRemark + '\'' +
                ", image1Src='" + image1Src + '\'' +
                ", image2Src='" + image2Src + '\'' +
                '}';
    }
}
