package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.admin.AdminDao;
import net.shopnc.b2b2c.dao.admin.AdminGroupDao;
import net.shopnc.b2b2c.domain.admin.Admin;
import net.shopnc.b2b2c.domain.admin.AdminGroup;
import net.shopnc.b2b2c.exception.AdminExistingException;
import net.shopnc.b2b2c.service.admin.AdminService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 后台管理员
 */
@Controller
public class AdminJsonAction extends BaseJsonAction {

    @Autowired
    AdminService adminService;

    @Autowired
    AdminDao adminDao;

    /**
     * 管理员列表json数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping("/admin/list.json")
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = adminDao.getDtGridList(dtGridPager, Admin.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("/admin/add.json")
    public ResultEntity addJson(Admin admin) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            admin.setIsSuper(State.NO);
            adminService.addAdmin(admin);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (AdminExistingException e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("登录名已存在");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 编辑管理员
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("/admin/edit.json")
    public ResultEntity editJson(Admin admin) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            adminService.editAdmin(admin);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 删除管理员
     * @param adminId
     * @return
     */
    @ResponseBody
    @RequestMapping("/admin/del.json")
    public ResultEntity delJson(int adminId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            adminService.delAdmin(adminId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

}