package net.shopnc.b2b2c.dao.orders;

import net.shopnc.b2b2c.domain.orders.Invoice;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 发票
 * Created by shopnc on 2015/10/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class InvoiceDao extends BaseDaoHibernate4<Invoice> {

    /**
     * 取得会员发票列表
     * @param memberId
     * @return
     */
    public List<Invoice> getInvoiceList(int memberId) {
        String hql = "from Invoice where memberId = :memberId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId", memberId);
        return super.find(hql, hashMap);
    }

    /**
     * 删除发票
     * @param invoiceId
     * @param memberId
     */
    public void delInvoice(int invoiceId, int memberId) {
        String hql = "delete from Invoice where invoiceId = :invoiceId and memberId = :memberId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("invoiceId",invoiceId);
        hashMap.put("memberId", memberId);
        super.delete(hql, hashMap);
    }

    /**
     * 取得单条信息
     * @param invoiceId
     * @param memberId
     * @return
     */
    public Invoice getInvoiceInfo(int invoiceId, int memberId) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        String hql = "from Invoice where invoiceId = :invoiceId and memberId = :memberId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("invoiceId",invoiceId);
        hashMap.put("memberId", memberId);
        List<Invoice> list = super.find(hql, hashMap);
        return list.size()>0 ? list.get(0) : null;
    }

    /**
     * 取得发票数量
     * @param memberId
     * @return
     */
    public long getInvoiceCount(int memberId) {
        String hql = "select count(*) from Invoice where memberId = :memberId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId",memberId);
        return super.findCount(hql,hashMap);
    }

}
