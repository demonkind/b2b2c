package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by shopnc.feng on 2015-11-30.
 */
@Controller
public class GoodsAction extends BaseAction {
    @Autowired
    SiteService siteService;
    @Autowired
    GoodsService goodsService;
    /**
     * 商品列表页
     * @return
     */
    @RequestMapping(value = "goods/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        long waitCount = goodsService.findWaitCount();
        modelMap.put("waitCount", waitCount);
        return getAdminTemplate("goods/goods/list");
    }

    /**
     * 等待审核的商品列表
     * @return
     */
    @RequestMapping(value = "goods/verify/list", method = RequestMethod.GET)
    public String verifyList() {
        return getAdminTemplate("goods/goods/verify_list");
    }

    /**
     * 商品设置
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/site", method = RequestMethod.GET)
    public String site(ModelMap modelMap) {
        modelMap.put(SiteTitle.GOODSVERIFY, siteService.getSiteInfo().get(SiteTitle.GOODSVERIFY));
        return getAdminTemplate("goods/goods/site");
    }

}
