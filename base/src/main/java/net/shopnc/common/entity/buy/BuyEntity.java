package net.shopnc.common.entity.buy;

import net.shopnc.b2b2c.vo.buy.BuyStoreVo;
import net.shopnc.common.util.PriceHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购买最后一步信息存储
 * Created by hbj on 2015/12/16.
 */
public class BuyEntity {
    /**
     * 店铺商品列表，以店铺分组
     */
    private List<BuyStoreVo> buyStoreVoList;

    private BuySecondPostDataEntity buySecondPostDataEntity;
    /**
     *
     * 平台促销信息
     * ...................
     *
     */
    /**
     * 各店铺订单金额累加
     */
    private BigDecimal buyAmount;

    /**
     * ************************************************************************
     * 构造函数 块
     * ************************************************************************
     */
    public BuyEntity(){}

    /**
     * ************************************************************************
     * Geter Seter toString 块
     * ************************************************************************
     */
    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public List<BuyStoreVo> getBuyStoreVoList() {
        return buyStoreVoList;
    }

    public void setBuyStoreVoList(List<BuyStoreVo> buyStoreVoList) {
        this.buyStoreVoList = buyStoreVoList;
    }

    public BuySecondPostDataEntity getBuySecondPostDataEntity() {
        return buySecondPostDataEntity;
    }

    public void setBuySecondPostDataEntity(BuySecondPostDataEntity buySecondPostDataEntity) {
        this.buySecondPostDataEntity = buySecondPostDataEntity;
    }

    @Override
    public String toString() {
        return "BuyEntity{" +
                "buyStoreVoList=" + buyStoreVoList +
                ", buySecondPostDataEntity=" + buySecondPostDataEntity +
                ", buyAmount=" + buyAmount +
                '}';
    }

    /**
     * ************************************************************************
     * 自定义类内使用函数 块
     * ************************************************************************
     */
    /**
     * 各店铺订单金额累加
     * @param buyStoreVoList
     */
    private void ncSetBuyAmount(List<BuyStoreVo> buyStoreVoList){
        for (int i=0; i<buyStoreVoList.size(); i++) {
            buyAmount = PriceHelper.add(buyAmount,buyStoreVoList.get(i).getBuyAmount());
        }
        buyAmount = PriceHelper.format(buyAmount);
    }

    /**
     * ************************************************************************
     * 自定义类外使用函数 块
     * ************************************************************************
     */
    public void ncSetBuyFirstData(List<BuyStoreVo> buyStoreVoList){
        this.buyStoreVoList = buyStoreVoList;
        ncSetBuyAmount(buyStoreVoList);
    }
    public void ncSetBuySecondData(List<BuyStoreVo> buyStoreVoList){
        this.buyStoreVoList = buyStoreVoList;
        ncSetBuyAmount(buyStoreVoList);
    }
    public void ncSetBuySecondData(List<BuyStoreVo> buyStoreVoList, BuySecondPostDataEntity buySecondPostDataEntity){
        this.buyStoreVoList = buyStoreVoList;
        this.buySecondPostDataEntity = buySecondPostDataEntity;
        ncSetBuyAmount(buyStoreVoList);
    }
}
