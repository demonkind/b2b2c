package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.domain.goods.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Controller
public class CategoryAction extends BaseAction {
    @Autowired
    BrandDao brandDao;

    /**
     * 分类列表
     * @param model
     * @return
     */
    @RequestMapping(value = "category/list", method = RequestMethod.GET)
    public String list(ModelMap model) {
        List<Brand> brands = brandDao.findAll(Brand.class);
        model.put("brands", brands);

        return getAdminTemplate("goods/category/list");
    }
}
