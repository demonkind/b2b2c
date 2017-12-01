package net.shopnc.common.entity.buy.postjson;

import java.util.List;

/**
 * Created by hbj on 2015/12/29.
 */
public class PostJsonStoreListEntity {
    private Integer addressId;
    private String paymentTypeCode;
    private Integer invoiceId;
    private Integer isCart;

    private List<PostJsonStoreEntity> storeList;

    public List<PostJsonStoreEntity> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<PostJsonStoreEntity> storeList) {
        this.storeList = storeList;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getIsCart() {
        return isCart;
    }

    public void setIsCart(Integer isCart) {
        this.isCart = isCart;
    }

    @Override
    public String toString() {
        return "PostJsonStoreListEntity{" +
                ", addressId=" + addressId +
                ", paymentTypeCode='" + paymentTypeCode + '\'' +
                ", invoiceId=" + invoiceId +
                ", isCart=" + isCart +
                ", storeList=" + storeList +
                '}';
    }
}
