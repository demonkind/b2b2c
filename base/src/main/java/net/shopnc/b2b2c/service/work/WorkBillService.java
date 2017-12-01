package net.shopnc.b2b2c.service.work;

import net.shopnc.b2b2c.constant.BillCycleType;
import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.dao.orders.BillDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.refund.RefundDao;
import net.shopnc.b2b2c.domain.orders.Bill;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hbj on 2016/2/14.
 */
@Service
public class WorkBillService extends BaseService {
    @Autowired
    private BillDao billDao;
    @Autowired
    private OrdersDao ordersDao;

    /**
     * 取得生成店铺结算单的起始时间(某天的00:00:00)
     * @param storeId
     * @return
     */
    public Timestamp getStartTime(int storeId) {
        Timestamp timestamp = null;
        //从结算表中取得上次该店铺账单的截止时间，如果未查询到记录，则查询订单表中该店铺的最早订单记录(不考虑订单状态)
        Bill billInfo = billDao.getLastBillInfo(storeId);
        if (billInfo != null) {
            timestamp = billInfo.getEndTime();
        } else {
            Orders ordersInfo = ordersDao.getFirstOrdersInfo(storeId);
            if (ordersInfo != null) {
                timestamp = ordersInfo.getCreateTime();
            }
        }
        if (timestamp != null) {
            timestamp = ShopHelper.getFutureTimestamp(timestamp, Calendar.SECOND, 1);
        }
        return timestamp;
    }

    public void createBillByMonth(Timestamp startTime, Store storeInfo) throws Exception {
        //结算周期N个月
        int billCycle = storeInfo.getBillCycle();
        //当前月的1号00:00:00
        Timestamp currentMonthFirstTime = getMonthStartTime(ShopHelper.getCurrentTimestamp());

        //计数变量
        int i = 1;
        if (ShopHelper.getFutureTimestamp(startTime, Calendar.MONTH,billCycle-1).compareTo(currentMonthFirstTime) < 0) {
            while (ShopHelper.getFutureTimestamp(startTime, Calendar.MONTH,billCycle-1).compareTo(currentMonthFirstTime) < 0) {
                Timestamp itemTime = ShopHelper.getFutureTimestamp(startTime, Calendar.MONTH, billCycle-1);
                Timestamp billStartTime;
                Timestamp billEndTime;
                //结算单结束时间 = 结束月最后一天23:59:59
                billStartTime = i == 1 ? startTime : getMonthStartTime(startTime);
                billEndTime = getMonthLastTime(itemTime);

                //自动加N个月
                startTime = ShopHelper.getFutureTimestamp(startTime, Calendar.MONTH, billCycle);
                logger.info("开始生成结算单,起止时间: " + billStartTime + " - " + billEndTime);
                i++;
                createBill(storeInfo, billStartTime, billEndTime);
            }
        } else {
            logger.info("起始时间距离现在不足一个结算周期,不生成结算单");
        }

    }

    public void createBillByDate(Timestamp startTime, Store storeInfo) throws Exception {
        //结算周期N天
        int billCycle = storeInfo.getBillCycle();

        //今天00:00:00
        Timestamp currentMonthFirstTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());

