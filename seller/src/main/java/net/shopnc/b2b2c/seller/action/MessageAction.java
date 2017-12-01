package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.MessageTemplateCommonTplClass;
import net.shopnc.b2b2c.domain.MessageTemplateCommon;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.StoreMessageService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * 商家消息
 * Created by shopnc.feng on 2016-02-04.
 */
@Controller
public class MessageAction extends BaseAction {
    @Autowired
    StoreMessageService storeMessageService;

    public MessageAction() {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<>();
        tabMenuMap.put("message/list/1", "全部消息");
        HashMap<Integer, String> map = new MessageTemplateCommonTplClass().getStoreClassMap();
        for (Integer tplClass : map.keySet()) {
            tabMenuMap.put("message/list/" + tplClass, map.get(tplClass));
        }
        tabMenuMap.put("message/setting", "接收设置");
        setSellerTabMenu(tabMenuMap);
        setMenuPath("message/list/1");
    }

    /**
     * 商家消息列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequiresPermissions("message/list/1")
    @RequestMapping(value = "message/list/{tplClass:\\d+}", method = RequestMethod.GET)
    public String list(@PathVariable Integer tplClass,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       ModelMap modelMap) {

        List<Integer> tplClassList = new MessageTemplateCommonTplClass().getStoreClassList();
        tplClassList.add(1);
        if (!tplClassList.contains(tplClass)) {
            return "redirect:/message/list/1";
        }
        HashMap<String, Object> map = storeMessageService.findList(SellerSessionHelper.getSellerId(), tplClass, page);
        modelMap.addAllAttributes(map);
        return getSellerTemplate("message/list");
    }

    /**
     * 消息设置
     * @param modelMap
     * @return
     */
    @RequiresPermissions("message/list/1")
    @RequestMapping(value = "message/setting", method = RequestMethod.GET)
    public String setting(ModelMap modelMap) {
        List<MessageTemplateCommon> messageTemplateCommonList = storeMessageService.findMessageTemplateSeller(SellerSessionHelper.getStoreId());
        modelMap.put("messageTemplateCommonList", messageTemplateCommonList);
        return getSellerTemplate("message/setting");
    }
}
