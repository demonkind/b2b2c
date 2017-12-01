package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.MemberGradeDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.MemberGrade;
import net.shopnc.b2b2c.service.member.ExperienceService;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by zxy on 2015-12-16.
 */
@Controller
public class HomeExppointsAction extends HomeBaseAction {

    @Autowired
    ExperienceService experienceService;
    @Autowired
    MemberDao memberDao;
    @Autowired
    MemberGradeDao memberGradeDao;

    /**
     * 经验成长进度页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "exppoints/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        HashMap<String,Object> MInfoMap = getMInfo();
        modelMap.put("member",MInfoMap.get("member"));
        modelMap.put("memberCurrentGrade", MInfoMap.get("memberCurrentGrade"));
        //会员等级列表
        List<MemberGrade> memberGradeList = memberGradeDao.getGradeList();
        modelMap.put("memberGradeList", memberGradeList);
        //面包屑
        List<CrumbsVo> crumbsVoList = new ArrayList<>();
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName("我的成长进度");
        crumbsVoList.add(crumbsVo);
        setCrumbsList(crumbsVoList);
        modelMap.put("crumbsList", super.getCrumbsList());
        return getHomeTemplate("exppoints/index");
    }
    /**
     * 经验日志
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "exppoints/log", method = RequestMethod.GET)
    public String log(@RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                      ModelMap modelMap) {
        HashMap<String,Object> MInfoMap = getMInfo();
        modelMap.put("member",MInfoMap.get("member"));
        modelMap.put("memberCurrentGrade",MInfoMap.get("memberCurrentGrade"));
        //获得经验值日志
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        HashMap<String,Object> result = experienceService.getExppointsLogListByPage(params, page);
        modelMap.put("logList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //面包屑
        List<CrumbsVo> crumbsVoList = new ArrayList<>();
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName("经验值明细");
        crumbsVoList.add(crumbsVo);
        setCrumbsList(crumbsVoList);
        modelMap.put("crumbsList", super.getCrumbsList());
        return getHomeTemplate("exppoints/log");
    }
    /**
     * 获得公用会员信息
     * @return
     */
    private HashMap<String,Object> getMInfo(){
        HashMap<String,Object> result = new HashMap<String, Object>();
        //查询会员信息
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        result.put("member", member);
        //会员经验等级信息
        HashMap<String,Object> gradeMap = experienceService.getMemberGradeAndProgress(member.getExperiencePoints());
        result.put("memberCurrentGrade",gradeMap);
        return result;
    }
}