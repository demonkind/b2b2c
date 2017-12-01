package net.shopnc.common.entity.buy;

import net.shopnc.b2b2c.constant.OrdersPaymentTypeCode;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.common.entity.buy.postjson.PostJsonGoodsEntity;
import net.shopnc.common.entity.buy.postjson.PostJsonStoreEntity;
import net.shopnc.common.entity.buy.postjson.PostJsonStoreListEntity;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 下单提交数据接收实体
 * Created by shopnc on 2015/12/13.
 */
public class BuySecondPostDataEntity {
    /**
     * 收货地址表Id
     */
    private Integer addressId;
    /**
     * 由addressId查表得到的收货地址信息
     */
    private Address address;
    /**
     * 线上支付/货到付款
     */
    private String paymentTypeCode;
    /**
     * 发票表Id
     */
    private int invoiceId;
    /**
     * 由invoiceId查表得到的发票信息
     */
    private String invoiceInfo;
    /**
     * 发票类型
     */
    private int invoiceType;
    /**
     * 购买留言
     */
    private String receiverMessage;
    /**
     * 处理后的购物车Id和购买数量HashMap<cartId,buyNum>[购物车下单时]
     */
    private HashMap<Integer,Integer> buyCartIdAndBuyNumList = new HashMap<Integer, Integer>();
    /**
     * 处理后的商品Id和购买数量HashMap<goodsId,buyNum>
     */
    private HashMap<Integer,Integer> buyGoodsIdAndBuyNumList = new HashMap<Integer, Integer>();
    /**
     * 处理后的购物车Id list[购物车下单时]
     */
    private List<Integer> cartIdList = new ArrayList<Integer>();
    /**
     * 处理后的商品Id list
     */
    private List<Integer> goodsIdList = new ArrayList<Integer>();
    /**
     * 处理后的买家留言 HashMap<storeId,message>
     */
    private HashMap<Integer,String> receiverMessageList = new HashMap<Integer, String>();
    /**
     * 会员Id
     */
    private int memberId;
    /**
     * 会员名称
     */
    private String memberName;
    /**
     * 订单来源
     */
    private String ordersFrom;
    /**
     * 是否来源于购物车
     */
    private int isCart;

