package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.dao.store.AlbumFilesDao;
import net.shopnc.b2b2c.dao.store.SellerDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.ArticleService;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.bill.BillService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.service.member.ConsultService;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.b2b2c.service.refund.RefundService;
import net.shopnc.b2b2c.service.statistical.StatOrdersService;
import net.shopnc.b2b2c.vo.member.EvaluateStoreVo;
import net.shopnc.b2b2c.vo.statistical.StatOrdersVo;
import net.shopnc.common.util.ChartsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class IndexJsonAction extends BaseJsonAction {
    @Autowired
    StatOrdersService statOrdersService;
    @Autowired
    ArticleService articleService;
    @Autowired
    StoreDao storeDao;
    @Autowired
    SellerDao sellerDao;
    @Autowired
    EvaluateService evaluateSerivce;
    @Autowired
    GoodsCommonDao goodsCommonDao;
    @Autowired
    StoreGradeDao storeGradeDao;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ConsultService consultService;
    @Autowired
    OrdersService ordersService;
    @Autowired
    RefundService refundService;
    @Autowired
    BillService billService;
    @Autowired
    SiteService siteService;
    @Autowired
    AlbumFilesDao albumFilesDao;

    /**
     * 查询店铺商品相关统计数字
     */
    @ResponseBody
    @RequestMapping(value = "index/stat/goods", method = RequestMethod.GET)
    public HashMap goodsStat() {
        HashMap<String, Object> statMap = new HashMap<>();
        //查询发布商品总数
        long goodsCommonCount = goodsCommonDao.findCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("goodsCommonCount", goodsCommonCount);
        //查询店铺已推荐商品数量
        long goodsCommendCount = goodsCommonDao.findCommendCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("goodsCommendCount", goodsCommendCount);
        long imageCount = albumFilesDao.findCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("imageCount", imageCount);
        //查询出售中的商品总数
        long goodsCommonOnSaleCount = goodsService.getGoodsCommonOnSaleCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("goodsCommonOnSaleCount", goodsCommonOnSaleCount);
        //发布待平台审核商品
        long goodsCommonWaitCount = goodsService.getGoodsCommonWaitCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("goodsCommonWaitCount", goodsCommonWaitCount);
        //平台审核失败商品
        long goodsCommonVerifyFailCount = goodsService.getGoodsCommonVerifyFailCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("goodsCommonVerifyFailCount", goodsCommonVerifyFailCount);
        //仓库中已审核
        long goodsCommonOfflineAndPassCount = goodsService.getGoodsCommonOfflineAndVerifyPassCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("goodsCommonOfflineAndPassCount", goodsCommonOfflineAndPassCount);
        //违规禁售商品
        long goodsCommonBanCount = goodsService.getGoodsCommonBanCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("goodsCommonBanCount", goodsCommonBanCount);
        //商品咨询待回复
        long consultNoReplyCount = consultService.getConsultNoReplyCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("consultNoReplyCount", consultNoReplyCount);
        return statMap;
    }
    /**
     * 查询店铺订单相关统计数字
     */
    @ResponseBody
    @RequestMapping(value = "index/stat/orders", method = RequestMethod.GET)
    public HashMap ordersStat(){
        HashMap<String, Object> statMap = new HashMap<>();
        //交易中的订单
        long ordersProgressingCount = ordersService.getProgressingCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("ordersProgressingCount", ordersProgressingCount);
        //退款数
        long refundWaitingCount = refundService.getRefundWaitingCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("refundWaitingCount", refundWaitingCount);
        //退货数
        long returnWaitingCount = refundService.getReturnWaitingCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("returnWaitingCount", returnWaitingCount);
        //退款退货总数
        statMap.put("refundAndReturnCount", refundWaitingCount+returnWaitingCount);
        //待收款订单
        long ordersWaitPayCount = ordersService.getOrdersNewCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("ordersWaitPayCount", ordersWaitPayCount);
        //待发货订单
        long ordersWaitSendCount = ordersService.getOrdersPayCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("ordersWaitSendCount", ordersWaitSendCount);
        //待确认结算账单
        long billNewCount = billService.getBillNewCountByStoreId(SellerSessionHelper.getStoreId());
        statMap.put("billNewCount", billNewCount);
        return statMap;
    }
    /**
     * 取得当前店铺在同行业分类下的 描述评价、服务评价、发货评价的高中低
     */
    @ResponseBody
    @RequestMapping(value = "index/stat/evaluate", method = RequestMethod.GET)
    public EvaluateStoreVo evaluateStat(){
        //店铺评价信息
        EvaluateStoreVo evaluateStoreVo = evaluateSerivce.getEvalStoreClass(SellerSessionHelper.getStoreId());
        return evaluateStoreVo;
    }
    /**
     * 昨日今日销售趋势图
     */
    @ResponseBody
    @RequestMapping(value = "index/hourtrend", method = RequestMethod.GET)
    public HashMap<String, Object> hourSaleTrend(){
        List<StatOrdersVo> todayList = statOrdersService.getOrdersSalesHourByTodayAndStoreId(SellerSessionHelper.getStoreId());
        List<StatOrdersVo> yesterdayList = statOrdersService.getOrdersSalesHourByYesterdayAndStoreId(SellerSessionHelper.getStoreId());
        //构造走势图数据
        HashMap<String, Object> chartsParams = new HashMap<>();
        //走势图Y轴提示商品分类名称
        chartsParams.put("labels", new String[]{"今日","昨日"});
        //走势图Y轴数据key
        chartsParams.put("ykeys", new String[]{"ykeys0","ykeys1"});
        //走势图线条颜色
        chartsParams.put("lineColors", new String[]{ChartsHelper.colorArr.get("blue"),ChartsHelper.colorArr.get("orange")});
        //X轴提示小时
        Integer[] xLabelsArr = new Integer[24];
        for (int i = 0; i < 24; i++) {
            xLabelsArr[i] = i;
        }
        //构造data
        List<HashMap<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            HashMap<String, Object> dataItem = new HashMap<>();
            dataItem.put("xkey", xLabelsArr[i]);
            //今天数据
            dataItem.put("ykeys0", 0);
            if (todayList!=null && todayList.size()>0) {
                for (StatOrdersVo item:todayList) {
                    if (item.getCreateTimeHour() == i) {
                        dataItem.put("ykeys0", item.getOrdersAmountSum());
                    }
                }
            }
            //昨天数据
            dataItem.put("ykeys1", 0);
            if (yesterdayList!=null && yesterdayList.size()>0) {
                for (StatOrdersVo item:yesterdayList) {
                    if (item.getCreateTimeHour() == i) {
                        dataItem.put("ykeys1", item.getOrdersAmountSum());
                    }
                }
            }
            dataList.add(dataItem);
        }
        chartsParams.put("data", dataList);
        HashMap<String, Object> chartsMap;
        chartsMap = ChartsHelper.getMorrisDataLineCharts(chartsParams);
        return chartsMap;
    }
}