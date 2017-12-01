package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.dao.ArticleCategoryDao;
import net.shopnc.b2b2c.dao.ArticleDao;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.domain.ArticleCategory;
import net.shopnc.b2b2c.service.ArticleService;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.common.entity.PageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章
 * Created by hbj on 2015/12/8.
 */
@Controller
public class HomeArticleAction extends HomeBaseAction {
    private final static int NEWTOPSIZE = 10;
    @Autowired
    private ArticleCategoryDao articleCategoryDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleService articleService;

    /**
     * 页面显示当前位置
     */
    public HomeArticleAction() {
        List<CrumbsVo> crumbsVoList = new ArrayList<CrumbsVo>();
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName("文章");
        crumbsVoList.add(crumbsVo);
        setCrumbsList(crumbsVoList);
    }

    /**
     * 显示分类下的文章列表
     * @param categoryId
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "article/list/{categoryId}", method = RequestMethod.GET)
    public String list(
            @PathVariable int categoryId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            ModelMap modelMap) {
        //全部分类列表
        List<ArticleCategory> articleCategoryList = articleCategoryDao.getArticleCategoryListByPositionId(ArticlePositions.DEFAULT_LIST);
        modelMap.put("articleCategoryList", articleCategoryList);

        //最新文章
        List<Article> articleNewList = articleService.getArticleListByPositionList(ArticlePositions.DEFAULT_LIST, NEWTOPSIZE);
        modelMap.put("articleNewList", articleNewList);

        //本分类信息
        ArticleCategory articleCategory = articleCategoryDao.getArticleCategoryByPositionIdAndCategoryId(ArticlePositions.DEFAULT_LIST, categoryId);
        if (articleCategory != null) {
            //本分类下文章列表
            PageEntity pageEntity = new PageEntity();
            pageEntity.setTotal(articleDao.getArticleCountByCategoryId(categoryId));
            pageEntity.setPageNo(page);
            List<Article> articleList = articleDao.getArticleListByCategoryId(categoryId,pageEntity.getPageNo(),pageEntity.getPageSize());

            modelMap.put("articleCategory", articleCategory);
            modelMap.put("articleList",articleList);
            modelMap.put("showPage",pageEntity.getPageHtml());
            return getHomeTemplate("article_list");
        } else {
            return "redirect:/404";
        }

    }

    /**
     * 文章详情
     * @param articleId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "article/info/{articleId}", method = RequestMethod.GET)
    public String info(@PathVariable int articleId, ModelMap modelMap) {
        String path;

        //全部分类列表
        List<ArticleCategory> articleCategoryList = articleCategoryDao.getArticleCategoryListByPositionId(ArticlePositions.DEFAULT_LIST);
        modelMap.put("articleCategoryList", articleCategoryList);

        //最新文章
        List<Article> articleNewList = articleService.getArticleListByPositionList(ArticlePositions.DEFAULT_LIST, NEWTOPSIZE);
        modelMap.put("articleNewList",articleNewList);
        Article article = articleDao.get(Article.class, articleId);
        if (article != null) {
            //本分类信息
            ArticleCategory articleCategory = articleCategoryDao.getArticleCategoryByPositionIdAndCategoryId(article.getCategoryId());
            if (articleCategory == null) {
                path = "redirect:/404";
            } else {
                //上下一条
                Article articlePrev = articleDao.getPrevArticle(article.getCategoryId(),articleId);
                Article articleNext = articleDao.getNextArticle(article.getCategoryId(),articleId);
                modelMap.put("article",article);
                modelMap.put("articlePrev",articlePrev);
                modelMap.put("articleNext",articleNext);
                path = getHomeTemplate("article_info");
            }
        }else {
            path = "redirect:/404";
        }
        return path;
    }
}
