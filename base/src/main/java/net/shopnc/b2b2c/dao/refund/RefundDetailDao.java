package net.shopnc.b2b2c.dao.refund;

import net.shopnc.b2b2c.domain.refund.RefundDetail;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 退款原因dao
 * Created by cj on 2016/2/1.
 */

@Repository
@Transactional(rollbackFor = {Exception.class})
public class RefundDetailDao extends BaseDaoHibernate4<RefundDetail> {

    public RefundDetail getRefundDetail(String batchNo){
        String hql = "from RefundDetail where batchNo=:batchNo";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("batchNo",batchNo);
        List<RefundDetail> list = super.find(hql, hashMap);
        return list.size()>0 ? list.get(0) : null;
    }

}
