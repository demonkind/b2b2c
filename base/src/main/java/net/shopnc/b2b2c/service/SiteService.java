package net.shopnc.b2b2c.service;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.CacheKey;
import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.dao.SiteDao;
import net.shopnc.b2b2c.domain.Site;
import net.shopnc.common.util.CacheHelper;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 站点设置
 * Created by shopnc on 2015/11/16.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SiteService {
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private CacheHelper cacheHelper;

    /**
     * 取得基本信息
     *
     * @return
     */
    public HashMap getSiteInfo() {
        HashMap<String, String> siteInfo = new HashMap<String, String>();
        String siteString = cacheHelper.get(CacheKey.SITE);
        //缓存处理
        if (siteString == null || siteString.equals("")) {
            List<Site> siteList = siteDao.findAll(Site.class);
            for (int i = 0; i < siteList.size(); i++) {
                siteInfo.put(siteList.get(i).getTitle(), siteList.get(i).getValue());
            }
            siteInfo.put(SiteTitle.SITE_LOGO_URL, getSiteLogo(siteInfo.get(SiteTitle.SITELOGO)));
            siteInfo.put(SiteTitle.SELLER_LOGO_URL, this.getSellerLogo(siteInfo.get(SiteTitle.SELLERLOGO)));
            siteInfo.put(SiteTitle.LOGIN_IMAGE1_URL, this.getLoginImage1(siteInfo.get(SiteTitle.LOGINIMAGE1)));
            siteInfo.put(SiteTitle.LOGIN_IMAGE2_URL, this.getLoginImage2(siteInfo.get(SiteTitle.LOGINIMAGE2)));
            siteInfo.put(SiteTitle.LOGIN_IMAGE3_URL, this.getLoginImage3(siteInfo.get(SiteTitle.LOGINIMAGE3)));
            siteInfo.put(SiteTitle.LOGIN_IMAGE4_URL, this.getLoginImage4(siteInfo.get(SiteTitle.LOGINIMAGE4)));

            cacheHelper.set(CacheKey.SITE, JsonHelper.toJson(siteInfo));
        } else {
            siteInfo = JsonHelper.toGenericObject(siteString, new TypeReference<HashMap<String, String>>() {
            });
        }

        return siteInfo;
    }

    /**
     * 更新站点信息
     *
     * @param siteList
     */
    public void updateSite(HashMap<String, String> siteList) {
        //清除商城配置缓存
        cacheHelper.del(CacheKey.SITE);

        List<Site> sites = new ArrayList<Site>();
        for (String title : siteList.keySet()) {
            Site site = new Site();
            site.setTitle(title);
            site.setValue(siteList.get(title));
            sites.add(site);
        }
        siteDao.updateAll(sites);
    }

    /**
     * 取得网站LOGO
     *
     * @param param
     * @return
     */
    private String getSiteLogo(String param) {
        String value;
        if (param == null || param.equals("")) {
            value = ShopConfig.getPublicRoot() + Common.DEFAULT_SITE_LOGO_URL;
        } else {
            value = ShopConfig.getUploadRoot() + param.toString();
        }
        return value;
    }

    /**
     * 取得商家LOGO
     *
     * @param param
     * @return
     */
    private String getSellerLogo(String param) {
        String value;
        if (param == null || param.equals("")) {
            value = ShopConfig.getPublicRoot() + Common.DEFAULT_SELLER_LOGO_URL;
        } else {
            value = ShopConfig.getUploadRoot() + param.toString();
        }
        return value;
    }

    /**
     * 取得登录图
     *
     * @param param
     * @return
     */
    private String getLoginImage1(String param) {
        String value;
        if (param == null || param.equals("")) {
            value = ShopConfig.getPublicRoot() + Common.DEFAULT_LOGIN_IMAGE_URL1;
        } else {
            value = ShopConfig.getUploadRoot() + param;
        }
        return value;
    }

    /**
     * 取得登录图
     *
     * @param param
     * @return
     */
    private String getLoginImage2(String param) {
        String value;
        if (param == null || param.equals("")) {
            value = ShopConfig.getPublicRoot() + Common.DEFAULT_LOGIN_IMAGE_URL2;
        } else {
            value = ShopConfig.getUploadRoot() + param;
        }
        return value;
    }

    /**
     * 取得登录图
     *
     * @param param
     * @return
     */
    private String getLoginImage3(String param) {
        String value;
        if (param == null || param.equals("")) {
            value = ShopConfig.getPublicRoot() + Common.DEFAULT_LOGIN_IMAGE_URL3;
        } else {
            value = ShopConfig.getUploadRoot() + param;
        }
        return value;
    }

    /**
     * 取得登录图
     *
     * @param param
     * @return
     */
    private String getLoginImage4(String param) {
        String value;
        if (param == null || param.equals("")) {
            value = ShopConfig.getPublicRoot() + Common.DEFAULT_LOGIN_IMAGE_URL4;
        } else {
            value = ShopConfig.getUploadRoot() + param;
        }
        return value;
    }
}
