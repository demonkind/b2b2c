package net.shopnc.b2b2c.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 商品评价<br>
 * Created by zxy on 2015-10-26
 */
@Entity
@Table(name = "evaluate_goods")
public class EvaluateGoods implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="evaluate_id")
    private int evaluateId;
    /**
     * 订单编码
     */
    @Column(name="order_id")
    private int orderId;
    /**
     * 订单商品编号
     */
    @Column(name="ordergoods_id")
    private int orderGoodsId;
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
     * 评分1-5
     */
    @Min(1)
    @Max(5)
    @Column(name="scores")
    private int scores;
    /**
     * 是否匿名评价 0不匿名 1匿名
     */
    @Column(name="is_anonymous")
    private int isAnonymous;
    /**
     * 评价时间
     */
    @Column(name="evaluate_time")
    private Timestamp evaluateTime;
    /**
     * 评价人编号
     */
    @Column(name="from_member_id")
    private int fromMemberId;
    /**
     * 评价次数，用于判断是否可以继续追加评价
     */
    @Column(name="evaluate_num")
    private int evaluateNum;
    
    /**
     * 评价时是否含有图片0-否 1-是
     */
    @Column(name="has_image")
    private Integer hasImage=0;

    public Integer getHasImage() {
		return hasImage;
	}

	public void setHasImage(Integer hasImage) {
		this.hasImage = hasImage;
	}

	public int getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderGoodsId() {
        return orderGoodsId;
    }

    public void setOrderGoodsId(int orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
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

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public Timestamp getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Timestamp evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public int getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(int fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public int getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(int evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

}
