package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.ArticlePositionDao;
import net.shopnc.b2b2c.domain.ArticlePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 文章分类
 * Created by shopnc on 2015/11/18.
 */
@Controller
public class ArticleCategoryAction extends BaseAction {
    @Autowired
    private ArticlePositionDao articlePositionDao;

    /**
     * 文章分类列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "article_category/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        List<ArticlePosition> articlePositionList = articlePositionDao.getArticleAllowAddCategoryList();
        modelMap.put("articlePositionList", articlePositionList);
        return getAdminTemplate("article/article_category/list");
    }
}
