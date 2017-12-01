package net.shopnc.b2b2c.web.action.buy;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.NavigationPosition;
import net.shopnc.b2b2c.dao.NavigationDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.Navigation;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.member.ExperienceService;
import net.shopnc.b2b2c.vo.member.MemberVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购买基类
 * Created by hbj on 2015/11/30.
 */
@Controller
public class BuyBaseAction {
    @Autowired
    private SiteService siteService;
    @Autowired
    private NavigationDao navigationDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ExperienceService experienceService;

    protected final Logger logger = Logger.getLogger(getClass());

    protected static final String BUY_TEMPLATE_ROOT = "buy/";

    protected String getBuyTemplate(String template) {
        return BUY_TEMPLATE_ROOT + template;
    }

    protected String getMemberBuyRoot() {
        return ShopConfig.getMemberRoot() + "buy/";
    }
    /**
     * freemarker 中获取全局配置
     * @return
     */
    @ModelAttribute("config")
    public Map<String, String> getConfig() {
        return siteService.getSiteInfo();
    }
    /**
     * 导航
     * @return
     */
    @ModelAttribute("navList")
    public HashMap<String,List<Navigation>> getNavigationList() {
        HashMap<String,List<Navigation>> navList = new HashMap<String, List<Navigation>>();
        List<Navigation> navigationList = navigationDao.getNavigationListByPositionId(NavigationPosition.TOP);
        navList.put(NavigationPosition.TOP_NAME,navigationList);
        navigationList = navigationDao.getNavigationListByPositionId(NavigationPosition.BODY);
        navList.put(NavigationPosition.BODY_NAME,navigationList);
        navigationList = navigationDao.getNavigationListByPositionId(NavigationPosition.FOOTER);
        navList.put(NavigationPosition.FOOTER_NAME,navigationList);
        return navList;
    }
    /**
     * freemarker 中获取全局配置
     * @return
     */
    @ModelAttribute("memberCommon")
    public MemberVo getMemberInfo() {
        MemberVo memberVo = null;
        if (SessionEntity.getIsLogin() == false) {
            return memberVo;
        }
        //会员基本信息
        Member memberInfo = memberDao.get(Member.class, SessionEntity.getMemberId());
        if (memberInfo!=null) {
            memberVo = new MemberVo(memberInfo);
        }
        //会员等级
        if (memberInfo!=null) {
            memberVo.setCurrGrade(experienceService.getMemberGrade(memberInfo.getExperiencePoints()));
        }
        return memberVo;
    }
}
