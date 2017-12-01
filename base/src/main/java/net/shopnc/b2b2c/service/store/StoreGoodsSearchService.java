package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.goods.GoodsImageDao;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsImage;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.common.entity.PageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by dqw on 2016/01/29.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class StoreGoodsSearchService extends BaseService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsImageDao goodsImageDao;

    /**
     * 商品搜索方法
     * @param table
     * @param whereList
     * @param sort
     * @param map
     * @param pageNo
     * @param pageSize
     * @return
     */
    private List<GoodsVo> searchGoods(String table,
                                      List<String> whereList,
                                      String sort,
                                      HashMap<String, Object> map,
                                      int pageNo,
                                      int pageSize) {
        // 商品列表
        List<Object> list = goodsDao.findStoreGoodsVo(table, whereList, sort, map, pageNo, pageSize);
        List<GoodsVo> goodsVoList = new ArrayList<GoodsVo>();
        for (Object goodsList : list) {
            GoodsVo goodsVo = (GoodsVo) goodsList;
            List<GoodsImage> goodsImageList = goodsImageDao.findDefaultImageByCommonId(goodsVo.getCommonId());
            if (goodsImageList.size() > 0) {
                goodsVo.setImageSrc(goodsImageList.get(0).getImageSrc());
            }
            if (goodsImageList.size() > 1) {
                goodsVo.setGoodsImageList(goodsImageList);
            }

            goodsVoList.add(goodsVo);
        }
        return goodsVoList;
    }

    /**
     * 店铺商品搜索
     * @param params
     * @return
     */
    public HashMap<String, Object> search(HashMap<String, Object> params) {
        List<String> whereList = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        String table = "";

        //店铺编号
        Integer storeId = (Integer) params.get("storeId");
        whereList.add("gc.storeId = :storeId");
        map.put("storeId", storeId);

        //店铺商品分类
        if(params.containsKey("labelId")) {
            Integer labelId = (Integer) params.get("labelId");
            if (labelId != null && labelId > 0) {
                table += ", StoreLabelGoods sl ";
                whereList.add(" sl.commonId = gc.commonId and sl.storeLabelId = :storeLabelId");
                map.put("storeLabelId", labelId);
            }
        }

        // 关键字
        String keyword = (String) params.get("keyword");
        if (!keyword.isEmpty()) {
            whereList.add("(gc.goodsName like :keyword or g.goodsSerial like :keyword or g.goodsSpecs like :keyword)");
            map.put("keyword", "%" + keyword + "%");
        }

        // 排序
        String sort = "g.goodsId desc";
        if (!params.get("sort").equals("")) {
            String sortString = String.valueOf(params.get("sort"));
            switch(sortString) {
                case "default_desc":
                    sort = "g.goodsId desc";
                    break;
                case "default_asc":
                    sort = "g.goodsId asc";
                    break;
                case "price_desc":
                    sort = "g.goodsPrice desc";
                    break;
                case "price_asc":
                    sort = "g.goodsPrice asc";
                    break;
                case "sale_desc":
                    sort = "gs.goodsSaleNum desc";
                    break;
                case "sale_asc":
                    sort = "gs.goodsSaleNum asc";
                    break;
                case "fav_desc":
                    sort = "g.goodsFavorite desc";
                    break;
                case "fav_asc":
                    sort = "g.goodsFavorite asc";
                    break;
                default:
                    sort = "g.goodsId desc";
                    break;
            }
        }

        // 分页
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageNo((Integer) params.get("page"));
        pageEntity.setPageSize(50);
        pageEntity.setTotal(goodsDao.findStoreGoodsVoCount(table, whereList, map));

        List<GoodsVo> goodsVoList = searchGoods(table, whereList, sort, map, pageEntity.getPageNo(), pageEntity.getPageSize());

        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("list", goodsVoList);
        map1.put("showPage", pageEntity.getPageHtml());
        return map1;
    }

    /**
     * 根据店铺编号返回推荐商品集合
     * @param storeId
     * @param limit 返回的推荐商品数量
     * @return
     */
    public List<GoodsVo> findCommendGoodsVoByStoreId(int storeId, int limit) {
        List<String> whereList = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        whereList.add("gc.storeId = :storeId");
        map.put("storeId", storeId);

        whereList.add("gc.isCommend = :isCommend");
        map.put("isCommend", State.YES);

        String sort = "gc.commonId desc";

        return  searchGoods("", whereList, sort, map, 1, limit);
    }

    /**
     * 根据店铺编号返回店铺新发布商品集合
     * @param storeId
     * @param limit 返回新商品的数量
     * @return
     */
    public List<GoodsVo> findNewGoodsVoByStoreId(int storeId, int limit) {
        List<String> whereList = new ArrayList<String>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        whereList.add("gc.storeId = :storeId");
        map.put("storeId", storeId);

        String sort = "gc.commonId desc";

        return  searchGoods("", whereList, sort, map, 1, limit);
    }
}
