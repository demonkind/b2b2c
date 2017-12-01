package net.shopnc.b2b2c.service.member;


import net.shopnc.b2b2c.constant.PointsLogStage;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.PointsLogDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.PointsLog;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.vo.PointsLogMemberVo;
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
 * Created by zxy on 2015-12-01.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class PointsService extends BaseService {

    @Autowired
    private PointsLogDao pointsLogDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SiteService siteService;

    /**
     * 获取积分明细列表表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getPointsLogDtGridList(String dtGridPager) throws Exception {
        DtGrid dtGrid = pointsLogDao.getDtGridList(dtGridPager, PointsLog.class);

        List<Object> logs = dtGrid.getExhibitDatas();
        List<Object> pointsLogMemberList = new ArrayList<Object>();
        for (int j = 0; j < logs.size(); j++) {
            PointsLog logInfo = (PointsLog) logs.get(j);
            PointsLogMemberVo pointsLogMemberInfo = new PointsLogMemberVo();
            pointsLogMemberInfo.setLogId(logInfo.getLogId());
            pointsLogMemberInfo.setMemberId(logInfo.getMemberId());
            pointsLogMemberInfo.setPoints(logInfo.getPoints());
            pointsLogMemberInfo.setAddTime(logInfo.getAddTime());
            pointsLogMemberInfo.setDescription(logInfo.getDescription());
            pointsLogMemberInfo.setOperationStage(logInfo.getOperationStage());
            pointsLogMemberInfo.setAdminId(logInfo.getAdminId());
            pointsLogMemberInfo.setAdminName(logInfo.getAdminName());
            //会员信息
            Member memberInfo = memberDao.get(Member.class, logInfo.getMemberId());
            if (memberInfo == null) {
                pointsLogMemberInfo.setMemberName("");
            }else{
                pointsLogMemberInfo.setMemberName(memberInfo.getMemberName());
            }
            pointsLogMemberList.add(pointsLogMemberInfo);
        }
        dtGrid.setExhibitDatas(pointsLogMemberList);
        return dtGrid;
    }
    /**
     * 积分列表分页
     * @param params
     * @param page
     * @return
     */
    public HashMap<String,Object> getPointsLogListByPage(HashMap<String,Object> params, int page) {
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("addTimeGt")) {
                where.put("addTimeGt", "addTime >= :addTimeGt");
            }
            if (key.equals("addTimeLt")) {
                where.put("addTimeLt", "addTime <= :addTimeLt");
            }
            if (key.equals("operationStage")) {
                where.put("operationStage", "operationStage = :operationStage");
            }
            if (key.equals("descriptionLike")) {
                where.put("description", "description like :descriptionLike");
            }
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(pointsLogDao.findPointsLogCount(where, params));
        pageEntity.setPageNo(page);
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("list", pointsLogDao.getPointsLogListByPage(where, params, pageEntity.getPageNo(), pageEntity.getPageSize()));
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    /**
     * 管理员手动增加积分
     * @param memberId
     * @param operateType
     * @param points
     * @param description
     * @param adminId
     * @param adminName
     * @return
     * @throws ParameterErrorException
     */
    public boolean addPointsAdmin(int memberId,int operateType,int points,String description,int adminId,String adminName) throws ParameterErrorException {
        if (points <= 0) {
            throw new ParameterErrorException("积分应该大于1");
        }
        PointsLog pointsLog = new PointsLog();
        if (operateType == 2) {//减少
            pointsLog.setPoints(-points);
        } else {
            pointsLog.setPoints(points);
        }
        pointsLog.setDescription(description);
        pointsLog.setMemberId(memberId);
        pointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        pointsLog.setOperationStage(PointsLogStage.ADMIN);
        pointsLog.setAdminId(adminId);
        pointsLog.setAdminName(adminName);
        try {
            return this.addPoints(pointsLog);
        } catch (ParameterErrorException e) {
            throw new ParameterErrorException(e.getMessage());
        }
    }
    /**
     * 注册增加积分
     * @param memberId
     * @return
     */
    public boolean addPointsRegister(int memberId){
        PointsLog pointsLog = new PointsLog();
        int pointsNum = 0;
        try{
            pointsNum = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.POINTSRULEREGISTER));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (pointsNum <= 0) {
            return false;
        }
        pointsLog.setPoints(pointsNum);
        pointsLog.setDescription("注册会员");
        pointsLog.setMemberId(memberId);
        pointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        pointsLog.setOperationStage(PointsLogStage.REGISTER);
        try{
            return this.addPoints(pointsLog);
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            return false;
        }
    }
    /**
     * 登录增加积分
     * @param memberId
     * @return
     */
    public boolean addPointsLogin(int memberId){
        //查询当天是否已经赠送积分
        HashMap<String,String> where = new HashMap<>();
        where.put("memberId", "memberId = :memberId");
        where.put("operationStage", "operationStage = :operationStage");
        where.put("addTimeGt", "addTime >= :addTimeGt");
        where.put("addTimeLt", "addTime < :addTimeLt");

        HashMap<String,Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("operationStage", PointsLogStage.LOGIN);
        params.put("addTimeGt", ShopHelper.getTimestampOfDayStart(ShopHelper.getCurrentTimestamp()));
        params.put("addTimeLt", ShopHelper.getTimestampOfDayEnd(ShopHelper.getCurrentTimestamp()));
        long count = pointsLogDao.findPointsLogCount(where, params);
        if (count > 0) {
            return true;
        }
        PointsLog pointsLog = new PointsLog();
        int pointsNum = 0;
        try{
            pointsNum = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.POINTSRULELOGIN));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (pointsNum <= 0) {
            return false;
        }
        pointsLog.setPoints(pointsNum);
        pointsLog.setDescription("会员登录");
        pointsLog.setMemberId(memberId);
        pointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        pointsLog.setOperationStage(PointsLogStage.LOGIN);
        try{
            return this.addPoints(pointsLog);
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            return false;
        }
    }
    /**
     * 订单完成增加积分
     * @param memberId
     * @param ordersAmount
     * @param ordersSn
     * @return
     */
    public boolean addPointsOrders(int memberId, BigDecimal ordersAmount, long ordersSn){
        PointsLog pointsLog = new PointsLog();
        int orderrate = 0;
        int ordermax = 0;
        try{
            orderrate = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.POINTSRULEORDERRATE));
            ordermax = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.POINTSRULEORDERMAX));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (orderrate <= 0 || PriceHelper.isLessThanOrEquals(ordersAmount, BigDecimal.ZERO)) {
            return false;
        }
        //计算积分数
        int pointsNum = 0;
        try {
            pointsNum = (PriceHelper.mul(PriceHelper.div(ordersAmount, 100), orderrate)).intValue();
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (pointsNum <= 0) {
            return false;
        }
        //大于最大值则按照最大值进行积分
        if (ordermax>0 && pointsNum>ordermax) {
            pointsNum = ordermax;
        }
        pointsLog.setPoints(pointsNum);
        pointsLog.setDescription("订单" + String.valueOf(ordersSn) + "购物消费");
        pointsLog.setMemberId(memberId);
        pointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        pointsLog.setOperationStage(PointsLogStage.ORDERS);
        try{
            return this.addPoints(pointsLog);
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            return false;
        }
    }
    /**
     * 评价增加积分
     * @param memberId
     * @return
     */
    public boolean addPointsComments(int memberId){
        PointsLog pointsLog = new PointsLog();
        int pointsNum = 0;
        try{
            pointsNum = Integer.parseInt((String)siteService.getSiteInfo().get(SiteTitle.POINTSRULECOMMENTS));
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if (pointsNum <= 0) {
            return false;
        }
        pointsLog.setPoints(pointsNum);
        pointsLog.setDescription("评论商品");
        pointsLog.setMemberId(memberId);
        pointsLog.setAddTime(ShopHelper.getCurrentTimestamp());
        pointsLog.setOperationStage(PointsLogStage.COMMENTS);
        try{
            return this.addPoints(pointsLog);
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            return false;
        }
    }
    /**
     * 增加积分
     * @param pointsLog
     * @return
     * @throws ParameterErrorException
     */
    public boolean addPoints(PointsLog pointsLog) throws ParameterErrorException {
        Member member = memberDao.get(Member.class, pointsLog.getMemberId());
        if (member == null) {
            throw new ParameterErrorException("会员信息不存在");
        }
        //增加积分日志
        int logId = (Integer)pointsLogDao.save(pointsLog);
        if (logId > 0) {
            //更新会员积分
            HashMap<String,Object> updateMap = new HashMap<String, Object>();
            updateMap.put("memberPoints", member.getMemberPoints() + pointsLog.getPoints());
            memberDao.updateByMemberId(updateMap, pointsLog.getMemberId());
        }else{
            return false;
        }
        return true;
    }
}