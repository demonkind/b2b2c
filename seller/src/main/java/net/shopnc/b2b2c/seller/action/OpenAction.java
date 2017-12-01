package net.shopnc.b2b2c.seller.action;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.ArticlePositions;
import net.shopnc.b2b2c.constant.StoreJoininState;
import net.shopnc.b2b2c.dao.goods.StoreBindCategoryDao;
import net.shopnc.b2b2c.dao.store.StoreCertificateDao;
import net.shopnc.b2b2c.dao.store.StoreClassDao;
import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.dao.store.StoreJoininDao;
import net.shopnc.b2b2c.domain.Article;
import net.shopnc.b2b2c.domain.goods.StoreBindCategory;
import net.shopnc.b2b2c.domain.store.StoreCertificate;
import net.shopnc.b2b2c.domain.store.StoreClass;
import net.shopnc.b2b2c.domain.store.StoreGrade;
import net.shopnc.b2b2c.domain.store.StoreJoinin;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.ArticleService;
import net.shopnc.b2b2c.service.store.StoreJoininService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商家开店
 */
@Controller
public class OpenAction extends BaseAction {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private StoreJoininDao storeJoininDao;

    @Autowired
    private StoreBindCategoryDao storeBindCategoryDao;

    @Autowired
    private StoreGradeDao storeGradeDao;

    @Autowired
    private StoreClassDao storeClassDao;

    @Autowired
    private StoreCertificateDao storeCertificateDao;

    @Autowired
    private StoreJoininService storeJoininService;


    /**
     * 商家开店
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "open", method = RequestMethod.GET)
    public String open(ModelMap modelMap) {
        String page = "open/open";
        //读取开店信息
        StoreJoinin storeJoinin = storeJoininDao.get(StoreJoinin.class, SellerSessionHelper.getSellerId());

        if (storeJoinin == null || storeJoinin.getState() == StoreJoininState.VERIFY_FAIL) {
            //未申请或者初审未通过走商家入驻流程
            page = "open/open";
        } else if (storeJoinin.getState() == StoreJoininState.VERIFY_SUCCESS
                || storeJoinin.getState() == StoreJoininState.PAY_VERIFY_FAIL) {
            //初审通过或者付款审批未通过走商家付款流程
            page = "open/open2";
        } else {
            return "redirect:open/info";
        }

        List<Article> articleList = articleService.getArticleListByPositionList(ArticlePositions.SELLER_JOIN);
        modelMap.put("joinAgreement", articleList.get(0));

        List<StoreGrade> storeGradeList = storeGradeDao.findAll(StoreGrade.class);
        modelMap.put("storeGradeList", storeGradeList);

        List<StoreClass> storeClassList = storeClassDao.findAll(StoreClass.class);
        modelMap.put("storeClassList", storeClassList);

        modelMap.put("storeJoinin", storeJoinin);

        //读取店铺资质信息
        StoreCertificate storeCertificate = storeCertificateDao.get(StoreCertificate.class, SellerSessionHelper.getSellerId());
        modelMap.put("storeCertificate", storeCertificate);

        //读取绑定的分类列表
        List<StoreBindCategory> storeBindCategoryList = storeBindCategoryDao.getListByStoreId(SellerSessionHelper.getSellerId());
        modelMap.put("storeBindCategoryList", storeBindCategoryList);
        modelMap.put("storeBindCategoryJson", JsonHelper.toJson(storeBindCategoryList));

        //by hbj 左侧文章
        List<Article> articleLeftList = articleService.getArticleListByPositionList(ArticlePositions.SELLER_AddIn);
        modelMap.put("articleLeftList", articleLeftList);

        return getSellerTemplate(page);
    }

    /**
     * 商家开店信息页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "open/info", method = RequestMethod.GET)
    public String info(ModelMap modelMap) {
        int openState;
        String message;
        Boolean isShowBtn;
        Boolean isShowLogoutBtn = false;
        String btnTitle = "";
        //读取开店信息
        StoreJoinin storeJoinin = storeJoininDao.get(StoreJoinin.class, SellerSessionHelper.getSellerId());
        if(storeJoinin != null) {
            switch (storeJoinin.getState()) {
                case StoreJoininState.VERIFY_FAIL:
                case StoreJoininState.PAY_VERIFY_FAIL:
                    message = "您的入驻申请审核失败，失败原因：" + storeJoinin.getJoininMessage();
                    btnTitle = "商家申请开店入驻";
                    isShowBtn = true;
                    break;
                case StoreJoininState.SUBMIT:
                case StoreJoininState.PAYED:
                    message = "您的入驻申请已经提交请等待审核";
                    isShowBtn = false;
                    break;
                case StoreJoininState.VERIFY_SUCCESS:
                    message = "您的入驻申请初审已经通过，请尽快完成缴费";
                    btnTitle = "查看审核意见，提交付款凭证";
                    isShowBtn = true;
                    break;
                default:
                    message = "店铺已经开通，退出再次登录后进入店铺管理";
                    isShowBtn = false;
                    isShowLogoutBtn = true;
            }
            openState = storeJoinin.getState();
        } else {
            openState = 0;
            message = "您还没有开通店铺";
            btnTitle = "商家申请开店入驻";
            isShowBtn = true;
        }
        modelMap.addAttribute("openState", openState);
        modelMap.addAttribute("joinMessage", message);
        modelMap.addAttribute("isShowBtn", isShowBtn);
        modelMap.addAttribute("isShowLogoutBtn", isShowLogoutBtn);
        modelMap.addAttribute("btnTitle", btnTitle);

        //by hbj 左侧文章
        List<Article> articleLeftList = articleService.getArticleListByPositionList(ArticlePositions.SELLER_AddIn);
        modelMap.put("articleLeftList", articleLeftList);

        return getSellerTemplate("open/info");
    }

}