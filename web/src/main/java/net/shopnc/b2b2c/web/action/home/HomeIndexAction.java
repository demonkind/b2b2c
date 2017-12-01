package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.b2b2c.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 首页
 * Created by shopnc.feng on 2015-12-03.
 */
@Controller
public class HomeIndexAction extends HomeBaseAction {

    @Autowired
    ArticleService articleService;
    @Autowired
    BrandDao brandDao;
    @Autowired
    GoodsDao goodsDao;

    /**
     * 首页
     * @param modelMap
     * @return
     */
    @RequestMapping("")
    public String index(ModelMap modelMap) {
        //查询商城快讯
        List<Article> newsArticleList = articleService.getArticleListByPositionList(ArticlePositions.SHOP_NEWS);
        modelMap.put("newsArticleList", newsArticleList);
        // 首页标记，只显示12个商品分类
        modelMap.put("webHome", "");

        //
        List<Brand> brandList1 = brandDao.findByBrandLabelId(11, 6);
        modelMap.put("brandList1", brandList1);
        //
        List<Brand> brandList2 = brandDao.findByBrandLabelId(10, 6);
        modelMap.put("brandList2", brandList2);
        //
        List<Brand> brandList3 = brandDao.findByBrandLabelId(8, 6);
        modelMap.put("brandList3", brandList3);
        //
        List<Brand> brandList4 = brandDao.findByBrandLabelId(6, 6);
        modelMap.put("brandList4", brandList4);
        //
        List<Brand> brandList5 = brandDao.findByBrandLabelId(4, 6);
        modelMap.put("brandList5", brandList5);
        //
        List<Brand> brandList6 = brandList3;
        modelMap.put("brandList6", brandList6);
        //
        List<Brand> brandList7 = brandList2;
        modelMap.put("brandList7", brandList7);
        //
        List<Brand> brandList8 = brandDao.findByBrandLabelId(9, 6);
        modelMap.put("brandList8", brandList8);

        //
        List<Object> goodsList1 = goodsDao.findGoodsVoByCategoryId1(1, 10);
        modelMap.put("goodsList1", goodsList1);

        //
        List<Object> goodsList2 = goodsDao.findGoodsVoByCategoryId2(1034, 10);
        modelMap.put("goodsList2", goodsList2);

        //
        List<Object> goodsList3 = goodsDao.findGoodsVoByCategoryId1(3, 10);
        modelMap.put("goodsList3", goodsList3);

        //
        List<Object> goodsList4 = goodsDao.findGoodsVoByCategoryId1(593, 10);
        modelMap.put("goodsList4", goodsList4);

        //
        List<Object> goodsList5 = goodsDao.findGoodsVoByCategoryId1(959, 10);
        modelMap.put("goodsList5", goodsList5);

        //
        List<Object> goodsList6 = goodsDao.findGoodsVoByCategoryId1(470, 10);
        modelMap.put("goodsList6", goodsList6);

        //
        List<Object> goodsList7 = goodsDao.findGoodsVoByCategoryId2(390, 10);
        modelMap.put("goodsList7", goodsList7);

        //
        List<Object> goodsList8 = goodsDao.findGoodsVoByCategoryId1(308, 10);
        modelMap.put("goodsList8", goodsList8);

        return getHomeTemplate("index");
    }
}