package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.service.ArticleService;
import net.shopnc.b2b2c.service.SiteService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class LoginAction {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SiteService siteService;

    @RequestMapping("login")
    public String login(ModelMap modelMap) {
        //如果已经登录跳转到首页
        if(SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/";
        }

        //商家入驻标题及内容
        List<Article> articleList = articleService.getArticleListByPositionList(ArticlePositions.SELLER_REGISTER);
        modelMap.put("articleList",articleList);
        modelMap.put("sellerLogoUrl", siteService.getSiteInfo().get(SiteTitle.SELLER_LOGO_URL));

        return "login";
    }

    @RequestMapping("logout")
    public String logout() {
        return "redirect:login";
    }

}