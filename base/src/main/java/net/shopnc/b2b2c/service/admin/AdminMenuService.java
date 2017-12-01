package net.shopnc.b2b2c.service.admin;

import net.shopnc.b2b2c.dao.admin.AdminMenuDao;
import net.shopnc.b2b2c.domain.admin.AdminMenu;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.admin.AdminMenuStateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dqw on 2015/11/25.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AdminMenuService extends BaseService {

    @Autowired
    private AdminMenuDao adminMenuDao;

    /**
     * 获得所有后台菜单
     *
     * @return
     */
    public List<AdminMenu> getAdminMenu() {
        List<AdminMenu> adminMenuList = new LinkedList<AdminMenu>();
        List<AdminMenu> menu1List = adminMenuDao.findAdminMenuListByParentId(0);
        for (AdminMenu menu1 : menu1List) {
            List<AdminMenu> menu2List2 = new LinkedList<AdminMenu>();
            List<AdminMenu> menu2List = adminMenuDao.findAdminMenuListByParentId(menu1.getId());
            for (AdminMenu menu2 : menu2List) {
                List<AdminMenu> menu3List = adminMenuDao.findAdminMenuListByParentId(menu2.getId());
                menu2.setSubMenu(menu3List);
                menu2List2.add(menu2);
            }
            menu1.setSubMenu(menu2List2);
            adminMenuList.add(menu1);
        }
        return adminMenuList;
    }

    /**
     * 获得后台菜单选中状态
     *
     * @param path
     * @return
     */
    public AdminMenuStateVo getAdminMenuState(String path) {
        if (path.equals("")) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            path = request.getServletPath();
            path = path.substring(1, path.length());
        }

        AdminMenuStateVo adminMenuStateVo = new AdminMenuStateVo();
        List<String> breadCrumbList = new LinkedList<String>();

        List<AdminMenu> adminMenuList = adminMenuDao.findAdminMenuList();

        AdminMenu menu1 = null;
        AdminMenu menu2 = null;
        List<AdminMenu> menuTree = new ArrayList<AdminMenu>();
        for (AdminMenu menu : adminMenuList) {
            if (menu.getUrl().equals(path)) {
                menu1 = menu;
                menuTree.add(menu);
                break;
            }
        }

        if (menu1 != null && menu1.getParentId() > 0) {
            for (AdminMenu menu : adminMenuList) {
                if (menu1.getParentId() == menu.getId()) {
                    menu2 = menu;
                    menuTree.add(menu);
                    break;
                }
            }
        }

        if (menu2 != null && menu2.getParentId() > 0) {
            for (AdminMenu menu : adminMenuList) {
                if (menu2.getParentId() == menu.getId()) {
                    menuTree.add(menu);
                    break;
                }
            }
        }

        int count = menuTree.size();
        if (count == 3) {
            adminMenuStateVo.setCurrentMenu1(menuTree.get(2).getId());
            adminMenuStateVo.setCurrentMenu2(menuTree.get(1).getId());
            adminMenuStateVo.setCurrentMenu3(menuTree.get(0).getId());
        }

        if (count == 2) {
            adminMenuStateVo.setCurrentMenu1(menuTree.get(1).getId());
            adminMenuStateVo.setCurrentMenu2(menuTree.get(0).getId());
        }

        if (count == 1) {
            adminMenuStateVo.setCurrentMenu1(menuTree.get(0).getId());
        }

        for (int i = count; i > 0; i--) {
            breadCrumbList.add(menuTree.get(i - 1).getName());
        }

        adminMenuStateVo.setBreadCrumbList(breadCrumbList);

        return adminMenuStateVo;
    }
}
