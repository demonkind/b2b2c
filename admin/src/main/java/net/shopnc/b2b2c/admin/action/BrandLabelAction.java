package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 品牌标签
 * Created by shopnc.feng on 2015-11-03.
 */
@Controller
public class BrandLabelAction extends BaseAction {

    /**
     * 品牌标签列表
     *
     * @return
     */
    @RequestMapping(value = "brand_label/list", method = RequestMethod.GET)
    public String list() {
        return getAdminTemplate("goods/brand_label/list");
    }
}
