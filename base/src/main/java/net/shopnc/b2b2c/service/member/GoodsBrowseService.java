package net.shopnc.b2b2c.service.member;


import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.member.GoodsBrowseDao;
import net.shopnc.b2b2c.domain.member.GoodsBrowse;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.b2b2c.vo.goodsbrowse.GoodsBrowseVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.util.CookieHelper;
import net.shopnc.common.util.JsonHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zxy on 2016-01-28
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class GoodsBrowseService extends BaseService {

    @Autowired
    private GoodsBrowseDao goodsBrowseDao;
    @Autowired
    private GoodsDao goodsDao;

    /**
     * 获取浏览记录时间轴列表
     * @param memberId
     * @param goodsCategoryId1
     * @param goodsCategoryId2
     * @param page
     * @param pageSize
     * @return
     */
    public HashMap<String,Object> getGoodsBrowseTimelineList(Integer memberId, Integer goodsCategoryId1, Integer goodsCategoryId2, int page, int pageSize) {
        HashMap<String, String> whereCount = new HashMap<>();
        HashMap<String, Object> paramsCount = new HashMap<>();
        if (memberId!=null && memberId>0) {
            whereCount.put("memberId", "memberId = :memberId");
            paramsCount.put("memberId", memberId);
        }
        if (goodsCategoryId1!=null && goodsCategoryId1>0) {
            whereCount.put("goodsCategoryId1", "goodsCategoryId1 = :goodsCategoryId1");
            paramsCount.put("goodsCategoryId1", goodsCategoryId1);
        }
        if (goodsCategoryId2!=null && goodsCategoryId2>0) {
            whereCount.put("goodsCategoryId2", "goodsCategoryId2 = :goodsCategoryId2");
            paramsCount.put("goodsCategoryId2", goodsCategoryId2);
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(goodsBrowseDao.findGoodsBrowseCount(whereCount, paramsCount));
        pageEntity.setPageNo(page);
        if (pageSize > 0) {
            pageEntity.setPageSize(pageSize);
        }
        HashMap<String, String> whereList = new HashMap<>();
        HashMap<String, Object> paramsList = new HashMap<>();
        if (memberId!=null && memberId>0) {
            whereList.put("memberId", "gBrowse.memberId = :memberId");
            paramsList.put("memberId", memberId);
        }
        if (goodsCategoryId1!=null && goodsCategoryId1>0) {
            whereList.put("goodsCategoryId1", "gBrowse.goodsCategoryId1 = :goodsCategoryId1");
            paramsList.put("goodsCategoryId1", goodsCategoryId1);
        }
        if (goodsCategoryId2!=null && goodsCategoryId2>0) {
            whereList.put("goodsCategoryId2", "gBrowse.goodsCategoryId2 = :goodsCategoryId2");
            paramsList.put("goodsCategoryId2", goodsCategoryId2);
        }
        List<GoodsBrowseVo> browseList = goodsBrowseDao.getGoodsBrowseListByPage(whereList, paramsList, pageEntity.getPageNo(), pageEntity.getPageSize(), "", "");
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("list", browseList);
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    /**
     * 获取浏览记录商品分类
     * @param memberId
     * @return
     */
    public HashMap<String,Object> getGoodsBrowseCategoryList(Integer memberId){
        HashMap<String, String> where = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        if (memberId!=null && memberId>0) {
            where.put("memberId", "memberId = :memberId");
            params.put("memberId", memberId);
        }
        HashMap<String, Object> categoryList = goodsBrowseDao.getGoodsBrowseCategoryList(where, params);
        return categoryList;
    }
    /**
     * 添加浏览记录至数据库
     * @param goodsId
     * @param memberId
     * @return
     * @throws ShopException
     */
    public Serializable addGoodsBrowseToDatabase(int goodsId, int memberId) throws ShopException {
        if (goodsId<=0 || memberId<=0) {
            throw new ParameterErrorException();
        }
        //查询商品信息
        GoodsVo goodsVo = goodsDao.findGoodsAndCommonByGoodsId(goodsId);
        if (goodsVo==null) {
            throw new ParameterErrorException();
        }
        //删除用户已存在的该goodsCommonId记录
        HashMap<String,String> whereDelete = new HashMap<>();
        whereDelete.put("commonId", "commonId = :commonId");
        whereDelete.put("memberId", "memberId = :memberId");
        HashMap<String,Object> paramsDelete = new HashMap<>();
        paramsDelete.put("commonId", goodsVo.getCommonId());
        paramsDelete.put("memberId", memberId);
        goodsBrowseDao.deleteGoodsBrowse(whereDelete, paramsDelete);
        //新增浏览记录
        GoodsBrowse goodsBrowse = new GoodsBrowse();
        goodsBrowse.setMemberId(memberId);
        goodsBrowse.setGoodsId(goodsId);
        goodsBrowse.setCommonId(goodsVo.getCommonId());
        goodsBrowse.setAddTime(ShopHelper.getCurrentTimestamp());
        goodsBrowse.setGoodsCategoryId(goodsVo.getCategoryId());
        goodsBrowse.setGoodsCategoryId1(goodsVo.getCategoryId1());
        goodsBrowse.setGoodsCategoryId2(goodsVo.getCategoryId2());
        goodsBrowse.setGoodsCategoryId3(goodsVo.getCategoryId3());
        return goodsBrowseDao.save(goodsBrowse);
    }
    /**
     * 添加浏览记录至Cookie
     * @param goodsId
     * @throws ShopException
     */
    public void addGoodsBrowseToCookie(int goodsId) throws ShopException {
        if (goodsId<=0) {
            throw new ParameterErrorException();
        }
        //查询商品信息
        GoodsVo goodsVo = goodsDao.findGoodsAndCommonByGoodsId(goodsId);
        if (goodsVo==null) {
            throw new ParameterErrorException();
        }
        //浏览时间
        long browseTime = ShopHelper.getCurrentTime();
        //浏览记录List
        List<String> cookieList = new ArrayList<>();

        if (CookieHelper.getCookie("browse_goods")!=null && CookieHelper.getCookie("browse_goods").length()>0) {
            List<String> cookieListOld = JsonHelper.toGenericObject(CookieHelper.getCookie("browse_goods"), new TypeReference<List<String>>(){});
            if (cookieListOld!=null && cookieListOld.size()>0) {
                for (int i = 0; i < cookieListOld.size(); i++){
                    String itemCommonId = cookieListOld.get(i).split("-")[0];
                    if (String.valueOf(goodsVo.getCommonId()).equals(itemCommonId)) {
                        cookieListOld.remove(i);
                    }
                }
                cookieList = cookieListOld;
            }
        }
        cookieList.add(String.valueOf(goodsVo.getCommonId()) + "-" + String.valueOf(goodsId) + "-" + String.valueOf(browseTime));
        //最多保留最新的50条数据
        if (cookieList.size()>50) {
            cookieList.subList(0, cookieList.size()-50).clear();
        }
        CookieHelper.setCookie("browse_goods", JsonHelper.toJson(cookieList));
    }
    /**
     * 删除会员浏览历史
     * @param memberId
     * @param browseId
     * @return
     * @throws ShopException
     */
    public boolean delGoodsBrowse(int memberId, Integer browseId) throws ShopException{
        if (memberId<=0) {
            throw new ParameterErrorException();
        }
        HashMap<String,String> where = new HashMap<>();
        where.put("memberId", "memberId = :memberId");
        if (browseId!=null && browseId>0) {
            where.put("browseId", "browseId = :browseId");
        }

        HashMap<String,Object> params = new HashMap<>();
        params.put("memberId", memberId);
        if (browseId!=null && browseId>0) {
            params.put("browseId", browseId);
        }
        goodsBrowseDao.deleteGoodsBrowse(where, params);
        return true;
    }
    /**
     * 删除会员30天的浏览历史
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean delGoodsBrowseExpire(int memberId) throws ShopException{
        if (memberId<=0) {
            throw new ParameterErrorException();
        }
        HashMap<String,String> where = new HashMap<>();
        where.put("memberId", "memberId = :memberId");
        where.put("addTime", "addTime < :addTimeLt");

        HashMap<String,Object> params = new HashMap<>();
        params.put("memberId", memberId);
        Timestamp timeAgo30 = ShopHelper.getFutureTimestamp(Calendar.DATE, -30);
        timeAgo30 = ShopHelper.getTimestampOfDayStart(timeAgo30);
        params.put("addTimeLt", timeAgo30);
        goodsBrowseDao.deleteGoodsBrowse(where, params);
        return true;
    }
    /**
     * 浏览过的商品
     * @param memberId
     * @param showNum
     * @return
     * @throws ShopException
     */
    public List<GoodsBrowseVo> getGoodsBrowseList(int memberId, int showNum) throws ShopException{
        if (showNum <= 0) {
            throw new ParameterErrorException();
        }
        if (memberId > 0) {
            //查询数据库浏览历史
            return this.getGoodsBrowseListByDatabase(memberId, showNum);
        }else{
            //查询浏览过的商品记录cookie
            return this.getGoodsBrowseListByCookie(showNum);
        }
    }
    /**
     * 查询数据库中会员浏览过的商品
     * @param memberId
     * @param showNum
     * @return
     * @throws ShopException
     */
    public List<GoodsBrowseVo> getGoodsBrowseListByDatabase(int memberId, int showNum) throws ShopException{
        if (showNum <= 0) {
            throw new ParameterErrorException();
        }
        if (memberId<=0) {
            throw new ParameterErrorException();
        }
        HashMap<String, String> whereList = new HashMap<>();
        HashMap<String, Object> paramsList = new HashMap<>();
        whereList.put("memberId", "gBrowse.memberId = :memberId");
        paramsList.put("memberId", memberId);
        List<GoodsBrowseVo> browseList = goodsBrowseDao.getGoodsBrowseList(whereList, paramsList, showNum, "", "");
        return browseList;
    }
    /**
     * 查询cookie中会员浏览过的商品
     * @param showNum
     * @return
     * @throws ShopException
     */
    public List<GoodsBrowseVo> getGoodsBrowseListByCookie(int showNum) throws ShopException{
        if (showNum <= 0) {
            throw new ParameterErrorException();
        }
        //浏览记录List
        List<GoodsBrowseVo> browseList = new ArrayList<>();

        List<Integer> goodsIdList = new ArrayList<>();
        List<String> browseTimeList = new ArrayList<>();

        if (CookieHelper.getCookie("browse_goods")!=null && CookieHelper.getCookie("browse_goods").length()>0) {
            List<String> cookieList = JsonHelper.toGenericObject(CookieHelper.getCookie("browse_goods"), new TypeReference<List<String>>(){});
            if (cookieList!=null && cookieList.size()>0) {
                //截取所需条数的最新的showNum条数据
                if (cookieList.size()>showNum) {
                    cookieList.subList(0, cookieList.size()-showNum).clear();
                }
                for (int i = 0; i < cookieList.size(); i++){
                    //向list前端逐个增加元素
                    goodsIdList.add(Integer.valueOf(cookieList.get(i).split("-")[1]));
                    browseTimeList.add(cookieList.get(i).split("-")[2]);
                }
                Collections.reverse(goodsIdList);
                Collections.reverse(browseTimeList);
            }
        }
        //查询商品信息
        if (goodsIdList!=null && goodsIdList.size()>0) {
            HashMap<String,String> goodsWhere = new HashMap<>();
            goodsWhere.put("goodsIdIn", "g.goodsId in (:goodsIdIn)");
            HashMap<String,Object> goodsParams = new HashMap<>();
            goodsParams.put("goodsIdIn", goodsIdList);
            List<Object> goodsList = goodsBrowseDao.getGoodsAndCommonList(goodsWhere, goodsParams);
            HashMap<Integer, GoodsBrowseVo> goodsMap = new HashMap<>();
            for (int i = 0; i < goodsList.size(); i++) {
                goodsMap.put(((GoodsBrowseVo)goodsList.get(i)).getGoods().getGoodsId(), (GoodsBrowseVo)goodsList.get(i));
            }
            for (int i = 0; i < goodsIdList.size(); i++) {
                if (goodsMap.get(goodsIdList.get(i)) != null) {
                    GoodsBrowseVo item = new GoodsBrowseVo();
                    item.setGoodsId(goodsIdList.get(i));
                    item.setCommonId(goodsMap.get(goodsIdList.get(i)).getGoodsCommon().getCommonId());
                    item.setAddTime(ShopHelper.time2Timestamp(browseTimeList.get(i), ""));
                    item.setGoods(goodsMap.get(goodsIdList.get(i)).getGoods());
                    item.setGoodsCommon(goodsMap.get(goodsIdList.get(i)).getGoodsCommon());
                    item.setGoodsCategoryId(goodsMap.get(goodsIdList.get(i)).getGoodsCommon().getCategoryId());
                    item.setGoodsCategoryId1(goodsMap.get(goodsIdList.get(i)).getGoodsCommon().getCategoryId1());
                    item.setGoodsCategoryId2(goodsMap.get(goodsIdList.get(i)).getGoodsCommon().getCategoryId2());
                    item.setGoodsCategoryId3(goodsMap.get(goodsIdList.get(i)).getGoodsCommon().getCategoryId3());
                    browseList.add(item);
                }
            }
        }
        return browseList;
    }
    /**
     * 将cookie浏览历史加入数据库
     * @param memberId
     * @throws ShopException
     */
    public void mergeGoodsBrowse(int memberId) throws ShopException{
        if (memberId <= 0) {
            return;
        }
        //读取10000条数据，防止出现丢失部分cookie数据的情况
        List<GoodsBrowseVo> goodsBrowseVoList = this.getGoodsBrowseListByCookie(1000);
        if (goodsBrowseVoList==null || goodsBrowseVoList.size()<=0) {
            return;
        }
        //批量增加浏览历史
        List<GoodsBrowse> insertList = new ArrayList<>();
        for (int i = 0; i < goodsBrowseVoList.size(); i++){
            GoodsBrowse insertItem = new GoodsBrowse();
            insertItem.setGoodsId(goodsBrowseVoList.get(i).getGoodsId());
            insertItem.setCommonId(goodsBrowseVoList.get(i).getCommonId());
            insertItem.setMemberId(memberId);
            insertItem.setAddTime(goodsBrowseVoList.get(i).getAddTime());
            insertItem.setGoodsCategoryId(goodsBrowseVoList.get(i).getGoodsCategoryId());
            insertItem.setGoodsCategoryId1(goodsBrowseVoList.get(i).getGoodsCategoryId1());
            insertItem.setGoodsCategoryId2(goodsBrowseVoList.get(i).getGoodsCategoryId2());
            insertItem.setGoodsCategoryId3(goodsBrowseVoList.get(i).getGoodsCategoryId3());
            insertList.add(insertItem);
        }
        if (insertList!=null && insertList.size()>0) {
            goodsBrowseDao.saveAll(insertList);
        }
        //清空cookie
        CookieHelper.removeCookie("browse_goods");
        return;
    }
    /**
     * 获取会员浏览记录总数
     * @param memberId
     * @return
     */
    public long getGoodsBrowseCountByMemberId(int memberId){
        HashMap<String,String> where = new HashMap<>();
        where.put("memberId", "memberId = :memberId");
        HashMap<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        return goodsBrowseDao.findGoodsBrowseCount(where, params);
    }
}