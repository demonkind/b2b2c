package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.ArticleCategoryDao;
import net.shopnc.b2b2c.domain.ArticleCategory;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.ArticleCategoryService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hou on 2016/3/15.
 */
@Controller
public class ArticleCategoryJsonAction extends BaseJsonAction {
    @Autowired
    private ArticleCategoryDao articleCategoryDao;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    /**
     * 文章分类列表
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article_category/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = articleCategoryService.getArticleCategoryDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 新增保存文章分类
     * @param articleCategory
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article_category/save", method = RequestMethod.POST)
    public ResultEntity save(@Valid ArticleCategory articleCategory,
                             BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "article_category/list");
        if (bindingResult.hasErrors()) {
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
                errorList.add(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                articleCategoryDao.save(articleCategory);
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
     * 更新文章分类
     * @param articleCategory
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article_category/update", method = RequestMethod.POST)
    public ResultEntity update(@Valid ArticleCategory articleCategory,
                               BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "article_category/list");
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                articleCategoryDao.update(articleCategory);
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
     * 删除文章分类
     * @param categoryId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article_category/delete", method = RequestMethod.POST)
    public ResultEntity delete(@RequestParam(value = "categoryId", required = true) int categoryId)  {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "article_category/list");
        try {
            articleCategoryService.deleteArticleCategoryByCategoryId(categoryId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (ShopException e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
}
