package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.UrlAdmin;
import net.shopnc.b2b2c.domain.goods.CategoryCommission;
import net.shopnc.b2b2c.service.goods.CategoryCommissionService;
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

/**
 * 分类分佣json相关
 * Created by shopnc.cj on 2015-11-02.
 */
@Controller
public class CategoryCommissionJsonAction extends BaseJsonAction {

    @Autowired
    private CategoryCommissionService categoryCommissionService;

    /**
     * 分类列表JSON数据
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "rates/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        try {
            DtGrid dtGrid = categoryCommissionService.getCategoryDtGridListForAdmin(dtGridPager);
            return dtGrid;
        } catch (Exception e) {
            return new DtGrid();
        }
    }

    /**
     * 保存
     * @param categoryCommission 分佣实体
     * @param isRel 是否关联到下级
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "rates/save.json", method = RequestMethod.POST)
    public ResultEntity save(
            CategoryCommission categoryCommission,
            @RequestParam(value = "isRel", required = false) int isRel,
            BindingResult bindingResult
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
        categoryCommissionService.saveCates(categoryCommission, isRel);
        resultEntity.setMessage("添加成功");
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
}
