package net.shopnc.b2b2c.domain.store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 商家消息接收设置
 * Created by shopnc.feng on 2016-02-04.
 */
@Entity
@Table(name = "store_message_setting")
public class StoreMessageSetting implements Serializable {
    /**
     * 店铺编号
     */
    @Id
    @Column(name = "store_id")
    private int storeId;
    /**
     * 消息模板编号
     */
    @Id
    @Column(name = "tpl_code")
    private String tplCode;
    /**
     * 是否接收
     */
    @Column(name = "is_receive")
    private int isReceive;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    @Override
    public String toString() {
        return "StoreMessageSetting{" +
                "storeId=" + storeId +
                ", tplCode='" + tplCode + '\'' +
                ", isReceive=" + isReceive +
                '}';
    }
}
