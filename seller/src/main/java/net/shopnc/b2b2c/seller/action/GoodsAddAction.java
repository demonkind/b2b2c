package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.GoodsSellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * 商品发布
 * Created by shopnc.feng on 2015-11-25.
 */
@Controller
public class GoodsAddAction extends BaseAction {
    @Autowired
    GoodsSellerService goodsSellerService;

    public GoodsAddAction() {
        setMenuPath("goods/add/step_one");
    }

    /**
     * 发布商品第一步
     * 校验商品发布数量是否达到上限
     * @return
     */
    @RequestMapping(value = "goods/add/step_one", method = RequestMethod.GET)
    public String addStepOne() {
        try {
            goodsSellerService.checkGoodsCount(SellerSessionHelper.getStoreId(), SellerSessionHelper.getStoreGoodsLimit(), SellerSessionHelper.getIsOwnShop());
        } catch (ShopException e) {
            SellerSessionHelper.setError(e.getMessage());
            return "redirect:/error";
        }
        return getSellerTemplate("goods/add/step_one");
    }

    /**
     * 发布商品第二步
     */
    @RequestMapping(value = "goods/add/step_two", method = RequestMethod.POST)
    public String addStepTwo(@RequestParam(value = "categoryId") int categoryId, ModelMap modelMap) {
        HashMap<String, Object> param = goodsSellerService.getAddGoodsParam(categoryId, SellerSessionHelper.getStoreId());
        modelMap.put("param", param);
        return getSellerTemplate("goods/add/step_two");
    }

    /**
     * 发布商品第三步
     */
    @RequestMapping(value = "goods/add/step_three", method = RequestMethod.GET)
    public String addStepThree(int commonId,
                               int goodsId,
                               ModelMap modelMap) {
        modelMap.put("commonId", commonId);
        modelMap.put("goodsId", goodsId);
        return getSellerTemplate("goods/add/step_three");
    }
}
