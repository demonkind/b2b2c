package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 规格管理
 * Created by shopnc.feng on 2015-11-02.
 */
@Controller
public class SpecAction extends BaseAction {
    /**
     * 规格列表页
     * @return
     */
    @RequestMapping(value = "spec/list", method = RequestMethod.GET)
    public String list() {
        return getAdminTemplate("goods/spec/list");
    }
}
