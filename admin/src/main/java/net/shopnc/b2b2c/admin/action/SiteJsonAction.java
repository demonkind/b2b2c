package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.dao.SiteDao;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class SiteJsonAction extends BaseJsonAction {
    @Autowired
    private SiteService siteService;

    /**
     * 保存基本设置
     * @param siteName
     * @param icp
     * @param flowCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "site/update_base", method = RequestMethod.POST)
    public ResultEntity save(String siteName,
                             String icp,
                             String flowCode) {
        HashMap<String,String> siteList = new HashMap<String, String>();
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "site/edit");
        try {
            siteList.put(SiteTitle.SITENAME, siteName);
            siteList.put(SiteTitle.ICP, icp);
            siteList.put(SiteTitle.FLOWCODE, flowCode);
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 保存其它设置
     * @param siteEmail
     * @param sitePhone
     * @param siteLogo
     * @param sellerLogo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "site/update_other", method = RequestMethod.POST)
    public ResultEntity save1(String siteEmail, String sitePhone,
                              String siteLogo, String sellerLogo) {
        HashMap<String, String> siteList = new HashMap<String, String>();
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "site/edit");
        try {
            siteList.put(SiteTitle.SITEEMAIL, siteEmail);
            siteList.put(SiteTitle.SITEPHONE, sitePhone);
            siteList.put(SiteTitle.SITELOGO, siteLogo);
            siteList.put(SiteTitle.SELLERLOGO, sellerLogo);
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }

        return resultEntity;
    }
    /**
     * 保存登录主题图片
     * @param loginImage1
     * @param loginImage2
     * @param loginImage3
     * @param loginImage4
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "site/login/image", method = RequestMethod.POST)
    public ResultEntity siteLoginImage(@RequestParam("loginImage1") String loginImage1,
                                       @RequestParam("loginImage2") String loginImage2,
                                       @RequestParam("loginImage3") String loginImage3,
                                       @RequestParam("loginImage4") String loginImage4) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            HashMap<String,String> siteList = new HashMap<String, String>();
            siteList.put(SiteTitle.LOGINIMAGE1, loginImage1);
            siteList.put(SiteTitle.LOGINIMAGE2, loginImage2);
            siteList.put(SiteTitle.LOGINIMAGE3, loginImage3);
            siteList.put(SiteTitle.LOGINIMAGE4, loginImage4);
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
    /**
     * 保存手机登录设置
     * @param smsRegister
     * @param smsLogin
     * @param smsPassword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "site/login/mobile", method = RequestMethod.POST)
    public ResultEntity siteLoginMobile(@RequestParam("smsRegister") String smsRegister,
                                       @RequestParam("smsLogin") String smsLogin,
                                       @RequestParam("smsPassword") String smsPassword) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            HashMap<String,String> siteList = new HashMap<String, String>();
            siteList.put(SiteTitle.SMSREGISTER, smsRegister);
            siteList.put(SiteTitle.SMSLOGIN, smsLogin);
            siteList.put(SiteTitle.SMSPASSWORD, smsPassword);
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
    /**
     * 保存汇款信息
     * @param accountName
     * @param accountNumber
     * @param bankName
     * @param bankAddress
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "site/pay_info", method = RequestMethod.POST)
    public ResultEntity payInfoSave(String accountName,
                             String accountNumber,
                             String bankName,
                             String bankAddress) {
        ResultEntity resultEntity = new ResultEntity();
        HashMap<String, String> siteList = new HashMap<String, String>();
        try {
            siteList.put(SiteTitle.PAY_ACCOUNT_NAME, accountName);
            siteList.put(SiteTitle.PAY_ACCOUNT_NUMBER, accountNumber);
            siteList.put(SiteTitle.PAY_BANK_NAME, bankName);
            siteList.put(SiteTitle.PAY_BANK_ADDRESS, bankAddress);
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
    /**
     * 保存默认搜索
     * @param defaultSearch
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "site/default_search", method = RequestMethod.POST)
    public ResultEntity defaultSearchSave(String defaultSearch) {
        ResultEntity resultEntity = new ResultEntity();
        HashMap<String, String> siteList = new HashMap<String, String>();
        try {
            siteList.put(SiteTitle.DEFAULT_SEARCH, defaultSearch);
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
}