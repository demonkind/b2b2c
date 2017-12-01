package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.StoreService;
import net.shopnc.b2b2c.service.store.StoreSlideService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 店铺设置
 */
@Controller
public class StoreSettingJsonAction extends BaseJsonAction {

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreSlideService storeSlideService;

    /**
     * 店铺设置保存
     * @param store
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store/setting/save.json", method = RequestMethod.POST)
    public ResultEntity settingSaveJson(Store store) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            storeService.updateStoreSetting(SellerSessionHelper.getStoreId(), store);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(UrlSeller.STORE_SETTING);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 店铺幻灯保存
     * @param slideImgList
     * @param slideUrlList
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store/slide/save.json", method = RequestMethod.POST)
    public ResultEntity saveSlideJson(@RequestParam(name = "slideImg[]") List<String> slideImgList,
                                      @RequestParam(name = "slideUrl[]") List<String> slideUrlList) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            storeSlideService.saveByStoreId(SellerSessionHelper.getStoreId(), slideImgList, slideUrlList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(UrlSeller.STORE_SLIDE);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }
}
