package net.shopnc.b2b2c.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shopnc.b2b2c.constant.ArticleAllowDelete;
import net.shopnc.b2b2c.constant.ArticleCategoryType;
import net.shopnc.b2b2c.dao.ArticleCategoryDao;
import net.shopnc.b2b2c.dao.ArticlePositionDao;
import net.shopnc.b2b2c.domain.ArticleCategory;
import net.shopnc.b2b2c.domain.ArticlePosition;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.vo.ArticleCategoryVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.entity.dtgrid.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文章分类
 * Created by shopnc on 2015/11/19.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ArticleCategoryService {
    @Autowired
    private ArticleCategoryDao articleCategoryDao;
    @Autowired
    private ArticlePositionDao articlePositionDao;

    /**
     * 根据主键删除文章分类
     * @param categoryId
     * @return
     */
    public void deleteArticleCategoryByCategoryId(int categoryId) throws ShopException {
        if (categoryId <= 0) {
            throw new ShopException("参数错误");
        }
        ArticleCategory articleCategory = articleCategoryDao.get(ArticleCategory.class,categoryId);
        if (articleCategory.getType() == ArticleCategoryType.ONE || articleCategory.getType() == ArticleCategoryType.TWO) {
            throw new ShopException("该分类不可删除");
        }
        articleCategoryDao.delete(ArticleCategory.class, categoryId);
    }

    /**
     * 取得文章分类列表
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getArticleCategoryDtGridList(String dtGridPager) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
            for (String key : dtGrid.getNcColumnsType().keySet()) {
                for (int i = 0; i< dtGrid.getNcColumnsType().get(key).size(); i++) {
                    hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                }
                dtGrid.setNcColumnsTypeList(hashMap);
            }
        }
        QueryUtils.parseDtGridHql(dtGrid);
        dtGrid = articleCategoryDao.getDtGridList(dtGrid,ArticleCategory.class);

        List<Object> exhibitDatas = dtGrid.getExhibitDatas();
        List<Object> articleCategoryVoList = new ArrayList<Object>();
        for (int i = 0; i < exhibitDatas.size(); i++) {
            ArticleCategory articleCategory = (ArticleCategory)exhibitDatas.get(i);
            ArticleCategoryVo articleCategoryVo = new ArticleCategoryVo();
            articleCategoryVo.setCategoryId(articleCategory.getCategoryId());
            articleCategoryVo.setSort(articleCategory.getSort());
            articleCategoryVo.setTitle(articleCategory.getTitle());
            articleCategoryVo.setPositionId(articleCategory.getPositionId());
            articleCategoryVo.setType(articleCategory.getType());
            if (articleCategory.getPositionId() > 0) {
                ArticlePosition articlePosition = articlePositionDao.get(ArticlePosition.class, articleCategory.getPositionId());
                if (articlePosition != null) {
                    articleCategoryVo.setPositionTitle(articlePosition.getPositionTitle());
                }
            }
            articleCategoryVoList.add(articleCategoryVo);
        }
        dtGrid.setExhibitDatas(articleCategoryVoList);
        return dtGrid;
    }
}
