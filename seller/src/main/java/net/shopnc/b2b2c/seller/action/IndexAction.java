package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.dao.store.AlbumFilesDao;
import net.shopnc.b2b2c.dao.store.SellerDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.domain.store.StoreGrade;
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
import net.shopnc.b2b2c.vo.statistical.StatOrdersGoodsVo;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class IndexAction extends BaseAction {
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

    @RequestMapping("")
    public String index(ModelMap modelMap) {
        //如果未开店跳转到开店页面
        if(SellerSessionHelper.getStoreId() == 0) {
            return "redirect:" + UrlSeller.OPEN_INFO;
        }
        //查询店铺信息
        Store storeInfo = storeDao.get(Store.class, SellerSessionHelper.getStoreId());
        modelMap.put("storeInfo", storeInfo);
        //查询商家信息
        Seller sellerInfo = sellerDao.get(Seller.class, SellerSessionHelper.getSellerId());
        modelMap.put("sellerInfo", sellerInfo);
        modelMap.put("storeReopenTip", 0);
        if (storeInfo.getIsOwnShop() == State.YES) {
            modelMap.put("storeEndTimeText", "不限制");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date storeEndTime = storeInfo.getStoreEndTime();
            modelMap.put("storeEndTimeText", sdf.format(storeEndTime));

            if (ShopHelper.getFutureTimestamp(storeInfo.getStoreEndTime(), Calendar.DATE, -30).before(ShopHelper.getCurrentTimestamp())) {
                modelMap.put("storeReopenTip", 1);
            }
        }
        //查询商家公告
        List<Article> storeNoticeList = articleService.getArticleListByPositionList(ArticlePositions.SELLER_NOTICE, 5);
        modelMap.put("storeNoticeList", storeNoticeList);
        //查询店铺等级
        StoreGrade storeGradeInfo = storeGradeDao.get(StoreGrade.class, storeInfo.getGradeId());
        modelMap.put("storeGradeInfo", storeGradeInfo);
        //平台联系方式
        HashMap<String, Object> siteConfigMap = new HashMap<>();
        String sitePhoneStr = siteService.getSiteInfo().get(SiteTitle.SITEPHONE).toString();
        if (sitePhoneStr.length()>0) {
            siteConfigMap.put("sitePhone", sitePhoneStr.split(","));
        }
        siteConfigMap.put("siteEmail", siteService.getSiteInfo().get(SiteTitle.SITEEMAIL).toString());
        modelMap.put("siteConfigMap", siteConfigMap);
        return "index";
    }
    /**
     * 30日内热销商品前10
     */
    @RequestMapping(value = "index/goodshotsale", method = RequestMethod.GET)
    public String ordersGoodsSaleTop(ModelMap modelMap) {
        List<StatOrdersGoodsVo> ordersGoodsList = statOrdersService.getOrdersGoodsByOrdersGoodsBuyNumAndStoreId(10, 30, SellerSessionHelper.getStoreId());
        modelMap.put("ordersGoodsList",ordersGoodsList);
        return getSellerTemplate("index/goods_hotsales");
    }
}