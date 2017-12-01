package net.shopnc.b2b2c.web.action.member;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.dao.member.PredepositCashDao;
import net.shopnc.b2b2c.dao.member.PredepositRechargeDao;
import net.shopnc.b2b2c.domain.member.Coupons;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;

/**
 * Created by yfb on 2016-05-30.
 */

@Controller
public class MemberCardCouponsAction extends MemberBaseAction {
    @Autowired
    PredepositService predepositService;
    @Autowired
    PredepositCashDao predepositCashDao;
    @Autowired
    PredepositRechargeDao predepositRechargeDao;

	@Autowired
    private MemberService memberService;

    
    /**
     * 预存款充值记录列表
     * @param rechargeSn
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "card/index", method = RequestMethod.GET)
    public String cardIndex(ModelMap modelMap) {

    	try {
    		List<Coupons> coupons =  memberService.getCouponsList(SessionEntity.getMemberId(), SessionEntity.getMemberName());
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
		return getMemberTemplate("card/index");
		
    }
    /**
     * 预存款充值详情
     * @param rechargeId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "card/cardDetail", method = RequestMethod.GET)
    public String rechargeInfo(@RequestParam(value = "couponsId")int couponsId,ModelMap modelMap) {
    	//获取卡券信息
		try {
			Coupons coupons = memberService.findCoupons(couponsId);
			modelMap.put("coupons", coupons);
		} catch (ShopException e) {
			e.printStackTrace();
		}
   		return getMemberTemplate("card/detail");
    }
    
}