package net.shopnc.b2b2c.service;

import net.shopnc.b2b2c.constant.ArticleAllowDelete;
import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.dao.ArticleCategoryDao;
import net.shopnc.b2b2c.dao.ArticleDao;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.domain.ArticleCategory;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.vo.ArticleCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章
 * Created by shopnc on 2015/11/19.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleCategoryDao articleCategoryDao;

    /**
     * 删除
     * @param articleId
     * @throws ShopException
     */
    public void deleteArticleByArticleId(int articleId) throws ShopException {
        if (articleId <= 0) {
            throw new ShopException("参数错误");
        }
        Article article = articleDao.get(Article.class,articleId);
        if (article.getAllowDelete() == ArticleAllowDelete.NO) {
            throw new ShopException("该文章不可删除");
        }
        articleDao.delete(Article.class, articleId);
    }

    /**
     * 由位置取得文章列表
     * @param positionId
     * @return
     */
    public List<Article> getArticleListByPositionList(int positionId) {
        List<Integer> articleCagegoryIdList = new ArrayList<Integer>();
        List<ArticleCategory> articleCategoryList = articleCategoryDao.getArticleCategoryListByPositionId(positionId);
        for (int i=0; i<articleCategoryList.size(); i++) {
            articleCagegoryIdList.add(articleCategoryList.get(i).getCategoryId());
        }
        return articleDao.getArticleListByCategoryId(articleCagegoryIdList);
    }

    /**
     * 由位置取得文章列表[取得前pageSize个]
     * @param positionId
     * @param pageSize
     * @return
     */
    public List<Article> getArticleListByPositionList(int positionId, int pageSize) {
        List<Integer> articleCagegoryIdList = new ArrayList<Integer>();
        List<ArticleCategory> articleCategoryList = articleCategoryDao.getArticleCategoryListByPositionId(positionId);
        for (int i=0; i<articleCategoryList.size(); i++) {
            articleCagegoryIdList.add(articleCategoryList.get(i).getCategoryId());
        }
        return articleDao.getArticleListByCategoryId(articleCagegoryIdList,pageSize);
    }

}
