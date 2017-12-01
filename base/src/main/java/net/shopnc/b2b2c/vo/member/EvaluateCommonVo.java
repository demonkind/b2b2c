package net.shopnc.b2b2c.vo.member;

import net.shopnc.b2b2c.domain.EvaluateGoods;
import net.shopnc.b2b2c.domain.EvaluateGoodsContent;
import net.shopnc.b2b2c.domain.EvaluateStore;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;

public class EvaluateCommonVo {
	private String evaluateId;
	private String evaluateContent;
	
	private EvaluateGoods evaluateGoods;
	private OrdersGoods ordersGoods;
	private Member member;
	private EvaluateStore evaluateStore;
	
	public EvaluateCommonVo(EvaluateGoods a,EvaluateGoodsContent b){
		this.evaluateId=String.valueOf(a.getEvaluateId());
		this.evaluateContent=b.getContent();
	}
	
	public EvaluateCommonVo(EvaluateGoods eg,OrdersGoods og,Member m){
		this.evaluateGoods=eg;
		this.ordersGoods=og;
		this.member=m;
	}
	
	public EvaluateCommonVo(EvaluateStore es,Member m){
		this.evaluateStore=es;
		this.member=m;
	}
	

	public EvaluateStore getEvaluateStore() {
		return evaluateStore;
	}

	public void setEvaluateStore(EvaluateStore evaluateStore) {
		this.evaluateStore = evaluateStore;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public OrdersGoods getOrdersGoods() {
		return ordersGoods;
	}

	public void setOrdersGoods(OrdersGoods ordersGoods) {
		this.ordersGoods = ordersGoods;
	}

	public EvaluateGoods getEvaluateGoods() {
		return evaluateGoods;
	}

	public void setEvaluateGoods(EvaluateGoods evaluateGoods) {
		this.evaluateGoods = evaluateGoods;
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
