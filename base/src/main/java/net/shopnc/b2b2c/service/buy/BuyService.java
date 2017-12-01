package net.shopnc.b2b2c.service.buy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.constant.InvoiceInvoiceType;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.dao.goods.GoodsSaleDao;
import net.shopnc.b2b2c.dao.orders.CartDao;
import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.goods.GoodsSale;
import net.shopnc.b2b2c.domain.orders.Invoice;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.b2b2c.domain.orders.OrdersPay;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.member.InvoiceService;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo;
import net.shopnc.b2b2c.vo.buy.BuyStoreVo;
import net.shopnc.common.entity.buy.BuyFirstPostDataEntity;
import net.shopnc.common.entity.buy.BuySecondPostDataEntity;
import net.shopnc.common.entity.buy.OrdersEntity;

/**
 * 下单过程
 * Created by hbj on 2015/12/11.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BuyService extends BaseService {
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private BuyOrdersService buyOrdersService;
    @Autowired
    private BuyGoodsService buyGoodsService;
    @Autowired
    private BuyFreightService buyFreightService;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private GoodsSaleDao goodsSaleDao;
    
    @Autowired
    private MemberOrdersService memberOrdersService;
    
    @Autowired
    private GoodsActivityService goodsActivityService;

    /**
     * 购买第一步（1） - 得到购买商品列表 及 商品促销
     * @param buyFirstPostDataEntity
     * @return
     * @throws Exception
     */
    public List<BuyGoodsItemVo> buyFirstGetGoodsList(BuyFirstPostDataEntity buyFirstPostDataEntity,int memberId) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        //得到商品列表
        List<BuyGoodsItemVo> buyGoodsItemVoList = buyGoodsService.getBuyFirstGoodsItemVoList(buyFirstPostDataEntity);

        if(buyGoodsItemVoList!=null){
        	for(BuyGoodsItemVo buyGoodsItemVo:buyGoodsItemVoList){
        		GoodsActivity goodsActivity=(GoodsActivity) goodsActivityService.checkBound(buyGoodsItemVo.getCommonId());
        		 int manxNum=memberOrdersService.findTotalPurchasesOfGoods(buyGoodsItemVo.getCommonId(),memberId);
		            //商铺最大限购数量
		         int maxWeight=Integer.valueOf(goodsActivity.getMaxNum())-(manxNum+buyGoodsItemVo.getBuyNum());
		         if(maxWeight<0){
		        	 throw new ShopException("购买量已超过上限！");
		         }
        	}
        }
        //处理商品级促销[会员折扣改单价、某些商品限时促销等]
        //.......................................

        if (buyGoodsItemVoList.size() == 0) {
            throw new ShopException("未找到商品");
        }
        return buyGoodsItemVoList;
    }

    /**
     * 购买第一步（2） - 得到商品店铺列表[商品以店铺分组] 及 店铺促销
     * @param buyGoodsItemVoList
     * @return
     * @throws Exception
     */
    public List<BuyStoreVo> buyFirstGetStoreList(List<BuyGoodsItemVo> buyGoodsItemVoList) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        //商品按店铺分组
        List<BuyStoreVo> buyStoreVoList = buyGoodsService.getBuyStoreVoList(buyGoodsItemVoList);

        //店铺满X减X、送X、代金券
        //.......................................

        if (buyStoreVoList.size() == 0) {
            throw new ShopException("未找到店铺");
        }
        return buyStoreVoList;
    }

    /**
     * 购买第一步（3） - 得到平台促销
     * @return
     */
    public void buyFirstGetPlatFormPromotionsList() throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());

        //平台红包、券、卡等
        //.......................................

    }

    /**
     * 购买第二步（1） - 验证并处理提交数据[收货地址]
     * @param buySecondPostDataEntity
     * @return
     * @throws Exception
     */
    public BuySecondPostDataEntity buySecondValidatePostDataEntity(BuySecondPostDataEntity buySecondPostDataEntity) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        Address address = addressDao.getAddressInfo(buySecondPostDataEntity.getAddressId(),buySecondPostDataEntity.getMemberId());
        if (address == null) {
            throw new ShopException("收货地址提交错误");
        }
        buySecondPostDataEntity.setAddress(address);

        if (buySecondPostDataEntity.getInvoiceId() > 0) {
            Invoice invoice = invoiceDao.getInvoiceInfo(buySecondPostDataEntity.getInvoiceId(),buySecondPostDataEntity.getMemberId());
            if (invoice == null) {
                throw new ShopException("发票信息提交错误");
            }
            buySecondPostDataEntity.setInvoiceInfo(invoiceService.getOrderFormatInvoiceInfo(invoice));
            buySecondPostDataEntity.setInvoiceType(invoice.getInvoiceType());
        }

        //代金券，红包
        //.......................................

        return buySecondPostDataEntity;

    }


    /**
     * 购买第二步(2) - 得到购买商品列表 及 商品促销
     * @param buySecondPostDataEntity
     * @return
     * @throws Exception
     */
    public List<BuyGoodsItemVo> buySecondGetGoodsList(BuySecondPostDataEntity buySecondPostDataEntity) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        //得到商品列表
        List<BuyGoodsItemVo> buyGoodsItemVoList = buyGoodsService.getBuySecondGoodsItemVoList(buySecondPostDataEntity);
        //处理商品级促销[会员折扣改单价、某些商品限时促销等]
        //.......................................

        if (buyGoodsItemVoList.size() == 0) {
            throw new ShopException("未找到商品");
        }
        return buyGoodsItemVoList;
    }

    /**
     * 锁定并获取商品最新库存
     * @param buyGoodsItemVoList
     * @return
     * @throws Exception
     */
    public List<BuyGoodsItemVo> buySecondGetLockGoodsList(List<BuyGoodsItemVo> buyGoodsItemVoList) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        for (int i=0; i<buyGoodsItemVoList.size(); i++) {
            GoodsSale goodsSale = goodsSaleDao.getLockModelGoodsSaleInfo(buyGoodsItemVoList.get(i).getGoodsId());
            if (goodsSale == null) {
                throw new ShopException("部分商品已下架，请重新下单");
            }
            if (goodsSale.getGoodsStorage() < buyGoodsItemVoList.get(i).getBuyNum()) {
                throw new ShopException("部分商品库存不足，请重新下单");
            }
            //重新更新库存
            buyGoodsItemVoList.get(i).setGoodsStorage(goodsSale.getGoodsStorage());
            if (goodsSale.getGoodsStorageAlarm() >= goodsSale.getGoodsStorage() - buyGoodsItemVoList.get(i).getBuyNum()) {
                buyGoodsItemVoList.get(i).setIsGoodsStorageAlarm(1);
            }
        }
        return buyGoodsItemVoList;
    }

    /**
     * 购买第二步(3) - 得到商品店铺列表[商品以店铺分组]
     * @param buyGoodsItemVoList
     * @return
     * @throws Exception
     */
    public List<BuyStoreVo> buySecondGetStoreList(List<BuyGoodsItemVo> buyGoodsItemVoList) throws Exception {
        //商品按店铺分组
        List<BuyStoreVo> buyStoreVoList = buyGoodsService.getBuyStoreVoList(buyGoodsItemVoList);

        if (buyStoreVoList.size() == 0) {
            throw new ShopException("未找到店铺");
        }
        return buyStoreVoList;
    }

    /**
     * 购买第二步(4) - 计算运费及配送
     * @param buySecondPostDataEntity
     * @param buyStoreVoList
     * @return
     * @throws Exception
     */
    public List<BuyStoreVo> buySecondCalcStoreFreight(BuySecondPostDataEntity buySecondPostDataEntity,List<BuyStoreVo> buyStoreVoList) throws Exception {
        List<BuyStoreVo> buyStoreVoList1 = buyFreightService.calcFreight(buySecondPostDataEntity.getAddressId(), buySecondPostDataEntity.getMemberId(), buyStoreVoList);
        return buyStoreVoList1;
    }

    /**
     * 购买第二步(5) - 是否存在不支持配送的商品
     * @param buyStoreVoList
     * @throws Exception
     */
    public void buySecondIsExitsNoAllowSendGoods(List<BuyStoreVo> buyStoreVoList) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        for(BuyStoreVo buyStoreVo : buyStoreVoList) {
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyStoreVo.getBuyGoodsItemVoList();
            for (BuyGoodsItemVo buyGoodsItemVo : buyGoodsItemVoList) {
                if (buyGoodsItemVo.getAllowSend() == State.NO) {
                    throw new ShopException("部分商品无法配送");
                }
            }
        }
    }

    /**
     * 购买第二步(6) - 处理店铺促销
     */
    public void buySecondParseStorePromotions() {
        //店铺满X减X、送X、代金券
        //.......................................
    }

    /**
     * 购买第二步(7) - 验证买家使用的平台促销
     * @return
     */
    public void buySecondParsePlatFormPromotions() {
        //平台营销信息[平台红包、券、卡等]
        //.......................................
    }

    /**
     * 生成订单前的其它通用验证
     * @param buySecondPostDataEntity
     * @param buyGoodsItemVoList
     */
    public BuySecondPostDataEntity buySecondValidateCommon(BuySecondPostDataEntity buySecondPostDataEntity,
                                        List<BuyGoodsItemVo> buyGoodsItemVoList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        //验证发票
        if (buySecondPostDataEntity.getInvoiceType() == InvoiceInvoiceType.VAT && getAllowVat(buyGoodsItemVoList) == State.NO) {
            //如果验证错误，直接设置为不需要发票，不提示异常
            logger.info("增值税发票验证错误，系统重置为默认不需要发票");
            buySecondPostDataEntity.setInvoiceInfo(null);
        }

        //货到付款不在此验证，生成订单时判断，到时如果判断非法，直接生成在线支付订单

        return buySecondPostDataEntity;

    }

    /**
     * 购买第二步(8) - 生成订单（如果发生异常，全部回滚）
     * @param buySecondPostDataEntity
     * @param buyStoreVoList
     * @return
     * @throws Exception
     */
    public OrdersEntity buySecondeCreateOrders(BuySecondPostDataEntity buySecondPostDataEntity,List<BuyStoreVo> buyStoreVoList) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        //生成订单支付表数据
        logger.info("生成订单支付表数据");
        OrdersPay ordersPay = buyOrdersService.addOrdersPay(buySecondPostDataEntity);

        //生成订单表数据
        logger.info("生成订单表数据");
        OrdersEntity ordersEntity = buyOrdersService.addOrders(buyStoreVoList, buySecondPostDataEntity, ordersPay);

        //更新库存等
        logger.info("更新库存等");
        buySecondOrdersAfterUpdate(buySecondPostDataEntity, ordersEntity);

        return ordersEntity;
    }

    /**
     * 购买第二步（9） 更新库存、删除购物车等[如果错误需要回滚]
     * @param buySecondPostDataEntity
     * @param ordersEntity
     * @throws Exception
     */
    public void buySecondOrdersAfterUpdate(BuySecondPostDataEntity buySecondPostDataEntity, OrdersEntity ordersEntity) throws Exception {
        //更新库存和销量
        logger.info("更新库存和销量");
        List<OrdersGoods> ordersGoodsList = new ArrayList<>();
        for (Integer storeId : ordersEntity.getOrdersGoodsList().keySet()) {
            List<OrdersGoods> ordersGoodsList1 = ordersEntity.getOrdersGoodsList().get(storeId);
            ordersGoodsList.addAll(ordersGoodsList1);
        }
        buyOrdersService.ordersAfterUpdateStorage(ordersGoodsList);

        //删除购物车
        if (buySecondPostDataEntity.getCartIdList().size() > 0) {
            logger.info("删除购物车");
            cartDao.delCart(buySecondPostDataEntity.getMemberId(),buySecondPostDataEntity.getCartIdList());
        }

    }

    /**
     * 购买第二步（10） 消息通知等不需要回滚的操作
     * @param ordersEntity
     * @param buyGoodsItemVoList
     * @throws Exception
     */
    public void buySecondOrdersAfterCommon(OrdersEntity ordersEntity, List<BuyGoodsItemVo> buyGoodsItemVoList) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        //消息通知
        logger.info("新订单消息通知");
        HashMap<String, Object> hashMapMsg = new HashMap<>();
        for (Orders orders : ordersEntity.getOrdersList()) {
            hashMapMsg.put("ordersSn",Long.toString(orders.getOrdersSn()));
            sendMessageService.sendStore("storeOrdersNew", orders.getStoreId(), hashMapMsg, Integer.toString(orders.getOrdersId()));
        }

        //消息通知
        hashMapMsg = new HashMap<>();
        for (BuyGoodsItemVo buyGoodsItemVo : buyGoodsItemVoList) {
            if (buyGoodsItemVo.getIsGoodsStorageAlarm() == 1) {
                logger.info("库存预警消息通知");
                hashMapMsg.put("commonId",Integer.toString(buyGoodsItemVo.getCommonId()));
                hashMapMsg.put("goodsId",Integer.toString(buyGoodsItemVo.getGoodsId()));
                sendMessageService.sendStore("storeGoodsStorageAlarm", buyGoodsItemVo.getStoreId(), hashMapMsg, Integer.toString(buyGoodsItemVo.getCommonId()));
            }
        }
    }

    /**
     * 是否允许使用增值税发票 0否/1是
     * @param buyGoodsItemVoList
     * @return
     */
    public int getAllowVat(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        int allowVat = State.YES;
        for (BuyGoodsItemVo buyGoodsItemVo : buyGoodsItemVoList) {
            if (buyGoodsItemVo.getAllowVat() == State.NO) {
                allowVat = State.NO;
                break;
            }
        }
        return allowVat;
    }

    /**
     * 取得允许货到付款的商品列表
     * @param buyStoreVoList
     * @return
     */
    public List<BuyGoodsItemVo> getBuyGoodsItemVoOffLineList(List<BuyStoreVo> buyStoreVoList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        List<BuyGoodsItemVo> buyGoodsItemVoList = new ArrayList<>();
        for (BuyStoreVo buyStoreVo : buyStoreVoList) {
            if (buyStoreVo.getIsOwnShop() == State.YES) {
                buyGoodsItemVoList.addAll(buyStoreVo.getBuyGoodsItemVoList());
            }
        }
        return buyGoodsItemVoList;
    }

    /**
     * 取得允许货到付款的商品列表
     * @param buyStoreVoList
     * @return
     */
    public List<BuyGoodsItemVo> getBuyGoodsItemVoOnLineList(List<BuyStoreVo> buyStoreVoList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        List<BuyGoodsItemVo> buyGoodsItemVoList = new ArrayList<>();
        for (BuyStoreVo buyStoreVo : buyStoreVoList) {
            if (buyStoreVo.getIsOwnShop() == State.NO) {
                buyGoodsItemVoList.addAll(buyStoreVo.getBuyGoodsItemVoList());
            }
        }
        return buyGoodsItemVoList;
    }

}