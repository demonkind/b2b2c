package net.shopnc.b2b2c.domain.store;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 相册文件实体<br>
 * Created by shopnc.feng on 2015-10-23.
 */
@Entity
@Table(name = "album_files")
public class AlbumFiles implements Serializable {
    /**
     * 文件编号
     */
    @Id
    @GeneratedValue
    @Column(name = "files_id")
    private int filesId;
    /**
     * 文件名称
     */
    @Column(name = "files_name")
    private String filesName;
    /**
     * 店铺编号
     */
    @Column(name = "store_id")
    private int storeId;
    /**
     * 相册编号
     */
    @Column(name = "album_id")
    private int albumId = 0;
    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "upload_time")
    private Timestamp uploadTime;
    /**
     * 图片类型
     */
    @Column(name = "album_type")
    private int filesType;
    /**
     * 高
     */
    @Column(name = "files_height")
    private int filesHeight = 0;

    /**
     * 宽
     */
    @Column(name = "files_width")
    private int filesWidth = 0;

    /**
     * 大小<br>
     *     单位：字节
     */
    @Column(name = "files_size")
    private long filesSize = 0;

    public int getFilesId() {
        return filesId;
    }

    public void setFilesId(int filesId) {
        this.filesId = filesId;
    }

    public String getFilesName() {
        return filesName;
    }

    public void setFilesName(String filesName) {
        this.filesName = filesName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getFilesType() {
        return filesType;
    }

    public void setFilesType(int filesType) {
        this.filesType = filesType;
    }

    public int getFilesHeight() {
        return filesHeight;
    }

    public void setFilesHeight(int filesHeight) {
        this.filesHeight = filesHeight;
    }

    public int getFilesWidth() {
        return filesWidth;
    }

    public void setFilesWidth(int filesWidth) {
        this.filesWidth = filesWidth;
    }

    public long getFilesSize() {
        return filesSize;
    }

    public void setFilesSize(long filesSize) {
        this.filesSize = filesSize;
    }

    @Override
    public String toString() {
        return "AlbumFiles{" +
                "filesId=" + filesId +
                ", filesName='" + filesName + '\'' +
                ", storeId=" + storeId +
                ", albumId=" + albumId +
                ", uploadTime=" + uploadTime +
                ", filesType=" + filesType +
                ", filesHeight=" + filesHeight +
                ", filesWidth=" + filesWidth +
                ", filesSize=" + filesSize +
                '}';
    }
}
