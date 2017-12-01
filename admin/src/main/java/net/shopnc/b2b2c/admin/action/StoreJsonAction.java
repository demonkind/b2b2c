package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.store.StoreClassDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.service.store.StoreService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;

/**
 * 店铺管理
 * Created by dqw on 2015-12-25.
 */
@Controller
public class StoreJsonAction extends BaseJsonAction {

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreClassDao storeClassDao;

    /**
     * 店铺列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = storeDao.getDtGridList(dtGridPager, Store.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 店铺保存
     * @param storeId
     * @param storeName
     * @param gradeId
     * @param classId
     * @param storeEndTime
     * @param state
     * @param billCycle
     * @param billCycleType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store/edit.json", method = RequestMethod.POST)
    public ResultEntity editJson(@RequestParam int storeId,
                                 @RequestParam String storeName,
                                 @RequestParam int gradeId,
                                 @RequestParam int classId,
                                 @RequestParam Timestamp storeEndTime,
                                 @RequestParam int state,
                                 @RequestParam int billCycle,
                                 @RequestParam int billCycleType) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            storeService.adminUpdateStore(storeId, storeName, gradeId, classId, storeEndTime, state, billCycle, billCycleType);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

}
