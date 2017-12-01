package net.shopnc.b2b2c.vo.buy.freight;

import java.math.BigDecimal;
import java.util.List;

/**
 * 配送规则Vo[店铺列表]
 * Created by hbj on 2015/12/29.
 */
public class BuyStoreFreightListVo {
    /**
     * 应付总金额
     */
    private BigDecimal buyAmount;
    /**
     * 店铺列表
     */
    private List<BuyStoreFreightVo> storeList;

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public List<BuyStoreFreightVo> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<BuyStoreFreightVo> storeList) {
        this.storeList = storeList;
    }

    @Override
    public String toString() {
        return "BuyStoreFreightListVo{" +
                "buyAmount=" + buyAmount +
                ", storeList=" + storeList +
                '}';
    }
}
