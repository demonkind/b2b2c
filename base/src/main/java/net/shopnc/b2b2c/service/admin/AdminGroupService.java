package net.shopnc.b2b2c.service.admin;

import net.shopnc.b2b2c.dao.admin.AdminGroupDao;
import net.shopnc.b2b2c.dao.admin.AdminGroupPermissionDao;
import net.shopnc.b2b2c.domain.admin.AdminGroup;
import net.shopnc.b2b2c.domain.admin.AdminGroupPermission;
import net.shopnc.b2b2c.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dqw on 2015/12/30.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AdminGroupService extends BaseService {

    @Autowired
    private AdminGroupDao adminGroupDao;

    @Autowired
    private AdminGroupPermissionDao adminGroupPermissionDao;

    /**
     * 保存管理员权限组
     *
     * @param groupId
     * @param groupName
     * @throws Exception
     */
    public void saveAdminGroup(int groupId, String groupName, List<String> menuIdList) throws Exception {
        AdminGroup adminGroup = new AdminGroup();
        adminGroup.setGroupName(groupName);

        if(groupId > 0) {
            adminGroup.setGroupId(groupId);
            adminGroupDao.update(adminGroup);
        } else {
            groupId = (Integer) adminGroupDao.save(adminGroup);
        }

        //清空权限记录
        adminGroupPermissionDao.delByGroupId(groupId);

        //添加新的权限记录
        List<AdminGroupPermission> adminGroupPermissionList = new ArrayList<AdminGroupPermission>();
        for(String menuId : menuIdList) {
            AdminGroupPermission adminGroupPermission = new AdminGroupPermission();
            adminGroupPermission.setGroupId(groupId);
            adminGroupPermission.setMenuId(Integer.valueOf(menuId));
            adminGroupPermissionList.add(adminGroupPermission);
        }
        adminGroupPermissionDao.saveAll(adminGroupPermissionList);
    }

    /**
     * 删除管理员权限组
     */
    public void delAdminGroup(int groupId) throws Exception {
        adminGroupDao.delete(AdminGroup.class, groupId);

        //清空权限记录
        adminGroupPermissionDao.delByGroupId(groupId);
    }
}

