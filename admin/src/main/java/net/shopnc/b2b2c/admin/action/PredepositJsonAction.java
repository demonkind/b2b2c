package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.admin.util.AdminSessionHelper;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.PaymentDao;
import net.shopnc.b2b2c.dao.member.PredepositRechargeDao;
import net.shopnc.b2b2c.domain.Payment;
import net.shopnc.b2b2c.domain.member.PredepositCash;
import net.shopnc.b2b2c.domain.member.PredepositRecharge;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * 预存款管理
 * Created by zxy on 2016-03-14.
 */
@Controller
public class PredepositJsonAction extends BaseJsonAction {
    @Autowired
    PredepositService predepositService;
    @Autowired
    PredepositRechargeDao predepositRechargeDao;
    @Autowired
    PaymentDao paymentDao;
    /**
     * 预存款JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/log.json", method = RequestMethod.POST)
    public DtGrid logListJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = predepositService.getPredepositLogDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
    /**
     * 充值列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/recharge.json", method = RequestMethod.POST)
    public DtGrid rechargeListJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = predepositService.getPredepositRechargeDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
    /**
     * 审核充值信息
     * @param rechargeId
     * @param paymentCode
     * @param tradeSn
     * @param payTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/recharge/audit", method = RequestMethod.POST)
    public ResultEntity rechargeAudit(@RequestParam(value = "rechargeId") Integer rechargeId,
                                      @RequestParam(value = "paymentCode") String paymentCode,
                                      @RequestParam(value = "tradeSn") String tradeSn,
                                      @RequestParam(value = "payTime") Timestamp payTime) {
        ResultEntity resultEntity = new ResultEntity();

        PredepositRecharge rechargeUpdate = new PredepositRecharge();
        rechargeUpdate.setRechargeId(rechargeId);
        rechargeUpdate.setPayTime(payTime);
        rechargeUpdate.setPaymentCode(paymentCode);
        rechargeUpdate.setTradeSn(tradeSn);
        rechargeUpdate.setAdminId(AdminSessionHelper.getAdminId());
        rechargeUpdate.setAdminName(AdminSessionHelper.getAdminName());
        try {
            predepositService.auditRecharge(rechargeUpdate);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("审核成功");
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
        params.put("rechargeId", rechargeId);
        predepositService.deleteRechargeForNotpay(params);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("删除成功");
        return resultEntity;
    }
    /**
     * 查询支付方式
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/payment", method = RequestMethod.GET)
    public ResultEntity getPayment() {
        ResultEntity resultEntity = new ResultEntity();
        List<Payment> paymentList = paymentDao.findAll(Payment.class);
        HashMap<String,Payment> paymentMap = new HashMap<String,Payment>();
        for (int i = 0, len = paymentList.size(); i < len; i++) {
            String paymentCode = paymentList.get(i).getPaymentCode();
            if (paymentList.get(i).getPaymentState()==State.YES && !paymentCode.equals("predeposit") && !paymentCode.equals("offline")) {
                paymentMap.put(paymentCode, paymentList.get(i));
            }
        }
        resultEntity.setData(paymentMap);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
    /**
     * 提现列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/cash.json", method = RequestMethod.POST)
    public DtGrid cashListJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = predepositService.getPredepositCashDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
    /**
     * 审核提现信息
     * @param cashId
     * @param state
     * @param payTime
     * @param refuseReason
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/cash/audit", method = RequestMethod.POST)
    public ResultEntity cashEdit(@RequestParam(value = "cashId") Integer cashId,
                                 @RequestParam(value = "state") Integer state,
                                 @RequestParam(value = "payTime", required = false) Timestamp payTime,
                                 @RequestParam(value = "refuseReason", required = false) String refuseReason) {
        ResultEntity resultEntity = new ResultEntity();
        if (state == null) {
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        if (cashId == null || cashId <= 0) {
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        PredepositCash cash = new PredepositCash();
        cash.setCashId(cashId);
        cash.setState(state);
        cash.setPayTime(payTime);
        cash.setRefuseReason(refuseReason);
        cash.setAdminId(AdminSessionHelper.getAdminId());
        cash.setAdminName(AdminSessionHelper.getAdminName());
        try {
            predepositService.auditCash(cash);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("审核成功");
            return resultEntity;
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
    /**
     * 删除提现记录
     * @param cashId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "predeposit/cash/del", method = RequestMethod.POST)
    public ResultEntity cashDel(@RequestParam(value = "cashId") Integer cashId) {
        ResultEntity resultEntity = new ResultEntity();
        if (cashId <= 0) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        HashMap<String, Object> params = new HashMap<String,Object>();
        params.put("cashId", cashId);
        try{
            predepositService.deleteCashForNotDealwith(params);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
            return resultEntity;
        }catch (ShopException e){
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("删除失败");
            return resultEntity;
        }
    }
}
