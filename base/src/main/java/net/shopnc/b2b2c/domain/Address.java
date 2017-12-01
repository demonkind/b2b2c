package net.shopnc.b2b2c.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 买家收货地址实体</br>
 * Created by shopnc on 2015/10/20.
 */
@Entity
@Table(name = "address")
public class Address implements Serializable {
    /**
     * 收货地址编号</br>
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Integer addressId;
    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private int memberId;
    /**
     * 联系人
     */
    @Size(min = 1)
    @Column(name = "real_name")
    private String realName;
    /**
     * 一级地区ID
     */
    @Column(name = "area_id_1")
    private int areaId1;
    /**
     * 二级地区ID
     */
    @Column(name = "area_id_2")
    private int areaId2;
    /**
     * 三级地区Id
     */
    @Column(name = "area_id_3")
    private int areaId3;
    /**
     * 四级地区
     */
    @Column(name = "area_id_4")
    private int areaId4;
    /**
     * 最末级地区
     */
    @Column(name = "area_id")
    private int areaId;
    /**
     * 省市县(区)内容
     */
    @Size(min = 1)
    @Column(name = "area_info")
    private String areaInfo;
    /**
     * 街道地址
     */
    @Size(min = 1)
    @Column(name = "address")
    private String address;
    /**
     * 手机
     */
    @Column(name = "mobphone")
    private String mobPhone;
    /**
     * 固话
     */
    @Column(name = "telphone")
    private String telPhone;
    /**
     * 是否作为默认收货地址</br>
     * 0-否 1-是
     */
    @Column(name = "is_default")
    private int isDefault;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public int getAreaId3() {
        return areaId3;
    }

    public void setAreaId3(int areaId3) {
        this.areaId3 = areaId3;
    }

    public int getAreaId4() {
        return areaId4;
    }

    public void setAreaId4(int areaId4) {
        this.areaId4 = areaId4;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobPhone() {
        return mobPhone;
    }

    public void setMobPhone(String mobPhone) {
        this.mobPhone = mobPhone;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", memberId=" + memberId +
                ", realName='" + realName + '\'' +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", areaId3=" + areaId3 +
                ", areaId4=" + areaId4 +
                ", areaId=" + areaId +
                ", areaInfo='" + areaInfo + '\'' +
                ", address='" + address + '\'' +
                ", mobPhone='" + mobPhone + '\'' +
                ", telPhone='" + telPhone + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
