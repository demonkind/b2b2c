package net.shopnc.b2b2c.api.alipay.action;

import net.shopnc.b2b2c.api.alipay.common.util.AlipayNotify;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.PayOrdersType;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.b2b2c.service.orders.ReceiveService;
import net.shopnc.common.entity.buy.OrdersPayEntity;
import net.shopnc.common.util.ShopHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zxy on 2016-01-25.
 */

@Controller
@RequestMapping("/api/alipay/receive")
public class ReceiveAction {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PredepositService predepositService;
    @Autowired
    private ReceiveService receiveService;

    /**
     * 支付方式code
     */
    private String paymentCode = "alipay";
    /**
     * 支付方式详细信息
     */
    private HashMap<String, String> paymentInfo = null;
    protected final Logger logger = Logger.getLogger(getClass());

    private void setPaymentInfo(){
        //查询支付方式
        HashMap<String, Object> paymentDetail = paymentService.getPaymentDetail(this.paymentCode);
        if (paymentDetail!=null && paymentDetail.get("paymentConfig")!=null) {
            this.paymentInfo = (HashMap<String, String>)paymentDetail.get("paymentConfig");
        }else{
            this.paymentInfo = null;
        }
    }

    /**
     * 同步通知
     * @param requestParams
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "return", method = RequestMethod.GET)
    public String receiveReturn(@RequestParam HashMap requestParams, ModelMap modelMap) {

        setPaymentInfo();

        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        /*for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }*/
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String valueStr = (String)requestParams.get(name);
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //交易状态
        String trade_status = params.get("trade_status");
        //公用回传参数
        String orderType = params.get("extra_common_param");
        HashMap<String, String> hashMapMessage = new HashMap<>();
        if(new AlipayNotify(this.paymentInfo).verify(params)) {//验证成功
            if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                if (orderType.equals(PayOrdersType.PREDEPOSIT)) {
                    //处理预存款充值单
                    try {
                        predepositService.onlineRechargePay(params.get("out_trade_no"), params.get("trade_no"), this.paymentCode);
                        return "redirect:" + ShopConfig.getMemberRoot() + "predeposit/recharge/list";
                    } catch (ShopException e) {
                        hashMapMessage.put("message", e.getMessage());
                        hashMapMessage.put("url", ShopConfig.getMemberRoot() + "predeposit/recharge/list");
                        return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
                    } catch (Exception e) {
                        logger.error(e.toString());
                        hashMapMessage.put("message", "支付失败");
                        hashMapMessage.put("url", ShopConfig.getMemberRoot() + "predeposit/recharge/list");
                        return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
                    }
                } else if (orderType.equals(PayOrdersType.ORDERS)) {
                    //商品订单
                    try {
                        OrdersPayEntity ordersPayEntity = receiveService.onLineOrdersPay(params.get("out_trade_no"), params.get("trade_no"), this.paymentCode);
                        return "redirect:/buy/pay/success/" + ordersPayEntity.getPayId();
                    } catch (ShopException e) {
                        hashMapMessage.put("message", e.getMessage());
                        hashMapMessage.put("url", ShopConfig.getMemberRoot() + "orders/list");
                        return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        hashMapMessage.put("message", "支付失败");
                        hashMapMessage.put("url", ShopConfig.getMemberRoot() + "orders/list");
                        return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
                    }
                } else {
                    hashMapMessage.put("message", "参数错误");
                    hashMapMessage.put("url", ShopConfig.getMemberRoot());
                    return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
                }
            } else {
                hashMapMessage.put("message", "回调数据状态错误");
                hashMapMessage.put("url", ShopConfig.getMemberRoot());
                return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
            }
        } else {
            hashMapMessage.put("message", "回调数据验证失败");
            hashMapMessage.put("url", ShopConfig.getMemberRoot());
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }
    }

    /**
     * 异步通知
     * @param requestParams
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "notify", method = RequestMethod.POST)
    public String receiveNotify(@RequestParam HashMap requestParams) {
        //返回状态 success成功 fail失败
        String message = "fail";
        setPaymentInfo();

        //获取支付宝POST过来反馈信息
        HashMap<String,String> params = new HashMap<String,String>();
//        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
//            params.put(name, valueStr);
//        }
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String valueStr = (String)requestParams.get(name);
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //交易状态
        String trade_status = params.get("trade_status");
        //公用回传参数
        String orderType = params.get("extra_common_param");

        if(new AlipayNotify(paymentInfo).verify(params)) {//验证成功
            if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                if (orderType.equals(PayOrdersType.PREDEPOSIT)) {
                    //处理预存款充值单
                    try{
                        boolean result = predepositService.onlineRechargePay(params.get("out_trade_no"), params.get("trade_no"), this.paymentCode);
                        if (result == true) {
                            message = "success";
                        }
                    } catch (ShopException e){
                        //记录错误日志
                        logger.error(e.getMessage());
                    }
                } else if (orderType.equals(PayOrdersType.ORDERS)) {
                    //商品订单
                    try {
                        receiveService.onLineOrdersPay(params.get("out_trade_no"), params.get("trade_no"), this.paymentCode);
                        message = "success";
                    } catch (ShopException e) {
                        logger.error(e.getMessage());
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                } else {
                    //记录错误日志
                    logger.error("订单信息错误");
                }
            }else{
                //记录错误日志
            }
        }else{
            //记录错误日志
            logger.error("通知信息验证失败");
        }
        return message;
    }
}