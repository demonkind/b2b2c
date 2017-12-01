package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.OrdersPaymentCode;
import net.shopnc.b2b2c.dao.orders.BillDao;
import net.shopnc.b2b2c.domain.orders.Bill;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.b2b2c.service.bill.BillService;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hbj on 2016/2/16.
 */
@Controller
public class BillAction extends BaseAction {
    @Autowired
    private BillDao billDao;
    @Autowired
    private BillService billService;
    @Autowired
    private PaymentService paymentService;

    /**
     * 结算列表
     */
    public BillAction() {
        setMenuPath("bill/list");
    }
    @RequestMapping(value = "bill/list", method = RequestMethod.GET)
    public String list(@RequestParam(name = "type",defaultValue = "") String type,ModelMap modelMap) {

        //账单状态列表
        HashMap<Integer,String> hashMap = billService.getBillStateList();
        modelMap.put("billStateListJson", JsonHelper.toJson(hashMap));

        //支付方式列表
        HashMap<String,String> hashMap1 = paymentService.getAllPaymentList();
        hashMap1.remove(OrdersPaymentCode.OFFLINE);
        modelMap.put("paymentList", hashMap1);

        modelMap.put("type",type);

        return getAdminTemplate("bill/list");
    }

    /**
     * 结算单详情 - 订单列表
     * @param billId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "bill/orders/list/{billId}", method = RequestMethod.GET)
    public String ordersList(@PathVariable Integer billId, ModelMap modelMap) {
        Bill bill = billDao.get(Bill.class,billId);
        modelMap.put("bill", bill);
        modelMap.put("billJson", JsonHelper.toJson(bill));

        //支付方式列表
        HashMap<String,String> hashMap = paymentService.getAllPaymentList();
        modelMap.put("paymentListJson", JsonHelper.toJson(hashMap));

        return getAdminTemplate("bill/orders/list");
    }

    /**
     * 结算单详情 - 退单列表
     * @param billId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "bill/refund/list/{billId}", method = RequestMethod.GET)
    public String refundList(@PathVariable Integer billId, ModelMap modelMap) {
        Bill bill = billDao.get(Bill.class, billId);
        modelMap.put("bill", bill);
        modelMap.put("billJson", JsonHelper.toJson(bill));

        return getAdminTemplate("bill/refund/list");
    }

    /**
     * 打印结算单
     * @param billId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "bill/print/{billId}", method = RequestMethod.GET)
    public String print(@PathVariable Integer billId,ModelMap modelMap) {
        List<Object> condition = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        condition.add("billId = :billId");
        params.put("billId", billId);
        Bill bill = billDao.getBillInfo(condition,params);
        if (bill == null) {
            return "redirect:/bill/list";
        }
        modelMap.put("bill", bill);
        return getAdminTemplate("bill/print");
    }

}
