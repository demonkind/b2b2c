package net.shopnc.b2b2c.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.dao.goods.CategoryDao;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.goods.GoodsImageDao;
import net.shopnc.b2b2c.dao.member.EvaluateGoodsDao;
import net.shopnc.b2b2c.domain.goods.AttributeValue;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.domain.goods.GoodsImage;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.goods.AttributeService;
import net.shopnc.b2b2c.vo.AttributeAndValueVo;
import net.shopnc.b2b2c.vo.CategoryNavVo;
import net.shopnc.b2b2c.vo.SearchCheckedFilterVo;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.util.SearchEngineHelper;
import net.shopnc.common.util.UtilsHelper;


/**
 * Created by shopnc.feng on 2016-01-04.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SearchService extends BaseService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsImageDao goodsImageDao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private EvaluateGoodsDao evaluateGoodsDao;
    @Autowired
    private SearchEngineHelper searchEngineHelper;

    private int PAGE_SIZE = 50;

    /**
     * 商品筛选项<br>
     * 如存在下级分类显示下级分类，否则显示品牌、属性
     *
     * @param params
     * @return
     */
    private HashMap<String, Object> getFilterByCategoryId(Map<String, Object> params) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<SearchCheckedFilterVo> searchCheckedFilterList = new ArrayList<SearchCheckedFilterVo>();

        Integer categoryId = (Integer) params.get("categoryId");
        if (categoryId != 0) {
            // 面包屑导航
            List<CategoryNavVo> categoryCrumbsList = getCrumbsCategoryList(categoryId);
            map.put("categoryCrumbsList", categoryCrumbsList);
        } else {

        }

        Integer brandId = (Integer) params.get("brandId");
        if (brandId > 0) {
            // 已选择品牌，添加商品筛选已选项VO
            Brand brand = brandDao.get(Brand.class, brandId);
            SearchCheckedFilterVo searchCheckedFilterVo = new SearchCheckedFilterVo();
            searchCheckedFilterVo.setName("品牌");
            searchCheckedFilterVo.setValue(brand.getBrandName());
            searchCheckedFilterVo.setParam(String.valueOf(brand.getBrandId()));
            searchCheckedFilterList.add(searchCheckedFilterVo);
        }
        List<Category> categoryList = categoryDao.findByParentId(categoryId);
        if (categoryList.size() == 0) {
            List<Brand> brandList = brandDao.findByCategoryId(categoryId, "asc");
            if (brandId == 0) {
                // 未选择品牌，显示品牌筛选
                map.put("brandList", brandList);
            }

            List<AttributeAndValueVo> attributeList = attributeService.getAttributeAndValueVoByCategoryId(categoryId);

            String attrList = (String) params.get("attr");
            List<AttributeAndValueVo> removeAttributeAndValueList = new ArrayList<AttributeAndValueVo>();
            for (AttributeAndValueVo attributeAndValue : attributeList) {
                for (AttributeValue attributeValue : attributeAndValue.getAttributeValueList()) {
                    String str = String.valueOf(attributeAndValue.getAttributeId()) + '-' + String.valueOf(attributeValue.getAttributeValueId());
                    // 验证属性是否已经选择
                    if (attrList.contains(str)) {
                        SearchCheckedFilterVo searchCheckedFilterVo = new SearchCheckedFilterVo();
                        searchCheckedFilterVo.setName(attributeAndValue.getAttributeName());
                        searchCheckedFilterVo.setValue(attributeValue.getAttributeValueName());
                        searchCheckedFilterVo.setParam(str);
                        searchCheckedFilterList.add(searchCheckedFilterVo);
                        // 添加需要移除的属性
                        removeAttributeAndValueList.add(attributeAndValue);
                    }
                }
            }
            // 移除已选择的属性列
            attributeList.removeAll(removeAttributeAndValueList);
            map.put("attributeList", attributeList);
        } else {
            map.put("categoryList", categoryList);
        }
        // 排序
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("order", "goods");
        hashMap.put("sort", "desc");
        String sort = (String) params.get("sort");
        if (!sort.isEmpty() && !sort.equals("")) {
            String[] strings = sort.split("_");
            if (strings[0].equals("sale")) {
                hashMap.put("order", "sale");
            } else if (strings[0].equals("comment")) {
                hashMap.put("order", "comment");
            } else if (strings[0].equals("price")) {
                hashMap.put("order", "price");
            } else {
                hashMap.put("order", "goods");
            }
            hashMap.put("sort", strings[1].equals("asc") ? "asc" : "desc");
        }
        map.put("sort", hashMap);
        // 自营
        Integer own = (Integer) params.get("own");
        map.put("own", own);
        // 关键字
        String keyword = (String) params.get("keyword");
        if (!keyword.isEmpty()) {
            map.put("keyword", keyword);
        }
        // 包邮
        Integer express = (Integer) params.get("express");
        map.put("express", express);

        map.put("searchCheckedFilterList", searchCheckedFilterList);
        return map;
    }

    /**
     * 分类面包屑导航
     *
     * @param categoryId
     * @return
     */
    private List<CategoryNavVo> getCrumbsCategoryList(int categoryId) {
        Category category = categoryDao.get(Category.class, categoryId);
        CategoryNavVo categoryNavVo = new CategoryNavVo();
        categoryNavVo.setCategoryId(category.getCategoryId());
        categoryNavVo.setCategoryName(category.getCategoryName());
        categoryNavVo.setParentId(category.getParentId());
        categoryNavVo.setCategorySort(category.getCategorySort());
        categoryNavVo.setDeep(category.getDeep());
        categoryNavVo.setCategoryList(categoryDao.findCategoryNavVoByParentId(category.getParentId()));
        List<CategoryNavVo> categoryList = new ArrayList<CategoryNavVo>();
        categoryList.add(categoryNavVo);
        if (categoryNavVo.getParentId() > 0) {
            List<CategoryNavVo> categoryNavVoList = this.getCrumbsCategoryList(categoryNavVo.getParentId());
            categoryNavVoList.add(categoryNavVo);
            categoryList = categoryNavVoList;
        }
        return categoryList;
    }

    /**
     * 搜索
     *
     * @param params
     * @return
     */
    public Map<String, Object> search(Map<String, Object> params) {
        //如果开启搜索引擎使用搜索引擎方案，否则使用SQL方案
        if (ShopConfig.getSearchOpen()) {
            return searchByEngine(params);
        } else {
            return searchBySql(params);
        }
    }

    /**
     * 使用搜索引擎搜索
     *
     * @param params
     * @return
     */
    private HashMap<String, Object> searchByEngine(Map<String, Object> params) {
        // 关键字
        String keyword = (String) params.get("keyword");

        // 查询条件
        String condition = getSearchByEngineCondition(params);

        // 排序
        String sort = "";
        if (!params.get("sort").equals("")) {
            String sorts = (String) params.get("sort");
            String[] strings = sorts.split("_");
            sort = strings[1].equals("asc") ? "asc" : "desc";
            if (strings[0].equals("sale")) {
                sort = "goodsSaleNum " + sort;
            } else if (strings[0].equals("comment")) {
                sort = "evaluateNum " + sort;
            } else if (strings[0].equals("price")) {
                sort = "goodsPrice " + sort;
            }
        }

        Map<String, Object> resultMap = null;
        try {
            resultMap = searchEngineHelper.goodsQuery(keyword, condition.toString(), sort, (Integer) params.get("page"), PAGE_SIZE);
        } catch (Exception e) {
            //搜索引擎查询出现异常，返回空搜索结果
            resultMap = new HashMap<>();
            resultMap.put("goodsVoList", new ArrayList<GoodsVo>());
            resultMap.put("total", 0);
            resultMap.put("categoryList", new ArrayList<Category>());
            logger.error(e.toString());
        }

        // 商品列表
        List<GoodsVo> goodsVoList = new ArrayList<GoodsVo>();
        for (GoodsVo goodsVo : (List<GoodsVo>) (resultMap.get("goodsVoList"))) {
            List<GoodsImage> goodsImageList = goodsImageDao.findDefaultImageByCommonId(goodsVo.getCommonId());
            if (goodsImageList.size() > 0) {
                goodsVo.setImageSrc(goodsImageList.get(0).getImageSrc());
            }
            if (goodsImageList.size() > 1) {
                goodsVo.setGoodsImageList(goodsImageList);
            }
            goodsVoList.add(goodsVo);
        }

        // 分页
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageNo((Integer) params.get("page"));
        pageEntity.setPageSize(PAGE_SIZE);
        pageEntity.setTotal((Long) (resultMap.get("total")));

        HashMap<String, Object> filter = getFilterByCategoryId(params);
        // 如根据关键字查询商品，显示商品结果中的分类
        List<Category> categoryList = (List<Category>) (resultMap.get("categoryList"));
        if ((Integer) params.get("categoryId") == 0 && categoryList.size() > 0) {
            filter.put("categoryList", categoryList);
        }

        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("list", goodsVoList);
        map1.put("showPage", pageEntity.getPageHtml());
        map1.put("filter", filter);
        return map1;
    }

    /**
     * 返回搜索引擎查询条件
     * @param params
     * @return
     */
    private String getSearchByEngineCondition(Map<String, Object> params) {
        StringBuilder condition = new StringBuilder();
        condition.append("");
        int conditionCount = 0;

        // 分类
        Integer categoryId = (Integer) params.get("categoryId");
        if (categoryId > 0) {
            if (conditionCount > 0) {
                condition.append(" AND ");
            }
            conditionCount++;
            condition.append("categoryIds:");
            condition.append(categoryId);
        }

        // 品牌
        Integer brandId = (Integer) params.get("brandId");
        if (brandId > 0) {
            if (conditionCount > 0) {
                condition.append(" AND ");
            }
            conditionCount++;
            condition.append("brandId:");
            condition.append(brandId);
        }

        // 属性
        String attrString = (String) params.get("attr");
        if (!attrString.equals("")) {
            List<String> attrList = Arrays.asList(attrString.split(","));
            for (int i = 0; i < attrList.size(); i++) {
                String attr = attrList.get(i);
                String[] attrs = attr.split("-");
                if (attrs.length != 2) continue;

                if (conditionCount > 0) {
                    condition.append(" AND ");
                }
                conditionCount++;
                condition.append("attr:");
                condition.append(attrs[1]);
            }
        }

        // 包邮
        Integer express = (Integer) params.get("express");
        if (express > 0) {
            if (conditionCount > 0) {
                condition.append(" AND ");
            }
            conditionCount++;
            condition.append("freightTemplateId:0 AND goodsFreight:0.0");
        }

        // 自营
        Integer isOwnShop = (Integer) params.get("own");
        if (isOwnShop > 0) {
            if (conditionCount > 0) {
                condition.append(" AND ");
            }
            conditionCount++;
            condition.append("isOwnShop:");
            condition.append(isOwnShop);
        }
        return condition.toString();
    }

    /**
     * 使用SQL搜索
     *
     * @param params
     * @return
     */
    private HashMap<String, Object> searchBySql(Map<String, Object> params) {
        List<String> whereList = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        String table = "";
        // 关键字
        String keyword = (String) params.get("keyword");
        if (!keyword.isEmpty()) {
            whereList.add("(gc.goodsName like :keyword or g.goodsSerial like :keyword or g.goodsSpecs like :keyword)");
            map.put("keyword", "%" + keyword + "%");
        }
        // 分类
        Integer categoryId = (Integer) params.get("categoryId");
        if (categoryId > 0) {
            whereList.add("gc.categoryId = :categoryId");
            map.put("categoryId", categoryId);
        }
        // 品牌
        Integer brandId = (Integer) params.get("brandId");
        if (brandId > 0) {
            whereList.add("gc.brandId = :brandId");
            map.put("brandId", brandId);
        }
        // 属性
        String attrString = (String) params.get("attr");
        if (!attrString.equals("")) {
            List<String> attrList = Arrays.asList(attrString.split(","));
            for (int i = 0; i < attrList.size(); i++) {
                String attr = attrList.get(i);
                String[] attrs = attr.split("-");
                if (attrs.length != 2) continue;
                table += ",GoodsAttribute ga" + String.valueOf(i);

                whereList.add("g.commonId = ga" + String.valueOf(i) + ".commonId");
                whereList.add("ga" + String.valueOf(i) + ".attributeValueId = :attributeValueId" + String.valueOf(i));
                map.put("attributeValueId" + String.valueOf(i), Integer.parseInt(attrs[1]));
            }
        }
        // 自营
        Integer isOwnShop = (Integer) params.get("own");
        if (isOwnShop > 0) {
            whereList.add("s.isOwnShop = :isOwnShop");
            map.put("isOwnShop", isOwnShop);
        }
        
        String activityId = UtilsHelper.getString(params.get("activityId"));
        if(!UtilsHelper.isEmpty(activityId)){
        	whereList.add("(at.activityId = :activityId or at.activityId = 3) and at.StartTime is not null");
            map.put("activityId", activityId);
        }
        
        // 排序
        String sort = "g.goodsId desc";
        if (!params.get("sort").equals("")) {
            String sorts = (String) params.get("sort");
            String[] strings = sorts.split("_");
            sort = strings[1].equals("asc") ? "asc" : "desc";
            if (strings[0].equals("sale")) {
                sort = "gs.goodsSaleNum " + sort;
            } else if (strings[0].equals("comment")) {
                sort = "g.evaluateNum " + sort;
            } else if (strings[0].equals("price")) {
                sort = "g.goodsPrice " + sort;
            }
        }

        // 分页
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageNo((Integer) params.get("page"));
        pageEntity.setPageSize(PAGE_SIZE);
        long totalCnt = 0;
        if(!UtilsHelper.isEmpty(activityId)){
        	totalCnt = goodsDao.findGoodsVoCountActivity(table, whereList, map);
        } else {
        	totalCnt = goodsDao.findGoodsVoCount(table, whereList, map);
        }
        pageEntity.setTotal(totalCnt);

        // 商品列表
        List<Object> list = new ArrayList<>();
        if(!UtilsHelper.isEmpty(activityId)){
        	list = goodsDao.findGoodsVoActivity(table, whereList, sort, map, pageEntity.getPageNo(), pageEntity.getPageSize());
        }else{
        	list = goodsDao.findGoodsVo(table, whereList, sort, map, pageEntity.getPageNo(), pageEntity.getPageSize());
        }
        
        List<GoodsVo> goodsVoList = new ArrayList<GoodsVo>();
        List<Category> categoryList = new ArrayList<Category>();
        for (Object goodsList : list) {
            GoodsVo goodsVo = (GoodsVo) goodsList;
            List<GoodsImage> goodsImageList = goodsImageDao.findDefaultImageByCommonId(goodsVo.getCommonId());
            if (goodsImageList.size() > 0) {
                goodsVo.setImageSrc(goodsImageList.get(0).getImageSrc());
            }
            if (goodsImageList.size() > 1) {
                goodsVo.setGoodsImageList(goodsImageList);
            }

            goodsVoList.add(goodsVo);

            Category category = new Category();
            category.setCategoryId(goodsVo.getCategoryId());
            category.setCategoryName(goodsVo.getCategoryName());
            // 去重复
            if (!categoryList.toString().contains(category.toString())) {
                categoryList.add(category);
            }
        }


        HashMap<String, Object> filter = getFilterByCategoryId(params);
        // 如根据关键字查询商品，显示商品结果中的分类
        if (categoryId == 0 && categoryList.size() > 0) {
            filter.put("categoryList", categoryList);
        }

        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("list", goodsVoList);
        map1.put("showPage", pageEntity.getPageHtml());
        map1.put("filter", filter);
        return map1;
    }

    public HashMap<String, Object> getSku(int goodsId) throws ShopException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        GoodsVo goodsVo = (GoodsVo) goodsDao.getGoodsVoByGoodsId(goodsId);
        map.put("goodsName", goodsVo.getGoodsName());
        map.put("goodsPrice", goodsVo.getGoodsPrice());
        map.put("goodsStorage", goodsVo.getGoodsStorage());
        map.put("imageSrc", goodsVo.getImageSrc());

        return map;
    }
}
