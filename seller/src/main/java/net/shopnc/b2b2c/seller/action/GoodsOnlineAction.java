package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.SellerGoodsListSignal;
import net.shopnc.b2b2c.dao.goods.FormatTemplateDao;
import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.domain.goods.FormatTemplate;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.goods.GoodsSellerService;
import net.shopnc.b2b2c.vo.goods.GoodsCommonVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 出售中的商品
 * Created by shopnc.feng on 2015-12-14.
 */
@Controller
public class GoodsOnlineAction extends BaseAction {
    @Autowired
    GoodsCommonDao goodsCommonDao;
    @Autowired
    GoodsSellerService goodsSellerService;
    @Autowired
    FormatTemplateDao formatTemplateDao;
    @Autowired
    GoodsActivityService goodsActivityService;

    public GoodsOnlineAction() {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("goods/online/list", "出售中的商品");
        setSellerTabMenu(tabMenuMap);
        setMenuPath("goods/online/list");
    }

    /**
     * 商品列表
     * @param page
     * @param type
     * @param keyword
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/online/list", method = RequestMethod.GET)
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(name = "type", required = false, defaultValue = "0") Integer type,
                       @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                       ModelMap modelMap) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("page", page);
        paramMap.put("type", type);
        paramMap.put("keyword", keyword);
        HashMap<String, Object> map = goodsSellerService.getList(SellerSessionHelper.getStoreId(), paramMap, SellerGoodsListSignal.ONLINE);
        List<Object> obj=(List<Object>) map.get("goodsCommonList");
        modelMap.addAllAttributes(map);
        modelMap.addAllAttributes(paramMap);
        
        // 顶部关联版式
        List<FormatTemplate> formatTopList = formatTemplateDao.findTopByStoreId(SellerSessionHelper.getStoreId());
        modelMap.put("formatTopList", formatTopList);
        // 底部关联版式
        List<FormatTemplate> formatBottomList = formatTemplateDao.findBottomByStoreId(SellerSessionHelper.getStoreId());
        modelMap.put("formatBottomList", formatBottomList);
        return getSellerTemplate("goods/online/list");
    }

    /**
     * 编辑商品
     * @param commonId
     * @param categoryId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/edit/{commonId}", method = RequestMethod.GET)
    public String edit(@PathVariable int commonId,
                       @RequestParam(name = "categoryId", required = false, defaultValue = "0") Integer categoryId,
                       ModelMap modelMap) {

        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        if (goodsCommon == null || SellerSessionHelper.getStoreId() != goodsCommon.getStoreId()) {
            SellerSessionHelper.setError("参数错误");
            return "redirect:/error";
        }
        modelMap.put("goodsCommon", goodsCommon);
        categoryId = categoryId == 0 ? goodsCommon.getCategoryId() : categoryId;
        HashMap<String, Object> param = goodsSellerService.getAddGoodsParam(categoryId, SellerSessionHelper.getStoreId());
        modelMap.put("param", param);

        HashMap<String, Object> check = goodsSellerService.getCheckParam(commonId);
        modelMap.put("check", check);

        return getSellerTemplate("goods/online/edit");
    }


    /**
     * 编辑SKU商品
     * @param goodsId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/edit/sku/{goodsId}", method = RequestMethod.GET)
    public String editSku(@PathVariable int goodsId, ModelMap modelMap) {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("goods/online/list", "出售中的商品");
        tabMenuMap.put("goods/edit/sku/" + goodsId, "编辑商品SKU");
        modelMap.put("sellerTabMenuMap", tabMenuMap);
        try {
            HashMap<String, Object> map = goodsSellerService.getEditSkuParam(goodsId, SellerSessionHelper.getStoreId());
            modelMap.putAll(map);
        } catch (Exception e) {
            logger.error(e.toString());
            SellerSessionHelper.setError("参数错误");
            return "redirect:/error";
        }
        return getSellerTemplate("goods/online/edit_sku");
    }

    /**
     * 编辑商品分类
     * @param commonId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/edit_category/{commonId}", method = RequestMethod.GET)
    public String editCategory(@PathVariable int commonId,
                               ModelMap modelMap) {
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, commonId);
        if (goodsCommon == null || SellerSessionHelper.getStoreId() != goodsCommon.getStoreId()) {
            SellerSessionHelper.setError("参数错误");
            return "redirect:/error";
        }
        modelMap.put("goodsCommon", goodsCommon);
        return getSellerTemplate("goods/online/edit_category");
    }
}
