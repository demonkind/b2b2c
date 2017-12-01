package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.BrandLabel;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class BrandLabelDao extends BaseDaoHibernate4<BrandLabel> {
    /**
     * 根据品牌编号查询品牌标签
     * @param brandId
     * @return
     */
    public List<BrandLabel> findByBrandId(int brandId) {
        String hql = "select bl from BrandLabel bl,BrandBrandLabel bbl where bl.brandLabelId = bbl.brandLabelId and bbl.brandId = :brandId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("brandId", brandId);
        return super.find(hql, map);
    }
}
