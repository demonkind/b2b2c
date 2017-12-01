package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.domain.Article;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文章
 * Created by shopnc on 2015/11/19.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class ArticleDao extends BaseDaoHibernate4<Article> {

    /**
     * 由分类ID List取得文章列表
     * @param articleCagegoryIdList
     * @return
     */
    public List<Article> getArticleListByCategoryId(List<Integer> articleCagegoryIdList) {
        if (articleCagegoryIdList.size() > 0) {
            String hql = "from Article where categoryId in(:articleCagegoryIdList) order by recommendState desc,articleId desc";
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            hashMap.put("articleCagegoryIdList",articleCagegoryIdList);
            return super.find(hql, hashMap);
        } else {
            return new ArrayList<>();
        }

    }

    /**
     * 由分类ID List取得文章列表
     * @param articleCagegoryIdList
     * @param pageSize
     * @return
     */
    public List<Article> getArticleListByCategoryId(List<Integer> articleCagegoryIdList, int pageSize) {
        if (articleCagegoryIdList.size() > 0) {
            String hql = "from Article where categoryId in(:articleCagegoryIdList) order by recommendState desc,articleId desc";
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            hashMap.put("articleCagegoryIdList",articleCagegoryIdList);
            return super.findByPage(hql, 1, pageSize, hashMap);
        } else {
            return new ArrayList<>();
        }

    }

    /**
     *
     * @param categoryId
     * @param pageSize
     * @return
     */
    public List<Article> getArticleListByCategoryId(int categoryId, int pageNo, int pageSize) {
        String hql = "from Article where categoryId = :categoryId order by recommendState desc,articleId desc";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("categoryId",categoryId);
        return super.findByPage(hql, pageNo, pageSize, hashMap);
    }

    /**
     * 取得记录数量
     * @param categoryId
     * @return
     */
    public long getArticleCountByCategoryId(int categoryId) {
        String hql = "select count(*) from Article where categoryId = :categoryId";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("categoryId",categoryId);
        return super.findCount(hql, hashMap);
    }

    /**
     * 取得上一条
     * @param categoryId
     * @param articleId
     * @return
     */
    public Article getPrevArticle(int categoryId, int articleId) {
        String hql = "from Article where categoryId = :categoryId and articleId <:articleId order by articleId desc";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("articleId",articleId);
        hashMap.put("categoryId",categoryId);
        List<Article> articleList = super.findByPage(hql,1,1,hashMap);
        return articleList.size() > 0 ? articleList.get(0) : null;
    }

    /**
     * 取得下一条
     * @param categoryId
     * @param articleId
     * @return
     */
    public Article getNextArticle(int categoryId, int articleId) {
        String hql = "from Article where categoryId = :categoryId and articleId >:articleId order by articleId asc";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("articleId", articleId);
        hashMap.put("categoryId",categoryId);
        List<Article> articleList = super.findByPage(hql,1,1,hashMap);
        return articleList.size() > 0 ? articleList.get(0) : null;
    }
}
