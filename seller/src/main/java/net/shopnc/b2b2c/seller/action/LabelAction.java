package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.domain.store.StoreLabel;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.StoreLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 店内商品分类
 * Created by shopnc.feng on 2015-12-01.
 */
@Controller
public class LabelAction extends BaseAction {
    @Autowired
    StoreLabelService storeLabelService;

    public LabelAction() {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("label/list", "店内商品分类");
        setSellerTabMenu(tabMenuMap);
        setMenuPath("label/list");
    }

    /**
     * 店内分类列表
     * @return
     */
    @RequestMapping(value = "label/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        List<StoreLabel> storeLabelList = storeLabelService.findByStoreId(SellerSessionHelper.getStoreId());
        modelMap.put("storeLabelList", storeLabelList);
        return getSellerTemplate("store/label/list");
    }
}
