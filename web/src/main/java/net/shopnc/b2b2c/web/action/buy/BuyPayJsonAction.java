package net.shopnc.b2b2c.web.action.buy;

import net.shopnc.b2b2c.api.alipay.Alipay;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.PayOrdersType;
import net.shopnc.b2b2c.constant.PredepositRechargePayState;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.PredepositRechargeDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.PredepositRecharge;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.service.orders.PayService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.buy.OrdersPayEntity;
import net.shopnc.common.util.PriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 商品订单、预存款充值支付
 * Created by hou on 2016/3/14.
 */
@Controller
public class BuyPayJsonAction extends BuyBaseJsonAction {
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
    private GoodsActivityService goodsActivityService;
    /**
     * 商品订单支付[提交支付方式]
     * @param payId
     * @param paymentCode
     * @param predepositPay
     * @param payPwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/pay/pay",method = RequestMethod.POST)
    public ResultEntity pay(@RequestParam(value = "payId",required = true) Integer payId,
                            @RequestParam(value = "paymentCode",required = false,defaultValue = "") String paymentCode,
                            @RequestParam(value = "predepositPay",required = false,defaultValue = "") String predepositPay,
                            @RequestParam(value = "payPwd",required = false,defaultValue = "") String payPwd) {
        logger.info("支付商品订单");
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            if (payId == null) {
                throw new ShopException("参数错误");
            }
            
            List<Object> condition=new ArrayList<Object>();
            condition.add("o.payId = :payId");
            HashMap<String,Object> params=new HashMap<String,Object>();
            params.put("payId", payId);
            OrdersVo orderVo=ordersDao.getOrdersVoInfo(condition, params);
            List<Integer> ordersIdList = new ArrayList<Integer>();
            ordersIdList.add(orderVo.getOrdersId());
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
            logger.info("取得订单信息");
            OrdersPayEntity ordersPayEntity = payService.getOrdersPayEntityInfo(payId, SessionEntity.getMemberId());

            //会员信息
            logger.info("取得会员信息");
            Member member = memberDao.getLockModelMemberInfo(SessionEntity.getMemberId());
            ordersPayEntity.setMember(member);

            //预存款支付
            if (!payPwd.equals("") && predepositPay.equals("1")) {
                logger.info("使用预存款支付");
                ordersPayEntity = payService.predepositPay(ordersPayEntity, payPwd);
            }

            //在线API支付

            //如果在线需要支付金额为0，无论何种情况，都返回支付成功
            if (PriceHelper.isEquals(ordersPayEntity.getOrdersOnlineDiffAmount(), new BigDecimal(0))) {
                if (ordersPayEntity.getIsExistOnlineNoPay() == State.YES) {
                    //0元订单时更改成已支付状态[可能存在货到付款订单，但不影响]
                    logger.info("0元订单时更改成已支付状态");
                    memberOrdersService.payOrders(ordersPayEntity.getOrdersList());
                }
                resultEntity.setUrl(ShopConfig.getWebRoot() + "buy/pay/success/" + payId);
                resultEntity.setCode(ResultEntity.SUCCESS);
                logger.info("还需在线支付金额为0，支付成功了");
                return resultEntity;
            }

            //如果在线需要支付金额>0，继续调用第三方API支付
            logger.info("在线支付金额仍>0，继续调用API支付");
            List<String> paymentCodeList = Arrays.asList("alipay");
            if (!paymentCodeList.contains(paymentCode)) {
                throw new ShopException("支付方式错误");
            }

            //准备支付数据
            HashMap<String, Object> payInfo = new HashMap();
            payInfo.put("payOrdersType", PayOrdersType.ORDERS);
            payInfo.put("paySn", ordersPayEntity.getPaySn());
            payInfo.put("subject", "商品订单——" + ordersPayEntity.getPaySn());
            payInfo.put("showUrl", ShopConfig.getMemberRoot() + "orders/list");
            payInfo.put("payAmount", ordersPayEntity.getOrdersOnlineDiffAmount());
            //调用支付接口
            String url = _apiPay(paymentCode, payInfo);
            if (url == null || url.length() <= 0) {
                throw new ShopException("调取支付接口失败");
            }
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(url);

        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setMessage("支付失败");
        }
        return resultEntity;
    }
    /**
     * 预存款充值支付[提交支付方式]
     * @param rechargeId
     * @param paymentCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/pay/pdpay/pay",method = RequestMethod.POST)
    public ResultEntity predepositPay(@RequestParam(value = "rechargeId",required = true) Integer rechargeId,
                                      @RequestParam(value = "paymentCode",required = false) String paymentCode){
        ResultEntity resultEntity = new ResultEntity();
        if (rechargeId==null || rechargeId<=0) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            resultEntity.setUrl(ShopConfig.getMemberRoot() + "predeposit/recharge/list");
            return resultEntity;
        }
        if (paymentCode==null || paymentCode.length()<=0) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("请选择支付方式");
            return resultEntity;
        }
        //查询充值单信息
        PredepositRecharge rechargeInfo = predepositRechargeDao.get(PredepositRecharge.class, rechargeId);
        if (rechargeInfo==null) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        if (rechargeInfo.getPayState() == PredepositRechargePayState.PAID) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("该充值单已支付，请不要重复支付");
            return resultEntity;
        }
        try{
            HashMap<String, Object> payInfo = new HashMap();
            payInfo.put("payOrdersType", PayOrdersType.PREDEPOSIT);
            payInfo.put("paySn", rechargeInfo.getRechargeSn());
            payInfo.put("subject", "预存款充值单——"+rechargeInfo.getRechargeSn());
            payInfo.put("showUrl", ShopConfig.getMemberRoot()+"predeposit/recharge/list");
            payInfo.put("payAmount", rechargeInfo.getAmount());
            //调用支付接口
            String url = _apiPay(paymentCode, payInfo);
            if (url==null || url.length()<=0) {
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("调取支付接口失败");
                return resultEntity;
            }
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(url);
            return resultEntity;
        }catch (Exception e){
            logger.info(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("调取支付接口失败");
            return resultEntity;
        }
    }

    /**
     * 调用第三方支付API
     * @param paymentCode
     * @param payInfo
     * @return
     * @throws Exception
     */
    private String _apiPay(String paymentCode, HashMap<String, Object> payInfo) throws Exception{
        //查询支付方式
        HashMap<String, Object> paymentDetail = paymentService.getPaymentDetail(paymentCode);
        if (paymentDetail==null || Integer.valueOf(paymentDetail.get("paymentState").toString())== State.NO || paymentDetail.get("paymentConfig")==null) {
            throw new ParameterErrorException();
        }
        //支付宝
        if (paymentDetail.get("paymentCode").equals("alipay")) {
            Alipay alipayClass = new Alipay((HashMap<String,String>)paymentDetail.get("paymentConfig"), payInfo);
            return alipayClass.getUrl();
        }
        return "";
    }
}
