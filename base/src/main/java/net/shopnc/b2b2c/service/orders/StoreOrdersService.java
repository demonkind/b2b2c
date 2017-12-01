package net.shopnc.b2b2c.service.orders;

import net.shopnc.b2b2c.constant.OrdersOperationRole;
import net.shopnc.b2b2c.constant.OrdersOperationType;
import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.dao.ShipCompanyDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.orders.OrdersGoodsDao;
import net.shopnc.b2b2c.domain.ShipCompany;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.refund.SellerRefundService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.util.OrdersHelper;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 商家订单处理专用
 * Created by hbj on 2016/1/7.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class StoreOrdersService extends BaseService {
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrdersGoodsDao ordersGoodsDao;
    @Autowired
     private OrdersService ordersService;
     @Autowired
     private ShipCompanyDao shipCompanyDao;
     // bycj [  ]
     @Autowired
     private SellerRefundService sellerRefundService;

     /**
      * 商家订单列表取总数量
     * @param params
     * @return
     */
    public long getMemberOrdersCount(HashMap<String,Object> params) {
        List<Object> list = getOrderListCondition(params);
        List<Object> condition = (List<Object>)list.get(0);
        HashMap<String, Object> values = (HashMap<String, Object>)list.get(1);
        return ordersDao.getOrdersCount(condition, values);
    }

    /**
     * 商家订单列表
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<OrdersVo> getOrdersVoList(HashMap<String,Object> params, int pageNo, int pageSize) {
        List<Object> list = getOrderListCondition(params);
        List<Object> condition = (List<Object>)list.get(0);
        HashMap<String, Object> values = (HashMap<String, Object>)list.get(1);
        List<Object> ordersVoObjectList = ordersDao.getOrdersVoList(condition,values, pageNo, pageSize);

        //取得订单商品
        List<Integer> ordersIdList = new ArrayList<Integer>();
        for (int i=0; i<ordersVoObjectList.size(); i++) {
            ordersIdList.add(((OrdersVo)ordersVoObjectList.get(i)).getOrdersId());
        }
        List<Object> ordersGoodsObjectList = ordersDao.getOrdersGoodsVoList(ordersIdList);

        //归档商品列表
        List<OrdersVo> ordersVoList = new ArrayList<OrdersVo>();
        for (int i=0; i<ordersVoObjectList.size(); i++) {
            OrdersVo ordersVo = (OrdersVo)ordersVoObjectList.get(i);
            List<OrdersGoodsVo> ordersGoodsVoList = new ArrayList<OrdersGoodsVo>();
            //获取属于该订单的所有退款单
            List<RefundItemVo> refundItemVoList = sellerRefundService.getRefundVoList(ordersVo.getOrdersId());
            //循环商品
            for (int a=0; a<ordersGoodsObjectList.size(); a++) {
                OrdersGoodsVo ordersGoodsVo = (OrdersGoodsVo)ordersGoodsObjectList.get(a);
                if (ordersGoodsVo.getOrdersId() == ordersVo.getOrdersId()) {
                    List<RefundItemVo> refundItemVoListByGoodsId = sellerRefundService.filterRefundItemVoByGoodsId(refundItemVoList , ordersGoodsVo.getGoodsId());
                    //退款成功的
                    RefundItemVo agreeRefund = sellerRefundService.getSellerAgreeRefund(refundItemVoListByGoodsId);
                    //退款进行中的
                    RefundItemVo disagreeRefund = sellerRefundService.getSellerDisagreeRefund(refundItemVoListByGoodsId);
                    //查看退款信息按钮
                    if (disagreeRefund != null){
                        ordersGoodsVo.setShowRefundInfo(1);
                        ordersGoodsVo.setRefundId(disagreeRefund.getRefundId());
                        ordersGoodsVo.setRefundType(disagreeRefund.getRefundType());
                        ordersGoodsVo.setRefundSn(disagreeRefund.getRefundSn());
                    }
                    //退款金额
                    if (agreeRefund != null){
                        // bycj [ 退款金额 ]
                        ordersGoodsVo.setRefundAmount(agreeRefund.getRefundAmount());
                    }

                    ordersGoodsVoList.add(ordersGoodsVo);
                }
            }
            ordersVo.setOrdersGoodsVoList(ordersGoodsVoList);
            //快递公司信息
            if (ordersVo.getOrdersState() == OrdersOrdersState.SEND || ordersVo.getOrdersState() == OrdersOrdersState.FINISH) {
                if (ordersVo.getShipCode() != null) {
                    ShipCompany shipCompany = shipCompanyDao.getShipCompanyInfoByShipCode(ordersVo.getShipCode());
                    ordersVo.setShipUrl(shipCompany.getShipUrl());
                }
            }
            //筛选出等待卖家审核的退款单
            RefundItemVo waitingSellerRefund = sellerRefundService.filterSellerWaitingRefundVo(refundItemVoList);
            //如果卖家审核的退款单是空的就获取未完成的退款单
            RefundItemVo orderRefundVo =  waitingSellerRefund != null ? waitingSellerRefund : sellerRefundService .filterUnfinishedRefundItemVo(refundItemVoList);
            // bycj [ 是否显示退款退货中 ]
            ordersVo.setShowRefundWaiting((orderRefundVo != null && ordersVo.getPaymentTypeCode().equals(ordersVo.getPaymentTypeCode())) ? 1 :0);
            // bycj [ 加入退款信息 ]
            ordersVo.setRefundItemVo(orderRefundVo);

            ordersVoList.add(ordersVo);
        }

        return ordersVoList;
    }

    /**
     * 商家订单详情
     * @param params
     * @return
     */
    public OrdersVo getOrdersVoInfo(HashMap<String,Object> params) {
        //取得订单ordersVo
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String,Object> values = new HashMap<String, Object>();
        for(String key : params.keySet()) {
            if (key.equals("ordersId")) {
                conditions.add("ordersId = :ordersId");
                values.put("ordersId",params.get(key));
            }
            if (key.equals("storeId")) {
                conditions.add("storeId = :storeId");
                values.put("storeId",params.get(key));
            }
        }
        OrdersVo ordersVo = ordersDao.getOrdersVoInfo(conditions, values);
        if (ordersVo == null) {
            logger.info("订单不存在");
            return null;
        }

        //快递公司信息
        if (ordersVo.getOrdersState() == OrdersOrdersState.SEND || ordersVo.getOrdersState() == OrdersOrdersState.FINISH) {
            if (ordersVo.getShipCode() != null) {
                ShipCompany shipCompany = shipCompanyDao.getShipCompanyInfoByShipCode(ordersVo.getShipCode());
                ordersVo.setShipUrl(shipCompany.getShipUrl());
            }
        }

        //取得订单商品ordersGodosVoList
        List<Integer> ordersIdList = new ArrayList<Integer>();
        ordersIdList.add(ordersVo.getOrdersId());
        List<Object> ordersGoodsObjectList = ordersDao.getOrdersGoodsVoList(ordersIdList);
        if (ordersGoodsObjectList.size() == 0) {
            logger.info("订单商品不存在");
            return null;
        }
        //归档商品列表
        List<OrdersGoodsVo> ordersGoodsVoList = new ArrayList<OrdersGoodsVo>();
        for (int i=0; i<ordersGoodsObjectList.size(); i++) {
            OrdersGoodsVo ordersGoodsVo = (OrdersGoodsVo)ordersGoodsObjectList.get(i);

            List<RefundItemVo> refundItemVoList = sellerRefundService.getRefundVoList(ordersGoodsVo.getOrdersId(),ordersGoodsVo.getOrdersGoodsId());
            //退款成功的
            RefundItemVo agreeRefund = sellerRefundService.getSellerAgreeRefund(refundItemVoList);
            //退款进行中的
            RefundItemVo disagreeRefund = sellerRefundService.getSellerDisagreeRefund(refundItemVoList);
            //查看退款信息按钮
            if (disagreeRefund != null){
                ordersGoodsVo.setShowRefundInfo(1);
                ordersGoodsVo.setRefundId(disagreeRefund.getRefundId());
                ordersGoodsVo.setRefundType(disagreeRefund.getRefundType());
                ordersGoodsVo.setRefundSn(disagreeRefund.getRefundSn());
            }
            //退款金额
            if (agreeRefund != null){
                // bycj [ 退款金额 ]
                ordersGoodsVo.setRefundAmount(agreeRefund.getRefundAmount());
            }

            // bycj [ 单商品是否显示退款按钮 ]
//            ordersGoodsVo.setShowRefund((ordersVo.getOrdersState() > OrdersOrdersState.PAY && disagreeRefund == null && agreeRefund == null )? 1:0);
            ordersGoodsVo.setShowRefund(disagreeRefund !=null ? 1:0);

            ordersGoodsVoList.add(ordersGoodsVo);
        }
        ordersVo.setOrdersGoodsVoList(ordersGoodsVoList);
        // bycj [ 退款信息 ]
        RefundItemVo refundItemVo = sellerRefundService.getRefundWaitingRefundVo(ordersVo.getOrdersId());
        // bycj [ 是否显示退款退货中 ]
        ordersVo.setShowRefundWaiting((refundItemVo != null && ordersVo.getPaymentTypeCode().equals(ordersVo.getPaymentTypeCode())) ? 1 :0);
        // bycj [ 加入退款信息 ]
        ordersVo.setRefundItemVo(refundItemVo);
        return ordersVo;
    }

    /**
     * 处理查询条件
     * @param params
     * @return list list(0) => condition,list(1) => values
     */
    private List<Object> getOrderListCondition(HashMap<String,Object> params) {
        List<String> conditions = new ArrayList<String>();
        HashMap<String,Object> values = new HashMap<String, Object>();
        for(String key : params.keySet()) {
            if (key.equals("storeId")) {
                conditions.add("storeId = :storeId");
                values.put("storeId",params.get(key));
            }
            if (key.equals("memberName")) {
                conditions.add("memberName = :memberName");
                values.put("memberName",params.get(key));
            }
            if (key.equals("createTimeStart")) {
                conditions.add("createTime >= :createTimeStart");
                values.put("createTimeStart",params.get(key));
            }
            if (key.equals("createTimeEnd")) {
                conditions.add("createTime <= :createTimeEnd");
                Date date = new Date(((Timestamp)params.get(key)).getTime()+60*60*1000*24-1000);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                values.put("createTimeEnd", Timestamp.valueOf(simpleDateFormat.format(date)));
            }
            if (key.equals("ordersSn")) {
                conditions.add("ordersSn = :ordersSn");
                if (Pattern.matches("^\\d{16}$", params.get(key).toString())) {
                    values.put("ordersSn",Long.parseLong(params.get(key).toString()));
                } else {
                    params.put("ordersSn",0L);
                }
            }
            if (key.equals("goodsName")) {
                //商品名称搜索，storeId必须也存在
                List<OrdersGoods> ordersGoodsList = ordersGoodsDao.getOrdersGoodsListForStoreLimit(
                        params.get(key).toString(),
                        Integer.parseInt(params.get("storeId").toString()),50);
                if (ordersGoodsList.size()>0) {
                    List<Integer> ordersIdList = new ArrayList<Integer>();
                    for (int i=0; i<ordersGoodsList.size();i++) {
                        ordersIdList.add(ordersGoodsList.get(i).getOrdersId());
                    }
                    conditions.add("ordersId in (:ordersIdList)");
                    values.put("ordersIdList",ordersIdList);
                } else {
                    conditions.add("1=0");
                }
            }
            if (key.equals("ordersState")) {
                //待付款
                if (params.get(key).equals("new")) {
                    conditions.add(String.format("ordersState = %d", OrdersOrdersState.NEW));
                }
                //待发货
                if (params.get(key).equals("pay")) {
                    conditions.add(String.format("ordersState = %d", OrdersOrdersState.PAY));
                }
                //待收货
                if (params.get(key).equals("send")) {
                    conditions.add(String.format("ordersState = %d", OrdersOrdersState.SEND));
                }
                //已完成
                if (params.get(key).equals("finish")) {
                    conditions.add(String.format("ordersState = %d", OrdersOrdersState.FINISH));
                }
                //已取消
                if (params.get(key).equals("cancel")) {
                    conditions.add(String.format("ordersState = %d", OrdersOrdersState.CANCEL));
                }
            }
        }

        List<Object> list = new ArrayList<Object>();
        list.add(conditions);
        list.add(values);
        return list;
    }

    /**
     * 取消订单
     * @param ordersId
     * @param storeId
     * @param cancelReason
     * @throws ShopException
     */
    public void cancelOrders(int ordersId, int storeId, int cancelReason) throws ShopException {
        //验证提交数据
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        whereItems.add("storeId = :storeId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        params.put("storeId", storeId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        if (OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.STORE_CANCEL) == 0) {
            throw new ShopException("无权操作");
        }

        ordersInfo.setCancelReason(cancelReason);
        ordersInfo.setCancelRole(OrdersOperationRole.STORE);
        ordersInfo.setCancelTime(ShopHelper.getCurrentTimestamp());
        ordersService.cancelOrders(ordersInfo);
    }

    /**
     * 修改运费
     * @param ordersId
     * @param storeId
     * @param freightAmount
     * @throws ShopException
     */
    public void modifyFreight(int ordersId, int storeId, BigDecimal freightAmount) throws ShopException {
        //验证提交数据
        if (PriceHelper.isLessThan(freightAmount,new BigDecimal(0))) {
            throw new ShopException("运费不能小于0");
        }
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        whereItems.add("storeId = :storeId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        params.put("storeId", storeId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        if (OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.STORE_MODIFY_FREIGHT) == 0) {
            throw new ShopException("无权操作");
        }
        ordersInfo.setOrdersAmount(
                PriceHelper.add(PriceHelper.sub(ordersInfo.getOrdersAmount(), ordersInfo.getFreightAmount()), freightAmount)
        );
        ordersInfo.setFreightAmount(freightAmount);
        ordersService.modifyFreight(ordersInfo);
    }

    /**
     * 发货
     * @param areaId1
     * @param areaId2
     * @param areaId3
     * @param areaId4
     * @param areaInfo
     * @param receiverName
     * @param receiverPhone
     * @param receiverAddress
     * @param ordersId
     * @param storeId
     * @param shipCode
     * @param shipSn
     * @param shipNote
     * @throws ShopException
     */
    public void send(int areaId1,int areaId2,int areaId3,int areaId4,String areaInfo,String receiverName,String receiverPhone,String receiverAddress,int ordersId,
                     int storeId,
                     String shipCode,
                     String shipSn,
                     String shipNote) throws ShopException {
        //验证提交数据
        if (shipCode != null && !shipCode.equals("") && (shipSn == null || shipSn.equals(""))) {
            throw new ShopException("请填写物流单号");
        }

        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        whereItems.add("storeId = :storeId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        params.put("storeId", storeId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        if (OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.STORE_SEND) == 0
                && OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.STORE_SEND_MODIFY) == 0) {
            throw new ShopException("无权操作");
        }

        if (shipCode != null && !shipCode.equals("") && shipSn != null && !shipSn.equals("")) {
            ShipCompany shipCompany = shipCompanyDao.getShipCompanyInfoByShipCode(shipCode);
            if (shipCompany == null) {
                throw new ShopException("该物流公司不存在");
            }
            ordersInfo.setShipName(shipCompany.getShipName());
            ordersInfo.setShipCode(shipCompany.getShipCode());
            ordersInfo.setShipSn(Pattern.compile("\"|'").matcher(shipSn).replaceAll(""));
        }
        ordersInfo.setShipNote(shipNote);
        ordersInfo.setSendTime(ShopHelper.getCurrentTimestamp());
        if (areaId1 > 0) {
            ordersInfo.setReceiverAreaId1(areaId1);
            ordersInfo.setReceiverAreaId2(areaId2);
            ordersInfo.setReceiverAreaId3(areaId3);
            ordersInfo.setReceiverAreaId4(areaId4);
            ordersInfo.setReceiverAreaInfo(areaInfo);
        }
        ordersInfo.setReceiverName(Pattern.compile("\"|'").matcher(receiverName).replaceAll(""));
        ordersInfo.setReceiverPhone(Pattern.compile("\"|'").matcher(receiverPhone).replaceAll(""));
        ordersInfo.setReceiverAddress(Pattern.compile("\"|'").matcher(receiverAddress).replaceAll(""));
        ordersService.sendOrders(ordersInfo);
    }

}
