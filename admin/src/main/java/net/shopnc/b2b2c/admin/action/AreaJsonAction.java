package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.AreaDao;
import net.shopnc.b2b2c.domain.Area;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.AreaService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 地区
 * Created by hou on 2016/3/15.
 */
@Controller
public class AreaJsonAction extends BaseJsonAction {
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private AreaService areaService;
    /**
     * 列表
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "area/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = areaService.getArticleCategoryDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return  dtGrid;
    }

    /**
     * 新增
     * @param area
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "area/save", method = RequestMethod.POST)
    public ResultEntity save(@Valid Area area,
                             BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "area/list");
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            areaDao.save(area);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        }
        return resultEntity;
    }

    /**
     * 编辑
     * @param area
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "area/edit", method = RequestMethod.POST)
    public ResultEntity edit(@Valid Area area,
                             BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "area/list");
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            areaDao.update(area);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        }
        return resultEntity;
    }

    /**
     * 删除
     * @param areaId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "area/del", method = RequestMethod.POST)
    public ResultEntity del(String areaId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "area/list");
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            areaService.delArea(Integer.parseInt(areaId));
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
            resultEntity.setMessage("操作失败");
        } catch (Exception e) {
            logger.equals(e.toString());
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 得到子类列表
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "area/list.json/{id}", method = RequestMethod.GET)
    public ResultEntity listChidrenJson(@PathVariable String id) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "area/list");
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            List<Area> areaList = areaDao.findByAreaParentId(Integer.parseInt(id));
            map.put("areaList", areaList);
            resultEntity.setData(map);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
}
