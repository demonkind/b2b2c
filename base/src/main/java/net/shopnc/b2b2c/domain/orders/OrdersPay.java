package net.shopnc.b2b2c.domain.orders;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 支付单实体</br>
 * Created by shopnc on 2015/10/23.
 */
@Entity
@Table(name="orders_pay")
public class OrdersPay implements Serializable {
    /**
     * 编号</br>
     * 主键、自增
     */
    @Id
    @GeneratedValue
    @Column(name = "pay_id")
    private int payId;
    /**
     * 支付单号
     */
    @Column(name = "pay_sn")
    private long paySn;
    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private int memberId;
    /**
     * 支付状态</br>
     * 0-未支付 1-已支付
     */
//    @Column(name = "pay_state")
//    private int payState;

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

//    public int getPayState() {
//        return payState;
//    }
//
//    public void setPayState(int payState) {
//        this.payState = payState;
//    }

    @Override
    public String toString() {
        return "OrdersPay{" +
                "payId=" + payId +
                ", paySn=" + paySn +
                ", memberId=" + memberId +
//                ", payState=" + payState +
                '}';
    }
}
