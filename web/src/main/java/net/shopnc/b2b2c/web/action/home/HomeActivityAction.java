package net.shopnc.b2b2c.web.action.home;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;

import net.shopnc.b2b2c.constant.BrandShowType;
import net.shopnc.b2b2c.dao.orders.OrdersGoodsDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.domain.store.StoreLabel;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.SearchService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.service.store.StoreLabelService;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.b2b2c.vo.contract.ContractVo;
import net.shopnc.b2b2c.vo.goods.GoodsDetailVo;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.b2b2c.vo.member.EvaluateGoodsVo;
import net.shopnc.b2b2c.vo.member.EvaluateStoreVo;
import net.shopnc.b2b2c.vo.store.ServiceVo;
import net.shopnc.common.util.JsonHelper;


@Controller
@RequestMapping("activity")
public class HomeActivityAction extends HomeBaseAction {
	@Autowired
    SearchService searchService;
	@Autowired
    GoodsService goodsService;
    @Autowired
    EvaluateService evaluateService;
    @Autowired
    StoreDao storeDao;
    @Autowired
    OrdersGoodsDao ordersGoodsDao;
    @Autowired
    StoreLabelService storeLabelService;
    @Autowired
    ContractService contractService;
    @Autowired
    GoodsActivityService goodsActivityService;
    
    /**
     * 斐讯活动页面
     * @param modelMap
     * @return 
     * @return
     */
    @RequestMapping("/activityList")
    public String index(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "cat", required = false, defaultValue = "0") Integer categoryId,
            @RequestParam(name = "brand", required = false, defaultValue = "0") Integer brandId,
            @RequestParam(name = "attr", required = false, defaultValue = "") String attr,
            @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
            @RequestParam(name = "express", required = false, defaultValue = "0") Integer express,
            @RequestParam(name = "own", required = false, defaultValue = "0") Integer own,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value="activityId" ,required = false, defaultValue = "") String activityId,
            @RequestParam(value="pageNo" , defaultValue="1") int pageNo,
            @RequestParam(value="pageSize" , defaultValue="30") int pageSize,
            ModelMap modelMap,
            HttpServletRequest request) {
    	modelMap.put("paramMap", request.getParameterMap());
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("categoryId", categoryId);
        params.put("brandId", brandId);
        params.put("attr", attr);
        params.put("sort", sort);
        params.put("express", express);
        params.put("own", own);
        params.put("keyword", keyword);
        params.put("activityId", activityId);
        modelMap.put("brandShowType", new BrandShowType().get());
        List<GoodsVo> goodsList = new ArrayList<>();
        List<GoodsVo> topHotGoodsList = new ArrayList<>();
        try {
            Map<String, Object> map = searchService.search(params);
            modelMap.addAllAttributes(map);
            // 搜索页顶部热销商品，暂时从查询结果中随机取4条
            goodsList = (List<GoodsVo>) map.get("list");
            int toIndex = 4 < goodsList.size() ? 4 : goodsList.size();
            if (toIndex > 0) {
            	topHotGoodsList = goodsList.subList(0, toIndex);
            }
            List<Object> list = new ArrayList<Object>();
	      	  for(Object obj : goodsList){
	      		  GoodsVo goodsVo = (GoodsVo) obj;
	      		  if(goodsVo.getStartTime() != null){
	      			  list.add(obj);
	      		  }
	      	  }
      	  modelMap.put("goodsList", goodsList);
        } catch (Exception e) {
            logger.error(e.toString());
        }     	
        modelMap.put("topHotGoodsList", topHotGoodsList);
        modelMap.put("goodsList", goodsList);
        return getHomeTemplate("activity/search");
    }
   

    /**
     * 斐讯活动商品详细信息
     *
     * @param goodsId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/activityInfo", method = RequestMethod.GET)
    public String info(@RequestParam(value = "goodsId") int goodsId, ModelMap modelMap) throws ShopException {
            // 商品详细信息
            GoodsDetailVo goodsDetail = goodsService.getDetail(goodsId);
            modelMap.put("goodsDetail", goodsDetail);
            
            // 商品活动详细信息
            GoodsActivity goodsActivity = (goodsActivityService.findGoodsActivityById(goodsDetail.getCommonId())).get(0);
            modelMap.put("goodsActivity", goodsActivity);

            // 店铺信息
            Store storeInfo = storeDao.get(Store.class, goodsDetail.getStoreId());
            modelMap.put("storeInfo", storeInfo);

            // 面包屑
            List<CrumbsVo> crumbsVoList = goodsService.getGoodsCrumbs(goodsDetail.getCategoryId(), goodsDetail.getGoodsName());
            modelMap.put("crumbsList", crumbsVoList);

            EvaluateGoodsVo evaluateGoodsVo = evaluateService.queryGoodsEvaluate(goodsId);
            modelMap.put("evaluateGoodsVo", evaluateGoodsVo);

            EvaluateStoreVo evaluateStoreVo = evaluateService.getEvalStoreClass(goodsDetail.getStoreId());
            modelMap.put("evaluateStoreVo", evaluateStoreVo);

            //店铺客服
            if (storeInfo.getStorePresales() != null && !storeInfo.getStorePresales().equals("") && !storeInfo.getStorePresales().equals("[]")) {
                modelMap.put("storePresales", JsonHelper.toGenericObject(storeInfo.getStorePresales(), new TypeReference<List<ServiceVo>>() {
                }));
            }
            if (storeInfo.getStoreAftersales() != null && !storeInfo.getStoreAftersales().equals("") && !storeInfo.getStoreAftersales().equals("[]")) {
                modelMap.put("storeAftersales", JsonHelper.toGenericObject(storeInfo.getStoreAftersales(), new TypeReference<List<ServiceVo>>() {
                }));
            }

            //店铺分类
            List<StoreLabel> storeLabelList = storeLabelService.findByStoreId(storeInfo.getStoreId());
            modelMap.put("storeLabelList", storeLabelList);

            //商品热销列表
            List<Object> hotGoodsVoList = goodsService.findHotGoodsVoByStoreId(storeInfo.getStoreId(), 5);
            modelMap.put("hotGoodsVoList", hotGoodsVoList);

            //商品收藏列表
            List<Object> favoriteGoodsVoList = goodsService.findFavoriteGoodsVoByStoreId(storeInfo.getStoreId(), 5);
            modelMap.put("favoriteGoodsVoList", favoriteGoodsVoList);
            
            //保障服务
            List<ContractVo> contractVoList=contractService.getStoreContract(goodsDetail.getStoreId());
            modelMap.put("contractVoList", contractVoList);

            return getHomeTemplate("activity/goods");
    }
    
}
