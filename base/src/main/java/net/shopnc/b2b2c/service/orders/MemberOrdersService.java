package net.shopnc.b2b2c.service.orders;

import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.dao.ShipCompanyDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.orders.OrdersGoodsDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.ShipCompany;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.refund.MemberRefundService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersPayVo;
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
import java.util.*;
import java.util.regex.Pattern;

/**
 * 买家订单处理专用<br/>
 * Created by hbj on 2015/12/22.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class MemberOrdersService extends BaseService {
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrdersGoodsDao ordersGoodsDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private ShipCompanyDao shipCompanyDao;

    @Autowired
    private MemberRefundService memberRefundService;

    /**
     * 买家我的订单列表取总数量
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
     * 买家我的订单列表
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<OrdersPayVo> getOrdersPayVoList(HashMap<String,Object> params, int pageNo, int pageSize) {
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

        //处理整理成订单列表显示的形式

        //归档商品列表
        List<OrdersVo> ordersVoList = new ArrayList<OrdersVo>();
        for (int i=0; i<ordersVoObjectList.size(); i++) {
            OrdersVo ordersVo = (OrdersVo)ordersVoObjectList.get(i);
            List<OrdersGoodsVo> ordersGoodsVoList = new ArrayList<OrdersGoodsVo>();
            //循环商品
            for (int a=0; a<ordersGoodsObjectList.size(); a++) {
                OrdersGoodsVo ordersGoodsVo = (OrdersGoodsVo)ordersGoodsObjectList.get(a);
                if (ordersGoodsVo.getOrdersId() == ordersVo.getOrdersId()) {
                    List<RefundItemVo> refundItemVoList = memberRefundService.getRefundVoList(ordersGoodsVo.getOrdersId(),ordersGoodsVo.getOrdersGoodsId());
                    //退款成功的
                    RefundItemVo agreeRefund = memberRefundService.getSellerAgreeRefund(refundItemVoList);
                    //退款进行中的
                    RefundItemVo disagreeRefund = memberRefundService.getSellerDisagreeRefund(refundItemVoList);
                    //查看退款信息按钮
                    if (refundItemVoList.size() > 0){
                        ordersGoodsVo.setShowRefundInfo(1);
                        ordersGoodsVo.setRefundId(refundItemVoList.get(0).getRefundId());
                        ordersGoodsVo.setRefundType(refundItemVoList.get(0).getRefundType());
                    }
                    //退款金额
                    if (agreeRefund != null){
                        // bycj [ 退款金额 ]
                        ordersGoodsVo.setRefundAmount(agreeRefund.getRefundAmount());
                    }

//                    // bycj [ 单商品是否显示退款按钮 ]
                    ordersGoodsVo.setShowRefund(( memberRefundService.allowRefundByOrders(ordersVo) && PriceHelper.isGreaterThan(ordersGoodsVo.getGoodsPayAmount(), BigDecimal.ZERO) &&ordersVo.getOrdersState() > OrdersOrdersState.PAY && disagreeRefund == null && agreeRefund == null )? 1:0);

                    ordersGoodsVoList.add(ordersGoodsVo);
                }
            }
            ordersVo.setOrdersGoodsVoList(ordersGoodsVoList);
            //快递公司信息
            if (ordersVo.getOrdersState() == OrdersOrdersState.SEND || ordersVo.getOrdersState() == OrdersOrdersState.FINISH) {
                if (ordersVo.getShipCode() != null) {
                    ShipCompany shipCompany = shipCompanyDao.getShipCompanyInfoByShipCode(ordersVo.getShipCode());
                    if(null != shipCompany){
                    	ordersVo.setShipUrl(shipCompany.getShipUrl());
                    }
                }
            }

            RefundItemVo refundItemVo = memberRefundService.getRefundItemVoLatest(ordersVo.getOrdersId());
            // bycj [ 是否显示整单退款按钮 ]
            ordersVo.setShowMemberRefundAll(memberRefundService.allowRefundAll(refundItemVo, ordersVo.getOrdersState(), ordersVo.getPaymentTypeCode()) ? 1 : 0);

            // bycj [ 是否显示退款退货中 ]
//            ordersVo.setShowRefundWaiting(refundService.isRefundWaiting(refundItemVo,ordersVo.getOrdersState(),ordersVo.getPaymentTypeCode()) ? 1 :0);
            ordersVo.setShowRefundWaiting((refundItemVo != null && ordersVo.getPaymentTypeCode().equals(ordersVo.getPaymentTypeCode())) ? 1 :0);
            
            setRefundReturnState(ordersVo, refundItemVo);

            // bycj [ 加入退款信息 ]
            ordersVo.setRefundItemVo(refundItemVo);

            ordersVoList.add(ordersVo);
        }

        //归档订单列表[先处理一下订单列表]
        HashMap<Integer,List<OrdersVo>> hashMap = new HashMap<Integer, List<OrdersVo>>();
        for (int i=0; i<ordersVoList.size(); i++) {
            int payId = ordersVoList.get(i).getPayId();
            if (hashMap.containsKey(payId)) {
                hashMap.get(payId).add(ordersVoList.get(i));
            } else {
                List<OrdersVo> ordersVoList1 = new ArrayList<OrdersVo>();
                ordersVoList1.add(ordersVoList.get(i));
                hashMap.put(payId,ordersVoList1);
            }
        }
        //归档订单列表[按payId 倒序]
        Object[] keyArrays = hashMap.keySet().toArray();
        Arrays.sort(keyArrays, Collections.reverseOrder());

        List<OrdersPayVo> ordersPayVoList = new ArrayList<OrdersPayVo>();
        for (int i=0; i<keyArrays.length; i++) {
            OrdersPayVo ordersPayVo = new OrdersPayVo(hashMap.get(keyArrays[i]));
            ordersPayVoList.add(ordersPayVo);
        }
        return ordersPayVoList;
    }

    /**
     * 买家订单详情
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
            if (key.equals("memberId")) {
                conditions.add("memberId = :memberId");
                values.put("memberId",params.get(key));
            }
        }
        OrdersVo ordersVo = ordersDao.getOrdersVoInfo(conditions, values);
        if (ordersVo == null) {
            return null;
        }


        //取得订单商品ordersGodosVoList
        List<Integer> ordersIdList = new ArrayList<Integer>();
        ordersIdList.add(ordersVo.getOrdersId());
        List<Object> ordersGoodsObjectList = ordersDao.getOrdersGoodsVoList(ordersIdList);

        //归档商品列表
        List<OrdersGoodsVo> ordersGoodsVoList = new ArrayList<OrdersGoodsVo>();
        for (int i=0; i<ordersGoodsObjectList.size(); i++) {
            OrdersGoodsVo ordersGoodsVo = (OrdersGoodsVo)ordersGoodsObjectList.get(i);
            List<RefundItemVo> refundItemVoList = memberRefundService.getRefundVoList(ordersGoodsVo.getOrdersId(),ordersGoodsVo.getOrdersGoodsId());
            //退款成功的
            RefundItemVo agreeRefund = memberRefundService.getSellerAgreeRefund(refundItemVoList);
            //退款进行中的
            RefundItemVo disagreeRefund = memberRefundService.getSellerDisagreeRefund(refundItemVoList);
            //查看退款信息按钮
            if (refundItemVoList.size() > 0){
                ordersGoodsVo.setShowRefundInfo(1);
                ordersGoodsVo.setRefundId(refundItemVoList.get(0).getRefundId());
                ordersGoodsVo.setRefundType(refundItemVoList.get(0).getRefundType());
            }
            //退款金额
            if (agreeRefund != null){
                // bycj [ 退款金额 ]
                ordersGoodsVo.setRefundAmount(agreeRefund.getRefundAmount());
            }
            // bycj [ 单商品是否显示退款按钮 ]
//            ordersGoodsVo.setShowRefund((ordersVo.getOrdersState() > OrdersOrdersState.PAY && disagreeRefund == null && agreeRefund == null )? 1:0);
            ordersGoodsVo.setShowRefund(( memberRefundService.allowRefundByOrders(ordersVo) && PriceHelper.isGreaterThan(ordersGoodsVo.getGoodsPayAmount(), BigDecimal.ZERO) &&ordersVo.getOrdersState() > OrdersOrdersState.PAY && disagreeRefund == null && agreeRefund == null )? 1:0);
            ordersGoodsVoList.add(ordersGoodsVo);
        }
        ordersVo.setOrdersGoodsVoList(ordersGoodsVoList);

        //取得店铺信息
        Store store = storeDao.get(Store.class,ordersVo.getStoreId());
        ordersVo.setStoreAddress(store.getStoreAddress());
        ordersVo.setStorePhone(store.getStorePhone());
        ordersVo.setStoreQq(store.getStoreQq());
        ordersVo.setStoreWw(store.getStoreWw());

        //快递公司信息
        if (ordersVo.getOrdersState() == OrdersOrdersState.SEND || ordersVo.getOrdersState() == OrdersOrdersState.FINISH) {
            if (ordersVo.getShipCode() != null) {
                ShipCompany shipCompany = shipCompanyDao.getShipCompanyInfoByShipCode(ordersVo.getShipCode());
                ordersVo.setShipUrl(shipCompany.getShipUrl());
            }
        }

        RefundItemVo refundItemVo = memberRefundService.getRefundItemVoLatest(ordersVo.getOrdersId());

        // bycj [ 是否显示退款退货中（整单） ]
        ordersVo.setShowMemberRefundAll(memberRefundService.allowRefundAll(refundItemVo, ordersVo.getOrdersState(), ordersVo.getPaymentTypeCode()) ? 1 : 0);

        // 设置退货退款状态
        setRefundReturnState(ordersVo,refundItemVo);
        // 退款等待中
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
            if (key.equals("memberId")) {
                conditions.add("memberId = :memberId");
                values.put("memberId",params.get(key));
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
            //买家订单列表关键字搜索
            if (key.equals("keyword")) {
                if (Pattern.matches("^\\d{16}$", params.get(key).toString())) {
                    //订单号搜索
                    conditions.add("ordersSn = :ordersSn");
                    values.put("ordersSn",Long.parseLong(params.get(key).toString()));
                } else {
                    //商品名称搜索，memberId必须也存在
                    List<OrdersGoods> ordersGoodsList = ordersGoodsDao.getOrdersGoodsListForMemberLimit(
                            params.get(key).toString(),
                            Integer.parseInt(params.get("memberId").toString()),50);
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
            }
            //买家订单列表
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
                //待评价
                if (params.get(key).equals("noeval")) {
                    conditions.add(String.format("ordersState = %d and evaluationState = %d", OrdersOrdersState.FINISH, OrdersEvaluationState.NO));
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
     * @param memberId
     * @param cancelReason
     * @throws ShopException
     */
    public void cancelOrders(int ordersId, int memberId, int cancelReason) throws ShopException {
        //验证提交数据
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        whereItems.add("memberId = :memberId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        params.put("memberId", memberId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        if (OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.MEMBER_CANCEL) == 0) {
            throw new ShopException("无权操作");
        }

        ordersInfo.setCancelReason(cancelReason);
        ordersInfo.setCancelRole(OrdersOperationRole.MEMBER);
        ordersInfo.setCancelTime(ShopHelper.getCurrentTimestamp());
        ordersService.cancelOrders(ordersInfo);
    }

    /**
     * 订单收货
     * @param ordersId
     * @param memberId
     * @throws ShopException
     */
    public void receiveOrders(int ordersId, int memberId) throws ShopException {
        //验证提交数据
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        whereItems.add("memberId = :memberId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        params.put("memberId", memberId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        if (OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.MEMBER_RECEIVE) == 0) {
            throw new ShopException("无权操作");
        }
        ordersInfo.setFinishTime(ShopHelper.getCurrentTimestamp());
        ordersService.receiveOrders(ordersInfo);
    }

    /**
     * 延迟收货
     * @param ordersId
     * @param memberId
     * @param delayDay
     */
    public void delayReceiveOrders(int ordersId, int memberId, int delayDay) throws Exception {
        //验证提交数据
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        whereItems.add("memberId = :memberId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("ordersId",ordersId);
        params.put("memberId", memberId);
        Orders ordersInfo = ordersDao.getOrdersInfo(whereItems, params);
        if (ordersInfo == null) {
            throw new ShopException("该订单不存在");
        }
        if (OrdersHelper.operationValidate(ordersInfo, OrdersOperationType.MEMBER_DELAY_RECEIVE) == 0) {
            throw new ShopException("无权操作");
        }

        ordersInfo.setDelayReceiveState(State.YES);
        ordersInfo.setAutoReceiveTime(ShopHelper.getFutureTimestamp(ordersInfo.getAutoReceiveTime(),Calendar.DATE,delayDay));
        ordersService.delayReceivOrders(ordersInfo);
    }

    /**
     * 支付订单
     * @param ordersList
     * @throws ShopException
     */
    public void payOrders(List<Orders> ordersList) throws Exception{
        for (int i=0; i<ordersList.size(); i++) {
            if (ordersList.get(i).getOrdersState() == OrdersOrdersState.PAY && ordersList.get(i).getPaymentTypeCode().equals(OrdersPaymentTypeCode.ONLINE)) {
                ordersService.payOrders(ordersList.get(i));
            }
        }
    }
    /**
     * 查询商品购买总数
     * @param commonId 商品id
     */
    public int findTotalPurchasesOfGoods(int commonId,int memberId){
    	List<Map<String, Object>> obj=ordersDao.findTotalPurchasesOfGoods(commonId, memberId);
    	int wasMaxNum=0;
    	if(obj!=null){
        	Map<String,Object> map=obj.get(0);
        	if(map.get("0")!=null){
        		wasMaxNum=Integer.valueOf(map.get("0").toString());
        	}
        }
    	return wasMaxNum;
    }
    
    /**
     * 设置退款退货状态
     * @param ordersVo
     * @param refundItemVo
     */
    private void setRefundReturnState(OrdersVo ordersVo, RefundItemVo refundItemVo){
    	ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_0);
    	if(refundItemVo != null){
    		
    		if(refundItemVo.getRefundType() == RefundState.REFUND_TYPE_REFUND){
    			// 退款
            	if (refundItemVo.getRefundState() != RefundState.REFUND_STATE_FINISH){
            		// 11:退款中
            		ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_REFUNDING);
            	}
    		} else {
    			// 21:退货中
    			if(refundItemVo.getSellerState() == RefundState.SELLER_STATE_WAITING){
    				ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_RETURNING);
    			}
    			
    			//  25:同意退货并弃货 = 11:退款中
    			if(refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE && refundItemVo.getRefundState() == RefundState.REFUND_STATE_CHECK){
    				// 11:退款中
            		ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_REFUNDING);
    			}
    			
    			//  22:同意退货并要求用户发货时
    			if((refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE && refundItemVo.getRefundState() != RefundState.REFUND_STATE_CHECK)
    					|| refundItemVo.getGoodsState() == RefundState.RETURN_SHIP_STATE_UNRECEIVED){
    				ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_RETURN_AGREE);
    			}
    			//  29:退货拒绝
    			if(refundItemVo.getSellerState() == RefundState.SELLER_STATE_DISAGREE){
    				ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_RETURN_REFUSE);
    			}
    			
    			//  24:退货结束(从买家手里收到货物)
    			if(refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE && refundItemVo.getGoodsState() == RefundState.RETURN_SHIP_STATE_FINISH){
    				ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_REFUNDING);
    			}
    			
    			//  23:买家发送货物，卖家等待收货中
    			if(refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE && refundItemVo.getGoodsState() == RefundState.RETURN_SHIP_STATE_SEND){
    				ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_RETURN_SEND);
    			}
    		}
    		
    		// 退款
    		// 如果退款已结束
        	if (refundItemVo.getRefundState() == RefundState.REFUND_STATE_FINISH){
        		// 18:退款结束
        		ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_REFUND_END);
        		// 19:退款拒绝
        		if(refundItemVo.getSellerState() == RefundState.SELLER_STATE_DISAGREE){
        			ordersVo.setRefundReturnStatus(RefundState.REFUND_RETURN_STATUS_REFUND_REFUSE);
        		}
        	}
        }
    }

}
