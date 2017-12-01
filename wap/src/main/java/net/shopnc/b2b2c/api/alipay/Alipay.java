package net.shopnc.b2b2c.api.alipay;

import net.shopnc.b2b2c.api.alipay.common.config.AlipayConfig;
import net.shopnc.b2b2c.api.alipay.common.util.AlipaySubmit;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.PayOrdersType;
import net.shopnc.common.util.ShopHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxy on 2016-01-25.
 */
public class Alipay {
    /**
     * 订单信息
     */
    private HashMap<String, Object> orders;
    /**
     * 支付方式信息
     */
    private HashMap<String, String> paymentInfo;
    /**
     * 构造函数
     */
    public Alipay(HashMap<String, String> paymentInfo, HashMap<String, Object> ordersInfo) {
        this.paymentInfo = paymentInfo;
        this.orders = ordersInfo;
    }

    /**
     * 获取请求路径
     */
    public String getUrl() throws Exception{
        try{
            //把请求参数打包成数组
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "create_direct_pay_by_user");
            sParaTemp.put("partner", this.paymentInfo.get("alipayPartner"));
            sParaTemp.put("seller_email", this.paymentInfo.get("alipayAccount"));
            sParaTemp.put("_input_charset", AlipayConfig.input_charset);
            sParaTemp.put("sign_type", "MD5");
            sParaTemp.put("payment_type", "1");
            sParaTemp.put("notify_url", ShopConfig.getWebRoot()+"api/alipay/receive/notify");
            sParaTemp.put("return_url", ShopConfig.getWebRoot()+"api/alipay/receive/return");
            //公用回传参数，订单类型普通订单、预存款充值等
            if (this.orders.get("payOrdersType")==null) {
                sParaTemp.put("extra_common_param", "");
            }else{
                sParaTemp.put("extra_common_param", this.orders.get("payOrdersType").toString());
            }
            //支付单号
            if (this.orders.get("paySn")==null) {
                sParaTemp.put("out_trade_no", "");
            }else{
                sParaTemp.put("out_trade_no", this.orders.get("paySn").toString());
            }
            //商品名称
            if (this.orders.get("subject")==null) {
                sParaTemp.put("subject", "");
            }else{
                sParaTemp.put("subject", this.orders.get("subject").toString());
            }
            //商品描述
            if (this.orders.get("paySn")==null) {
                sParaTemp.put("body", "");
            }else{
                sParaTemp.put("body", this.orders.get("paySn").toString());
            }
            //支付总金额
            if (this.orders.get("payAmount")==null) {
                sParaTemp.put("total_fee", "0");
            }else{
                sParaTemp.put("total_fee", this.orders.get("payAmount").toString());
            }
            if (this.orders.get("showUrl")==null) {
                sParaTemp.put("show_url", "");
            }else{
                sParaTemp.put("show_url", this.orders.get("showUrl").toString());
            }
            if (this.orders.get("payOrdersType").toString().equals(PayOrdersType.PREDEPOSIT)) {
                sParaTemp.put("enable_paymethod", "directPay^cartoon^bankPay^cash^debitCardExpress");
            }
            sParaTemp.put("anti_phishing_key", new AlipaySubmit(this.paymentInfo).query_timestamp());
            sParaTemp.put("exter_invoke_ip", ShopHelper.getAddressIP());


            AlipaySubmit alipaySubmit = new AlipaySubmit(this.paymentInfo);
            Map<String, String> sPara = alipaySubmit.buildRequestPara(sParaTemp);
            String url = AlipayConfig.alipayGatewayNew;
            int i = 0;
            for (String key : sPara.keySet()) {
                url += (key+"="+java.net.URLEncoder.encode(sPara.get(key), AlipayConfig.input_charset));
                if (i < sPara.size()-1) {
                    url +="&";
                }
                i++;
            }
            return url;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
