package net.shopnc.b2b2c.service.goods;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.constant.CacheKey;
import net.shopnc.b2b2c.dao.goods.*;
import net.shopnc.b2b2c.domain.goods.*;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.CategoryAdminListVo;
import net.shopnc.b2b2c.vo.CategoryNavVo;
import net.shopnc.b2b2c.vo.goods.CategoryCommissionVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.CacheHelper;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-12.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class CategoryService extends BaseService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryBrandDao categoryBrandDao;
    @Autowired
    AttributeDao attributeDao;
    @Autowired
    AttributeService attributeService;
    @Autowired
    CustomDao customDao;
    @Autowired
    CustomService customService;
    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryCommissionDao categoryCommissionDao;
    @Autowired
    CacheHelper cacheHelper;
    @Autowired
    CategoryCommissionService categoryCommissionService;

    /**
     * 根据分类编号取得全部绑定品牌编号
     *
     * @param categoryId
     * @return
     */
    private String getBrandIdsByCategoryId(int categoryId) {
        List<Brand> brands = brandDao.findByCategoryId(categoryId);
        if (brands.size() == 0) {
            return "-";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < brands.size(); i++) {
            if (i > 0) {
                stringBuilder.append(',');
            }
            stringBuilder.append(brands.get(i).getBrandId());
        }
        return stringBuilder.toString();
    }

    /**
     * 根据分类编号取得全部绑定品牌的名称
     *
     * @param categoryId
     * @return
     */
    private String getBrandNamesByCategoryId(int categoryId) {
        List<Brand> brands = brandDao.findByCategoryId(categoryId);
        if (brands.size() == 0) {
            return "-";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < brands.size(); i++) {
            if (i > 0) {
                stringBuilder.append('，');
            }
            stringBuilder.append(brands.get(i).getBrandName());
        }
        return stringBuilder.toString();
    }

    /**
     * 根据属性编号取得全部绑定品牌的名称
     *
     * @param categoryId
     * @return
     */
    private String getAttributeNamesByCategoryId(int categoryId) {
        List<Attribute> attributes = attributeDao.findByCategoryId(categoryId);
        if (attributes.size() == 0) {
            return "-";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < attributes.size(); i++) {
            if (i > 0) {
                stringBuilder.append('，');
            }
            stringBuilder.append(attributes.get(i).getAttributeName());
        }
        return stringBuilder.toString();
    }

    /**
     * 根据自定义属性编号取得全部绑定品牌的名称
     *
     * @param categoryId
     * @return
     */
    private String getCustomNamesByCategoryId(int categoryId) {
        List<Custom> customs = customDao.findByCategoryId(categoryId);
        if (customs.size() == 0) {
            return "-";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < customs.size(); i++) {
            if (i > 0) {
                stringBuilder.append('，');
            }
            stringBuilder.append(customs.get(i).getCustomName());
        }
        return stringBuilder.toString();
    }

    /**
     * 保存分类以及相关数据1
     *
     * @param category
     * @param brandIds
     * @param attributes
     * @param customValues
     * @return
     */
    public Boolean saveCategoryForAdmin(Category category, int[] brandIds, String[] attributes, String[] customValues) {
        // 删除商品分类缓存
        cacheHelper.del(CacheKey.CATEGORY_LIST);

        int categoryId = saveCategory(category);

        saveCategoryBrand(categoryId, brandIds);

        attributeService.saveAttributes(categoryId, attributes);

        customService.saveCustom(categoryId, customValues);
        // bycj [ 添加一个分类的佣金比例 ]
        categoryCommissionService.addCates(categoryId);

        return true;
    }

    /**
     * 保存属于与自定义属性
     *
     * @param categoryId
     * @param attributes
     * @param customValues
     * @return
     */
    public Boolean saveAttributeAndCustom(int categoryId, String[] attributes, String[] customValues) {
        attributeService.saveAttributes(categoryId, attributes);

        customService.saveCustom(categoryId, customValues);

        return true;
    }

    /**
     * 保存商品分类
     *
     * @param category
     * @return
     */
    public int saveCategory(Category category) {
        return (Integer) categoryDao.save(category);
    }

    /**
     * 保存商品分类
     *
     * @param category
     * @return
     */
    public void updateCategory(Category category) {
        // 删除商品分类缓存
        cacheHelper.del(CacheKey.CATEGORY_LIST);

        categoryDao.update(category);
    }

    /**
     * 保存商品分类与品牌关系
     *
     * @param categoryId
     * @param brandIds
     * @return
     */
    public Boolean saveCategoryBrand(int categoryId, int[] brandIds) {
        if (brandIds == null) {
            return true;
        }
        List<CategoryBrand> categoryBrands = new ArrayList<CategoryBrand>();
        for (int brandId : brandIds) {
            CategoryBrand categoryBrand = new CategoryBrand();
            categoryBrand.setCategoryId(categoryId);
            categoryBrand.setBrandId(brandId);
            categoryBrands.add(categoryBrand);
        }
        categoryBrandDao.saveAll(categoryBrands);
        return true;
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
            CategoryAdminListVo categoryAdminList = new CategoryAdminListVo();
            categoryAdminList.setCategoryId(category.getCategoryId());
            categoryAdminList.setCategoryName(category.getCategoryName());
            categoryAdminList.setCategorySort(category.getCategorySort());
            categoryAdminList.setDeep(category.getDeep());
            categoryAdminList.setParentId(category.getParentId());
            // 父级分类名称
            if (category.getParentId() > 0) {
                Category parent = categoryDao.get(Category.class, category.getParentId());
                categoryAdminList.setParentName(parent.getCategoryName());
            } else {
                categoryAdminList.setParentName("--");
            }
            // 品牌编号
            categoryAdminList.setBrandIds(getBrandIdsByCategoryId(category.getCategoryId()));
            // 品牌名称
            categoryAdminList.setBrandNames(getBrandNamesByCategoryId(category.getCategoryId()));
            // 属性名称
            categoryAdminList.setAttributeNames(getAttributeNamesByCategoryId(category.getCategoryId()));
            // 自定义属性名称
            categoryAdminList.setCustomsNames(getCustomNamesByCategoryId(category.getCategoryId()));
            categoryAdminLists.add(categoryAdminList);
        }
        dtGrid.setExhibitDatas(categoryAdminLists);
        return dtGrid;
    }

    /**
     * 更新分类与品牌关系
     *
     * @param categoryId
     * @param brandIds
     * @return
     */
    public Boolean updateCategoryBrand(int categoryId, int[] brandIds) {
        // 删除分类与品牌关系
        categoryBrandDao.deleteByCategoryId(categoryId);
        saveCategoryBrand(categoryId, brandIds);
        return true;
    }

    /**
     * 根据分类编号删除自定义属性
     *
     * @param categoryId
     * @return
     */
    public Boolean deleteByCategoryId(int categoryId) {
        // 删除商品分类缓存
        cacheHelper.del(CacheKey.CATEGORY_LIST);
        // 删除分类
        categoryDao.delete(Category.class, categoryId);
        // 删除分类与品牌关系
        categoryBrandDao.deleteByCategoryId(categoryId);
        // 删除属性
        attributeService.deleteByCategoryId(categoryId);
        // 自定义属性
        customDao.deleteByCategoryId(categoryId);
        return true;
    }

    /**
     * 由父类取得商品分类列表[带佣金]
     *
     * @param parentId
     * @return
     */
    public List<CategoryCommissionVo> getCategoryCommissionVoListByParentId(int parentId) {
        List<CategoryCommissionVo> categoryCommissionVoList = new ArrayList<CategoryCommissionVo>();
        List<Category> categoryList = categoryDao.findByParentId(parentId);
        for (int i = 0; i < categoryList.size(); i++) {
            CategoryCommissionVo categoryCommissionVo = new CategoryCommissionVo(categoryList.get(i));
            CategoryCommission categoryCommission = categoryCommissionDao.getCategoryCommissionByCategoryId(categoryList.get(i).getCategoryId());
            if (categoryCommission != null) {
                categoryCommissionVo.setCommissionRate(categoryCommission.getCommissionRate());
            }
            categoryCommissionVoList.add(categoryCommissionVo);
        }
        return categoryCommissionVoList;
    }

    /**
     * 查询分类导航
     *
     * @return
     */
    public List<Object> getCategoryNav() {
        List<Object> categoryList;
        String categoryString = cacheHelper.get(CacheKey.CATEGORY_LIST);
        if (categoryString == null || categoryString.equals("")) {
            categoryList = this.getCategoryNavVoByParentId(0);
            cacheHelper.set(CacheKey.CATEGORY_LIST, JsonHelper.toJson(categoryList));
        } else {
            categoryList = JsonHelper.toGenericObject(categoryString, new TypeReference<List<Object>>() {
            });
        }
        return categoryList;
    }

    /**
     * 根据父级Id查询分类导航面包屑
     *
     * @param parentId
     * @return
     */
    public List<Object> getCategoryNavVoByParentId(int parentId) {
        List<Object> categoryList = new ArrayList<Object>();
        List<Object> objectList = categoryDao.findCategoryNavVoByParentId(parentId);
        if (objectList.size() > 0) {
            for (Object object : objectList) {
                CategoryNavVo categoryNavVo = (CategoryNavVo) object;
                categoryNavVo.setCategoryList(this.getCategoryNavVoByParentId(categoryNavVo.getCategoryId()));
                categoryList.add(categoryNavVo);
            }
        }
        return categoryList;
    }

    /**
     * 根据分类编号查询所有父级名称
     *
     * @param categoryId
     * @return
     */
    public String getCategoryNameCrumbsByEndCategoryId(int categoryId) {
        String Line = "";
        Category category = categoryDao.get(Category.class, categoryId);
        Line = Line + category.getCategoryName();
        if (category.getParentId() != 0) {
            Line = this.getCategoryNameCrumbsByEndCategoryId(category.getParentId()) + " > " + Line;
        }
        return Line;
    }

}
