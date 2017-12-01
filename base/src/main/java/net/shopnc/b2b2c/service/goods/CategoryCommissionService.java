package net.shopnc.b2b2c.service.goods;

import net.shopnc.b2b2c.dao.goods.CategoryCommissionDao;
import net.shopnc.b2b2c.dao.goods.CategoryDao;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.domain.goods.CategoryCommission;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.CategoryCommissionAdminListVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2015/01/26.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class CategoryCommissionService extends BaseService {

    @Autowired
    CategoryCommissionDao categoryCommissionDao;

    @Autowired
    CategoryDao categoryDao;

    private List<Integer> categoryList;

    /**
     * 根据categoryId 获取佣金表信息
     *
     * @param categoryId
     * @return
     * @throws Exception
     */
    private CategoryCommission getCategoryCommissionByCategoryId(int categoryId) {
        return categoryCommissionDao.getCategoryCommissionByCategoryId(categoryId);
    }

    /**
     * 后台分类列表
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getCategoryDtGridListForAdmin(String dtGridPager) throws Exception {
        DtGrid dtGrid = categoryDao.getDtGridList(dtGridPager, Category.class);

        List<Object> categorys = dtGrid.getExhibitDatas();
        List<Object> categoryAdminLists = new ArrayList<Object>();
        for (int i = 0; i < categorys.size(); i++) {
            Category category = (Category) categorys.get(i);
            CategoryCommissionAdminListVo categoryCommisRatesAdminList = new CategoryCommissionAdminListVo();

            categoryCommisRatesAdminList.setCategoryId(category.getCategoryId());
            categoryCommisRatesAdminList.setCategoryName(category.getCategoryName());
            categoryCommisRatesAdminList.setDeep(category.getDeep());
            categoryCommisRatesAdminList.setParentId(category.getParentId());
            categoryCommisRatesAdminList.setCategorySort(category.getCategorySort());

            // 父级分类名称
            if (category.getParentId() > 0) {
                Category parent = categoryDao.get(Category.class, category.getParentId());
                categoryCommisRatesAdminList.setParentName(parent.getCategoryName());
            } else {
                categoryCommisRatesAdminList.setParentName("--");
            }
            CategoryCommission categoryCommisRates = getCategoryCommissionByCategoryId(category.getCategoryId());
            categoryCommisRatesAdminList.setRatesId(categoryCommisRates.getCommissionId());
            categoryCommisRatesAdminList.setCommisRate(categoryCommisRates.getCommissionRate());
            // 佣金
            categoryAdminLists.add(categoryCommisRatesAdminList);
        }
        dtGrid.setExhibitDatas(categoryAdminLists);
        return dtGrid;
    }

    /**
     * 添加一个分佣比例
     *
     * @return
     */
    public Boolean addCates(int categoryId) {
        CategoryCommission categoryCommission = new CategoryCommission();
        categoryCommission.setCategoryId(categoryId);
        categoryCommission.setCommissionRate(0);
        categoryCommissionDao.save(categoryCommission);
        return true;
    }

    /**
     * 保存佣金比例
     *
     * @param
     * @param
     * @return
     */
    public Boolean saveCates(CategoryCommission cate, int isRel) {
        //获取单条信息
        CategoryCommission categoryCommisRates = categoryCommissionDao.get(CategoryCommission.class, cate.getCommissionId());
        categoryList = new ArrayList<Integer>();
        if (isRel == 1) {

            //获取全部下级分类
            getCategoryChildId(categoryCommisRates.getCategoryId());
        }
        //添加自身的id
        categoryList.add(categoryCommisRates.getCommissionId());
        categoryCommissionDao.saveRates(cate.getCommissionRate(), categoryList);
        return true;
    }

    /**
     * 根据商品分类id 获取全部下级id
     */
    public void getCategoryChildId(int categoryId) {
        List<Category> categoryList = categoryDao.findByParentId(categoryId);
        if (categoryList.size() == 0) {
            return;
        }
        for (int i = 0; i < categoryList.size(); i++) {
            Category category = categoryList.get(i);
            this.categoryList.add(category.getCategoryId());
            getCategoryChildId(category.getCategoryId());
        }
    }
}
