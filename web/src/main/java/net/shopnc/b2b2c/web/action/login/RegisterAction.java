package net.shopnc.b2b2c.web.action.login;

import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.service.ArticleService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by zxy on 2015-11-3.
 */
@Controller
@RequestMapping("register")
public class RegisterAction extends LoginBaseAction {

    @Autowired
    private ArticleService articleService;

    /**
     * 会员注册
     * @param modelMap
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        if (SessionEntity.getIsLogin() == true) {
            return "redirect:/member";
        }
        //会员注册协议
        List<Article> articleList = articleService.getArticleListByPositionList(ArticlePositions.MEMBER_REGISTER);
        if (articleList.size() > 0) {
            Article article = articleList.get(0);
            modelMap.put("articleInfo", article);
        } else {
            modelMap.put("articleInfo", null);
        }
        return getLoginTemplate("register");
    }
}