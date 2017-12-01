package net.shopnc.b2b2c.worker;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.OrdersOperationRole;
import net.shopnc.b2b2c.constant.OrdersOperationType;
import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.common.util.OrdersHelper;
import net.shopnc.common.util.ShopHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hbj on 2016/2/18.
 */
@Component
public class WorkHour {
    protected final Logger logger = Logger.getLogger(getClass());
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrdersService ordersService;

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        WorkHour p = context.getBean(WorkHour.class);
        try {
            p.ordersAutoCancel();
        } catch (Exception ex) {
            p.logger.info(ex.toString());
        }
    }
    /**
     * 超期未支付订单自动取消
     * @throws Exception
     */
    private void ordersAutoCancel() throws Exception {
        logger.info("超期未支付订单定时任务 开始执行");
        //订单总金额
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String,Object> values = new HashMap<String, Object>();
        conditions.add("ordersState = " + OrdersOrdersState.NEW);
        conditions.add("createTime < :createTime");
        values.put("createTime", ShopHelper.getFutureTimestamp(ShopHelper.getCurrentTimestamp(), Calendar.SECOND, -Common.ORDER_AUTO_CANCEL_TIME*3600));
        //分批，每批处理100个订单，最多处理5W个订单
        for (int i=0; i<500; i++) {
            List<Object> objectList = ordersDao.getOrdersList(conditions,values,1,100);
            logger.info("查到" + objectList.size() + "条待取消订单");
            for (Object object : objectList) {
                Orders orders = (Orders)object;
                if (OrdersHelper.operationValidate(orders, OrdersOperationType.AUTO_CANCEL) == 0) {
                    throw new ShopException("无权操作超期未支付订单自动取消");
                }
                orders.setCancelRole(OrdersOperationRole.AUTO);
                orders.setCancelTime(ShopHelper.getCurrentTimestamp());
                ordersService.cancelOrders(orders);
            }
            if (objectList.size() < 100) {
                break;
            }
        }
        logger.info("超期未支付订单定时任务 结束执行");

    }
}
