package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.store.StoreNavigationDao;
import net.shopnc.b2b2c.domain.store.StoreNavigation;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 店铺导航Action
 * Created by dqw on 2016/01/22.
 */
@Controller
public class StoreNavigationJsonAction extends BaseJsonAction {

    @Autowired
    StoreNavigationDao storeNavigationDao;
    @Autowired
    SecurityHelper securityHelper;

    /**
     * 店铺导航保存
     * @param navId
     * @param storeNavigation
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store/navigation/save.json", method = RequestMethod.POST)
    public ResultEntity saveJson(@RequestParam(defaultValue = "0") int navId,
                                 StoreNavigation storeNavigation) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            if (navId > 0) {
                StoreNavigation oldStoreNavigation = storeNavigationDao.get(StoreNavigation.class, navId);
                if (oldStoreNavigation.getStoreId() == SellerSessionHelper.getStoreId()) {
                    oldStoreNavigation.setUrl(storeNavigation.getUrl());
                    oldStoreNavigation.setContent(securityHelper.xssClean(storeNavigation.getContent()));
                    oldStoreNavigation.setIsNewPage(storeNavigation.getIsNewPage());
                    oldStoreNavigation.setSort(storeNavigation.getSort());
                    oldStoreNavigation.setTitle(storeNavigation.getTitle());
                    oldStoreNavigation.setIsShow(storeNavigation.getIsShow());
                    storeNavigationDao.update(oldStoreNavigation);
                    resultEntity.setCode(ResultEntity.SUCCESS);
                    resultEntity.setUrl(UrlSeller.STORE_NAVIGATION_LIST);
                } else {
                    resultEntity.setCode(ResultEntity.FAIL);
                    resultEntity.setUrl(UrlSeller.STORE_NAVIGATION_LIST);
                }
            } else {
                storeNavigation.setStoreId(SellerSessionHelper.getStoreId());
                storeNavigationDao.save(storeNavigation);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setUrl(UrlSeller.STORE_NAVIGATION_LIST);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }


    /**
     * 店铺导航删除
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store/navigation/del.json", method = RequestMethod.POST)
    public ResultEntity delJson(@RequestParam int id) {
        ResultEntity resultEntity = new ResultEntity();
        StoreNavigation storeNavigation = storeNavigationDao.get(StoreNavigation.class, id);
        if (storeNavigation.getStoreId() == SellerSessionHelper.getStoreId()) {
            storeNavigationDao.delete(storeNavigation);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(UrlSeller.STORE_NAVIGATION_LIST);
        } else {
            resultEntity.setCode(ResultEntity.FAIL);
        }

        return resultEntity;
    }

}