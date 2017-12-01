package net.shopnc.b2b2c.dao.orders;

import net.shopnc.b2b2c.domain.orders.Bill;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.util.PriceHelper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 结算
 * Created by shopnc on 2015/10/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class BillDao extends BaseDaoHibernate4<Bill> {

    /**
     * 取得店铺最后一条结算单信息
     * @param storeId 店铺ID
     * @return
     */
    public Bill getLastBillInfo(int storeId) {
        String hql = "from Bill where storeId = :storeId order by billId desc";
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("storeId",storeId);
        List<Bill> billList = super.find(hql,hashMap);
        return billList.size()>0 ? billList.get(0) : null;
    }

    /**
     * 计算结算单订单基本信息（订单金额、运费、佣金）
     * @param condition 查询条件
     * @param params 查询值
     * @return
     */
    public HashMap<String,BigDecimal> getCalcBillBaseItemsAmount(List<Object> condition, HashMap<String,Object> params) {
        String hql = "select new map(sum(ordersAmount) as ordersAmount,sum(freightAmount) as freightAmount,sum(commissionAmount) as commissionAmount) from Orders where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        List list = super.find(hql,params);
        HashMap<String,Object> hashMap = (HashMap<String,Object>)list.get(0);
        HashMap<String,BigDecimal> hashMap1 = new HashMap<>();
        if (hashMap.get("ordersAmount") != null) {
            hashMap1.put("ordersAmount",new BigDecimal(hashMap.get("ordersAmount").toString()));
            hashMap1.put("freightAmount",new BigDecimal(hashMap.get("freightAmount").toString()));
            hashMap1.put("commissionAmount",new BigDecimal(hashMap.get("commissionAmount").toString()));
        } else {
            hashMap1.put("ordersAmount",new BigDecimal(0));
            hashMap1.put("freightAmount", new BigDecimal(0));
            hashMap1.put("commissionAmount", new BigDecimal(0));
        }
        return hashMap1;
    }

    /**
     * 计算结算单退款基本信息（退款金额、退还佣金）
     * @param condition 查询条件
     * @param params 查询值
     * @return
     */
    public HashMap<String,BigDecimal> getCalcBillRefundItemsAmount(List<Object> condition, HashMap<String,Object> params) {
        String hql = "select new map(sum(refundAmount) as refundAmount,sum(refundAmount*commissionRate) as refundCommissionAmount) from Refund where 1=1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        List list = super.find(hql,params);
        HashMap<String,Object> hashMap = (HashMap<String,Object>)list.get(0);
        HashMap<String,BigDecimal> hashMap1 = new HashMap<>();
        if (hashMap.get("refundAmount") != null) {
            hashMap1.put("refundAmount",new BigDecimal(hashMap.get("refundAmount").toString()));
            hashMap1.put("refundCommissionAmount", PriceHelper.div(new BigDecimal(hashMap.get("refundCommissionAmount").toString()),100));
        } else {
            hashMap1.put("refundAmount",new BigDecimal(0));
            hashMap1.put("refundCommissionAmount", new BigDecimal(0));
        }
        return hashMap1;
    }

    /**
     * 取得记录数量
     * @param condition 查询条件
     * @param params 查询值
     * @return
     */
    public long getBillCount(List<Object> condition, HashMap<String,Object> params) {
        String hql = "select count(*) from Bill where 1 = 1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        return super.findCount(hql, params);
    }

    /**
     * 账单列表
     * @param condition 查询条件
     * @param params 查询值
     * @param pageNo 当前页
     * @param pageSize 每页显示数量
     * @return
     */
    public List<Bill> getBillList(List<Object> condition, HashMap<String,Object> params, int pageNo, int pageSize) {
        String hql = "from Bill where 1 = 1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by billId desc";
        return super.findByPage(hql, pageNo, pageSize, params);
    }

    /**
     * 取得结算单详情
     * @param condition 查询条件
     * @param params 查询值
     * @return
     */
    public Bill getBillInfo(List<Object> condition, HashMap<String,Object> params) {
        String hql = "from Bill where 1 = 1";
        for (int i=0; i<condition.size(); i++) {
            hql += (" and " + condition.get(i));
        }
        hql += " order by billId desc";
        List<Bill> billList = super.find(hql,params);
        return billList.size()>0 ? billList.get(0) : null;
    }

    /**
     * 更新生成结算单号
     * @param billId 结算单Id
     * @param billSn 结算单号
     */
    public void updateBillSn(int billId, int billSn) {
        String hql = "update Bill set billSn = :billSn where billId = :billId";
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("billSn",billSn);
        hashMap.put("billId",billId);
        super.update(hql,hashMap);
    }
}
