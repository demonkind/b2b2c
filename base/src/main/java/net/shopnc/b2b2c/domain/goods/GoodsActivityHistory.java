package net.shopnc.b2b2c.domain.goods;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/***
 * 商品与活动绑定关系的实体历史记录表
 * 
 * */
@Entity
@Table(name = "goods_activity_history")
public class GoodsActivityHistory implements Serializable {
	
	/***
	 * ID
	 * **/
	@Id
    @GeneratedValue
    @Column(name = "id")
	private int Id;
	
	
	/***
	 * 商品活动ID
	 * 
	 * */
    @Column(name = "goods_activity_id")
	private int goodsActivityId;

	/***
	 * 商店ID
	 * 
	 * */
	@Column(name = "store_id")
	private int storeId;
	
	/***
	 * 商品ID
	 * 
	 * */
	@Column(name = "common_id")
	private int commonId;
	
	/***
	 * 活动ID
	 * 
	 * */
	@Column(name = "activity_id")
	private String activityId;
	
	/***
	 * 活动类型：0表示0元购，1表示预售，2表示0元够+预售
	 * 
	 * */
	@Column(name = "activity_type")
	private String activityType;

	/***
	 * 活动开始时间
	 * 
	 * */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	@Column(name = "start_time")
	private Timestamp StartTime;
	/***
	 * 活动结束时间
	 * 
	 * */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	@Column(name = "end_time")
	private Timestamp endTime;
	/***
	 * 份数
	 * 
	 * */
	@Column(name = "weight")
	private String weight;
	/***
	 * 限购数量
	 * 
	 * */
	@Column(name = "max_weight")
	private String maxNum;
	/***
	 * 卡卷类型
	 * 
	 * */
	@Column(name = "card_type")
	private String cartType;
	/***
	 * 发送K码时间 0：在线支付时 1：确定收货时
	 * 
	 * */
	@Column(name = "send_K_code")
	private String sendKCodeType;
	/***
	 * 返回金额
	 * 
	 * */
	@Column(name = "retrun_amount")
	private String returnAmount;
	/***
	 * 描述
	 * 
	 * */
	@Column(name = "description")
	private String description;
	
	public int getGoodsActivityId() {
		return goodsActivityId;
	}
	public void setGoodsActivityId(int goodsActivityId) {
		this.goodsActivityId = goodsActivityId;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	
	public int getCommonId() {
		return commonId;
	}
	public void setCommonId(int commonId) {
		this.commonId = commonId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public Timestamp getStartTime() {
		return StartTime;
	}
	public void setStartTime(Timestamp startTime) {
		StartTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(String maxNum) {
		this.maxNum = maxNum;
	}
	public String getCartType() {
		return cartType;
	}
	public void setCartType(String cartType) {
		this.cartType = cartType;
	}
	public String getSendKCodeType() {
		return sendKCodeType;
	}
	public void setSendKCodeType(String sendKCodeType) {
		this.sendKCodeType = sendKCodeType;
	}
	public String getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(String returnAmount) {
		this.returnAmount = returnAmount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	
	

}
