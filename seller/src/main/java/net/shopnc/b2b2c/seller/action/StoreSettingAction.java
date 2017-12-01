package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.domain.store.StoreSlide;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.StoreService;
import net.shopnc.b2b2c.service.store.StoreSlideService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 店铺设置
 */
@Controller
public class StoreSettingAction extends BaseAction {

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private StoreSlideService storeSlideService;

    public StoreSettingAction() {
        setMenuPath("store/setting");

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("store/setting", "店铺设置");
        tabMenuMap.put("store/slide", "店铺幻灯");
        setSellerTabMenu(tabMenuMap);
    }

    /**
     * 店铺设置页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/setting",method = RequestMethod.GET)
    public String setting(ModelMap modelMap) {
        Store store = storeDao.get(Store.class, SellerSessionHelper.getStoreId());
        modelMap.addAttribute("storeInfo", store);
        return getSellerTemplate("store/setting/setting");
    }

    /**
     * 店铺幻灯设置页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/slide", method = RequestMethod.GET)
    public String slide(ModelMap modelMap) {
        List<StoreSlide> storeSlideList = storeSlideService.findByStoreId(SellerSessionHelper.getStoreId());
        for (int i = storeSlideList.size(); i < 5; i++) {
            StoreSlide storeSlide = new StoreSlide();
            storeSlide.setImageUrl(Common.DEFAULT_GOODS_IMG);
            storeSlideList.add(storeSlide);
        }
        modelMap.addAttribute("storeSlideList", storeSlideList);
        modelMap.addAttribute("defaultSlideImg", ShopConfig.getPublicRoot() + Common.DEFAULT_GOODS_IMG);
        return getSellerTemplate("store/setting/slide");
    }

}
