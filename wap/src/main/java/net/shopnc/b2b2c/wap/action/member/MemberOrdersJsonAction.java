package net.shopnc.b2b2c.wap.action.member;

import net.sf.json.JSONObject;
import net.shopnc.b2b2c.domain.goods.GoodsActivity;
import net.shopnc.b2b2c.domain.member.Coupons;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.goods.GoodsActivityService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.service.orders.OrdersService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.KmsHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hou on 2016/3/14.
 */
@Controller
@RequestMapping("orders")
public class MemberOrdersJsonAction  extends MemberBaseJsonAction{
    @Autowired
    private MemberOrdersService memberOrdersService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private GoodsActivityService goodsActivityService;
    /**
     * 取消订单
     * @param ordersId
     * @param cancelReason
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResultEntity cancel(@RequestParam(value = "ordersId",required = true) Integer ordersId,
                               @RequestParam(value = "cancelReason",required = true ) Integer cancelReason) throws ShopException {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            memberOrdersService.cancelOrders(ordersId, SessionEntity.getMemberId(),cancelReason);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        }catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        }catch (Exception e) {
            resultEntity.setMessage("操作失败");
            logger.error(e.toString());
        }
        return resultEntity;
    }

    /**
     * 收货
     * @param ordersId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public ResultEntity receive(@RequestParam(value = "ordersId",required = true) Integer ordersId, HttpServletRequest request) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            memberOrdersService.receiveOrders(ordersId, SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
            //发送K码
            HashMap<String, Object> map = new HashMap<String ,Object>();
        	map.put("ordersId", ordersId);
        	map.put("memberId", SessionEntity.getMemberId());
        	OrdersVo ordersVo = memberOrdersService.getOrdersVoInfo(map);
            List<OrdersGoodsVo> ordersGoods = ordersVo.getOrdersGoodsVoList();
            for(OrdersGoodsVo g : ordersGoods){
            	//判断是否绑定了活动
            	if(goodsActivityService.checkBound(g.getCommonId()) != null){
	            	List<GoodsActivity> goodsActivity = goodsActivityService.findGoodsActivityById(g.getCommonId());
	            	for(GoodsActivity a: goodsActivity){
	            		//如果发送K码设定为收货（sendKCode = 1） 
	            		if("1".equals(a.getSendKCodeType()) && ("1".equals(a.getActivityId()) || "3".equals(a.getActivityId()))){
	            			for(int i=0; i<g.getBuyNum(); i++){
	            				memberService.sendCardCoupons(SessionEntity.getMemberId(), SessionEntity.getMemberName(), a.getReturnAmount(), "", "", g.getGoodsId(), g.getOrdersId(), g.getStoreId());
	            			}
	            		}
	            	}
            	}
            }
        }catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        }catch (Exception e) {
            resultEntity.setMessage("操作失败");
            logger.error(e.getMessage());
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
    @RequestMapping(value = "/shipSearch", method = RequestMethod.GET)
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

    /**
     * 延迟收货
     * @param ordersId
     * @param delayDay
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delayReceive", method = RequestMethod.POST)
    public ResultEntity delayReceive(@RequestParam(value = "ordersId",required = true) Integer ordersId,
                                     @RequestParam(value = "delayDay",required = true) Integer delayDay) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            List<Integer> list = Arrays.asList(5, 10, 15);
            if (list.contains(delayDay) == false) {
                throw new ShopException("参数错误");
            }

            memberOrdersService.delayReceiveOrders(ordersId, SessionEntity.getMemberId(), delayDay);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        }catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        }catch (Exception e) {
            resultEntity.setMessage("操作失败");
            logger.error(e.toString());
        }
        return resultEntity;
    }
    
}
