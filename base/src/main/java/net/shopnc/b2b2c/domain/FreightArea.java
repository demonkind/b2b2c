package net.shopnc.b2b2c.domain;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 运费模板详细价格及地区设置实体<br/>
 * Created by hbj on 2016/1/20.
 */
@Entity
@Table(name = "freight_area")
public class FreightArea {
    /**
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "freight_area_id")
    private int freightAreaId;
    /**
     * 运费模板Id
     */
    @Column(name = "freight_id")
    private int freightId;
    /**
     * 第三级地区ID组成的串，以英文","隔开，两端也有","
     */
    @Lob
    @Column(name = "area_id")
    private String areaId = "";
    /**
     * 地区name组成的串，以英文","隔开
     */
    @Lob
    @Column(name = "area_name")
    private String areaName = "";
    /**
     * 首[件数、重量、体积]
     */
    @Column(name = "item1")
    private BigDecimal item1 = new BigDecimal(0);
    /**
     * 价格
     */
    @Column(name = "price1")
    private BigDecimal price1 = new BigDecimal(0);
    /**
     * 续[件数、重量、体积]
     */
    @Column(name = "item2")
    private BigDecimal item2 = new BigDecimal(0);
    /**
     * 价格
     */
    @Column(name = "price2")
    private BigDecimal price2 = new BigDecimal(0);

    public int getFreightAreaId() {
        return freightAreaId;
    }

    public void setFreightAreaId(int freightAreaId) {
        this.freightAreaId = freightAreaId;
    }

    public int getFreightId() {
        return freightId;
    }

    public void setFreightId(int freightId) {
        this.freightId = freightId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getItem1() {
        return item1;
    }

    public void setItem1(BigDecimal item1) {
        this.item1 = item1;
    }

    public BigDecimal getItem2() {
        return item2;
    }

    public void setItem2(BigDecimal item2) {
        this.item2 = item2;
    }

    @Override
    public String toString() {
        return "FreightArea{" +
                "freightAreaId=" + freightAreaId +
                ", freightId=" + freightId +
                ", areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", item1=" + item1 +
                ", price1=" + price1 +
                ", item2=" + item2 +
                ", price2=" + price2 +
                '}';
    }
}
