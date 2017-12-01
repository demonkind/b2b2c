package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.UrlAdmin;
import net.shopnc.b2b2c.dao.goods.SpecDao;
import net.shopnc.b2b2c.dao.goods.SpecValueDao;
import net.shopnc.b2b2c.domain.goods.Spec;
import net.shopnc.b2b2c.domain.goods.SpecValue;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.goods.SpecService;
import net.shopnc.b2b2c.vo.SpecAndValueVo;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 规格管理
 * Created by shopnc.feng on 2015-11-02.
 */
@Controller
public class SpecJsonAction extends BaseJsonAction {
    @Autowired
    SpecDao specDao;
    @Autowired
    SpecService specService;
    @Autowired
    SpecValueDao specValueDao;

    /**
     * 规格列表JSON数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "spec/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        try {
            return specService.getSpecDtGridListForAdmin(dtGridPager);
        } catch (Exception e) {
            return new DtGrid();
        }
    }

    /**
     * 规格保存
     * @param spec
     * @param bindingResult
     * @param specValueNames
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/save.json", method = RequestMethod.POST)
    public ResultEntity save(@Valid Spec spec,
                             BindingResult bindingResult,
                             @RequestParam(value = "specValueNames", required = false) String[] specValueNames) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.SPEC_LIST);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setMessage("操作失败");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }

        try {
            specService.saveSpec(spec, specValueNames);
            resultEntity.setMessage("操作成功");
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }

        return resultEntity;
    }

    /**
     * 规格删除
     * @param specId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(@RequestParam(value = "specId") int specId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.SPEC_LIST);
        if (specId > 0) {
            specService.deleteBySpecId(specId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } else {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        return resultEntity;
    }

    /**
     * 更新规格信息
     * @param spec
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/update.json", method = RequestMethod.POST)
    public ResultEntity update(Spec spec,
                               BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.SPEC_LIST);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        specDao.update(spec);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 规格详细信息
     * @param specId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/info_value.json/{specId}", method = RequestMethod.GET)
    public ResultEntity infoValueJson(@PathVariable int specId) {
        ResultEntity resultEntity = new ResultEntity();
        HashMap<String, Object> map = new HashMap<String, Object>();
        resultEntity.setCode(ResultEntity.SUCCESS);
        SpecAndValueVo specAndValue = specService.getSpecAndValue(specId);
        map.put("specAndValue", specAndValue);
        resultEntity.setData(map);
        return resultEntity;
    }

    /**
     * 某规格下的全部规格值数据
     * @param specId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/values.json/{specId}", method = RequestMethod.GET)
    public ResultEntity valuesJson(@PathVariable int specId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<SpecValue> specValues = specValueDao.findBySpecId(specId);
        map.put("specValues", specValues);
        resultEntity.setData(map);
        return resultEntity;
    }

    /**
     * 更新规格值
     * @param specValueName
     * @param specValueId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/value/update.json", method = RequestMethod.POST)
    public ResultEntity valueUpdate(@RequestParam(value = "specValueName") String specValueName,
                                    @RequestParam(value = "specValueId") int specValueId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.SPEC_LIST);
        resultEntity.setCode(ResultEntity.SUCCESS);
        SpecValue specValue = specValueDao.get(SpecValue.class, specValueId);
        specValue.setSpecValueName(specValueName);
        specValueDao.update(specValue);
        return resultEntity;
    }

    /**
     * 删除规格值
     * @param specValueId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/value/delete.json", method = RequestMethod.POST)
    public ResultEntity valueDelete(@RequestParam(value = "specValueId") int specValueId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.SPEC_LIST);
        resultEntity.setCode(ResultEntity.SUCCESS);
        specValueDao.delete(SpecValue.class, specValueId);
        return resultEntity;
    }

    /**
     * 保存规格值
     * @param specId
     * @param specValueNames
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/value/save.json", method = RequestMethod.POST)
    public ResultEntity valueSave(@RequestParam(value = "specId") int specId,
                                  @RequestParam(value = "specValueNames") String[] specValueNames) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.SPEC_LIST);
        try {
            specService.saveSpecValue(specId, specValueNames);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
}
