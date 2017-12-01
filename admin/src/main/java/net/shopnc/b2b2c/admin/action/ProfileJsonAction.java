package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.admin.util.AdminSessionHelper;
import net.shopnc.b2b2c.service.admin.AdminService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileJsonAction extends BaseJsonAction {

    @Autowired
    AdminService adminService;

    @ResponseBody
    @RequestMapping("/profile.json")
    public ResultEntity profile(@RequestParam(defaultValue = "") String password,
                                @RequestParam(defaultValue = "") String avatar) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            if (!password.equals("") || !avatar.equals("")) {
                adminService.editAdminProfile(AdminSessionHelper.getAdminId(), password, avatar);
            }
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }
}