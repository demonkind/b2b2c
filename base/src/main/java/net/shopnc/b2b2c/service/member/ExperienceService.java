package net.shopnc.b2b2c.service.member;


import net.shopnc.b2b2c.constant.ExpPointsLogStage;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.dao.member.ExpPointsLogDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.MemberGradeDao;
import net.shopnc.b2b2c.domain.member.ExpPointsLog;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.MemberGrade;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.vo.ExpPointsLogMemberVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2015-11-27.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ExperienceService extends BaseService {

    @Autowired
    private ExpPointsLogDao expPointsLogDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberGradeDao memberGradeDao;
    @Autowired
    private SiteService siteService;

    /**
     * 获取经验明细列表表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getExpPointsLogDtGridList(String dtGridPager) throws Exception {
        DtGrid dtGrid = expPointsLogDao.getDtGridList(dtGridPager, ExpPointsLog.class);

        List<Object> logs = dtGrid.getExhibitDatas();
        List<Object> expPointsLogMemberList = new ArrayList<Object>();
        for (int j = 0; j < logs.size(); j++) {
            ExpPointsLog logInfo = (ExpPointsLog) logs.get(j);
            ExpPointsLogMemberVo expPointsLogMemberInfo = new ExpPointsLogMemberVo();
            expPointsLogMemberInfo.setLogId(logInfo.getLogId());
            expPointsLogMemberInfo.setMemberId(logInfo.getMemberId());
            expPointsLogMemberInfo.setPoints(logInfo.getPoints());
            expPointsLogMemberInfo.setAddTime(logInfo.getAddTime());
            expPointsLogMemberInfo.setDescription(logInfo.getDescription());
            expPointsLogMemberInfo.setOperationStage(logInfo.getOperationStage());

            Member memberInfo = memberDao.get(Member.class, logInfo.getMemberId());
            if (memberInfo != null) {
                expPointsLogMemberInfo.setMemberName(memberInfo.getMemberName());
            }else{
                expPointsLogMemberInfo.setMemberName("");
            }
            expPointsLogMemberList.add(expPointsLogMemberInfo);
        }
        dtGrid.setExhibitDatas(expPointsLogMemberList);
        return dtGrid;
    }
    /**
     * 注册增加经验
     * @param memberId
     * @return
     */
    public boolean addExperienceRegister(int memberId){
        ExpPointsLog expPointsLog = new ExpPointsLog();
        int expPoints = 0;
        try{
            expPoints = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.EXPRULEREGISTER));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (expPoints <= 0) {
            return false;
        }
        expPointsLog.setPoints(expPoints);
        expPointsLog.setDescription("注册会员");
        expPointsLog.setMemberId(memberId);
        expPointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        expPointsLog.setOperationStage(ExpPointsLogStage.REGISTER);
        try{
            return this.addExperience(expPointsLog);
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            return false;
        }
    }
    /**
     * 登录增加经验
     * @param memberId
     * @return
     */
    public boolean addExperienceLogin(int memberId){
        //查询当天是否已经赠送经验
        HashMap<String,String> where = new HashMap<>();
        where.put("memberId", "memberId = :memberId");
        where.put("operationStage", "operationStage = :operationStage");
        where.put("addTimeGt", "addTime >= :addTimeGt");
        where.put("addTimeLt", "addTime < :addTimeLt");

        HashMap<String,Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("operationStage", ExpPointsLogStage.LOGIN);
        params.put("addTimeGt", ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp()));
        params.put("addTimeLt", ShopHelper.getTimestampOfDayEnd(ShopHelper.getCurrentTimestamp()));
        long count = expPointsLogDao.findExppointsLogCount(where, params);
        if (count > 0) {
            return true;
        }
        ExpPointsLog expPointsLog = new ExpPointsLog();
        int expPoints = 0;
        try{
            expPoints = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.EXPRULELOGIN));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (expPoints <= 0) {
            return false;
        }
        expPointsLog.setPoints(expPoints);
        expPointsLog.setDescription("会员登录");
        expPointsLog.setMemberId(memberId);
        expPointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        expPointsLog.setOperationStage(ExpPointsLogStage.LOGIN);
        try{
            return this.addExperience(expPointsLog);
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            return false;
        }
    }
    /**
     * 订单完成增加经验
     * @param memberId
     * @param ordersAmount
     * @param ordersSn
     * @return
     */
    public boolean addExperienceOrders(int memberId, BigDecimal ordersAmount, long ordersSn){
        ExpPointsLog expPointsLog = new ExpPointsLog();
        int orderrate = 0;
        int ordermax = 0;
        try{
            orderrate = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.EXPRULEORDERRATE));
            ordermax = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.EXPRULEORDERMAX));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (orderrate <= 0 || PriceHelper.isLessThanOrEquals(ordersAmount, BigDecimal.ZERO)) {
            return false;
        }
        //计算积分数
        int expPoints = 0;
        try {
            expPoints = (PriceHelper.mul(PriceHelper.div(ordersAmount, 100), orderrate)).intValue();
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (expPoints <= 0) {
            return false;
        }
        //大于最大值则按照最大值累计经验
        if (ordermax>0 && expPoints>ordermax) {
            expPoints = ordermax;
        }
        expPointsLog.setPoints(expPoints);
        expPointsLog.setDescription("订单" + String.valueOf(ordersSn) + "购物消费");
        expPointsLog.setMemberId(memberId);
        expPointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        expPointsLog.setOperationStage(ExpPointsLogStage.ORDERS);
        try{
            return this.addExperience(expPointsLog);
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            return false;
        }
    }
    /**
     * 订单评价增加经验
     * @param memberId
     * @return
     */
    public boolean addExperienceComments(int memberId){
        ExpPointsLog expPointsLog = new ExpPointsLog();
        int expPoints = 0;
        try{
            expPoints = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.EXPRULECOMMENTS));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (expPoints <= 0) {
            return false;
        }
        expPointsLog.setPoints(expPoints);
        expPointsLog.setDescription("评论商品");
        expPointsLog.setMemberId(memberId);
        expPointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        expPointsLog.setOperationStage(ExpPointsLogStage.COMMENTS);
        try{
            return this.addExperience(expPointsLog);
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            return false;
        }
    }
    /**
     * 增加经验
     * @param expPointsLog
     * @return
     * @throws ParameterErrorException
     */
    public boolean addExperience(ExpPointsLog expPointsLog) throws ParameterErrorException {
        Member member = memberDao.get(Member.class, expPointsLog.getMemberId());
        if (member == null) {
            throw new ParameterErrorException("会员信息不存在");
        }
        //增加经验日志
        int logId = (Integer)expPointsLogDao.save(expPointsLog);
        if (logId > 0) {
            //更新会员经验
            HashMap<String,Object> updateMap = new HashMap<String, Object>();
            updateMap.put("experiencePoints", member.getExperiencePoints() + expPointsLog.getPoints());
            memberDao.updateByMemberId(updateMap, expPointsLog.getMemberId());
        }else{
            return false;
        }
        return true;
    }
    /**
     * 根据经验值获得会员当前等级信息
     * @param expPoints
     * @return
     */
    public HashMap<String, Object> getMemberGrade(int expPoints){
        return this.getOneMemberGrade(expPoints, false, null);
    }
    /**
     * 根据经验值和等级列表获得会员当前等级信息
     * @param expPoints
     * @param memberGradeList
     * @return
     */
    public HashMap<String, Object> getMemberGrade(int expPoints, List<MemberGrade> memberGradeList){
        return this.getOneMemberGrade(expPoints, false, memberGradeList);
    }
    /**
     * 根据经验值获得会员当前等级和进度
     * @param expPoints
     * @return
     */
    public HashMap<String, Object> getMemberGradeAndProgress(int expPoints){
        return this.getOneMemberGrade(expPoints, true, null);
    }
    /**
     * 根据经验值和等级列表获得会员当前等级和进度
     * @param expPoints
     * @param memberGradeList
     * @return
     */
    public HashMap<String, Object> getMemberGradeAndProgress(int expPoints, List<MemberGrade> memberGradeList){
        return this.getOneMemberGrade(expPoints, true, memberGradeList);
    }
    /**
     * 获得某一会员等级
     * @param expPoints
     * @param showProgress
     * @param memberGradeList
     * @return
     */
    public HashMap<String, Object> getOneMemberGrade(int expPoints, boolean showProgress, List<MemberGrade> memberGradeList){
        if (memberGradeList == null || memberGradeList.size() <= 0){
            memberGradeList = memberGradeDao.getGradeList();
        }
        HashMap<String, Object> gradeMap = new HashMap<String, Object>();
        if (memberGradeList == null || memberGradeList.size() <= 0){//如果会员等级设置为空
            gradeMap.put("gradeLevel", -1);
            gradeMap.put("gradeName", "暂无等级");
            return gradeMap;
        }
        int curLevel = 0;
        for(MemberGrade info:memberGradeList) {
            if (expPoints >= info.getExpPoints()) {
                curLevel = info.getGradeLevel();
                gradeMap.put("gradeLevel", info.getGradeLevel());
                gradeMap.put("gradeName", info.getGradeName());
            }
        }
        if (gradeMap==null || gradeMap.size()<=0) {
            gradeMap.put("gradeLevel", -1);
            gradeMap.put("gradeName", "暂无等级");
            return gradeMap;
        }
        //计算提升进度
        if (showProgress == true) {
            //如果已达到顶级会员
            if (curLevel >= memberGradeList.size()-1) {
                for(MemberGrade info:memberGradeList) {
                    //下一级会员等级
                    if (info.getGradeLevel() == curLevel-1) {
                        gradeMap.put("downGradeLevel", info.getGradeLevel());
                        gradeMap.put("downGradeName", info.getGradeName());
                        gradeMap.put("downGradeExppoints", info.getExpPoints());
                    }
                    //上一级会员等级
                    if (info.getGradeLevel() == curLevel) {
                        gradeMap.put("upGradeLevel", info.getGradeLevel());
                        gradeMap.put("upGradeName", info.getGradeName());
                        gradeMap.put("upGradeExppoints", info.getExpPoints());
                    }
                }
                gradeMap.put("lessExppoints", 0);
                gradeMap.put("exppointsRate", 100);
            }else{
                for(MemberGrade info:memberGradeList) {
                    //下一级会员等级
                    if (info.getGradeLevel() == curLevel) {
                        gradeMap.put("downGradeLevel", info.getGradeLevel());
                        gradeMap.put("downGradeName", info.getGradeName());
                        gradeMap.put("downGradeExppoints", info.getExpPoints());
                    }
                    //上一级会员等级
                    if (info.getGradeLevel() == curLevel+1) {
                        gradeMap.put("upGradeLevel", info.getGradeLevel());
                        gradeMap.put("upGradeName", info.getGradeName());
                        gradeMap.put("upGradeExppoints", info.getExpPoints());
                    }
                }
                gradeMap.put("lessExppoints", (Integer) gradeMap.get("upGradeExppoints") - expPoints);
                //计算比率
                float downGradeExppoints = Float.parseFloat(gradeMap.get("downGradeExppoints").toString());
                float upGradeExppoints = Float.parseFloat(gradeMap.get("upGradeExppoints").toString());
                float rate = ((float)expPoints - downGradeExppoints)/(upGradeExppoints - downGradeExppoints);
                rate = ((float)Math.round(rate * 10000))/100;
                gradeMap.put("exppointsRate", rate);
            }
        }
        return gradeMap;
    }
    /**
     * 经验值列表分页
     * @param params
     * @param page
     * @return
     */
    public HashMap<String,Object> getExppointsLogListByPage(HashMap<String,Object> params, int page) {
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(expPointsLogDao.findExppointsLogCount(where, params));
        pageEntity.setPageNo(page);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("list", expPointsLogDao.getExppointsLogListByPage(where, params, pageEntity.getPageNo(), pageEntity.getPageSize()));
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
}