package net.shopnc.b2b2c.domain.goods;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 关联板式实体<br>
 * Created by shopnc.feng on 2015-12-16.
 */
@Entity
@Table(name = "format_template")
public class FormatTemplate implements Serializable {
    /**
     * 关联板式编号
     */
    @Id
    @GeneratedValue
    @Column(name = "format_id")
    private int formatId;
    /**
     * 关联版式名称
     */
    @Length(min = 1, max = 10)
    @Column(name = "format_name")
    private String formatName;
    /**
     * 关联板式位置
     */
    @Min(0)
    @Max(1)
    @Column(name = "format_site")
    private int formatSite;
    /**
     * 管理板式内容
     */
    @Lob
    @Column(name = "format_content")
    private String formatContent;
    /**
     * 店编编号
     */
    @Column(name = "store_id")
    private int storeId;

    public int getFormatId() {
        return formatId;
    }

    public void setFormatId(int formatId) {
        this.formatId = formatId;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public int getFormatSite() {
        return formatSite;
    }

    public void setFormatSite(int formatSite) {
        this.formatSite = formatSite;
    }

    public String getFormatContent() {
        return formatContent;
    }

    public void setFormatContent(String formatContent) {
        this.formatContent = formatContent;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "FormatTemplate{" +
                "formatId=" + formatId +
                ", formatName='" + formatName + '\'' +
                ", formatSite=" + formatSite +
                ", formatContent='" + formatContent + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
