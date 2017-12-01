package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.store.SellerGroupDao;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.SellerGroup;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.SellerService;
import net.shopnc.common.entity.ResultEntity;
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
 * 商家账号管理
 */
@Controller
public class AccountJsonAction extends BaseJsonAction {

    @Autowired
    private SellerService sellerService;

    /**
     * 商家账号添加保存
     * @param sellerName
     * @param password
     * @param sellerEmail
     * @param sellerMobile
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "account/save.json", method = RequestMethod.POST)
    public ResultEntity saveJson(@RequestParam String sellerName,
                                 @RequestParam String password,
                                 @RequestParam String sellerEmail,
                                 @RequestParam String sellerMobile,
                                 @RequestParam int groupId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            sellerService.addSubSeller(sellerName, password, SellerSessionHelper.getStoreId(), SellerSessionHelper.getStoreName(), sellerEmail, sellerMobile, groupId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 商家账号编辑保存
     * @param sellerId
     * @param password
     * @param sellerEmail
     * @param sellerMobile
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "account/update.json",method = RequestMethod.POST)
    public ResultEntity updateJson(@RequestParam int sellerId,
                                 @RequestParam(defaultValue = "") String password,
                                 @RequestParam String sellerEmail,
                                 @RequestParam String sellerMobile,
                                 @RequestParam int groupId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            sellerService.updateSubSeller(sellerId, password, SellerSessionHelper.getStoreId(), sellerEmail, sellerMobile, groupId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 店主账号编辑保存
     * @param sellerId
     * @param sellerEmail
     * @param sellerMobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "account/owner/update.json", method = RequestMethod.POST)
    public ResultEntity saveOwnerJson(@RequestParam int sellerId,
                                 @RequestParam String sellerEmail,
                                 @RequestParam String sellerMobile) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            sellerService.updateSeller(sellerId, SellerSessionHelper.getStoreId(), sellerEmail, sellerMobile);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 商家账号删除
     * @param sellerId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "account/del.json", method = RequestMethod.POST)
    public ResultEntity delJson(@RequestParam int sellerId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            sellerService.delSubSeller(sellerId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

}