package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.UrlAdmin;
import net.shopnc.b2b2c.dao.goods.BrandLabelDao;
import net.shopnc.b2b2c.domain.goods.BrandLabel;
import net.shopnc.b2b2c.service.goods.BrandLabelService;
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
 * 品牌标签
 * Created by shopnc.feng on 2015-11-03.
 */
@Controller
public class BrandLabelJsonAction extends BaseJsonAction {
    @Autowired
    BrandLabelDao brandLabelDao;
    @Autowired
    BrandLabelService brandLabelService;

    /**
     * 品牌标签列表数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand_label/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager){
        try {
            return brandLabelDao.getDtGridList(dtGridPager, BrandLabel.class);
        } catch (Exception e) {
            return new DtGrid();
        }
    }

    /**
     * 品牌标签添加
     *
     * @param brandLabel
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand_label/save.json", method = RequestMethod.POST)
    public ResultEntity save(@Valid BrandLabel brandLabel,
                             BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.BRAND_LABEL_LIST);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }

            resultEntity.setMessage("错误消息");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }

        brandLabelDao.save(brandLabel);

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("操作成功");
        return resultEntity;
    }

    /**
     * 编辑品牌标签
     *
     * @param brandLabel
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand_label/update.json", method = RequestMethod.POST)
    public ResultEntity update(@Valid BrandLabel brandLabel,
                               BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.BRAND_LABEL_LIST);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }

            resultEntity.setMessage("错误消息");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }

        brandLabelDao.update(brandLabel);

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("操作成功");
        return resultEntity;
    }

    /**
     * 品牌标签删除
     * @param brandLabelId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand_label/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(@RequestParam(value = "brandLabelId") int brandLabelId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.BRAND_LABEL_LIST);
        if (brandLabelId > 0) {
            brandLabelService.deleteByBrandLabelId(brandLabelId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } else {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
}
