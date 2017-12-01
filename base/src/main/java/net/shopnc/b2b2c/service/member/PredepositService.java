package net.shopnc.b2b2c.service.member;


import net.shopnc.b2b2c.constant.PredepositCashState;
import net.shopnc.b2b2c.constant.PredepositLogOperationStage;
import net.shopnc.b2b2c.constant.PredepositRechargePayState;
import net.shopnc.b2b2c.dao.PaymentDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.PredepositCashDao;
import net.shopnc.b2b2c.dao.member.PredepositLogDao;
import net.shopnc.b2b2c.dao.member.PredepositRechargeDao;
import net.shopnc.b2b2c.domain.Payment;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.PredepositCash;
import net.shopnc.b2b2c.domain.member.PredepositLog;
import net.shopnc.b2b2c.domain.member.PredepositRecharge;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.b2b2c.vo.predeposit.PredepositCashMemberVo;
import net.shopnc.b2b2c.vo.predeposit.PredepositLogMemberVo;
import net.shopnc.b2b2c.vo.predeposit.PredepositRechargeMemberVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2015-12-24.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class PredepositService extends BaseService {
    @Autowired
    private PredepositLogDao predepositLogDao;
    @Autowired
    private PredepositRechargeDao predepositRechargeDao;
    @Autowired
    private PredepositCashDao predepositCashDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private SendMessageService sendMessageService;

    /**
     * 预存款日志列表分页
     * @param params
     * @param page
     * @return
     */
    public HashMap<String,Object> getPredepositLogListByPage(HashMap<String,Object> params, int page) {
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
            if (key.equals("availableAmountNotEq")) {
                where.put("availableAmount", "availableAmount <> :availableAmountNotEq");
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(predepositLogDao.findPredepositLogCount(where, params));
        pageEntity.setPageNo(page);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("list", predepositLogDao.getPredepositLogListByPage(where, params, pageEntity.getPageNo(), pageEntity.getPageSize()));
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    /**
     * 预存款充值
     * @param recharge
     * @return
     * @throws ShopException
     */
    public Serializable addRecharge(PredepositRecharge recharge) throws ShopException{
        if (PriceHelper.isGreaterThan(recharge.getAmount(), BigDecimal.ZERO).equals(false)) {
            throw new ShopException("充值金额应大于0");
        }
        if (recharge.getMemberId()<=0) {
            throw new ParameterErrorException();
        }
        String sn = "";
        boolean snAble = false;
        //验证该单号是否存在
        for(int i = 0; i<100; i++) {
            if (snAble == true) {
                break;
            }
            sn = this.getRechargeSn(recharge.getMemberId());
            PredepositRecharge predepositRecharge = predepositRechargeDao.getRechargeInfoByRechargeSn(sn);
            if (predepositRecharge == null) {
                snAble = true;
            }else{
                sn = "";
            }
        }
        if (sn.length() <= 0) {
            throw new ShopException("充值失败");
        }
        recharge.setRechargeSn(sn);
        recharge.setAddTime(ShopHelper.getCurrentTimestamp());
        recharge.setPayState(PredepositRechargePayState.NOTPAY);
        return predepositRechargeDao.save(recharge);
    }
    /**
     * 生成预存款充值单号
     * 生成规则：两位随机 + 从2000-01-01 00:00:00 到现在的秒数+微秒+会员ID%1000，该值会传给第三方支付接口
     * 长度 =2位 + 10位 + 3位 + 3位  = 18位
     * 1000个会员同一微秒提订单，重复机率为1/100
     * @param memberId
     * @return
     */
    private String getRechargeSn(int memberId){
        String sn = Integer.toString((int)Math.round(Math.random() * 89 + 10))
                + String.format("%010d", Integer.parseInt(Long.toString(System.currentTimeMillis() / 1000L)) - 946656000)
                + String.format("%03d", Math.round(Math.random() * 1000))
                + String.format("%03d", memberId % 1000);
        return sn;
    }
    /**
     * 充值列表分页
     * @param params
     * @param page
     * @return
     */
    public HashMap<String,Object> getRechargeListByPage(HashMap<String,Object> params, int page) {
        HashMap<String,String> whereCount = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("rechargeSn")) {
                whereCount.put("rechargeSn", "rechargeSn = :rechargeSn");
            }
            if (key.equals("memberId")) {
                whereCount.put("memberId", "memberId = :memberId");
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(predepositRechargeDao.findRechargeCount(whereCount, params));
        pageEntity.setPageNo(page);
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("rechargeSn")) {
                where.put("rechargeSn", "rechargeSn = :rechargeSn");
            }
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
        }
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("list", predepositRechargeDao.getRechargeListByPage(where, params, pageEntity.getPageNo(), pageEntity.getPageSize()));
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    /**
     * 提现列表分页
     * @param params
     * @param page
     * @return
     */
    public HashMap<String,Object> getCashListByPage(HashMap<String,Object> params, int page) {
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("cashSn")) {
                where.put("cashSn", "cashSn = :cashSn");
            }
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(predepositCashDao.findCashCount(where, params));
        pageEntity.setPageNo(page);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("list", predepositCashDao.getCashListByPage(where, params, pageEntity.getPageNo(), pageEntity.getPageSize()));
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    /**
     * 删除充值记录
     * @param params
     */
    public void deleteRechargeForNotpay(HashMap<String,Object> params)
    {
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
            if (key.equals("rechargeId")) {
                where.put("rechargeId", "rechargeId = :rechargeId");
            }
        }
        where.put("payState", "payState = :payState");
        //参数
        params.put("payState", PredepositRechargePayState.NOTPAY);
        predepositRechargeDao.deleteRecharge(where, params);
    }
    /**
     * 获取预存款明细列表表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getPredepositLogDtGridList(String dtGridPager) throws Exception {
        DtGrid dtGrid = predepositLogDao.getDtGridList(dtGridPager, PredepositLog.class);
        List<Object> logs = dtGrid.getExhibitDatas();
        List<Object> logList = new ArrayList<Object>();
        for (int j = 0; j < logs.size(); j++) {
            PredepositLog logInfo = (PredepositLog) logs.get(j);
            PredepositLogMemberVo predepositLogMemberInfo = new PredepositLogMemberVo();
            predepositLogMemberInfo.setLogId(logInfo.getLogId());
            predepositLogMemberInfo.setMemberId(logInfo.getMemberId());
            predepositLogMemberInfo.setAdminId(logInfo.getAdminId());
            predepositLogMemberInfo.setAdminName(logInfo.getAdminName());
            predepositLogMemberInfo.setOperationStage(logInfo.getOperationStage());
            predepositLogMemberInfo.setAvailableAmount(logInfo.getAvailableAmount());
            predepositLogMemberInfo.setFreezeAmount(logInfo.getFreezeAmount());
            predepositLogMemberInfo.setAddTime(logInfo.getAddTime());
            predepositLogMemberInfo.setDescription(logInfo.getDescription());
            //会员信息
            Member memberInfo = memberDao.get(Member.class, logInfo.getMemberId());
            if (memberInfo!=null && memberInfo.getMemberName()!=null && memberInfo.getMemberName().length()>0) {
                predepositLogMemberInfo.setMemberName(memberInfo.getMemberName());
            }else{
                predepositLogMemberInfo.setMemberName("");
            }
            logList.add(predepositLogMemberInfo);
        }
        dtGrid.setExhibitDatas(logList);
        return dtGrid;
    }
    /**
     * 获取预存款充值列表表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getPredepositRechargeDtGridList(String dtGridPager) throws Exception {
        DtGrid dtGrid = predepositRechargeDao.getDtGridList(dtGridPager, PredepositRecharge.class);
        List<Object> lists = dtGrid.getExhibitDatas();
        List<Object> logList = new ArrayList<Object>();
        for (int j = 0; j < lists.size(); j++) {
            PredepositRecharge info = (PredepositRecharge) lists.get(j);
            PredepositRechargeMemberVo predepositRechargeMemberInfo = new PredepositRechargeMemberVo();
            predepositRechargeMemberInfo.setRechargeId(info.getRechargeId());
            predepositRechargeMemberInfo.setRechargeSn(info.getRechargeSn());
            predepositRechargeMemberInfo.setMemberId(info.getMemberId());
            predepositRechargeMemberInfo.setAmount(info.getAmount());
            predepositRechargeMemberInfo.setPaymentCode(info.getPaymentCode());
            predepositRechargeMemberInfo.setPaymentName(info.getPaymentName());
            predepositRechargeMemberInfo.setTradeSn(info.getTradeSn());
            predepositRechargeMemberInfo.setAddTime(info.getAddTime());
            predepositRechargeMemberInfo.setPayState(info.getPayState());
            predepositRechargeMemberInfo.setPayStateText(info.getPayStateText());
            predepositRechargeMemberInfo.setPayTime(info.getPayTime());
            predepositRechargeMemberInfo.setAdminId(info.getAdminId());
            predepositRechargeMemberInfo.setAdminName(info.getAdminName());
            //会员信息
            Member memberInfo = memberDao.get(Member.class, info.getMemberId());
            if (memberInfo!=null && memberInfo.getMemberName()!=null && memberInfo.getMemberName().length()>0) {
                predepositRechargeMemberInfo.setMemberName(memberInfo.getMemberName());
            }else{
                predepositRechargeMemberInfo.setMemberName("");
            }
            logList.add(predepositRechargeMemberInfo);
        }
        dtGrid.setExhibitDatas(logList);
        return dtGrid;
    }
    /**
     * 审核充值信息
     * @param recharge
     * @return
     * @throws ShopException
     */
    public boolean auditRecharge(PredepositRecharge recharge) throws ShopException {
        if (recharge.getRechargeId() <= 0) {
            throw new ParameterErrorException();
        }
        if (recharge.getPayTime() == null) {
            throw new ParameterErrorException();
        }
        if (recharge.getPaymentCode() == null || recharge.getPaymentCode().length() <= 0 || recharge.getPaymentCode().equals("offline") || recharge.getPaymentCode().equals("predeposit")) {
            throw new ParameterErrorException();
        }
        if (recharge.getTradeSn() == null || recharge.getTradeSn().length() <= 0) {
            throw new ParameterErrorException();
        }
        //查询充值信息并验证是否为未支付记录
        PredepositRecharge rechargeInfo = predepositRechargeDao.get(PredepositRecharge.class, recharge.getRechargeId());
        if (rechargeInfo == null || rechargeInfo.getPayState() != PredepositRechargePayState.NOTPAY) {
            throw new ParameterErrorException();
        }
        //查询支付方式
        Payment payment = paymentDao.get(Payment.class, recharge.getPaymentCode());
        if (payment == null || payment.getPaymentCode().equals("offline") || payment.getPaymentCode().equals("predeposit")) {
            throw new ParameterErrorException();
        }
        HashMap<String, String> update = new HashMap<String, String>();
        update.put("payTime", "updatePayTime");
        update.put("paymentCode", "updatePaymentCode");
        update.put("paymentName", "updatePaymentName");
        update.put("tradeSn", "updateTradeSn");
        update.put("payState", "updatePayState");
        update.put("adminId", "updateAdminId");
        update.put("adminName", "updateAdminName");

        HashMap<String, String> where = new HashMap<String, String>();
        where.put("rechargeId", "whereRechargeId");
        where.put("payState", "wherePayState");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("whereRechargeId", recharge.getRechargeId());
        params.put("wherePayState", PredepositRechargePayState.NOTPAY);

        params.put("updatePayTime", recharge.getPayTime());
        params.put("updatePaymentCode", recharge.getPaymentCode());
        params.put("updatePaymentName", payment.getPaymentName());
        params.put("updateTradeSn", recharge.getTradeSn());
        params.put("updatePayState", PredepositRechargePayState.PAID);
        params.put("updateAdminId", recharge.getAdminId());
        params.put("updateAdminName", recharge.getAdminName());
        predepositRechargeDao.updateByWhere(where, update, params);
        //增加预存款日志并增加会员预存款
        this.addLogRecharge(rechargeInfo.getAmount(), rechargeInfo.getMemberId(), rechargeInfo.getRechargeSn(), recharge.getAdminId(), recharge.getAdminName());
        return true;
    }
    /**
     * 在线支付充值
     * @param rechargeSn
     * @param tradeSn
     * @param paymentCode
     * @return
     * @throws ShopException
     */
    public boolean onlineRechargePay(String rechargeSn, String tradeSn, String paymentCode)  throws ShopException{
        if (rechargeSn==null || rechargeSn.length()<=0) {
            return false;
        }
        //查询充值记录
        PredepositRecharge rechargeInfoTmp = predepositRechargeDao.getRechargeInfoByRechargeSn(rechargeSn);
        if (rechargeInfoTmp==null) {
            return false;
        }
        PredepositRecharge rechargeInfo = predepositRechargeDao.getRechargeInfoByRechargeIdAndLock(rechargeInfoTmp.getRechargeId());
        if (rechargeInfoTmp.getPayState() == PredepositRechargePayState.PAID) {
            return true;
        }
        //查询支付方式
        Payment payment = paymentDao.get(Payment.class, paymentCode);
        if (payment == null || payment.getPaymentCode().equals("offline") || payment.getPaymentCode().equals("predeposit")) {
            throw new ParameterErrorException();
        }
        //更新充值记录
        HashMap<String, String> update = new HashMap<String, String>();
        update.put("payTime", "updatePayTime");
        update.put("paymentCode", "updatePaymentCode");
        update.put("paymentName", "updatePaymentName");
        update.put("tradeSn", "updateTradeSn");
        update.put("payState", "updatePayState");

        HashMap<String, String> where = new HashMap<String, String>();
        where.put("rechargeId", "whereRechargeId");
        where.put("payState", "wherePayState");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("whereRechargeId", rechargeInfo.getRechargeId());
        params.put("wherePayState", PredepositRechargePayState.NOTPAY);

        params.put("updatePayTime", ShopHelper.getCurrentTimestamp());
        params.put("updatePaymentCode", paymentCode);
        params.put("updatePaymentName", payment.getPaymentName());
        params.put("updateTradeSn", tradeSn);
        params.put("updatePayState", PredepositRechargePayState.PAID);
        predepositRechargeDao.updateByWhere(where, update, params);
        //增加预存款日志并增加会员预存款
        this.addLogRecharge(rechargeInfo.getAmount(), rechargeInfo.getMemberId(), rechargeInfo.getRechargeSn(),0,"");
        return true;
    }
    /**
     * 获取预存款提现列表表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getPredepositCashDtGridList(String dtGridPager) throws Exception {
        DtGrid dtGrid = predepositCashDao.getDtGridList(dtGridPager, PredepositCash.class);
        List<Object> lists = dtGrid.getExhibitDatas();
        List<Object> logList = new ArrayList<Object>();
        for (int j = 0; j < lists.size(); j++) {
            PredepositCash info = (PredepositCash) lists.get(j);
            PredepositCashMemberVo predepositCashMemberInfo = new PredepositCashMemberVo();
            predepositCashMemberInfo.setCashId(info.getCashId());
            predepositCashMemberInfo.setCashSn(info.getCashSn());
            predepositCashMemberInfo.setMemberId(info.getMemberId());
            predepositCashMemberInfo.setAmount(info.getAmount());
            predepositCashMemberInfo.setReceiveCompany(info.getReceiveCompany());
            predepositCashMemberInfo.setReceiveAccount(info.getReceiveAccount());
            predepositCashMemberInfo.setReceiveUser(info.getReceiveUser());
            predepositCashMemberInfo.setAddTime(info.getAddTime());
            predepositCashMemberInfo.setPayTime(info.getPayTime());
            predepositCashMemberInfo.setState(info.getState());
            predepositCashMemberInfo.setStateText(info.getStateText());
            predepositCashMemberInfo.setAdminId(info.getAdminId());
            predepositCashMemberInfo.setAdminName(info.getAdminName());
            //会员信息
            Member memberInfo = memberDao.get(Member.class, info.getMemberId());
            if (memberInfo!=null && memberInfo.getMemberName()!=null && memberInfo.getMemberName().length()>0) {
                predepositCashMemberInfo.setMemberName(memberInfo.getMemberName());
            }else{
                predepositCashMemberInfo.setMemberName("");
            }
            logList.add(predepositCashMemberInfo);
        }
        dtGrid.setExhibitDatas(logList);
        return dtGrid;
    }
    /**
     * 预存款提现
     * @param cash
     * @param payPwd
     * @return
     * @throws ShopException
     */
    public Serializable addCash(PredepositCash cash, String payPwd) throws ShopException{
        if (PriceHelper.isGreaterThan(cash.getAmount(), BigDecimal.ZERO).equals(false)) {
            throw new ShopException("提现金额应大于0");
        }
        if (payPwd == null || payPwd.length()<=0) {
            throw new ShopException("请输入支付密码");
        }
        if (cash.getMemberId() <= 0) {
            throw new ParameterErrorException();
        }
        //验证支付密码
        Member member = memberDao.get(Member.class, cash.getMemberId());
        if (member==null || member.getPayPwd()==null || member.getPayPwd().length()<=0 || !member.getPayPwd().equals(ShopHelper.getMd5(payPwd))) {
            throw new ShopException("支付密码错误");
        }
        //验证金额是否足够
        if (PriceHelper.isGreaterThan(cash.getAmount(),member.getPredepositAvailable()).equals(true)) {
            throw new ShopException("预存款金额不足");
        }
        String sn = "";
        boolean snAble = false;
        //验证该单号是否存在
        for(int i = 0; i<100; i++) {
            if (snAble == true) {
                break;
            }
            sn = this.getCashSn(cash.getMemberId());
            PredepositCash predepositCash = predepositCashDao.getCashInfoByCashSn(sn);
            if (predepositCash == null) {
                snAble = true;
            }else{
                sn = "";
            }
        }
        if (sn.length() <= 0) {
            throw new ShopException("提现失败");
        }
        cash.setCashSn(sn);
        cash.setAddTime(ShopHelper.getCurrentTimestamp());
        cash.setState(PredepositCashState.NOTDEALWITH);
        Serializable cashId = predepositCashDao.save(cash);
        //增加预存款日志并冻结预存款
        this.addLogCashApply(cash.getAmount(), cash.getMemberId(), sn);
        return cashId;
    }
    /**
     * 生成预存款提现单号
     * 生成规则：两位随机 + 从2000-01-01 00:00:00 到现在的秒数+微秒+会员ID%1000
     * 长度 =2位 + 10位 + 3位 + 3位  = 18位
     * 1000个会员同一微秒提订单，重复机率为1/100
     * @param memberId
     * @return
     */
    private String getCashSn(int memberId){
        String sn = Integer.toString((int)Math.round(Math.random() * 89 + 10))
                + String.format("%010d", Integer.parseInt(Long.toString(System.currentTimeMillis() / 1000L)) - 946656000)
                + String.format("%03d", Math.round(Math.random() * 1000))
                + String.format("%03d", memberId % 1000);
        return sn;
    }
    /**
     * 预存款申请提现，冻结预存款
     * @param amount
     * @param memberId
     * @param cashSn
     * @return
     * @throws ShopException
     */
    public boolean addLogCashApply(BigDecimal amount, int memberId, String cashSn) throws ShopException {
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("申请提现，冻结预存款，提现单号: " + cashSn);
        log.setOperationStage(PredepositLogOperationStage.CASHAPPLY);
        //减少可用预存款
        log.setAvailableAmount(amount.negate());
        //增加冻结预存款
        log.setFreezeAmount(amount);
        int logId = this.addLog(log);
        //发送信息
        HashMap<String, Object> messageParams = new HashMap<>();
        messageParams.put("amount", amount.negate().toString());
        sendMessageService.sendMember("memberPredepositChange", memberId, messageParams, String.valueOf(logId));
        return true;
    }
    /**
     * 预存款提现成功，减少冻结预存款
     * @param amount
     * @param memberId
     * @param cashSn
     * @return
     * @throws ShopException
     */
    public boolean addLogCashPay(BigDecimal amount, int memberId, String cashSn) throws ShopException {
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("提现成功，提现单号: " + cashSn);
        log.setOperationStage(PredepositLogOperationStage.CASHPAY);
        //减少冻结预存款
        log.setFreezeAmount(amount.negate());
        this.addLog(log);
        return true;
    }
    /**
     * 拒绝预存款提现，解冻预存款
     * @param amount
     * @param memberId
     * @param cashSn
     * @param cashId
     * @return
     * @throws ShopException
     */
    public boolean addLogCashRefuse(BigDecimal amount, int memberId, String cashSn, int cashId) throws ShopException {
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("管理员拒绝提现，提现单号: " + cashSn);
        log.setOperationStage(PredepositLogOperationStage.CASHREFUSE);
        //解冻预存款
        log.setAvailableAmount(amount);
        log.setFreezeAmount(amount.negate());
        int logId = this.addLog(log);
        //发送信息
        HashMap<String, Object> messageParams = new HashMap<>();
        messageParams.put("amount", amount.toString());
        messageParams.put("cashSn", cashSn);
        sendMessageService.sendMember("memberPredepositCashFail", memberId, messageParams, String.valueOf(cashId));
        return true;
    }
    /**
     * 删除预存款提现，解冻预存款
     * @param amount
     * @param memberId
     * @param cashSn
     * @return
     * @throws ShopException
     */
    public boolean addLogCashDel(BigDecimal amount, int memberId, String cashSn) throws ShopException {
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("管理员删除提现记录，提现单号: " + cashSn);
        log.setOperationStage(PredepositLogOperationStage.CASHDEL);
        //解冻预存款
        log.setAvailableAmount(amount);
        log.setFreezeAmount(amount.negate());
        int logId = this.addLog(log);
        //发送信息
        HashMap<String, Object> messageParams = new HashMap<>();
        messageParams.put("amount", amount.toString());
        messageParams.put("cashSn", cashSn);
        sendMessageService.sendMember("memberPredepositCashFail", memberId, messageParams, String.valueOf(logId));
        return true;
    }
    /**
     * 充值成功，增加预存款
     * @param amount
     * @param memberId
     * @param rechargeSn
     * @return
     * @throws ShopException
     */
    public boolean addLogRecharge(BigDecimal amount, int memberId, String rechargeSn, int adminId, String adminName) throws ShopException {
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("充值，充值单号: " + rechargeSn);
        log.setOperationStage(PredepositLogOperationStage.RECHARGE);
        //增加可用预存款
        log.setAvailableAmount(amount);
        log.setAdminId(adminId);
        log.setAdminName(adminName);
        int logId = this.addLog(log);
        //发送信息
        HashMap<String, Object> messageParams = new HashMap<>();
        messageParams.put("amount", amount.toString());
        sendMessageService.sendMember("memberPredepositChange", memberId, messageParams, String.valueOf(logId));
        return true;
    }
    /**
     * 预存款完全独立支付成功
     * @param amount
     * @param memberId
     * @param ordersSn
     * @throws ShopException
     */
    public void addLogOrdersFullPay(BigDecimal amount, int memberId, long ordersSn) throws ShopException {
        logger.info("商品订单支付，扣除预存款，订单号: " + Long.toString(ordersSn));
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("商品订单支付，扣除预存款，订单号: " + Long.toString(ordersSn));
        log.setOperationStage(PredepositLogOperationStage.ORDERPAY);
        //减少可用预存款
        log.setAvailableAmount(amount.negate());
        int logId = this.addLog(log);
        //发送信息
        HashMap<String, Object> messageParams = new HashMap<>();
        messageParams.put("amount", amount.negate().toString());
        sendMessageService.sendMember("memberPredepositChange", memberId, messageParams, String.valueOf(logId));
    }
    /**
     * 预存款完成部分支付
     * @param amount
     * @param memberId
     * @param ordersSn
     * @throws ShopException
     */
    public void addLogOrdersPartPay(BigDecimal amount, int memberId, long ordersSn) throws ShopException {
        logger.info("商品订单支付，冻结预存款，订单号: " + Long.toString(ordersSn));
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("商品订单支付，冻结预存款，订单号: " + Long.toString(ordersSn));
        log.setOperationStage(PredepositLogOperationStage.ORDERPAY);
        //减少可用预存款
        log.setAvailableAmount(amount.negate());
        //增加冻结预存款
        log.setFreezeAmount(amount);
        int logId = this.addLog(log);
        //发送信息
        HashMap<String, Object> messageParams = new HashMap<>();
        messageParams.put("amount", amount.negate().toString());
        sendMessageService.sendMember("memberPredepositChange", memberId, messageParams, String.valueOf(logId));
    }
    /**
     * 第三方平台支付成功后彻底支付冻结的预存款
     * @param amount
     * @param memberId
     * @param ordersSn
     * @throws ShopException
     */
    public void addLogPaySuccessOrders(BigDecimal amount, int memberId, long ordersSn) throws ShopException {
        logger.info("商品订单支付，扣除冻结的预存款，订单号: " + Long.toString(ordersSn));
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("商品订单支付，扣除冻结的预存款，订单号: " + Long.toString(ordersSn));
        log.setOperationStage(PredepositLogOperationStage.ORDERPAY);
        //减少冻结预存款
        log.setFreezeAmount(amount.negate());
        log.setAvailableAmount(null);
        int logId = this.addLog(log);
        //发送信息(此时可用金额并未发生变化，不发站内通知)
//        HashMap<String, Object> messageParams = new HashMap<>();
//        messageParams.put("amount", amount.negate().toString());
//        sendMessageService.sendMember("memberPredepositChange", memberId, messageParams, String.valueOf(logId));
    }
    /**
     * 取消订单解冻预存款
     * @param amount
     * @param memberId
     * @param ordersSn
     * @throws ShopException
     */
    public void addLogCancelOrders(BigDecimal amount, int memberId, long ordersSn) throws ShopException {
        logger.info("取消商品订单，解冻预存款，订单号: " + Long.toString(ordersSn));
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("取消商品订单，解冻预存款，订单号: " + Long.toString(ordersSn));
        log.setOperationStage(PredepositLogOperationStage.ORDERCANCEL);
        //减少可用预存款
        log.setAvailableAmount(amount);
        //增加冻结预存款
        log.setFreezeAmount(amount.negate());
        int logId = this.addLog(log);
        //发送信息
        HashMap<String, Object> messageParams = new HashMap<>();
        messageParams.put("amount", amount.toString());
        sendMessageService.sendMember("memberPredepositChange", memberId, messageParams, String.valueOf(logId));
    }
    /**
     * 回退预存款
     * @param amount
     * @param memberId
     * @param refundSn
     * @return
     * @throws ShopException
     */
    public boolean addLogForRefund(BigDecimal amount, int memberId, String refundSn) throws ShopException {
        PredepositLog log = new PredepositLog();
        log.setMemberId(memberId);
        log.setDescription("退款成功，退货单号: " + refundSn);
        log.setOperationStage(PredepositLogOperationStage.REFUND);
        //减少可用预存款
        log.setAvailableAmount(amount);
        //增加冻结预存款
//        log.setFreezeAmount(amount);
        int logId = this.addLog(log);
        //发送信息
        HashMap<String, Object> messageParams = new HashMap<>();
        messageParams.put("amount", amount.toString());
        sendMessageService.sendMember("memberPredepositChange", memberId, messageParams, String.valueOf(logId));
        return true;
    }
    /**
     * 增加预存款日志
     * @param log
     * @return
     * @throws ShopException
     */
    private int addLog(PredepositLog log) throws ShopException {
        if (memberDao.get(Member.class, log.getMemberId()) == null) {
            throw new ParameterErrorException("会员信息不存在");
        }
        //增加预存款日志
        log.setAddTime(ShopHelper.getCurrentTimestamp());
        int logId = (Integer)predepositLogDao.save(log);
        if (logId > 0) {
            memberService.modifyPredeposit(log.getAvailableAmount(), log.getFreezeAmount(), log.getMemberId());
        }
        return logId;
    }
    /**
     * 审核提现信息
     * @param cash
     * @return
     * @throws ShopException
     */
    public boolean auditCash(PredepositCash cash) throws ShopException{
        if (cash.getState()!=PredepositCashState.SUCCESS && cash.getState()!=PredepositCashState.FAIL) {
            throw new ParameterErrorException();
        }
        //查询提现信息并验证是否为未处理记录
        PredepositCash cashInfo = predepositCashDao.get(PredepositCash.class, cash.getCashId());
        if (cashInfo == null || cashInfo.getState()!=PredepositCashState.NOTDEALWITH) {
            throw new ParameterErrorException();
        }
        if (cash.getState() == PredepositCashState.SUCCESS) {
            this.auditCashSuccess(cash, cashInfo);
        } else {
            this.auditCashFail(cash, cashInfo);
        }
        return true;
    }
    /**
     * 同意提现并支付完成
     * @param cash
     * @param cashInfo
     * @return
     * @throws ShopException
     */
    private boolean auditCashSuccess(PredepositCash cash,PredepositCash cashInfo) throws ShopException {
        if (cash.getPayTime()==null) {
            throw new ShopException("请选择付款时间");
        }
        HashMap<String, String> update = new HashMap<String, String>();
        update.put("payTime", "updatePayTime");
        update.put("state", "updateState");
        update.put("adminId", "updateAdminId");
        update.put("adminName", "updateAdminName");

        HashMap<String, String> where = new HashMap<String, String>();
        where.put("cashId", "whereCashId");
        where.put("state", "whereState");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("whereCashId", cashInfo.getCashId());
        params.put("whereState", PredepositCashState.NOTDEALWITH);

        params.put("updatePayTime", cash.getPayTime());
        params.put("updateState", PredepositCashState.SUCCESS);
        params.put("updateAdminId", cash.getAdminId());
        params.put("updateAdminName", cash.getAdminName());
        predepositCashDao.updateByWhere(where, update, params);
        //增加预存款日志并减少会员冻结预存款
        this.addLogCashPay(cashInfo.getAmount(), cashInfo.getMemberId(), cashInfo.getCashSn());
        return true;
    }
    /**
     * 拒绝提现
     * @param cash
     * @param cashInfo
     * @return
     * @throws ShopException
     */
    private boolean auditCashFail(PredepositCash cash,PredepositCash cashInfo) throws ShopException{
        if (cash.getRefuseReason()==null || cash.getRefuseReason().length()<=0) {
            throw new ShopException("请填写拒绝原因");
        }
        HashMap<String, String> update = new HashMap<String, String>();
        update.put("refuseReason", "updateRefuseReason");
        update.put("state", "updateState");
        update.put("adminId", "updateAdminId");
        update.put("adminName", "updateAdminName");

        HashMap<String, String> where = new HashMap<String, String>();
        where.put("cashId", "whereCashId");
        where.put("state", "whereState");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("whereCashId", cashInfo.getCashId());
        params.put("whereState", PredepositCashState.NOTDEALWITH);

        params.put("updateRefuseReason", cash.getRefuseReason());
        params.put("updateState", PredepositCashState.FAIL);
        params.put("updateAdminId", cash.getAdminId());
        params.put("updateAdminName", cash.getAdminName());
        predepositCashDao.updateByWhere(where, update, params);
        //增加预存款日志并解冻预存款
        this.addLogCashRefuse(cashInfo.getAmount(), cashInfo.getMemberId(), cashInfo.getCashSn(), cashInfo.getCashId());
        return true;
    }
    /**
     * 删除提现记录
     * @param params
     * @return
     * @throws ShopException
     */
    public boolean deleteCashForNotDealwith(HashMap<String,Object> params) throws ShopException{
        if (params.get("cashId")==null || (Integer)params.get("cashId")<=0) {
            throw new ParameterErrorException();
        }
        HashMap<String, String> where = new HashMap<String, String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
            if (key.equals("cashId")) {
                where.put("cashId", "cashId = :cashId");
            }
        }
        where.put("state", "state = :state");
        //参数
        params.put("state", PredepositCashState.NOTDEALWITH);
        //查询提现记录
        PredepositCash cashInfo = predepositCashDao.get(PredepositCash.class, (Integer) params.get("cashId"));
        //删除记录
        predepositCashDao.deleteCash(where, params);
        //增加预存款日志并解冻预存款
        this.addLogCashDel(cashInfo.getAmount(), cashInfo.getMemberId(), cashInfo.getCashSn());
        return true;
    }
    /**
     * 查询待处理提现记录数
     * @return
     */
    public long getNotDealwithCashCount(){
        HashMap<String,String> where = new HashMap<>();
        where.put("state", "state = :state");
        HashMap<String,Object> params = new HashMap<>();
        params.put("state", PredepositCashState.NOTDEALWITH);
        return predepositCashDao.findCashCount(where, params);
    }
}