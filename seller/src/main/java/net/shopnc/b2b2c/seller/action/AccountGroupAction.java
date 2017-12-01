package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.store.SellerGroupDao;
import net.shopnc.b2b2c.dao.store.SellerGroupMenuDao;
import net.shopnc.b2b2c.domain.store.SellerGroup;
import net.shopnc.b2b2c.domain.store.SellerMenu;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.SellerMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家账号组管理
 */
@Controller
public class AccountGroupAction extends BaseAction {

    @Autowired
    private SellerGroupDao sellerGroupDao;

    @Autowired
    private SellerMenuService sellerMenuService;

    @Autowired
    private SellerGroupMenuDao sellerGroupMenuDao;

    public AccountGroupAction() {
        setMenuPath("account_group/list");

    }

    /**
     * 商家账号组列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "account_group/list",method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        List<SellerGroup> sellerGroupList = sellerGroupDao.findByStoreId(SellerSessionHelper.getSellerId());
        modelMap.addAttribute("sellerGroupList", sellerGroupList);

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("account_group/list", "账号组");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        return getSellerTemplate("account/account_group/list");
    }

    /**
     * 商家账号组添加
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "account_group/add", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {

        Map<String, List<SellerMenu>> sellerMenuList = sellerMenuService.getSellerMenu();
        modelMap.addAttribute("sellerMenuList", sellerMenuList);

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("account_group/list", "账号组");
        tabMenuMap.put("account_group/add", "添加组");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        return getSellerTemplate("account/account_group/add");
    }

    /**
     * 商家账号组编辑
     * @param groupId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "account_group/edit", method = RequestMethod.GET)
    public String edit(@RequestParam int groupId,
                       ModelMap modelMap) {

        SellerGroup sellerGroup = sellerGroupDao.get(SellerGroup.class, groupId);
        if (sellerGroup == null || sellerGroup.getStoreId() != SellerSessionHelper.getStoreId()) {
            return "redirect:/account_group/list";
        }
        modelMap.addAttribute("sellerGroup", sellerGroup);

        List<Integer> groupMenuIdList = sellerGroupMenuDao.findMenuIdListByGroupId(groupId);
        modelMap.addAttribute("groupMenuIdList", groupMenuIdList);

        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("account_group/list", "账号组");
        tabMenuMap.put("account_group/edit", "编辑组");
        modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        return getSellerTemplate("account/account_group/add");
    }

}