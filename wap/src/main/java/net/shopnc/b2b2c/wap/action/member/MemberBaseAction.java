package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.*;
import net.shopnc.b2b2c.dao.ArticleCategoryDao;
import net.shopnc.b2b2c.dao.ArticleDao;
import net.shopnc.b2b2c.dao.NavigationDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.domain.ArticleCategory;
import net.shopnc.b2b2c.domain.Navigation;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.goods.CategoryService;
import net.shopnc.b2b2c.service.member.ExperienceService;
import net.shopnc.b2b2c.vo.contract.ContractItemBean;
import net.shopnc.b2b2c.vo.member.MemberVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.util.MyWebBindingInitializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

/**
 * Created by zxy on 2015-11-25.
 */
@Controller
@RequestMapping("/member")
public class MemberBaseAction {
    @Autowired
    private SiteService siteService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private NavigationDao navigationDao;
    @Autowired
    private ArticleCategoryDao articleCategoryDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    protected MemberDao memberDao;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private OrdersDao ordersDao;

    protected final Logger logger = Logger.getLogger(getClass());

    protected static final String MEMBER_TEMPLATE_ROOT = "tmpl/member/";

    @Autowired
    protected MyWebBindingInitializer myWebBindingInitializer;

    /**
     * 过滤表单
     * @param binder
     * @param request
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        myWebBindingInitializer.initBinder(binder, request);
    }

    /**
     * 获得模板路径
     * @param template
     * @return
     */
    protected String getMemberTemplate(String template) {
        return MEMBER_TEMPLATE_ROOT + template;
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
     * 获得默认搜索关键字
     * @return
     */
    @ModelAttribute("defaultSearch")
    public List<String> getDefaultSearch() {
        return Arrays.asList(((String) siteService.getSiteInfo().get(SiteTitle.DEFAULT_SEARCH)).split(","));
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
     * 底部保障服务
     */
    @ModelAttribute("mainContract")
    public List<ContractItemBean> getMainContract() {
        return contractService.getMainContract();
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
     * 左侧菜单
     * @return
     */
    @ModelAttribute("menuList")
    public LinkedHashMap<String, Object> getMenuList() {
        LinkedHashMap<String, Object> menuList = new LinkedHashMap<String, Object>();
        //交易中心
        LinkedHashMap<String, Object> menuItemListTrade = new LinkedHashMap<String, Object>();
        menuItemListTrade.put("orders", new HashMap<String,String>(){{put("name", "商品订单");put("url", ShopConfig.getMemberRoot()+"orders/list");}});
        menuItemListTrade.put("evaluate", new HashMap<String,String>(){{put("name", "交易评价");put("url", ShopConfig.getMemberRoot()+"evaluate");}});
        menuList.put("trade", new HashMap<String, Object>(){{put("name", "交易中心");put("child",menuItemListTrade);}});
        //关注中心
        LinkedHashMap<String, Object> menuItemListFollow = new LinkedHashMap<String, Object>();
        menuItemListFollow.put("favoritesGoods", new HashMap<String,String>(){{put("name", "商品关注");put("url", ShopConfig.getMemberRoot()+"favorites/goods");}});
        menuItemListFollow.put("favoritesStore", new HashMap<String,String>(){{put("name", "店铺关注");put("url", ShopConfig.getMemberRoot()+"favorites/store");}});
        menuItemListFollow.put("goodsbrowse", new HashMap<String,String>(){{put("name", "我的足迹");put("url", ShopConfig.getMemberRoot()+"goodsbrowse");}});
        menuList.put("follow", new HashMap<String, Object>(){{put("name", "关注中心");put("child",menuItemListFollow);}});
        //客户服务
        LinkedHashMap<String, Object> menuItemListClient = new LinkedHashMap<String, Object>();
        menuItemListClient.put("refund", new HashMap<String,String>(){{put("name", "退款及退货");put("url", ShopConfig.getMemberRoot()+"refund/list");}});
        menuItemListClient.put("consult", new HashMap<String,String>(){{put("name", "商品咨询");put("url", ShopConfig.getMemberRoot()+"consult/list");}});
        menuList.put("client", new HashMap<String, Object>(){{put("name", "客户服务");put("child",menuItemListClient);}});
        //会员资料
        LinkedHashMap<String, Object> menuItemListInformation = new LinkedHashMap<String, Object>();
        menuItemListInformation.put("information", new HashMap<String,String>(){{put("name", "账户信息");put("url", ShopConfig.getMemberRoot()+"information");}});
        menuItemListInformation.put("security", new HashMap<String,String>(){{put("name", "账户安全");put("url", ShopConfig.getMemberRoot()+"security");}});
        menuItemListInformation.put("address", new HashMap<String,String>(){{put("name", "收货地址");put("url", ShopConfig.getMemberRoot()+"address");}});
        menuItemListInformation.put("message", new HashMap<String,String>(){{put("name", "我的消息");put("url", ShopConfig.getMemberRoot()+"message/list");}});
        menuList.put("information", new HashMap<String, Object>(){{put("name", "会员资料");put("child",menuItemListInformation);}});
        //财产中心
        LinkedHashMap<String, Object> menuItemListProperty = new LinkedHashMap<String, Object>();
        menuItemListProperty.put("predeposit", new HashMap<String,String>(){{put("name", "账户余额");put("url", ShopConfig.getMemberRoot()+"predeposit/log");}});
        menuItemListProperty.put("pointslog", new HashMap<String,String>(){{put("name", "我的积分");put("url", ShopConfig.getMemberRoot()+"pointslog");}});
        menuItemListProperty.put("exppoints", new HashMap<String,String>(){{put("name", "我的经验");put("url", ShopConfig.getWebRoot()+"exppoints/index");}});
        menuList.put("property", new HashMap<String, Object>(){{put("name", "财产中心");put("child",menuItemListProperty);}});
        return menuList;
    }
    /**
     * 会员基本信息
     * @return
     */
    @ModelAttribute("memberCommon")
    public MemberVo getMemberInfo() {
        MemberVo memberVo = null;
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
     * 获得会员中心头部统计数
     * @return
     */
    @ModelAttribute("count")
    public HashMap<String,Object> count() {
        HashMap<String,Object> hashMap = new HashMap<>();
        List<Object> condition = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>();
        //待付款数量
        condition.add("memberId = :memberId");
        condition.add("ordersState = :ordersState");
        params.put("memberId", SessionEntity.getMemberId());
        params.put("ordersState", OrdersOrdersState.NEW);
        long ordersStateNewCount = ordersDao.getOrdersCount(condition,params);

        //待收货数量
        condition.add("memberId = :memberId");
        condition.add("ordersState = :ordersState");
        params.put("memberId", SessionEntity.getMemberId());
        params.put("ordersState", OrdersOrdersState.SEND);
        long ordersStateSendCount = ordersDao.getOrdersCount(condition,params);

        //待评价数量
        condition.add("memberId = :memberId");
        condition.add("ordersState = :ordersState");
        condition.add("evaluationState = :evaluationState");
        params.put("memberId", SessionEntity.getMemberId());
        params.put("ordersState", OrdersOrdersState.FINISH);
        params.put("evaluationState", OrdersEvaluationState.NO);
        long ordersStateEvaluationCount = ordersDao.getOrdersCount(condition,params);

        HashMap<String,Object> hashMap1 = new HashMap<>();
        hashMap1.put("ordersStateNewCount",ordersStateNewCount);
        hashMap1.put("ordersStateSendCount",ordersStateSendCount);
        hashMap1.put("ordersStateEvaluationCount",ordersStateEvaluationCount);
        return hashMap1;
    }

}