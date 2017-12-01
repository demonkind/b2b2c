package net.shopnc.b2b2c.service.buy;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.buy.BuyGoodsDao;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo;
import net.shopnc.b2b2c.vo.buy.BuyStoreVo;
import net.shopnc.common.entity.buy.BuyFirstPostDataEntity;
import net.shopnc.common.entity.buy.BuySecondPostDataEntity;
import net.shopnc.common.util.PriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 下单时处理商品相关
 * Created by hbj on 2015/12/16.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BuyGoodsService extends BaseService {
    @Autowired
    private BuyGoodsDao buyGoodsDao;

    /**
     * 购买第一步 得到购买商品列表
     * @param buyFirstPostDataEntity
     * @return
     */
    public List<BuyGoodsItemVo> getBuyFirstGoodsItemVoList(BuyFirstPostDataEntity buyFirstPostDataEntity) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        List<BuyGoodsItemVo> buyGoodsItemVoList;
        //得到商品列表
        if (buyFirstPostDataEntity.getIsCart() == State.YES) {
            //来源于购物车时联查购物车得到购买商品列表
            buyGoodsItemVoList = getBuyGoodsItemVoListFromCart(buyFirstPostDataEntity.getCartIdList(), buyFirstPostDataEntity.getMemberId());
        } else {
            //来源于直接购买时得到购买商品列表
            buyGoodsItemVoList = getBuyGoodsItemVoListFromBuy(buyFirstPostDataEntity.getGoodsIdList());
            //直接购买时判断设置库存状态
            buyGoodsItemVoList = getBuyGoodsItemVoListForSetStorage(buyGoodsItemVoList, buyFirstPostDataEntity.getBuyGoodsIdAndBuyNumList());
        }
        return buyGoodsItemVoList;
    }

    /**
     * 购买第二步 得到购买商品列表
     * @param buySecondPostDataEntity
     * @return
     */
    public List<BuyGoodsItemVo> getBuySecondGoodsItemVoList(BuySecondPostDataEntity buySecondPostDataEntity) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        List<BuyGoodsItemVo> buyGoodsItemVoList;
        //得到商品列表
        if (buySecondPostDataEntity.getIsCart() == State.YES) {
            //来源于购物车时联查购物车得到购买商品列表
            buyGoodsItemVoList = getBuyGoodsItemVoListFromCart(buySecondPostDataEntity.getCartIdList(), buySecondPostDataEntity.getMemberId());
        } else {
            //来源于直接购买时得到购买商品列表
            buyGoodsItemVoList = getBuyGoodsItemVoListFromBuy(buySecondPostDataEntity.getGoodsIdList());
            //直接购买时判断设置库存状态
            buyGoodsItemVoList = getBuyGoodsItemVoListForSetStorage(buyGoodsItemVoList, buySecondPostDataEntity.getBuyGoodsIdAndBuyNumList());
        }
        return buyGoodsItemVoList;
    }

    /**
     * 购买第一/二步 - 来源于购物车时联查购物车得到购买商品列表
     * @param cartIdList
     * @param memberId
     * @return
     */
    @Transactional
    public List<BuyGoodsItemVo> getBuyGoodsItemVoListFromCart(List<Integer> cartIdList, int memberId) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        List<Object> buyGoodsItemVoList = buyGoodsDao.getBuyGoodsItemVoList(cartIdList,memberId);
        List<BuyGoodsItemVo> buyGoodsItemVoListNew = new ArrayList<BuyGoodsItemVo>();
        for (int i=0; i<buyGoodsItemVoList.size(); i++) {
            BuyGoodsItemVo buyGoodsItemVo = (BuyGoodsItemVo)buyGoodsItemVoList.get(i);
            buyGoodsItemVoListNew.add(buyGoodsItemVo);
        }
        return buyGoodsItemVoListNew;
    }

    /**
     * 购买第一/二步 - 来源于直接购买时得到购买商品列表
     * @param goodsIdList
     * @return
     */
    @Transactional
    public List<BuyGoodsItemVo> getBuyGoodsItemVoListFromBuy(List<Integer> goodsIdList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        List<Object> buyGoodsItemVoList = buyGoodsDao.getBuyGoodsItemVoList(goodsIdList);
        List<BuyGoodsItemVo> buyGoodsItemVoListNew = new ArrayList<BuyGoodsItemVo>();
        for (int i=0; i<buyGoodsItemVoList.size(); i++) {
            BuyGoodsItemVo buyGoodsItemVo = (BuyGoodsItemVo)buyGoodsItemVoList.get(i);
            buyGoodsItemVoListNew.add(buyGoodsItemVo);
        }
        return buyGoodsItemVoListNew;
    }

    /**
     * 购买第一/二步 - 将商品列表按店铺分组
     * @param buyGoodsItemVoList
     * @return
     */
    @Transactional
    public List<BuyStoreVo> getBuyStoreVoList(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        //先存入hashMap
        HashMap<Integer, List<BuyGoodsItemVo>> hashMap = new HashMap<Integer, List<BuyGoodsItemVo>>();
        int storeId;
        for (int i = 0; i < buyGoodsItemVoList.size(); i++) {
            List<BuyGoodsItemVo> buyGoodsItemVoList1 = new ArrayList<BuyGoodsItemVo>();
            storeId = buyGoodsItemVoList.get(i).getStoreId();
            if (hashMap.containsKey(storeId)) {
                buyGoodsItemVoList1 = hashMap.get(storeId);
            }
            buyGoodsItemVoList1.add(buyGoodsItemVoList.get(i));
            hashMap.put(storeId, buyGoodsItemVoList1);
        }

        //再存入buyStoreVoList
        List<BuyStoreVo> buyStoreVoList = new ArrayList<BuyStoreVo>();
        for(int storeId1 : hashMap.keySet()) {
            BuyStoreVo buyStoreVo = new BuyStoreVo(hashMap.get(storeId1));
            buyStoreVoList.add(buyStoreVo);
        }
        return buyStoreVoList;
    }

    /**
     * 直接购买时，设置购买量及判断库存是否足够
     * @param buyGoodsItemVoList
     * @param goodsIdAndBuyNumList
     * @return
     */
    public List<BuyGoodsItemVo> getBuyGoodsItemVoListForSetStorage(List<BuyGoodsItemVo> buyGoodsItemVoList,HashMap<Integer,Integer> goodsIdAndBuyNumList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        int buyNum;
        for (int i=0; i<buyGoodsItemVoList.size(); i++) {
            buyNum = goodsIdAndBuyNumList.get(buyGoodsItemVoList.get(i).getGoodsId());
            buyGoodsItemVoList.get(i).setBuyNum(buyNum);
            buyGoodsItemVoList.get(i).setItemAmount(PriceHelper.mul(buyGoodsItemVoList.get(i).getGoodsPrice(),buyNum));
            buyGoodsItemVoList.get(i).setStorageStatus(buyGoodsItemVoList.get(i).getGoodsStorage() < buyNum ? 0 : 1);
        }
        return buyGoodsItemVoList;
    }

    /**
     * 购买第一/二步 - 来源于直接购买时得到购买商品列表
     * @param goodsIdList
     * @return
     */
    @Transactional
    public List<BuyGoodsItemVo> getBuyGoodsItemVoByGoodsId(List<Integer> goodsIdList) {
        List<Object> buyGoodsItemVoList = buyGoodsDao.getBuyGoodsItemVoList(goodsIdList);
        List<BuyGoodsItemVo> buyGoodsItemVoListNew = new ArrayList<BuyGoodsItemVo>();
        for (int i=0; i<buyGoodsItemVoList.size(); i++) {
            BuyGoodsItemVo buyGoodsItemVo = (BuyGoodsItemVo)buyGoodsItemVoList.get(i);
            buyGoodsItemVoListNew.add(buyGoodsItemVo);
        }
        return buyGoodsItemVoListNew;
    }

}
