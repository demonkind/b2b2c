package net.shopnc.b2b2c.vo.buy;

import net.shopnc.b2b2c.constant.OrdersPaymentTypeCode;
import net.shopnc.common.util.PriceHelper;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;

/**
 * 存放下单时单个店铺的购买信息<br/>
 * Created by hbj on 2015/11/27.
 */
public class BuyStoreVo {
    /**
     * 店铺下商品列表集合
     */
    private List<BuyGoodsItemVo> buyGoodsItemVoList;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺Id
     */
    private int storeId;
    /**
     * 店铺商品总价[不含运费]
     */
    private BigDecimal buyItemAmount;
    /**
     * 店铺最终运费
     */
    private BigDecimal storeFreightAmount;
    /**
     * 店铺代金券列表[类型待定..]
     */
    private List<Object> voucherList;
    /**
     * 线上支付/货到付款[offline/online]
     */
    private String paymentTypeCode = OrdersPaymentTypeCode.ONLINE;
    /**
     * 店铺金额小计[店铺商品总价 + 店铺运费]
     */
    private BigDecimal buyAmount;
    /**
     * 是否自营
     */
    private int isOwnShop;

    protected final Logger logger = Logger.getLogger(getClass());
    /**
     * ************************************************************************
     * 构造函数 块
     * ************************************************************************
     */
    public BuyStoreVo(){}
    public BuyStoreVo(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        this.storeId = buyGoodsItemVoList.get(0).getStoreId();
        this.buyGoodsItemVoList = buyGoodsItemVoList;
        this.storeName = buyGoodsItemVoList.get(0).getStoreName();
        this.ncSetBuyItemAmount(buyGoodsItemVoList);
        this.isOwnShop = buyGoodsItemVoList.get(0).getIsOwnShop();
    }

    /**
     * ************************************************************************
     * Geter Seter toString 块
     * ************************************************************************
     */
    public List<BuyGoodsItemVo> getBuyGoodsItemVoList() {
        return buyGoodsItemVoList;
    }

    public void setBuyGoodsItemVoList(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        this.buyGoodsItemVoList = buyGoodsItemVoList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public BigDecimal getBuyItemAmount() {
        return buyItemAmount;
    }

    public void setBuyItemAmount(BigDecimal buyItemAmount) {
        this.buyItemAmount = buyItemAmount;
    }

    public BigDecimal getStoreFreightAmount() {
        return storeFreightAmount;
    }

    public void setStoreFreightAmount(BigDecimal storeFreightAmount) {
        this.storeFreightAmount = storeFreightAmount;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public List<Object> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<Object> voucherList) {
        this.voucherList = voucherList;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    @Override
    public String toString() {
        return "BuyStoreVo{" +
                "buyGoodsItemVoList=" + buyGoodsItemVoList +
                ", storeName='" + storeName + '\'' +
                ", storeId=" + storeId +
                ", buyItemAmount=" + buyItemAmount +
                ", storeFreightAmount=" + storeFreightAmount +
                ", voucherList=" + voucherList +
                ", paymentTypeCode='" + paymentTypeCode + '\'' +
                ", buyAmount=" + buyAmount +
                ", isOwnShop=" + isOwnShop +
                '}';
    }

    /**
     * 设置 店铺商品总价[不含运费]
     * @param buyGoodsItemVoList
     * @return
     */
    private void ncSetBuyItemAmount(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        if (buyGoodsItemVoList.size() > 0) {
            for (int i = 0; i < buyGoodsItemVoList.size(); i++) {
                this.buyItemAmount = PriceHelper.add(this.buyItemAmount, buyGoodsItemVoList.get(i).getItemAmount());
            }
            this.buyItemAmount = PriceHelper.format(this.buyItemAmount);
        }
    }

}