    protected final Logger logger = Logger.getLogger(getClass());
    /**
     * ************************************************************************
     * 构造函数 块
     * ************************************************************************
     */
    public BuySecondPostDataEntity(){}
    public BuySecondPostDataEntity(PostJsonStoreListEntity postJsonStoreListEntity,
                                   Integer memberId,
                                   String memberName,
                                   String ordersFrom
    ) {
//        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        this.addressId = postJsonStoreListEntity.getAddressId();
        this.paymentTypeCode = postJsonStoreListEntity.getPaymentTypeCode() != null && postJsonStoreListEntity.getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE) ? OrdersPaymentTypeCode.OFFLINE : OrdersPaymentTypeCode.ONLINE;
        this.invoiceId = postJsonStoreListEntity.getInvoiceId() != null ? postJsonStoreListEntity.getInvoiceId() : 0;
        this.memberId = memberId;
        this.memberName = memberName;
        this.receiverMessageList = this.ncGetReceiverMessageList(postJsonStoreListEntity.getStoreList());
        this.ncParseBuyItemList(postJsonStoreListEntity);
        this.ordersFrom = ordersFrom;
        this.isCart = postJsonStoreListEntity.getIsCart() != null && postJsonStoreListEntity.getIsCart().equals(State.YES) ? State.YES: State.NO;
    }

    /**
     * ************************************************************************
     * Geter Seter toString 块
     * ************************************************************************
     */
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(String invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    public HashMap<Integer, Integer> getBuyCartIdAndBuyNumList() {
        return buyCartIdAndBuyNumList;
    }

    public void setBuyCartIdAndBuyNumList(HashMap<Integer, Integer> buyCartIdAndBuyNumList) {
        this.buyCartIdAndBuyNumList = buyCartIdAndBuyNumList;
    }

    public HashMap<Integer, Integer> getBuyGoodsIdAndBuyNumList() {
        return buyGoodsIdAndBuyNumList;
    }

    public void setBuyGoodsIdAndBuyNumList(HashMap<Integer, Integer> buyGoodsIdAndBuyNumList) {
        this.buyGoodsIdAndBuyNumList = buyGoodsIdAndBuyNumList;
    }

    public List<Integer> getCartIdList() {
        return cartIdList;
    }

    public void setCartIdList(List<Integer> cartIdList) {
        this.cartIdList = cartIdList;
    }

    public List<Integer> getGoodsIdList() {
        return goodsIdList;
    }

    public void setGoodsIdList(List<Integer> goodsIdList) {
        this.goodsIdList = goodsIdList;
    }

    public HashMap<Integer, String> getReceiverMessageList() {
        return receiverMessageList;
    }

    public void setReceiverMessageList(HashMap<Integer, String> receiverMessageList) {
        this.receiverMessageList = receiverMessageList;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getOrdersFrom() {
        return ordersFrom;
    }

    public void setOrdersFrom(String ordersFrom) {
        this.ordersFrom = ordersFrom;
    }

    public int getIsCart() {
        return isCart;
    }

    public void setIsCart(int isCart) {
        this.isCart = isCart;
    }

    @Override
    public String toString() {
        return "BuySecondPostDataEntity{" +
                "addressId=" + addressId +
                ", address=" + address +
                ", paymentTypeCode='" + paymentTypeCode + '\'' +
                ", invoiceId=" + invoiceId +
                ", invoiceInfo='" + invoiceInfo + '\'' +
                ", invoiceType=" + invoiceType +
                ", receiverMessage='" + receiverMessage + '\'' +
                ", buyCartIdAndBuyNumList=" + buyCartIdAndBuyNumList +
                ", buyGoodsIdAndBuyNumList=" + buyGoodsIdAndBuyNumList +
                ", cartIdList=" + cartIdList +
                ", goodsIdList=" + goodsIdList +
                ", receiverMessageList=" + receiverMessageList +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", ordersFrom='" + ordersFrom + '\'' +
                ", isCart=" + isCart +
                '}';
    }
/**
     * ************************************************************************
     * 自定义类内使用函数 块
     * ************************************************************************
     */

    /**
     * 解析处理购物车Id/商品Id及数量 类内公用
     * @param postJsonStoreListEntity
     * @return
     */
    private void ncParseBuyItemList(PostJsonStoreListEntity postJsonStoreListEntity) {
//        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        for (int i=0; i<postJsonStoreListEntity.getStoreList().size(); i++) {
	        	if(null!=postJsonStoreListEntity.getStoreList().get(i).getGoodsList()){
		            for(int k=0; k<postJsonStoreListEntity.getStoreList().get(i).getGoodsList().size(); k++) {
		                PostJsonGoodsEntity goodsEntity = postJsonStoreListEntity.getStoreList().get(i).getGoodsList().get(k);
		                if (postJsonStoreListEntity.getIsCart() != null && postJsonStoreListEntity.getIsCart().equals(1)) {
		                    this.buyCartIdAndBuyNumList.put(goodsEntity.getCartId(),goodsEntity.getBuyNum());
		                    this.cartIdList.add(goodsEntity.getCartId());
		                }
		                if (goodsEntity.getGoodsId() > 0) {
		                    this.buyGoodsIdAndBuyNumList.put(goodsEntity.getGoodsId(),goodsEntity.getBuyNum());
		                    this.goodsIdList.add(goodsEntity.getGoodsId());
		                }
		            }
	        	}
        }
    }

    /**
     * 得到买家留言
     * @param postJsonStoreEntityList
     * @return
     */
    private HashMap<Integer,String> ncGetReceiverMessageList(List<PostJsonStoreEntity> postJsonStoreEntityList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        HashMap<Integer,String> hashMap = new HashMap<Integer, String>();
        for (int i=0; i<postJsonStoreEntityList.size(); i++) {
            hashMap.put(postJsonStoreEntityList.get(i).getStoreId(),postJsonStoreEntityList.get(i).getReceiverMessage());
        }
        return hashMap;
    }
}
