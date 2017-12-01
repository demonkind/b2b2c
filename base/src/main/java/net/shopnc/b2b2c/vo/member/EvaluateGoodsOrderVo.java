package net.shopnc.b2b2c.vo.member;


public class EvaluateGoodsOrderVo {
	private String goodsId;
	private String goodsName;
	private String goodsImage;
	private String evaluateId;
	private String evaluateContent;
	private String ordersGoodsId;
	
	public String getOrdersGoodsId() {
		return ordersGoodsId;
	}
	public void setOrdersGoodsId(String ordersGoodsId) {
		this.ordersGoodsId = ordersGoodsId;
	}
	private int storeId;
	
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	public String getEvaluateId() {
		return evaluateId;
	}
	public void setEvaluateId(String evaluateId) {
		this.evaluateId = evaluateId;
	}
	public String getEvaluateContent() {
		return evaluateContent;
	}
	public void setEvaluateContent(String evaluateContent) {
		this.evaluateContent = evaluateContent;
	}
}
