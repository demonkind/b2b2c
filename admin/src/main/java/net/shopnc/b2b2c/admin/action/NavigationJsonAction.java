package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.NavigationDao;
import net.shopnc.b2b2c.domain.Navigation;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by hou on 2016/3/15.
 */
@Controller
public class NavigationJsonAction extends BaseJsonAction {
    private final static int OPENTYPE0 = 0;
    private final static int OPENtYPE1 = 1;
    @Autowired
    private NavigationDao navigationDao;
    /**
     * 列表
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "navigation/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = navigationDao.getDtGridList(dtGridPager, Navigation.class);
        } catch (Exception e) {
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 新增
     * @param navigation
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "navigation/save", method = RequestMethod.POST)
    public ResultEntity add(@Valid Navigation navigation, BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "navigation/list");
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                navigation.setOpenType(navigation.getOpenType() == OPENtYPE1 ? OPENtYPE1 : OPENTYPE0);
                navigationDao.save(navigation);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("操作成功");
            } catch (Exception e) {
                logger.error(e.toString());
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("操作失败");
            }
        }
        return resultEntity;
    }

    /**
     * 编辑
     * @param navigation
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "navigation/edit", method = RequestMethod.POST)
    public ResultEntity edit(@Valid Navigation navigation, BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "navigation/list");
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                navigation.setOpenType(navigation.getOpenType() == OPENtYPE1 ? OPENtYPE1 : OPENTYPE0);
                navigationDao.update(navigation);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("操作成功");
            } catch (Exception e) {
                logger.error(e.toString());
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("操作失败");
            }
        }
        return resultEntity;
    }

    /**
     * 删除
     * @param navId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "navigation/del", method = RequestMethod.POST)
    public ResultEntity del(@RequestParam(value = "navId", required = true) int navId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "navigation/list");
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            navigationDao.delete(Navigation.class,navId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.equals(e.toString());
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
}
