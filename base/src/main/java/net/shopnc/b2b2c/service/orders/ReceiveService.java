package net.shopnc.b2b2c.service.orders;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.dao.orders.OrdersPayDao;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersPay;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.common.entity.buy.OrdersPayEntity;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 第三方API支付返回订单通知
 * Created by hbj on 2016/2/2.
 */
@Service
public class ReceiveService extends BaseService {
    @Autowired
    private OrdersPayDao ordersPayDao;
    @Autowired
    private PayService payService;
    @Autowired
    private OrdersService ordersService;

    /**
     * 商品订单在线支付
     * @param paySn
     * @param outOrdersSn
     * @param paymentCode
     * @return
     * @throws Exception
     */
    public OrdersPayEntity onLineOrdersPay(String paySn, String outOrdersSn, String paymentCode) throws Exception {
        if (paySn == null) {
            throw new ShopException("参数错误");
        }
        OrdersPay ordersPay = ordersPayDao.getOrdersPayInfo(Long.parseLong(paySn));
        if (ordersPay == null) {
            throw new ShopException("订单不存在");
        }
        OrdersPayEntity ordersPayEntity = payService.getOrdersPayEntityInfo(ordersPay.getPayId());

        //如果支付成功
        if (PriceHelper.isEquals(ordersPayEntity.getOrdersOnlineDiffAmount(),new BigDecimal(0))) {
            return ordersPayEntity;
        }

        List<Orders> ordersList = ordersPayEntity.getOrdersList();
        for(int i=0; i<ordersList.size(); i++) {
            if (ordersList.get(i).getOrdersState() != OrdersOrdersState.NEW) {
                continue;
            }
            ordersList.get(i).setOutOrdersSn(outOrdersSn);
            ordersList.get(i).setPaymentCode(paymentCode);
            ordersList.get(i).setPaymentTime(ShopHelper.getCurrentTimestamp());
            ordersService.payOrders(ordersList.get(i));
        }
        return ordersPayEntity;
    }
}
