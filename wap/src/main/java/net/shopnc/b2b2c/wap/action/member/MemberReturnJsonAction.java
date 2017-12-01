package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.service.refund.MemberRefundService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 退货处理
 * Created by cj on 2016/2/2.
 */
@Controller
@RequestMapping("/member/return")
public class MemberReturnJsonAction extends MemberBaseJsonAction {
    @Autowired
    private MemberRefundService memberRefundService;
    /**
     * 退货申请保存
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResultEntity saveRetrun(@RequestParam(value = "buyerMessage", required = true) String buyerMessage,
                                   @RequestParam(value = "refundAmount", required = true) BigDecimal refundAmount,
                                   @RequestParam(value = "picJson", required = false) String picJson,
                                   @RequestParam(value = "ordersId", required = true) int ordersId,
                                   @RequestParam(value = "ordersGoodsId", required = true) int ordersGoodsId,
                                   @RequestParam(value = "reasonId", required = true) int reasonId,
                                   @RequestParam(value = "goodsNum", required = true) int goodsNum
    ) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            Refund refund = new Refund();
            refund.setBuyerMessage(buyerMessage);
            refund.setPicJson(picJson);
            refund.setOrdersId(ordersId);
            refund.setRefundAmount(refundAmount);
            refund.setOrdersGoodsId(ordersGoodsId);
            refund.setMemberId(SessionEntity.getMemberId());
            refund.setReasonId(reasonId);
            refund.setMemberName(SessionEntity.getMemberName());
            refund.setRefundType(RefundState.REFUND_TYPE_RETURN);
            refund.setGoodsNum(goodsNum);
            memberRefundService.saveGoodsRefund(refund);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setMessage("退款申请成功，请等待卖家审核");
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(ShopConfig.getWebRoot() + "member/return/list");
        return resultEntity;
    }


    /**
     * 退货发货保存
     *
     * @param shipId
     * @param shipSn
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveship", method = RequestMethod.POST)
    public ResultEntity saveShip(
            @RequestParam(value = "refundId", required = true) int refundId,
            @RequestParam(value = "shipId", required = true) int shipId,
            @RequestParam(value = "shipSn", required = true) String shipSn
    ) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            memberRefundService.saveReturnShip(refundId, shipId, shipSn, SessionEntity.getMemberId());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setMessage("发货成功，请等待卖家收货");
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(ShopConfig.getWebRoot() + "member/return/list");
        return resultEntity;
    }

    /**
     * 退货延迟
     * @param refundId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "savedelay", method = RequestMethod.POST)
    public ResultEntity saveDelay(
            @RequestParam(value = "refundId", required = true) int refundId
    ) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            memberRefundService.saveReturnDelay(refundId,SessionEntity.getMemberId());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setMessage("退货延时成功");
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(ShopConfig.getWebRoot() + "member/return/list");
        return resultEntity;
    }
}
