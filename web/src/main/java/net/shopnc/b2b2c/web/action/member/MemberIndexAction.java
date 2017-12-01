package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.service.member.FavoritesGoodsService;
import net.shopnc.b2b2c.service.member.FavoritesStoreService;
import net.shopnc.b2b2c.vo.favorites.FavoritesGoodsVo;
import net.shopnc.b2b2c.vo.favorites.FavoritesStoreVo;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberIndexAction extends MemberBaseAction {

    @Autowired
    private FavoritesGoodsService favoritesGoodsService;
    @Autowired
    private FavoritesStoreService favoritesStoreService;
    @Autowired
    private OrdersDao ordersDao;

    @RequestMapping("")
    public String index(ModelMap modelMap) {
        return getMemberTemplate("index");
    }

    /**
     * 交易提醒
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "index/orders/list", method = RequestMethod.GET)
    public String ordersList(ModelMap modelMap) {
        /**
         * 最新3个交易订单
         */
        List<Object> condition = new ArrayList<>();
        condition.add("memberId = :memberId");
        condition.add("ordersState in (:ordersStateList)");
        HashMap<String,Object> map = new HashMap<>();
        map.put("ordersStateList", Arrays.asList(OrdersOrdersState.NEW, OrdersOrdersState.PAY, OrdersOrdersState.SEND, OrdersOrdersState.FINISH));
        map.put("memberId",SessionEntity.getMemberId());
        List<Object> ordersVoObjectList = ordersDao.getOrdersVoList(condition,map,1,3,"ordersState asc,ordersId desc");

        //取得订单商品
        List<Integer> ordersIdList = new ArrayList<Integer>();
        for (int i=0; i<ordersVoObjectList.size(); i++) {
            ordersIdList.add(((OrdersVo)ordersVoObjectList.get(i)).getOrdersId());
        }
        List<Object> ordersGoodsObjectList = ordersDao.getOrdersGoodsVoList(ordersIdList);

        //归档商品列表
        List<OrdersVo> ordersVoList = new ArrayList<OrdersVo>();
        for (int i=0; i<ordersVoObjectList.size(); i++) {
            OrdersVo ordersVo = (OrdersVo)ordersVoObjectList.get(i);
            List<OrdersGoodsVo> ordersGoodsVoList = new ArrayList<OrdersGoodsVo>();
            //循环商品
            for (int a=0; a<ordersGoodsObjectList.size(); a++) {
                OrdersGoodsVo ordersGoodsVo = (OrdersGoodsVo)ordersGoodsObjectList.get(a);
                if (ordersGoodsVo.getOrdersId() == ordersVo.getOrdersId()) {
                    ordersGoodsVoList.add(ordersGoodsVo);
                }
            }
            ordersVo.setOrdersGoodsVoList(ordersGoodsVoList);
            ordersVoList.add(ordersVo);
        }

        modelMap.put("ordersVoList",ordersVoList);
        return getMemberTemplate("index/orders");
    }

    /**
     * 商品关注
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "index/favorites/goods", method = RequestMethod.GET)
    public String favoritesGoods(ModelMap modelMap) {
        //查询列表
        HashMap<String,Object> listParams = new HashMap<String, Object>();
        listParams.put("memberId", SessionEntity.getMemberId());
        List<FavoritesGoodsVo> favoritesList = favoritesGoodsService.getFavoritesGoodsList(listParams, 50, "", "");
        modelMap.put("favoritesList", favoritesList);
        return getMemberTemplate("index/favorites_goods");
    }

    /**
     * 店铺关注
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "index/favorites/store", method = RequestMethod.GET)
    public String favoritesStore(ModelMap modelMap) {
        //查询列表
        HashMap<String,Object> listParams = new HashMap<String, Object>();
        listParams.put("memberId", SessionEntity.getMemberId());
        List<FavoritesStoreVo> favoritesList = favoritesStoreService.getFavoritesStoreList(listParams, 50, "", "");
        modelMap.put("favoritesList", favoritesList);
        return getMemberTemplate("index/favorites_store");
    }

}