package net.shopnc.b2b2c.domain.store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 商户子账号接受设置
 * Created by shopnc.feng on 2016-02-04.
 */
@Entity
@Table(name = "store_message_seller")
public class StoreMessageSeller implements Serializable {
    /**
     * 卖家编号
     */
    @Id
    @Column(name = "sellerId")
    private int sellerId;
    /**
     * 消息模板编码
     */
    @Id
    @Column(name = "tpl_code")
    private String tplCode;
    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private int storeId;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "StoreMessageSeller{" +
                "sellerId=" + sellerId +
                ", tplCode='" + tplCode + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
