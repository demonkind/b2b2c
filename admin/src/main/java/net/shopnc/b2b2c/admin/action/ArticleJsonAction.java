package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.ArticleDao;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.ArticleService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.SecurityHelper;
import net.shopnc.common.util.ShopHelper;
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
 * 文章
 * Created by hou on 2016/3/15.
 */
@Controller
public class ArticleJsonAction extends BaseJsonAction {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SecurityHelper securityHelper;
    /**
     * 文章列表
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = articleDao.getDtGridList(dtGridPager,Article.class);
        } catch (Exception e) {
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 新增文章
     * @param article
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article/save", method = RequestMethod.POST)
    public ResultEntity save(@Valid Article article,
                             BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "article/list");
        if (bindingResult.hasErrors()) {
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                logger.error(objectError.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                article.setContent(securityHelper.xssClean(article.getContent()));
                article.setCreateTime(ShopHelper.getCurrentTimestamp());
                articleDao.save(article);
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
     * 更新文章
     * @param article
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article/update", method = RequestMethod.POST)
    public ResultEntity update(@Valid Article article,
                               BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "article/list");
        if (bindingResult.hasErrors()) {
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                logger.error(objectError.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                article.setContent(securityHelper.xssClean(article.getContent()));
                article.setCreateTime(ShopHelper.getCurrentTimestamp());
                articleDao.update(article);
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
     * 删除文章
     * @param articleId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article/delete", method = RequestMethod.POST)
    public ResultEntity delete(@RequestParam(value = "articleId", required = true) int articleId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "article/list");
        try {
            articleService.deleteArticleByArticleId(articleId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (ShopException e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 编辑文章
     * @param article
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "article/state/edit.json", method = RequestMethod.POST)
    public ResultEntity updateState(@Valid Article article,BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        if (bindingResult.hasErrors()) {
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                logger.error(objectError.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("保存失败");
        } else {
            try {
                Article article1 = articleDao.get(Article.class,article.getArticleId());
                if (article1 == null) {
                    throw new Exception();
                }
                article1.setRecommendState(article.getRecommendState());
                articleDao.update(article1);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("保存成功");
            } catch (Exception e) {
                logger.error(e.toString());
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("保存失败");
            }
        }
        return resultEntity;
    }
}
