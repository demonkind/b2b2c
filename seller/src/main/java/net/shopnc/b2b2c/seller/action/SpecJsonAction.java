package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.goods.SpecDao;
import net.shopnc.b2b2c.dao.goods.SpecValueDao;
import net.shopnc.b2b2c.domain.goods.Spec;
import net.shopnc.b2b2c.domain.goods.SpecValue;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.SpecService;
import net.shopnc.b2b2c.vo.SpecAndValueVo;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 商品规格
 * Created by shopnc.feng on 2015-12-07.
 */
@Controller
public class SpecJsonAction extends BaseJsonAction {
    @Autowired
    SpecService specService;
    @Autowired
    SpecDao specDao;
    @Autowired
    SpecValueDao specValueDao;

    /**
     * 规格保存
     * @param specName
     * @param specValueName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/save.json", method = RequestMethod.POST)
    public ResultEntity save(String specName,
                             String[] specValueName) {
        ResultEntity resultEntity = new ResultEntity();

        SpecAndValueVo SpecAndValueVo = null;
        try {
            SpecAndValueVo = specService.saveStoreSpec(specName, specValueName, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setData(SpecAndValueVo);

        return resultEntity;
    }

    /**
     * 规格值保存
     * @param specId
     * @param specValueName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/value/save.json", method = RequestMethod.POST)
    public ResultEntity saveJson(int specId,
                             String specValueName) {
        ResultEntity resultEntity = new ResultEntity();

        Spec spec = specDao.get(Spec.class, specId);
        if (spec.getStoreId() != SellerSessionHelper.getStoreId()) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        Serializable integerList = null;
        try {
            integerList = specService.saveSpecValue(specId, specValueName);
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setData(integerList);

        return resultEntity;
    }

    /**
     * 编辑规格
     * @param specId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/edit.json", method = RequestMethod.POST)
    public ResultEntity editJson(int specId) {
        ResultEntity resultEntity = new ResultEntity();
        SpecAndValueVo specAndValueVo;
        try {
            specAndValueVo = specService.getSpecAndValueVoBySpecId(specId, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setData(specAndValueVo);
        return resultEntity;
    }

    /**
     * 删除规格
     * @param specId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/delete.json", method = RequestMethod.POST)
    public ResultEntity deleteJson(int specId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            specService.deleteBySpecId(specId, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("删除成功");
        return resultEntity;
    }

    /**
     * 更新规格名称
     * @param spec
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/update/spec_name.json", method = RequestMethod.POST)
    public ResultEntity updateSpecName(Spec spec,
                                       BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();

        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }

        try {
            specService.updateSpecName(spec, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("编辑成功");
        return resultEntity;
    }

    /**
     * 规格规格值名称
     * @param specValue
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/update/spec_value_name.json", method = RequestMethod.POST)
    public ResultEntity updateSpecValueName(SpecValue specValue,
                                            BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();

        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }

        try {
            specService.updateSpecValueName(specValue, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("编辑成功");
        return resultEntity;
    }

    /**
     * 保存规格值名称
     * @param specValue
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/save/spec_value_name.json", method = RequestMethod.POST)
    public ResultEntity saveSpecValueName(SpecValue specValue,
                                          BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();

        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        try {
            Serializable specValueId = specService.saveSpecValue(SellerSessionHelper.getStoreId(), specValue);
            SpecValue specValue1 = specValueDao.get(SpecValue.class, specValueId);
            resultEntity.setData(specValue1);
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("添加成功");
        return resultEntity;
    }

    /**
     * 删除规格值
     * @param specValueId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "spec/delete/spec_value.json", method = RequestMethod.POST)
    public ResultEntity deleteSpecValue(int specValueId) {
        ResultEntity resultEntity = new ResultEntity();

        try {
            specService.deleteSpecValueBySpecValueId(specValueId, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("删除成功");
        return resultEntity;
    }
}
