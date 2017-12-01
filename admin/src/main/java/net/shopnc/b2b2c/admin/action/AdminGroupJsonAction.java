package net.shopnc.b2b2c.admin.action;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.constant.UrlAdmin;
import net.shopnc.b2b2c.dao.admin.AdminGroupDao;
import net.shopnc.b2b2c.dao.admin.AdminGroupPermissionDao;
import net.shopnc.b2b2c.domain.admin.AdminGroup;
import net.shopnc.b2b2c.service.admin.AdminGroupService;
import net.shopnc.b2b2c.service.admin.AdminMenuService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 后台管理员组
 */
@Controller
public class AdminGroupJsonAction extends BaseJsonAction {

    @Autowired
    AdminGroupDao adminGroupDao;

    @Autowired
    AdminGroupService adminGroupService;

    /**
     * 管理员组列表JSON
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping("/admin_group/list.json")
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = adminGroupDao.getDtGridList(dtGridPager, AdminGroup.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 管理员组保存
     *
     * @param groupId
     * @param groupName
     * @param permission
     * @return
     */
    @ResponseBody
    @RequestMapping("/admin_group/save.json")
    public ResultEntity addJson(@RequestParam(value = "groupId", defaultValue = "0") int groupId,
                                @RequestParam String groupName,
                                @RequestParam String permission) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            List<String> menuIdList = JsonHelper.toGenericObject(permission, new TypeReference<List<String>>() {
            });
            adminGroupService.saveAdminGroup(groupId, groupName, menuIdList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(UrlAdmin.ADMIN_GROUP_LIST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 管理员组删除
     *
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping("/admin_group/del.json")
    public ResultEntity delJson(@RequestParam int groupId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            adminGroupService.delAdminGroup(groupId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }
}