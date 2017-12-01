package net.shopnc.b2b2c.domain.store;

import javax.persistence.*;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.BillCycleType;
import net.shopnc.b2b2c.constant.Common;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 店铺实体
 * Created by dqw on 2015/12/10.
 */
@Entity
@Table(name = "store")
public class Store {
    /**
     * 店铺编号
     */
    @Id
    @Column(name = "store_id")
    private int storeId;
    /**
     * 店铺名称
     */
    @NotEmpty
    @Size(min = 2, max = 50)
    @Column(name = "store_name")
    private String storeName;
    /**
     * 店铺等级编号
     */
    @Column(name = "grade_id")
    private int gradeId;
    /**
     * 店铺等级名称
     */
    @Column(name = "grade_name")
    private String gradeName;
    /**
     * 商家编号
     */
    @Column(name = "seller_id")
    private int sellerId;
    /**
     * 商家用户名
     */
    @Size(min = 3, max = 20)
    @Column(name = "seller_name")
    private String sellerName;
    /**
     * 店铺分类编号
     */
    @Column(name = "class_id")
    private int classId;
    /**
     * 店铺分类名称
     */
    @Column(name = "class_name")
    private String className;
    /**
     * 店铺状态</br>
     * 0-关闭 1-开启
     */
    @Column(name = "state")
    private int state;
    /**
     * 店铺地址
     */
    @Column(name = "store_address")
    private String storeAddress;
    /**
     * 店铺创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "store_create_time")
    private Timestamp storeCreateTime;
    /**
     * 店铺过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "store_end_time")
    private Timestamp storeEndTime;
    /**
     * 店铺Logo
     */
    @Column(name = "store_logo")
    private String storeLogo= "";
    /**
     * 店铺横幅
     */
    @Column(name = "store_banner")
    private String storeBanner = "";
    /**
     * 店铺头像
     */
    @Column(name = "store_avatar")
    private String storeAvatar = "";
    /**
     * 店铺seo关键字
     */
    @Size(min = 0, max = 50)
    @Column(name = "store_seo_keywords")
    private String storeSeoKeywords = "";
    /**
     * 店铺seo描述
     */
    @Size(min = 0, max = 120)
    @Column(name = "store_seo_description")
    private String storeSeoDescription = "";
    /**
     * 店铺QQ
     */
    @Size(min = 0, max = 20)
    @Column(name = "store_qq")
    private String storeQq = "";
    /**
     * 店铺阿里旺旺
     */
    @Size(min = 0, max = 20)
    @Column(name = "store_ww")
    private String storeWw = "";
    /**
     * 店铺微信
     */
    @Size(min = 0, max = 20)
    @Column(name = "store_weixin")
    private String storeWeixin = "";
    /**
     * 店铺商家电话
     */
    @Size(min = 0, max = 20)
    @Column(name = "store_phone")
    private String storePhone = "";
    /**
     * 店铺主营商品
     */
    @Size(min = 0, max = 50)
    @Column(name = "store_zy")
    private String storeZy = "";
    /**
     * 是否推荐店铺</br>
     * 0-否 1-是，默认为0
     */
    @Column(name = "is_recommend")
    private int isRecommend = 0;
    /**
     * 店铺当前主题
     */
    @Column(name = "store_theme")
    private String storeTheme = "default";
    /**
     * 描述相符度分数
     */
    @Column(name = "store_desccredit")
    private double storeDesccredit = 5.0;
    /**
     * 服务态度分数
     */
    @Column(name = "store_servicecredit")
    private double storeServicecredit = 5.0;
    /**
     * 发货速度分数
     */
    @Column(name = "store_deliverycredit")
    private double storeDeliverycredit = 5.0;
    /**
     * 店铺关注数量
     */
    @Column(name = "store_collect")
    private int storeCollect = 0;
    /**
     * 店铺销量
     */
    @Column(name = "store_sales")
    private int storeSales = 0;
    /**
     * 售前客服
     */
    @Lob
    @Column(name = "store_presales")
    private String storePresales = "";
    /**
     * 售后客服
     */
    @Lob
    @Column(name = "store_aftersales")
    private String storeAftersales = "";
    /**
     * 工作时间
     */
    @Column(name = "store_workingtime")
    private String storeWorkingtime = "";
    /**
     * 是否自营店铺</br>
     * 0-否 1-是
     */
    @Column(name = "is_own_shop")
    private int isOwnShop;
    /**
     * 店铺Logo URL
     */
    @Transient
    private String storeLogoUrl;
    /**
     * 店铺条幅URL
     */
    @Transient
    private String storeBannerUrl;

