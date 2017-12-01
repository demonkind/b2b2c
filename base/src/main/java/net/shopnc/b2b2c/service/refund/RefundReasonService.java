package net.shopnc.b2b2c.service.refund;

import net.shopnc.b2b2c.dao.refund.RefundReasonDao;
import net.shopnc.b2b2c.domain.refund.RefundReason;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 退款退货原因
 * Created by cj on 2016/2/1.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class RefundReasonService extends BaseService {
    @Autowired
    private RefundReasonDao refundReasonDao;

    /**
     * 列表
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getRefundReasonDtGridListForAdmin(String dtGridPager) throws Exception {
        return refundReasonDao.getDtGridList(dtGridPager, RefundReason.class);
    }

    /**
     * 编辑
     *
     * @param refundReason
     */
    public void saveRefundReason(RefundReason refundReason) {
        refundReasonDao.update(refundReason);
    }

    /**
     * 新增
     *
     * @param refundReason
     * @return
     */
    public int addRefundReason(RefundReason refundReason) {
        return (Integer) refundReasonDao.save(refundReason);
    }

    /**
     * 单个删除
     *
     * @param reasonId
     * @throws ShopException
     */
    public void delRefundReason(int reasonId) throws ShopException {
        if (reasonId <= 0) {
            throw new ShopException("参数错误");
        }
        refundReasonDao.delete(RefundReason.class, reasonId);
    }

    /**
     * 根据id 获取退款信息详情
     * @param refundReasonId
     * @return
     */
    public RefundReason getRefundReasonInfoById(int refundReasonId)
    {
        return refundReasonDao.get(RefundReason.class,refundReasonId);
    }

}
