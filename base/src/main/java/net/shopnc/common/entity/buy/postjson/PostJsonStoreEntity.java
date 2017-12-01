package net.shopnc.common.entity.buy.postjson;

import java.util.List;

/**
 * Created by hbj on 2015/12/29.
 */
public class PostJsonStoreEntity {
    private Integer storeId;
    private List<PostJsonGoodsEntity> goodsList;
    private String receiverMessage;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public List<PostJsonGoodsEntity> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<PostJsonGoodsEntity> goodsList) {
        this.goodsList = goodsList;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    @Override
    public String toString() {
        return "PostJsonStoreEntity{" +
                "storeId=" + storeId +
                ", goodsList=" + goodsList +
                ", receiverMessage='" + receiverMessage + '\'' +
                '}';
    }
}
