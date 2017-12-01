package net.shopnc.b2b2c.dao.refund;

import net.shopnc.b2b2c.domain.refund.RefundReason;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 退款原因dao
 * Created by cj on 2016/2/1.
 */

@Repository
@Transactional(rollbackFor = {Exception.class})
public class RefundReasonDao extends BaseDaoHibernate4<RefundReason> {

    /**
     * 获取全部退款退货原因
     * @return
     */
    public List<RefundReason> getRefundReasonList() {
        String hql = "from RefundReason where 1=1";
        List<RefundReason> refundReasonList = super.find(hql);
        return refundReasonList;
    }

}
