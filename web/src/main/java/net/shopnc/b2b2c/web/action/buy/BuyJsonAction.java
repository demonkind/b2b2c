package net.shopnc.b2b2c.web.action.buy;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.OrdersOrdersFrom;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.buy.BuyFreightService;
import net.shopnc.b2b2c.service.buy.BuyService;
import net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo;
import net.shopnc.b2b2c.vo.buy.BuyStoreVo;
import net.shopnc.b2b2c.vo.buy.freight.BuyStoreFreightListVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.buy.BuySecondPostDataEntity;
import net.shopnc.common.entity.buy.OrdersEntity;
import net.shopnc.common.entity.buy.postjson.PostJsonStoreListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 购买
 * Created by hou on 2016/3/14.
 */
@Controller
public class BuyJsonAction extends BuyBaseJsonAction {
    @Autowired
    private BuyService buyService;
    @Autowired
    private BuyFreightService buyFreightService;

    /**
     * 购买第二步[保存生成订单]
     * @param buyData
     * @param modelMap
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/save", method = RequestMethod.POST)
    public ResultEntity buySecond(@RequestParam(value = "buyData", required = true) String buyData, ModelMap modelMap) {
        logger.info("购买第二步[保存生成订单]");
        ResultEntity resultEntity = new ResultEntity();
        try {
            if (SessionEntity.getAllowBuy() == State.NO) {
                throw new ShopException("您的账户被禁止购买商品");
            }
            //解析提交json数据
            logger.info("解析提交数据");
            logger.info(buyData);
            ObjectMapper mapper = new ObjectMapper();
            PostJsonStoreListEntity postJsonStoreListEntity = mapper.readValue(buyData,PostJsonStoreListEntity.class);
            BuySecondPostDataEntity buySecondPostDataEntity = new BuySecondPostDataEntity(
                    postJsonStoreListEntity,
                    SessionEntity.getMemberId(),
                    SessionEntity.getMemberName(),
                    OrdersOrdersFrom.WEB);

            //数据验证
            logger.info("数据验证");
            buySecondPostDataEntity = buyService.buySecondValidatePostDataEntity(buySecondPostDataEntity);

            //得到商品列表
            logger.info("得到商品列表");
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyService.buySecondGetGoodsList(buySecondPostDataEntity);

            //锁定并获取商品最新库存
            logger.info("锁定并获取商品最新库存");
            buyGoodsItemVoList = buyService.buySecondGetLockGoodsList(buyGoodsItemVoList);

            //得到店铺列表
            logger.info("得到店铺列表");
            List<BuyStoreVo> buyStoreVoList = buyService.buySecondGetStoreList(buyGoodsItemVoList);

            //计算店铺运费
            logger.info("计算店铺运费");
            buyStoreVoList = buyService.buySecondCalcStoreFreight(buySecondPostDataEntity, buyStoreVoList);

            logger.info("是否存在不支持配送的商品");
            buyService.buySecondIsExitsNoAllowSendGoods(buyStoreVoList);

            //验证店铺促销
            logger.info("验证店铺促销");
            buyService.buySecondParseStorePromotions();

            //验证平台促销
            logger.info("验证平台促销");
            buyService.buySecondParsePlatFormPromotions();

            //其它验证
            logger.info("其它验证");
            buyService.buySecondValidateCommon(buySecondPostDataEntity, buyGoodsItemVoList);

            //生成订单
            logger.info("生成订单");
            OrdersEntity ordersEntity = buyService.buySecondeCreateOrders(buySecondPostDataEntity, buyStoreVoList);

            logger.info("消息通知");
            buyService.buySecondOrdersAfterCommon(ordersEntity, buyGoodsItemVoList);

            //返回操作结果
            resultEntity.setUrl(ShopConfig.getWebRoot() + "buy/pay/payment/list/" + ordersEntity.getOrdersPay().getPayId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("订单生成功");
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            logger.error(e.getMessage());
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("订单生成失败");
            logger.error(e.getMessage());
        }

        return resultEntity;
    }

    /**
     * 异步运费计算
     * @param buyData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/calc/freight", method = RequestMethod.POST)
    public ResultEntity calcFreight(@RequestParam(value = "buyData", required = true) String buyData) {
        logger.info("下单时根据地区异步计算运费");
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            //解析提交json数据
            logger.info("解析postjson数据:");
            logger.info(buyData);
            ObjectMapper mapper = new ObjectMapper();
            PostJsonStoreListEntity postJsonStoreListEntity = mapper.readValue(buyData,PostJsonStoreListEntity.class);
            BuySecondPostDataEntity buySecondPostDataEntity = new BuySecondPostDataEntity(
                    postJsonStoreListEntity,
                    SessionEntity.getMemberId(),
                    SessionEntity.getMemberName(),
                    OrdersOrdersFrom.WEB);
            //得到商品列表
            logger.info("得到商品列表");
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyService.buySecondGetGoodsList(buySecondPostDataEntity);

            //得到店铺列表
            logger.info("商品按店铺分组");
            List<BuyStoreVo> buyStoreVoList = buyService.buySecondGetStoreList(buyGoodsItemVoList);

            //计算店铺运费
            logger.info("计算店铺运费");
            buyStoreVoList = buyService.buySecondCalcStoreFreight(buySecondPostDataEntity, buyStoreVoList);

            logger.info("生成返回格式Vo");
            BuyStoreFreightListVo buyStoreFreightListVo = buyFreightService.getFreightListVo(buyStoreVoList);

            resultEntity.setData(buyStoreFreightListVo);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("获取运费信息成功");
        } catch (ShopException e) {
            logger.error(e.getMessage());
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setMessage("获取运费信息失败");
        }
        return resultEntity;
    }
}
