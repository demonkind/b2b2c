package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.dao.member.FavoritesGoodsDao;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.service.member.FavoritesGoodsService;
import net.shopnc.b2b2c.service.member.FavoritesStoreService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
@RequestMapping(value = "favorites")
public class MemberFavoritesAction extends MemberBaseAction {

    @Autowired
    private FavoritesGoodsService favoritesGoodsService;
    @Autowired
    private FavoritesGoodsDao favoritesGoodsDao;
    @Autowired
    private FavoritesStoreService favoritesStoreService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 商品关注列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/goods")
    public String index(@RequestParam(value = "page", required = false, defaultValue="5") Integer page,
                        ModelMap modelMap) {

        //查询关注商品总数
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("memberId", "memberId = :memberId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        Long count = favoritesGoodsDao.findFavoritesGoodsCount(where, params, "");
        modelMap.put("favoritesGoodsCount", count);
        modelMap.put("favoritesGoodsMaxPage", count/18);

        //查询列表
        HashMap<String,Object> result = this.getGoodsList(1, 18);
        modelMap.put("favoritesList", result.get("list"));
        //menuKey
        modelMap.put("menuKey", "favoritesGoods");
        return getMemberTemplate("favorites");
    }
    /**
     * 商品关注列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    public String goodsList(@RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                            ModelMap modelMap) {
        HashMap<String,Object> result = this.getGoodsList(page, 18);
        modelMap.put("favoritesList", result.get("list"));
        //menuKey
        modelMap.put("menuKey", "favoritesGoods");
        return getMemberTemplate("favorites/goods_pic_list");
    }
    /**
     * 查询商品关注列表
     * @param page
     * @param pageSize
     * @return
     */
    private HashMap<String,Object> getGoodsList(int page, int pageSize){
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        return favoritesGoodsService.getFavoritesGoodsListByPage(params, page, pageSize, "", "");
    }
    /**
     * 关注店铺列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store", method = RequestMethod.GET)
    public String storeList(@RequestParam(value = "page", required = false, defaultValue="5") Integer page,
                            ModelMap modelMap) {
        //查询列表
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        HashMap<String,Object> result = favoritesStoreService.getFavoritesStoreListByPage(params, page, 10, "", "");
        modelMap.put("favoritesList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //menuKey
        modelMap.put("menuKey", "favoritesStore");
        return getMemberTemplate("favorites_store");
    }
    /**
     * 店铺新品
     * @param storeId
     * @param page
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "favorites/store/newgoods", method = RequestMethod.GET)
    public String newGoodsList(@RequestParam(value = "storeid") Integer storeId,
                               @RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                               @RequestParam(value = "pagesize", required = false) Integer pageSize,
                               ModelMap modelMap) {
        if (pageSize==null || pageSize<=0) {
            pageSize = 0;
        }
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        HashMap<String, Object> result = goodsService.findNewGoodsVoByStoreId(storeId, page, pageSize);
        modelMap.put("goodsList", result.get("list"));
        return getMemberTemplate("favorites/store_list_newgoods");
    }
    /**
     * 热销商品
     * @param storeId
     * @param page
     * @param pageSize
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "favorites/store/hotgoods", method = RequestMethod.GET)
    public String hotGoodsList(@RequestParam(value = "storeid") Integer storeId,
                               @RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                               @RequestParam(value = "pagesize", required = false) Integer pageSize,
                               ModelMap modelMap) {
        if (pageSize==null || pageSize<=0) {
            pageSize = 0;
        }
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        HashMap<String, Object> result = goodsService.findHotGoodsVoByStoreId(storeId, page, pageSize);
        modelMap.put("goodsList", result.get("list"));
        return getMemberTemplate("favorites/store_list_hotgoods");
    }
}