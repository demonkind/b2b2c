package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.common.entity.ResultEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Controller
public class ServiceAction extends BaseAction {
    @Autowired
    private StoreDao storeDao;

    public ServiceAction() {
        setMenuPath("service/list");

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("service/list", "客服设置");
        setSellerTabMenu(tabMenuMap);
    }

    @RequestMapping(value = "service/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        Store store = storeDao.get(Store.class, SellerSessionHelper.getStoreId());
        modelMap.addAttribute("storePresales", store.getStorePresales());
        modelMap.addAttribute("storeAftersales", store.getStoreAftersales());
        modelMap.addAttribute("storeWorkingTime", store.getStoreWorkingtime());
        return getSellerTemplate("service/list");
    }

    @ResponseBody
    @RequestMapping(value = "service/save.json", method = RequestMethod.POST)
    public ResultEntity saveJson(@RequestParam String storePresales,
                           @RequestParam String storeAftersales,
                           @RequestParam String storeWorkingTime) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            Store store = storeDao.get(Store.class, SellerSessionHelper.getStoreId());
            store.setStorePresales(storePresales);
            store.setStoreAftersales(storeAftersales);
            store.setStoreWorkingtime(storeWorkingTime);
            storeDao.update(store);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(UrlSeller.SERVICE_LIST);
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }
}
