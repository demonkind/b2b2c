package net.shopnc.b2b2c.domain.member;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品咨询分类<br>
 * Created by zxy on 2016-01-12
 */
@Entity
@Table(name = "consult_class")
public class ConsultClass implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="class_id")
    private int classId;
    /**
     * 分类名称
     */
    @Column(name="class_name", length = 100)
    private String className = "";
    /**
     * 分类介绍
     */
    @Column(name="introduce", length = 5000)
    private String introduce = "";
    /**
     * 排序
     */
    @Column(name="class_sort")
    private int classSort = 0;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getClassSort() {
        return classSort;
    }

    public void setClassSort(int classSort) {
        this.classSort = classSort;
    }
}
