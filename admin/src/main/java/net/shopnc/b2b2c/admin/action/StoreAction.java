package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.store.StoreClassDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.domain.store.StoreClass;
import net.shopnc.b2b2c.domain.store.StoreGrade;
import net.shopnc.b2b2c.service.store.StoreService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.List;

/**
 * 店铺管理
 * Created by dqw on 2015-12-25.
 */
@Controller
public class StoreAction extends BaseAction {

    @Autowired
    private StoreClassDao storeClassDao;

    @Autowired
    private StoreGradeDao storeGradeDao;

    /**
     * 店铺列表页
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        List<StoreClass> storeClassList = storeClassDao.findAll(StoreClass.class);
        modelMap.addAttribute("storeClassList", storeClassList);
        List<StoreGrade> storeGradeList = storeGradeDao.findAll(StoreGrade.class);
        modelMap.addAttribute("storeGradeList", storeGradeList);
        return getAdminTemplate("store/store/list");
    }
}
