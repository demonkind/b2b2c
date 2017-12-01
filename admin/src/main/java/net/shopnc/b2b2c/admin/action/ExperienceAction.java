package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.member.MemberGradeDao;
import net.shopnc.b2b2c.domain.member.MemberGrade;
import net.shopnc.b2b2c.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2015-11-25.
 */
@Controller
public class ExperienceAction extends BaseAction {

    @Autowired
    private SiteService siteService;

    @Autowired
    private MemberGradeDao memberGradeDao;

    /**
     * 经验值规则设置
     * @param model
     * @return
     */
    @RequestMapping(value = "experience/setting", method = RequestMethod.GET)
    public String setting(ModelMap model) {
        HashMap siteInfo = siteService.getSiteInfo();
        model.put("siteInfo", siteInfo);
        return getAdminTemplate("member/experience/setting");
    }
    /**
     * 会员等级
     * @param model
     * @return
     */
    @RequestMapping(value = "experience/membergrade", method = RequestMethod.GET)
    public String memberGrade(ModelMap model) {
        List<MemberGrade> gradeList = memberGradeDao.getGradeList();
        model.put("gradeList", gradeList);
        return getAdminTemplate("member/experience/membergrade");
    }
    /**
     * 经验明细
     * @return
     */
    @RequestMapping(value = "experience/explog", method = RequestMethod.GET)
    public String explog() {
        return getAdminTemplate("member/experience/explog");
    }
}
