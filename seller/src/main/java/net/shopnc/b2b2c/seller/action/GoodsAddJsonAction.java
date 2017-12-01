package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.GoodsSellerService;
import net.shopnc.b2b2c.service.goods.SpecService;
import net.shopnc.b2b2c.vo.SpecAndValueVo;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * 商品发布
 * Created by shopnc.feng on 2015-11-25.
 */
@Controller
public class GoodsAddJsonAction extends BaseJsonAction {
    @Autowired
    GoodsSellerService goodsSellerService;
    @Autowired
    SpecService specService;
    @Autowired
    SecurityHelper securityHelper;

    /**
     * 取得允许使用分类
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/add/get/category.json/{parentId}", method = RequestMethod.GET)
    public ResultEntity getCategory(@PathVariable int parentId) {
        ResultEntity resultEntity = new ResultEntity();

        List<Object> categoryList = goodsSellerService.getBindCategory(parentId, SellerSessionHelper.getStoreId(), SellerSessionHelper.getIsOwnShop());

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setData(categoryList);
        return resultEntity;
    }

    /**
     * 取得可用规格
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/get/spec.json", method = RequestMethod.GET)
    public ResultEntity getSpec() {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);

        List<SpecAndValueVo> specList = specService.findSpecAndValueVoByStoreId(SellerSessionHelper.getStoreId());
        resultEntity.setData(specList);
        return resultEntity;
    }

    /**
     * 保存商品
     * @param goodsCommon
     * @param bindingResult
     * @param goodsJson
     * @param goodsPic
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/add/save.json", method = RequestMethod.POST)
    public ResultEntity save(GoodsCommon goodsCommon,
                             BindingResult bindingResult,
                             String goodsJson,
                             String goodsPic,
                             int[] attributeValueId,
                             int[] storeLabelId,
                             String custom) {
        // 定义返回结果对象
        ResultEntity resultEntity = new ResultEntity();

        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        try {
            //商品详情XSS过滤
            goodsCommon.setGoodsBody(securityHelper.xssClean(goodsCommon.getGoodsBody()));

            // 保存商品
            HashMap<String, Object> map = goodsSellerService.save(
                    goodsCommon,
                    goodsJson,
                    goodsPic,
                    attributeValueId,
                    storeLabelId,
                    custom,
                    SellerSessionHelper.getStoreId(),
                    SellerSessionHelper.getStoreGoodsLimit(),
                    SellerSessionHelper.getIsOwnShop()
            );
            resultEntity.setData(map);
        } catch (ShopException e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlSeller.GOODS_ADD_STEP_THREE);
        return resultEntity;
    }
}
