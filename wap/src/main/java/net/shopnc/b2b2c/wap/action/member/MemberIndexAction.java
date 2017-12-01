package net.shopnc.b2b2c.wap.action.member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.shopnc.b2b2c.constant.OrdersOrdersState;
import net.shopnc.b2b2c.dao.member.FavoritesGoodsDao;
import net.shopnc.b2b2c.dao.member.FavoritesStoreDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.FavoritesGoodsService;
import net.shopnc.b2b2c.service.member.FavoritesStoreService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.vo.favorites.FavoritesGoodsVo;
import net.shopnc.b2b2c.vo.favorites.FavoritesStoreVo;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
@RequestMapping("members")
public class MemberIndexAction extends MemberBaseAction {

    @Autowired
    private FavoritesGoodsService favoritesGoodsService;
    @Autowired
    private FavoritesStoreService favoritesStoreService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private MemberService memberService;
    

    @Autowired
    private FavoritesGoodsDao favoritesGoodsDao;
    

    @Autowired
    private FavoritesStoreDao favoritesStoreDao;
    

    @Autowired
    private MemberOrdersService memberOrdersService;

    /**
     * 跳转到我的界面
     * @return
     * @throws ShopException 
     */
    @RequestMapping("index")
    public String index(ModelMap modelMap) throws ShopException {
    	int memberid = SessionEntity.getMemberId();
    	if(memberid > 0){
    		Member member = memberDao.get(Member.class, memberid);
    		modelMap.put("memberId", 0);
    		modelMap.put("member", member);
    		modelMap.put("memberId", member.getMemberId());
    		
    		//查询关注商品总数
            HashMap<String,String> where = new HashMap<String,String>();
            where.put("memberId", "memberId = :memberId");
            HashMap<String,Object> params = new HashMap<String, Object>();
            params.put("memberId", member.getMemberId());
            Long count = favoritesGoodsDao.findFavoritesGoodsCount(where, params, "");
            modelMap.put("favoritesGoodsCount", count);
            
            // 关注的店铺数量
            Long favoritesStoresCount = favoritesStoreDao.findFavoritesStoreCount(where, params, "");
            modelMap.put("favoritesStoresCount", favoritesStoresCount);
    		
            // 订单待付款数量   0-已取消 10-未支付 20-已支付 30-已发货 40-已收货 50-已评价
            
            // 待付款
            long orderNopayCount = 0;
            params = new HashMap<String, Object>();
            params.put("memberId", SessionEntity.getMemberId());
            params.put("ordersState", OrdersOrdersState.NEW_STR);
            orderNopayCount = memberOrdersService.getMemberOrdersCount(params);
            modelMap.put("orderNopayCount", orderNopayCount);
            
            // 待发货
            long orderNoreceiptCount = 0;
            params.put("ordersState", OrdersOrdersState.PAY_STR);
            orderNoreceiptCount = memberOrdersService.getMemberOrdersCount(params);
            modelMap.put("orderNoreceiptCount", orderNoreceiptCount);
            
            // 待收货
            long orderNotakesCount = 0;
            params.put("ordersState", OrdersOrdersState.SEND_STR);
            orderNotakesCount = memberOrdersService.getMemberOrdersCount(params);
            modelMap.put("orderNotakesCount", orderNotakesCount);
            
            // 已收货 待评价
            long orderNoevalCount = 0;
            params.put("ordersState", OrdersOrdersState.FINISH_STR);
            orderNoevalCount = memberOrdersService.getMemberOrdersCount(params);
            modelMap.put("orderNoevalCount", orderNoevalCount);
            
            // 已取消
            long orderReturnCount = 0;
            params.put("ordersState", OrdersOrdersState.CANCEL_STR);
            orderReturnCount = memberOrdersService.getMemberOrdersCount(params);
            modelMap.put("orderReturnCount", orderReturnCount);
    	}
        return getMemberTemplate("member");
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