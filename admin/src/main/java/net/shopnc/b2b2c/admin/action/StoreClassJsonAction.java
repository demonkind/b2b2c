package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.store.StoreClassDao;
import net.shopnc.b2b2c.domain.store.StoreClass;
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
 * 店铺主营行业管理
 * Created by dqw on 2015-12-11.
 */
@Controller
public class StoreClassJsonAction extends BaseJsonAction {

    @Autowired
    private StoreClassDao storeClassDao;

    /**
     * 主营行业列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_class/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = storeClassDao.getDtGridList(dtGridPager, StoreClass.class);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 主营行业保存
     * @param storeClass
     * @param classId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_class/save.json", method = RequestMethod.POST)
    public ResultEntity saveJson(StoreClass storeClass,
            @RequestParam(value = "classId", defaultValue = "0") int classId){

        ResultEntity resultEntity = new ResultEntity();

        try {
            if (classId > 0) {
                storeClass.setId(classId);
                storeClassDao.update(storeClass);
            } else {
                storeClassDao.save(storeClass);
            }
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("保存失败");
        }

        return resultEntity;
    }

    /**
     * 主营行业删除
     * @param classId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_class/del.json", method = RequestMethod.POST)
    public ResultEntity delJson(@RequestParam("classId") int classId) {

        ResultEntity resultEntity = new ResultEntity();

        try {
            storeClassDao.delete(StoreClass.class, classId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }

        return resultEntity;
    }
}
