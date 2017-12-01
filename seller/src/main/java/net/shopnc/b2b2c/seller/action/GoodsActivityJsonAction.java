package net.shopnc.b2b2c.seller.action;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.common.entity.ResultEntity;

@Controller
public class GoodsActivityJsonAction  extends BaseJsonAction  {
		@Autowired
		GoodsActivityService goodsActivityService;
		 @Autowired
		 GoodsDao goodsDao;
		
		@RequestMapping(value = "/goodsActivityUpdate", method = RequestMethod.POST)
		@ResponseBody
		public ResultEntity goodsActivityUpdate(HttpServletRequest request){
			ResultEntity resultEntity=new ResultEntity();
			String goodsActivityId =request.getParameter("goodsActivityId");
			String commonId=request.getParameter("commonId");
			String activityId=request.getParameter("activityId");
			String startTime=request.getParameter("startTime");
			String endTime=request.getParameter("endTime");
			String weight=request.getParameter("weight");
			String maxNum=request.getParameter("maxNum");
			String cartType=request.getParameter("cartType");
			String sendKCodeType=request.getParameter("sendKCodeType");
			String returnAmount=request.getParameter("returnAmount");
			String description=request.getParameter("description");
			List<Map<String, Object>> saleAndAcount=goodsDao.getProductSaleNumByCommonId(Integer.valueOf(commonId)); 
			for(int i=0;i<saleAndAcount.size();i++){
	            if(saleAndAcount!=null&&saleAndAcount.size()>0){
	            	Map<String,Object> map=saleAndAcount.get(i);
	            	int saleNum=Integer.valueOf(map.get("0").toString());
	                if(Integer.valueOf(maxNum)>saleNum){
	            	   resultEntity.setMessage("限购数量超过单个属性库存数量!");
	            	   return resultEntity;
	                }
	            }
			}
			GoodsActivity goodsActivity=new GoodsActivity();
			goodsActivity.setGoodsActivityId(Integer.valueOf(goodsActivityId));
			goodsActivity.setActivityId(activityId);
			goodsActivity.setStartTime(this.StrToDate(startTime));
			goodsActivity.setEndTime(this.StrToDate(endTime));
			goodsActivity.setWeight(weight);
			goodsActivity.setMaxNum(maxNum);
			goodsActivity.setCommonId(Integer.valueOf(commonId));
			goodsActivity.setCartType(cartType);
			goodsActivity.setSaleWeight(weight);
			goodsActivity.setSendKCodeType(sendKCodeType);
			if(returnAmount==null||returnAmount.equals("")){
				returnAmount="0";
			}
			goodsActivity.setReturnAmount(returnAmount);
			goodsActivity.setDescription(description);
			goodsActivity.setActivityType(activityId);
			int i=goodsActivityService.goodsActivityUpdate(goodsActivity);
			if(i>0){
				resultEntity.setCode(200);
				resultEntity.setMessage("设置成功!");
			}else{
				resultEntity.setMessage("设置失败!");
			}
			return resultEntity;
		}
		
		@RequestMapping(value = "/goodsActivityByGoodsId", method = RequestMethod.POST)
		@ResponseBody
		public ResultEntity goodsActivityByGoodsId(HttpServletRequest request){
			ResultEntity resultEntity=new ResultEntity();
			String goodsId =request.getParameter("goodsId");
			if(goodsId==null||goodsId.equals("")){
				goodsId="0";
			}
			resultEntity.setData(goodsActivityService.checkBound(Integer.valueOf(goodsId)));
			return resultEntity;
		}
		
		
		
		/**
		* 字符串转换成日期
		* @param str
		* @return date
		*/
		public static Timestamp StrToDate(String str) {
		  
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   Date date = null;
		   try {
		    date = format.parse(str);
		   } catch (ParseException e) {
		    e.printStackTrace();
		   }
		   return new Timestamp(date.getTime());
		}
}
