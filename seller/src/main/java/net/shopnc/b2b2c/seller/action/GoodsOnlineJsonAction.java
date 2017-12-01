package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.GoodsSellerService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * 出售中的商品
 * Created by shopnc.feng on 2015-12-14.
 */
@Controller
public class GoodsOnlineJsonAction extends BaseJsonAction {
    @Autowired
    GoodsSellerService goodsSellerService;
    @Autowired
    SecurityHelper securityHelper;

    /**
     * 获取SPU下已选择规格，和已添加图片
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/edit/get/goods.json", method = RequestMethod.POST)
    public ResultEntity getGoods(int commonId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.GOODS_ONLINE_LIST);

        try {
            HashMap<String, Object> map = goodsSellerService.findGoodsEditParam(commonId);
            resultEntity.setData(map);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 商品编辑保存
     * @param goodsCommon
     * @param bindingResult
     * @param goodsJson
     * @param goodsPic
     * @param attributeValueId
     * @param storeLabelId
     * @param custom
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/update.json", method = RequestMethod.POST)
    public ResultEntity update(GoodsCommon goodsCommon,
                               BindingResult bindingResult,
                               String goodsJson,
                               String goodsPic,
                               int[] attributeValueId,
                               int[] storeLabelId,
                               String custom) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.GOODS_ONLINE_LIST);

        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("错误消息");
            return resultEntity;
        }
        try {
            goodsCommon.setGoodsBody(securityHelper.xssClean(goodsCommon.getGoodsBody()));
            goodsSellerService.update(goodsCommon, goodsJson, goodsPic, attributeValueId, storeLabelId, custom, SellerSessionHelper.getStoreId());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("编辑成功");
        return resultEntity;
    }

    /**
     * 查询SPU下全部SKU
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/get/sku.json", method = RequestMethod.POST)
    public ResultEntity getSku(int commonId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            List<Object> goodsList = goodsSellerService.findSkuByCommonId(commonId, SellerSessionHelper.getStoreId());
            resultEntity.setData(goodsList);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 保存SKU商品
     * @param goods
     * @param bindingResult
     * @param goodsStorage
     * @param goodsStorageAlarm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/save/sku.json", method = RequestMethod.POST)
    public ResultEntity saveSku(Goods goods,
                                BindingResult bindingResult,
                                Integer goodsStorage,
                                Integer goodsStorageAlarm) {
        ResultEntity resultEntity = new ResultEntity();
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("错误消息");
            return resultEntity;
        }

        try {
            goods.setGoodsBody(securityHelper.xssClean(goods.getGoodsBody()));
            goodsSellerService.saveSku(goods, goodsStorage, goodsStorageAlarm, SellerSessionHelper.getStoreId());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setData(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 删除商品
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(Integer commonId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setData(ShopConfig.getSellerRoot() + "goods/online/list");
        try {
            goodsSellerService.delete(commonId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 商品下架
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/offline.json", method = RequestMethod.POST)
    public ResultEntity goodsOffline(Integer[] commonId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getSellerRoot() + "goods/online/list");
        try {
            goodsSellerService.offline(commonId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
        }
        return resultEntity;
    }

    /**
     * 推荐商品
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/commend.json", method = RequestMethod.POST)
    public ResultEntity goodsCommend(Integer[] commonId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getSellerRoot() + "goods/online/list");
        try {
            goodsSellerService.commend(commonId, SellerSessionHelper.getStoreId(), SellerSessionHelper.getStoreCommendLimit());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.toString());
        }
        return resultEntity;
    }

    /**
     * 取消推荐
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/cancel_commend.json", method = RequestMethod.POST)
    public ResultEntity goodsCancelCommend(Integer[] commonId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getSellerRoot() + "goods/online/list");
        try {
            goodsSellerService.cancelCommend(commonId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
        }
        return resultEntity;
    }

    @ResponseBody
    @RequestMapping(value = "goods/save/format.json", method = RequestMethod.POST)
    public ResultEntity saveFormat(Integer[] commonId,
                                   int formatTop,
                                   int formatBottom) {
        ResultEntity resultEntity = new ResultEntity();
        goodsSellerService.editFormat(commonId, formatTop, formatBottom, SellerSessionHelper.getStoreId());
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
}
