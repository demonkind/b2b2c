package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.GoodsSellerService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 仓库中的商品
 * Created by shopnc.feng on 2015-12-23.
 */
@Controller
public class GoodsOfflineJsonAction extends BaseJsonAction {
    @Autowired
    GoodsSellerService goodsSellerService;

    /**
     * 商品上架
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/online.json", method = RequestMethod.POST)
    public ResultEntity online(Integer[] commonId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getSellerRoot() + "goods/offline/list");
        try {
            goodsSellerService.online(commonId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
}
