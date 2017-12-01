package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class IndexAction extends BaseAction {

    @Autowired
    private GoodsCommonDao goodsCommonDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrdersDao ordersDao;

    @RequestMapping("")
    public String index(ModelMap modelMap) {
        //查询商品总数
        long goodsCommonCount = goodsCommonDao.findGoodsCommonCount(new ArrayList<String>(), new HashMap<String, Object>());
        modelMap.put("goodsCommonCount", goodsCommonCount);
        //店铺总数
        long storeCount = storeDao.findStoreCount(new ArrayList<String>(), new HashMap<String, Object>());
        modelMap.put("storeCount", storeCount);
        //会员总数
        long memberCount = memberDao.findMemberCount(new HashMap<String,String>(), new HashMap<String,Object>());
        modelMap.put("memberCount", memberCount);
        //订单总数
        long ordersCount = ordersDao.getOrdersCount(new ArrayList<Object>(), new HashMap<String, Object>());
        modelMap.put("ordersCount", ordersCount);
        return getAdminTemplate("index");
    }
}