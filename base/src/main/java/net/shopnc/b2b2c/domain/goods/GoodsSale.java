package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * 商品销售实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "goods_sale")
public class GoodsSale implements Serializable {
    /**
     * 商品SKU编号
     */
    @Id
    @Column(name = "goods_id")
    private int goodsId;
    /**
     * 销售数量
     */
    @Column(name = "goods_sale_num")
    private int goodsSaleNum = 0;
    /**
     * 库存
     */
    @Column(name = "goods_storage")
    private int goodsStorage;
    /**
     * 库存预警
     */
    @Column(name = "goods_storage_alarm")
    private int goodsStorageAlarm;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getGoodsStorageAlarm() {
        return goodsStorageAlarm;
    }

    public void setGoodsStorageAlarm(int goodsStorageAlarm) {
        this.goodsStorageAlarm = goodsStorageAlarm;
    }

    @Override
    public String toString() {
        return "GoodsSale{" +
                "goodsId=" + goodsId +
                ", goodsSaleNum=" + goodsSaleNum +
                ", goodsStorage=" + goodsStorage +
                ", goodsStorageAlarm=" + goodsStorageAlarm +
                '}';
    }
}
