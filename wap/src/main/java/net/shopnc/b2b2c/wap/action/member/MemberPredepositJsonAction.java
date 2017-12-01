package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.dao.member.PredepositCashDao;
import net.shopnc.b2b2c.dao.member.PredepositRechargeDao;
import net.shopnc.b2b2c.domain.member.PredepositCash;
import net.shopnc.b2b2c.domain.member.PredepositRecharge;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberPredepositJsonAction extends MemberBaseJsonAction {
    @Autowired
    PredepositService predepositService;
    @Autowired
    PredepositCashDao predepositCashDao;
    @Autowired
    PredepositRechargeDao predepositRechargeDao;

    /**
     * 保存预存款充值信息
     * @param amount
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/recharge", method = RequestMethod.POST)
    public ResultEntity rechargeSave(@RequestParam(value = "amount") BigDecimal amount) {
        ResultEntity resultEntity = new ResultEntity();
        PredepositRecharge predepositRecharge = new PredepositRecharge();
        predepositRecharge.setAmount(amount);
        predepositRecharge.setMemberId(SessionEntity.getMemberId());
        try {
            Serializable rechargeId = predepositService.addRecharge(predepositRecharge);
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("rechargeId", rechargeId);
            resultEntity.setData(data);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("提交成功");
            return resultEntity;
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
    /**
     * 删除预存款充值记录
     * @param rechargeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/recharge/del", method = RequestMethod.POST)
    public ResultEntity rechargeDel(@RequestParam(value = "rechargeId") Integer rechargeId) {
        ResultEntity resultEntity = new ResultEntity();
        if (rechargeId <= 0) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        HashMap<String, Object> params = new HashMap<String,Object>();
        params.put("memberId", SessionEntity.getMemberId());
        params.put("rechargeId", rechargeId);
        predepositService.deleteRechargeForNotpay(params);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("删除成功");
        return resultEntity;
    }
    /**
     * 保存预存款提现记录
     * @param cash
     * @param bindingResult
     * @param payPwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/cash", method = RequestMethod.POST)
    public ResultEntity cashSave(@Valid PredepositCash cash,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "payPwd") String payPwd) {
        ResultEntity resultEntity = new ResultEntity();
        //获取表单错误信息
        if (bindingResult.hasErrors()) {
            String[] fieldArr = {"amount","receiveCompany","receiveAccount","receiveUser"};
            boolean isError = false;
            for(String field: fieldArr) {
                if (bindingResult.hasFieldErrors(field)) {
                    isError = true;
                    logger.info(bindingResult.getFieldError(field).getDefaultMessage());
                }
            }
            if (isError == true) {
                resultEntity.setMessage("提现失败");
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
        }
        cash.setMemberId(SessionEntity.getMemberId());
        try {
            Serializable cashId = predepositService.addCash(cash, payPwd);
            SessionEntity.destroySecurityAuthState();
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("cashId", cashId);
            resultEntity.setData(data);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("提交成功");
            return resultEntity;
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
}