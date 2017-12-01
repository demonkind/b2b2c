package net.shopnc.b2b2c.domain.store;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 相册实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "album")
public class Album implements Serializable {
    /**
     * 相册编号
     */
    @Id
    @GeneratedValue
    @Column(name = "album_id")
    private int albumId;
    /**
     * 相册名称
     */
    @Column(name = "album_name")
    private int albumName;
    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private int storeId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "create_time")
    private Timestamp createTime;
    /**
     * 默认相册<br>
     * 1是 0否<br>
     * 开店时创建一个默认相册，且不能删除
     */
    @Column(name = "is_default")
    private int isDefault;

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getAlbumName() {
        return albumName;
    }

    public void setAlbumName(int albumName) {
        this.albumName = albumName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "StoreAlbum{" +
                "albumId=" + albumId +
                ", albumName=" + albumName +
                ", storeId=" + storeId +
                ", createTime=" + createTime +
                ", isDefault=" + isDefault +
                '}';
    }
}
