package net.shopnc.b2b2c.service.statistical;


import net.shopnc.b2b2c.dao.goods.CategoryDao;
import net.shopnc.b2b2c.dao.statistical.StatOrdersDao;
import net.shopnc.b2b2c.dao.statistical.StatOrdersGoodsDao;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.statistical.StatOrdersGoodsVo;
import net.shopnc.b2b2c.vo.statistical.StatOrdersVo;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-02-03
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class StatOrdersService extends BaseService {
    @Autowired
    private StatOrdersGoodsDao statOrdersGoodsDao;
    @Autowired
    private StatOrdersDao statOrdersDao;
    @Autowired
    private CategoryDao categoryDao;

    /**
     * 通过OrdersAmount统计最近days天商品一级分类销售额
     * @param num
     * @param days
     * @return
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsCategoryByOrdersGoodsPriceSum(int num, int days) {
        HashMap<String, Object> params = new HashMap<>();
        Timestamp ltTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());
        Timestamp gtTime = ShopHelper.getFutureTimestamp(ltTime, Calendar.DATE, -days);
        params.put("createTimeGt", gtTime);
        params.put("createTimeLt", ltTime);
        return this.getOrdersGoodsCategoryList(num, "ordersGoodsPriceSum desc", params);
    }
    /**
     * 通过ordersGoods查询热销一级商品分类
     * @param num
     * @param orderBy
     * @param params
     * @return
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsCategoryList(int num, String orderBy, HashMap<String, Object> params) {
        HashMap<String,String> where = new HashMap<>();
        for (String key : params.keySet()) {
            if (key.equals("createTimeGt")) {
                where.put("createTimeGt", "orders.createTime > :createTimeGt");
            }
            if (key.equals("createTimeLt")) {
                where.put("createTimeLt", "orders.createTime < :createTimeLt");
            }
        }
        List<StatOrdersGoodsVo> statList = statOrdersGoodsDao.getOrdersGoodsList(num, where, params, "ordersGoods.categoryId1", orderBy);
        List<StatOrdersGoodsVo> statListNew = new ArrayList<>();
        if (statList!=null && statList.size()>0) {
            for(StatOrdersGoodsVo item:statList) {
                StatOrdersGoodsVo statOrdersGoodsVo = item;
                //查询商品分类
                if (item.getOrdersGoods().getCategoryId1() > 0) {
                    Category categoryTmp = categoryDao.get(Category.class, item.getOrdersGoods().getCategoryId1());
                    statOrdersGoodsVo.setOrdersGoodsCategoryName1(categoryTmp.getCategoryName());
                }
                statListNew.add(statOrdersGoodsVo);
            }
        }
        return statListNew;
    }
    /**
     * 查询最近days天指定分类每天的销售额
     * @param days
     * @param categoryId1
     * @return
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsSalesDayByCategoryId1(int days, int categoryId1) {
        HashMap<String, Object> params = new HashMap<>();
        Timestamp ltTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());
        Timestamp gtTime = ShopHelper.getFutureTimestamp(ltTime, Calendar.DATE, -days);
        params.put("createTimeGt", gtTime);
        params.put("createTimeLt", ltTime);
        params.put("categoryId1", categoryId1);
        return this.getOrdersGoodsSalesDay(params);
    }
    /**
     * 通过ordersGoods查询一段时间内每天销售情况
     * @param params
     * @return
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsSalesDay(HashMap<String, Object> params) {
        HashMap<String,String> where = new HashMap<>();
        for (String key : params.keySet()) {
            if (key.equals("categoryId1")) {
                where.put("categoryId1", "ordersGoods.categoryId1 = :categoryId1");
            }
            if (key.equals("createTimeGt")) {
                where.put("createTimeGt", "orders.createTime > :createTimeGt");
            }
            if (key.equals("createTimeLt")) {
                where.put("createTimeLt", "orders.createTime < :createTimeLt");
            }
        }
        return statOrdersGoodsDao.getOrdersGoodsListByDay(where, params);
    }
    /**
     * 查询今天销售额走势
     * @return
     */
    public List<StatOrdersVo> getOrdersSalesHourByToday() {
        HashMap<String, Object> params = new HashMap<>();
        Timestamp ltTime = ShopHelper.getCurrentTimestamp();
        Timestamp gtTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());
        params.put("createTimeGt", gtTime);
        params.put("createTimeLt", ltTime);
        return this.getOrdersSalesHour(params);
    }
    /**
     * 查询昨天销售额走势
     * @return
     */
    public List<StatOrdersVo> getOrdersSalesHourByYesterday() {
        HashMap<String, Object> params = new HashMap<>();
        Timestamp ltTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());
        Timestamp gtTime = ShopHelper.getFutureTimestamp(ltTime, Calendar.DATE, -1);
        params.put("createTimeGt", gtTime);
        params.put("createTimeLt", ltTime);
        return this.getOrdersSalesHour(params);
    }
    /**
     * 查询店铺今天销售额走势
     * @param storeId
     * @return
     */
    public List<StatOrdersVo> getOrdersSalesHourByTodayAndStoreId(int storeId) {
        HashMap<String, Object> params = new HashMap<>();
        Timestamp ltTime = ShopHelper.getCurrentTimestamp();
        Timestamp gtTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());
        params.put("createTimeGt", gtTime);
        params.put("createTimeLt", ltTime);
        params.put("storeId", storeId);
        return this.getOrdersSalesHour(params);
    }
    /**
     * 查询店铺昨天销售额走势
     * @param storeId
     * @return
     */
    public List<StatOrdersVo> getOrdersSalesHourByYesterdayAndStoreId(int storeId) {
        HashMap<String, Object> params = new HashMap<>();
        Timestamp ltTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());
        Timestamp gtTime = ShopHelper.getFutureTimestamp(ltTime, Calendar.DATE, -1);
        params.put("createTimeGt", gtTime);
        params.put("createTimeLt", ltTime);
        params.put("storeId", storeId);
        return this.getOrdersSalesHour(params);
    }
    /**
     * 查询订单某小时销售走势
     * @param params
     * @return
     */
    public List<StatOrdersVo> getOrdersSalesHour(HashMap<String, Object> params) {
        HashMap<String,String> where = new HashMap<>();
        for (String key : params.keySet()) {
            if (key.equals("createTimeGt")) {
                where.put("createTimeGt", "orders.createTime > :createTimeGt");
            }
            if (key.equals("createTimeLt")) {
                where.put("createTimeLt", "orders.createTime < :createTimeLt");
            }
            if (key.equals("storeId")) {
                where.put("storeId", "orders.storeId = :storeId");
            }
        }
        return statOrdersDao.getOrdersListByHours(where, params);
    }
    /**
     * 通过OrdersAmount统计最近days天商品销售排行
     * @param num
     * @param days
     * @param storeId
     * @return
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsByOrdersGoodsPriceAndStoreId(int num, int days, int storeId){
        HashMap<String, Object> params = new HashMap<>();
        Timestamp ltTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());
        Timestamp gtTime = ShopHelper.getFutureTimestamp(ltTime, Calendar.DATE, -days);
        params.put("createTimeGt", gtTime);
        params.put("createTimeLt", ltTime);
        params.put("storeId", storeId);
        return this.getOrdersGoodsSales(num, "ordersGoodsPriceSum desc", params);
    }
    /**
     * 通过OrdersGoods BuyNum统计最近days天商品销量排行
     * @param num
     * @param days
     * @param storeId
     * @return
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsByOrdersGoodsBuyNumAndStoreId(int num, int days, int storeId){
        HashMap<String, Object> params = new HashMap<>();
        Timestamp ltTime = ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp());
        Timestamp gtTime = ShopHelper.getFutureTimestamp(ltTime, Calendar.DATE, -days);
        params.put("createTimeGt", gtTime);
        params.put("createTimeLt", ltTime);
        params.put("storeId", storeId);
        return this.getOrdersGoodsSales(num, "ordersGoodsBuyNumSum desc", params);
    }
    /**
     * 查询热销商品排行
     * @param num
     * @param orderBy
     * @param params
     * @return
     */
    public List<StatOrdersGoodsVo> getOrdersGoodsSales(int num, String orderBy, HashMap<String, Object> params){
        HashMap<String,String> where = new HashMap<>();
        for (String key : params.keySet()) {
            if (key.equals("createTimeGt")) {
                where.put("createTimeGt", "orders.createTime > :createTimeGt");
            }
            if (key.equals("createTimeLt")) {
                where.put("createTimeLt", "orders.createTime < :createTimeLt");
            }
            if (key.equals("storeId")) {
                where.put("storeId", "orders.storeId = :storeId");
            }
        }
        List<StatOrdersGoodsVo> statList = statOrdersGoodsDao.getOrdersGoodsList(num, where, params, "ordersGoods.goodsId", orderBy);
        return statList;
    }
}