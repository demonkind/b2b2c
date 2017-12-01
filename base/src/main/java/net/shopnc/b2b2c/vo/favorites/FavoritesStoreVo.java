package net.shopnc.b2b2c.vo.favorites;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.domain.member.FavoritesStore;
import net.shopnc.b2b2c.domain.store.Store;

import java.sql.Timestamp;

/**
 * Created by zxy on 2016-01-20
 */
public class FavoritesStoreVo {
    /**
     * 自增编码
     */
    private int favoritesId;
    /**
     * 会员编码
     */
    private int memberId = 0;
    /**
     * 店铺编码
     */
    private int storeId = 0;
    /**
     * 关注时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp addTime;
    /**
     * 店铺详情
     */
    private Store store;

    public FavoritesStoreVo(FavoritesStore favStore, Store store) {
        this.favoritesId = favStore.getFavoritesId();
        this.memberId = favStore.getMemberId();
        this.storeId = favStore.getStoreId();
        this.addTime = favStore.getAddTime();
        this.store = store;
    }

    public int getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(int favoritesId) {
        this.favoritesId = favoritesId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
