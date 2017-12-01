package net.shopnc.b2b2c.domain.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.constant.OrdersOrdersStateName;

/**
 * 卡券信息
 * @author LBS666bb
 *
 */
@Entity
@Table(name = "kcode")
public class Coupons implements Serializable{
	
	/**
     * 卡券自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="coupons_id")
    private int couponsId;
    /**
     * 用户ID
     */
    @Column(name="member_id")
    private int memberId;
    /**
     * 订单ID
     */
    @Column(name="orders_id")
    private int ordersId;
    /**
     * 商品ID
     */
    @Column(name="goods_id")
    private int goodsId;
    /**
     * 店铺ID
     */
    @Column(name="store_id")
    private int storeId;
    /**
     * 卡券领取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @Column(name="create_time")
    private Timestamp createTime;
    /**
     * 卡号
     */
    @Column(name="code_key")
    private String codeKey;
    /**
     * K码
     */
    @Column(name="k_code")
    private String kCode;
    /**
     * 卡券可抵用金额
     */
    @Column(name="amount_money")
    private BigDecimal amountMoney;
    
    /**
     * 卡券到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @Column(name="due_time")
    private Timestamp dueTime;
    /**
     * 卡券状态。“1”：未使用；“0”：已失效。
     */
    @Column(name="is_useful")
    private int isUseful;
	public int getCouponsId() {
		return couponsId;
	}
	public void setCouponsId(int couponsId) {
		this.couponsId = couponsId;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public int getOrdersId() {
		return ordersId;
	}
	public void setOrdersId(int ordersId) {
		this.ordersId = ordersId;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
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
	public String getCodeKey() {
		return codeKey;
	}
	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}
	public String getkCode() {
		return kCode;
	}
	public void setkCode(String kCode) {
		this.kCode = kCode;
	}
	public BigDecimal getAmountMoney() {
		return amountMoney;
	}
	public void setAmountMoney(BigDecimal amountMoney) {
		this.amountMoney = amountMoney;
	}
	
	public Timestamp getDueTime() {
		return dueTime;
	}
	public void setDueTime(Timestamp dueTime) {
		this.dueTime = dueTime;
	}
	public int getIsUseful() {
		return isUseful;
	}
	public void setIsUseful(int isUseful) {
		this.isUseful = isUseful;
	}
	public Coupons() {
		super();
	}
	public Coupons(int couponsId, int memberId, int ordersId, int goodsId, int storeId, Timestamp createTime,
			String codeKey, String kCode, BigDecimal amountMoney, Timestamp dueTime, int isUseful) {
		super();
		this.couponsId = couponsId;
		this.memberId = memberId;
		this.ordersId = ordersId;
		this.goodsId = goodsId;
		this.storeId = storeId;
		this.createTime = createTime;
		this.codeKey = codeKey;
		this.kCode = kCode;
		this.amountMoney = amountMoney;
		this.dueTime = dueTime;
		this.isUseful = isUseful;
	}

}
