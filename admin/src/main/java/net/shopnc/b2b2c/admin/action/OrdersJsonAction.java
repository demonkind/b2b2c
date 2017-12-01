package net.shopnc.b2b2c.admin.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.OrdersPaymentCode;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.b2b2c.service.orders.AdminOrdersService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by hou on 2016/3/14.
 */
@Controller
public class OrdersJsonAction extends BaseJsonAction {
    @Autowired
    private AdminOrdersService adminOrdersService;
    @Autowired
    private PaymentService paymentService;

    /**
     * 订单列表
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orders/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager){
        DtGrid dtGrid = new DtGrid();
        try {
            ObjectMapper mapper = new ObjectMapper();
            dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
            dtGrid = adminOrdersService.getOrdersVoDtGridList(dtGrid);
        } catch (Exception e) {
            logger.error("whereHql:" + dtGrid.getWhereHql());
            logger.error("sortHql:" + dtGrid.getSortHql());
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 取消订单
     * @param ordersId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orders/cancel", method = RequestMethod.POST)
    public ResultEntity cancel(@RequestParam(value = "ordersId", required = true) Integer ordersId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "orders/list");
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            adminOrdersService.cancelOrders(ordersId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.equals(e.toString());
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 收款
     * @param ordersId
     * @param paymentTime
     * @param paymentCode
     * @param outOrdersSn
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orders/pay", method = RequestMethod.POST)
    public ResultEntity confirmPay(@RequestParam(value = "ordersId", required = true) Integer ordersId,
                                   @RequestParam(value = "paymentTime",required = true) String paymentTime,
                                   @RequestParam(value = "paymentCode",required = true) String paymentCode,
                                   @RequestParam(value = "outOrdersSn",required = true) String outOrdersSn) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "orders/list");
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            //验证支付方式
            HashMap<String, String> hashMap = paymentService.getAllPaymentList();
            hashMap.remove(OrdersPaymentCode.OFFLINE);
            hashMap.remove(OrdersPaymentCode.PREDEPOSIT);
            if (hashMap.containsKey(paymentCode) == false) {
                throw new ShopException("支付方式错误");
            }
            adminOrdersService.payOrders(ordersId, paymentTime, paymentCode, outOrdersSn);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.equals(e.toString());
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
}
