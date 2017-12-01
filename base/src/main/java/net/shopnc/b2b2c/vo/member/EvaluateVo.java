package net.shopnc.b2b2c.vo.member;

import java.util.ArrayList;
import java.util.List;

public class EvaluateVo {
	private String orderId;//订单号
	private String storeId;//商铺ID
	private List<String> orderGoodsIdList=new ArrayList<String>();//订单商品编号
	private List<String> goodsIdList=new ArrayList<String>();//商品ID
	private List<String> isAnonymousList=new ArrayList<String>();//是否匿名
	private List<String> contentList=new ArrayList<String>();//评价内容
	private List<String> scoreList=new ArrayList<String>();//评分
	private List<String> imageList=new ArrayList<String>();//图片
	
	private String memberId;
	private String isOwnShop;//是否是自营 0-否 1-是
	private String descriptionCredit;//描述相符评分
	private String serviceCredit;//服务态度评分
	private String deliveryCredit;//发货速度评分
	private List<String> evaluateIdList=new ArrayList<String>();
	
	private String memberName;
	
	
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public List<String> getEvaluateIdList() {
		return evaluateIdList;
	}
	public void setEvaluateIdList(List<String> evaluateIdList) {
		this.evaluateIdList = evaluateIdList;
	}
	public List<String> getImageList() {
		return imageList;
	}
	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
	public String getDescriptionCredit() {
		return descriptionCredit;
	}
	public void setDescriptionCredit(String descriptionCredit) {
		this.descriptionCredit = descriptionCredit;
	}
	public String getServiceCredit() {
		return serviceCredit;
	}
	public void setServiceCredit(String serviceCredit) {
		this.serviceCredit = serviceCredit;
	}
	public String getDeliveryCredit() {
		return deliveryCredit;
	}
	public void setDeliveryCredit(String deliveryCredit) {
		this.deliveryCredit = deliveryCredit;
	}
	public String getIsOwnShop() {
		return isOwnShop;
	}
	public void setIsOwnShop(String isOwnShop) {
		this.isOwnShop = isOwnShop;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public List<String> getOrderGoodsIdList() {
		return orderGoodsIdList;
	}
	public void setOrderGoodsIdList(List<String> orderGoodsIdList) {
		this.orderGoodsIdList = orderGoodsIdList;
	}
	public List<String> getGoodsIdList() {
		return goodsIdList;
	}
	public void setGoodsIdList(List<String> goodsIdList) {
		this.goodsIdList = goodsIdList;
	}
	public List<String> getIsAnonymousList() {
		return isAnonymousList;
	}
	public void setIsAnonymousList(List<String> isAnonymousList) {
		this.isAnonymousList = isAnonymousList;
	}
	public List<String> getContentList() {
		return contentList;
	}
	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}
	public List<String> getScoreList() {
		return scoreList;
	}
	public void setScoreList(List<String> scoreList) {
		this.scoreList = scoreList;
	}
}
