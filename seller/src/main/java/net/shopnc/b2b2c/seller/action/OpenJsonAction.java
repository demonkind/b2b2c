package net.shopnc.b2b2c.seller.action;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.store.StoreJoininDao;
import net.shopnc.b2b2c.domain.goods.StoreBindCategory;
import net.shopnc.b2b2c.domain.store.StoreCertificate;
import net.shopnc.b2b2c.domain.store.StoreJoinin;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.StoreJoininService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商家开店
 */
@Controller
public class OpenJsonAction extends BaseJsonAction {

    @Autowired
    private StoreJoininDao storeJoininDao;

    @Autowired
    private StoreJoininService storeJoininService;

    /**
     * 商家开店申请保存
     * @param storeCertificate
     * @param storeJoinin
     * @param bindCategory
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "open.json", method = RequestMethod.POST)
    public ResultEntity save(
            StoreCertificate storeCertificate,
            StoreJoinin storeJoinin,
            @RequestParam String bindCategory) {

        ResultEntity resultEntity = new ResultEntity();
        try {
            List<StoreBindCategory> storeBindCategoryList = JsonHelper.toGenericObject(bindCategory, new TypeReference<List<StoreBindCategory>>() {
            });
            storeJoininService.submit(SellerSessionHelper.getSellerId(),
                    SellerSessionHelper.getSellerName(),
                    storeCertificate,
                    storeJoinin,
                    storeBindCategoryList);

            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(ShopConfig.getSellerRoot() + "open/info");
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 商家完成付款保存
     * @param payingCertificate
     * @param payingCertificateExp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "pay.json", method = RequestMethod.POST)
    public ResultEntity pay(@RequestParam String payingCertificate,
                              @RequestParam String payingCertificateExp) {

        ResultEntity resultEntity = new ResultEntity();
        try {
            storeJoininService.pay(SellerSessionHelper.getSellerId(), payingCertificate, payingCertificateExp);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(ShopConfig.getSellerRoot() + "open/info");
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 检查店铺名称是否存在
     * @param storeName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "open/is_store_name_exist", method = RequestMethod.GET)
    public String isStoreNameExist(@RequestParam String storeName) {
        StoreJoinin storeJoinin = storeJoininDao.findByStoreName(storeName);
        if (storeJoinin == null || storeJoinin.getSellerId() == SellerSessionHelper.getSellerId()) {
            return "true";
        } else {
            return "false";
        }
    }

}