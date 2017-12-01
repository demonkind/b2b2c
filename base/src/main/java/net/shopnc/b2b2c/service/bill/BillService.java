package net.shopnc.b2b2c.service.bill;

import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.dao.orders.BillDao;
import net.shopnc.b2b2c.domain.orders.Bill;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 结算单
 * Created by hbj on 2016/2/16.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BillService extends BaseService {
    @Autowired
    private BillDao billDao;
    /**
     * 账单状态搜索列表
     * @return
     */
    public HashMap<Integer,String> getBillStateList() {
        HashMap<Integer,String> hashMap = new HashMap<>();
        hashMap.put(BillState.NEW,BillStateName.NEW);
        hashMap.put(BillState.CONFIRM,BillStateName.CONFIRM);
        hashMap.put(BillState.ACCESS,BillStateName.ACCESS);
        hashMap.put(BillState.PAY,BillStateName.PAY);
        return hashMap;
    }

    /**
     * 结算单操作权限判断
     * @param bill
     * @param operation
     * @return
     */
    public static int operationValidate(Bill bill, int operation) {
        boolean validate = false;
        switch (operation) {
            //商家确认
            case BillOperationType.CONFIRM:
                validate = bill.getBillState() == BillState.NEW;
                break;
            //平台确认
            case BillOperationType.ACCESS:
                validate = bill.getBillState() == BillState.CONFIRM;
                break;
            //付款
            case BillOperationType.PAY:
                validate = bill.getBillState() == BillState.ACCESS;
                break;
        }
        return validate == true ? 1 : 0;
    }

    /**
     * 商家确认账单
     * @param billId
     */
    public void confirmBill(int billId) throws Exception {
        Bill bill = billDao.get(Bill.class, billId);
        int validate = operationValidate(bill,BillOperationType.CONFIRM);
        if (validate == 0) {
            throw new ShopException("无权操作");
        }
        bill.setBillState(BillState.CONFIRM);
        billDao.update(bill);
    }

    /**
     * 平台审核账单
     * @param billId
     */
    public void accessBill(int billId) throws Exception {
        Bill bill = billDao.get(Bill.class, billId);
        int validate = operationValidate(bill,BillOperationType.ACCESS);
        if (validate == 0) {
            throw new ShopException("无权操作");
        }
        bill.setBillState(BillState.ACCESS);
        billDao.update(bill);
    }

    /**
     * 平台支付账单
     * @param billId
     * @param paymentTime
     * @param paymentNote
     * @throws Exception
     */
    public void payBill(int billId, String paymentTime, String paymentNote) throws Exception {
        Bill bill = billDao.get(Bill.class, billId);
        int validate = operationValidate(bill,BillOperationType.PAY);
        if (validate == 0) {
            throw new ShopException("无权操作");
        }
        bill.setBillState(BillState.PAY);
        bill.setPaymentTime(Timestamp.valueOf(paymentTime));
        bill.setPaymentNote(paymentNote);
        billDao.update(bill);
    }

    /**
     * 店铺未确认账单数
     * @param storeId
     * @return
     */
    public long getBillNewCountByStoreId(int storeId){
        List<Object> where = new ArrayList<Object>();
        where.add("storeId = :storeId");
        where.add("billState = :billState");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        params.put("billState", BillState.NEW);
        return billDao.getBillCount(where, params);
    }

    /**
     * 平台待确认账单数
     * @return
     */
    public long getBillConfirmCount(){
        List<Object> where = new ArrayList<Object>();
        where.add("billState = :billState");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("billState", BillState.CONFIRM);
        return billDao.getBillCount(where, params);
    }

    /**
     * 平台待付款账单数
     * @return
     */
    public long getBillAccessCount(){
        List<Object> where = new ArrayList<Object>();
        where.add("billState = :billState");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("billState", BillState.ACCESS);
        return billDao.getBillCount(where, params);
    }

}
