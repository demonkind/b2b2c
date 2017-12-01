package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.BillState;
import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.dao.orders.BillDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.refund.RefundDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.orders.Bill;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.common.entity.PageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 结算
 * Created by hbj on 2016/2/17.
 */
@Controller
public class BillAction extends BaseAction {
    private static int PAGESIZE = 20;
    @Autowired
    private BillDao billDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private RefundDao refundDao;
    public BillAction() {
        setMenuPath("bill/list");
    }

    /**
     * 商家结算单列表
     * @param page
     * @param billId
     * @param billState
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "bill/list", method = RequestMethod.GET)
    public String billList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value = "billId",required = false) Integer billId,
                       @RequestParam(value = "billState", required = false) Integer billState,
                       ModelMap modelMap) {

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("bill/list", "结算管理");
        modelMap.put("sellerTabMenuMap", tabMenuMap);

        List<Integer> ordersStateList = Arrays.asList(BillState.NEW,BillState.CONFIRM,BillState.ACCESS,BillState.PAY);
        if (billState != null && ordersStateList.contains(billState) == false) {
            return "redirect:/bill/list";
        }

        //店铺信息，取结算周期
        Store store = storeDao.get(Store.class, SellerSessionHelper.getStoreId());

        List<Object> condition = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        condition.add("storeId = :storeId");
        params.put("storeId",SellerSessionHelper.getStoreId());
        if (billId != null) {
            condition.add("billId = :billId");
            params.put("billId",billId);
        }
        if (billState != null) {
            condition.add("billState = :billState");
            params.put("billState",billState);
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(billDao.getBillCount(condition, params));
        pageEntity.setPageNo(page);
        pageEntity.setPageSize(PAGESIZE);
        List<Bill> billList = billDao.getBillList(condition,params, page, PAGESIZE);
        modelMap.put("billList",billList);
        modelMap.put("showPage", pageEntity.getPageHtml());
        modelMap.put("store",store);
        return getSellerTemplate("bill/list");
    }

    /**
     * 账单下的订单列表
     * @param billId
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "bill/orders/list/{billId}", method = RequestMethod.GET)
    public String ordersList(@PathVariable Integer billId,
                       @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, ModelMap modelMap) {

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("bill/list", "结算管理");
        tabMenuMap.put("bill/orders/list/" + billId, "账单详情");
        modelMap.put("sellerTabMenuMap", tabMenuMap);

        //结算单信息
        List<Object> condition = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        condition.add("billId = :billId");
        condition.add("storeId = :storeId");
        params.put("billId", billId);
        params.put("storeId",SellerSessionHelper.getStoreId());
        Bill bill = billDao.getBillInfo(condition,params);
        if (bill == null) {
            return "redirect:/bill/list";
        }

        //订单列表
        condition = new ArrayList<Object>();
        params = new HashMap<String, Object>();
        condition.add("storeId = :storeId");
        condition.add("ordersState = " + OrdersOrdersState.FINISH);
        condition.add("finishTime >= :startTime");
        condition.add("finishTime <= :endTime");
        params.put("storeId", SellerSessionHelper.getStoreId());
        params.put("startTime", bill.getStartTime());
        params.put("endTime", bill.getEndTime());

        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(ordersDao.getOrdersCount(condition, params));
        pageEntity.setPageNo(page);
        List<Object> ordersVoList = ordersDao.getOrdersVoList(condition, params, page, PAGESIZE);
        modelMap.put("ordersVoList", ordersVoList);
        modelMap.put("bill", bill);
        modelMap.put("showPage", pageEntity.getPageHtml());
        return getSellerTemplate("bill/orders/list");
    }

    /**
     * 账单下的退单列表
     * @param billId
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "bill/refund/list/{billId}", method = RequestMethod.GET)
    public String refundList(@PathVariable Integer billId,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, ModelMap modelMap) {

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("bill/list", "结算管理");
        tabMenuMap.put("bill/refund/list/" + billId, "账单详情");
        modelMap.put("sellerTabMenuMap", tabMenuMap);

        //结算单信息
        List<Object> condition = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        condition.add("billId = :billId");
        condition.add("storeId = :storeId");
        params.put("billId", billId);
        params.put("storeId",SellerSessionHelper.getStoreId());
        Bill bill = billDao.getBillInfo(condition,params);
        if (bill == null) {
            return "redirect:/bill/list";
        }

        //退单列表
        condition = new ArrayList<Object>();
        params = new HashMap<String, Object>();
        condition.add("storeId = :storeId");
        condition.add("adminTime >= :startTime");
        condition.add("adminTime <= :endTime");
        condition.add("sellerState = 2");
        condition.add("goodsId > 0");
        params.put("storeId", SellerSessionHelper.getStoreId());
        params.put("startTime", bill.getStartTime());
        params.put("endTime", bill.getEndTime());

        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(refundDao.getRefundListCount(condition, params));
        pageEntity.setPageNo(page);
        List<Object> refundVoList = refundDao.getRefundVoList(condition, params, page, PAGESIZE);
        modelMap.put("refundVoList", refundVoList);
        modelMap.put("bill", bill);
        modelMap.put("showPage", pageEntity.getPageHtml());
        return getSellerTemplate("bill/refund/list");
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
        condition.add("storeId = :storeId");
        params.put("billId", billId);
        params.put("storeId",SellerSessionHelper.getStoreId());
        Bill bill = billDao.getBillInfo(condition,params);
        if (bill == null) {
            return "redirect:/bill/list";
        }
        modelMap.put("bill", bill);
        return getSellerTemplate("bill/print");
    }
}
