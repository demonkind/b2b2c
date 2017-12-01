package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.dao.member.PredepositCashDao;
import net.shopnc.b2b2c.dao.member.PredepositRechargeDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.PredepositCash;
import net.shopnc.b2b2c.domain.member.PredepositRecharge;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberPredepositAction extends MemberBaseAction {
    @Autowired
    PredepositService predepositService;
    @Autowired
    PredepositCashDao predepositCashDao;
    @Autowired
    PredepositRechargeDao predepositRechargeDao;

    /**
     * 预存款日志列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "predeposit/log", method = RequestMethod.GET)
    public String index(@RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                        ModelMap modelMap) {
        //查询会员详情
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //查询列表
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("memberId",SessionEntity.getMemberId());
        params.put("availableAmountNotEq", BigDecimal.ZERO);
        HashMap<String,Object> result = predepositService.getPredepositLogListByPage(params, page);
        modelMap.put("logList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //menuKey
        modelMap.put("menuKey", "predeposit");
        return getMemberTemplate("predeposit/log");
    }
    /**
     * 预存款充值页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "predeposit/recharge", method = RequestMethod.GET)
    public String recharge(ModelMap modelMap) {
        //menuKey
        modelMap.put("menuKey", "predeposit");
        return getMemberTemplate("predeposit/recharge");
    }
    /**
     * 预存款充值记录列表
     * @param rechargeSn
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "predeposit/recharge/list", method = RequestMethod.GET)
    public String rechargeList(@RequestParam(value = "rechargeSn", required = false) String rechargeSn,
                               @RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                               ModelMap modelMap) {
        //查询会员详情
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //查询列表
        HashMap<String,Object> params = new HashMap<String, Object>();
        if (rechargeSn != null && !rechargeSn.equals("")) {
            params.put("rechargeSn", rechargeSn);
        }
        params.put("memberId", SessionEntity.getMemberId());
        HashMap<String,Object> result = predepositService.getRechargeListByPage(params, page);
        modelMap.put("rechargeList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //menuKey
        modelMap.put("menuKey", "predeposit");
        return getMemberTemplate("predeposit/recharge_list");
    }
    /**
     * 预存款充值详情
     * @param rechargeId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "predeposit/recharge/info/{id}", method = RequestMethod.GET)
    public String rechargeInfo(@PathVariable(value = "id") Integer rechargeId,
                           ModelMap modelMap) {
        if (rechargeId <= 0) {
            return "redirect:/member/predeposit/recharge/list";
        }
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("rechargeId", "rechargeId = :rechargeId");
        where.put("memberId", "memberId = :memberId");
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("rechargeId", rechargeId);
        params.put("memberId", SessionEntity.getMemberId());
        PredepositRecharge predepositRecharge = predepositRechargeDao.getRechargeInfo(where, params);
        if (predepositRecharge!=null) {
            modelMap.put("recharge", predepositRecharge);
            //menuKey
            modelMap.put("menuKey", "predeposit");
            return getMemberTemplate("predeposit/recharge_info");
        }else{
            return "redirect:/member/predeposit/recharge/list";
        }
    }
    /**
     * 预存款提现
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "predeposit/cash", method = RequestMethod.GET)
    public String cash(ModelMap modelMap) {
        if (SessionEntity.getSecurityAuthState()==false) {
            return "redirect:/member/security/auth/predepositcash";
        }
        //查询会员信息
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //menuKey
        modelMap.put("menuKey", "predeposit");
        return getMemberTemplate("predeposit/cash");
    }
    /**
     * 提现记录列表
     * @param cashSn
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "predeposit/cash/list", method = RequestMethod.GET)
    public String cashList(@RequestParam(value = "cashSn", required = false) String cashSn,
                           @RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                           ModelMap modelMap) {
        //查询会员详情
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //查询列表
        HashMap<String,Object> params = new HashMap<String, Object>();
        if (cashSn != null && !cashSn.equals("")) {
            params.put("cashSn", cashSn);
        }
        params.put("memberId", SessionEntity.getMemberId());
        HashMap<String,Object> result = predepositService.getCashListByPage(params, page);
        modelMap.put("cashList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //menuKey
        modelMap.put("menuKey", "predeposit");
        return getMemberTemplate("predeposit/cash_list");
    }
    /**
     * 提现详情
     * @param cashId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "predeposit/cash/info/{id}", method = RequestMethod.GET)
    public String cashInfo(@PathVariable(value = "id") Integer cashId,
                           ModelMap modelMap) {
        if (cashId <= 0) {
            return "redirect:/member/predeposit/cash/list";
        }
        PredepositCash predepositCash = predepositCashDao.get(PredepositCash.class, cashId);
        if (predepositCash.getMemberId() == SessionEntity.getMemberId()) {
            modelMap.put("cash", predepositCash);
            //menuKey
            modelMap.put("menuKey", "predeposit");
            return getMemberTemplate("predeposit/cash_info");
        }else{
            return "redirect:/member/predeposit/cash/list";
        }
    }
}