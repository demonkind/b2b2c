package net.shopnc.b2b2c.wap.action.login;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.service.ArticleService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

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
    @RequestMapping("popupRegister")
    public String index(ModelMap modelMap) {
       /* if (SessionEntity.getIsLogin() == true) {
            return "redirect:/member";
        }*/
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
    
    /**
     * 手机注册页面
     * @param modelMap
     * @return
     */
    @RequestMapping("mobile")
    public String mobile(ModelMap modelMap) {
        //会员注册协议
        List<Article> articleList = articleService.getArticleListByPositionList(ArticlePositions.MEMBER_REGISTER);
        if (articleList.size() > 0) {
            Article article = articleList.get(0);
            modelMap.put("articleInfo", article);
        } else {
            modelMap.put("articleInfo", null);
        }
        return getLoginTemplate("register_mobile");
    }
    
    /**
     * 手机注册-输入验证码和动态码
     * @param modelMap
     * @return
     */
    @RequestMapping("mobileNest")
    public String mobileNest(@RequestParam(value="mobile")String mobile,ModelMap modelMap) {
        modelMap.put("mobile", mobile);
        return getLoginTemplate("register_mobile_code");
    }
    
    /**
     * 手机注册-设置密码
     * @param modelMap
     * @return
     */
    @RequestMapping("modsetPwd")
    public String setPwd(@RequestParam(value="mobile")String mobile,ModelMap modelMap) {
        modelMap.put("mobile", mobile);
        return getLoginTemplate("register_mobile_password");
    }
}