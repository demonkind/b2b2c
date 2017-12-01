package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.service.goods.CategoryCommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 分类分佣相关
 * Created by shopnc.cj on 2015-11-02.
 */
@Controller
public class CategoryCommissionAction extends BaseAction {

    @Autowired
    private CategoryCommissionService categoryCommissionService;

    /**
     * 分类列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "rates/list", method = RequestMethod.GET)
    public String list(ModelMap model) {
        String path = getAdminTemplate("goods/rates/list");
        return path;
    }
}
