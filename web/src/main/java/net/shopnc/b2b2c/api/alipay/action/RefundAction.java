package net.shopnc.b2b2c.api.alipay.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.api.alipay.common.util.AlipayNotify;
import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.domain.refund.RefundDetail;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.b2b2c.service.refund.AdminRefundService;
import net.shopnc.b2b2c.service.refund.RefundService;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.util.ShopHelper;

/**
 * Created by cj on 2016/2/23.
 */

@Controller
@RequestMapping("/api/alipay/refund_notify")
public class RefundAction {
    @Autowired
    private PaymentService paymentService;
    

    @Autowired
    private RefundService refundService;

    @Autowired
    private AdminRefundService adminRefundService;

    /**
     * 支付方式code
     */
    private String paymentCode = "alipay";
    /**
     * 支付方式详细信息
     */
    private HashMap<String, String> paymentInfo = null;

    protected final Logger logger = Logger.getLogger(getClass());


    private void setPaymentInfo() {
        //查询支付方式
        HashMap<String, Object> paymentDetail = paymentService.getPaymentDetail(this.paymentCode);
        if (paymentDetail != null && paymentDetail.get("paymentConfig") != null) {
            this.paymentInfo = (HashMap<String, String>)paymentDetail.get("paymentConfig");
        } else {
            this.paymentInfo = null;
        }
    }

    @RequestMapping(value = "return", method = RequestMethod.POST)
    public String notifyReturn(@RequestParam HashMap requestParams) throws Exception {
        String message = "fail";
        setPaymentInfo();
        //获取参数
        HashMap<String, String> params = new HashMap<String, String>();
//        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
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
        String batchNo = params.get("batch_no");
        int successNum = Integer.parseInt(params.get("success_num"));
        String resultDetails = params.get("result_details");
        String[] details = resultDetails.split("\\^");
        //验证
        if (new AlipayNotify(paymentInfo).verify(params) && successNum > 0 && details.length > 0) {
            try {

                BigDecimal payAmount = new BigDecimal(details[1]);
                //获取详细信息
                RefundDetail refundDetail = adminRefundService.getRefundDetail(batchNo);
                if (refundDetail == null) {
                    throw new ShopException("退款批次:" + batchNo + ",退款失败[为找到相关退款信息]");
                }
                refundDetail.setPayAmount(payAmount);
                refundDetail.setPayTime(ShopHelper.getCurrentTimestamp());
                adminRefundService.updateRefundDetail(refundDetail);
                
                // add by yfb start 追加refund 的退款成功的方法
                Refund refund = new Refund();
                refund.setRefundId(refundDetail.getRefundId());
                refund.setRefundState(RefundState.REFUND_STATE_FINISH);
                refundService.saveRefund(refund);
                message = "success";
            } catch (ShopException e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.error("通知信息验证失败");
        }
        return message;
    }

}
