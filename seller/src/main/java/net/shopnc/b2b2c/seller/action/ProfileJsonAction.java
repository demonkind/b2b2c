package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.exception.SellerPasswordErrorException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.SellerService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileJsonAction extends BaseJsonAction {

    @Autowired
    private SellerService sellerService;

    @ResponseBody
    @RequestMapping(value = "profile", method = RequestMethod.POST)
    public ResultEntity savePassword(@RequestParam(defaultValue = "") String password,
                               @RequestParam(defaultValue = "") String newPassword,
                               @RequestParam(defaultValue = "") String avatar) {

        ResultEntity resultEntity = new ResultEntity();

        try {
            sellerService.updateProfile(SellerSessionHelper.getSellerName(), password, newPassword, avatar);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(UrlSeller.HOME);
        } catch (SellerPasswordErrorException ex) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("原密码错误");
        } catch (Exception ex) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("密码修改失败");
        }

        return resultEntity;
    }
}