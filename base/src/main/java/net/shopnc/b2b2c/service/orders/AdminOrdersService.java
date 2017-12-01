package net.shopnc.b2b2c.service.orders;

import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.entity.dtgrid.QueryUtils;
import net.shopnc.common.util.OrdersHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 后台订单处理专用
 * Created by hbj on 2016/1/26.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AdminOrdersService extends BaseService {
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private StoreDao storeDao;

    /**
     * 后台订单列表
     * @param dtGrid
     * @return
     * @throws Exception
     */
    public DtGrid getOrdersVoDtGridList(DtGrid dtGrid) throws Exception {

        if (dtGrid.getFastQueryParameters().containsKey("eq_ordersSnStr")) {
            dtGrid.getFastQueryParameters().put("eq_ordersSn",dtGrid.getFastQueryParameters().get("eq_ordersSnStr"));
            dtGrid.getFastQueryParameters().remove("eq_ordersSnStr");
        }
        if (dtGrid.getFastQueryParameters().containsKey("eq_paySnStr")) {
            dtGrid.getFastQueryParameters().put("eq_paySn",dtGrid.getFastQueryParameters().get("eq_paySnStr"));
            dtGrid.getFastQueryParameters().remove("eq_paySnStr");
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
            for (String key : dtGrid.getNcColumnsType().keySet()) {
                for (int i = 0; i< dtGrid.getNcColumnsType().get(key).size(); i++) {
                    hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                }
                dtGrid.setNcColumnsTypeList(hashMap);
            }
        }
        QueryUtils.parseDtGridHql(dtGrid);
        dtGrid = ordersDao.getDtGridList(dtGrid,Orders.class);
        List<Object> exhibitDatas = dtGrid.getExhibitDatas();
        List<Object> ordersVoList  = new ArrayList<Object>();
        for (int i = 0; i < exhibitDatas.size(); i++) {
            Orders orders = (Orders)exhibitDatas.get(i);
            OrdersVo ordersVo = new OrdersVo(orders);
            ordersVoList.add(ordersVo);
        }
        dtGrid.setExhibitDatas(ordersVoList);
        return dtGrid;
    }

    /**
     * 订单详情
     * @param ordersId
     * @return
     */
    public OrdersVo getOrdersVoInfo(int ordersId) throws ShopException {
        //取得订单ordersVo
        Orders orders = ordersDao.get(Orders.class, ordersId);
        if (orders == null) {
            throw new ShopException("该订单不存在");
        }
        OrdersVo ordersVo = new OrdersVo(orders);

        //取得订单商品ordersGodosVoList
        List<Integer> ordersIdList = new ArrayList<Integer>();
        ordersIdList.add(ordersVo.getOrdersId());
        List<Object> ordersGoodsObjectList = ordersDao.getOrdersGoodsVoList(ordersIdList);

        //归档商品列表
        List<OrdersGoodsVo> ordersGoodsVoList = new ArrayList<OrdersGoodsVo>();
        for (int i=0; i<ordersGoodsObjectList.size(); i++) {
            OrdersGoodsVo ordersGoodsVo = (OrdersGoodsVo)ordersGoodsObjectList.get(i);
            ordersGoodsVoList.add(ordersGoodsVo);
        }
        ordersVo.setOrdersGoodsVoList(ordersGoodsVoList);

        //取得店铺信息
        Store store = storeDao.get(Store.class,ordersVo.getStoreId());
        ordersVo.setStoreAddress(store.getStoreAddress());
        ordersVo.setStorePhone(store.getStorePhone());
        ordersVo.setStoreQq(store.getStoreQq());
        ordersVo.setStoreWw(store.getStoreWw());
        ordersVo.setSellerName(store.getSellerName());
        return ordersVo;
    }

    /**
     * 取消订单
     * @param ordersId
     */
    public void cancelOrders(int ordersId) throws ShopException {
        //验证提交数据
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId", ordersId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        if (OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.ADMIN_CANCEL) == 0) {
            throw new ShopException("无权操作");
        }

        ordersInfo.setCancelRole(OrdersOperationRole.ADMIN);
        ordersInfo.setCancelTime(ShopHelper.getCurrentTimestamp());
        ordersService.cancelOrders(ordersInfo);
    }

    /**
     * bycj [ 取消订单,退款时候用 ]
     */
    public void cancelOrdersForRefund(int ordersId) throws ShopException {
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId", ordersId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        ordersInfo.setCancelRole(OrdersOperationRole.ADMIN);
        ordersInfo.setCancelTime(ShopHelper.getCurrentTimestamp());
        ordersService.cancelOrders(ordersInfo);
    }

    /**
     * 收款
     * @param ordersId
     * @param paymentTime
     * @param paymentCode
     * @param outOrdersSn
     * @throws Exception
     */
    public void payOrders(Integer ordersId, String paymentTime, String paymentCode, String outOrdersSn) throws Exception {
        logger.info("收款");
        if (ordersId == null) {
            throw new ShopException("参数错误");
        }
        //验证提交数据
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        if (OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.ADMIN_CANCEL) == 0) {
            throw new ShopException("无权操作");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(paymentTime);

        ordersInfo.setPaymentTime(new Timestamp(date.getTime()));
        ordersInfo.setPaymentCode(paymentCode);
        ordersInfo.setOutOrdersSn(Pattern.compile("\"|'").matcher(outOrdersSn).replaceAll(""));
        ordersInfo.setAdminReceivePayState(State.YES);
        ordersService.payOrders(ordersInfo);
    }

    /**
     * 后台订单状态搜索列表
     * @return
     */
    public HashMap<Integer,String> getOrdersStateList() {
        HashMap<Integer,String> hashMap = new HashMap<>();
        hashMap.put(OrdersOrdersState.CANCEL, OrdersOrdersStateName.CANCEL);
        hashMap.put(OrdersOrdersState.NEW, OrdersOrdersStateName.NEW);
        hashMap.put(OrdersOrdersState.PAY, OrdersOrdersStateName.PAY);
        hashMap.put(OrdersOrdersState.SEND, OrdersOrdersStateName.SEND);
        hashMap.put(OrdersOrdersState.FINISH, OrdersOrdersStateName.FINISH);
        return hashMap;
    }
}
