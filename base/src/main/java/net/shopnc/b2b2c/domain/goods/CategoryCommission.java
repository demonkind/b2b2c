package net.shopnc.b2b2c.domain.goods;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 商品分类绑定佣金实体
 * Created by hbj on 2015/12/14.
 */
@Entity
@Table(name = "category_commission")
public class CategoryCommission implements Serializable {


    /**
     * 主键 自增
     */
    @Id
    @GeneratedValue
    @Column(name = "commission_id")
    private int commissionId;
    /**
     * 商品分类
     */
    @Column(name = "category_id")
    private int categoryId;
    /**
     * 佣金比例
     */
    @Min(0)
    @Max(100)
    @Column(name = "commission_rate")
    private int commissionRate;


    public int getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(int commissionId) {
        this.commissionId = commissionId;
    }
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Override
    public String toString() {
        return "CategoryCommission{" +
                "commissionId=" + commissionId +
                ", categoryId=" + categoryId +
                ", commissionRate=" + commissionRate +
                '}';
    }
}
