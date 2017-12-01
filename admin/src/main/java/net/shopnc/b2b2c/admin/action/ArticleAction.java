package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.ArticleCategoryDao;
import net.shopnc.b2b2c.domain.ArticleCategory;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

/**
 * 文章
 * Created by shopnc on 2015/11/19.
 */
@Controller
public class ArticleAction extends BaseAction {
    @Autowired
    private ArticleCategoryDao articleCategoryDao;
    /**
     * 文章列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "article/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        List<ArticleCategory> articleCategoryList = articleCategoryDao.getArticleCategoryAllowAddArticleList();
        modelMap.put("articleCategoryList", articleCategoryList);
        HashMap<Integer,String> hashMap = new HashMap<Integer, String>();
        List<ArticleCategory> articleCategoryList1 = articleCategoryDao.findAll(ArticleCategory.class);
        for(int i = 0; i < articleCategoryList1.size(); i++) {
            hashMap.put(articleCategoryList1.get(i).getCategoryId(),articleCategoryList1.get(i).getTitle());
        }
        modelMap.put("articleCategoryJson",JsonHelper.toJson(hashMap));
        return getAdminTemplate("article/article/list");
    }


}