package net.shopnc.b2b2c.worker;

import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.b2b2c.service.refund.RefundService;
import net.shopnc.b2b2c.service.work.WorkBillService;
import net.shopnc.common.util.OrdersHelper;
import net.shopnc.common.util.ShopHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hbj on 2016/2/14.
 */
@Component
public class WorkDate {
    protected final Logger logger = Logger.getLogger(getClass());
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private WorkBillService workBillService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private RefundService refundService;

    public static void main(String[] args){
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        WorkDate p = context.getBean(WorkDate.class);
        try {
            p.refundConfirm();
            p.bill();
            p.ordersEvaluationExpire();
            p.ordersReceive();

        } catch (Exception ex) {
            p.logger.info(ex.toString());
        }
    }

    /**
     * 生成商品订单账单,只要店铺有订单(不考虑订单状态)数据，或费用数据，就生成结算单
     * @throws Exception
     */
    private void bill() throws Exception {
        logger.info("结算定时任务 开始执行");
        //依次生成每个店铺结算单
        long storeCount = storeDao.findCount(Store.class);
        logger.info(String.format("当前店铺数：%d",storeCount));
        for (int i=1; i<= storeCount; i++) {
            List<Store> storeList = storeDao.getStoreLimitList(i,1);
            if (storeList != null) {
                Store storeInfo = storeList.get(0);
                logger.info(String.format("开始生成 storeId=%d 的结算单,结算周期%s/%d",
                        storeInfo.getStoreId(),storeInfo.getBillCycleType() == BillCycleType.MONTH ? "月" : "天",storeInfo.getBillCycle()));
                //结算起始时间
                Timestamp startTime = workBillService.getStartTime(storeInfo.getStoreId());
                if (startTime != null) {
                    logger.info("本次结算单计算起始时间:" + startTime);
                    if (storeInfo.getBillCycleType() == BillCycleType.MONTH) {
                        //月结
                        workBillService.createBillByMonth(startTime,storeInfo);
                    } else {
                        //天结
                        workBillService.createBillByDate(startTime,storeInfo);
                    }
                } else {
                    logger.info("未计算出本次结算单计算起始时间,不生成结算单");
                }
            }
        }
        logger.info("结算定时任务 执行结束");
    }

    /**
     * 订单超期后不允许评价
     * @throws Exception
     */
    private void ordersEvaluationExpire() throws Exception {
        logger.info("订单超期后不允许评价定时任务 开始执行");
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String,Object> values = new HashMap<String, Object>();
        conditions.add("ordersState = " + OrdersOrdersState.FINISH);
        conditions.add("evaluationState = 0");
        conditions.add("finishTime < :finishTime");
        values.put("finishTime", ShopHelper.getFutureTimestamp(ShopHelper.getCurrentTimestamp(), Calendar.DATE, -Common.ORDER_EVALUATION_MAX_TIME));
        //分批，每批处理100个订单，最多处理5W个订单
        for (int i=0; i<500; i++) {
            List<Object> objectList = ordersDao.getOrdersList(conditions, values, 1, 100);
            logger.info("查到" + objectList.size() + "条待处理的未评价订单");
            for (Object object : objectList) {
                Orders orders = (Orders)object;
                orders.setEvaluationTime(ShopHelper.getCurrentTimestamp());
                orders.setEvaluationState(OrdersEvaluationState.YES);
                orders.setEvaluationAppendState(OrdersEvaluationState.YES);
                ordersDao.update(orders);
            }
            if (objectList.size() < 100) {
                break;
            }
        }
        logger.info("订单超期后不允许评价定时任务 结束执行");
    }

    /**
     * 订单超期自动收货
     * @throws Exception
     */
    private void ordersReceive() throws Exception {
        logger.info("订单超期自动收货定时任务 开始执行");
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String,Object> values = new HashMap<String, Object>();
        conditions.add("ordersState = " + OrdersOrdersState.SEND);
        conditions.add("lockState = 0");
        conditions.add("autoReceiveTime < :autoReceiveTime");
        values.put("autoReceiveTime", ShopHelper.getCurrentTimestamp());
        //分批，每批处理100个订单，最多处理5W个订单
        for (int i=0; i<500; i++) {
            List<Object> objectList = ordersDao.getOrdersList(conditions, values, 1, 100);
            logger.info("查到" + objectList.size() + "条超期待自动收货订单");
            for (Object object : objectList) {
                Orders orders = (Orders)object;
                if (OrdersHelper.operationValidate(orders, OrdersOperationType.AUTO_FINISH) == 0) {
                    throw new ShopException("无权操作订单超期自动收货");
                }
                orders.setFinishTime(ShopHelper.getCurrentTimestamp());
                ordersService.receiveOrders(orders);
            }
            if (objectList.size() < 100) {
                break;
            }
        }
        logger.info("订单超期自动收货定时任务 执行结束");
    }

    /**
     * 更新退款相关信息
     */
    private void refundConfirm() {
        logger.info("更新退款相关信息，定时任务开始执行");
        List<Refund> refundList = refundService.getRefundConfirm();
        if (refundList.size() > 0) {
            logger.info("退款退货自动处理提醒");
            for (Refund refund : refundList) {
                refund.setRefundState(RefundState.REFUND_STATE_CHECK);
                refund.setSellerState(RefundState.SELLER_STATE_AGREE);
                refund.setReturnType(RefundState.RETURN_TYPE_RETURNED_NO);
                refund.setSellerTime(ShopHelper.getCurrentTimestamp());
                refund.setSellerMessage("超过" + RefundService.REFUND_CONFIRM + "天未处理退款退货申请，按同意处理。");
                refundService.saveRefund(refund);

                HashMap<String ,Object> hashMap = new HashMap<>();
                hashMap.put("refundSn",refund.getRefundSn());
                //发送商家提醒
                if (refund.getRefundType() == RefundState.REFUND_TYPE_REFUND)
                {
                    refundService.sendStoreMessage(RefundService.STORE_REFUND_AUTO_PROCESS,refund.getMemberId(),hashMap,Integer.toString(refund.getRefundId()));
                }else{
                    refundService.sendStoreMessage(RefundService.STORE_RETURN_AUTO_PROCESS,refund.getMemberId(),hashMap,Integer.toString(refund.getRefundId()));
                }
            }
        }
        List<Refund> receiptRefundList = refundService.getReceiptRefundList();
        if (receiptRefundList.size() > 0 ){
            for (Refund refund : receiptRefundList){
                refund.setRefundState(RefundState.REFUND_STATE_CHECK);
                refund.setReturnType(RefundState.RETURN_TYPE_RETURNED_NO);
                refund.setSellerMessage("超过" + RefundService.REFUND_CONFIRM + "天未处理收货，按弃货处理。");

                refundService.saveRefund(refund);
                HashMap<String ,Object> hashMap = new HashMap<>();
                hashMap.put("refundSn",refund.getRefundSn());
                refundService.sendStoreMessage(RefundService.STORE_RETURN_AUTO_RECEIPT,refund.getMemberId(),hashMap,Integer.toString(refund.getRefundId()));
            }
        }
    }
}
