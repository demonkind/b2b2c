package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.goods.FormatTemplateDao;
import net.shopnc.b2b2c.domain.goods.FormatTemplate;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.FormatTemplateService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 关联板式
 * Created by shopnc.feng on 2015-12-16.
 */
@Controller
public class FormatTemplateJsonAction extends BaseJsonAction {
    @Autowired
    FormatTemplateDao formatTemplateDao;
    @Autowired
    FormatTemplateService formatTemplateService;
    @Autowired
    SecurityHelper securityHelper;

    /**
     * 关联板式保存
     * @param formatTemplate
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "format_template/save.json", method = RequestMethod.POST)
    public ResultEntity save(FormatTemplate formatTemplate,
                             BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.FORMAT_TEMPLATE_LIST);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        formatTemplate.setStoreId(SellerSessionHelper.getStoreId());
        formatTemplate.setFormatContent(securityHelper.xssClean(formatTemplate.getFormatContent()));
        formatTemplateDao.save(formatTemplate);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("保存成功");
        return resultEntity;
    }

    /**
     * 异步调用单条数据
     * @param formatId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "format_template/edit.json", method = RequestMethod.POST)
    public ResultEntity edit(int formatId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.FORMAT_TEMPLATE_LIST);
        FormatTemplate formatTemplate = formatTemplateDao.get(FormatTemplate.class, formatId);
        if (formatTemplate.getStoreId() != SellerSessionHelper.getStoreId()) {
            resultEntity.setData(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setData(formatTemplate);
        return resultEntity;
    }


    /**
     * 关联板式更新
     * @param formatTemplate
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "format_template/update.json", method = RequestMethod.POST)
    public ResultEntity update(FormatTemplate formatTemplate,
                               BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.FORMAT_TEMPLATE_LIST);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        try {
            formatTemplate.setFormatContent(securityHelper.xssClean(formatTemplate.getFormatContent()));
            formatTemplateService.update(formatTemplate, SellerSessionHelper.getStoreId());
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
     * 关联板式删除
     * @param formatId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "format_template/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(int formatId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.FORMAT_TEMPLATE_LIST);
        try {
            formatTemplateService.delete(formatId, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("删除成功");
        return resultEntity;
    }
}
