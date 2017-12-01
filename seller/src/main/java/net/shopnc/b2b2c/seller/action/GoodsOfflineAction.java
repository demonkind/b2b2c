package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.SellerGoodsListSignal;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.GoodsSellerService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 仓库中的商品
 * Created by shopnc.feng on 2015-12-23.
 */
@Controller
public class GoodsOfflineAction extends BaseAction {
    @Autowired
    GoodsSellerService goodsSellerService;

    public GoodsOfflineAction() {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("goods/offline/list", "仓库中的商品");
        tabMenuMap.put("goods/ban/list", "禁售的商品");
        tabMenuMap.put("goods/wait/list", "等待审核的商品");
        tabMenuMap.put("goods/fail/list", "审核失败的商品");
        setSellerTabMenu(tabMenuMap);
        setMenuPath("goods/offline/list");
    }

    /**
     * 仓库中的商品列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/offline/list", method = RequestMethod.GET)
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(name = "type", required = false, defaultValue = "0") Integer type,
                       @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                       ModelMap modelMap) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("page", page);
        paramMap.put("type", type);
        paramMap.put("keyword", keyword);
        HashMap<String, Object> map = goodsSellerService.getList(SellerSessionHelper.getStoreId(), paramMap, SellerGoodsListSignal.OFFLINE);
        modelMap.addAllAttributes(map);
        modelMap.addAllAttributes(paramMap);
        return getSellerTemplate("goods/offline/list_offline");
    }

    /**
     * 禁售的商品列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/ban/list", method = RequestMethod.GET)
    public String banList(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                          @RequestParam(name = "type", required = false, defaultValue = "0") Integer type,
                          @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                          ModelMap modelMap) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("page", page);
        paramMap.put("type", type);
        paramMap.put("keyword", keyword);
        HashMap<String, Object> map = goodsSellerService.getList(SellerSessionHelper.getStoreId(), paramMap, SellerGoodsListSignal.BAN);
        modelMap.addAllAttributes(map);
        modelMap.addAllAttributes(paramMap);
        return getSellerTemplate("goods/offline/list_ban");
    }

    /**
     * 等待审核的商品列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/wait/list", method = RequestMethod.GET)
    public String waitList(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                           @RequestParam(name = "type", required = false, defaultValue = "0") Integer type,
                           @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                           ModelMap modelMap) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("page", page);
        paramMap.put("type", type);
        paramMap.put("keyword", keyword);
        HashMap<String, Object> map = goodsSellerService.getList(SellerSessionHelper.getStoreId(), paramMap, SellerGoodsListSignal.WAIT);
        modelMap.addAllAttributes(map);
        modelMap.addAllAttributes(paramMap);
        return getSellerTemplate("goods/offline/list_wait");
    }

    /**
     * 审核失败的商品列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/fail/list", method = RequestMethod.GET)
    public String failList(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                           @RequestParam(name = "type", required = false, defaultValue = "0") Integer type,
                           @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                           ModelMap modelMap) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("page", page);
        paramMap.put("type", type);
        paramMap.put("keyword", keyword);
        HashMap<String, Object> map = goodsSellerService.getList(SellerSessionHelper.getStoreId(), paramMap, SellerGoodsListSignal.FAIL);
        modelMap.addAllAttributes(map);
        modelMap.addAllAttributes(paramMap);
        return getSellerTemplate("goods/offline/list_fail");
    }

    /**
     * 编辑SKU商品
     * @param goodsId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/offline/edit/sku/{goodsId}", method = RequestMethod.GET)
    public String editSku(@PathVariable int goodsId, ModelMap modelMap) {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("goods/offline/list", "仓库中的商品");
        tabMenuMap.put("goods/ban/list", "禁售的商品");
        tabMenuMap.put("goods/wait/list", "等待审核的商品");
        tabMenuMap.put("goods/fail/list", "审核失败的商品");
        tabMenuMap.put("goods/offline/edit/sku/" + goodsId, "编辑商品SKU");
        modelMap.put("sellerTabMenuMap", tabMenuMap);
        try {
            HashMap<String, Object> map = goodsSellerService.getEditSkuParam(goodsId, SellerSessionHelper.getStoreId());
            modelMap.putAll(map);
        } catch (Exception e) {
            SellerSessionHelper.setError("参数错误");
            return "redirect:/error";
        }
        return getSellerTemplate("goods/online/edit_sku");
    }
}
