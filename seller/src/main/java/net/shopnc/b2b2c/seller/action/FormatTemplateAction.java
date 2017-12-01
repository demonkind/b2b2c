package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.FormatSite;
import net.shopnc.b2b2c.dao.goods.FormatTemplateDao;
import net.shopnc.b2b2c.domain.goods.FormatTemplate;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.FormatTemplateService;
import net.shopnc.common.entity.PageEntity;
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
 * 关联板式
 * Created by shopnc.feng on 2015-12-16.
 */
@Controller
public class FormatTemplateAction extends BaseAction {
    @Autowired
    FormatTemplateDao formatTemplateDao;
    @Autowired
    FormatTemplateService formatTemplateService;

    public FormatTemplateAction() {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("format_template/list", "关联板式");
        setSellerTabMenu(tabMenuMap);
        setMenuPath("format_template/list");
    }

    /**
     * 关联板式列表
     * @return
     */
    @RequestMapping(value = "format_template/list", method = RequestMethod.GET)
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "formatSite", required = false, defaultValue = "-1") int formatSite,
                       @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            ModelMap modelMap) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("formatSite", formatSite);
        map.put("keyword", keyword);
        HashMap<String, Object> list = formatTemplateService.list(map, SellerSessionHelper.getStoreId());
        modelMap.addAllAttributes(list);
        modelMap.addAllAttributes(map);

        HashMap<String, Object> siteMap = new FormatSite().get();
        modelMap.put("site", siteMap);
        return getSellerTemplate("goods/format_template/list");
    }
}
