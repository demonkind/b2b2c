package net.shopnc.b2b2c.wap.action.home;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.service.ArticleService;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.common.util.UtilsHelper;

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
    
    @Autowired
    GoodsService goodsService;
    
    @Autowired
    GoodsActivityService goodsActivityService;

    /**
     * 首页
     * @param modelMap
     * @return
     */
    @RequestMapping("")
    public String index(ModelMap modelMap) {
    	
    	List<String> whereList = new  ArrayList<>();
    	whereList.add("at.StartTime is not null");
        //获取的所有商品数据
        List<Object> goodsList=goodsService.getGoodsActivityList(whereList,null,0,null);
        //根据商品信息，查询商品是否是活动信息、是否活动已经开始
        List<Object> goodsListDetail=new ArrayList<Object>();
        //循环便利所有商品数据，根据商品ID判断该商品是否有绑定过活动已经该商品的活动是什么类型
        for(int i=0;i<goodsList.size();i++){
        	boolean c=false;
        	GoodsVo gv=new GoodsVo();
        	gv=(GoodsVo) goodsList.get(i);
        	gv.setMaxWeight(gv.getMaxNum());
    		gv.setActivityType(gv.getActivityId());
    		if(new Date().getTime()<gv.getStartTime().getTime()){
				//活动未开始
				gv.setActivityIsStart(0);
			}else{
				//活动已开始
				gv.setActivityIsStart(1);
			}
//			if(gv.getActivityId().equals("2")){
//				gv.setMarkerPrice(Double.valueOf(0));
//			}
			goodsListDetail.add(gv);	
        }
        modelMap.put("goodsList9", goodsListDetail);
        return getHomeTemplate("index");
    }
    
	
	// TODO delete by yfb start =========================================================
//	GoodsActivity goodsActivity=new GoodsActivity();
//	//获取商品活动类型
//	goodsActivity=(GoodsActivity) goodsActivityService.checkBound(gv.getCommonId());
//	//判断时候有绑定过活动
//	if(goodsActivity!=null){
//		//判断商品是否已经设置活动
//		if(goodsActivity.getMaxNum()!=null){
//    		gv.setMaxWeight(goodsActivity.getMaxNum());
//    		gv.setActivityType(goodsActivity.getActivityId());
//    		//gv.setStartTime(new Timestamp(System.currentTimeMillis()));
//    		gv.setStartTime(goodsActivity.getStartTime());
//    		//查看活动是否已经开始
////        		&&!goodsActivity.getActivityType().equals("2")
//    		if(goodsActivity.getStartTime()!=null){
//    			if(new Date().getTime()<goodsActivity.getStartTime().getTime()){
//    				//活动未开始
//    				gv.setStartTime(new Timestamp(System.currentTimeMillis()));
//    				gv.setEndTime(goodsActivity.getStartTime());
//    				gv.setActivityIsStart(0);
//    				gv.setActivityType(goodsActivity.getActivityId());
//    			}else{
//    				//活动已开始
//    				gv.setActivityIsStart(1);
//    				gv.setEndTime(goodsActivity.getEndTime());
//    			}
//    			if(goodsActivity.getActivityId().equals("2")){
//    				gv.setMarkerPrice(Double.valueOf(0));
//    			}
//    		}
//		}else{
//			c=true;
//		}
//	}else{
//		c=true;
//	}
//	if(c){
//		gv.setActivityType("0");
//		gv.setActivityIsStart(0);
//		gv.setEndTime(null);
//			
//	}else{
//		goodsListDetail.add(gv);
//	}
//	

}