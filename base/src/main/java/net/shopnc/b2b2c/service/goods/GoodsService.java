package net.shopnc.b2b2c.service.goods;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.BrandShowType;
import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.goods.*;
import net.shopnc.b2b2c.dao.orders.CartDao;
import net.shopnc.b2b2c.dao.store.StoreLabelGoodsDao;
import net.shopnc.b2b2c.domain.goods.*;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.b2b2c.vo.goods.GoodsAttrVo;
import net.shopnc.b2b2c.vo.goods.GoodsCommonVo;
import net.shopnc.b2b2c.vo.goods.GoodsDetailVo;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.b2b2c.vo.goods.SpecJsonVo;
import net.shopnc.b2b2c.vo.goods.SpecValueListVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shopnc.feng on 2015-12-03.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class GoodsService extends BaseService {
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    GoodsImageDao goodsImageDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    GoodsAttributeDao goodsAttributeDao;
    @Autowired
    GoodsCustomDao goodsCustomDao;
    @Autowired
    GoodsCommonDao goodsCommonDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    GoodsSaleDao goodsSaleDao;
    @Autowired
    StoreLabelGoodsDao storeLabelGoodsDao;
    @Autowired
    SendMessageService sendMessageService;
    @Autowired
    FormatTemplateDao formatTemplateDao;
    @Autowired
    GoodsActivityDao goodsActivityDao;
    @Autowired
    CartDao cartDao;

    /**
     * 商品分页列表
     *
     * @param whereList
     * @param params
     * @param page
     * @param pageSize
     * @param sort
     * @return
     */
    private HashMap<String, Object> findGoodsVoGCS(List<String> whereList, HashMap<String, Object> params, int page, int pageSize, String sort) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(goodsDao.findGoodsVoCount("", whereList, params));
        pageEntity.setPageNo(page);
        if (pageSize > 0) {
            pageEntity.setPageSize(pageSize);
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", goodsCommonDao.findGoodsVoGCS(whereList, params, pageEntity.getPageNo(), pageEntity.getPageSize(), sort));
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    
  //根据活动类型获取商品列表
    public List<Object> getGoodsListByActivityType(HashMap<String, Object> param,int pageNo,int pageSize){
    	PageEntity pageEntity = new PageEntity();
    	List<Object> goodsList=new ArrayList<Object>();
    	List<String> whereList=new ArrayList<String>();
//    	 hql.append("select new net.shopnc.b2b2c.vo.goods.GoodsVo(g, gc, gs, s, c, at, a) from Goods g, GoodsCommon gc, GoodsSale gs, Store s, Category c,GoodsActivity at,Activity a " 
//                 + " where g.commonId = gc.commonId and g.goodsId = gs.goodsId and g.commonId = at.commonId and gc.storeId = s.storeId and gc.categoryId = c.categoryId "
//                 + " and a.activityId = at.activityId ");
    	if(param.get("activityId")!=null){
    		whereList.add(" and a.activityId = :activityId");
    		param.put("activityId", param.get("activityId"));
    	}
    	if(param.get("categoryId")!=null){
    		whereList.add(" and c.categoryId = :categoryId");
    		param.put("categoryId", param.get("categoryId"));
    	}
    	if(param.get("attr")!=null){
    		whereList.add(" and g.goodsPrice = :goodsPrice");
    		param.put("goodsPrice", param.get("categoryId"));
    	}

    	goodsList=goodsDao.getGoodsListByActivityType(param,whereList, pageNo, pageSize);
    	return goodsList;
    }
    
//    HashMap<String, Object> params = new HashMap<String, Object>();
//    params.put("page", page);
//    params.put("brandId", brandId);
//    params.put("attr", attr);
//    params.put("sort", sort);
//    params.put("express", express);
//    params.put("own", own);
//    params.put("keyword", keyword);
    
    /**
     * 商品列表
     * @param params
     * @param limit
     * @param sort
     * @return
     */
    public List<Object> getGoodsList(List<String> whereList, HashMap<String, Object> params, int limit, String sort) {
        List<Object> obj= goodsDao.findGoods(null, null, null, null, 0, 0);
        List<Object> goodsList=new ArrayList<Object>();
        if(obj!=null){
        	for(int i=0;i<obj.size();i++){
        		GoodsVo gv=new GoodsVo();
        		gv=(GoodsVo) obj.get(i);
        		 //查询商品库存总数
                int commonId=gv.getCommonId();
                List<Map<String, Object>> saleAndAcount=goodsDao.getProductCountByCommonId(commonId); 
                if(saleAndAcount!=null){
                	Map<String,Object> map=saleAndAcount.get(0);
	                gv.setCommonSaleNum(Integer.valueOf(map.get("0").toString()));
	                gv.setCommonStorage(Integer.valueOf(map.get("1").toString()));
                }
                goodsList.add(gv);
        	}
        }
        return goodsList;
    }

    /**
     * 商品列表（wap用获取活动列表）
     * @param params
     * @param limit
     * @param sort
     * @return
     */
    public List<Object> getGoodsActivityList(List<String> whereList, HashMap<String, Object> params, int limit, String sort) {
        List<Object> obj= goodsDao.findGoodsVoActivity("", whereList, sort, params, 0, 0);
        List<Object> goodsList=new ArrayList<Object>();
        if(obj!=null){
        	for(int i=0;i<obj.size();i++){
        		GoodsVo gv=new GoodsVo();
        		gv=(GoodsVo) obj.get(i);
        		 //查询商品库存总数
                int commonId=gv.getCommonId();
                List<Map<String, Object>> saleAndAcount=goodsDao.getProductCountByCommonId(commonId); 
                if(saleAndAcount!=null){
                	Map<String,Object> map=saleAndAcount.get(0);
	                gv.setCommonSaleNum(Integer.valueOf(map.get("0").toString()));
	                gv.setCommonStorage(Integer.valueOf(map.get("1").toString()));
                }
                goodsList.add(gv);
        	}
        }
        return goodsList;
    }
    
    
    /**
     * 商品分页列表
     *
     * @param whereList
     * @param params
     * @param page
     * @param pageSize
     * @param sort
     * @return
     */
    private HashMap<String, Object> findGoodsVoGC(List<String> whereList, HashMap<String, Object> params, int page, int pageSize, String sort) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(goodsDao.findGoodsVoCount("", whereList, params));
        pageEntity.setPageNo(page);
        if (pageSize > 0) {
            pageEntity.setPageSize(pageSize);
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", goodsCommonDao.findGoodsVoGC(whereList, params, pageEntity.getPageNo(), pageEntity.getPageSize(), sort));
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }

    /**
     * 商品列表
     *
     * @param params
     * @param limit
     * @param sort
     * @return
     */
    private List<Object> findGoodsVoGC(List<String> whereList, HashMap<String, Object> params, int limit, String sort) {
        return goodsCommonDao.findGoodsVoGC(whereList, params, 1, limit, sort);
    }

    /**
     * 商品详情
     *
     * @param goodsId
     * @return
     * @throws ShopException
     */
    public GoodsDetailVo getDetail(int goodsId) throws ShopException {
        // 基础信息
        GoodsDetailVo goodsDetailVo = goodsDao.getDetail(goodsId);
        //查询商品库存总数
        int commonId=goodsDetailVo.getCommonId();
        List<Map<String, Object>> saleAndAcount=goodsDao.getProductCountByCommonId(commonId);
        if(saleAndAcount!=null){
        	Map<String,Object> map=saleAndAcount.get(0);
        	goodsDetailVo.setCommonSaleNum(Integer.valueOf(map.get("0").toString()));
        	goodsDetailVo.setCommonStorage(Integer.valueOf(map.get("1").toString()));
        }
        // 商品图片
        List<GoodsImage> goodsImageList = goodsImageDao.findGoodsDetailImage(goodsDetailVo.getCommonId(), goodsDetailVo.getColorId());
        goodsDetailVo.setGoodsImageList(goodsImageList);
        // 主图
        if (goodsImageList.size() > 0) {
            goodsDetailVo.setImageSrc(goodsImageList.get(0).getImageSrc());
        }

        // 规格默认主图
        List<GoodsImage> imageList = goodsImageDao.findDefaultImageByCommonId(goodsDetailVo.getCommonId());
        HashMap<Integer, GoodsImage> defaultMap = new HashMap<Integer, GoodsImage>();
        if (imageList.size() > 0) {
            for (GoodsImage goodsImage : imageList) {
                defaultMap.put(goodsImage.getColorId(), goodsImage);
            }
        }
	        if (goodsDetailVo.getSpecJson()!=null) {
	            for (SpecJsonVo specJson : goodsDetailVo.getSpecJson()) {
	                for (SpecValueListVo specValueList : specJson.getSpecValueList()) {
	                    if (defaultMap.get(specValueList.getSpecValueId()) != null) {
	                        specValueList.setImageSrc(defaultMap.get(specValueList.getSpecValueId()).getImageSrc());
	                    }
	                }
	            }
	        }	

        // 商品参数
        List<GoodsAttrVo> goodsAttrList = new ArrayList<GoodsAttrVo>();

        // 品牌
        if (goodsDetailVo.getBrandId() > 0) {
            Brand brand = brandDao.get(Brand.class, goodsDetailVo.getBrandId());
            if (brand != null) {
                GoodsAttrVo goodsAttr = new GoodsAttrVo();
                goodsAttr.setName("品牌");
                goodsAttr.setValue(brand.getBrandName() + "/" + brand.getBrandEnglish());
                goodsAttrList.add(goodsAttr);
            }
        }

        // 属性
        List<Object> attrList = goodsAttributeDao.findGoodsAttrVoByCommonId(goodsDetailVo.getCommonId());
        if (attrList != null) {
            for (Object attrObject : attrList) {
                GoodsAttrVo attr = (GoodsAttrVo) attrObject;
                goodsAttrList.add(attr);
            }
        }

        // 自定义属性
        List<Object> customList = goodsCustomDao.findGoodsAttrVoByCommonId(goodsDetailVo.getCommonId());
        if (customList != null) {
            for (Object customObject : customList) {
                GoodsAttrVo custom = (GoodsAttrVo) customObject;
                goodsAttrList.add(custom);
            }
        }

        StringBuilder goodsBody = new StringBuilder();
        if (goodsDetailVo.getFormatTop() > 0) {
            // 关联板式-顶部
            FormatTemplate formatTemplateTop = formatTemplateDao.get(FormatTemplate.class, goodsDetailVo.getFormatTop());
            if (formatTemplateTop != null && formatTemplateTop.getFormatContent().length() > 0) {
                goodsBody.append(formatTemplateTop.getFormatContent());
            }
        }
        goodsBody.append(goodsDetailVo.getGoodsBody());
        if (goodsDetailVo.getFormatBottom() > 0) {
            // 关联板式-底部
            FormatTemplate formatTemplateBottom = formatTemplateDao.get(FormatTemplate.class, goodsDetailVo.getFormatBottom());
            if (formatTemplateBottom != null && formatTemplateBottom.getFormatContent().length() > 0) {
                goodsBody.append(formatTemplateBottom.getFormatContent());
            }
        }

        goodsDetailVo.setGoodsBody(goodsBody.toString());

        goodsDetailVo.setGoodsAttrList(goodsAttrList);
        return goodsDetailVo;
    }

    /**
     * 商品页面包屑
     *
     * @param categoryId 商品的分类编号
     * @param goodsName  商品名称
     * @return
     */
    public List<CrumbsVo> getGoodsCrumbs(int categoryId, String goodsName) {
        List<CrumbsVo> crumbsList = this.getGoodsCategoryCrumbs(categoryId);
        Collections.reverse(crumbsList);
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName(goodsName);
        crumbsList.add(crumbsVo);

        return crumbsList;
    }

    /**
     * 查看更多商品评价时的面包屑
     *
     * @param goodsId
     * @return
     * @throws ShopException
     */
    public List<CrumbsVo> getEvaluateCrumbs(int goodsId) throws ShopException {
        GoodsDetailVo goodsDetail = this.getDetail(goodsId);
        List<CrumbsVo> crumbsList = this.getGoodsCategoryCrumbs(goodsDetail.getCategoryId());
        Collections.reverse(crumbsList);
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setUrl(ShopConfig.getWebRoot() + "goods/" + goodsId);
        crumbsVo.setName(goodsDetail.getGoodsName());
        crumbsList.add(crumbsVo);
        crumbsVo = new CrumbsVo();
        crumbsVo.setName("商品评价");
        crumbsList.add(crumbsVo);
        return crumbsList;
    }

    /**
     * 商品页面分类面包屑
     * List顺序为从末级到等级，可用Collections.reverse(List<>)反向排序
     *
     * @param categoryId
     * @return
     */
    public List<CrumbsVo> getGoodsCategoryCrumbs(int categoryId) {
        List<CrumbsVo> crumbsList = new ArrayList<CrumbsVo>();
        Category category = categoryDao.get(Category.class, categoryId);
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName(category.getCategoryName());
        crumbsVo.setUrl(ShopConfig.getWebRoot() + "search?cat=" + category.getCategoryId());
        crumbsList.add(crumbsVo);
        if (category.getParentId() != 0) {
            crumbsList.addAll(this.getGoodsCategoryCrumbs(category.getParentId()));
        }
        return crumbsList;
    }

    /**
     * 后台商品列表数据
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getGoodsDtGridListForAdmin(String dtGridPager) throws Exception {
        DtGrid dtGrid = goodsCommonDao.getDtGridList(dtGridPager, GoodsCommon.class);
        List<Object> goodsCommonList = dtGrid.getExhibitDatas();
        List<Object> goodsCommonVoList = new ArrayList<Object>();
        List<GoodsActivity> list = goodsActivityDao.findAll(GoodsActivity.class);
        
        for (int i = 0; i < goodsCommonList.size(); i++) {
        	GoodsCommon goodsCommon = (GoodsCommon) goodsCommonList.get(i);
        	GoodsCommonVo obj = (GoodsCommonVo) goodsCommonDao.getGoodsCommonVoByCommonId(goodsCommon.getCommonId());
        	obj.setIsBoundActivity(0);
        	for(GoodsActivity a : list){
        		if(a.getCommonId() == obj.getCommonId()){
        			obj.setIsBoundActivity(1);
        			//.如果商品绑定活动，将商品状态改为0
        			obj.setGoodsState(0);
        			break;
        		}
            }
        	goodsCommonVoList.add(obj);
        }
        dtGrid.setExhibitDatas(goodsCommonVoList);
        return dtGrid;
    }

    /**
     * 根据商品SPU编号删除商品
     *
     * @param commonId
     * @throws ShopException
     */
    public void deleteByCommonId(int commonId) throws ShopException {
        if (commonId <= 0) {
            throw new ShopException("参数错误");
        }

        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        delete(goodsCommon);
    }

    /**
     * 删除商品
     *
     * @param goodsCommon
     * @throws ShopException
     */
    public void delete(GoodsCommon goodsCommon) throws ShopException {
        if (goodsCommon == null) {
            throw new ShopException("参数错误");
        }

        goodsCommonDao.delete(goodsCommon);

        List<Goods> goodsList = goodsDao.findByCommonId(goodsCommon.getCommonId());
        List<Integer> integerList = new ArrayList<Integer>();
        for (Goods goods : goodsList) {
            integerList.add(goods.getGoodsId());
        }
        goodsDao.deleteAll(goodsList);

        goodsImageDao.deleteByCommonId(goodsCommon.getCommonId());

        goodsSaleDao.deleteByInGoodsIds(integerList);

        goodsCustomDao.deleteByCommonId(goodsCommon.getCommonId());

        goodsAttributeDao.deleteByCommonId(goodsCommon.getCommonId());

        storeLabelGoodsDao.deleteByCommonId(goodsCommon.getCommonId());
    }

    /**
     * 商品禁售
     *
     * @param commonId
     * @param stateRemark
     */
    public void ban(int commonId, String stateRemark) throws ShopException {
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        if (goodsCommon == null) {
            throw new ShopException("参数错误");
        }
        if (stateRemark.isEmpty()) {
            throw new ShopException("请填写禁售原因");
        }
        goodsCommon.setGoodsState(GoodsState.BAN);
        goodsCommon.setStateRemark(stateRemark);
        goodsCommonDao.update(goodsCommon);

        // 给商家发送商品禁售消息
        HashMap<String, Object> map = new HashMap<>();
        map.put("commonId", commonId);
        map.put("stateRemark", stateRemark);
        sendMessageService.sendStore("storeGoodsViolation", goodsCommon.getStoreId(), map, Integer.toString(commonId));
    }

    /**
     * 通过审核
     *
     * @param commonId
     */
    public void pass(int commonId) {
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        if (goodsCommon == null) {
            return;
        }
        goodsCommon.setGoodsVerify(GoodsVerify.PASS);
        goodsCommonDao.update(goodsCommon);
    }

    /**
     * 审核失败
     *
     * @param commonId
     * @param verifyRemark
     * @throws ShopException
     */
    public void fail(int commonId, String verifyRemark) throws ShopException {
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        if (goodsCommon == null) {
            throw new ShopException("参数错误");
        }
        if (verifyRemark.isEmpty()) {
            throw new ShopException("请填写审核失败原因");
        }
        goodsCommon.setGoodsVerify(GoodsVerify.FAIL);
        goodsCommon.setVerifyRemark(verifyRemark);
        goodsCommonDao.update(goodsCommon);

        // 给商家发送审核失败消息
        HashMap<String, Object> map = new HashMap<>();
        map.put("commonId", commonId);
        map.put("verifyRemark", verifyRemark);
        sendMessageService.sendStore("storeGoodsVerify", goodsCommon.getStoreId(), map, Integer.toString(commonId));
    }

    /**
     * 店铺热销商品
     *
     * @param storeId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public HashMap<String, Object> findHotGoodsVoByStoreId(int storeId, int pageNo, int pageSize) {
        List<String> where = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        where.add("gc.storeId = :storeId");
        map.put("storeId", storeId);
        return findGoodsVoGCS(where, map, pageNo, pageSize, "gs.goodsSaleNum desc");
    }

    /**
     * 店铺最新上架商品
     *
     * @param storeId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public HashMap<String, Object> findNewGoodsVoByStoreId(int storeId, int pageNo, int pageSize) {
        List<String> where = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        where.add("gc.storeId = :storeId");
        map.put("storeId", storeId);
        return findGoodsVoGC(where, map, pageNo, pageSize, "gc.createTime desc");
    }

    /**
     * 店铺热销商品
     *
     * @param storeId
     * @param limit
     * @return
     */
    public List<Object> findHotGoodsVoByStoreId(int storeId, int limit) {
        List<String> where = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        where.add("gc.storeId = :storeId");
        map.put("storeId", storeId);
        return getGoodsList(where, map, limit, "gs.goodsSaleNum desc");
    }

    /**
     * 店铺收藏商品
     *
     * @param storeId
     * @param limit
     * @return
     */
    public List<Object> findFavoriteGoodsVoByStoreId(int storeId, int limit) {
        List<String> where = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        where.add("gc.storeId = :storeId");
        map.put("storeId", storeId);
        return findGoodsVoGC(where, map, limit, "g.goodsFavorite desc");
    }

    /**
     * 店铺新品商品
     *
     * @param storeId
     * @param limit
     * @return
     */
    public List<Object> findNewGoodsVoByStoreId(int storeId, int limit) {
        List<String> where = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        where.add("gc.storeId = :storeId");
        map.put("storeId", storeId);
        return findGoodsVoGC(where, map, limit, "gc.commonId desc");
    }

    /**
     * 店铺推荐商品
     *
     * @param storeId
     * @param limit
     * @return
     */
    public List<Object> findCommendGoodsVoByStoreId(int storeId, int limit) {
        List<String> where = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        where.add("gc.storeId = :storeId");
        map.put("storeId", storeId);
        where.add("gc.isCommend = :isCommend");
        map.put("isCommend", State.YES);
        List<Object> list = findGoodsVoGC(where, map, 0, "");
        Collections.shuffle(list);
        int toIndex = limit < list.size() ? limit : list.size();
        return list.subList(0, toIndex);
    }

    /**
     * 店铺推荐商品
     *
     * @param storeId
     * @param commonId
     * @param limit
     * @return
     */
    public List<Object> findCommendGoodsVoByStoreIdNeqCommonId(int storeId, int commonId, int limit) {
        List<String> where = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        where.add("gc.storeId = :storeId");
        map.put("storeId", storeId);
        where.add("gc.isCommend = :isCommend");
        map.put("isCommend", State.YES);
        where.add("gc.commonId != :commonId");
        map.put("commonId", commonId);
        List<Object> list = findGoodsVoGC(where, map, 0, "");
        Collections.shuffle(list);
        int toIndex = limit < list.size() ? limit : list.size();
        return list.subList(0, toIndex);
    }

    /**
     * 查询店铺出售中商品总数
     */
    public long getGoodsCommonOnSaleCountByStoreId(int storeId) {
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return goodsCommonDao.findOnSaleCount(where, params);
    }

    /**
     * 查询店铺发布待平台审核商品总数
     */
    public long getGoodsCommonWaitCountByStoreId(int storeId) {
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return goodsCommonDao.findWaitCount(where, params);
    }

    /**
     * 查询店铺审核失败商品总数
     */
    public long getGoodsCommonVerifyFailCountByStoreId(int storeId) {
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return goodsCommonDao.findVerifyFailCount(where, params);
    }

    /**
     * 查询店铺仓库中并且审核通过商品总数
     */
    public long getGoodsCommonOfflineAndVerifyPassCountByStoreId(int storeId) {
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return goodsCommonDao.findOfflineAndPassCount(where, params);
    }

    /**
     * 查询违规禁售商品总数
     */
    public long getGoodsCommonBanCountByStoreId(int storeId) {
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return goodsCommonDao.findBanCount(where, params);
    }

    /**
     * 等待审核的商品数量
     *
     * @return
     */
    public long findWaitCount() {
        return goodsCommonDao.findWaitCount();
    }
}
