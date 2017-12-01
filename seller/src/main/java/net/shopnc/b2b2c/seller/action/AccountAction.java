package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.store.SellerGroupDao;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.SellerGroup;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.SellerService;
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
 * 商家账号管理
 */
@Controller
public class AccountAction extends BaseAction {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerGroupDao sellerGroupDao;

    public AccountAction() {
        setMenuPath("account/list");

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("account/list", "账号列表");
        setSellerTabMenu(tabMenuMap);
    }

    /**
     * 商家账号列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "account/list", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        List<Seller> sellerList = sellerService.findSellerListByStoreId(SellerSessionHelper.getStoreId());
        modelMap.addAttribute("sellerList", sellerList);

        List<SellerGroup> sellerGroupList = sellerGroupDao.findByStoreId(SellerSessionHelper.getStoreId());
        modelMap.addAttribute("sellerGroupList", sellerGroupList);
        return getSellerTemplate("account/account/list");
    }
}