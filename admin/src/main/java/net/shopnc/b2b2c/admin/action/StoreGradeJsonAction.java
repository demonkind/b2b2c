package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.domain.store.StoreGrade;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 店铺等级管理
 * Created by dqw on 2015-12-11.
 */
@Controller
public class StoreGradeJsonAction extends BaseJsonAction {

    @Autowired
    private StoreGradeDao storeGradeDao;

    /**
     * 店铺等级列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_grade/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = storeGradeDao.getDtGridList(dtGridPager, StoreGrade.class);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 店铺等级保存
     * @param storeGrade
     * @param gradeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_grade/save.json", method = RequestMethod.POST)
    public ResultEntity save(StoreGrade storeGrade,
                             @RequestParam(value = "gradeId", defaultValue = "0") int gradeId) {

        ResultEntity resultEntity = new ResultEntity();

        try {
            if (gradeId > 0) {
                storeGrade.setId(gradeId);
                storeGradeDao.update(storeGrade);
            } else {
                storeGradeDao.save(storeGrade);
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
     * 店铺等级删除
     * @param gradeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_grade/del.json", method = RequestMethod.POST)
    public ResultEntity save(@RequestParam("gradeId") int gradeId) {

        ResultEntity resultEntity = new ResultEntity();

        try {
            storeGradeDao.delete(StoreGrade.class, gradeId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }

        return resultEntity;
    }

}