    /**
     * 店铺头像URL
     */
    @Transient
    private String storeAvatarUrl;
    /**
     * 店铺设置的快递公司Id
     */
    @Column(name = "ship_company",length = 500)
    private String shipCompany;
    /**
     * 公司名称
     */
    @Size(min = 0, max = 20)
    @Column(name = "company_name")
    private String companyName = "";
    /**
     * 所在地
     */
    @Column(name = "company_area")
    private String companyArea = "";
    /**
     * 结算周期
     */
    @Column(name = "bill_cycle")
    private int billCycle = 1;
    /**
     * 结算类型 1按月结/2按天结
     */
    @Column(name = "bill_cycle_type")
    private int billCycleType = 1;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Timestamp getStoreCreateTime() {
        return storeCreateTime;
    }

    public void setStoreCreateTime(Timestamp storeCreateTime) {
        this.storeCreateTime = storeCreateTime;
    }

    public Timestamp getStoreEndTime() {
        return storeEndTime;
    }

    public void setStoreEndTime(Timestamp storeEndTime) {
        this.storeEndTime = storeEndTime;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreBanner() {
        return storeBanner;
    }

    public void setStoreBanner(String storeBanner) {
        this.storeBanner = storeBanner;
    }

    public String getStoreAvatar() {
        return storeAvatar;
    }

    public void setStoreAvatar(String storeAvatar) {
        this.storeAvatar = storeAvatar;
    }

    public String getStoreSeoKeywords() {
        return storeSeoKeywords;
    }

    public void setStoreSeoKeywords(String storeSeoKeywords) {
        this.storeSeoKeywords = storeSeoKeywords;
    }

    public String getStoreSeoDescription() {
        return storeSeoDescription;
    }

    public void setStoreSeoDescription(String storeSeoDescription) {
        this.storeSeoDescription = storeSeoDescription;
    }

    public String getStoreQq() {
        return storeQq;
    }

    public void setStoreQq(String storeQq) {
        this.storeQq = storeQq;
    }

    public String getStoreWw() {
        return storeWw;
    }

    public void setStoreWw(String storeWw) {
        this.storeWw = storeWw;
    }

    public String getStoreWeixin() {
        return storeWeixin;
    }

    public void setStoreWeixin(String storeWeixin) {
        this.storeWeixin = storeWeixin;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreZy() {
        return storeZy;
    }

    public void setStoreZy(String storeZy) {
        this.storeZy = storeZy;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getStoreTheme() {
        return storeTheme;
    }

    public void setStoreTheme(String storeTheme) {
        this.storeTheme = storeTheme;
    }

    public double getStoreDesccredit() {
        return storeDesccredit;
    }

    public void setStoreDesccredit(double storeDesccredit) {
        this.storeDesccredit = storeDesccredit;
    }

    public double getStoreServicecredit() {
        return storeServicecredit;
    }

    public void setStoreServicecredit(double storeServicecredit) {
        this.storeServicecredit = storeServicecredit;
    }

    public double getStoreDeliverycredit() {
        return storeDeliverycredit;
    }

    public void setStoreDeliverycredit(double storeDeliverycredit) {
        this.storeDeliverycredit = storeDeliverycredit;
    }

    public int getStoreCollect() {
        return storeCollect;
    }

    public void setStoreCollect(int storeCollect) {
        this.storeCollect = storeCollect;
    }

    public int getStoreSales() {
        return storeSales;
    }

    public void setStoreSales(int storeSales) {
        this.storeSales = storeSales;
    }

    public String getStorePresales() {
        return storePresales;
    }

    public void setStorePresales(String storePresales) {
        this.storePresales = storePresales;
    }

    public String getStoreAftersales() {
        return storeAftersales;
    }

    public void setStoreAftersales(String storeAftersales) {
        this.storeAftersales = storeAftersales;
    }

    public String getStoreWorkingtime() {
        return storeWorkingtime;
    }

    public void setStoreWorkingtime(String storeWorkingtime) {
        this.storeWorkingtime = storeWorkingtime;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public String getStoreLogoUrl() {
        if (storeLogo == null || storeLogo.equals("")) {
            storeLogoUrl = ShopConfig.getPublicRoot() + Common.DEFAULT_STORE_LOGO;
        }else{
            storeLogoUrl = ShopConfig.getUploadRoot() + storeLogo;
        }
        return storeLogoUrl;
    }

    public void setStoreLogoUrl(String storeLogoUrl) {
        this.storeLogoUrl = storeLogoUrl;
    }

    public String getStoreBannerUrl() {
        if (storeBanner == null || storeBanner.equals("")) {
            storeBannerUrl = ShopConfig.getPublicRoot() + Common.DEFAULT_STORE_BANNER;
        }else{
            storeBannerUrl = ShopConfig.getUploadRoot() + storeBanner;
        }
        return storeBannerUrl;
    }

    public void setStoreBannerUrl(String storeBannerUrl) {
        this.storeBannerUrl = storeBannerUrl;
    }

    public String getStoreAvatarUrl() {
        if (storeAvatar == null || storeAvatar.equals("")) {
            storeAvatarUrl = ShopConfig.getPublicRoot() + Common.DEFAULT_STORE_AVATAR;
        }else{
            storeAvatarUrl = ShopConfig.getUploadRoot() + storeAvatar;
        }
        return storeAvatarUrl;
    }

    public void setStoreAvatarUrl(String storeAvatarUrl) {
        this.storeAvatarUrl = storeAvatarUrl;
    }

    public String getShipCompany() {
        return shipCompany;
    }

    public void setShipCompany(String shipCompany) {
        this.shipCompany = shipCompany;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyArea() {
        return companyArea;
    }

    public void setCompanyArea(String companyArea) {
        this.companyArea = companyArea;
    }

    public int getBillCycle() {
        return billCycle;
    }

    public void setBillCycle(int billCycle) {
        this.billCycle = billCycle;
    }

    public int getBillCycleType() {
        return billCycleType;
    }

    public void setBillCycleType(int billCycleType) {
        this.billCycleType = billCycleType;
    }

    /**
     * 返回结算周期与结算周期计算单位的整体描述
     * by hbj 2016/02/16
     * @return
     */
    public String getBillCyleDescription() {
        return (billCycleType == BillCycleType.MONTH ? "月" : "天") + " / " + billCycle;
    }
    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", gradeId=" + gradeId +
                ", gradeName='" + gradeName + '\'' +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", classId=" + classId +
                ", className='" + className + '\'' +
                ", state=" + state +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeCreateTime=" + storeCreateTime +
                ", storeEndTime=" + storeEndTime +
                ", storeLogo='" + storeLogo + '\'' +
                ", storeBanner='" + storeBanner + '\'' +
                ", storeAvatar='" + storeAvatar + '\'' +
                ", storeSeoKeywords='" + storeSeoKeywords + '\'' +
                ", storeSeoDescription='" + storeSeoDescription + '\'' +
                ", storeQq='" + storeQq + '\'' +
                ", storeWw='" + storeWw + '\'' +
                ", storeWeixin='" + storeWeixin + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", storeZy='" + storeZy + '\'' +
                ", isRecommend=" + isRecommend +
                ", storeTheme='" + storeTheme + '\'' +
                ", storeDesccredit=" + storeDesccredit +
                ", storeServicecredit=" + storeServicecredit +
                ", storeDeliverycredit=" + storeDeliverycredit +
                ", storeCollect=" + storeCollect +
                ", storeSales=" + storeSales +
                ", storePresales='" + storePresales + '\'' +
                ", storeAftersales='" + storeAftersales + '\'' +
                ", storeWorkingtime='" + storeWorkingtime + '\'' +
                ", isOwnShop=" + isOwnShop +
                ", storeLogoUrl='" + storeLogoUrl + '\'' +
                ", storeBannerUrl='" + storeBannerUrl + '\'' +
                ", storeAvatarUrl='" + storeAvatarUrl + '\'' +
                ", shipCompany='" + shipCompany + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyArea='" + companyArea + '\'' +
                ", billCycle=" + billCycle +
                ", billCycleType=" + billCycleType +
                '}';
    }
}