        //计数变量
        int i = 1;
        if (ShopHelper.getFutureTimestamp(startTime, Calendar.DATE,billCycle-1).compareTo(currentMonthFirstTime) < 0) {
            while (ShopHelper.getFutureTimestamp(startTime, Calendar.DATE,billCycle-1).compareTo(currentMonthFirstTime) < 0) {
                Timestamp itemTime = ShopHelper.getFutureTimestamp(startTime, Calendar.DATE, billCycle-1);
                Timestamp billStartTime;
                Timestamp billEndTime;
                //结算单结束时间 = 结束天23:59:59
                billStartTime = i == 1 ? startTime : ShopHelper.getTimestampOfDayStart(startTime);;
                billEndTime = ShopHelper.getTimestampOfDayEnd(itemTime);

                //自动加N天
                startTime = ShopHelper.getFutureTimestamp(startTime, Calendar.DATE, billCycle);
                logger.info("开始生成结算单,起止时间: " + billStartTime + " - " + billEndTime);
                i++;
                createBill(storeInfo, billStartTime, billEndTime);
            }
        } else {
            logger.info("起始时间距离现在不足一个结算周期,不生成结算单");
        }

    }

    /**
     * 计算结算单所需要的各项统计数据
     * @param storeInfo
     * @param startTime
     * @param endTime
     * @return
     */
    private void createBill(Store storeInfo, Timestamp startTime, Timestamp endTime) {

        logger.info("开始计算订单总金额、运费总金额、佣金总金额");
        //订单总金额
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String,Object> values = new HashMap<String, Object>();
        conditions.add("storeId = :storeId");
        conditions.add("ordersState = " + OrdersOrdersState.FINISH);
        conditions.add("finishTime >= :startTime");
        conditions.add("finishTime <= :endTime");
        values.put("storeId", storeInfo.getStoreId());
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        HashMap<String,BigDecimal> hashMapOrders = billDao.getCalcBillBaseItemsAmount(conditions, values);

        logger.info("开始计算退款金额、返还佣金");
        conditions = new ArrayList<Object>();
        values = new HashMap<String, Object>();
        conditions.add("storeId = :storeId");
        conditions.add("sellerState = 2");
        conditions.add("adminTime >= :startTime");
        conditions.add("adminTime <= :endTime");
        conditions.add("goodsId > 0");
        values.put("storeId", storeInfo.getStoreId());
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        HashMap<String,BigDecimal> hashMapRefund = billDao.getCalcBillRefundItemsAmount(conditions, values);

        Bill bill = new Bill();
        bill.setOrdersAmount(hashMapOrders.get("ordersAmount"));
        bill.setFreightAmount(hashMapOrders.get("freightAmount"));
        bill.setCommissionAmount(hashMapOrders.get("commissionAmount"));
        bill.setRefundAmount(hashMapRefund.get("refundAmount"));
        bill.setRefundCommissionAmount(hashMapRefund.get("refundCommissionAmount"));

        //订单金额 - 佣金
        BigDecimal billAmount = PriceHelper.sub(bill.getOrdersAmount(), bill.getCommissionAmount());
        //-退款
        billAmount = PriceHelper.sub(billAmount,bill.getRefundAmount());
        //+退还佣金
        billAmount = PriceHelper.add(billAmount,bill.getRefundCommissionAmount());
        //本期应结
        bill.setBillAmount(billAmount);

        bill.setStoreId(storeInfo.getStoreId());
        bill.setStoreName(storeInfo.getStoreName());
        bill.setCreateTime(ShopHelper.getCurrentTimestamp());
        bill.setStartTime(startTime);
        bill.setEndTime(endTime);

        logger.info("正在将结算数据保存到结算表");
        int billId = (Integer)billDao.save(bill);
        if (billId > 0) {
            logger.info("成功生成结算单，单号:" + billId);
            billDao.updateBillSn(billId, makeBillSn(billId));
            logger.info("成功生成结算单号，单号:" + makeBillSn(billId));
        }
    }

    /**
     * 生成 0 元结算单
     * @param storeInfo
     */
//    private void createZeroBill(Store storeInfo) {
//        if (storeInfo.getBillCycleType() == BillCycleType.MONTH) {
//            //月结
//
//        } else {
//            //天结
//        }
//        Bill bill = new Bill();
//        bill.setStoreId(storeInfo.getStoreId());
//        bill.setStoreName(storeInfo.getStoreName());
//        bill.setCreateTime(ShopHelper.getCurrentTimestamp());
//        bill.setStartTime(startTime);
//        bill.setEndTime(endTime);
//
//        logger.info("正在将结算数据保存到结算表");
//        int billId = (Integer)billDao.save(bill);
//        if (billId > 0) {
//            logger.info("成功生成结算单，单号:" + billId);
//        }
//    }

    /**
     * 取得当前时间所在月的1号00:00:00
     * @param timestamp
     * @return
     */
    private Timestamp getMonthStartTime(Timestamp timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-01 00:00:00");
        Timestamp timestamp1 = Timestamp.valueOf(simpleDateFormat.format(timestamp));
        return timestamp1;
    }

    /**
     * 取得当前时间所在月的1号23:59:59(下月第一天00:00:00减1秒)
     * @param timestamp
     * @return
     */
    private Timestamp getMonthLastTime(Timestamp timestamp) {
        timestamp = ShopHelper.getFutureTimestamp(timestamp, Calendar.MONTH,1);
        SimpleDateFormat simpleFirstDateFormat = new SimpleDateFormat("yyyy-MM-01 00:00:00");
        Timestamp timestamp1 = Timestamp.valueOf(simpleFirstDateFormat.format(timestamp));
        timestamp1 = ShopHelper.getFutureTimestamp(timestamp1, Calendar.SECOND, -1);
        return timestamp1;
    }

    /**
     * 生成账单编号
     * @param billId
     * @return
     */
    private int makeBillSn(int billId) {
        String sn = Integer.toString(1) + String.format("%07d",billId);
        return Integer.parseInt(sn);
    }
}
