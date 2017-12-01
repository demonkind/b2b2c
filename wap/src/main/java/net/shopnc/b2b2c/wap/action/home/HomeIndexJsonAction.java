package net.shopnc.b2b2c.wap.action.home;

import net.shopnc.b2b2c.dao.member.MemberMessageDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.service.member.ConsultService;
import net.shopnc.b2b2c.service.member.FavoritesGoodsService;
import net.shopnc.b2b2c.service.member.GoodsBrowseService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 首页
 * Created by shopnc.feng on 2015-12-03.
 */
@Controller
public class HomeIndexJsonAction extends HomeBaseJsonAction {
    @Autowired
    ConsultService consultService;
    @Autowired
    OrdersDao ordersDao;
    @Autowired
    FavoritesGoodsService favoritesGoodsService;
    @Autowired
    GoodsBrowseService goodsBrowseService;
    @Autowired
    MemberMessageDao memberMessageDao;

    /**
     * 查询顶部公用信息
     */
    @ResponseBody
    @RequestMapping("index/member/relateddate")
    public HashMap memberRelatedDate() {
        HashMap<String, Object> map = new HashMap<>();
        if (SessionEntity.getIsLogin() == true) {
            //查询咨询数量
            map.put("consultNoReadCount", consultService.getConsultNoReadCountByMemberId(SessionEntity.getMemberId()));
            //查询订单总数
            List<Object> where = new ArrayList<>();
            where.add("memberId = :memberId");
            HashMap<String, Object> params = new HashMap<>();
            params.put("memberId", SessionEntity.getMemberId());
            map.put("ordersCount", ordersDao.getOrdersCount(where, params));
            //我的关注
            map.put("favoritesGoodsCount", favoritesGoodsService.getFavoritesGoodsCountByMemberId(SessionEntity.getMemberId()));
            //我的足迹
            map.put("goodsBrowseCount", goodsBrowseService.getGoodsBrowseCountByMemberId(SessionEntity.getMemberId()));
            // 未读消息
            map.put("messageUnreadCount", memberMessageDao.findUnreadCountByMemberId(SessionEntity.getMemberId()));
        }
        return map;
    }


}