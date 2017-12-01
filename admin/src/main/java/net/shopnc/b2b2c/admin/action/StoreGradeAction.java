package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.domain.store.StoreGrade;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 店铺等级管理
 * Created by dqw on 2015-12-11.
 */
@Controller
public class StoreGradeAction extends BaseAction {

    /**
     * 店铺等级列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store_grade/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        return getAdminTemplate("store/grade/list");
    }

}
