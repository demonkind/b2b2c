package net.shopnc.b2b2c.service.goods;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;

import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.SellerGoodsListSignal;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.dao.goods.CategoryDao;
import net.shopnc.b2b2c.dao.goods.CustomDao;
import net.shopnc.b2b2c.dao.goods.FormatTemplateDao;
import net.shopnc.b2b2c.dao.goods.GoodsAttributeDao;
import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.dao.goods.GoodsCustomDao;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.goods.GoodsImageDao;
import net.shopnc.b2b2c.dao.goods.GoodsSaleDao;
import net.shopnc.b2b2c.dao.orders.FreightTemplateDao;
import net.shopnc.b2b2c.dao.store.StoreLabelGoodsDao;
import net.shopnc.b2b2c.domain.FreightTemplate;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.domain.goods.Custom;
import net.shopnc.b2b2c.domain.goods.FormatTemplate;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.goods.GoodsAttribute;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.goods.GoodsCustom;
import net.shopnc.b2b2c.domain.goods.GoodsImage;
import net.shopnc.b2b2c.domain.goods.GoodsSale;
import net.shopnc.b2b2c.domain.store.StoreLabel;
import net.shopnc.b2b2c.domain.store.StoreLabelGoods;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.store.StoreLabelService;
import net.shopnc.b2b2c.vo.AttributeAndValueVo;
import net.shopnc.b2b2c.vo.goods.GoodsCommonVo;
import net.shopnc.b2b2c.vo.goods.GoodsCustomVo;
import net.shopnc.b2b2c.vo.goods.GoodsJsonVo;
import net.shopnc.b2b2c.vo.goods.GoodsPicVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.util.JsonHelper;
import net.shopnc.common.util.ShopHelper;

