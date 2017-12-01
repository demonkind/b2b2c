package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.GoodsImage;
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
public class GoodsImageDao extends BaseDaoHibernate4<GoodsImage> {
    /**
     * 查询商品详细的全部图片
     * @param commonId
     * @return
     */
    public List<GoodsImage> findGoodsDetailImage(int commonId, int colorId) {
        String hql = "from GoodsImage where commonId = :commonId and colorId = :colorId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        map.put("colorId", colorId);
        return super.find(hql, map);
    }

    /**
     * 根据SPU编号查询商品主图列表
     * @param commonId
     * @return
     */
    public List<GoodsImage> findDefaultImageByCommonId(int commonId) {
        String hql = "from GoodsImage where isDefault = 1 and commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return super.find(hql, map);
    }

    /**
     * 根据SPU编号查询GoodsPicVo列表
     * @param commonId
     * @return
     */
    public List<Object> findGoodsPicVoByCommonId(int commonId) {
        String hql = "select new net.shopnc.b2b2c.vo.goods.GoodsPicVo(gi) from GoodsImage gi where gi.commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        return super.findObject(hql, map);
    }

    /**
     * 根据SPU编号删除商品图片
     * @param commonId
     */
    public void deleteByCommonId(int commonId) {
        String hql = "from GoodsImage where commonId = :commonId";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("commonId", commonId);
        List<GoodsImage> goodsImageList = super.find(hql, map);
        super.deleteAll(goodsImageList);
    }
}
