package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.FreightTemplateCalcType;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.orders.FreightAreaDao;
import net.shopnc.b2b2c.dao.orders.FreightTemplateDao;
import net.shopnc.b2b2c.domain.FreightArea;
import net.shopnc.b2b2c.domain.FreightTemplate;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.FreightTemplateService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 运费模板
 * Created by hou on 2016/3/14.
 */
@Controller
public class FreightTemplateJsonAcion extends BaseJsonAction {
    @Autowired
    private FreightTemplateDao freightTemplateDao;
    @Autowired
    private FreightTemplateService freightTemplateService;
    @Autowired
    private FreightAreaDao freightAreaDao;

    /**
     * 复制
     * @param freightId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "freight/template/clone", method = RequestMethod.POST)
    public ResultEntity clone(@RequestParam(value = "freightId",required = true) Integer freightId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            freightTemplateService.cloneFreight(freightId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 新增保存运费模板
     * @param calcType
     * @param title
     * @param freightId
     * @param freightFree
     * @param item1
     * @param price1
     * @param item2
     * @param price2
     * @param areasList
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "freight/template/save",method = RequestMethod.POST)
    public ResultEntity save(@RequestParam(value = "calcType",required = false,defaultValue = FreightTemplateCalcType.NUMBER) String calcType,
                             @RequestParam(value = "title",required = true) String title,
                             @RequestParam(value = "freightId",required = false) Integer freightId,
                             @RequestParam(value = "freightFree",required = false, defaultValue = "0") Integer freightFree,
                             @RequestParam(value = "item1",required = true) List<BigDecimal> item1,
                             @RequestParam(value = "price1",required = true) List<BigDecimal> price1,
                             @RequestParam(value = "item2",required = true) List<BigDecimal> item2,
                             @RequestParam(value = "price2",required = true) List<BigDecimal> price2,
                             @RequestParam(value = "areas",required = true) List<String> areasList
    ) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            freightTemplateService.saveFreight(SellerSessionHelper.getStoreId(), calcType, title, freightId, freightFree, item1, price1, item2, price2, areasList);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setMessage("保存失败");
        }
        return resultEntity;
    }

    /**
     * 删除
     * @param freightId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "freight/template/del", method = RequestMethod.POST)
    public ResultEntity del(@RequestParam(value = "freightId",required = false) Integer freightId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            freightTemplateService.delFreight(freightId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 异步读取运费模板信息
     * @param freightId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "freight/template/info/json/{freightId}", method = RequestMethod.GET)
    public ResultEntity infoJson(@PathVariable Integer freightId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            if (freightId == null) {
                throw new ShopException("参数错误");
            }
            FreightTemplate freightTemplate = freightTemplateDao.getFreightTemplateInfo(freightId, SellerSessionHelper.getStoreId());
            if (freightTemplate == null) {
                throw new ShopException("该信息不存在");
            }

            List<FreightArea> freightAreaList = freightAreaDao.getFreightAreaByFreightId(freightId);
            if (freightAreaList == null) {
                throw new ShopException("该信息不存在");
            }

            String item = "";
            if (freightTemplate.getCalcType().equals(FreightTemplateCalcType.NUMBER)) {
                item = "件";
            }else if (freightTemplate.getCalcType().equals(FreightTemplateCalcType.WEIGHT)) {
                item = "kg";
            }else if (freightTemplate.getCalcType().equals(FreightTemplateCalcType.VOLUME)) {
                item = "m<sup>3</sup>";
            }

            String calcRule = "";
            if (freightTemplate.getFreightFree() == State.YES) {
                calcRule = "包邮";
            } else {
                calcRule = String.format("%s%s内%s元，每增加%s%s，加%s元",
                        freightTemplate.getCalcType().equals(FreightTemplateCalcType.NUMBER) ? freightAreaList.get(0).getItem1().setScale(0, BigDecimal.ROUND_DOWN).toString() : freightAreaList.get(0).getItem1().toString(),
                        item,
                        freightAreaList.get(0).getPrice1().toString(),
                        freightAreaList.get(0).getItem2().toString(),
                        item,
                        freightAreaList.get(0).getPrice2());
            }

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("area",freightAreaList.get(0).getAreaName());
            hashMap.put("calcRule",calcRule);
            hashMap.put("calcType",freightTemplate.getCalcType());
            resultEntity.setData(hashMap);

            resultEntity.setCode(ResultEntity.SUCCESS);

        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setMessage("读取失败");
        }
        return resultEntity;
    }
}
