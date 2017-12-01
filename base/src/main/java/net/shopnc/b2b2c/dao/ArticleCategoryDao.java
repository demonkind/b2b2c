package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.constant.ArticleCategoryType;
import net.shopnc.b2b2c.domain.ArticleCategory;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc on 2015/11/18.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class  ArticleCategoryDao extends BaseDaoHibernate4<ArticleCategory> {
    /**
     * 取得可以添加文章分类列表
     * @return
     */
    public List<ArticleCategory> getArticleCategoryAllowAddArticleList() {
        String hql = "from ArticleCategory where type = " + ArticleCategoryType.THREE + " or type = " + ArticleCategoryType.TWO;
        return super.find(hql);
    }
    /**
     * 根据位置取得文章分类列表
     * @param positionId
     */
    public List<ArticleCategory> getArticleCategoryListByPositionId(int positionId) {
        String hql = "from ArticleCategory where positionId = :positionId order by sort";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("positionId", positionId);
        return super.find(hql, hashMap);
    }

    /**
     * 按排序取得前N条文章分类
     * @param positionId
     * @param pageSize
     * @return
     */
    public List<ArticleCategory> getArticleCategorylistByPositionId(int positionId, int pageSize) {
        String hql = "from ArticleCategory where positionId = :positionId order by sort";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("positionId", positionId);
        return super.findByPage(hql, 1, pageSize, hashMap);
    }

    /**
     * 取得文章分类详情
     * @param positionId
     * @param categoryId
     * @return
     */
    public ArticleCategory getArticleCategoryByPositionIdAndCategoryId(int positionId, int categoryId) {
        String hql = "from ArticleCategory where positionId = :positionId and categoryId = :categoryId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("positionId",positionId);
        hashMap.put("categoryId",categoryId);
        List<ArticleCategory> articleCategoryList = super.find(hql, hashMap);
        if (articleCategoryList.size() > 0) {
            return articleCategoryList.get(0);
        }
        return null;
    }
    /**
     * 取得文章分类详情
     * @param categoryId
     * @return
     */
    public ArticleCategory getArticleCategoryByPositionIdAndCategoryId(int categoryId) {
        String hql = "from ArticleCategory where categoryId = :categoryId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("categoryId",categoryId);
        List<ArticleCategory> articleCategoryList = super.find(hql, hashMap);
        if (articleCategoryList.size() > 0) {
            return articleCategoryList.get(0);
        }
        return null;
    }
}
