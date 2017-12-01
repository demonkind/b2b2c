package net.shopnc.b2b2c.web.action.buy;

import net.sf.json.JSONObject;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.PredepositRechargeDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.domain.Payment;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.member.Coupons;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.PredepositRecharge;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.service.orders.PayService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.buy.OrdersPayEntity;
import net.shopnc.common.util.KmsHelper;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品订单、预存款充值支付
 * Created by hbj on 2015/12/22.
 */
@Controller
public class BuyPayAction extends BuyBaseAction {

	@Autowired
	private OrdersDao ordersDao;
    @Autowired
    private PayService payService;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PredepositRechargeDao predepositRechargeDao;
    @Autowired
    private MemberOrdersService memberOrdersService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GoodsActivityService goodsActivityService;
    /**
     * 商品订单支付[选择支付方式]
     * @param payId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "buy/pay/payment/list/{payId}", method = RequestMethod.GET)
    public String paymentList(@PathVariable Integer payId, ModelMap modelMap) {
        logger.info("订单生成，选择支付方式");
        String tips;
        try {
            if (payId == null) {
                throw new ShopException("参数错误");
            }
//          List<Object> condition=new ArrayList<Object>();
//          condition.add("o.payId = :payId");
//          HashMap<String,Object> params=new HashMap<String,Object>();
//          params.put("payId", payId);
//          OrdersVo orderVo=ordersDao.getOrdersVoInfo(condition, params);
          List<Integer> ordersIdList = new ArrayList<Integer>();
          ordersIdList.add(payId);
          List<Object> ordersGoodsObjectList = ordersDao.getOrdersGoodsVoList(ordersIdList);
          if(ordersGoodsObjectList!=null){
          	 for (int a=0; a<ordersGoodsObjectList.size(); a++) {
          		 OrdersGoodsVo ordersGoodsVo = (OrdersGoodsVo)ordersGoodsObjectList.get(a);
          		 GoodsActivity goodsActivity=new GoodsActivity();
          		 goodsActivity=(GoodsActivity) goodsActivityService.checkBound(ordersGoodsVo.getCommonId());
          		 int manxNum=memberOrdersService.findTotalPurchasesOfGoods(ordersGoodsVo.getCommonId(),SessionEntity.getMemberId());
          		 
      	            //商铺最大限购数量
      	         int maxWeight=Integer.valueOf(goodsActivity.getMaxNum())-(manxNum+ordersGoodsVo.getBuyNum());
      	         if(maxWeight<0){
      	        	 throw new ShopException("购买量已超过上限！");
      	         }
          	}
          }
            //订单信息
            logger.info("得到订单信息");
            OrdersPayEntity ordersPayEntity = payService.getOrdersPayEntityInfo(payId, SessionEntity.getMemberId());

            //如果线上线支付差额和线下支付金额均为0
            if (PriceHelper.isEquals(ordersPayEntity.getOrdersOnlineDiffAmount(),new BigDecimal(0))
                    && PriceHelper.isEquals(ordersPayEntity.getOrdersOfflineAmount(),new BigDecimal(0))) {
                if (ordersPayEntity.getIsExistOnlineNoPay() == State.YES) {
                    //如果是0元订单，设置成已经支付状态
                    logger.info("0元订单，设置成已经支付");
                    for (int i=0; i<ordersPayEntity.getOrdersList().size(); i++) {
                        if (ordersPayEntity.getOrdersList().get(i).getPaymentTypeCode().equals(OrdersPaymentTypeCode.ONLINE)) {
                            ordersPayEntity.getOrdersList().get(i).setOrdersState(OrdersOrdersState.PAY);
                            ordersPayEntity.getOrdersList().get(i).setPaymentCode(OrdersPaymentCode.PREDEPOSIT);
                            ordersPayEntity.getOrdersList().get(i).setPaymentTime(ShopHelper.getCurrentTimestamp());
                        }
                    }
                    memberOrdersService.payOrders(ordersPayEntity.getOrdersList());
                    return "redirect:/buy/pay/success/" + payId;
                } else {
                    //已经支付过，直接跳走
                    return "redirect:/member/orders/list";
                }
            }

            //提示信息
            if (PriceHelper.isEquals(ordersPayEntity.getOrdersOnlineDiffAmount(),new BigDecimal(0))) {
                tips = "下单成功，我们会尽快为您发货，请保持电话畅通。";
            } else if (PriceHelper.isEquals(ordersPayEntity.getOrdersOfflineAmount(),new BigDecimal(0))) {
                tips = String.format("请您在%d分钟内完成支付，逾期订单将自动取消。", Common.ORDER_AUTO_CANCEL_TIME * 60);
            } else {
                tips = String.format("部分商品需要在线支付，请您在%d分钟内完成支付，逾期订单将自动取消。",Common.ORDER_AUTO_CANCEL_TIME * 60);
            }

            //是否可选站内余额
            int allowPredeposit = (PriceHelper.isGreaterThan(ordersPayEntity.getOrdersOnlineDiffAmount(),new BigDecimal(0)) == true &&
            PriceHelper.isEquals(ordersPayEntity.getPredepositAmount(), new BigDecimal(0)) == true) ? 1 : 0;

            //在线支付方式列表
            logger.info("取得在线支付方式列表");
            if (PriceHelper.isGreaterThan(ordersPayEntity.getOrdersOnlineAmount(),new BigDecimal(0))) {
                List<Payment> paymentList = paymentService.getOpenPaymentOnlineList();
                modelMap.put("paymentList",paymentList);
            }

            logger.info("生成ordersVoList");
            List<OrdersVo> ordersVoList = new ArrayList<>();
            for (int i=0; i<ordersPayEntity.getOrdersList().size(); i++) {
                OrdersVo ordersVo = new OrdersVo(ordersPayEntity.getOrdersList().get(i));
                ordersVoList.add(ordersVo);
            }

            //会员信息
            logger.info("取得会员信息");
            Member member = memberDao.get(Member.class, SessionEntity.getMemberId());

            modelMap.put("ordersVoList",ordersVoList);
            modelMap.put("ordersPayVo",ordersPayEntity);
            modelMap.put("tips",tips);
            modelMap.put("member",member);
            modelMap.put("allowPredeposit",allowPredeposit);

        } catch (ShopException e) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", e.getMessage());
            hashMapMessage.put("url", ShopConfig.getMemberRoot() + "orders/list");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        } catch (Exception e) {
            logger.error(e.toString());
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", "数据查询异常");
            hashMapMessage.put("url", ShopConfig.getMemberRoot() + "orders/list");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }

        //购买进度坐标
        modelMap.put("buyStep", "3");

        return getBuyTemplate("pay/pay");
    }

    /**
     * 商品订单支付完后的提示信息
     * @param payId
     */
    @RequestMapping(value = "buy/pay/success/{payId}",method = RequestMethod.GET)
    public String pay(@PathVariable Integer payId, ModelMap modelMap,HttpServletRequest request) {
        try {
            if (payId == null) {
                throw new ShopException("参数错误");
            }
            //订单信息
            OrdersPayEntity ordersPayEntity = payService.getOrdersPayEntityInfo(payId, SessionEntity.getMemberId());
            modelMap.put("ordersOnlinePayAmount",ordersPayEntity.getOrdersOnlinePayAmount());

            //购买进度坐标
            modelMap.put("buyStep","4");

            //发送K码
            List<Orders> orders = payService.getOrdersByPauId(payId);
            for(Orders o : orders){
            	HashMap<String, Object> map = new HashMap<String ,Object>();
            	map.put("ordersId",o.getOrdersId());
            	map.put("memberId", SessionEntity.getMemberId());
            	OrdersVo ordersVo = memberOrdersService.getOrdersVoInfo(map);
            	List<OrdersGoodsVo> ordersGoods = ordersVo.getOrdersGoodsVoList();
            	for(OrdersGoodsVo g : ordersGoods){
            		//判断是否绑定了活动
            		if(goodsActivityService.checkBound(g.getCommonId()) != null){
            			List<GoodsActivity> goodsActivity = goodsActivityService.findGoodsActivityById(g.getCommonId());
            			for(GoodsActivity a: goodsActivity){
            				//如果发送K码设定为收货（sendKCode = 1） 
            				if("0".equals(a.getSendKCodeType())){
            					for(int i=0; i<g.getBuyNum(); i++){
            						memberService.sendCardCoupons(SessionEntity.getMemberId(), SessionEntity.getMemberName(), a.getReturnAmount(), "", "", g.getGoodsId(), g.getOrdersId(), g.getStoreId());
            					}
            				}
            			}
            		}
            	}
            }
            return getBuyTemplate("pay/success");
        } catch (ShopException e) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", e.getMessage());
            hashMapMessage.put("url", ShopConfig.getMemberRoot());
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        } catch (Exception e) {
            return "redirect:/error";
        }

    }

    /**
     * 预存款充值支付[选择支付方式]
     * @param rechargeId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "buy/pay/pdpay/payment/{rechargeId}", method = RequestMethod.GET)
    public String predepositPaymentList(@PathVariable Integer rechargeId,
                                        ModelMap modelMap) {
        if (rechargeId==null || rechargeId<=0) {
            return "redirect:/error";
        }
        //查询充值单信息
        PredepositRecharge rechargeInfo = predepositRechargeDao.get(PredepositRecharge.class, rechargeId);
        if (rechargeInfo==null) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", "参数错误");
            hashMapMessage.put("url", ShopConfig.getMemberRoot()+"predeposit/recharge/list");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }
        //查询会员
        Member memberInfo = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (memberInfo==null) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", "参数错误");
            hashMapMessage.put("url", ShopConfig.getMemberRoot()+"predeposit/recharge/list");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }
        //判断是否为该会员充值单
        if (rechargeInfo.getMemberId() != memberInfo.getMemberId()) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", "参数错误");
            hashMapMessage.put("url", ShopConfig.getMemberRoot()+"predeposit/recharge/list");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }
        //判断充值单是否已支付
        if (rechargeInfo.getPayState() == PredepositRechargePayState.PAID) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", "请勿重复支付");
            hashMapMessage.put("url", ShopConfig.getMemberRoot()+"predeposit/recharge/list");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }
        modelMap.put("rechargeInfo", rechargeInfo);
        //在线支付方式列表
        List<Payment> paymentList = paymentService.getOpenPaymentOnlineList();
        modelMap.put("paymentList",paymentList);

        //购买进度坐标
        modelMap.put("buyStep","4");

        return getBuyTemplate("pay/predeposit_pay");
    }

    /**
     * 预存款支付完后的提示信息
     * @param rechargeId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "buy/pay/pdpay/success/{rechargeId}",method = RequestMethod.GET)
    public String predepositPaySuccess(@PathVariable Integer rechargeId, ModelMap modelMap) {
        try {
            if (rechargeId==null || rechargeId<=0) {
                throw new ShopException("参数错误");
            }
            //查询充值单信息
            PredepositRecharge rechargeInfo = predepositRechargeDao.get(PredepositRecharge.class, rechargeId);
            if (rechargeInfo==null) {
                throw new ShopException("参数错误");
            }
            if (rechargeInfo.getMemberId() != SessionEntity.getMemberId()) {
                throw new ShopException("参数错误");
            }
            if (rechargeInfo.getPayState() == PredepositRechargePayState.NOTPAY) {
                throw new ShopException("未支付成功");
            }
            modelMap.put("rechargeInfo",rechargeInfo);
            //购买进度坐标
            modelMap.put("buyStep", "4");
            return getBuyTemplate("pay/predeposit_success");
        } catch (ShopException e) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", e.getMessage());
            hashMapMessage.put("url", ShopConfig.getMemberRoot()+"predeposit/recharge/list");
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
}
