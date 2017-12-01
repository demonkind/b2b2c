package net.shopnc.b2b2c.domain.store;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 店铺标签与商品关系实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "store_label_goods")
public class StoreLabelGoods implements Serializable {
    /**
     * 商品SPU编号
     */
    @Id
    @Column(name = "common_id")
    private int commonId;
    /**
     * 店铺标签编号
     */
    @Id
    @Column(name = "store_label_id")
    private int storeLabelId;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getStoreLabelId() {
        return storeLabelId;
    }

    public void setStoreLabelId(int storeLabelId) {
        this.storeLabelId = storeLabelId;
    }

    @Override
    public String toString() {
        return "StoreLabelGoods{" +
                "commonId=" + commonId +
                ", storeLabelId=" + storeLabelId +
                '}';
    }
}
