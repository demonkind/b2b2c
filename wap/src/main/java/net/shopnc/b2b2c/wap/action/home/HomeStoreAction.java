package net.shopnc.b2b2c.wap.action.home;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.dao.store.StoreLabelDao;
import net.shopnc.b2b2c.dao.store.StoreNavigationDao;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.domain.store.StoreLabel;
import net.shopnc.b2b2c.domain.store.StoreNavigation;
import net.shopnc.b2b2c.domain.store.StoreSlide;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.service.store.StoreGoodsSearchService;
import net.shopnc.b2b2c.service.store.StoreLabelService;
import net.shopnc.b2b2c.service.store.StoreSlideService;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.b2b2c.vo.member.EvaluateStoreVo;
import net.shopnc.b2b2c.vo.store.ServiceVo;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dqw on 2016/01/27.
 */
@Controller
public class HomeStoreAction extends HomeBaseAction {

    @Autowired
    private StoreDao storeDao;
    @Autowired
    private StoreLabelDao storeLabelDao;
    @Autowired
    private StoreLabelService storeLabelService;
    @Autowired
    private StoreNavigationDao storeNavigationDao;
    @Autowired
    private StoreSlideService storeSlideService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private EvaluateService evaluateSerivce;
    @Autowired
    private StoreGoodsSearchService storeGoodsSearchService;

    /**
     * 店铺首页
     *
     * @param storeId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/{storeId}", method = RequestMethod.GET)
    public String index(@PathVariable int storeId, ModelMap modelMap) throws Exception {

        outputStoreCommon(storeId, modelMap);

        //店铺幻灯
        List<StoreSlide> storeSlideList = storeSlideService.findByStoreId(storeId);
        modelMap.put("storeSlideList", storeSlideList);

        //商品热销列表
        List<Object> hotGoodsVoList = goodsService.findHotGoodsVoByStoreId(storeId, 5);
        modelMap.put("hotGoodsVoList", hotGoodsVoList);

        //商品收藏列表
        List<Object> favoriteGoodsVoList = goodsService.findFavoriteGoodsVoByStoreId(storeId, 5);
        modelMap.put("favoriteGoodsVoList", favoriteGoodsVoList);

        //店铺推荐列表
        List<GoodsVo> commendGoodsVoList = storeGoodsSearchService.findCommendGoodsVoByStoreId(storeId, 12);
        modelMap.put("commendGoodsVoList", commendGoodsVoList);

        //店铺新品列表
        List<GoodsVo> newGoodsVoList = storeGoodsSearchService.findNewGoodsVoByStoreId(storeId, 12);
        modelMap.put("newGoodsVoList", newGoodsVoList);

        return getHomeTemplate("tmpl/store");
    }


    /**
     * 店铺导航
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/navigation/{id}", method = RequestMethod.GET)
    public String navigation(@PathVariable int id, ModelMap modelMap) throws Exception {
        //导航信息
        StoreNavigation storeNavigation = storeNavigationDao.get(StoreNavigation.class, id);
        modelMap.put("navInfo", storeNavigation);

        outputStoreCommon(storeNavigation.getStoreId(), modelMap);

        return getHomeTemplate("store/navigation");
    }

    /**
     * 店铺搜索
     *
     * @param storeId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/search", method = RequestMethod.GET)
    public String search(@RequestParam int storeId,
                         @RequestParam(name = "labelId", required = false, defaultValue = "0") Integer labelId,
                         @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                         @RequestParam(name = "sort", required = false, defaultValue = "default_desc") String sort,
                         @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                         ModelMap modelMap) throws Exception {

        outputStoreCommon(storeId, modelMap);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        params.put("labelId", labelId);
        params.put("page", page);
        params.put("sort", sort);
        params.put("keyword", keyword);

        modelMap.addAttribute("sort", sort);
        modelMap.addAttribute("keyword", keyword);

        HashMap<String, Object> map = storeGoodsSearchService.search(params);
        modelMap.addAttribute("showPage", map.get("showPage"));
        modelMap.addAttribute("goodsVoList", map.get("list"));

        //搜索标题
        String searchTitle = "店铺全部商品";
        if (labelId > 0) {
            StoreLabel storeLabel = storeLabelDao.get(StoreLabel.class, labelId);
            searchTitle = storeLabel.getStoreLabelName();
        }
        if (!keyword.equals("")) {
            searchTitle = "含有'" + keyword + "'的商品";
        }
        modelMap.addAttribute("searchTitle", searchTitle);

        return getHomeTemplate("store/search");
    }

    /**
     * 输出店铺公共信息
     *
     * @param storeId
     * @param modelMap
     */
    private void outputStoreCommon(int storeId, ModelMap modelMap) throws Exception {
        //店铺信息
        Store storeInfo = storeDao.get(Store.class, storeId);
        if (storeInfo == null) {
            throw new Exception("店铺不存在");
        }
        modelMap.put("storeInfo", storeInfo);
        if (storeInfo.getStorePresales() != null && !storeInfo.getStorePresales().equals("") && !storeInfo.getStorePresales().equals("[]")) {
            modelMap.put("storePresales", JsonHelper.toGenericObject(storeInfo.getStorePresales(), new TypeReference<List<ServiceVo>>() {
            }));
        }
        if (storeInfo.getStoreAftersales() != null && !storeInfo.getStoreAftersales().equals("") && !storeInfo.getStoreAftersales().equals("[]")) {
            modelMap.put("storeAftersales", JsonHelper.toGenericObject(storeInfo.getStoreAftersales(), new TypeReference<List<ServiceVo>>() {
            }));
        }

        //店铺分类
        List<StoreLabel> storeLabelList = storeLabelService.findByStoreId(storeId);
        modelMap.put("storeLabelList", storeLabelList);

        //店铺导航
        List<StoreNavigation> storeNavigationList = storeNavigationDao.findByStoreId(storeId);
        modelMap.put("storeNavigationList", storeNavigationList);

        //店铺评价信息
        EvaluateStoreVo evaluateStoreVo = evaluateSerivce.getEvalStoreClass(storeId);
        modelMap.put("evaluateStoreVo", evaluateStoreVo);
    }

}

