package net.shopnc.b2b2c.service.goods;

import com.github.stuxuhai.jpinyin.PinyinHelper;
import net.shopnc.b2b2c.constant.BrandApplyState;
import net.shopnc.b2b2c.dao.goods.*;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.b2b2c.domain.goods.BrandApply;
import net.shopnc.b2b2c.domain.goods.BrandBrandLabel;
import net.shopnc.b2b2c.domain.goods.BrandLabel;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.BrandAndLabelVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-05.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BrandService extends BaseService {
    @Autowired
    BrandDao brandDao;
    @Autowired
    BrandLabelDao brandLabelDao;
    @Autowired
    BrandBrandLabelDao brandBrandLabelDao;
    @Autowired
    CategoryBrandDao categoryBrandDao;
    @Autowired
    BrandApplyDao brandApplyDao;


    /**
     * 根据品牌实体查询BrandAndLabelVo
     * @param brand
     * @return
     */
    private BrandAndLabelVo getBrandAndLabel(Brand brand) {
        BrandAndLabelVo brandAndLabel = new BrandAndLabelVo();
        brandAndLabel.setBrandId(brand.getBrandId());
        brandAndLabel.setBrandName(brand.getBrandName());
        brandAndLabel.setBrandEnglish(brand.getBrandEnglish());
        brandAndLabel.setBrandInitial(brand.getBrandInitial());
        brandAndLabel.setBrandImage(brand.getBrandImage());
        brandAndLabel.setBrandImageSrc(brand.getBrandImageSrc());
        brandAndLabel.setBrandSort(brand.getBrandSort());
        brandAndLabel.setIsRecommend(brand.getIsRecommend());
        brandAndLabel.setApplyState(brand.getApplyState());
        brandAndLabel.setShowType(brand.getShowType());
        List<BrandLabel> brandLabels = brandLabelDao.findByBrandId(brand.getBrandId());
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (int i = 0; i < brandLabels.size(); i++) {
            if (i > 0) {
                stringBuilder.append('，');
                stringBuilder1.append(',');
            }
            stringBuilder.append(brandLabels.get(i).getBrandLabelName());
            stringBuilder1.append(brandLabels.get(i).getBrandLabelId());
        }
        brandAndLabel.setBrandLabelNames(stringBuilder.toString());
        brandAndLabel.setBrandLabelIds(stringBuilder1.toString());
        return brandAndLabel;
    }
    /**
     * 保存品牌与品牌标签关系
     * @param brandId
     * @param brandLabelIds
     * @return
     */
    private void saveBrandBrandLabel(int brandId, int[] brandLabelIds) {
        if (brandLabelIds == null) {
            return;
        }
        List<BrandBrandLabel> brandBrandLabels = new ArrayList<BrandBrandLabel>();
        for (int i = 0; i < brandLabelIds.length; i++) {
            BrandBrandLabel brandBrandLabel = new BrandBrandLabel();
            brandBrandLabel.setBrandId(brandId);
            brandBrandLabel.setBrandLabelId(brandLabelIds[i]);
            brandBrandLabels.add(brandBrandLabel);
        }
        brandBrandLabelDao.saveAll(brandBrandLabels);
    }

    /**
     * 保存品牌及品牌与品牌标签<br>
     *     平台
     * @param brand
     * @param brandLabelIds
     * @return
     */
    public Integer save(Brand brand, int[] brandLabelIds) {
        // 取得品牌拼音首字母大写
        String name = PinyinHelper.getShortPinyin(brand.getBrandName());
        String initial = name.substring(0, 1).toUpperCase();
        brand.setBrandInitial(initial);
        int brandId = (Integer) brandDao.save(brand);
        saveBrandBrandLabel(brandId, brandLabelIds);
        return brandId;
    }

    /**
     * 更新品牌及品牌与品牌标签<br>
     *     平台
     * @param brand
     * @param brandLabelIds
     * @return
     */
    public void update(Brand brand, int[] brandLabelIds) {
        // 取得品牌拼音首字母大写
        String name = PinyinHelper.getShortPinyin(brand.getBrandName());
        String initial = name.substring(0, 1).toUpperCase();
        brand.setBrandInitial(initial);
        brandDao.update(brand);
        int brandId = brand.getBrandId();
        // 删除原品牌与品牌标签关系
        brandBrandLabelDao.deleteByBrandId(brandId);
        // 保存品牌与品牌标签关系
        saveBrandBrandLabel(brandId, brandLabelIds);
    }

    /**
     * 品牌审核
     */
    public void verify(Brand brand, int[] brandLabelIds, String stateRemark) throws ShopException {
        if (brand.getApplyState() == BrandApplyState.FAIL && stateRemark.equals("")) {
            throw new ShopException("请填写审核失败原因");
        }
        update(brand, brandLabelIds);
        BrandApply brandApply = brandApplyDao.get(BrandApply.class, brand.getBrandId());
        brandApply.setStateRemark(stateRemark);
        brandApplyDao.update(brandApply);
    }

    /**
     * 保存品牌及与标签关系<br>
     *    店铺
     * @param brand
     * @param brandLabelIds
     */
    public Integer saveBrandForStore(Brand brand, int[] brandLabelIds, int storeId) {
        brand.setApplyState(BrandApplyState.WAIT);
        brand.setStoreId(storeId);
        return save(brand, brandLabelIds);
    }

    /**
     * 更新品牌及与标签关系<br>
     *     店铺
     * @param brand
     * @param brandLabelIds
     */
    public void updateBrandForStore(Brand brand, int[] brandLabelIds, int storeId) {
        brand.setApplyState(BrandApplyState.WAIT);
        brand.setStoreId(storeId);
        update(brand, brandLabelIds);
    }

    /**
     * 后台品牌列表
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getBrandDtGridListForAdmin(String dtGridPager) throws Exception {
        DtGrid dtGrid = brandDao.getDtGridList(dtGridPager, Brand.class);

        List<Object> brands = dtGrid.getExhibitDatas();
        List<Object> brandAdminLists = new ArrayList<Object>();
        for (int j = 0; j < brands.size(); j++) {
            Brand brand = (Brand) brands.get(j);
            BrandAndLabelVo brandAndLabel = getBrandAndLabel(brand);
            brandAdminLists.add(brandAndLabel);
        }

        dtGrid.setExhibitDatas(brandAdminLists);
        return dtGrid;
    }

    /**
     * 后台品牌列表
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getBrandDtGridVerifyListForAdmin(String dtGridPager) throws Exception {
        DtGrid dtGrid = brandDao.getDtGridList(dtGridPager, Brand.class);
        return dtGrid;
    }

    /**
     * 删除品牌相关
     * @param brandId
     * @return
     */
    public void deleteByBrandId(int brandId) {
        // 删除品牌
        brandDao.delete(Brand.class, brandId);
        // 删除品牌与品牌标签关系
        brandBrandLabelDao.deleteByBrandId(brandId);
        // 删除品牌与分类关系
        categoryBrandDao.deleteByBrandId(brandId);
    }

    /**
     * 删除店铺品牌
     * @param brandId
     * @param storeId
     * @throws ShopException
     */
    public void deleteByBrandIdForStore(int brandId, int storeId) throws ShopException {
        if (brandId <= 0) {
            throw new ShopException("参数错误");
        }
        Brand brand = brandDao.get(Brand.class, brandId);
        if (brand.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        deleteByBrandId(brandId);
        brandApplyDao.delete(BrandApply.class, brandId);
    }

    /**
     * 根据品牌编号查询BrandAndLabelVo
     * @param brandId
     * @return
     * @throws ShopException
     */
    public BrandAndLabelVo getBrandAndLabelForBrandId(int brandId) throws ShopException {
        if (brandId <= 0) {
            throw new ShopException("参数错误");
        }
        Brand brand = brandDao.get(Brand.class, brandId);
        return getBrandAndLabel(brand);
    }

    /**
     * 查询不是通过审核状态的字段
     * @return
     */
    public long findWaitCount() {
        return brandDao.findWaitCount();
    }
}
