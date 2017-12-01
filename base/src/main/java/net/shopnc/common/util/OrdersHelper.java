package net.shopnc.common.util;

import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.domain.orders.Orders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;

/**
 * 订单操作权限验证
 * Created by hbj on 2016/1/4.
 */
public class OrdersHelper {
    public static int operationValidate(Orders orders, int operation) {
        boolean validate = false;
        switch (operation) {
            //买家[取消]
            case OrdersOperationType.MEMBER_CANCEL:
                validate = orders.getOrdersState() == OrdersOrdersState.NEW ||
                        (orders.getOrdersState() == OrdersOrdersState.PAY && orders.getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE));
                break;
            //买家[支付]
            case OrdersOperationType.MEMBER_PAY:
                validate = orders.getOrdersState() == OrdersOrdersState.NEW && orders.getPaymentTypeCode().equals(OrdersPaymentTypeCode.ONLINE);
                break;
            //买家[评价]
            case OrdersOperationType.MEMBER_EVALUATION:
                validate = orders.getOrdersState() == OrdersOrdersState.FINISH && orders.getEvaluationState() == OrdersEvaluationState.NO;
                if (validate && orders.getFinishTime() != null) {
                    Timestamp endTime = new Timestamp(orders.getFinishTime().getTime()+60*60*1000*24*Common.ORDER_EVALUATION_MAX_TIME);
                    validate = endTime.compareTo(ShopHelper.getCurrentTimestamp()) > 0;
                } else {
                    validate = false;
                }
                break;
            //买家[追加评价]
            case OrdersOperationType.MEMBER_EVALUATION_APPEND:
                validate = orders.getOrdersState() == OrdersOrdersState.FINISH &&
                        orders.getEvaluationState() == OrdersEvaluationState.YES &&
                        orders.getEvaluationAppendState() == OrdersEvaluationState.NO;
                if (validate && orders.getFinishTime() != null && orders.getEvaluationTime() != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, -Common.ORDER_EVALUATION_APPEND_MAX_TIME);
                    Timestamp EvalTime = new Timestamp(cal.getTime().getTime());
                    validate = EvalTime.compareTo(orders.getFinishTime()) < 0;
                } else {
                    validate = false;
                }
                break;
            //买家[收货]
            case OrdersOperationType.MEMBER_RECEIVE:
                validate = orders.getOrdersState() == OrdersOrdersState.SEND;
                break;
            //买家[延迟收货]
            case OrdersOperationType.MEMBER_DELAY_RECEIVE:
                validate = orders.getOrdersState() == OrdersOrdersState.SEND && orders.getDelayReceiveState() == State.NO && orders.getLockState() == State.NO;
                break;
            //商家[取消]
            case OrdersOperationType.STORE_CANCEL:
                validate = orders.getOrdersState() == OrdersOrdersState.NEW ||
                        (
                                orders.getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE) &&
                                        (orders.getOrdersState() == OrdersOrdersState.PAY || orders.getOrdersState() == OrdersOrdersState.SEND)
                        );
                break;
            //商家[修改运费]
            case OrdersOperationType.STORE_MODIFY_FREIGHT:
                validate = PriceHelper.isGreaterThan(orders.getFreightAmount(),new BigDecimal(0)) &&
                        (
                                orders.getOrdersState() == OrdersOrdersState.NEW ||
                                        (
                                                orders.getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE) && orders.getOrdersState() == OrdersOrdersState.PAY
                                        )
                        );
                break;
            //商家[发货]
            case OrdersOperationType.STORE_SEND:
                validate = orders.getOrdersState() == OrdersOrdersState.PAY;
                break;
            //商家[编辑发货信息]
            case OrdersOperationType.STORE_SEND_MODIFY:
                validate = orders.getOrdersState() == OrdersOrdersState.SEND;
                break;
            //商家[编辑收货人信息]
            case OrdersOperationType.STORE_MODIFY_RECEIVER:
                validate = orders.getOrdersState() == OrdersOrdersState.PAY || orders.getOrdersState() == OrdersOrdersState.SEND;
                break;

            //管理员[取消订单]
            case OrdersOperationType.ADMIN_CANCEL:
                validate = orders.getOrdersState() == OrdersOrdersState.NEW ||
                        (orders.getOrdersState() == OrdersOrdersState.PAY && orders.getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE));
                break;
            case OrdersOperationType.ADMIN_PAY:
                validate = orders.getOrdersState() == OrdersOrdersState.NEW;
                break;
            //系统自动操作
            case OrdersOperationType.AUTO_CANCEL:
                validate = orders.getOrdersState() == OrdersOrdersState.NEW ||
                        (orders.getOrdersState() == OrdersOrdersState.PAY && orders.getPaymentTypeCode().equals(OrdersPaymentTypeCode.OFFLINE));
                break;
            case OrdersOperationType.AUTO_FINISH:
                validate = orders.getOrdersState() == OrdersOrdersState.SEND;
                break;
            //查看物流
            case OrdersOperationType.SHIP_SEARCH:
                validate = orders.getShipSn() != null && Arrays.asList(OrdersOrdersState.SEND,OrdersOrdersState.FINISH).contains(orders.getOrdersState());
                break;
        }
        return validate == true ? 1 : 0;

    }
}
