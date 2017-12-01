package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * 积分管理
 * Created by zxy on 2015-11-30.
 */
@Controller
public class PointsAction extends BaseAction {

    @Autowired
    private SiteService siteService;
    /**
     * 积分规则设置
     * @param model
     * @return
     */
    @RequestMapping(value = "points/setting", method = RequestMethod.GET)
    public String setting(ModelMap model) {
        HashMap siteInfo = siteService.getSiteInfo();
        model.put("siteInfo", siteInfo);
        return getAdminTemplate("member/points/setting");
    }
    /**
     * 积分明细日志
     * @return
     */
    @RequestMapping(value = "points/pointslog", method = RequestMethod.GET)
    public String pointslog() {
        return getAdminTemplate("member/points/pointslog");
    }
    /**
     * 积分增减
     * @return
     */
    @RequestMapping(value = "points/addpoints", method = RequestMethod.GET)
    public String addPoints() {
        return getAdminTemplate("member/points/addpoints");
    }
}
