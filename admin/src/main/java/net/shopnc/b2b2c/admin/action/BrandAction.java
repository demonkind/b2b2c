package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.goods.BrandLabelDao;
import net.shopnc.b2b2c.domain.goods.BrandLabel;
import net.shopnc.b2b2c.service.goods.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 品牌管理
 * Created by shopnc.feng on 2015-11-02.
 */
@Controller
public class BrandAction extends BaseAction {
    @Autowired
    BrandLabelDao brandLabelDao;
    @Autowired
    BrandService brandService;

    /**
     * 品牌列表页
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "brand/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        List<BrandLabel> brandLabels = brandLabelDao.findAll(BrandLabel.class);
        modelMap.put("brandLabels", brandLabels);

        long noPassCount = brandService.findWaitCount();
        modelMap.put("noPassCount", noPassCount);

        return getAdminTemplate("goods/brand/list");
    }

    /**
     * 品牌审核列表
     * @return
     */
    @RequestMapping(value = "brand/verify/list", method = RequestMethod.GET)
    public String verifyList(ModelMap modelMap) {
        List<BrandLabel> brandLabels = brandLabelDao.findAll(BrandLabel.class);
        modelMap.put("brandLabels", brandLabels);
        return getAdminTemplate("goods/brand/verify_list");
    }
}
