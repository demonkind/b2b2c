package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.admin.api.alipay.AlipayRefund;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.refund.RefundDetail;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.b2b2c.service.orders.AdminOrdersService;
import net.shopnc.b2b2c.service.refund.AdminRefundService;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.vo.refund.RefundDetailVo;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * 支付宝退款
 * Created by cj on 2016/2/1.
 */
@Controller
public class RefundOnlineAction extends BaseJsonAction {
    @Autowired
    AdminRefundService adminRefundService;

    @Autowired
    AdminOrdersService adminOrdersService;

    @Autowired
    private PaymentService paymentService;
    @RequestMapping(value = "refund/refund_online/{paymentCode}/{refundId}", method = RequestMethod.GET)
    public String refundOnline(
            @PathVariable(value = "paymentCode") String paymentCode,
            @PathVariable(value = "refundId") int refundId
    ){

        try{
            if (paymentCode==null || paymentCode.length()<=0 || refundId <=0 ) {
                throw new ShopException("退款失败[请选择支付方式]");
            }
            RefundItemVo refundItemVo = adminRefundService.getRefundInfo(refundId);
            if (refundItemVo == null) {
                throw new ShopException("退款失败[获取退款信息失败]");
            }

            RefundDetail refundDetail = adminRefundService.getRefundDetailByRefundId(refundId);
            if (refundDetail == null ){
                throw new ShopException("退款失败[获取退款详情信息失败]");
            }
//            if (PriceHelper.isEquals(refundDetail.getPdAmount() ,BigDecimal.ZERO))
//            {
//                throw new ShopException("退款失败[退款金额为空]");
//            }
            OrdersVo ordersVo = adminOrdersService.getOrdersVoInfo(refundItemVo.getOrdersId());
            logger.info("判断是否修改批次号");
            logger.info(isModBatchNo(refundDetail.getBatchNo()) );
            //修改批次号+++++++++++++++++++
            logger.info("原退款批次号 ：" + refundDetail.getBatchNo());
            if (isModBatchNo(refundDetail.getBatchNo())){
                refundDetail.setBatchNo(adminRefundService.makeBatchNo(refundDetail.getRefundId()));
                adminRefundService.updateRefundDetail(refundDetail);
                logger.info("修改退款批次号 ：" + refundDetail.getBatchNo());
            }

            //获取退款金额
            BigDecimal refundAmount = adminRefundService.getPayRefundAmount(refundDetail);
            HashMap<String, Object> payInfo = new HashMap();


            payInfo.put("batchNo",refundDetail.getBatchNo());



            payInfo.put("refundAmount",refundAmount);
            //
            payInfo.put("outOrdersSn",ordersVo.getOutOrdersSn());
            String url = _apiPay(paymentCode, payInfo);
            if (url == null || url.length() <= 0) {
                throw new ShopException("退款失败[调取支付接口失败]");
            }
            logger.info("支付宝退款连接是:" + url);
            return "redirect:"+ url;
        }catch (ShopException e){
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", e.getMessage());
            hashMapMessage.put("url", ShopConfig.getMemberRoot());
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        }
    }

    /**
     * 在线退款查询
     */
    @ResponseBody
    @RequestMapping(value = "refund/refund_query", method = RequestMethod.POST)
    public ResultEntity edit(@RequestParam(value = "refundId",required = true) int refundId) {

        ResultEntity resultEntity = new ResultEntity();
        try {
            RefundDetailVo refundDetailVo = adminRefundService.getRefundDetailVoByRefundId(refundId);
            if (refundDetailVo.getPayTime() == null){
                throw new ShopException("退款正在处理中或已失败，稍后查询");
            }
            resultEntity.setMessage("成功退款:"+ refundDetailVo.getPayAmount());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("退款正在处理中或已失败，稍后查询");
        }


        return resultEntity;

    }

    private String _apiPay(String paymentCode, HashMap<String, Object> payInfo) throws ShopException {
        //查询支付方式
        HashMap<String, Object> paymentDetail = paymentService.getPaymentDetail(paymentCode);
        if (paymentDetail == null || Integer.valueOf(paymentDetail.get("paymentState").toString()) == State.NO || paymentDetail.get("paymentConfig") == null) {
            throw new ParameterErrorException();
        }
        //支付宝
        if (paymentDetail.get("paymentCode").equals("alipay")) {
            AlipayRefund alipayClass = new AlipayRefund((HashMap<String, String>) paymentDetail.get("paymentConfig"), payInfo);
            return alipayClass.getUrl();
        }
        return "";
    }

    /**
     * 判断是否需要修改批次号，如果不是当天的就修改
     * 批次号。支付宝要求格式为：当天退款日期+流水号。
     * @return
     */
    private Boolean isModBatchNo(String batchNo) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Timestamp timestamp = ShopHelper.getCurrentTimestamp();
        String batchNoDate = batchNo.substring(0, 8);
        return batchNoDate.equals(df.format(timestamp)) ? false : true;
    }
}
