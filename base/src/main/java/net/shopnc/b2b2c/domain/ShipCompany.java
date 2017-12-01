package net.shopnc.b2b2c.domain;


import org.jboss.logging.annotations.Message;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 快递公司实体</br>
 * Created by shopnc on 2015/10/21.
 */
@Entity
@Table(name = "ship_company")
public class ShipCompany implements Serializable {
    /**
     * 快递公司编号</br>
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "ship_id")
    private int shipId;
    /**
     * 公司名称
     */
    @Column(name = "ship_name")
    private String shipName;
    /**
     * 是否启用</br>
     * 1-是 0-否
     */
    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "ship_state")
    private int shipState;
    /**
     * 公司英文代码
     */
    @Size(min = 2, max = 15)
    @Column(name = "ship_code")
    private String shipCode;
    /**
     * 公司名称拼音首字母(大写)
     */
    @Column(name = "ship_letter")
    private Character shipLetter;
    /**
     * 是否经常使用
     * 0-否 1-是
     */
    @Min(0)
    @Max(1)
    @Column(name = "ship_type")
    private int shipType;
    /**
     * 公司网址
     */
    @Column(name = "ship_url")
    private String shipUrl;

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public int getShipState() {
        return shipState;
    }

    public void setShipState(int shipState) {
        this.shipState = shipState;
    }
    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public Character getShipLetter() {
        return shipLetter;
    }

    public void setShipLetter(Character shipLetter) {
        this.shipLetter = shipLetter;
    }

    public int getShipType() {
        return shipType;
    }

    public void setShipType(int shipType) {
        this.shipType = shipType;
    }

    public String getShipUrl() {
        return shipUrl;
    }

    public void setShipUrl(String shipUrl) {
        shipUrl = shipUrl;
    }

    @Override
    public String toString() {
        return "ShipCompany{" +
                "shipId=" + shipId +
                ", shipName='" + shipName + '\'' +
                ", shipState=" + shipState +
                ", shipCode='" + shipCode + '\'' +
                ", shipLetter=" + shipLetter +
                ", shipType=" + shipType +
                ", shipUrl='" + shipUrl + '\'' +
                '}';
    }
}
