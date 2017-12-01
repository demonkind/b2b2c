package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.UrlAdmin;
import net.shopnc.b2b2c.dao.goods.*;
import net.shopnc.b2b2c.domain.goods.*;
import net.shopnc.b2b2c.service.goods.AttributeService;
import net.shopnc.b2b2c.service.goods.CategoryService;
import net.shopnc.b2b2c.vo.AttributeAndValueVo;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Controller
public class CategoryJsonAction extends BaseJsonAction {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BrandDao brandDao;
    @Autowired
    AttributeDao attributeDao;
    @Autowired
    AttributeValueDao attributeValueDao;
    @Autowired
    AttributeService attributeService;
    @Autowired
    CustomDao customDao;

    /**
     * 分类三级联动接口
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/list.json/{parentId}", method = RequestMethod.GET)
    public ResultEntity listJson(@PathVariable int parentId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            List<Category> categoryList = categoryDao.findByParentId(parentId);
            map.put("categoryList", categoryList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(map);
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            logger.error(e.toString());
            return resultEntity;
        }
        return resultEntity;
    }

    @ResponseBody
    @RequestMapping(value = "category/brand_all.json", method = RequestMethod.GET)
    public ResultEntity brandAll() {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);

        List<Brand> brands = brandDao.findAll(Brand.class);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("brands", brands);
        resultEntity.setData(map);
        return resultEntity;
    }

    /**
     * 分类列表JSON数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "category/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        try {
            DtGrid dtGrid = categoryService.getCategoryDtGridListForAdmin(dtGridPager);
            return dtGrid;
        } catch (Exception e) {
            return new DtGrid();
        }
    }

    /**
     * 商品分类保存
     * @param category
     * @param bindingResult
     * @param brandIds
     * @param attributes
     * @param customValues
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/save.json", method = RequestMethod.POST)
    public ResultEntity save(Category category,
                       BindingResult bindingResult,
                       @RequestParam(value = "brandId", required = false) int[] brandIds,
                       @RequestParam(value = "attribute", required = false) String[] attributes,
                       @RequestParam(value = "custom", required = false) String[] customValues
                       ) {
        ResultEntity resultEntity = new ResultEntity();
        // 定义返回结果对象
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("错误消息");
            return resultEntity;
        }
        categoryService.saveCategoryForAdmin(category, brandIds, attributes, customValues);
        resultEntity.setMessage("添加成功");
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 删除分类
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(@RequestParam(value = "categoryId") int categoryId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);
        if (categoryId > 0) {
            categoryService.deleteByCategoryId(categoryId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } else {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 编辑分类基本信息
     * @param category
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/update.json", method = RequestMethod.POST)
    public ResultEntity update(Category category,
                               BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();

        // 定义返回结果对象
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("错误消息");
            return resultEntity;
        }
        categoryService.updateCategory(category);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("编辑成功");
        return resultEntity;
    }

    /**
     * 更新分类绑定品牌
     * @param categoryId
     * @param brandIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/brand/update.json", method = RequestMethod.POST)
    public ResultEntity brandUpdate(@RequestParam(value = "categoryId") int categoryId,
                                    @RequestParam(value = "brandId", required = false) int[] brandIds) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        categoryService.updateCategoryBrand(categoryId, brandIds);

        return resultEntity;
    }

    /**
     * 某分类编号下的全部属性数据
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute/list.json/{categoryId}", method = RequestMethod.GET)
    public ResultEntity attributeList(@PathVariable int categoryId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        List<AttributeAndValueVo> attributeAndValueVos = attributeService.getAttributeAndValueVoByCategoryId(categoryId);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("attributeAndValueVos",attributeAndValueVos);
        resultEntity.setData(map);

        return resultEntity;
    }

    /**
     * 更新属性排序
     * @param attributeId
     * @param attributeSort
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute/sort/update.json", method = RequestMethod.POST)
    public ResultEntity attributeUpdate(@RequestParam(value = "attributeId") int attributeId,
                                        @RequestParam(value = "attributeSort") int attributeSort) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        Attribute attribute = attributeDao.get(Attribute.class, attributeId);
        attribute.setAttributeSort(attributeSort);
        attributeDao.update(attribute);

        return resultEntity;
    }

    /**
     * 更新品牌名称
     * @param attributeId
     * @param attributeName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute/name/update.json", method = RequestMethod.POST)
    public ResultEntity attributeUpdate(@RequestParam(value = "attributeId") int attributeId,
                                        @RequestParam(value = "attributeName") String attributeName) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        Attribute attribute = attributeDao.get(Attribute.class, attributeId);
        attribute.setAttributeName(attributeName);
        attributeDao.update(attribute);

        return resultEntity;
    }

    /**
     * 更新品牌名称
     * @param attributeId
     * @param isShow
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute/is_show/update.json", method = RequestMethod.POST)
    public ResultEntity attributeIsShowUpdate(@RequestParam(value = "attributeId") int attributeId,
                                        @RequestParam(value = "isShow") int isShow) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        Attribute attribute = attributeDao.get(Attribute.class, attributeId);
        attribute.setIsShow(isShow);
        attributeDao.update(attribute);

        return resultEntity;
    }

    /**
     * 根据属性编号删除属性及属性值
     * @param attributeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute/delete.json", method = RequestMethod.POST)
    public ResultEntity attributeDelete(@RequestParam(value = "attributeId") int attributeId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        attributeService.deleteByAttributeId(attributeId);

        return resultEntity;
    }

    /**
     * 新增属性及自定义属性
     * @param categoryId
     * @param attributes
     * @param customValues
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute_and_custom/save.json", method = RequestMethod.POST)
    public ResultEntity attributeAndCustomSave(@RequestParam(value = "categoryId") int categoryId,
                             @RequestParam(value = "attribute", required = false) String[] attributes,
                             @RequestParam(value = "custom", required = false) String[] customValues
    ) {
        ResultEntity resultEntity = new ResultEntity();
        // 定义返回结果对象
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);
        categoryService.saveAttributeAndCustom(categoryId, attributes, customValues);
        resultEntity.setMessage("添加成功");
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 某属性编号下的全部属性值
     * @param attributeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute_value/list.json/{attributeId}", method = RequestMethod.GET)
    public ResultEntity attributeValueList(@PathVariable int attributeId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        List<AttributeValue> attributeValues = attributeValueDao.findByAttributeId(attributeId);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("attributeValues", attributeValues);
        resultEntity.setData(map);

        return resultEntity;
    }

    /**
     * 根据属性值编号更新属性值名称
     * @param attributeValueId
     * @param attributeValueName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute_value/name/update.json", method = RequestMethod.POST)
    public ResultEntity attributeValueUpdate(@RequestParam(value = "attributeValueId") int attributeValueId,
                                             @RequestParam(value = "attributeValueName") String attributeValueName) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        AttributeValue attributeValue = attributeValueDao.get(AttributeValue.class, attributeValueId);
        attributeValue.setAttributeValueName(attributeValueName);
        attributeValueDao.update(attributeValue);

        return resultEntity;
    }

    /**
     * 根据属性值编号删除属性值
     * @param attributeValueId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute_value/delete.json", method = RequestMethod.POST)
    public ResultEntity attributeValueDelete(@RequestParam(value = "attributeValueId") int attributeValueId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        attributeValueDao.delete(AttributeValue.class, attributeValueId);

        return resultEntity;
    }

    /**
     * 新增属性值保存
     * @param attributeId
     * @param attributeValueNames
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/attribute_value/save.json", method = RequestMethod.POST)
    public ResultEntity attributeValueSave(@RequestParam(value = "attributeId") int attributeId,
                                           @RequestParam(value = "attributeValueNames") String[] attributeValueNames) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        attributeService.saveAttributeValues(attributeId, attributeValueNames);

        return resultEntity;
    }

    /**
     * 某属性编号下的全部属性值
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/custom/list.json/{categoryId}", method = RequestMethod.GET)
    public ResultEntity customList(@PathVariable int categoryId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        List<Custom> customs = customDao.findByCategoryId(categoryId);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("customs", customs);
        resultEntity.setData(map);

        return resultEntity;
    }

    /**
     * 更新自定义属性
     * @param customId
     * @param customName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/custom_name/update.json", method = RequestMethod.POST)
    public ResultEntity customNameUpdate(@RequestParam(value = "customId") int customId,
                                         @RequestParam(value = "customName") String customName) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        Custom custom = customDao.get(Custom.class, customId);
        custom.setCustomName(customName);
        customDao.update(custom);

        return resultEntity;
    }

    /**
     * 删除自定义属性
     * @param customId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/custom/delete.json", method = RequestMethod.POST)
    public ResultEntity customDelete(@RequestParam(value = "customId") int customId) {

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.CATEGORY_LIST);

        customDao.delete(Custom.class, customId);

        return resultEntity;
    }
}
