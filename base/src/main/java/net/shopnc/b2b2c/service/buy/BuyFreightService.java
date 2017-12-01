package net.shopnc.b2b2c.service.buy;

import net.shopnc.b2b2c.constant.FreightTemplateCalcType;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.dao.orders.FreightAreaDao;
import net.shopnc.b2b2c.dao.orders.FreightTemplateDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.domain.FreightArea;
import net.shopnc.b2b2c.domain.FreightTemplate;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.FreightTemplateService;
import net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo;
import net.shopnc.b2b2c.vo.buy.BuyStoreVo;
import net.shopnc.b2b2c.vo.buy.freight.BuyGoodsItemFreightVo;
import net.shopnc.b2b2c.vo.buy.freight.BuyStoreFreightListVo;
import net.shopnc.b2b2c.vo.buy.freight.BuyStoreFreightVo;
import net.shopnc.common.util.PriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下单时计算配送规则
 * Created by hbj on 2015/12/29.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BuyFreightService extends BaseService {
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private FreightTemplateDao freightTemplateDao;
    @Autowired
    private FreightTemplateService freightTemplateService;
    @Autowired
    private FreightAreaDao freightAreaDao;

    /**
     * 计算商品运费及配送
     * @param addressId
     * @param memberId
     * @param buyStoreVoList
     * @return
     * @throws Exception
     */
    public List<BuyStoreVo> calcFreight(Integer addressId, Integer memberId, List<BuyStoreVo> buyStoreVoList) throws Exception {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        logger.info("得到收货地址");
        Address address = addressDao.getAddressInfo(addressId,memberId);
        if (address == null) {
            throw new ShopException("收货地址错误");
        }

        //计算店铺商品总金额、店铺运费(固定运费商品运费总金额)
        logger.info("计算固定运费");
        List<BuyStoreVo> buyStoreVoList1 = calcFixedFreight(buyStoreVoList);

        //计算是否配送、店铺运费(固定运费总金额 + 运费模板运费总金额)
        logger.info("计算运费模板");
        buyStoreVoList1 = calcTemplateFreight(buyStoreVoList1,address.getAreaId2());

        //计算满X包邮后的运费
        logger.info("计算满X包邮");
        buyStoreVoList1 = calcFreightFree(buyStoreVoList1);

        //计算店铺总金额(商品 + 运费)
        logger.info("计算店铺总金额(商品 + 运费)");
        for (int i=0; i<buyStoreVoList1.size(); i++) {
            buyStoreVoList1.get(i).setBuyAmount(PriceHelper.add(buyStoreVoList1.get(i).getBuyItemAmount(), buyStoreVoList1.get(i).getStoreFreightAmount()));
        }

        return buyStoreVoList1;

    }

    /**
     * 计算店铺商品总金额和固定运费商品总运费金额
     * @param buyStoreVoList
     * @return
     */
    private List<BuyStoreVo> calcFixedFreight(List<BuyStoreVo> buyStoreVoList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        BigDecimal storeFreightAmount;
        BigDecimal buyItemAmount;
        for (int i=0; i<buyStoreVoList.size(); i++) {
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyStoreVoList.get(i).getBuyGoodsItemVoList();
            storeFreightAmount = PriceHelper.format(new BigDecimal(0));
            buyItemAmount = PriceHelper.format(new BigDecimal(0));
            for (int ii=0; ii<buyGoodsItemVoList.size(); ii++) {
                //如果商品已下架或库存不足，不计算它的运费，不配送无货
                if (buyGoodsItemVoList.get(ii).getGoodsStatus() == State.NO || buyGoodsItemVoList.get(ii).getStorageStatus() == State.NO) {
                    buyGoodsItemVoList.get(ii).setAllowSend(State.NO);
                    continue;
                }
                if (buyGoodsItemVoList.get(ii).getFreightTemplateId() == 0) {
                    storeFreightAmount = PriceHelper.add(storeFreightAmount,buyGoodsItemVoList.get(ii).getGoodsFreight());
                }
                buyItemAmount = PriceHelper.add(buyItemAmount,buyGoodsItemVoList.get(ii).getItemAmount());
            }
            buyStoreVoList.get(i).setStoreFreightAmount(storeFreightAmount);
            buyStoreVoList.get(i).setBuyItemAmount(buyItemAmount);
        }
        return buyStoreVoList;
    }

    /**
     * 计算(设置运费模板的)运费金额
     * 首先得到单个店铺中运费模板Id和购买总件/重量/体积的关联
     * 然后计算每个运费模板的运费金额
     * @param buyStoreVoList
     * @param areaId2
     * @return
     */
    private List<BuyStoreVo> calcTemplateFreight(List<BuyStoreVo> buyStoreVoList, int areaId2) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        BigDecimal storeFreightAmount;
        for (int i=0; i<buyStoreVoList.size(); i++) {
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyStoreVoList.get(i).getBuyGoodsItemVoList();

            //得到单个店铺中运费模板Id和购买总量关联、运费模板信息
            HashMap<String, Object> hashMap = getFreightIdAndBuyNumList(buyGoodsItemVoList,areaId2);
            HashMap<Integer,BigDecimal> hashMapFreightIdAndBuyNumList = (HashMap<Integer,BigDecimal>)hashMap.get("hashMapFreightIdAndBuyNumList");
            HashMap<Integer,FreightTemplate> hashMapFreighTemplate = (HashMap<Integer,FreightTemplate>)hashMap.get("hashMapFreighTemplate");

            logger.info("运费模板Id和购买总量关联");
            logger.info(hashMapFreightIdAndBuyNumList);

            //记录不支持配送的商品Id
            List<Integer> noAllowSendGoodsIdList = new ArrayList<>();
            //当前店铺运费，即使用固定运费商品的运费总和
            storeFreightAmount = buyStoreVoList.get(i).getStoreFreightAmount();

            for (BuyGoodsItemVo buyGoodsItemVo : buyGoodsItemVoList) {
                //如果商品已下架或库存不足，不配送
                if (buyGoodsItemVo.getGoodsStatus() == State.NO || buyGoodsItemVo.getStorageStatus() == State.NO) {
                    logger.info("商品已下架或库存不足，不配送 goodsId:" + buyGoodsItemVo.getGoodsId());
                    noAllowSendGoodsIdList.add(buyGoodsItemVo.getGoodsId());
                }
            }

            //分别计算每个运费模板的运费
            //存储查询到的运费模板实体[记录一下是为了不重复查询相同的运费模板]
            HashMap<Integer,List<FreightArea>> hashMapFreightAreaList = new HashMap<>();
            for (Integer freightId : hashMapFreightIdAndBuyNumList.keySet()) {
                //先取得运费模板规则信息
                if (!hashMapFreightAreaList.containsKey(freightId)) {
                    List<FreightArea> freightAreaList = freightAreaDao.getFreightAreaByFreightId(freightId);
                    if (freightAreaList.size() > 0) {
                        hashMapFreightAreaList.put(freightId,freightAreaList);
                    }
                }
                BigDecimal freightAmount = freightTemplateService.calcFreight(hashMapFreightIdAndBuyNumList.get(freightId), areaId2, hashMapFreighTemplate.get(freightId), hashMapFreightAreaList.get(freightId));
                if (freightAmount == null) {
                    //商品不支持配送
                    List<Integer> goodsIdList = getBuyGoodsIdListByFreightId(buyGoodsItemVoList,freightId);
                    noAllowSendGoodsIdList.addAll(goodsIdList);
                } else {
                    //支持配送，运费累加
                    storeFreightAmount = PriceHelper.add(storeFreightAmount,freightAmount);
                }
            }

            //店铺最终运费
            buyStoreVoList.get(i).setStoreFreightAmount(storeFreightAmount);

            //设置不支持配送的商品Id
            if (noAllowSendGoodsIdList.size()>0) {
                for (int ii=0; ii<buyStoreVoList.get(i).getBuyGoodsItemVoList().size(); ii++) {
                    int goodsId = buyStoreVoList.get(i).getBuyGoodsItemVoList().get(ii).getGoodsId();
                    if (noAllowSendGoodsIdList.contains(goodsId)) {
                        buyStoreVoList.get(i).getBuyGoodsItemVoList().get(ii).setAllowSend(State.NO);
                    }
                }
            }

        }

        return buyStoreVoList;
    }

    /**
     * 由运费模板Id得到商品Id
     * @param buyGoodsItemVoList
     * @param freightId
     * @return
     */
    private List<Integer> getBuyGoodsIdListByFreightId(List<BuyGoodsItemVo> buyGoodsItemVoList,int freightId) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        List<Integer> goodsIdList = new ArrayList<>();
        for (int i=0; i<buyGoodsItemVoList.size(); i++) {
            if (buyGoodsItemVoList.get(i).getFreightTemplateId() == freightId) {
                goodsIdList.add(buyGoodsItemVoList.get(i).getGoodsId());
            }
        }
        return goodsIdList;
    }

    /**
     * 得到单个店铺中运费模板Id和购买总件/重量/体积的关联
     * @param buyGoodsItemVoList
     * @param areaId2
     * @return
     */
    private HashMap<String,Object> getFreightIdAndBuyNumList(List<BuyGoodsItemVo> buyGoodsItemVoList,int areaId2) {
        //当以重量/体积计价时，保存运费模板及所购买商品的总重量/体积(单件商品重量/体积*购买数量),使用相同模板的总重量/体积累加,
        //当以件计价时，保存运费模板及所购买的商品的总件数(单件1*购买数量)，使用相同模板的购买件数累加(这里件数也是BigDecimal型，方便三种计价方式统一运算)
        //定义最后返回的变量，格式：HashMap<运费模板Id,总品重量/体积/件数>
        HashMap<Integer,BigDecimal> hashMapFreightIdAndBuyNumList = new HashMap<>();

        //存储查询到的运费模板实体[记录一下是为了不重复查询相同的运费模板]
        HashMap<Integer,FreightTemplate> hashMapFreighTemplate = new HashMap<>();
        for (int i=0; i<buyGoodsItemVoList.size(); i++) {
            //如果商品已下架或库存不足，不计算它的关联信息
            if (buyGoodsItemVoList.get(i).getGoodsStatus() == State.NO || buyGoodsItemVoList.get(i).getStorageStatus() == State.NO) {
                continue;
            }
            int freightId = buyGoodsItemVoList.get(i).getFreightTemplateId();
            if (freightId > 0) {

                //先取得运费模板信息
                if (!hashMapFreighTemplate.containsKey(freightId)) {
                    FreightTemplate freightTemplate = freightTemplateDao.get(FreightTemplate.class,freightId);
                    if (freightTemplate != null) {
                        hashMapFreighTemplate.put(freightId,freightTemplate);
                    }
                }
                //计价方式
                String calcType = hashMapFreighTemplate.get(freightId).getCalcType();

                int buyNum = buyGoodsItemVoList.get(i).getBuyNum();
                BigDecimal buyWeightVolumeNew;
                if (calcType.equals(FreightTemplateCalcType.WEIGHT)) {
                    buyWeightVolumeNew = PriceHelper.mul(buyGoodsItemVoList.get(i).getFreightWeight(),buyNum);
                } else if (calcType.equals(FreightTemplateCalcType.VOLUME)) {
                    buyWeightVolumeNew = PriceHelper.mul(buyGoodsItemVoList.get(i).getFreightVolume(), buyNum);
                } else {
                    buyWeightVolumeNew = PriceHelper.format(new BigDecimal(buyNum));
                }
                if (hashMapFreightIdAndBuyNumList.containsKey(freightId)) {
                    BigDecimal buyWeightVolumeCurrent = hashMapFreightIdAndBuyNumList.get(freightId);
                    hashMapFreightIdAndBuyNumList.put(freightId, PriceHelper.add(buyWeightVolumeCurrent, buyWeightVolumeNew));
                    continue;
                } else {
                    hashMapFreightIdAndBuyNumList.put(freightId, buyWeightVolumeNew);
                }
            }
        }
        HashMap<String,Object> hashMapResult = new HashMap<>();
        hashMapResult.put("hashMapFreightIdAndBuyNumList",hashMapFreightIdAndBuyNumList);
        hashMapResult.put("hashMapFreighTemplate", hashMapFreighTemplate);
        return hashMapResult;
    }

    /**
     * 满X包邮
     * @param buyStoreVoList
     * @return
     */
    private List<BuyStoreVo> calcFreightFree(List<BuyStoreVo> buyStoreVoList) {
        //设置StoreFreightAmount = 0

        return buyStoreVoList;
    }

    /**
     * 下单切换地区时，异步返回json运费信息的vo
     * @param buyStoreVoList
     * @return
     */
    public BuyStoreFreightListVo getFreightListVo(List<BuyStoreVo> buyStoreVoList) {
        BuyStoreFreightListVo buyStoreFreightListVo = new BuyStoreFreightListVo();
        List<BuyStoreFreightVo> buyStoreFreightVoList = new ArrayList<BuyStoreFreightVo>();
        //应付总金额
        BigDecimal buyAmount = new BigDecimal(0);
        for (int i=0; i<buyStoreVoList.size(); i++) {
            BuyStoreFreightVo buyStoreFreightVo = new BuyStoreFreightVo();
            List<BuyGoodsItemFreightVo> buyGoodsItemFreightVoList = new ArrayList<BuyGoodsItemFreightVo>();
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyStoreVoList.get(i).getBuyGoodsItemVoList();
            for (int ii=0; ii<buyGoodsItemVoList.size(); ii++) {
                BuyGoodsItemFreightVo buyGoodsItemFreightVo = new BuyGoodsItemFreightVo();
                buyGoodsItemFreightVo.setAllowSend(buyGoodsItemVoList.get(ii).getAllowSend());
                buyGoodsItemFreightVo.setGoodsAmount(buyGoodsItemVoList.get(ii).getItemAmount());
                buyGoodsItemFreightVo.setGoodsFreightAmount(buyGoodsItemVoList.get(ii).getGoodsFreight());
                buyGoodsItemFreightVo.setGoodsId(buyGoodsItemVoList.get(ii).getGoodsId());
                buyGoodsItemFreightVoList.add(buyGoodsItemFreightVo);
            }
            buyStoreFreightVo.setGoodsList(buyGoodsItemFreightVoList);
            buyStoreFreightVo.setStoreFreightAmount(buyStoreVoList.get(i).getStoreFreightAmount());
            buyStoreFreightVo.setStoreGoodsAmount(buyStoreVoList.get(i).getBuyItemAmount());
            buyStoreFreightVo.setStoreAmount(PriceHelper.add(buyStoreVoList.get(i).getBuyItemAmount(),buyStoreVoList.get(i).getStoreFreightAmount()));
            buyStoreFreightVo.setStoreId(buyStoreVoList.get(i).getStoreId());
            buyStoreFreightVoList.add(buyStoreFreightVo);
            buyAmount = PriceHelper.add(buyAmount,buyStoreVoList.get(i).getBuyAmount());
        }
        buyStoreFreightListVo.setStoreList(buyStoreFreightVoList);
        buyStoreFreightListVo.setBuyAmount(buyAmount);

        return buyStoreFreightListVo;
    }
}
