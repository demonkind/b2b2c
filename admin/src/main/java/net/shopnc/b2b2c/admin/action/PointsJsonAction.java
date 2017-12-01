package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.admin.util.AdminSessionHelper;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.member.PointsService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 积分管理
 * Created by zxy on 2016-03-14.
 */
@Controller
public class PointsJsonAction extends BaseJsonAction {

    @Autowired
    private SiteService siteService;

    @Autowired
    private PointsService pointsService;

    @Autowired
    private MemberDao memberDao;

    /**
     * 保存积分规则
     * @param pointsRuleComments
     * @param pointsRuleLogin
     * @param pointsRuleOrdermax
     * @param pointsRuleOrderrate
     * @param pointsRuleRegister
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "points/setting", method = RequestMethod.POST)
    public ResultEntity setting(int pointsRuleComments, int pointsRuleLogin, int pointsRuleOrdermax, int pointsRuleOrderrate,int pointsRuleRegister) {
        HashMap<String,String> siteList = new HashMap<String, String>();
        ResultEntity resultEntity = new ResultEntity();
        try {
            siteList.put(SiteTitle.POINTSRULECOMMENTS, ((Integer)pointsRuleComments).toString());
            siteList.put(SiteTitle.POINTSRULELOGIN, ((Integer)pointsRuleLogin).toString());
            siteList.put(SiteTitle.POINTSRULEORDERMAX, ((Integer)pointsRuleOrdermax).toString());
            siteList.put(SiteTitle.POINTSRULEORDERRATE, ((Integer)pointsRuleOrderrate).toString());
            siteList.put(SiteTitle.POINTSRULEREGISTER, ((Integer)pointsRuleRegister).toString());
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
    /**
     * 积分明细列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "points/pointslog.json", method = RequestMethod.POST)
    public DtGrid pointslogJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = pointsService.getPointsLogDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
    /**
     * 保持积分增减
     * @param memberId
     * @param operateType
     * @param points
     * @param description
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "points/addpoints", method = RequestMethod.POST)
    public ResultEntity addpoints(int memberId,int operateType,int points,String description) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            pointsService.addPointsAdmin(memberId, operateType, points, description, AdminSessionHelper.getAdminId(), AdminSessionHelper.getAdminName());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        }catch (ParameterErrorException e){
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
    /**
     * 查询会员信息
     * @param memberName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "points/memberinfo", method = RequestMethod.GET)
    public ResultEntity getMemberInfo(String memberName) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            Member member = memberDao.getMemberInfoByMemberName(memberName);
            if (member != null) {
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setData(member);
                resultEntity.setMessage("操作成功");
            }else{
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("会员信息不存在");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("会员信息查询失败");
        }
        return resultEntity;
    }
}
