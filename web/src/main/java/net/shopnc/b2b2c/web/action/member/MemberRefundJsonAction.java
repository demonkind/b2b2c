package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.service.refund.MemberRefundService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 *
 * Created by cj on 2016/2/2.
 */
@Controller
@RequestMapping("/member/refund")
public class MemberRefundJsonAction extends MemberBaseJsonAction {
    @Autowired
    private MemberRefundService memberRefundService;
    /**
     * 整单退款申请保存
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveall", method = RequestMethod.POST)
    public ResultEntity saveAll(@RequestParam(value = "buyerMessage", required = true) String buyerMessage,
                                @RequestParam(value = "picJson", required = false) String picJson,
                                @RequestParam(value = "ordersId", required = true) int ordersId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            Refund refund = new Refund();
            refund.setBuyerMessage(buyerMessage);
            refund.setPicJson(picJson);
            refund.setOrdersId(ordersId);
            refund.setMemberId(SessionEntity.getMemberId());
            memberRefundService.saveRefundAll(refund);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setMessage("退款申请成功，请等待卖家审核");
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(ShopConfig.getWebRoot() + "member/refund/list");
        return resultEntity;
    }

    /**
     * 单品退款申请保存
     * @param buyerMessage
     * @param refundAmount
     * @param picJson
     * @param ordersId
     * @param ordersGoodsId
     * @param reasonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "savegoods", method = RequestMethod.POST)
    public ResultEntity saveGoods(@RequestParam(value = "buyerMessage", required = true) String buyerMessage,
                                  @RequestParam(value = "refundAmount", required = true) BigDecimal refundAmount,
                                  @RequestParam(value = "picJson", required = false) String picJson,
                                  @RequestParam(value = "ordersId", required = true) int ordersId,
                                  @RequestParam(value = "ordersGoodsId", required = true) int ordersGoodsId,
                                  @RequestParam(value = "reasonId", required = true) int reasonId
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
            refund.setRefundType(RefundState.REFUND_TYPE_REFUND);
            memberRefundService.saveGoodsRefund(refund);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setMessage("退款申请成功，请等待卖家审核");
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(ShopConfig.getWebRoot() + "member/refund/list");
        return resultEntity;
    }
}
