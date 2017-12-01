package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.dao.store.SellerMenuDao;
import net.shopnc.b2b2c.domain.store.SellerMenu;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.seller.SellerMenuStateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by dqw on 2015/11/25.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SellerMenuService extends BaseService {

    @Autowired
    private SellerMenuDao sellerMenuDao;

    private String name;
    private List<SellerMenu> menuList;

    /**
     * 获取商家所有菜单
     * @return
     */
    public Map<String, List<SellerMenu>> getSellerMenu() {

        Map<String, List<SellerMenu>> sellerMenu = new LinkedHashMap<String, List<SellerMenu>>();

        List<SellerMenu> sellerMenuList = sellerMenuDao.getSellerMenuList();

        for (SellerMenu menu : sellerMenuList) {
            if (menu.getParentId() == 0) {
                name = menu.getName();
                menuList = new ArrayList<SellerMenu>();
                if (name != null) {
                    sellerMenu.put(name, menuList);
                }
                continue;
            }
            menuList.add(menu);
        }
        sellerMenu.put(name, menuList);

        return sellerMenu;
    }

    /**
     * 根据当前路径返回菜单选中状态和面包屑
     * @param path
     * @return
     */
    public SellerMenuStateVo getSellerMenuState(String path) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String currentPath = request.getServletPath();
        if(currentPath == null || currentPath.equals("")) {
            currentPath = "/";
        }
        currentPath = currentPath.substring(1, currentPath.length());
        String mainMenu = "首页";
        String title = "index";
        String subMenu = "";

        SellerMenuStateVo sellerMenuStateVo = new SellerMenuStateVo();
        List<String> breadCrumbList = new LinkedList<String>();

        if (!path.equals("")) {
            List<SellerMenu> sellerMenuList = sellerMenuDao.getSellerMenuList();
            for (SellerMenu menu : sellerMenuList) {
                if (menu.getParentId() == 0) {
                    mainMenu = menu.getName();
                    title = menu.getTitle();
                    continue;
                }
                if (menu.getUrl().equals(path)) {
                    subMenu = menu.getName();
                    break;
                }
            }

            breadCrumbList.add(mainMenu);
            breadCrumbList.add(subMenu);
        }

        sellerMenuStateVo.setCurrentMainMenu(mainMenu);
        sellerMenuStateVo.setCurrentSubMenu(subMenu);
        sellerMenuStateVo.setCurrentMenuTitle(title);
        sellerMenuStateVo.setCurrentPath(currentPath);
        sellerMenuStateVo.setBreadCrumbList(breadCrumbList);

        return sellerMenuStateVo;
    }

    /**
     * 根据上商家组编号返回商家权限集合
     * @param groupId
     * @return
     */
    public Set<String> findPremissionsByGroupId(int groupId) {
        Set<String> premissionSet = new HashSet<String>();

        List<SellerMenu> sellerMenuList;

        if (groupId > 0) {
            sellerMenuList = sellerMenuDao.findPremissionsByGroupId(groupId);
        } else {
            sellerMenuList = sellerMenuDao.findAll(SellerMenu.class);
        }

        for (SellerMenu sellerMenu : sellerMenuList) {
            if (sellerMenu.getUrl() != null && !sellerMenu.getUrl().equals("")) {
                premissionSet.add(sellerMenu.getUrl());
            }
        }

        return premissionSet;
    }

}
