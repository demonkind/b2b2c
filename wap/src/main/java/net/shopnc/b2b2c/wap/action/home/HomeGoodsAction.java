package net.shopnc.b2b2c.wap.action.home;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.dao.orders.OrdersGoodsDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.domain.store.StoreLabel;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.service.store.StoreLabelService;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.b2b2c.vo.contract.ContractVo;
import net.shopnc.b2b2c.vo.goods.GoodsDetailVo;
import net.shopnc.b2b2c.vo.member.EvaluateGoodsVo;
import net.shopnc.b2b2c.vo.member.EvaluateStoreVo;
import net.shopnc.b2b2c.vo.store.ServiceVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品详情
 * Created by shopnc.feng on 2015-12-03.
 */
@Controller
public class HomeGoodsAction extends HomeBaseAction {
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

    /**
     * 商品详情信息
     *
     * @param goodsId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/{goodsId}", method = RequestMethod.GET)
    public String info(@PathVariable int goodsId, ModelMap modelMap) throws ShopException {
            // 商品详细信息
            GoodsDetailVo goodsDetail = goodsService.getDetail(goodsId);
            modelMap.put("goodsDetail", goodsDetail);

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

            return getHomeTemplate("goods");
    }

    /**
     * 商品详情中的评价列表
     *
     * @param modelMap
     * @param page
     * @param evaluateGoodsVo
     * @return
     */
    @RequestMapping(value = "goods/evaluate", method = RequestMethod.GET)
    public String queryPageGoodsEvaluate(ModelMap modelMap, @RequestParam(value = "page", required = false, defaultValue = "1") int page, EvaluateGoodsVo evaluateGoodsVo) {
        Map<String, Object> map = evaluateService.queryPageGoodsEvaluate(page, evaluateGoodsVo);
        List<EvaluateGoodsVo> voList = (List) map.get("list");
        modelMap.put("evaluateGoodsVo", voList);
        modelMap.put("goodsId", evaluateGoodsVo.getGoodsId());
        return getHomeTemplate("goods/evaluate_list");
    }

    /**
     * 商品详情中查看更多评价
     *
     * @param modelMap
     * @param goodsid
     * @param page
     * @param lv
     * @return
     * @throws ShopException
     */
    @RequestMapping(value = "goods/evaluate_more", method = RequestMethod.GET)
    public String initGoodsEvaluate(ModelMap modelMap, int goodsid, @RequestParam(value = "page", required = false, defaultValue = "1") int page, String lv) throws ShopException {
        EvaluateGoodsVo evaluateGoodsVo = evaluateService.queryGoodsEvaluate(goodsid);
        modelMap.put("evaluateGoodsVo", evaluateGoodsVo);

        EvaluateGoodsVo queryVo = new EvaluateGoodsVo();
        queryVo.setGoodsId(String.valueOf(goodsid));
        queryVo.setEvalLv(lv);
        Map<String, Object> map = evaluateService.queryPageGoodsEvaluate(page, queryVo);
        modelMap.put("evaluateList", map.get("list"));
        modelMap.put("showPage", map.get("showPage"));
        if (lv == null || lv.isEmpty()) {
            lv = "all";
        }
        modelMap.put("evalLv", lv);

        // 面包屑
        List<CrumbsVo> crumbsVoList = goodsService.getEvaluateCrumbs(goodsid);
        modelMap.put("crumbsList", crumbsVoList);
        
        
        // 店铺信息
        Store storeInfo = storeDao.get(Store.class, Integer.parseInt(evaluateGoodsVo.getStoreId()));
        modelMap.put("storeInfo", storeInfo);

        EvaluateStoreVo evaluateStoreVo = evaluateService.getEvalStoreClass(Integer.parseInt(evaluateGoodsVo.getStoreId()));
        modelMap.put("evaluateStoreVo", evaluateStoreVo);
        

        return getHomeTemplate("goods/evaluate_more");
    }

    /**
     * 商品销售记录
     * @param goodsId
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/sale_list", method = RequestMethod.GET)
    public String saleList(int goodsId,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           ModelMap modelMap) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageNo(page);
        pageEntity.setTotal(ordersGoodsDao.findCountByGoodsId(goodsId));

        List<Object> ordersGoodsList = ordersGoodsDao.findOrderGoodsVoByGoodsId(goodsId, pageEntity.getPageNo(), pageEntity.getPageSize());

        modelMap.put("orderGoodsList", ordersGoodsList);
        modelMap.put("showPage", pageEntity.getPageHtml());
        return getHomeTemplate("goods/sale_list");
    }
}
