package net.shopnc.b2b2c.service.member;


import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.member.FavoritesGoodsDao;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.member.FavoritesGoods;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.favorites.FavoritesGoodsVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-18
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class FavoritesGoodsService extends BaseService {
    @Autowired
    private FavoritesGoodsDao favoritesGoodsDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsCommonDao goodsCommonDao;
    
    

    /**
     * 关注商品
     * @param goodsId
     * @param memberId
     * @return
     * @throws ShopException
     */
    public String addFavoritesGoods(Integer goodsId,Integer memberId) throws ShopException {
        if (goodsId==null || goodsId<=0 || memberId==null || memberId<=0) {
            throw new ParameterErrorException();
        }
        //判断是否已经关注
        HashMap<String,String> where = new HashMap<String, String>();
        where.put("goodsId", "goodsId = :goodsId");
        where.put("memberId", "memberId = :memberId");

        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("goodsId", goodsId);
        params.put("memberId", memberId);
        Long count = favoritesGoodsDao.findFavoritesGoodsCount(where, params, "");
        if (count > 0) {
            throw new ShopException("您已关注过该商品");
        }
        //查询商品信息
        Goods goodsInfo = goodsDao.get(Goods.class, goodsId);
        if (goodsInfo==null) {
            throw new ShopException("商品信息错误");
        }
        GoodsCommon goodsCommonInfo = goodsCommonDao.get(GoodsCommon.class, goodsInfo.getCommonId());
        if (goodsCommonInfo==null) {
            throw new ShopException("商品信息错误");
        }
        //添加关注
        FavoritesGoods favoritesGoods = new FavoritesGoods();
        favoritesGoods.setGoodsId(goodsId);
        favoritesGoods.setMemberId(memberId);
        favoritesGoods.setStoreId(goodsCommonInfo.getStoreId());
        favoritesGoods.setCategoryId(goodsCommonInfo.getCategoryId());
        favoritesGoods.setFavGoodsPrice(goodsInfo.getGoodsPrice());
        favoritesGoods.setAddTime(ShopHelper.getCurrentTimestamp());
        Integer favId = (Integer)favoritesGoodsDao.save(favoritesGoods);
        //累计关注次数
        if (favId > 0) {
            goodsInfo.setGoodsFavorite(goodsInfo.getGoodsFavorite() + 1);
            goodsDao.update(goodsInfo);
        }
        return favId.toString();
    }
    /**
     * 商品关注列表分页
     * @param params
     * @param page
     * @param pageSize
     * @param sort
     * @param group
     * @return
     */
    public HashMap<String,Object> getFavoritesGoodsListByPage(HashMap<String,Object> params, int page, int pageSize, String sort, String group) {
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(favoritesGoodsDao.findFavoritesGoodsCount(where, params, ""));
        pageEntity.setPageNo(page);
        if (pageSize > 0) {
            pageEntity.setPageSize(pageSize);
        }
        HashMap<String,String> listWhere = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                listWhere.put("memberId", "fg.memberId = :memberId");
            }
        }
        List<FavoritesGoodsVo> favList = favoritesGoodsDao.getFavoritesGoodsListByPage(listWhere, params, pageEntity.getPageNo(), pageEntity.getPageSize(), sort, group);

        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("list", favList);
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    /**
     * 商品关注列表
     * @param params
     * @param num
     * @param sort
     * @param group
     * @return
     */
    public List<FavoritesGoodsVo> getFavoritesGoodsList(HashMap<String,Object> params, int num, String sort, String group) {
        HashMap<String,String> listWhere = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                listWhere.put("memberId", "fg.memberId = :memberId");
            }
        }
        return favoritesGoodsDao.getFavoritesGoodsListByPage(listWhere, params, 1, num, sort, group);
    }
    /**
     * 删除关注
     * @param favoritesId
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean delFavoritesGoods(Integer favoritesId, int memberId) throws ShopException{
        if (favoritesId==null || favoritesId<=0 || memberId<=0) {
            throw new ParameterErrorException();
        }
        //查询关注信息
        FavoritesGoods favoritesGoodsInfo = favoritesGoodsDao.get(FavoritesGoods.class, favoritesId);
        if (favoritesGoodsInfo==null || favoritesGoodsInfo.getMemberId()!=memberId) {
            throw new ParameterErrorException();
        }
        HashMap<String, String> where = new HashMap<String, String>();
        where.put("favoritesId", "favoritesId = :favoritesId");
        where.put("memberId", "memberId = :memberId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("favoritesId", favoritesId);
        params.put("memberId", memberId);
        favoritesGoodsDao.deleteFavoritesGoods(where, params);
        //更新关注数量
        Goods goods = goodsDao.get(Goods.class, favoritesGoodsInfo.getGoodsId());
        if (goods != null) {
            goods.setGoodsFavorite(goods.getGoodsFavorite() - 1);
            goodsDao.update(goods);
        }
        return true;
    }
    /**
     * 查询会员收藏商品总数
     * @param memberId
     * @return
     */
    public long getFavoritesGoodsCountByMemberId(int memberId) {
        HashMap<String,String> where = new HashMap<>();
        where.put("memberId", "memberId = :memberId");
        HashMap<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        return favoritesGoodsDao.findFavoritesGoodsCount(where, params, "");
    }
    
    public FavoritesGoods findFavoritesById(HashMap<String,Object> params){
    	return favoritesGoodsDao.findFavoritesById(params);
    }

}