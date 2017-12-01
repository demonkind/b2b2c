package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.dao.member.MemberGradeDao;
import net.shopnc.b2b2c.domain.member.MemberGrade;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.member.ExperienceService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-03-14.
 */
@Controller
public class ExperienceJsonAction extends BaseJsonAction {

    @Autowired
    private SiteService siteService;

    @Autowired
    private MemberGradeDao memberGradeDao;

    @Autowired
    private ExperienceService experienceService;

    /**
     * 保存经验值规则
     * @param expRuleComments
     * @param expRuleLogin
     * @param expRuleOrdermax
     * @param expRuleOrderrate
     * @param expRuleRegister
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "experience/setting", method = RequestMethod.POST)
    public ResultEntity setting(int expRuleComments, int expRuleLogin, int expRuleOrdermax, int expRuleOrderrate,int expRuleRegister) {
        HashMap<String,String> siteList = new HashMap<String, String>();
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "experience/setting");
        try {
            siteList.put(SiteTitle.EXPRULECOMMENTS, ((Integer)expRuleComments).toString());
            siteList.put(SiteTitle.EXPRULELOGIN, ((Integer)expRuleLogin).toString());
            siteList.put(SiteTitle.EXPRULEORDERMAX, ((Integer)expRuleOrdermax).toString());
            siteList.put(SiteTitle.EXPRULEORDERRATE, ((Integer)expRuleOrderrate).toString());
            siteList.put(SiteTitle.EXPRULEREGISTER, ((Integer)expRuleRegister).toString());
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("数据库保存失败");
        }
        return resultEntity;
    }
    /**
     * 保存会员等级
     * @param gradeId
     * @param gradeLevel
     * @param gradeName
     * @param expPoints
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "experience/membergrade", method = RequestMethod.POST)
    public ResultEntity memberGrade(@RequestParam(value = "gradeId[]", required = false) int gradeId[],
                              @RequestParam(value = "gradeLevel[]", required = false) int gradeLevel[],
                              @RequestParam(value = "gradeName[]", required = false) String gradeName[],
                              @RequestParam(value = "expPoints[]", required = false) int expPoints[]) {
        ResultEntity resultEntity = new ResultEntity();
        List<MemberGrade> gradeList = new ArrayList<MemberGrade>();
        for (int i = 0; i < gradeId.length; i++) {
            MemberGrade updateGrade = new MemberGrade();
            updateGrade.setGradeId(gradeId[i]);
            updateGrade.setGradeLevel(i);
            updateGrade.setGradeName(gradeName[i]);
            if (i == 0) {
                updateGrade.setExpPoints(0);
            }else{
                updateGrade.setExpPoints(expPoints[i]);
            }
            gradeList.add(updateGrade);
        }
        try {
            if (gradeList.size() > 0) {
                memberGradeDao.updateAll(gradeList);
            }
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("会员等级设置保存失败");
        }
        return resultEntity;
    }
    /**
     * 经验明细JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "experience/explog.json", method = RequestMethod.POST)
    public DtGrid explogJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = experienceService.getExpPointsLogDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
}
