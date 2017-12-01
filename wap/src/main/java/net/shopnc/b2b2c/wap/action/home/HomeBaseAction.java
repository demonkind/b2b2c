package net.shopnc.b2b2c.wap.action.home;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.constant.NavigationPosition;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.dao.ArticleCategoryDao;
import net.shopnc.b2b2c.dao.ArticleDao;
import net.shopnc.b2b2c.dao.NavigationDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.domain.ArticleCategory;
import net.shopnc.b2b2c.domain.Navigation;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.goods.CategoryService;
import net.shopnc.b2b2c.service.member.ExperienceService;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.b2b2c.vo.contract.ContractItemBean;
import net.shopnc.b2b2c.vo.member.MemberVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/")
public class HomeBaseAction {
    @Autowired
    private SiteService siteService;
    @Autowired
    private ArticleCategoryDao articleCategoryDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private NavigationDao navigationDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ExperienceService experienceService;

    protected final Logger logger = Logger.getLogger(getClass());

    protected static final String HOME_TEMPLATE_ROOT = "";

    private List<CrumbsVo> crumbsList = new ArrayList<CrumbsVo>();

    protected String getHomeTemplate(String template) {
        return HOME_TEMPLATE_ROOT + template;
    }

    /**
     * freemarker 中获取全局配置
     * @return
     */
    @ModelAttribute("config")
    public Map<String, String> getConfig() {
        return siteService.getSiteInfo();
    }

    @ModelAttribute("defaultSearch")
    public List<String> getDefaultSearch() {
        return Arrays.asList(((String) siteService.getSiteInfo().get(SiteTitle.DEFAULT_SEARCH)).split(","));
//        return new List<String>();
    }

    @Autowired
    MemberDao memberDao;
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
    /**
     * 底部文章列表
     * @return
     */
    @ModelAttribute("bottomArticleList")
    public HashMap<Integer,List<Article>> getFooterArticleList() {
        HashMap<Integer,List<Article>> bottomArticleList = new HashMap<Integer, List<Article>>();
        List<ArticleCategory> articleCategoryList = articleCategoryDao.getArticleCategoryListByPositionId(ArticlePositions.DEFAULT_LIST);
        for (int i=0; i<articleCategoryList.size(); i++) {
            List<Article> articleList = articleDao.getArticleListByCategoryId(articleCategoryList.get(i).getCategoryId(),1,6);
            if (articleList.size() > 0) {
                articleList.get(0).setCategoryTitle(articleCategoryList.get(i).getTitle());
                bottomArticleList.put(i,articleList);
            }
        }
        return bottomArticleList;
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
     * 商品分类
     * @return
     */
    @ModelAttribute("categoryList")
    public List<Object> getCategoryList() {
        return categoryService.getCategoryNav();
    }

    /**
     * 面包屑
     * @return
     */
    @ModelAttribute("crumbsList")
    public List<CrumbsVo> getCrumbsList() {
        return this.crumbsList;
    }

    protected void setCrumbsList(List<CrumbsVo> crumbsList) {
        List<CrumbsVo> crumbsVoList = new ArrayList<CrumbsVo>();
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName("首页");
        crumbsVo.setUrl(ShopConfig.getWebRoot());
        crumbsVoList.add(crumbsVo);
        crumbsVoList.addAll(crumbsList);
        this.crumbsList = crumbsVoList;
    }
    
    /**
     * 底部保障服务
     */
    @ModelAttribute("mainContract")
    public List<ContractItemBean> getMainContract() {
    	return contractService.getMainContract();
    }
}