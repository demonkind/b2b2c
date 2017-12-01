package net.shopnc.b2b2c.domain.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品SPU实体<br>
 * Created by shopnc.feng on 2015-10-22.
 */
@Entity
@Table(name = "goods_common")
public class GoodsCommon implements Serializable {
    /**
     * 商品SPU编号
     */
    @Id
    @GeneratedValue
    @Column(name = "common_id")
    private int commonId;
    /**
     * 商品名称
     */
    @Length(min = 3, max = 50, message = "商品名称为3到50个字符")
    @Column(name = "goods_name")
    private String goodsName;
    /**
     * 商品卖点
     */
    @Length(min = 0, max = 140, message = "商品买点长度为0到140个字符")
    @Column(name = "jingle")
    private String jingle;
    /**
     * 商品分类编号
     */
    @Min(value = 1)
    @Column(name = "category_id")
    private int categoryId;
    /**
     * 一级分类
     */
    @Column(name = "category_id_1")
    private int categoryId1;
    /**
     * 二级分类
     */
    @Column(name = "category_id_2")
    private int categoryId2;
    /**
     * 三级分类
     */
    @Column(name = "category_id_3")
    private int categoryId3;
    /**
     * 规格JSON
     */
    @Lob
    @Column(name = "spec_json")
    private String specJson;
    /**
     * 商品编号和规格值编号JSON
     */
    @Lob
    @Column(name = "goods_spec_value_json")
    private String goodsSpecValueJson;
    /**
     * 店铺编号
     */
    @Min(value = 1)
    @Column(name = "store_id")
    private int storeId;
    /**
     * 品牌编号
     */
    @Min(value = 0)
    @Column(name = "brand_id")
    private int brandId;
    /**
     * 商品描述
     */
    @Lob
    @Column(name = "goods_body")
    private String goodsBody;
    /**
     * 手机端商品描述
     */
    @Lob
    @Column(name = "mobile_body")
    private String mobileBody;
    /**
     * 商品状态<br>
     * 0下架，1正常，10违规禁售
     */
    @Column(name = "goods_state")
    private int goodsState;
    /**
     * 违规禁售原因
     */
    @Column(name = "state_remark")
    private String stateRemark;
    /**
     * 审核状态<br>
     * 0未通过，1已通过，10审核中
     */
    @Column(name = "goods_verify")
    private int goodsVerify;
    /**
     * 审核失败原因
     */
    @Column(name = "verify_remark")
    private String verifyRemark;
    /**
     * 商品创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "create_time")
    private Timestamp createTime;
    /**
     * 商品更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "update_time")
    private Timestamp updateTime;
    /**
     * 一级地区编号
     */
    @Min(value = 0)
    @Column(name = "area_id_1")
    private int areaId1 = 0;
    /**
     * 二级地区编号
     */
    @Min(value = 0)
    @Column(name = "area_id_2")
    private int areaId2 = 0;
    /**
     * 省市县(区)内容
     */
    @Column(name = "area_info")
    private String areaInfo;
    /**
     * 商品运费
     */
    @Column(name = "goods_freight")
    private BigDecimal goodsFreight =  new BigDecimal("0");
    /**
     * 运费模板ID
     */
    @Column(name = "freight_template_id")
    private int freightTemplateId = 0;
    /**
     * 商品重量
     */
    @Column(name = "freight_weight")
    private BigDecimal freightWeight = new BigDecimal(0);
    /**
     * 商品体积
     */
    @Column(name = "freight_volume")
    private BigDecimal freightVolume = new BigDecimal(0);
    /**
     * 是否使用增值税发票
     */
    @Column(name = "allow_vat")
    private int allowVat;
    /**
     * 顶部关联板式编号
     */
    @Column(name = "format_top")
    private int formatTop;
    /**
     * 底部关联板式编号
     */
    @Column(name = "format_bottom")
    private int formatBottom;
    /**
     * 是否推荐
     */
    @Column(name = "is_commend")
    private int isCommend;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getJingle() {
        return jingle;
    }

