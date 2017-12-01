package net.shopnc.b2b2c.vo.member;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EvaluateGoodsVo {
	private String evaluateId;
	private String goodsId;
	private String goodsName;
	private Timestamp evaluateTime;
	private String evaluateTimeStr;
	private String scores;
	private String goodsImage;
	private BigDecimal goodsPrice;
	private String isOwnShopStr;
	private int avgGoodsEval;
	
	private String evaluateContent1;
	private String evaluateContent2;
	private String images1;
	private String images2;
	private String explainContent1;
	private String explainContent2;
	private List<String> image1List=new ArrayList<String>();
	private List<String> image2List=new ArrayList<String>();
	
	private String days;//两次评价相隔天数
	
	private String scoreTitle;//评分满意度
	private String memberName;
	private String memberHeadUrl;
	
	private String storeName;
	private String ordersSn;//订单编号
	private String sellerId;
	private String memberId;
	
	private String storeId;
	
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public int getAvgGoodsEval() {
		return avgGoodsEval;
	}
	public void setAvgGoodsEval(int avgGoodsEval) {
		this.avgGoodsEval = avgGoodsEval;
	}
	public String getEvaluateTimeStr() {
		return evaluateTimeStr;
	}
	public void setEvaluateTimeStr(String evaluateTimeStr) {
		this.evaluateTimeStr = evaluateTimeStr;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getOrdersSn() {
		return ordersSn;
	}
	public void setOrdersSn(String ordersSn) {
		this.ordersSn = ordersSn;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getIsOwnShopStr() {
		return isOwnShopStr;
	}
	public void setIsOwnShopStr(String isOwnShopStr) {
		this.isOwnShopStr = isOwnShopStr;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberHeadUrl() {
		return memberHeadUrl;
	}
	public void setMemberHeadUrl(String memberHeadUrl) {
		this.memberHeadUrl = memberHeadUrl;
	}
	private String hasImage;//是否有图 0-无 1-有
	private String evalLv;//评价等级 1-好评 2-中评 3-差评
	private int evalCount;//评价数
	private String evalRate1;//好评率
	private String evalRate2;//中评率
	private String evalRate3;//差评率
	private String evalCount1;//好评数
	private String evalCount2;//中评数
	private String evalCount3;//差评数
	private String hasImageCount;//有图数
	
	public String getScoreTitle() {
		return scoreTitle;
	}
	public void setScoreTitle(String scoreTitle) {
		this.scoreTitle = scoreTitle;
	}
	public int getEvalCount() {
		return evalCount;
	}
	public void setEvalCount(int evalCount) {
		this.evalCount = evalCount;
	}
	public String getHasImageCount() {
		return hasImageCount;
	}
	public void setHasImageCount(String hasImageCount) {
		this.hasImageCount = hasImageCount;
	}
	public String getEvalCount1() {
		return evalCount1;
	}
	public void setEvalCount1(String evalCount1) {
		this.evalCount1 = evalCount1;
	}
	public String getEvalCount2() {
		return evalCount2;
	}
	public void setEvalCount2(String evalCount2) {
		this.evalCount2 = evalCount2;
	}
	public String getEvalCount3() {
		return evalCount3;
	}
	public void setEvalCount3(String evalCount3) {
		this.evalCount3 = evalCount3;
	}
	public String getHasImage() {
		return hasImage;
	}
	public void setHasImage(String hasImage) {
		this.hasImage = hasImage;
	}
	public String getEvalLv() {
		return evalLv;
	}
	public void setEvalLv(String evalLv) {
		this.evalLv = evalLv;
	}
	public String getEvalRate1() {
		return evalRate1;
	}
	public void setEvalRate1(String evalRate1) {
		this.evalRate1 = evalRate1;
	}
	public String getEvalRate2() {
		return evalRate2;
	}
	public void setEvalRate2(String evalRate2) {
		this.evalRate2 = evalRate2;
	}
	public String getEvalRate3() {
		return evalRate3;
	}
	public void setEvalRate3(String evalRate3) {
		this.evalRate3 = evalRate3;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public List<String> getImage1List() {
		return image1List;
	}
	public void setImage1List(List<String> image1List) {
		this.image1List = image1List;
	}
	public List<String> getImage2List() {
		return image2List;
	}
	public void setImage2List(List<String> image2List) {
		this.image2List = image2List;
	}
	public Timestamp getEvaluateTime() {
		return evaluateTime;
	}
	public void setEvaluateTime(Timestamp evaluateTime) {
		this.evaluateTime = evaluateTime;
	}
	public String getEvaluateId() {
		return evaluateId;
	}
	public void setEvaluateId(String evaluateId) {
		this.evaluateId = evaluateId;
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
	public String getScores() {
		return scores;
	}
	public void setScores(String scores) {
		this.scores = scores;
	}
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	public String getEvaluateContent1() {
		return evaluateContent1;
	}
	public void setEvaluateContent1(String evaluateContent1) {
		this.evaluateContent1 = evaluateContent1;
	}
	public String getEvaluateContent2() {
		return evaluateContent2;
	}
	public void setEvaluateContent2(String evaluateContent2) {
		this.evaluateContent2 = evaluateContent2;
	}
	public String getImages1() {
		return images1;
	}
	public void setImages1(String images1) {
		this.images1 = images1;
	}
	public String getImages2() {
		return images2;
	}
	public void setImages2(String images2) {
		this.images2 = images2;
	}
	public String getExplainContent1() {
		return explainContent1;
	}
	public void setExplainContent1(String explainContent1) {
		this.explainContent1 = explainContent1;
	}
	public String getExplainContent2() {
		return explainContent2;
	}
	public void setExplainContent2(String explainContent2) {
		this.explainContent2 = explainContent2;
	}
	
}
