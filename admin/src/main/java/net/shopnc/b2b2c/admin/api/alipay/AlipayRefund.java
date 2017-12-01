package net.shopnc.b2b2c.admin.api.alipay;


import net.shopnc.b2b2c.admin.api.alipay.common.config.AlipayConfig;
import net.shopnc.b2b2c.admin.api.alipay.common.util.AlipaySubmit;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.exception.ShopException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxy on 2016-01-25.
 */
public class AlipayRefund {
    /**
     * 订单信息
     */
    private HashMap<String, Object> ordersInfo;
    /**
     * 支付方式信息
     */
    private HashMap<String, String> paymentInfo;

    /**
     * 构造函数
     */
    public AlipayRefund(HashMap<String, String> paymentInfo, HashMap<String, Object> ordersInfo) {
        this.paymentInfo = paymentInfo;
        this.ordersInfo = ordersInfo;
    }

    /**
     * 获取请求路径
     */
    public String getUrl() throws ShopException {
        try {
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "refund_fastpay_by_platform_pwd");
            sParaTemp.put("partner", this.paymentInfo.get("alipayPartner"));
            sParaTemp.put("notify_url", ShopConfig.getWebRoot() + "api/alipay/refund_notify/return");
            sParaTemp.put("seller_email", this.paymentInfo.get("alipayAccount"));
            sParaTemp.put("_input_charset","utf-8");
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curDate = s.format(new Date());
            sParaTemp.put("refund_date", curDate);
            sParaTemp.put("batch_no", this.ordersInfo.get("batchNo").toString());
            sParaTemp.put("batch_num", "1");
            sParaTemp.put("detail_data", this.ordersInfo.get("outOrdersSn").toString() + "^" + this.ordersInfo.get("refundAmount").toString() + "^协商退款");

            AlipayConfig.key =(String) this.paymentInfo.get("alipayKey");
            AlipayConfig.partner =(String) this.paymentInfo.get("alipayPartner");

            //建立请求
            return AlipaySubmit.buildRequest(sParaTemp);
//            return AlipaySubmit.buildRequest(sParaTemp,"get","确认");
        } catch (Exception e) {
            throw new ShopException(e.getMessage());
        }
    }

}
