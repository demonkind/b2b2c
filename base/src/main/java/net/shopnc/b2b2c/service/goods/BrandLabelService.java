package net.shopnc.b2b2c.service.goods;

import net.shopnc.b2b2c.dao.goods.BrandBrandLabelDao;
import net.shopnc.b2b2c.dao.goods.BrandLabelDao;
import net.shopnc.b2b2c.domain.goods.BrandLabel;
import net.shopnc.b2b2c.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shopnc.feng on 2015-11-10.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BrandLabelService extends BaseService {
    @Autowired
    BrandLabelDao brandLabelDao;
    @Autowired
    BrandBrandLabelDao brandBrandLabelDao;

    /**
     * 根据品牌标签编号删除品牌标签及其相关数据表
     * @param brandLabelId
     * @return
     */
    public Boolean deleteByBrandLabelId(int brandLabelId) {
        // 删除品牌标签
        brandLabelDao.delete(BrandLabel.class, brandLabelId);
        // 删除品牌标签与品牌关系
        brandBrandLabelDao.deleteByBrandLabelId(brandLabelId);
        return true;
    }
}
