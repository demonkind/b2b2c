package net.shopnc.b2b2c.service.buy;

import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.dao.goods.GoodsActivityDao;
import net.shopnc.b2b2c.dao.goods.GoodsSaleDao;
import net.shopnc.b2b2c.dao.goods.StoreBindCategoryDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.orders.OrdersGoodsDao;
import net.shopnc.b2b2c.dao.orders.OrdersPayDao;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.goods.StoreBindCategory;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.b2b2c.domain.orders.OrdersPay;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo;
import net.shopnc.b2b2c.vo.buy.BuyStoreVo;
import net.shopnc.common.entity.buy.BuySecondPostDataEntity;
import net.shopnc.common.entity.buy.OrdersEntity;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 下单时生成订单
 * Created by hbj on 2015/12/17.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BuyOrdersService extends BaseService {

    @Autowired
    private OrdersPayDao ordersPayDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrdersGoodsDao ordersGoodsDao;
    @Autowired
    private StoreBindCategoryDao storeBindCategoryDao;
    @Autowired
    private GoodsSaleDao goodsSaleDao;
    @Autowired
    private GoodsActivityDao goodsActivityDao;

    /**
     * 生成支付单号
     * 生成规则：两位随机 + 从2000-01-01 00:00:00 到现在的秒数+微秒+会员ID%1000，该值会传给第三方支付接口
     * 长度 =2位 + 10位 + 3位 + 3位  = 18位
     * 1000个会员同一微秒提订单，重复机率为1/100
     * @param memberId
     * @return
     */
    private long getPaySn(int memberId ) {
        String paySn = Integer.toString((int)Math.round(Math.random() * 89 + 10))
                + String.format("%010d", Integer.parseInt(Long.toString(System.currentTimeMillis() / 1000L)) - 946656000)
                + String.format("%03d", Math.round(Math.random() * 1000))
                + String.format("%03d", memberId % 1000);
        return Long.parseLong(paySn);
    }

    /**
     * 生成订单编号
     * 生成规则：年取1位 + 两位随机 + payId取11位 + 第N个子订单取2位)
     * @param payId
     * @param counter
     * @return
     */
    private long makeOrdersSn(int payId,int counter) {
        String orderSn = Integer.toString(Calendar.getInstance().get(Calendar.YEAR) % 9 + 1)
                + Integer.toString((int) Math.round(Math.random() * 89 + 10))
                + String.format("%011d",payId)
                + String.format("%02d",counter);
        return Long.parseLong(orderSn);
    }

    /**
     * 生成订单支付表数据
     * @param buySecondPostDataEntity
     * @return
     * @throws Exception
     */
    public OrdersPay addOrdersPay(BuySecondPostDataEntity buySecondPostDataEntity) throws Exception {
        OrdersPay ordersPay = new OrdersPay();
        ordersPay.setPaySn(getPaySn(buySecondPostDataEntity.getMemberId()));
        ordersPay.setMemberId(buySecondPostDataEntity.getMemberId());
        int payId = (Integer)ordersPayDao.save(ordersPay);
        if (payId <= 0) {
            throw new Exception("订单生成失败");
        }
        ordersPay.setPayId(payId);
        return ordersPay;
    }

    /**
     * 生成订单表数据
     * @param buyStoreVoList
     * @param buySecondPostDataEntity
     * @param ordersPay
     * @return
     * @throws Exception
     */
    public OrdersEntity addOrders(List<BuyStoreVo> buyStoreVoList,BuySecondPostDataEntity buySecondPostDataEntity,OrdersPay ordersPay) throws Exception {
        OrdersEntity ordersEntity = new OrdersEntity();

        List<Orders> ordersList = new ArrayList<Orders>();
        for(int i=0; i<buyStoreVoList.size(); i++) {
            BuyStoreVo buyStoreVo = buyStoreVoList.get(i);
            Orders orders = new Orders();
            //订单信息
            orders.setOrdersSn(makeOrdersSn(ordersPay.getPayId(), i));
            orders.setCreateTime(ShopHelper.getCurrentTimestamp());
            //支付单信息
            orders.setPayId(ordersPay.getPayId());
            orders.setPaySn(ordersPay.getPaySn());
            //订单金额相关
            orders.setOrdersAmount(buyStoreVo.getBuyAmount());
            orders.setFreightAmount(buyStoreVo.getStoreFreightAmount());
            //买家信息
            orders.setMemberId(buySecondPostDataEntity.getMemberId());
            orders.setMemberName(buySecondPostDataEntity.getMemberName());
            orders.setReceiverAreaId1(buySecondPostDataEntity.getAddress().getAreaId1());
            orders.setReceiverAreaId2(buySecondPostDataEntity.getAddress().getAreaId2());
            orders.setReceiverAreaId3(buySecondPostDataEntity.getAddress().getAreaId3());
            orders.setReceiverAreaId4(buySecondPostDataEntity.getAddress().getAreaId4());
            orders.setReceiverAreaInfo(buySecondPostDataEntity.getAddress().getAreaInfo());
            orders.setReceiverAddress(buySecondPostDataEntity.getAddress().getAddress());
            orders.setReceiverName(buySecondPostDataEntity.getAddress().getRealName());
            if (buySecondPostDataEntity.getAddress().getMobPhone() != null && !buySecondPostDataEntity.getAddress().getMobPhone().equals("")) {
                orders.setReceiverPhone(buySecondPostDataEntity.getAddress().getMobPhone());
            } else {
                orders.setReceiverPhone(buySecondPostDataEntity.getAddress().getTelPhone());
            }
            orders.setReceiverMessage(buySecondPostDataEntity.getReceiverMessageList().get(buyStoreVo.getStoreId()));
            //商家信息
            orders.setStoreId(buyStoreVo.getStoreId());
            orders.setStoreName(buyStoreVo.getStoreName());
            //评价
            orders.setEvaluationState(OrdersEvaluationState.NO);
            //发票
            orders.setInvoiceInfo(buySecondPostDataEntity.getInvoiceInfo());
            //来源
            orders.setOrdersFrom(buySecondPostDataEntity.getOrdersFrom());
            //订单类型,目前只有一个默认普通订单
            orders.setOrdersType(OrdersOrdersType.NORMAL);
            //支付状态
            if (buySecondPostDataEntity.getPaymentTypeCode() == OrdersPaymentTypeCode.OFFLINE &&
                    buyStoreVo.getIsOwnShop() == State.YES) {
                orders.setOrdersState(OrdersOrdersState.PAY);
                orders.setPaymentTypeCode(OrdersPaymentTypeCode.OFFLINE);
                orders.setPaymentCode(OrdersPaymentCode.OFFLINE);
            } else {
                orders.setOrdersState(OrdersOrdersState.NEW);
                orders.setPaymentTypeCode(OrdersPaymentTypeCode.ONLINE);
            }

            int orderId = (Integer)ordersDao.save(orders);
            if (orderId <= 0) {
                throw new Exception("订单生成失败");
            }
            orders.setOrdersId(orderId);
            ordersList.add(orders);
            //插入订单商品表
            logger.info("生成订单商品表数据");
            List<OrdersGoods> ordersGoodsList = addOrdersGoods(buyStoreVo.getBuyGoodsItemVoList(), orderId, buyStoreVo.getStoreId(),buySecondPostDataEntity.getMemberId());
            ordersEntity.ncAddOrdersGoodsList(orderId,ordersGoodsList);
        }
        ordersEntity.setOrdersList(ordersList);
        ordersEntity.setOrdersPay(ordersPay);
        return ordersEntity;
    }

    /**
     * 生成订单商品表数据
     * @param buyGoodsItemVoList
     * @param orderId
     * @param storeId
     * @param memberId
     * @throws Exception
     */
    public List<OrdersGoods> addOrdersGoods(List<BuyGoodsItemVo> buyGoodsItemVoList, int orderId, int storeId, int memberId) throws Exception {
        List<OrdersGoods> ordersGoodsList = new ArrayList<OrdersGoods>();
        for (int i=0; i<buyGoodsItemVoList.size(); i++) {
            OrdersGoods ordersGoods = new OrdersGoods();
            ordersGoods.setOrdersId(orderId);
            ordersGoods.setGoodsId(buyGoodsItemVoList.get(i).getGoodsId());
            ordersGoods.setCommonId(buyGoodsItemVoList.get(i).getCommonId());
            ordersGoods.setGoodsName(buyGoodsItemVoList.get(i).getGoodsName());
            ordersGoods.setGoodsImage(buyGoodsItemVoList.get(i).getImageName());
            ordersGoods.setGoodsFullSpecs(buyGoodsItemVoList.get(i).getGoodsFullSpecs());
            ordersGoods.setBuyNum(buyGoodsItemVoList.get(i).getBuyNum());
            ordersGoods.setGoodsPayAmount(buyGoodsItemVoList.get(i).getItemAmount());
            ordersGoods.setGoodsType(OrdersGoodsGoodsType.NORMAL);
            ordersGoods.setGoodsPrice(buyGoodsItemVoList.get(i).getGoodsPrice());
            ordersGoods.setMemberId(memberId);
            ordersGoods.setStoreId(storeId);
            ordersGoods.setCategoryId1(buyGoodsItemVoList.get(i).getCategoryId1());
            ordersGoods.setCategoryId2(buyGoodsItemVoList.get(i).getCategoryId2());
            ordersGoods.setCategoryId3(buyGoodsItemVoList.get(i).getCategoryId3());
            ordersGoods.setCommissionRate(getStoreBindCategory(storeId,buyGoodsItemVoList.get(i).getCategoryId()));
            ordersGoods.setCategoryId(buyGoodsItemVoList.get(i).getCategoryId());
            int ordersGoodsId = (Integer)ordersGoodsDao.save(ordersGoods);
            if (ordersGoodsId <= 0) {
                throw new Exception("订单生成失败");
            }
            ordersGoods.setOrdersGoodsId(ordersGoodsId);
            ordersGoodsList.add(ordersGoods);
        }
        return ordersGoodsList;
    }

    /**
     * 更新库存
     * @param ordersGoodsList
     * @throws Exception
     */
    public void ordersAfterUpdateStorage(List<OrdersGoods> ordersGoodsList) throws Exception {
        for (OrdersGoods ordersGoods : ordersGoodsList) {
        	goodsSaleDao.updateFromCreateOrders(ordersGoods.getGoodsId(),ordersGoods.getCommonId(),ordersGoods.getBuyNum());
            //根据订单中的商品commonId来检索商品是否有参加活动 。如果存在参加活动就更新剩余活动中的商品剩余量
        	GoodsActivity goodsActivity=(GoodsActivity) goodsActivityDao.checkBount(Integer.valueOf(ordersGoods.getCommonId()));
            if(goodsActivity!=null&&goodsActivity.getSaleWeight()!=null){
            	int saleWeight=Integer.valueOf(goodsActivity.getSaleWeight());
            	goodsActivity.setSaleWeight(String.valueOf(saleWeight-ordersGoods.getBuyNum()));
            	goodsActivityDao.update(goodsActivity);
            }
        }
    }

    /**
     * 取得佣金比例
     * @param storeId
     * @param categoryId
     * @return
     */
    private int getStoreBindCategory(int storeId, int categoryId) {
        StoreBindCategory storeBindCategory = storeBindCategoryDao.getCommissionRateByStoreIdAndCategoryId(storeId,categoryId);
        if (storeBindCategory == null) {
            return 0;
        } else {
            return storeBindCategory.getCommissionRate();
        }
    }
}
