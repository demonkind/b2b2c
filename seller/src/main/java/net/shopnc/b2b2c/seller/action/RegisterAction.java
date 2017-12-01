package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.service.store.SellerService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.ShopHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterAction {

    protected final Logger logger = Logger.getLogger(getClass());

    @Autowired
    SellerService sellerService;

    @ResponseBody
    @RequestMapping(value = "register.json", method = RequestMethod.POST)
    public ResultEntity registerSave(
            Seller seller,
            @RequestParam String sellerPassword2) {

        ResultEntity resultEntity = new ResultEntity();

        try {
            seller.setSellerPassword(ShopHelper.getMd5(sellerPassword2));
            Timestamp timestamp = ShopHelper.getCurrentTimestamp();
            seller.setIsStoreOwner(State.YES);
            seller.setJoinDate(timestamp);
            seller.setLastLoginTime(timestamp);
            sellerService.addSeller(seller);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("商家会员注册成功");
            resultEntity.setUrl(UrlSeller.LOGIN);
        } catch (Exception e) {
            logger.warn(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("商家会员注册失败");
        }

        return resultEntity;
    }

    @ResponseBody
    @RequestMapping(value = "register/is_seller_exist", method = RequestMethod.GET)
    public String isSellerExist(@RequestParam String sellerName) {
        Seller seller = sellerService.findSellerByName(sellerName);
        if(seller == null) {
            return "true";
        } else {
            return "false";
        }
    }

    @ResponseBody
    @RequestMapping(value = "register/is_email_exist", method = RequestMethod.GET)
    public String isEmailExist(@RequestParam String sellerEmail) {
        Boolean result = sellerService.isSellerEmailExist(sellerEmail);
        if(result) {
            return "false";
        } else {
            return "true";
        }
    }
}