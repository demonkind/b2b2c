package net.shopnc.b2b2c.vo.buy.freight;

import java.math.BigDecimal;

/**
 * 配送规则Vo[单个商品]
 * Created by hbj on 2015/12/29.
 */
public class BuyGoodsItemFreightVo {
    private Integer goodsId;
    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;
    /**
     * 商品运费
     */
    private BigDecimal goodsFreightAmount;
    /**
     * 是否允许配送
     */
    private Integer allowSend;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getGoodsFreightAmount() {
        return goodsFreightAmount;
    }

    public void setGoodsFreightAmount(BigDecimal goodsFreightAmount) {
        this.goodsFreightAmount = goodsFreightAmount;
    }

    public Integer getAllowSend() {
        return allowSend;
    }

    public void setAllowSend(Integer allowSend) {
        this.allowSend = allowSend;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    @Override
    public String toString() {
        return "BuyGoodsItemFreightVo{" +
                "goodsId=" + goodsId +
                ", goodsAmount=" + goodsAmount +
                ", goodsFreightAmount=" + goodsFreightAmount +
                ", allowSend=" + allowSend +
                '}';
    }
}
