package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.admin.AdminGroupDao;
import net.shopnc.b2b2c.domain.admin.AdminGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 后台管理员
 */
@Controller
public class AdminAction extends BaseAction {

    @Autowired
    AdminGroupDao adminGroupDao;

    /**
     * 管理员列表
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin/list")
    public String list(ModelMap modelMap) {
        List<AdminGroup> adminGroupList = adminGroupDao.findAll(AdminGroup.class);
        modelMap.addAttribute("adminGroupList", adminGroupList);
        return getAdminTemplate("admin/admin/list");
    }

}