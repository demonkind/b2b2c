package net.shopnc.b2b2c.vo.goods;

import java.util.List;

/**
 * Created by shopnc.feng on 2015-12-04.
 */
public class GoodsValueJsonVo {

    private int goodsId;
    private String specValueIds;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }

    @Override
    public String toString() {
        return "GoodsSpecValueJsonVo{" +
                "goodsId=" + goodsId +
                ", specValueIds='" + specValueIds + '\'' +
                '}';
    }
}
