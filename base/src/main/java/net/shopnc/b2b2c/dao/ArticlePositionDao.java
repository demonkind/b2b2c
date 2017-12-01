package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.constant.ArticlePositionsAllowAddCategory;
import net.shopnc.b2b2c.domain.ArticlePosition;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 文章出现位置
 * Created by hbj on 2015/11/25.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class ArticlePositionDao extends BaseDaoHibernate4<ArticlePosition> {

    /**
     * 取前譩允许添加分类的位置列表
     * @return
     */
    public List<ArticlePosition> getArticleAllowAddCategoryList() {
        String hql = "from ArticlePosition where allowAddCategory = " + ArticlePositionsAllowAddCategory.YES;
        return super.find(hql);
    }
}
