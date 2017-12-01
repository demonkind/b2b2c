package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.domain.Payment;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016/01/05
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class PaymentDao extends BaseDaoHibernate4<Payment> {

    /**
     * 获取以Code为key的支付方式HashMap
     * @return 支付方式HashMap
     */
    public HashMap<String, Object> getPaymentMap() {
        List<Payment> paymentList = super.findAll(Payment.class);
        HashMap<String, Object> paymentMap = new HashMap<String, Object>();
        if (paymentList==null || paymentList.size()<=0) {
            paymentMap = null;
        }else{
            for (int i = 0; i < paymentList.size(); i++) {
                paymentMap.put(paymentList.get(i).getPaymentCode(), paymentList.get(i));
            }
        }
        return paymentMap;
    }

    /**
     * 支付方式列表
     * @param whereItems 查询条件
     * @param params HQL参数值
     * @return 支付方式列表
     */
    public List<Payment> getPaymentList(List<Object> whereItems,HashMap<String,Object> params) {
        String hql = "from Payment where 1=1";
        for (int i=0; i<whereItems.size(); i++) {
            hql += (" and " + whereItems.get(i));
        }
        return super.find(hql, params);
    }

    /**
     * 支付方式详情
     * @param paymentCode 支付方式code
     * @return 支付方式实体
     */
    public Payment getPaymentInfoByPaymentCode(String paymentCode) {
        String hql = "from Payment where paymentCode = :paymentCode";
        HashMap<String, Object> params = new HashMap();
        params.put("paymentCode", paymentCode);
        List<Payment> paymentList = super.find(hql, params);
        Payment payment = null;
        if (paymentList!=null && paymentList.size()>0) {
            payment = paymentList.get(0);
        }
        return payment;
    }

}
