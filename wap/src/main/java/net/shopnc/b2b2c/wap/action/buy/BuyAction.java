package net.shopnc.b2b2c.wap.action.buy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.dao.member.FavoritesGoodsDao;
import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.member.FavoritesGoods;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.buy.BuyService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.service.member.FavoritesGoodsService;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.vo.goods.GoodsDetailVo;
import net.shopnc.common.util.UtilsHelper;

/**
 * 购买
 * Created by hbj on 2015/12/11.
 */
@Controller
@RequestMapping("product")
public class BuyAction extends BuyBaseAction {
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private BuyService buyService;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    GoodsService goodsService;
    @Autowired
    StoreDao storeDao;
    @Autowired
    private FavoritesGoodsDao favoritesGoodsDao;
    
    @Autowired
    FavoritesGoodsService favoritesGoodsService;
    
    @Autowired
    GoodsActivityService goodsActivityService;
    
    @Autowired
    private MemberOrdersService memberOrdersService;
    
    /**
     * 跳转支付页面
     * @return
     */
    @RequestMapping(value = "pay/alipay", method = RequestMethod.GET)
    public String alipay( ModelMap modelMap) throws ShopException {
        return getBuyTemplate("order/vr_buy_step1");
    }
    
    /**
     * 跳转到商品页
     * @return
     */
    @RequestMapping(value = "productDetail")
    public String productDetail(HttpServletRequest request, ModelMap modelMap) throws ShopException {
    	Integer goodsId=Integer.valueOf(request.getParameter("goodsId"));
    	String key = request.getParameter("key");
    	GoodsDetailVo goodsDetail = goodsService.getDetail(goodsId);
    	GoodsActivity goodsActivity=new GoodsActivity();
    	//获取商品活动类型
    	goodsActivity=(GoodsActivity) goodsActivityService.checkBound(goodsDetail.getCommonId());
    	//判断时候有绑定过活动
    	if(goodsActivity!=null){
    		goodsDetail.setReturnAmount(UtilsHelper.getString(goodsActivity.getReturnAmount()));
    		goodsDetail.setGoodsBody(UtilsHelper.getString(goodsDetail.getGoodsBody()));
    		goodsDetail.setDescription(UtilsHelper.getString(goodsActivity.getDescription()));
    		goodsDetail.setActivityType(Integer.valueOf(goodsActivity.getActivityType()));
    		goodsDetail.setStartTime(new Timestamp(System.currentTimeMillis()));
    		goodsDetail.setMaxWeight(goodsActivity.getMaxNum());
    		goodsDetail.setMaxBuyNum(goodsActivity.getMaxNum());
    		//查看活动是否已经开始
//        		&&!goodsActivity.getActivityType().equals("2")
    		if(goodsActivity.getStartTime()!=null){
	    		if(new Date().getTime()<goodsActivity.getStartTime().getTime()){
	    			goodsDetail.setStartTime(goodsActivity.getStartTime());
	    			goodsDetail.setEndTime(goodsActivity.getEndTime());
	    			goodsDetail.setActivityIsStart(0);
	    			goodsDetail.setActivityType(1);
	    		}else{
	    			goodsDetail.setActivityIsStart(1);
	    			goodsDetail.setEndTime(goodsActivity.getEndTime());
	    			goodsDetail.setStartTime(goodsActivity.getStartTime());
	    		}
	    		//获取用户已经购买过的商品总数
	    		 if(!UtilsHelper.isEmpty(key)){
		            int manxNum=memberOrdersService.findTotalPurchasesOfGoods(goodsActivity.getCommonId(), Integer.valueOf(key));
		            //商铺最大限购数量
		            int maxWeight=Integer.valueOf(goodsActivity.getMaxNum())-manxNum;
		            goodsDetail.setMaxWeight(String.valueOf(maxWeight));
	    		 }else{
	    			 goodsDetail.setMaxWeight(String.valueOf(goodsActivity.getMaxNum()));
	    		 }
    		}else{
    			goodsDetail.setActivityType(0);
        		goodsDetail.setActivityIsStart(0);
        		goodsDetail.setEndTime(null);
    		}
//    		if(goodsActivity.getActivityType().equals("2")){
//    			goodsDetail.setMarkerPrice(BigDecimal.valueOf(0));
//    		}
    	}else{
    		goodsDetail.setActivityType(0);
    		goodsDetail.setActivityIsStart(0);
    		goodsDetail.setEndTime(null);
    	}
        String isFavorites="0"; 
        FavoritesGoods favoritesGoods=new FavoritesGoods();
        if(!UtilsHelper.isEmpty(key)){
            //判断是否已经关注
            HashMap<String,String> where = new HashMap<String, String>();
            where.put("goodsId", "goodsId = :goodsId");
            where.put("memberId", "memberId = :memberId");
            HashMap<String,Object> params = new HashMap<String, Object>();
            params.put("goodsId", goodsId);
            params.put("memberId", Integer.valueOf(key));
            favoritesGoods =favoritesGoodsService.findFavoritesById(params);
            if(favoritesGoods!=null){
            	isFavorites="1";
            	modelMap.put("favoritesId", favoritesGoods.getFavoritesId());
            }else{
            	modelMap.put("favoritesId", "");
            }
        }else{
        	modelMap.put("favoritesId", "");
        	goodsDetail.setMaxWeight(goodsActivity.getMaxNum());
        }
        modelMap.put("isFavorites", isFavorites);
        modelMap.put("goodsDetail", goodsDetail);
        return getBuyTemplate("product_detail");
    }
    
    /**
     * 马上抢页面跳转
     * @return
     */
    @RequestMapping(value="buyIt", method = RequestMethod.GET)
    public String buyIt(HttpServletRequest request, ModelMap modelMap) throws ShopException {
    	String goodsId=request.getParameter("goodsId");
    	String buynum=request.getParameter("buynum");
    	String key=request.getParameter("key");
    	GoodsDetailVo goodsDetail = goodsService.getDetail(Integer.valueOf(goodsId));
    	goodsDetail.setGoodsSaleNum(Integer.valueOf(buynum));
    	modelMap.put("goodsDetail", goodsDetail);
    	// 店铺信息
        Store storeInfo = storeDao.get(Store.class, goodsDetail.getStoreId());
        modelMap.put("storeInfo", storeInfo);
    	List<Address> addressList = addressDao.getAddressList(Integer.valueOf(key));
    	modelMap.put("addressList", addressList);
        return getBuyTemplate("order/buy_step1");
    }
    
    /**
     * 跳转到购物车页
     * @return
     */
    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request) {
        return getBuyTemplate("cart_list");
    }
    
    /**
     * 跳转到
     * @return
     */
    @RequestMapping("productEvalList")
    public String productEvalList(HttpServletRequest request) {
        return getBuyTemplate("product_eval_list");
    }
    
    /**
     * 跳转到详情页
     * @return
     */
    @RequestMapping("productInfo")
    public String productInfo(HttpServletRequest request) {
        return getBuyTemplate("product_info");
    }
    
    /**
     * 购买第一步[设置收货地址、发票等信息]
     *
     * @param buyItemList
     * @param isCart
     * @return
     */
    @RequestMapping(value = "goToBuy", method = RequestMethod.GET)
    public String buyFirst(HttpServletRequest request){
    	/*map.put("goodsId", request.getParameter("goodsId"));
    	map.put("buynum", request.getParameter("buynum"));
    	map.put("dataId", request.getParameter("dataId"));*/
    	return getBuyTemplate("order/buy_step1");
    }

}