/**
 * Created by shopnc.feng on 2015-11-26.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class GoodsSellerService extends BaseService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    AttributeService attributeService;
    @Autowired
    CustomDao customDao;
    @Autowired
    SiteService siteService;
    @Autowired
    GoodsCommonDao goodsCommonDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    GoodsSaleDao goodsSaleDao;
    @Autowired
    GoodsImageDao goodsImageDao;
    @Autowired
    StoreLabelService storeLabelService;
    @Autowired
    GoodsAttributeDao goodsAttributeDao;
    @Autowired
    StoreLabelGoodsDao storeLabelGoodsDao;
    @Autowired
    GoodsCustomDao goodsCustomDao;
    @Autowired
    FormatTemplateDao formatTemplateDao;
    @Autowired
    CategoryService categoryService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    FreightTemplateDao freightTemplateDao;

    @Autowired
    GoodsActivityService goodsActivityService;
    /**
     * 根据末级查询全部父级分类<br />
     * List顺序为从末级到等级，可用Collections.reverse(List<>)反向排序
     * @param categoryId
     * @return
     */
    private LinkedList<Integer> getParentIdAllByCategoryId(int categoryId) {
        Category category = categoryDao.get(Category.class, categoryId);
        LinkedList<Integer> linkedList = new LinkedList<Integer>();
        linkedList.addFirst(categoryId);
        if (category.getParentId() != 0) {
            linkedList.addAll(getParentIdAllByCategoryId(category.getParentId()));
        }
        return linkedList;
    }

    /**
     * 查询绑定的分类
     * @param parentId
     * @return
     */
    public List<Object> getBindCategory(int parentId, int storeId, int isOwnShop) {
        if (isOwnShop == State.YES) {
            return categoryDao.findObjectByParentId(parentId);
        } else {
            return categoryDao.findStoreBindCategoryByParentId(storeId, parentId);
        }
    }

    /**
     * 验证可发布商品数量
     * @param storeId
     * @param goodsLimit
     * @param isOwnShop
     * @throws ShopException
     */
    public void checkGoodsCount(int storeId, int goodsLimit, int isOwnShop) throws ShopException {
        if (isOwnShop == State.YES) return;
        long count = goodsCommonDao.findCountByStoreId(storeId);
        if (count >= goodsLimit) {
            throw new ShopException("商品数量已经达到最大值，不能继续发布商品");
        }
    }

    /**
     * 商品发布第二步相关数据
     * @param categoryId
     * @return
     */
    public HashMap<String, Object> getAddGoodsParam(int categoryId, int storeId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        // 分类编号
        map.put("categoryId", categoryId);
        // 分类面包屑
        map.put("categoryNameCrumbs", categoryService.getCategoryNameCrumbsByEndCategoryId(categoryId));
        // 品牌
        List<Brand> brandList = brandDao.findByCategoryId(categoryId, "asc");
        map.put("brandList", brandList);
        // 属性
        List<AttributeAndValueVo> attributeList = attributeService.getAttributeAndValueVoByCategoryId(categoryId);
        map.put("attributeList", attributeList);
        // 自定义属性
        List<Custom> customList = customDao.findByCategoryId(categoryId);
        map.put("customList", customList);
        // 店内分类
        List<StoreLabel> storeLabelList = storeLabelService.findByStoreId(storeId);
        map.put("storeLabelList", storeLabelList);
        // 顶部关联版式
        List<FormatTemplate> formatTopList = formatTemplateDao.findTopByStoreId(storeId);
        map.put("formatTopList", formatTopList);
        // 底部关联版式
        List<FormatTemplate> formatBottomList = formatTemplateDao.findBottomByStoreId(storeId);
        map.put("formatBottomList", formatBottomList);
        // 运费模板
        List<FreightTemplate> freightTemplateList = freightTemplateDao.getFreightTemplateListByStoreId(storeId);
        map.put("freightTemplateList",freightTemplateList);

        return map;
    }

    /**
     * 已选择属性、自定义属性、店内分类
     * @param commonId
     * @return
     */
    public HashMap<String, Object> getCheckParam(int commonId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        // 已选属性
        List<GoodsAttribute> goodsAttributeList = goodsAttributeDao.findByCommonId(commonId);
        map.put("goodsAttributeList", goodsAttributeList);
        // 已选自定义属性
        List<GoodsCustom> goodsCustomList = goodsCustomDao.findByCommonId(commonId);
        map.put("goodsCustomList", goodsCustomList);
        // 已选店内分类
        List<StoreLabelGoods> storeLabelGoodsList = storeLabelGoodsDao.findByCommonId(commonId);
        map.put("storeLabelGoodsList", storeLabelGoodsList);
        return map;
    }

    /**
     * 保存商品
     * @param goodsCommon
     * @param goodsJson
     * @param goodsPic
     * @param attributeValueId
     * @param storeLabelId
     * @param custom
     * @param storeId
     * @param goodsLimit
     * @param isOwnShop
     * @throws ShopException
     */
    public HashMap<String, Object> save(GoodsCommon goodsCommon,
                     String goodsJson,
                     String goodsPic,
                     int[] attributeValueId,
                     int[] storeLabelId,
                     String custom,
                     int storeId,
                     int goodsLimit,
                     int isOwnShop) throws ShopException{
        // 验证商品数量限制
        checkGoodsCount(storeId, goodsLimit, isOwnShop);

        int commonId = saveGoodsCommon(goodsCommon, storeId);
        List<Integer> goodsIdList = saveGoodsAll(goodsJson, commonId, storeId);
        if (goodsIdList.size() == 0) {
            throw new ShopException("参数错误");
        }

        saveImage(goodsPic, commonId);
        saveGoodsAttribute(attributeValueId, commonId);
        saveStoreLabelGoods(storeLabelId, commonId);
        saveGoodsCustom(custom, commonId);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        map.put("goodsId", goodsIdList.get(0));
        return map;
    }

    /**
     * 保存SPU
     * @param goodsCommon
     * @return
     */
    private Integer saveGoodsCommon(GoodsCommon goodsCommon, int storeId) {
        goodsCommon.setCreateTime(ShopHelper.getCurrentTimestamp());
        goodsCommon.setUpdateTime(ShopHelper.getCurrentTimestamp());
        int goodsVerify = (siteService.getSiteInfo().get(SiteTitle.GOODSVERIFY).equals("1") ? 0 : 1);
        goodsCommon.setGoodsVerify(goodsVerify);
        goodsCommon.setStoreId(storeId);

        LinkedList<Integer> list = getParentIdAllByCategoryId(goodsCommon.getCategoryId());
        Collections.reverse(list);
        if (list.size() >= 1 && list.get(0) != null) goodsCommon.setCategoryId1(list.get(0));
        if (list.size() >= 2 && list.get(1) != null) goodsCommon.setCategoryId2(list.get(1));
        if (list.size() >= 3 && list.get(2) != null) goodsCommon.setCategoryId3(list.get(2));

        return  (Integer) goodsCommonDao.save(goodsCommon);
    }

    /**
     * 保存SKU
     * @param commonId
     * @param goodsJson
     */
    private List<Integer> saveGoodsAll(String goodsJson, int commonId, int storeId) {
        List<GoodsJsonVo> goodsJsonVoList = JsonHelper.toGenericObject(goodsJson, new TypeReference<List<GoodsJsonVo>>() {
        });
        List<HashMap<String, Object>> goodsSpecValueList = new ArrayList<HashMap<String, Object>>();
        List<Integer> goodsIdList = new ArrayList<Integer>();
        for (GoodsJsonVo json : goodsJsonVoList) {
            // 保存商品
            int goodsId = this.saveGoods(json, commonId, storeId);
            goodsIdList.add(goodsId);

            HashMap<String, Object> goodsSpecValue = new HashMap<String, Object>();
            goodsSpecValue.put("goodsId", goodsId);
            goodsSpecValue.put("specValueIds", json.getSpecValueIds());
            goodsSpecValueList.add(goodsSpecValue);
        }
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        String param = JsonHelper.toJson(goodsSpecValueList);
        goodsCommon.setGoodsSpecValueJson(param);
        goodsCommonDao.update(goodsCommon);

        return goodsIdList;
    }

    /**
     * 保存单条商品记录
     * @param goodsJson
     * @param commonId
     * @return
     */
    private int saveGoods(GoodsJsonVo goodsJson, int commonId, int storeId) {
        Goods goods = new Goods();
        GoodsSale goodsSale = new GoodsSale();
        // sku
        goods.setCommonId(commonId);
        goods.setGoodsSpecs(goodsJson.getGoodsSpecs());
        goods.setGoodsFullSpecs(goodsJson.getGoodsFullSpecs());
        goods.setSpecValueIds(goodsJson.getSpecValueIds());
        goods.setGoodsPrice(goodsJson.getGoodsPrice());
        goods.setMarkerPrice(goodsJson.getMarkerPrice());
        goods.setGoodsSerial(goodsJson.getGoodsSerial());
        goods.setGoodsBarcode(goodsJson.getGoodsBarcode());
        goods.setColorId(goodsJson.getColorId());
        int goodsId = (Integer) goodsDao.save(goods);

        // 销售
        goodsSale.setGoodsId(goodsId);
        goodsSale.setGoodsStorage(goodsJson.getGoodsStorage());
        goodsSale.setGoodsStorageAlarm(goodsJson.getGoodsStorageAlarm());
        goodsSaleDao.save(goodsSale);
        // 生成二维码
        ShopHelper.createGoodsQRCode(storeId, goodsId);
        return goodsId;
    }


    /**
     * 保存图片
     * @param commonId
     * @param goodsPicString
     */
    private void saveImage(String goodsPicString, int commonId) {
        List<GoodsPicVo> goodsPicList = JsonHelper.toGenericObject(goodsPicString, new TypeReference<List<GoodsPicVo>>() {});
        List<GoodsImage> goodsImageList = new ArrayList<GoodsImage>();
        for (GoodsPicVo goodsPic : goodsPicList) {
            GoodsImage goodsImage = new GoodsImage();
            goodsImage.setColorId(goodsPic.getColorId());
            goodsImage.setCommonId(commonId);
            goodsImage.setImageName(goodsPic.getImageName());
            goodsImage.setImageSort(goodsPic.getImageSort());
            goodsImage.setIsDefault(goodsPic.getIsDefault());
            goodsImageList.add(goodsImage);

            // 更新商品主图
            if(goodsPic.getIsDefault() == State.YES) {
                goodsDao.updateGoodsImage(commonId, goodsPic.getColorId(), goodsPic.getImageName());
            }
        }
        goodsImageDao.saveAll(goodsImageList);
    }

    /**
     * 保存属性
     * @param commonId
     * @param attributeValueIds
     */
    private void saveGoodsAttribute(int[] attributeValueIds, int commonId) {
        if (attributeValueIds == null) {
            return;
        }
        List<GoodsAttribute> goodsAttributeList = new ArrayList<GoodsAttribute>();
        for (int attributeValueId : attributeValueIds) {
            if (attributeValueId == 0) continue;
            GoodsAttribute goodsAttribute = new GoodsAttribute();
            goodsAttribute.setCommonId(commonId);
            goodsAttribute.setAttributeValueId(attributeValueId);
            goodsAttributeList.add(goodsAttribute);
        }
        goodsAttributeDao.saveAll(goodsAttributeList);
    }

    /**
     * 保存店内商品分类
     * @param commonId
     * @param storeLabelIds
     */
    private void saveStoreLabelGoods(int[] storeLabelIds, int commonId) {
        if (storeLabelIds == null) {
            return;
        }
        List<StoreLabelGoods> storeLabelGoodsList = new ArrayList<StoreLabelGoods>();
        for (int storeLabelId : storeLabelIds) {
            StoreLabelGoods storeLabelGoods = new StoreLabelGoods();
            storeLabelGoods.setCommonId(commonId);
            storeLabelGoods.setStoreLabelId(storeLabelId);
            storeLabelGoodsList.add(storeLabelGoods);
        }
        storeLabelGoodsDao.saveAll(storeLabelGoodsList);
    }

    /**
     * 保存商品与自定义属性关系
     * @param custom
     * @param commonId
     */
    private void saveGoodsCustom(String custom, int commonId) {
        if (custom == null) {
            return;
        }
        List<GoodsCustomVo> goodsCustomVoList = JsonHelper.toGenericObject(custom, new TypeReference<List<GoodsCustomVo>>() {});
        List<GoodsCustom> goodsCustomList = new ArrayList<GoodsCustom>();
        for (GoodsCustomVo goodsCustomVo : goodsCustomVoList) {
            GoodsCustom goodsCustom = new GoodsCustom();
            goodsCustom.setCommonId(commonId);
            goodsCustom.setCustomId(goodsCustomVo.getCustomId());
            goodsCustom.setCustomValue(goodsCustomVo.getCustomValue());
            goodsCustomList.add(goodsCustom);
        }
        goodsCustomDao.saveAll(goodsCustomList);
    }

    /**
     * 获取SPU下已选择规格，和已添加图片
     * @param commonId
     * @return
     * @throws ShopException
     */
    public HashMap<String, Object> findGoodsEditParam(int commonId) throws ShopException{
        HashMap<String, Object> map = new HashMap<String, Object>();
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        map.put("specJson", goodsCommon.getSpecJson());
        // 已选规格
        List<Object> goodsJsonList = goodsDao.findGoodsJsonVoByCommonId(commonId);
        if (goodsJsonList == null) {
            throw new ShopException("参数错误");
        }
        map.put("goodsJson", goodsJsonList);
        // 已选图片
        List<Object> goodsPicList = goodsImageDao.findGoodsPicVoByCommonId(commonId);
        if (goodsPicList == null) {
            throw new ShopException("参数错误");
        }
        map.put("goodsPic", goodsPicList);
        return map;
    }

    /**
     * 更新商品数据
     * @param goodsCommon
     * @param goodsJson
     * @param goodsPic
     * @param attributeValueId
     * @param storeLabelId
     * @param custom
     * @param storeId
     * @throws ShopException
     */
    public void update(GoodsCommon goodsCommon,
                       String goodsJson,
                       String goodsPic,
                       int[] attributeValueId,
                       int[] storeLabelId,
                       String custom,
                       int storeId) throws ShopException{
        this.updateGoodsCommon(goodsCommon, storeId);
        this.updateGoodsAll(goodsJson, goodsCommon.getCommonId(), storeId);
        this.updateImage(goodsPic, goodsCommon.getCommonId());
        this.updateGoodsAttribute(attributeValueId, goodsCommon.getCommonId());
        this.updateStoreLabelGoods(storeLabelId, goodsCommon.getCommonId());
        this.updateGoodsCustom(custom, goodsCommon.getCommonId());
    }

    /**
     * 更新商品SPU
     * @param goodsCommonNew
     * @param storeId
     * @throws ShopException
     */
    private void updateGoodsCommon(GoodsCommon goodsCommonNew, int storeId) throws ShopException{
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, goodsCommonNew.getCommonId());
        if (goodsCommon == null || goodsCommon.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        goodsCommon.setCategoryId(goodsCommonNew.getCategoryId());
        goodsCommon.setGoodsName(goodsCommonNew.getGoodsName());
        goodsCommon.setJingle(goodsCommonNew.getJingle());
        goodsCommon.setBrandId(goodsCommonNew.getBrandId());
        goodsCommon.setGoodsBody(goodsCommonNew.getGoodsBody());
        goodsCommon.setSpecJson(goodsCommonNew.getSpecJson());
        goodsCommon.setAreaId1(goodsCommonNew.getAreaId1());
        goodsCommon.setAreaId2(goodsCommonNew.getAreaId2());
        goodsCommon.setAreaInfo(goodsCommonNew.getAreaInfo());
        goodsCommon.setAllowVat(goodsCommonNew.getAllowVat());
        goodsCommon.setGoodsFreight(goodsCommonNew.getGoodsFreight());
        goodsCommon.setFreightTemplateId(goodsCommonNew.getFreightTemplateId());
        goodsCommon.setGoodsState(goodsCommonNew.getGoodsState());
        int goodsVerify = (siteService.getSiteInfo().get(SiteTitle.GOODSVERIFY).equals("1") ? 0 : 1);
        goodsCommon.setGoodsVerify(goodsVerify);
        goodsCommon.setUpdateTime(ShopHelper.getCurrentTimestamp());
        goodsCommon.setFormatTop(goodsCommonNew.getFormatTop());
        goodsCommon.setFormatBottom(goodsCommonNew.getFormatBottom());
        goodsCommon.setIsCommend(goodsCommonNew.getIsCommend());
        goodsCommon.setFreightWeight(goodsCommonNew.getFreightWeight());
        goodsCommon.setFreightVolume(goodsCommonNew.getFreightVolume());
        LinkedList<Integer> list = getParentIdAllByCategoryId(goodsCommon.getCategoryId());
        Collections.reverse(list);
        if (list.size() >= 1 && list.get(0) != null) goodsCommon.setCategoryId1(list.get(0));
        if (list.size() >= 2 && list.get(1) != null) goodsCommon.setCategoryId2(list.get(1));
        if (list.size() >= 3 && list.get(2) != null) goodsCommon.setCategoryId3(list.get(2));
        goodsCommonDao.update(goodsCommon);
    }

    /**
     * 更新商品SKU
     * @param goodsJson
     * @param commonId
     * @param storeId
     * @throws ShopException
     */
    private void updateGoodsAll(String goodsJson, int commonId, int storeId) throws ShopException {
        List<GoodsJsonVo> goodsJsonVoList = JsonHelper.toGenericObject(goodsJson, new TypeReference<List<GoodsJsonVo>>() {});
        List<HashMap<String, Object>> goodsSpecValueList = new ArrayList<HashMap<String, Object>>();
        List<Integer> integerList = new ArrayList<Integer>();
        for (GoodsJsonVo json : goodsJsonVoList) {
            int goodsId;
            if (json.getGoodsId() > 0) {
                goodsId = this.updateGoods(json, commonId);
            } else {
                goodsId = this.saveGoods(json, commonId, storeId);
            }

            HashMap<String, Object> goodsSpecValue = new HashMap<String, Object>();
            goodsSpecValue.put("goodsId", goodsId);
            goodsSpecValue.put("specValueIds", json.getSpecValueIds());
            goodsSpecValueList.add(goodsSpecValue);

            integerList.add(goodsId);
        }
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        String param = JsonHelper.toJson(goodsSpecValueList);
        goodsCommon.setGoodsSpecValueJson(param);
        goodsCommonDao.update(goodsCommon);

        deleteGoodsByNotInGoodsId(integerList, commonId);
    }

    /**
     * 清理SKU<br>
     *     根据commonId和goodsId把其他SKU删除
     * @param goodsIds
     * @param commonId
     */
    private void deleteGoodsByNotInGoodsId(List<Integer> goodsIds, int commonId) {
        if (goodsIds.size() == 0) {
            return;
        }
        List<Integer> integerList = goodsDao.deleteByNotInGoodsIds(goodsIds, commonId);
        if (integerList.size() == 0) {
            return;
        }
        goodsSaleDao.deleteByInGoodsIds(integerList);
    }

    /**
     * 更新商品SKU
     * @param goodsJson
     * @param commonId
     * @return
     * @throws ShopException
     */
    private int updateGoods(GoodsJsonVo goodsJson, int commonId) throws ShopException {
        Goods goods = goodsDao.get(Goods.class, goodsJson.getGoodsId());
        if (goods == null || goods.getCommonId() != commonId) {
            throw new ShopException("参数错误");
        }
        // sku
        goods.setGoodsSpecs(goodsJson.getGoodsSpecs());
        goods.setGoodsFullSpecs(goodsJson.getGoodsFullSpecs());
        goods.setSpecValueIds(goodsJson.getSpecValueIds());
        goods.setGoodsPrice(goodsJson.getGoodsPrice());
        goods.setMarkerPrice(goodsJson.getMarkerPrice());
        goods.setGoodsSerial(goodsJson.getGoodsSerial());
        goods.setGoodsBarcode(goodsJson.getGoodsBarcode());
        goods.setColorId(goodsJson.getColorId());
        goodsDao.update(goods);

        GoodsSale goodsSale = goodsSaleDao.get(GoodsSale.class, goodsJson.getGoodsId());
        // 销售
        goodsSale.setGoodsStorage(goodsJson.getGoodsStorage());
        goodsSale.setGoodsStorageAlarm(goodsJson.getGoodsStorageAlarm());
        goodsSaleDao.update(goodsSale);

        return goods.getGoodsId();
    }

    /**
     * 更新图片
     * @param goodsPicString
     * @param commonId
     */
    private void updateImage(String goodsPicString, int commonId) {
        goodsImageDao.deleteByCommonId(commonId);
        this.saveImage(goodsPicString, commonId);
    }

    /**
     * 更新属性
     * @param attributeValueIds
     * @param commonId
     */
    private void updateGoodsAttribute(int[] attributeValueIds, int commonId) {
        goodsAttributeDao.deleteByCommonId(commonId);
        this.saveGoodsAttribute(attributeValueIds, commonId);
    }

    /**
     * 更新店内商品分类
     * @param storeLabelIds
     * @param commonId
     */
    private void updateStoreLabelGoods(int[] storeLabelIds, int commonId) {
        storeLabelGoodsDao.deleteByCommonId(commonId);
        this.saveStoreLabelGoods(storeLabelIds, commonId);
    }

    /**
     * 更新自定义属性
     * @param custom
     * @param commonId
     */
    private void updateGoodsCustom(String custom, int commonId) {
        goodsCustomDao.deleteByCommonId(commonId);
        saveGoodsCustom(custom, commonId);
    }

    /**
     * 商家中心商品列表
     * @param storeId
     * @param paramMap
     * @param signal
     * @return
     */
    public HashMap<String, Object> getList(int storeId, HashMap<String, Object> paramMap, SellerGoodsListSignal signal) {
        // 筛选条件
        HashMap<String, Object> map = listParams(paramMap);
        List<String> whereList = (List<String>) map.get("whereList");
        HashMap<String, Object> whereMap = (HashMap<String, Object>) map.get("whereMap");

        // 必要条件
        whereList.add("gc.storeId = :storeId");
        whereMap.put("storeId", storeId);
        switch (signal) {
            case ONLINE:
                // 出售中的商品
                whereList.add("gc.goodsState = :goodsState");
                whereMap.put("goodsState", GoodsState.ONLINE);
                whereList.add("gc.goodsVerify = :goodsVerify");
                whereMap.put("goodsVerify", GoodsVerify.PASS);
                break;
            case OFFLINE:
                // 仓库中的商品
                whereList.add("gc.goodsState = :goodsState");
                whereMap.put("goodsState", GoodsState.OFFLINE);
                whereList.add("gc.goodsVerify = :goodsVerify");
                whereMap.put("goodsVerify", GoodsVerify.PASS);
                break;
            case BAN:
                // 禁售的商品
                whereList.add("gc.goodsState = :goodsState");
                whereMap.put("goodsState", GoodsState.BAN);
                whereList.add("gc.goodsVerify = :goodsVerify");
                whereMap.put("goodsVerify", GoodsVerify.PASS);
                break;
            case WAIT:
                // 等待审核的商品
                whereList.add("gc.goodsVerify = :goodsVerify");
                whereMap.put("goodsVerify", GoodsVerify.WAIT);
                break;
            case FAIL:
                // 审核失败
                whereList.add("gc.goodsVerify = :goodsVerify");
                whereMap.put("goodsVerify", GoodsVerify.FAIL);
                break;
        }

        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(goodsCommonDao.findCount(whereList, whereMap));
        pageEntity.setPageNo((Integer) paramMap.get("page"));
        List<Object> goodsCommonList = goodsCommonDao.findGoodsCommonVoList(whereList, whereMap, pageEntity.getPageNo(), pageEntity.getPageSize());
        List<Object> goodsList=new ArrayList<Object>();
        //查看商品是否有绑定过活动
    	for(int i=0;i<goodsCommonList.size();i++){
			GoodsCommonVo gv=(GoodsCommonVo) goodsCommonList.get(i);
    		int goodsId=gv.getCommonId();
    		GoodsActivity goodsActivity=(GoodsActivity) goodsActivityService.checkBound(goodsId);
    		if(goodsActivity!=null){
    			gv.setGoodsActivityId(goodsActivity.getGoodsActivityId());
    			Timestamp time = new Timestamp(System.currentTimeMillis());
    			if(goodsActivity.getStartTime()!=null){
	    			if(time.getTime()>goodsActivity.getStartTime().getTime()){
	    				//活动已开始
	    				gv.setIsBoundActivity(3);
	    			}else{
	    				//活动未开始
	    				gv.setIsBoundActivity(1);
	    			}
    			}else{
    				gv.setIsBoundActivity(1);
    			}
    			gv.setActivityType(String.valueOf(goodsActivity.getActivityId()));
    		}else{
    			gv.setIsBoundActivity(0);
    			gv.setActivityType("0");
    		}
    		goodsList.add(gv);		
        }
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("goodsCommonList", goodsList);
        returnMap.put("showPage", pageEntity.getPageHtml());
        return returnMap;
    }

    /**
     * 拼写参数<br/>
     * 商家中心商品列表使用
     * @param paramMap
     * @return
     */
    private HashMap<String, Object> listParams(HashMap<String, Object> paramMap) {
        List<String> whereList = new ArrayList<String>();
        HashMap<String, Object> whereMap = new HashMap<String, Object>();
        int type = (Integer) paramMap.get("type");
        String keyword = (String) paramMap.get("keyword");
        if (!keyword.isEmpty()) {
            switch (type) {
                case 0:
                    break;
                case 1:        // 商品名称
                    whereList.add("(gc.goodsName like :keyword or g.goodsSpecs like :keyword)");
                    whereMap.put("keyword", "%" + paramMap.get("keyword") + "%");
                    break;
                case 2:         // 商家编号
                    whereList.add("g.goodsSerial like :keyword");
                    whereMap.put("keyword", "%" + paramMap.get("keyword") + "%");
                    break;
                case 3:         // SPU
                    whereList.add("gc.commonId = :keyword");

                    if (Pattern.matches("^\\d*$", keyword)) {
                        whereMap.put("keyword", Integer.parseInt(keyword));
                    } else {
                        whereMap.put("keyword", 0);
                    }
                    break;
            }
        }
        HashMap<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("whereList", whereList);
        returnMap.put("whereMap", whereMap);
        return returnMap;
    }

    /**
     * 根据commonId查询SKU
     * @param commonId
     * @param storeId
     * @return
     * @throws ShopException
     */
    public List<Object> findSkuByCommonId(int commonId, int storeId) throws ShopException {
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        if (goodsCommon == null || goodsCommon.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        return goodsDao.findGoodsSkuVoByCommonId(commonId);
    }

    /**
     * 编辑商品独立SKU所需数据
     * @param goodsId
     * @param storeId
     * @return
     * @throws ShopException
     */
    public HashMap<String, Object> getEditSkuParam(int goodsId, int storeId) throws ShopException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Goods goods = goodsDao.get(Goods.class, goodsId);
        map.put("goods", goods);
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, goods.getCommonId());
        if (goodsCommon == null || goodsCommon.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        GoodsSale goodsSale = goodsSaleDao.get(GoodsSale.class, goodsId);
        map.put("goodsSale", goodsSale);
        return map;
    }

    /**
     * 编辑独立SKU保存
     * @param goodsNew
     * @param goodsStorage
     * @param goodsStorageAlarm
     * @param storeId
     * @throws ShopException
     */
    public void saveSku(Goods goodsNew,
                        int goodsStorage,
                        int goodsStorageAlarm,
                        int storeId) throws ShopException {
        Goods goods = goodsDao.get(Goods.class, goodsNew.getGoodsId());
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, goods.getCommonId());
        if (goodsCommon == null || goodsCommon.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        goods.setGoodsPrice(goodsNew.getGoodsPrice());
        goods.setMarkerPrice(goodsNew.getMarkerPrice());
        goods.setGoodsSerial(goodsNew.getGoodsSerial());
        goods.setGoodsBarcode(goodsNew.getGoodsBarcode());
        goods.setGoodsBody(goodsNew.getGoodsBody());
        goods.setMobileBody(goodsNew.getMobileBody());
        goodsDao.update(goods);

        GoodsSale goodsSale = goodsSaleDao.get(GoodsSale.class, goodsNew.getGoodsId());
        goodsSale.setGoodsStorage(goodsStorage);
        goodsSale.setGoodsStorageAlarm(goodsStorageAlarm);
        goodsSaleDao.update(goodsSale);
    }

    /**
     * 删除商品
     * @param commonId
     * @param storeId
     * @throws ShopException
     */
    public void delete(int commonId, int storeId) throws ShopException {
        if (commonId <= 0) {
            throw new ShopException("参数错误");
        }

        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        if (goodsCommon == null || goodsCommon.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }

        goodsService.delete(goodsCommon);
    }

    /**
     * 商品下架
     * @param commonIds
     * @param storeId
     */
    public void offline(Integer[] commonIds, int storeId) {
        List<Integer> commonIdList = Arrays.asList(commonIds);
        List<GoodsCommon> goodsCommonList = goodsCommonDao.findByCommonIdList(commonIdList);
        if (goodsCommonList == null) return;
        // 需要更新的GoodsCommon
        List<GoodsCommon> goodsCommonUpdateList = new ArrayList<GoodsCommon>();
        for (GoodsCommon goodsCommon : goodsCommonList) {
            if (goodsCommon.getStoreId() != storeId) continue;
            goodsCommon.setGoodsState(GoodsState.OFFLINE);
            goodsCommonUpdateList.add(goodsCommon);
        }
        goodsCommonDao.updateAll(goodsCommonUpdateList);
    }

    /**
     * 商品上架
     * @param commonIds
     * @param storeId
     */
    public void online(Integer[] commonIds, int storeId) {
        List<Integer> commonIdList = Arrays.asList(commonIds);
        List<GoodsCommon> goodsCommonList = goodsCommonDao.findByCommonIdList(commonIdList);
        if (goodsCommonList == null) return;
        //  需要更新的GoodsCommon
        List<GoodsCommon> goodsCommonUpdateList = new ArrayList<GoodsCommon>();
        for (GoodsCommon goodsCommon : goodsCommonList) {
            if (goodsCommon.getStoreId() != storeId) continue;
            goodsCommon.setGoodsState(GoodsState.ONLINE);
            goodsCommonUpdateList.add(goodsCommon);
        }
        goodsCommonDao.updateAll(goodsCommonUpdateList);
    }

    /**
     * 推荐商品
     * @param commonIds
     * @param storeId
     */
    public void commend(Integer[] commonIds, int storeId, int commendLimit) throws ShopException {
        List<Integer> commonIdList = Arrays.asList(commonIds);
        List<GoodsCommon> goodsCommonList = goodsCommonDao.findByCommonIdList(commonIdList);
        if (goodsCommonList == null) return;

        // 查询已推荐商品数量
        long commendCount = goodsCommonDao.findCommendCountByStoreId(storeId);
        if (commendCount >= commendLimit) {
            throw new ShopException("推荐数量已经达到最大值，不能推荐商品");
        }
        // 需要更新的GoodsCommon
        List<GoodsCommon> goodsCommonUpdateList = new ArrayList<GoodsCommon>();
        for (GoodsCommon goodsCommon : goodsCommonList) {
            if (commendCount >= commendLimit) {
                // 抛出错误之前更新数量未超出部分的推荐
                goodsCommonDao.updateAll(goodsCommonUpdateList);
                throw new ShopException("推荐数量已经达到最大值，超出部分自定终止推荐");
            }
            if (goodsCommon.getStoreId() != storeId) continue;
            goodsCommon.setIsCommend(State.YES);
            goodsCommonUpdateList.add(goodsCommon);
            commendCount += 1;
        }
        if (goodsCommonUpdateList.size() == 0) return;
        goodsCommonDao.updateAll(goodsCommonUpdateList);
    }

    /**
     * 取消推荐商品
     * @param commonIds
     * @param storeId
     */
    public void cancelCommend(Integer[] commonIds, int storeId) {
        List<Integer> commonIdList = Arrays.asList(commonIds);
        List<GoodsCommon> goodsCommonList = goodsCommonDao.findByCommonIdList(commonIdList);
        if (goodsCommonList == null) return;
        // 需要更新的GoodsCommon
        List<GoodsCommon> goodsCommonUpdateList = new ArrayList<GoodsCommon>();
        for (GoodsCommon goodsCommon : goodsCommonList) {
            if (goodsCommon.getStoreId() != storeId) continue;
            goodsCommon.setIsCommend(State.NO);
            goodsCommonUpdateList.add(goodsCommon);
        }
        if (goodsCommonUpdateList.size() == 0) return;
        goodsCommonDao.updateAll(goodsCommonUpdateList);
    }

    /**
     * 编辑关联板式
     * @param commonIds
     * @param formatTop
     * @param formatBottom
     * @param storeId
     */
    public void editFormat(Integer[] commonIds,
                           int formatTop,
                           int formatBottom,
                           int storeId) {
        List<Integer> commonIdList = Arrays.asList(commonIds);
        List<GoodsCommon> goodsCommonList = goodsCommonDao.findByCommonIdList(commonIdList);
        if (goodsCommonList == null) return;
        // 需要更新的GoodsCommon
        List<GoodsCommon> goodsCommonUpdateList = new ArrayList<>();
        for (GoodsCommon goodsCommon : goodsCommonList) {
            if (goodsCommon.getStoreId() != storeId) continue;
            goodsCommon.setFormatTop(formatTop);
            goodsCommon.setFormatBottom(formatBottom);
            goodsCommonUpdateList.add(goodsCommon);
        }
        if (goodsCommonUpdateList.size() == 0) return;
        goodsCommonDao.updateAll(goodsCommonUpdateList);
    }
}
