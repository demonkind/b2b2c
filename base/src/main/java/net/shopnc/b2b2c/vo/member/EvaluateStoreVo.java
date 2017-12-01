package net.shopnc.b2b2c.vo.member;

public class EvaluateStoreVo {
	private int evaluateId;
	private int descriptionCredit;
	private int serviceCredit;
	private int deliveryCredit;
	private String memberName;
	private String storeName;
	private String evaluateTimeStr;
	private String ordersSn;
	
	private String desEvalArrow;//描述箭头
	private String serviceEvalArrow;//服务箭头
	private String deliveryEvalArrow;//发货速度箭头
	private String desEvalTitle;//描述title
	private String serviceEvalTitle;//服务title
	private String deliveryEvalTitle;//发货速度title
	private String desEvalRate;//描述高低率
	private String serviceEvalRate;//服务高低率
	private String deliveryEvalRate;//发货速度高低率
	private String storeDesEval;
	private String storeServiceEval;
	private String storeDeliveryEval;
	private String avgStoreEval;
	private String avgStoreEvalRate;
	
	public String getAvgStoreEvalRate() {
		return avgStoreEvalRate;
	}
	public void setAvgStoreEvalRate(String avgStoreEvalRate) {
		this.avgStoreEvalRate = avgStoreEvalRate;
	}
	public String getAvgStoreEval() {
		return avgStoreEval;
	}
	public void setAvgStoreEval(String avgStoreEval) {
		this.avgStoreEval = avgStoreEval;
	}
	public String getStoreDesEval() {
		return storeDesEval;
	}
	public void setStoreDesEval(String storeDesEval) {
		this.storeDesEval = storeDesEval;
	}
	public String getStoreServiceEval() {
		return storeServiceEval;
	}
	public void setStoreServiceEval(String storeServiceEval) {
		this.storeServiceEval = storeServiceEval;
	}
	public String getStoreDeliveryEval() {
		return storeDeliveryEval;
	}
	public void setStoreDeliveryEval(String storeDeliveryEval) {
		this.storeDeliveryEval = storeDeliveryEval;
	}
	public String getDesEvalArrow() {
		return desEvalArrow;
	}
	public void setDesEvalArrow(String desEvalArrow) {
		this.desEvalArrow = desEvalArrow;
	}
	public String getServiceEvalArrow() {
		return serviceEvalArrow;
	}
	public void setServiceEvalArrow(String serviceEvalArrow) {
		this.serviceEvalArrow = serviceEvalArrow;
	}
	public String getDeliveryEvalArrow() {
		return deliveryEvalArrow;
	}
	public void setDeliveryEvalArrow(String deliveryEvalArrow) {
		this.deliveryEvalArrow = deliveryEvalArrow;
	}
	public String getDesEvalTitle() {
		return desEvalTitle;
	}
	public void setDesEvalTitle(String desEvalTitle) {
		this.desEvalTitle = desEvalTitle;
	}
	public String getServiceEvalTitle() {
		return serviceEvalTitle;
	}
	public void setServiceEvalTitle(String serviceEvalTitle) {
		this.serviceEvalTitle = serviceEvalTitle;
	}
	public String getDeliveryEvalTitle() {
		return deliveryEvalTitle;
	}
	public void setDeliveryEvalTitle(String deliveryEvalTitle) {
		this.deliveryEvalTitle = deliveryEvalTitle;
	}
	public String getDesEvalRate() {
		return desEvalRate;
	}
	public void setDesEvalRate(String desEvalRate) {
		this.desEvalRate = desEvalRate;
	}
	public String getServiceEvalRate() {
		return serviceEvalRate;
	}
	public void setServiceEvalRate(String serviceEvalRate) {
		this.serviceEvalRate = serviceEvalRate;
	}
	public String getDeliveryEvalRate() {
		return deliveryEvalRate;
	}
	public void setDeliveryEvalRate(String deliveryEvalRate) {
		this.deliveryEvalRate = deliveryEvalRate;
	}
	public int getEvaluateId() {
		return evaluateId;
	}
	public void setEvaluateId(int evaluateId) {
		this.evaluateId = evaluateId;
	}
	public int getDescriptionCredit() {
		return descriptionCredit;
	}
	public void setDescriptionCredit(int descriptionCredit) {
		this.descriptionCredit = descriptionCredit;
	}
	public int getServiceCredit() {
		return serviceCredit;
	}
	public void setServiceCredit(int serviceCredit) {
		this.serviceCredit = serviceCredit;
	}
	public int getDeliveryCredit() {
		return deliveryCredit;
	}
	public void setDeliveryCredit(int deliveryCredit) {
		this.deliveryCredit = deliveryCredit;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getEvaluateTimeStr() {
		return evaluateTimeStr;
	}
	public void setEvaluateTimeStr(String evaluateTimeStr) {
		this.evaluateTimeStr = evaluateTimeStr;
	}
	public String getOrdersSn() {
		return ordersSn;
	}
	public void setOrdersSn(String ordersSn) {
		this.ordersSn = ordersSn;
	}
}
