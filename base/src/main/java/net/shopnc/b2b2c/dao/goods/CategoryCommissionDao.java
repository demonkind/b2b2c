package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.CategoryCommission;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hbj on 2015/12/14.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class CategoryCommissionDao extends BaseDaoHibernate4<CategoryCommission> {
    /**
     * 取得单条分类佣金
     * @param categoryId
     * @return
     */
    public CategoryCommission getCategoryCommissionByCategoryId(int categoryId) {
        String hql = "from CategoryCommission where categoryId = :categoryId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("categoryId",categoryId);
        List<CategoryCommission> categoryCommissionList = super.find(hql, hashMap);
        return categoryCommissionList.size()>0 ? categoryCommissionList.get(0) : null;
    }
    /**
     * bycj [ 保存分佣比例 ]
     * @param commissionRate
     * @param categoryIds
     */
    public void saveRates(int commissionRate , List<Integer> categoryIds){
        String hql = "update CategoryCommission set commissionRate = :commissionRate where categoryId in(:categoryIds)";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("commissionRate",commissionRate);
        hashMap.put("categoryIds",categoryIds);
        super.update(hql, hashMap);
    }





}
