package net.shopnc.b2b2c.service;

import net.shopnc.b2b2c.constant.FreightTemplateCalcType;
import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.AreaDao;
import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.goods.GoodsSaleDao;
import net.shopnc.b2b2c.dao.orders.FreightAreaDao;
import net.shopnc.b2b2c.dao.orders.FreightTemplateDao;
import net.shopnc.b2b2c.domain.Area;
import net.shopnc.b2b2c.domain.FreightArea;
import net.shopnc.b2b2c.domain.FreightTemplate;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.goods.GoodsSale;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 运费模板
 * Created by hbj on 2016/1/21.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class FreightTemplateService extends BaseService {

    @Autowired
    private AreaDao areaDao;
    @Autowired
    private FreightTemplateDao freightTemplateDao;
    @Autowired
    private FreightAreaDao freightAreaDao;
    @Autowired
    private GoodsCommonDao goodsCommonDao;
    @Autowired
    private GoodsSaleDao goodsSaleDao;

    /**
     * 取得运费模板需要的地区
     * @return
     */
    public HashMap<String,Object> getAreaList() {
        HashMap<Integer,String> hashMapName = new HashMap<Integer, String>();
        HashMap<Integer,List<Integer>> hashMapChildren = new HashMap<Integer, List<Integer>>();
        HashMap<String,List<Integer>> hashMapregion = new HashMap<String, List<Integer>>();

        List<Area> areaList = areaDao.getAreaListByDeep2();
        List<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<areaList.size(); i++) {

            hashMapName.put(areaList.get(i).getAreaId(), areaList.get(i).getAreaName());

            if (hashMapChildren.containsKey(areaList.get(i).getAreaParentId())) {
                hashMapChildren.get(areaList.get(i).getAreaParentId()).add(areaList.get(i).getAreaId());
            } else {
                list = new ArrayList<Integer>();
                list.add(areaList.get(i).getAreaId());
                hashMapChildren.put(areaList.get(i).getAreaParentId(), list);
            }

            if (areaList.get(i).getAreaParentId() == 0 && (areaList.get(i).getAreaRegion() != null && !areaList.get(i).getAreaRegion().equals(""))) {
                if (hashMapregion.containsKey(areaList.get(i).getAreaRegion())) {
                    hashMapregion.get(areaList.get(i).getAreaRegion()).add(areaList.get(i).getAreaId());
                } else {
                    list = new ArrayList<Integer>();
                    list.add(areaList.get(i).getAreaId());
                    hashMapregion.put(areaList.get(i).getAreaRegion(), list);
                }
            }
        }
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("nameList",hashMapName);
        hashMap.put("childrenList",hashMapChildren);
        hashMap.put("regionList", hashMapregion);
        return hashMap;
    }

    /**
     * 保存运费模板
     * @param storeId
     * @param calcType
     * @param title
     * @param freightId
     * @param item1
     * @param price1
     * @param item2
     * @param price2
     * @param areaList
     * @throws ShopException
     */
    public void saveFreight(int storeId,
                            String calcType,
                            String title,
                            Integer freightId,
                            Integer freightFree,
                            List<BigDecimal> item1,
                            List<BigDecimal> price1,
                            List<BigDecimal> item2,
                            List<BigDecimal> price2,
                            List<String> areaList) throws ShopException {
        if (areaList.size() == 0 || item1.size()==0) {
            throw new ShopException("地区数据提交错误");
        }
        if (item1.size() != item2.size()) {
            throw new ShopException("运费数据提交错误");
        }
        if (!Arrays.asList(FreightTemplateCalcType.WEIGHT,FreightTemplateCalcType.VOLUME,FreightTemplateCalcType.NUMBER).contains(calcType)) {
            throw new ShopException("计价方式提交错误");
        }

        if (freightId != null && freightId.equals(0)) {
            throw new ShopException("运费模板未找到");
        }

        if (title.equals("")) {
            throw new ShopException("未填写运费模板名称");
        }

        if (freightFree != State.NO) {
            freightFree = State.YES;
        }

        FreightTemplate freightTemplate;
        if (freightId != null) {
            freightTemplate = freightTemplateDao.getFreightTemplateInfo(freightId,storeId);
            if (freightTemplate == null) {
                throw new ShopException("运费模板不存在");
            }
        } else {
            freightTemplate = new FreightTemplate();
        }

        //保存运费模板主信息
        freightTemplate.setEditTime(ShopHelper.getCurrentTimestamp());
        freightTemplate.setTitle(title);
        freightTemplate.setStoreId(storeId);
        freightTemplate.setFreightFree(freightFree);
        if (freightId == null) {
            freightTemplate.setCalcType(calcType);
            freightId = (Integer)freightTemplateDao.save(freightTemplate);
        } else {
            freightTemplate.setFreightId(freightId);
            freightTemplateDao.update(freightTemplate);
            freightAreaDao.delFreightArea(freightId);
        }

        List<FreightArea> freightAreaList = new ArrayList<FreightArea>();
        //保存详细地区及价格信息
        for(int i=0; i<areaList.size(); i++) {
            FreightArea freightArea = new FreightArea();
            String[] areaArray = areaList.get(i).split("\\|\\|\\|");
            if (areaArray.length != 2) {
                throw new ShopException("地区数据错误");
            }
            if (!Pattern.compile("^[\\d_]+$").matcher(areaArray[0]).matches()) {
                throw new ShopException("地区数据错误");
            }
            freightArea.setFreightId(freightId);
            freightArea.setAreaId("_" + areaArray[0] + "_");
            freightArea.setAreaName(areaArray[1]);
            if (freightFree == State.NO) {
                freightArea.setItem1(item1.get(i));
                freightArea.setItem2(item2.get(i));
                freightArea.setPrice1(price1.get(i));
                freightArea.setPrice2(price2.get(i));
            } else {
                freightArea.setItem1(new BigDecimal(1));
                freightArea.setItem2(new BigDecimal(1));
                freightArea.setPrice1(BigDecimal.ZERO);
                freightArea.setPrice2(BigDecimal.ZERO);
            }
            freightAreaList.add(freightArea);
        }
        logger.info("待保存的freightAreaList");
        logger.info(freightAreaList);
        freightAreaDao.saveAll(freightAreaList);
    }

    /**
     * 复制运费模板
     * @param freightId
     * @param storeId
     * @throws ShopException
     */
    public void cloneFreight(Integer freightId,int storeId) throws ShopException{
        if (freightId == null) {
            throw new ShopException("参数错误");
        }
        FreightTemplate freightTemplate = freightTemplateDao.getFreightTemplateInfo(freightId, storeId);
        if (freightTemplate == null) {
            throw new ShopException("运费模板不存在");
        }
        FreightTemplate freightTemplateNew = new FreightTemplate();
        freightTemplateNew.setTitle(freightTemplate.getTitle() + "的副本");
        freightTemplateNew.setFreightFree(freightTemplate.getFreightFree());
        freightTemplateNew.setStoreId(freightTemplate.getStoreId());
        freightTemplateNew.setCalcType(freightTemplate.getCalcType());
        freightTemplateNew.setEditTime(freightTemplate.getEditTime());
        Integer newFreightIdNew = (Integer)freightTemplateDao.save(freightTemplateNew);

        List<FreightArea> freightAreaList = freightAreaDao.getFreightAreaByFreightId(freightId);
        List<FreightArea> freightAreaListNew = new ArrayList<FreightArea>();

        for (int i=0; i<freightAreaList.size(); i++) {
            FreightArea freightArea = new FreightArea();
            freightArea.setFreightId(newFreightIdNew);
            freightArea.setAreaId(freightAreaList.get(i).getAreaId());
            freightArea.setItem2(freightAreaList.get(i).getItem2());
            freightArea.setItem1(freightAreaList.get(i).getItem1());
            freightArea.setPrice2(freightAreaList.get(i).getPrice2());
            freightArea.setPrice1(freightAreaList.get(i).getPrice1());
            freightArea.setAreaName(freightAreaList.get(i).getAreaName());
            freightAreaListNew.add(freightArea);
        }
        freightAreaDao.saveAll(freightAreaListNew);
    }

    /**
     * 删除运费模板
     * @param freightId
     * @param storeId
     * @throws ShopException
     */
    public void delFreight(Integer freightId,int storeId) throws ShopException{
        if (freightId == null) {
            throw new ShopException("参数错误");
        }
        FreightTemplate freightTemplate = freightTemplateDao.getFreightTemplateInfo(freightId, storeId);
        if (freightTemplate == null) {
            throw new ShopException("运费模板不存在");
        }
        freightTemplateDao.delete(FreightTemplate.class,freightTemplate.getFreightId());
        freightAreaDao.delFreightArea(freightId);
    }

    /**
     * 根据运费模板列表取得运费模板详细地区列表
     * @param freightTemplateList 运费模板列表
     * @return
     */
    public HashMap<Integer,List<FreightArea>> getFreightAreaListGroupByFreightId(List<FreightTemplate> freightTemplateList) {
        if (freightTemplateList.size() == 0) {
            return null;
        }
        //根据freightIdList取得freightAreaList
        List<Integer> freightIdList = new ArrayList<Integer>();
        for (int i=0; i<freightTemplateList.size(); i++) {
            freightIdList.add(freightTemplateList.get(i).getFreightId());
        }
        List<FreightArea> freightAreaList = freightAreaDao.getFreightAreaByFreightIdList(freightIdList);

        //将地区列表按freightId分组
        HashMap<Integer,List<FreightArea>> hashMapFreightAreaList = new HashMap<Integer, List<FreightArea>>();
        for (int i=0; i<freightAreaList.size(); i++) {
            FreightArea freightArea = freightAreaList.get(i);
            if (hashMapFreightAreaList.containsKey(freightArea.getFreightId())) {
                hashMapFreightAreaList.get(freightArea.getFreightId()).add(freightArea);
            } else {
                List<FreightArea> freightAreaList1 = new ArrayList<FreightArea>();
                freightAreaList1.add(freightArea);
                hashMapFreightAreaList.put(freightArea.getFreightId(),freightAreaList1);
            }
        }
        return hashMapFreightAreaList;
    }

    /**
     * 计算商品运费及配送,商品详细页地区切换使用
     * @param goodsId 判断库存
     * @param commonId 计算运费
     * @param buyNum
     * @param areaId2
     * @throws Exception
     * @return allowSend 1支持/0不支持 配送,freightAmount运费，不支持配送时freightAmount=null
     */
    public HashMap<String,Object> calcFreight(int goodsId, int commonId, int buyNum, int areaId2) throws Exception {
        BigDecimal freightAmount = null;
        int allowSend;
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        if (goodsCommon == null) {
            throw new ShopException("商品spu不存在");
        }
        int goodsStatus = (goodsCommon.getGoodsState() == GoodsState.ONLINE && goodsCommon.getGoodsVerify() == GoodsVerify.PASS) ? State.YES : State.NO;
        if (goodsStatus == State.NO) {
            throw new ShopException("商品已经下架");
        }

        GoodsSale goodsSale = goodsSaleDao.get(GoodsSale.class,goodsId);
        if (goodsSale == null) {
            throw new ShopException("商品sku不存在");
        }
        if (goodsSale.getGoodsStorage() < buyNum) {
            throw new ShopException("商品库存不足");
        }

        if (goodsCommon.getFreightTemplateId() > 0) {
            //使用运费模板
            FreightTemplate freightTemplate = freightTemplateDao.get(FreightTemplate.class,goodsCommon.getFreightTemplateId());
            if (freightTemplate == null) {
                freightAmount = PriceHelper.format(new BigDecimal(0));
                allowSend = State.YES;
            } else {
                BigDecimal buyWeightVolume;
                String calcType = freightTemplate.getCalcType();
                if (calcType.equals(FreightTemplateCalcType.WEIGHT)) {
                    logger.info("商品重量:" + goodsCommon.getFreightWeight());
                    buyWeightVolume = PriceHelper.mul(goodsCommon.getFreightWeight(),buyNum);
                } else if (calcType.equals(FreightTemplateCalcType.VOLUME)) {
                    logger.info("商品体积:" + goodsCommon.getFreightVolume());
                    buyWeightVolume = PriceHelper.mul(goodsCommon.getFreightVolume(),buyNum);
                } else {
                    buyWeightVolume = PriceHelper.format(new BigDecimal(buyNum));
                }
                List<FreightArea> freightAreaList = freightAreaDao.getFreightAreaByFreightId(goodsCommon.getFreightTemplateId());
                if (freightAreaList.size() > 0) {
                    logger.info("开始运费计算");
                    logger.info("buyWeightVolume:"+buyWeightVolume);
                    logger.info("areaId2:"+areaId2);
//                    logger.info(freightTemplate);
//                    logger.info(freightAreaList);
                    BigDecimal freightTemplateAmount = calcFreight(buyWeightVolume,areaId2,freightTemplate,freightAreaList);
                    logger.info("结束计算，计算结果:");
                    logger.info("freightTemplateAmount:" +freightTemplateAmount);
                    if (freightTemplateAmount == null) {
                        allowSend = State.NO;
                    } else {
                        freightAmount = freightTemplateAmount;
                        allowSend = State.YES;
                    }
                } else {
                    freightAmount = PriceHelper.format(new BigDecimal(0));
                    allowSend = State.YES;
                }
            }
        } else {
            freightAmount = goodsCommon.getGoodsFreight();
            allowSend = State.YES;
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("freightAmount",freightAmount);
        hashMap.put("allowSend",allowSend);
        return hashMap;
    }

    /**
     * 计算单个运费模板运费及配送信息
     * @param buyNum 购买总量 = 购买数量 * 单个商品的件数/重量/体积
     * @param areaId2 二级地区Id
     * @param freightTemplate 运费模板信息
     * @param freightAreaList 运费模板下的地区信息
     * @return >0 有运费，支持配送 =0 包邮，支持配送 =null 不支持配送
     */
    public BigDecimal calcFreight(BigDecimal buyNum, int areaId2, FreightTemplate freightTemplate, List<FreightArea> freightAreaList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        logger.info("计算运费模板运费中");
        BigDecimal freightAmount = null;
        String areaId = "_" + areaId2 + "_";
        for (int i=0; i<freightAreaList.size(); i++) {
            if (freightAreaList.get(i).getAreaId().indexOf(areaId) != -1) {
                logger.info("匹配到areaId，支持配送");
                if (freightTemplate.getFreightFree() == State.YES) {
                    //包邮
                    freightAmount = PriceHelper.format(new BigDecimal(0));
                    break;
                }
                if (PriceHelper.isLessThanOrEquals(buyNum,freightAreaList.get(i).getItem1())) {
                    //在首件/重/体积范围内
                    freightAmount = freightAreaList.get(i).getPrice1();
                    logger.info("首费=运费:" + freightAmount);
                } else {
                    //超出首件/重/体积数量范围，需要计算续件/重/体积
                    //先计算续费 = 续数量 * 续单价 = (向上取整（（购买总量 - item1）/item2）)* price2
                    BigDecimal amount2;
                    if (PriceHelper.isGreaterThan(freightAreaList.get(i).getItem2(),BigDecimal.ZERO)) {
                        //0不能作除数
                        Integer buyNum2 = PriceHelper.sub(buyNum, freightAreaList.get(i).getItem1()).divide(freightAreaList.get(i).getItem2(),2).setScale(0, BigDecimal.ROUND_CEILING).intValue();
                        logger.info("续数量:" + buyNum2);
                        logger.info("续单价:" + freightAreaList.get(i).getPrice2());
                        //续费
                        amount2 = PriceHelper.mul(freightAreaList.get(i).getPrice2(), buyNum2);
                        logger.info("续费 = 续数量 * 续单价=" + amount2);
                    } else {
                        amount2 = BigDecimal.ZERO;
                    }

                    //首费
                    BigDecimal amount1 = freightAreaList.get(i).getPrice1();
                    logger.info("首费:" + amount1);
                    //运费 = 首费 +续费
                    freightAmount = PriceHelper.add(amount1,amount2);
                }
            }
        }
        return freightAmount;
    }
}
