package net.shopnc.b2b2c.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.shopnc.b2b2c.constant.State;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * 运费模板实体<br/>
 * Created by hbj on 2016/1/20.
 */
@Entity
@Table(name = "freight_template")
public class FreightTemplate {
    /**
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "freight_id")
    private int freightId;
    /**
     * 标题
     */
    @Size(min = 1)
    @Column(name = "title")
    private String title;
    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private int storeId;
    /**
     * 最后编辑时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name = "edit_time")
    private Timestamp editTime;
    /**
     * 计价方式 按体积、重量、件数
     */
    @Column(name = "calc_type")
    private String calcType;
    /**
     * 是否包邮 1-是，0-否
     */
    @Column(name = "freight_free")
    private int freightFree = State.NO;

    public int getFreightId() {
        return freightId;
    }

    public void setFreightId(int freightId) {
        this.freightId = freightId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getEditTime() {
        return editTime;
    }

    public void setEditTime(Timestamp editTime) {
        this.editTime = editTime;
    }

    public String getCalcType() {
        return calcType;
    }

    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public int getFreightFree() {
        return freightFree;
    }

    public void setFreightFree(int freightFree) {
        this.freightFree = freightFree;
    }

    @Override
    public String toString() {
        return "FreightTemplate{" +
                "freightId=" + freightId +
                ", title='" + title + '\'' +
                ", storeId=" + storeId +
                ", editTime=" + editTime +
                ", calcType='" + calcType + '\'' +
                ", freightFree=" + freightFree +
                '}';
    }
}
