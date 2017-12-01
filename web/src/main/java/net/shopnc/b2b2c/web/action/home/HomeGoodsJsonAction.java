package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.FreightTemplateService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * 商品详情
 * Created by shopnc.feng on 2015-12-03.
 */
@Controller
public class HomeGoodsJsonAction extends HomeBaseJsonAction {
    @Autowired
    GoodsService goodsService;
    @Autowired
    FreightTemplateService freightTemplateService;

    private static int SHOW_COMMEND_NUM = 3;
    /**
     * 获取推荐的商品
     *
     * @param storeId
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/commend.json", method = RequestMethod.POST)
    public ResultEntity getCommend(int storeId,
                                   int commonId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            List<Object> list = goodsService.findCommendGoodsVoByStoreIdNeqCommonId(storeId, commonId, this.SHOW_COMMEND_NUM);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(list);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
        }
        return resultEntity;
    }

    /**
     * 根据地区切换计算运费
     * @param goodsId
     * @param commonId
     * @param buyNum
     * @param areaId2
     * @return allowSend 1支持/0不支持 配送,freightAmount运费，不支持配送时freightAmount=null
     */
    @ResponseBody
    @RequestMapping(value = "goods/calc/freight",method = RequestMethod.GET)
    public ResultEntity calcFreight(int goodsId, int commonId, int buyNum, int areaId2) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            HashMap<String,Object> hashMap = freightTemplateService.calcFreight(goodsId, commonId, buyNum, areaId2);
            resultEntity.setData(hashMap);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            resultEntity.setMessage("计算失败");
            logger.error(e.getMessage());
        }
        return resultEntity;
    }
}
