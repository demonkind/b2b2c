package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.b2b2c.service.orders.StoreOrdersService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 订单列表
 * Created by hou on 2016/3/14.
 */
@Controller
public class OrdersJsonAction extends BaseJsonAction {
    @Autowired
    private StoreOrdersService storeOrdersService;
    @Autowired
    private OrdersService ordersService;

    /**
     * 取消订单
     * @param ordersId
     * @param cancelReason
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orders/cancel", method = RequestMethod.POST)
    public ResultEntity cancel(@RequestParam(value = "ordersId",required = true) Integer ordersId,
                               @RequestParam(value = "cancelReason",required = true) Integer cancelReason) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            storeOrdersService.cancelOrders(ordersId,SellerSessionHelper.getStoreId(),cancelReason);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        }catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        }catch (Exception e) {
            resultEntity.setMessage("操作失败");
            logger.error(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 修改运费
     * @param ordersId
     * @param freightAmount
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orders/modify/freight", method = RequestMethod.POST)
    public ResultEntity modifyFreight(@RequestParam(value = "ordersId",required = true) Integer ordersId,
                                      @RequestParam(value = "freightAmount",required = true) BigDecimal freightAmount) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            storeOrdersService.modifyFreight(ordersId, SellerSessionHelper.getStoreId(), freightAmount);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        }catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        }catch (Exception e) {
            resultEntity.setMessage("操作失败");
            logger.error(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 发货
     * @param areaId1
     * @param areaId2
     * @param areaId3
     * @param areaId4
     * @param areaInfo
     * @param ordersId
     * @param receiverName
     * @param receiverPhone
     * @param receiverAddress
     * @param shipCode
     * @param shipSn
     * @param shipNote
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orders/send/save", method = RequestMethod.POST)
    public ResultEntity send(@RequestParam(value = "areaId1",required = false,defaultValue = "0") Integer areaId1,
                             @RequestParam(value = "areaId2",required = false) Integer areaId2,
                             @RequestParam(value = "areaId3",required = false) Integer areaId3,
                             @RequestParam(value = "areaId4",required = false) Integer areaId4,
                             @RequestParam(value = "areaInfo",required = false) String areaInfo,
                             @RequestParam(value = "ordersId",required = true) Integer ordersId,
                             @RequestParam(value = "receiverName",required = true) String receiverName,
                             @RequestParam(value = "receiverPhone",required = true) String receiverPhone,
                             @RequestParam(value = "receiverAddress",required = true) String receiverAddress,
                             @RequestParam(value = "shipCode",required = false) String shipCode,
                             @RequestParam(value = "shipSn",required = false) String shipSn,
                             @RequestParam(value = "shipNote",required = false) String shipNote) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            storeOrdersService.send(areaId1, areaId2, areaId3, areaId4, areaInfo, receiverName, receiverPhone, receiverAddress, ordersId, SellerSessionHelper.getStoreId(), shipCode, shipSn, shipNote);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        }catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        }catch (Exception e) {
            resultEntity.setMessage("操作失败");
            logger.error(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 读取发货与收货信息
     * @param ordersId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orders/send/info/json/{ordersId}", method = RequestMethod.GET)
    public ResultEntity info(@PathVariable Integer ordersId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            HashMap<String,Object> params = new HashMap<String, Object>();
            params.put("ordersId",ordersId);
            params.put("storeId", SellerSessionHelper.getStoreId());
            Orders orders = ordersService.getOrdersInfo(params);
            resultEntity.setData(orders);
            resultEntity.setCode(ResultEntity.SUCCESS);
        }catch (Exception e) {
            resultEntity.setMessage("操作失败");
            logger.info(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 快递跟踪
     * @param shipSn
     * @param shipCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orders/ship/search/{shipSn}/{shipCode}", method = RequestMethod.GET)
    public ResultEntity shipSearch(@PathVariable String shipSn,
                                   @PathVariable String shipCode) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            resultEntity.setData(ordersService.shipSearch(shipSn, shipCode));
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return resultEntity;
    }
}
