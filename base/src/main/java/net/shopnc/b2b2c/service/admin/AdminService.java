package net.shopnc.b2b2c.service.admin;

import net.shopnc.b2b2c.dao.admin.AdminDao;
import net.shopnc.b2b2c.dao.admin.AdminGroupDao;
import net.shopnc.b2b2c.dao.admin.AdminMenuDao;
import net.shopnc.b2b2c.domain.admin.Admin;
import net.shopnc.b2b2c.domain.admin.AdminGroup;
import net.shopnc.b2b2c.domain.admin.AdminMenu;
import net.shopnc.b2b2c.exception.AdminExistingException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by dqw on 2015/12/30.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AdminService extends BaseService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private AdminGroupDao adminGroupDao;

    @Autowired
    private AdminMenuService adminMenuService;

    @Autowired
    private AdminMenuDao adminMenuDao;

    /**
     * 添加管理员
     *
     * @param admin
     * @throws Exception
     */
    public void addAdmin(Admin admin) throws Exception {
        Admin existAdmin = adminDao.findByName(admin.getName());
        if (existAdmin != null) {
            throw new AdminExistingException();
        }
        admin.setPassword(ShopHelper.getMd5(admin.getPassword()));
        AdminGroup adminGroup = adminGroupDao.get(AdminGroup.class, admin.getGroupId());
        if (adminGroup == null) {
            throw new Exception("管理员权限组不存在");
        }
        admin.setGroupName(adminGroup.getGroupName());
        adminDao.save(admin);
    }

    /**
     * 编辑管理员
     *
     * @param newAdmin
     * @throws Exception
     */
    public void editAdmin(Admin newAdmin) throws Exception {
        Admin admin = adminDao.get(Admin.class, newAdmin.getAdminId());
        if (admin == null) {
            throw new Exception("管理员不存在");
        }
        if (admin.getGroupId() == 0) {
            throw new Exception("超级管理员不允许编辑");
        }
        if (!newAdmin.getPassword().equals("")) {
            admin.setPassword(ShopHelper.getMd5(newAdmin.getPassword()));
        }
        AdminGroup adminGroup = adminGroupDao.get(AdminGroup.class, newAdmin.getGroupId());
        if (adminGroup == null) {
            throw new Exception("管理员权限组不存在");
        }
        admin.setGroupName(adminGroup.getGroupName());
        adminDao.update(admin);
    }

    /**
     * 修改管理员资料
     *
     * @param adminId
     * @param password
     * @param avatar
     * @throws Exception
     */
    public void editAdminProfile(int adminId, String password, String avatar) throws Exception {
        Admin admin = adminDao.get(Admin.class, adminId);
        if (admin == null) {
            throw new Exception("管理员不存在");
        }
        if (!password.equals("")) {
            admin.setPassword(ShopHelper.getMd5(password));
        }
        if (!avatar.equals("")) {
            admin.setAvatar(avatar);
        }
        adminDao.update(admin);
    }

    /**
     * 删除管理员
     * @param adminId
     * @throws Exception
     */
    public void delAdmin(int adminId) throws Exception {
        Admin admin = adminDao.get(Admin.class, adminId);
        if (admin == null) {
            throw new Exception("管理员不存在");
        }
        if (admin.getGroupId() == 0) {
            throw new Exception("超级管理员不允许编辑");
        }
        adminDao.delete(admin);
    }

    /**
     * 获取管理员有权限的列表
     * @param adminId
     * @return
     */
    public List<AdminMenu> findAdminMenuPermission(int adminId) {
        Admin admin = adminDao.get(Admin.class, adminId);

        Set<String> adminPermissionList = adminMenuDao.findAdminPermissionByGroupId(admin.getGroupId());

        List<AdminMenu> adminMenuList = adminMenuService.getAdminMenu();

        List<AdminMenu> newAdminMenuList = new LinkedList<AdminMenu>();

        for (AdminMenu adminMenu1 : adminMenuList) {
            List<AdminMenu> subMenu = new LinkedList<AdminMenu>();
            for (AdminMenu adminMenu2 : adminMenu1.getSubMenu()) {
                if (adminPermissionList.contains(adminMenu2.getPermission())) {
                    subMenu.add(adminMenu2);
                }
            }
            if (subMenu.size() > 0) {
                adminMenu1.setSubMenu(subMenu);
                newAdminMenuList.add(adminMenu1);
            }
        }

        return newAdminMenuList;
    }
}

