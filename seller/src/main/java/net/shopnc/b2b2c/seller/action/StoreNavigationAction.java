package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.store.StoreNavigationDao;
import net.shopnc.b2b2c.domain.store.StoreNavigation;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.common.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 店铺导航Action
 * Created by dqw on 2016/01/22.
 */
@Controller
public class StoreNavigationAction extends BaseAction {

    @Autowired
    StoreNavigationDao storeNavigationDao;
    @Autowired
    SecurityHelper securityHelper;

    public StoreNavigationAction() {
        setMenuPath("store/navigation/list");
    }

    /**
     * 店铺导航列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/navigation/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        List<StoreNavigation> storeNavigationList = storeNavigationDao.findByStoreId(SellerSessionHelper.getStoreId());
        modelMap.addAttribute("storeNavigationList", storeNavigationList);

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("store/navigation/list", "导航列表");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        return getSellerTemplate("store/navigation/list");
    }

    /**
     * 店铺导航添加
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/navigation/add", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("store/navigation/list", "导航列表");
        tabMenuMap.put("store/navigation/add", "添加导航");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        return getSellerTemplate("store/navigation/add");
    }

    /**
     * 店铺导航编辑
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/navigation/edit", method = RequestMethod.GET)
    public String edit(@RequestParam int id,
                       ModelMap modelMap) {

        StoreNavigation storeNavigation = storeNavigationDao.get(StoreNavigation.class, id);
        modelMap.addAttribute("storeNavigation", storeNavigation);

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("store/navigation/list", "导航列表");
        tabMenuMap.put("store/navigation/edit", "编辑导航");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        return getSellerTemplate("store/navigation/add");
    }
}