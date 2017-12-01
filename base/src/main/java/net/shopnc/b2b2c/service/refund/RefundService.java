package net.shopnc.b2b2c.service.refund;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.constant.OrdersPaymentCode;
import net.shopnc.b2b2c.constant.OrdersPaymentTypeCode;
import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.dao.ShipCompanyDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.orders.OrdersGoodsDao;
import net.shopnc.b2b2c.dao.refund.RefundDao;
import net.shopnc.b2b2c.dao.refund.RefundDetailDao;
import net.shopnc.b2b2c.dao.refund.RefundReasonDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.ShipCompany;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.domain.refund.RefundDetail;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.b2b2c.service.orders.AdminOrdersService;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.vo.refund.RefundDetailVo;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 退款退货原因
 * Created by cj on 2016/2/1.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class RefundService extends BaseService {

    /**
     * 收货完成后可以申请退款退货
     */
    public final static int ORDER_REFUND = 15;

    /**
     * 卖家不处理退款退货申请时按同意处理
     */
    public final static int REFUND_CONFIRM = 7;

    /**
     * 卖家不处理收货时按弃货处理
     */
    public final static int RETURN_CONFIRM = 7;

    /**
     * 退货的商品发货多少天以后才可以选择没收到
     */

    public final static int RETURN_DELAY = 5;

    /**
     * 会员退款消息的代码
     */
    public final static String MEMBER_REFUND_MESSAGE_CODE = "memberRefundUpdate";

    /**
     * 会员退货消息的代码
     */
    public final static String MEMBER_RETURN_MESSAGE_CODE = "memberReturnUpdate";
    /**
     * 商家--退款提醒
     */
    public final static String STORE_REFUND = "storeRefund";

    /**
     * 商家--退款自动处理提醒
     */
    public final static String STORE_REFUND_AUTO_PROCESS = "storeRefundAutoProcess";
    /**
     * 商家--退货提醒
     */
    public final static String STORE_RETURN = "storeReturn";
    /**
     * 商家--退货自动处理提醒
     */
    public final static String STORE_RETURN_AUTO_PROCESS = "storeReturnAutoProcess";

    /**
     * 商家--退货未收货自动处理提醒
     */
    public final static String STORE_RETURN_AUTO_RECEIPT = "storeReturnAutoReceipt";

    /**
     * 订单锁定
     */
    public final static int ORDERS_LOCK = 1;
    /**
     * 订单锁定
     */
    public final static int ORDERS_UNLOCK = 0;


    @Autowired
    protected RefundReasonDao refundReasonDao;
    @Autowired
    protected RefundDao refundDao;
    @Autowired
    protected OrdersDao ordersDao;
    @Autowired
    protected OrdersGoodsDao ordersGoodsDao;

    @Autowired
    protected OrdersService ordersService;

    @Autowired
    protected RefundDetailDao refundDetailDao;

    @Autowired
    protected PredepositService predepositService;

    @Autowired
    protected AdminOrdersService adminOrdersService;

    @Autowired
    private ShipCompanyDao shipCompanyDao;
    @Autowired
    SendMessageService sendMessageService;
    @Autowired
    private StoreDao storeDao;

    /**
     * 订单详情
     *
     * @param params           参数
     * @param excOrdersGoodsId 排除的ordersGoodsId
     * @return
     * @example HashMap<String, Object> params = new HashMap<String, Object>();
     * params.put("memberId", SessionEntity.getMemberId());
     * params.put("ordersId", ordersId);
     * OrdersVo ordersVo = memberOrdersService.getOrdersVoInfo(params);
     */
    public OrdersVo getOrdersVoInfo(HashMap<String, Object> params, int excOrdersGoodsId) {
        //取得订单ordersVo
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (String key : params.keySet()) {
            if (key.equals("ordersId")) {
                conditions.add("ordersId = :ordersId");
                values.put("ordersId", params.get(key));
            }
            if (key.equals("memberId")) {
                conditions.add("memberId = :memberId");
                values.put("memberId", params.get(key));
            }
            if (key.equals("storeId")){
                conditions.add("storeId=:storeId");
                values.put("storeId",params.get(key));
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
        for (int i = 0; i < ordersGoodsObjectList.size(); i++) {
            OrdersGoodsVo ordersGoodsVo = (OrdersGoodsVo) ordersGoodsObjectList.get(i);
            if (excOrdersGoodsId != 0 && ordersGoodsVo.getOrdersGoodsId() != excOrdersGoodsId) {
                continue;
            }
            // bycj [ 添加退款退货按钮 ]
            RefundItemVo refundItemVo = getRefundItemVo(ordersGoodsVo.getOrdersId(), ordersGoodsVo.getOrdersGoodsId());
            if (refundItemVo != null) {
                ordersGoodsVo.setShowRefundInfo(1);
                ordersGoodsVo.setRefundId(refundItemVo.getRefundId());
                ordersGoodsVo.setRefundType(refundItemVo.getRefundType());
                // bycj [ 退款金额 ]
                ordersGoodsVo.setRefundAmount((refundItemVo.getRefundState() == RefundState.REFUND_STATE_FINISH && refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE) ? refundItemVo.getRefundAmount() : null);
            }
            // bycj [ 单商品是否显示退款 ]
            ordersGoodsVo.setShowRefund((ordersVo.getOrdersState() > OrdersOrdersState.PAY && refundItemVo == null) ? 1 : 0);

            ordersGoodsVoList.add(ordersGoodsVo);
        }
        ordersVo.setOrdersGoodsVoList(ordersGoodsVoList);

        //取得店铺信息
        Store store = storeDao.get(Store.class, ordersVo.getStoreId());
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

        RefundItemVo refundItemVo = getRefundItemVoLatest(ordersVo.getOrdersId());
        // bycj [ 是否显示退款退货中（整单） ]
        ordersVo.setShowMemberRefundAll(allowRefundAll(refundItemVo, ordersVo.getOrdersState(), ordersVo.getPaymentTypeCode()) ? 1 : 0);
        // bycj [ 是否显示退款退货中 ]
        ordersVo.setShowRefundWaiting((refundItemVo != null && ordersVo.getPaymentTypeCode().equals(ordersVo.getPaymentTypeCode())) ? 1 : 0);
        
        // 设置退货退款状态
        setRefundReturnState(ordersVo,refundItemVo);
        // bycj [ 加入退款信息 ]
        ordersVo.setRefundItemVo(refundItemVo);
        return ordersVo;
    }

    /**
     * 保存
     *
     * @param refund
     */
    public void saveRefund(Refund refund) {
        refundDao.update(refund);
    }

    /**
     * 批量保存
     *
     * @param refundList
     */
    public void saveRefund(List<Refund> refundList) {
        refundDao.saveAll(refundList);
    }

    /**
     * 获取 卖家到时间不处理退款申请
     * 计划任务中调用
     *
     * @return
     */
    public List<Refund> getRefundConfirm() {
        //过期时间
        Timestamp outTime = ShopHelper.getFutureTimestamp(ShopHelper.getCurrentTimestamp(), Calendar.DATE, -REFUND_CONFIRM);
        List<Object> condition = new ArrayList<Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();

        condition.add("sellerState =:sellerState");
        params.put("sellerState", RefundState.SELLER_STATE_WAITING);

        condition.add("addTime <:addTime");
        params.put("addTime", outTime);

        return refundDao.getRefundList(condition, params);
    }

    /**
     * 获取 退货未收货自动处理的退货申请
     *
     * @return
     */
    public List<Refund> getReceiptRefundList() {

        //过期时间
        Timestamp outTime = ShopHelper.getFutureTimestamp(ShopHelper.getCurrentTimestamp(), Calendar.DATE, -RETURN_DELAY);
        List<Object> condition = new ArrayList<Object>();
        HashMap<String, Object> params = new HashMap<String, Object>();

        condition.add("sellerState =:sellerState");
        params.put("sellerState", RefundState.SELLER_STATE_AGREE);

        condition.add("goodsState=:goodsState");
        params.put("goodsState", RefundState.RETURN_SHIP_STATE_SEND);


        condition.add("returnType=:returnType");
        params.put("returnType", RefundState.REFUND_TYPE_RETURN);

        condition.add("delayTime <:delayTime");
        params.put("delayTime", outTime);

        return refundDao.getRefundList(condition, params);
    }


    /**
     * 发送会员日志
     *
     * @param tplCode  日志代码
     * @param memberId 会员id
     * @param map      末班替换变量
     * @param sn       标志，用于连接
     */
    public void sendMemberMessage(String tplCode, int memberId, HashMap<String, Object> map, String sn) {
        logger.info("向会员发送消息:" + memberId + ",tplcode:" + tplCode + ",map:" + map + ",sn :" + sn);
        sendMessageService.sendMember(tplCode, memberId, map, sn);
    }

    /**
     * 发送店铺日志
     *
     * @param tplCode  日志代码
     * @param memberId 会员id
     * @param map      末班替换变量
     * @param sn       标志，用于连接
     */
    public void sendStoreMessage(String tplCode, int memberId, HashMap<String, Object> map, String sn) {
        logger.info("向店铺发送消息:" + memberId + ",tplcode:" + tplCode + ",map:" + map + ",sn :" + sn);
        sendMessageService.sendStore(tplCode, memberId, map, sn);
    }

    /**
     * 获取平台常用的快递公司列表
     *
     * @return
     */
    public List<ShipCompany> getShipCompany() {
        return shipCompanyDao.getShipCompanyActivityList();
    }

    /**
     * 根据快递公司id 获取快递公司详细信息
     *
     * @param shipId
     * @return
     */
    public ShipCompany getShipCompany(int shipId) {
        return shipCompanyDao.getShipCompany(shipId);
    }

    /**
     * 根据退款id 获取退款金额详情
     *
     * @param refundId
     * @return
     */
    public RefundDetailVo getRefundDetailVoByRefundId(int refundId) {
        RefundDetail refundDetail = refundDetailDao.get(RefundDetail.class, refundId);
        return refundDetail != null ? new RefundDetailVo(refundDetail) : null;
    }

    public RefundDetail getRefundDetailByRefundId(int refundId) {
        return refundDetailDao.get(RefundDetail.class, refundId);
    }

    public RefundDetail getRefundDetail(String batchNo) {
        return refundDetailDao.getRefundDetail(batchNo);
    }

    /**
     * 锁定订单和解锁订单
     *
     * @param ordersId
     * @param type
     */
    public void lockAndUnlockOrdersById(int ordersId, int type) {
        //更新订单状态
        List<Object> setItems = new ArrayList<Object>();
        setItems.add("lockState = :lockState");
        List<Object> whereItems = new ArrayList<Object>();
        whereItems.add("ordersId = :ordersId");
        //如果是订单解锁，就查找是否还有退款正在处理
        if (type == ORDERS_UNLOCK){
            List<Object> conditions = new ArrayList<Object>();
            HashMap<String, Object> values = new HashMap<String, Object>();

            conditions.add("ordersId = :ordersId");
            values.put("ordersId", ordersId);

            conditions.add("refundState != :refundState ");
            values.put("refundState",RefundState.REFUND_STATE_FINISH);

            RefundItemVo refundItemVo = refundDao.getRefundVoByParams(conditions, values);
            if (refundItemVo != null ){
                return;
            }
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("lockState", type);

        params.put("ordersId", ordersId);
        ordersDao.editOrders(setItems, whereItems, params);
    }

    /**
     * 获取正在申请审核的退款
     *
     * @param ordersId
     * @param ordersGoodsId
     * @param memberId
     * @return
     */
    public int getRefundWaitingCount(int ordersId, int ordersGoodsId, int memberId) {
        return refundDao.getRefundWaitingCount(ordersId, ordersGoodsId, memberId);
    }

    /**
     * 添加退款详情信息
     *
     * @param refundItemVo
     * @return
     */
    public RefundDetailVo saveOrUpdateRefundDetail(RefundItemVo refundItemVo, OrdersVo ordersVo) {


        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
        String curDate = s.format(new Date());

        RefundDetail refundDetail = new RefundDetail();
        refundDetail.setRefundState(1);
        refundDetail.setRefundId(refundItemVo.getRefundId());
        refundDetail.setOrdersId(refundItemVo.getOrdersId());
        //批次号。支付宝要求格式为：当天退款日期+流水号。
        refundDetail.setBatchNo(makeBatchNo(refundItemVo.getRefundId()));

        refundDetail.setRefundAmount(refundItemVo.getRefundAmount());
        refundDetail.setAddTime(ShopHelper.getCurrentTimestamp());
        refundDetail.setRefundCode(ordersVo.getPaymentCode());

        //支付宝
        if ((ordersVo.getOutOrdersSn() != null && ordersVo.getOutOrdersSn().length() > 0) && ordersVo.getPaymentCode().equals(OrdersPaymentCode.ALIPAY)) {
            refundDetail.setRefundCode(OrdersPaymentCode.ALIPAY);
        }
        refundDetailDao.update(refundDetail);
        return new RefundDetailVo(refundDetail);
    }

    /**
     * 修改退款详情
     *
     * @param refundDetail
     */
    public void updateRefundDetail(RefundDetail refundDetail) {
        refundDetailDao.update(refundDetail);
    }

    /**
     * 根据退款id 和店铺id 获取退款详情
     *
     * @param refundId 退款id
     * @param storeId  店铺id
     * @return
     */
    public RefundItemVo getRefundVoByIds(int refundId, int storeId,int refundType) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("refundId = :refundId");
        values.put("refundId", refundId);
        conditions.add("storeId = :storeId");
        values.put("storeId", storeId);

        conditions.add("refundType = :refundType");
        values.put("refundType", refundType);
        return refundDao.getRefundVoByParams(conditions, values);
    }

    /**
     * 根据退款id 和店铺id 获取退款实体
     *
     * @param refundId
     * @param storeId
     * @return
     */
    public Refund getRefundByIds(int refundId, int storeId) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("refundId = :refundId");
        values.put("refundId", refundId);
        conditions.add("storeId = :storeId");
        values.put("storeId", storeId);
        return refundDao.getRefundByParams(conditions, values);
    }

    /**
     * 根据退款id获取退款的Vo
     *
     * @param refundId 退款id
     * @return
     */
    public RefundItemVo getRefundInfo(int refundId) {
        return new RefundItemVo(refundDao.get(Refund.class, refundId));
    }

    public RefundItemVo getRefundInfo(int refundId, int memberId) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("refundId = :refundId");
        values.put("refundId", refundId);
        conditions.add("memberId = :memberId");
        values.put("memberId", memberId);
        return refundDao.getRefundVoByParams(conditions, values);
    }

    public RefundItemVo getRefundItemVo1(int ordersId) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("ordersId = :ordersId");
        values.put("ordersId", ordersId);
        return refundDao.getRefundVoByParams(conditions, values);
    }
    
    /**
     * 根据订单ID获取最新的退货，退款信息
     * @param ordersId
     * @return
     */
    public RefundItemVo getRefundItemVoLatest(int ordersId) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("ordersId = :ordersId");
        values.put("ordersId", ordersId);
        
        List<Object> sort = new ArrayList<Object>();
        sort.add(" order by addTime desc");
        return refundDao.getRefundVoByParamsLatest(conditions, values, sort);
    }

    public RefundItemVo getRefundItemVo(int ordersId, int ordersGoodsId) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("ordersId = :ordersId");
        values.put("ordersId", ordersId);
        conditions.add("ordersGoodsId = :ordersGoodsId");
        values.put("ordersGoodsId", ordersGoodsId);
        return refundDao.getRefundVoByParams(conditions, values);
    }


    public Refund getRefund(int refundId, int memberId) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("refundId = :refundId");
        values.put("refundId", refundId);
        conditions.add("memberId = :memberId");
        values.put("memberId", memberId);
        return refundDao.getRefundByParams(conditions, values);
    }

    public RefundItemVo getRefundWaitingRefundVo(int ordersId) {
        return refundDao.getRefundWaitingRefundVo(ordersId);
    }

    public List<RefundItemVo> getRefundVoList(HashMap<String, Object> params, int pageNo, int pageSize) {
        List<Object> list = getRefundListCondition(params);
        List<Object> condition = (List<Object>) list.get(0);
        HashMap<String, Object> values = (HashMap<String, Object>) list.get(1);
        List<RefundItemVo> l = (List) refundDao.getRefundVoList(condition, values, pageNo, pageSize);
        return l;
    }

    public List<RefundItemVo> getRefundVoList(int ordersId, int ordersGoodsId) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("ordersId=:ordersId");
        values.put("ordersId", ordersId);
        conditions.add("ordersGoodsId=:ordersGoodsId");
        values.put("ordersGoodsId", ordersGoodsId);
        List<RefundItemVo> l = (List) refundDao.getRefundVoList(conditions, values);
        return l;
    }

    /**
     * 根据订单id获取所有退款单
     * @param ordersId
     * @return
     */
    public List<RefundItemVo> getRefundVoList(int ordersId) {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("ordersId=:ordersId");
        values.put("ordersId", ordersId);
        List<RefundItemVo> l = (List) refundDao.getRefundVoList(conditions, values);
        return l;
    }
    /**
     * 根据条件获取退款数量
     *
     * @param params
     * @return
     */
    public long getRefundListCount(HashMap<String, Object> params) {
        List<Object> list = getRefundListCondition(params);
        List<Object> condition = (List<Object>) list.get(0);
        HashMap<String, Object> values = (HashMap<String, Object>) list.get(1);
        return refundDao.getRefundListCount(condition, values);
    }

    /**
     * 判断是否能够整单退款
     *
     * @param
     * @return
     */
    public boolean allowRefundAll(int orderId, int orderState, String paymentTypeCode) {
        //未同意退款数
        int refundDisagreeCount = refundDao.getRefundDisagreeCount(orderId);
        return refundDisagreeCount == 0 &&
                orderState == OrdersOrdersState.PAY &&
                paymentTypeCode.equals(OrdersPaymentTypeCode.ONLINE);
    }

    public boolean allowRefundAll(RefundItemVo refundItemVo, int orderState, String paymentTypeCode) {

        return refundItemVo == null &&
                orderState == OrdersOrdersState.PAY &&
                paymentTypeCode.equals(OrdersPaymentTypeCode.ONLINE);
    }
    /**
     * 生成退款编号
     * 生成规则：年取1位 + 两位随机 + payId取11位 + 第N个子订单取2位)
     *
     * @param storeId
     * @return
     */
    protected long makeRefundSn(int storeId) {
        String a = Integer.toString((int) (storeId) + 100);
        SimpleDateFormat s = new SimpleDateFormat("MdHms");
        String curDate = s.format(new Date());  //当前日期
        String refundSn = Integer.toString((int) (Math.random() * 999999) + 100)
                + a.substring(a.length() - 3, a.length())
                + curDate;


        return Long.parseLong(refundSn);
    }

    /**
     * 生成一个退款批次号
     *
     * @param refundId
     * @return
     */
    public String makeBatchNo(int refundId) {
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
        String curDate = s.format(new Date());
        String a = Integer.toString((int) (Math.random() * 9999));
        String b = Integer.toString((int) (refundId + 100));
        String c = b.substring(b.length() - 3, b.length());
        return curDate + a + c;
    }

    /**
     * 处理查询条件
     *
     * @param params
     * @return
     */
    private List<Object> getRefundListCondition(HashMap<String, Object> params) {
        List<String> conditions = new ArrayList<String>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (String key : params.keySet()) {
            if (key.equals("storeId")) {
                conditions.add("storeId=:storeId");
                values.put("storeId", params.get(key));
            }
            if (key.equals("memberId")) {
                conditions.add("memberId = :memberId");
                values.put("memberId", params.get(key));
            }
            if (key.equals("addTimeStart")) {
                conditions.add("addTime >= :addTimeStart");
                values.put("addTimeStart", params.get(key));
            }
            if (key.equals("addTimeEnd")) {
                conditions.add("addTime <= :addTimeEnd");
                Date date = new Date(((Timestamp) params.get(key)).getTime() + 60 * 60 * 1000 * 24 - 1000);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                values.put("addTimeEnd", Timestamp.valueOf(simpleDateFormat.format(date)));
            }
            //买家订单列表
            if (key.equals("searchType")) {
                if (params.get(key).equals("ordersSn") && params.containsKey("keyword")) {
                    conditions.add("ordersSn = :ordersSn");
                    if (Pattern.matches("[0-9]*", params.get("keyword").toString())) {
                        values.put("ordersSn", Long.parseLong(params.get("keyword").toString()));
                    } else {
                        values.put("ordersSn", 0L);
                    }
                }
                if (params.get(key).equals("refundSn") && params.containsKey("keyword")) {
                    conditions.add("refundSn = :refundSn");
                    if (Pattern.matches("[0-9]*", params.get("keyword").toString())) {
                        values.put("refundSn", Long.parseLong(params.get("keyword").toString()));
                    } else {
                        values.put("refundSn", 0L);
                    }
                }
                if (params.get(key).equals("goodsName") && params.containsKey("keyword")) {
                    conditions.add("goodsName like :goodsName");
                    values.put("goodsName", "%" + params.get("keyword") + "%");
                }
                if (params.get(key).equals("memberName") && params.containsKey("keyword")) {
                    conditions.add("memberName like :memberName");
                    values.put("memberName", "%" + params.get("keyword") + "%");
                }
            }
            if (key.equals("ordersSn")) {
                conditions.add("ordersSn = :ordersSn");
                if (Pattern.matches("[0-9]*", params.get(key).toString())) {
                    values.put("ordersSn", Long.parseLong(params.get(key).toString()));
                } else {
                    values.put("ordersSn", 0L);
                }
            }
            if (key.equals("refundSn")) {
                conditions.add("refundSn = :refundSn");
                if (Pattern.matches("[0-9]*", params.get(key).toString())) {
                    values.put("refundSn", Long.parseLong(params.get(key).toString()));
                } else {
                    values.put("refundSn", 0L);
                }
            }
            if (key.equals("goodsName")) {
                conditions.add("goodsName like :goodsName");
                values.put("goodsName", "%" + params.get(key) + "%");
            }
            if (key.equals("memberName")) {
                conditions.add("memberName like :memberName");
                values.put("memberName", "%" + params.get(key) + "%");
            }

            if (key.equals("stateType")) {
                //待审核
                if (params.get(key).equals("waiting")) {
                    conditions.add("refundState = :refundState");
                    values.put("refundState", RefundState.REFUND_STATE_WAITING);
                }
                //同意
                if (params.get(key).equals("agree")) {
                    conditions.add("sellerState = :sellerState");
                    values.put("sellerState", RefundState.SELLER_STATE_AGREE);

                    conditions.add("refundState = :refundState");
                    values.put("refundState", RefundState.REFUND_STATE_FINISH);
                }
                //不同意
                if (params.get(key).equals("disagree")) {
                    conditions.add("sellerState = :sellerState");
                    values.put("sellerState", RefundState.SELLER_STATE_DISAGREE);
                    conditions.add("refundState = :refundState");
                    values.put("refundState", RefundState.REFUND_STATE_FINISH);
                }
            }
            if (key.equals("refundType")) {
                conditions.add("refundType = :refundType");
                values.put("refundType", params.get(key));
            }
        }

        List<Object> list = new ArrayList<Object>();
        list.add(conditions);
        list.add(values);
        return list;
    }

    /**
     * 获取店铺退款总数
     */
    public long getRefundWaitingCountByStoreId(int storeId) {
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return refundDao.getRefundWaitingCount(where, params);
    }

    /**
     * 获取店铺退货总数
     */
    public long getReturnWaitingCountByStoreId(int storeId) {
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return refundDao.getReturnWaitingCount(where, params);
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
