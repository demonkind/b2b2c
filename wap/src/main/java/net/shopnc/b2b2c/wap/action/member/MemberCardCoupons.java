package net.shopnc.b2b2c.wap.action.member;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.sf.json.JSONObject;
import net.shopnc.b2b2c.domain.member.Coupons;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.util.KmsHelper;
import net.shopnc.common.util.UtilsHelper;

/**
 * 卡券页面跳转
 * Created by zxy on 2016-03-14.
 */

@Controller
@RequestMapping("card")
public class MemberCardCoupons extends MemberBaseAction {

	
	@Autowired
    private MemberService memberService;
	
	/**
	 * 我的卡券页面
	 * @return
	 * @throws IOException 
	 */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap modelMap) throws IOException{
    	try {
    		List<Coupons> coupons =  memberService.getCouponsList(SessionEntity.getMemberId() , SessionEntity.getMemberName());
    		List<Coupons> coupons1 = new ArrayList<Coupons>();
    		List<Coupons> coupons0 = new ArrayList<Coupons>();
    		List<Coupons> coupons2 = new ArrayList<Coupons>();
			for(Coupons c : coupons){
				if(c.getIsUseful() == 2){
					coupons0.add(c);//已使用的卡券
				}else if (c.getIsUseful() == 3){
					coupons2.add(c);//已失效的卡券
				}else if (c.getIsUseful()==1  && c.getDueTime().getTime() <= System.currentTimeMillis()) {
					coupons2.add(c);//已失效的卡券
				}else if (c.getIsUseful()==1){
					coupons1.add(c);//未使用的
				}
			}
			modelMap.put("coupons1", coupons1);
			modelMap.put("coupons0", coupons0);
			modelMap.put("coupons2", coupons2);
		} catch (ShopException e) {
			e.printStackTrace();
		}
		return getMemberTemplate("card_coupons");
	}

    /**
	 * 卡券使用说明
	 * @return
	 */
    @RequestMapping(value = "/cardDetail", method = RequestMethod.GET)
   	public String cardDetail(@RequestParam(value = "couponsId")int couponsId,ModelMap modelMap){
    	//获取卡券信息
		try {
			Coupons coupons = memberService.findCoupons(couponsId);
			modelMap.put("coupons", coupons);
		} catch (ShopException e) {
			e.printStackTrace();
		}
   		return getMemberTemplate("card_detail");
   	}


}
