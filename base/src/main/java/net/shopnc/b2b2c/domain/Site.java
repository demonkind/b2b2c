package net.shopnc.b2b2c.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 全局设置实体<br/>
 * Created by shopnc on 2015/11/6.
 */
@Entity
@Table(name = "site")
public class Site implements Serializable {
    /**
     * 配置项名称<br/>
     * 主键
     */
    @Id
    @Column(name = "title",unique = true)
    private String title;
    /**
     * 配置项值
     */
    @Column(name = "value",length = 500)
    private String value = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
