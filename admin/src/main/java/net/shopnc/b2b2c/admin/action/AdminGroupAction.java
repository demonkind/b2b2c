package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.admin.AdminGroupDao;
import net.shopnc.b2b2c.dao.admin.AdminGroupPermissionDao;
import net.shopnc.b2b2c.domain.admin.AdminGroup;
import net.shopnc.b2b2c.domain.admin.AdminMenu;
import net.shopnc.b2b2c.service.admin.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 后台管理员组
 */
@Controller
public class AdminGroupAction extends BaseAction {

    @Autowired
    AdminGroupDao adminGroupDao;

    @Autowired
    AdminGroupPermissionDao adminGroupPermissionDao;

    @Autowired
    AdminMenuService adminMenuService;

    public AdminGroupAction(){
        setMenuPath("admin_group/list");
    }

    /**
     * 管理员组列表
     * @return
     */
    @RequestMapping("/admin_group/list")
    public String list() {
        return getAdminTemplate("admin/admin_group/list");
    }

    /**
     * 管理员组添加
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin_group/add")
    public String add(ModelMap modelMap) {

        List<AdminMenu> adminMenuList = adminMenuService.getAdminMenu();
        modelMap.addAttribute("adminMenuList", adminMenuList);
        return getAdminTemplate("admin/admin_group/add");
    }

    /**
     * 管理员组编辑
     * @param groupId
     * @param modelMap
     * @return
     */
    @RequestMapping("/admin_group/edit")
    public String edit(@RequestParam int groupId, ModelMap modelMap) {
        setMenuPath("admin_group/list");

        AdminGroup adminGroup = adminGroupDao.get(AdminGroup.class, groupId);
        modelMap.addAttribute("adminGroup", adminGroup);

        List<AdminMenu> adminMenuList = adminMenuService.getAdminMenu();
        modelMap.addAttribute("adminMenuList", adminMenuList);

        List<Integer> groupMenuIdList = adminGroupPermissionDao.findMenuIdListByGroupId(groupId);
        modelMap.addAttribute("groupMenuIdList", groupMenuIdList);
        return getAdminTemplate("admin/admin_group/add");
    }

}