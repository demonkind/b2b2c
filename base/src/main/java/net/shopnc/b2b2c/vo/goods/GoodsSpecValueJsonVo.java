package net.shopnc.b2b2c.vo.goods;

import java.util.List;

/**
 * Created by shopnc.feng on 2015-12-04.
 */
public class GoodsSpecValueJsonVo {

    private int goodsId;
    private List<Integer> specValueIds;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public List<Integer> getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(List<Integer> specValueIds) {
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
