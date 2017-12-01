package net.shopnc.b2b2c.service;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.constant.OrdersPaymentCode;
import net.shopnc.b2b2c.constant.OrdersPaymentName;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.PaymentDao;
import net.shopnc.b2b2c.domain.Payment;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-05.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class PaymentService extends BaseService {

    @Autowired
    private PaymentDao paymentDao;

    /**
     * 获取支付方式列表表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getPaymentDtGridList(String dtGridPager) throws Exception {
        return paymentDao.getDtGridList(dtGridPager, Payment.class);
    }
    /**
     * 编辑支付方式
     * @param params
     * @throws ShopException
     */
    public void editPayment(HashMap<String,String> params) throws ShopException {
        if (params.get("paymentCode")==null || params.get("paymentCode").length()<=0) {
            throw new ParameterErrorException();
        }
        String[] codeArr = {"alipay", "offline", "tenpay", "chinabank", "predeposit", "wxpay"};
        String currCode = "";
        for (String v : codeArr) {
            if (v.equals(params.get("paymentCode"))) {
                currCode = v;
            }
        }
        if (currCode.length() <= 0) {
            throw new ParameterErrorException();
        }
        if (currCode.equals("alipay")) {
            this.editPaymentAlipay(currCode, params);
        }
        if (currCode.equals("wxpay")) {
            this.editPaymentWxpay(currCode, params);
        }
        if (currCode.equals("offline")) {
            this.editPaymentOffline(currCode, params);
        }
    }
    /**
     * 编辑支付宝支付方式
     * @param code
     * @param params
     * @return
     * @throws ShopException
     */
    public boolean editPaymentAlipay(String code,HashMap<String,String> params) throws ShopException {
        if (params.get("alipayAccount")==null || params.get("alipayAccount").length()<=0) {
            throw new ParameterErrorException();
        }
        if (params.get("alipayKey")==null || params.get("alipayKey").length()<=0) {
            throw new ParameterErrorException();
        }
        if (params.get("alipayPartner")==null || params.get("alipayPartner").length()<=0) {
            throw new ParameterErrorException();
        }
        //查询支付方式
        Payment payment = paymentDao.get(Payment.class, code);
        Payment paymentUpdate = payment;
        HashMap<String,String> paymentInfo = new HashMap<String, String>();
        paymentInfo.put("alipayAccount", params.get("alipayAccount"));
        paymentInfo.put("alipayKey", params.get("alipayKey"));
        paymentInfo.put("alipayPartner", params.get("alipayPartner"));
        paymentUpdate.setPaymentInfo(JsonHelper.toJson(paymentInfo));
        if (Integer.parseInt(params.get("paymentState")) == State.YES) {
            paymentUpdate.setPaymentState(State.YES);
        }else{
            paymentUpdate.setPaymentState(State.NO);
        }
        paymentDao.update(paymentUpdate);
        return true;
    }
    /**
     * 编辑微信支付支付方式
     * @param code
     * @param params
     * @return
     * @throws ShopException
     */
    public boolean editPaymentWxpay(String code,HashMap<String,String> params) throws ShopException {
        if (params.get("wxAppId")==null || params.get("wxAppId").length()<=0) {
            throw new ParameterErrorException();
        }
        if (params.get("wxMchid")==null || params.get("wxMchid").length()<=0) {
            throw new ParameterErrorException();
        }
        if (params.get("wxKey")==null || params.get("wxKey").length()<=0) {
            throw new ParameterErrorException();
        }
        //查询支付方式
        Payment payment = paymentDao.get(Payment.class, code);
        Payment paymentUpdate = payment;
        HashMap<String,String> paymentInfo = new HashMap<String, String>();
        paymentInfo.put("wxAppId", params.get("wxAppId"));
        paymentInfo.put("wxMchid", params.get("wxMchid"));
        paymentInfo.put("wxKey", params.get("wxKey"));
        paymentUpdate.setPaymentInfo(JsonHelper.toJson(paymentInfo));
        if (Integer.parseInt(params.get("paymentState")) == State.YES) {
            paymentUpdate.setPaymentState(State.YES);
        }else{
            paymentUpdate.setPaymentState(State.NO);
        }
        paymentDao.update(paymentUpdate);
        return true;
    }
    /**
     * 编辑货到付款支付方式
     * @param code
     * @param params
     * @return
     * @throws ShopException
     */
    public boolean editPaymentOffline(String code,HashMap<String,String> params) throws ShopException {
        //查询支付方式
        Payment payment = paymentDao.get(Payment.class, code);
        Payment paymentUpdate = payment;
        if (Integer.parseInt(params.get("paymentState")) == State.YES) {
            paymentUpdate.setPaymentState(State.YES);
        }else{
            paymentUpdate.setPaymentState(State.NO);
        }
        paymentDao.update(paymentUpdate);
        return true;
    }
    /**
     * 取得开启的在线支付列表
     * @return
     */
    public List<Payment> getOpenPaymentOnlineList() {
        //订单列表
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("paymentState = :paymentState");
        whereItems.add("paymentCode != :paymentCode");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("paymentState",State.YES);
        params.put("paymentCode", "offline");
        return paymentDao.getPaymentList(whereItems,params);
    }
    /**
     * 获得支付方式详情
     * @param paymentCode
     * @return
     */
    public HashMap<String, Object> getPaymentDetail(String paymentCode){
        Payment paymentInfo = paymentDao.getPaymentInfoByPaymentCode(paymentCode);
        HashMap<String, Object> paymentDetail = new HashMap<String, Object>();
        if (paymentInfo!=null) {
            paymentDetail.put("paymentCode", paymentInfo.getPaymentCode());
            paymentDetail.put("paymentName", paymentInfo.getPaymentName());
            paymentDetail.put("paymentState", paymentInfo.getPaymentState());
            paymentDetail.put("paymentInfo", paymentInfo.getPaymentInfo());
            if(paymentInfo.getPaymentInfo()!=null && paymentInfo.getPaymentInfo().length()>0){
                paymentDetail.put("paymentConfig", JsonHelper.toGenericObject(paymentInfo.getPaymentInfo(), new TypeReference<HashMap<String, String>>() {}));
            }
        }
        return paymentDetail;
    }
    /**
     * 取得所有支付方式编码和名称
     * @return
     */
    public HashMap<String,String> getAllPaymentList() {
        HashMap<String,String> hashMap = new HashMap<>();
        List<Payment> paymentList = paymentDao.findAll(Payment.class);
        if (paymentList.size()>0) {
            for (int i=0; i<paymentList.size(); i++) {
                hashMap.put(paymentList.get(i).getPaymentCode(),paymentList.get(i).getPaymentName());
            }
        }
        hashMap.put(OrdersPaymentCode.PREDEPOSIT, OrdersPaymentName.PREDEPOSIT);
        return hashMap;
    }
}