    public void setJingle(String jingle) {
        this.jingle = jingle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public int getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(int categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public int getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(int categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public String getSpecJson() {
        return specJson;
    }

    public void setSpecJson(String specJson) {
        this.specJson = specJson;
    }

    public String getGoodsSpecValueJson() {
        return goodsSpecValueJson;
    }

    public void setGoodsSpecValueJson(String goodsSpecValueJson) {
        this.goodsSpecValueJson = goodsSpecValueJson;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getGoodsBody() {
        return goodsBody;
    }

    public void setGoodsBody(String goodsBody) {
        this.goodsBody = goodsBody;
    }

    public String getMobileBody() {
        return mobileBody;
    }

    public void setMobileBody(String mobileBody) {
        this.mobileBody = mobileBody;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
    }

    public String getStateRemark() {
        return stateRemark;
    }

    public void setStateRemark(String stateRemark) {
        this.stateRemark = stateRemark;
    }

    public int getGoodsVerify() {
        return goodsVerify;
    }

    public void setGoodsVerify(int goodsVerify) {
        this.goodsVerify = goodsVerify;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getAreaId1() {
        return areaId1;
    }

    public void setAreaId1(int areaId1) {
        this.areaId1 = areaId1;
    }

    public int getAreaId2() {
        return areaId2;
    }

    public void setAreaId2(int areaId2) {
        this.areaId2 = areaId2;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public BigDecimal getGoodsFreight() {
        return goodsFreight;
    }

    public void setGoodsFreight(BigDecimal goodsFreight) {
        this.goodsFreight = goodsFreight;
    }

    public int getFreightTemplateId() {
        return freightTemplateId;
    }

    public void setFreightTemplateId(int freightTemplateId) {
        this.freightTemplateId = freightTemplateId;
    }

    public BigDecimal getFreightWeight() {
        return freightWeight;
    }

    public void setFreightWeight(BigDecimal freightWeight) {
        this.freightWeight = freightWeight;
    }

    public BigDecimal getFreightVolume() {
        return freightVolume;
    }

    public void setFreightVolume(BigDecimal freightVolume) {
        this.freightVolume = freightVolume;
    }

    public int getAllowVat() {
        return allowVat;
    }

    public void setAllowVat(int allowVat) {
        this.allowVat = allowVat;
    }

    public int getFormatTop() {
        return formatTop;
    }

    public void setFormatTop(int formatTop) {
        this.formatTop = formatTop;
    }

    public int getFormatBottom() {
        return formatBottom;
    }

    public void setFormatBottom(int formatBottom) {
        this.formatBottom = formatBottom;
    }

    public int getIsCommend() {
        return isCommend;
    }

    public void setIsCommend(int isCommend) {
        this.isCommend = isCommend;
    }

    @Override
    public String toString() {
        return "GoodsCommon{" +
                "commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", jingle='" + jingle + '\'' +
                ", categoryId=" + categoryId +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", specJson='" + specJson + '\'' +
                ", goodsSpecValueJson='" + goodsSpecValueJson + '\'' +
                ", storeId=" + storeId +
                ", brandId=" + brandId +
                ", goodsBody='" + goodsBody + '\'' +
                ", mobileBody='" + mobileBody + '\'' +
                ", goodsState=" + goodsState +
                ", stateRemark='" + stateRemark + '\'' +
                ", goodsVerify=" + goodsVerify +
                ", verifyRemark='" + verifyRemark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", areaInfo='" + areaInfo + '\'' +
                ", goodsFreight=" + goodsFreight +
                ", freightTemplateId=" + freightTemplateId +
                ", freightWeight=" + freightWeight +
                ", freightVolume=" + freightVolume +
                ", allowVat=" + allowVat +
                ", formatTop=" + formatTop +
                ", formatBottom=" + formatBottom +
                ", isCommend=" + isCommend +
                '}';
    }
}
