package net.shopnc.b2b2c.service.refund;

import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.domain.refund.RefundDetail;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.orders.AdminOrdersService;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.entity.dtgrid.QueryUtils;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cj on 2016/2/1.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AdminRefundService extends RefundService {

    @Autowired
    AdminOrdersService adminOrdersService;

    /**
     * 退货列表
     *
     * @param dtGrid
     * @return
     * @throws Exception
     */
    public DtGrid getRefundDtGridList(DtGrid dtGrid) throws Exception {
        /**
         * long 型数据查询替换一下查询字段名
         * by hbj
         */
        if (dtGrid.getFastQueryParameters().containsKey("eq_refundSnStr")) {
            dtGrid.getFastQueryParameters().put("eq_refundSn", dtGrid.getFastQueryParameters().get("eq_refundSnStr"));
            dtGrid.getFastQueryParameters().remove("eq_refundSnStr");
        }
        if (dtGrid.getFastQueryParameters().containsKey("eq_ordersSnStr")) {
            dtGrid.getFastQueryParameters().put("eq_ordersSn", dtGrid.getFastQueryParameters().get("eq_refundSnStr"));
            dtGrid.getFastQueryParameters().remove("eq_ordersSnStr");
        }


//        dtGrid.getFastQueryParameters().put("eq_refundType", RefundState.REFUND_TYPE_REFUND);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
            for (String key : dtGrid.getNcColumnsType().keySet()) {
                for (int i = 0; i < dtGrid.getNcColumnsType().get(key).size(); i++) {
                    hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                }
                dtGrid.setNcColumnsTypeList(hashMap);
            }
        }
        QueryUtils.parseDtGridHql(dtGrid);
        dtGrid = refundDao.getDtGridList(dtGrid, Refund.class);

        List<Object> refunds = dtGrid.getExhibitDatas();
        List<Object> refundAdminLists = new ArrayList<Object>();
        for (int j = 0; j < refunds.size(); j++) {
            Refund refund = (Refund) refunds.get(j);
            RefundItemVo refundItemVo = new RefundItemVo(refund);
            refundAdminLists.add(refundItemVo);
        }
        dtGrid.setExhibitDatas(refundAdminLists);
        return dtGrid;
    }

    /**
     * 退货列表
     *
     * @param dtGrid
     * @return
     * @throws Exception
     */
    public DtGrid getReturnDtGridList(DtGrid dtGrid) throws Exception {
        /**
         * long 型数据查询替换一下查询字段名
         * by hbj
         */
        if (dtGrid.getFastQueryParameters().containsKey("eq_refundSnStr")) {
            dtGrid.getFastQueryParameters().put("eq_refundSn", dtGrid.getFastQueryParameters().get("eq_refundSnStr"));
            dtGrid.getFastQueryParameters().remove("eq_refundSnStr");
        }
//        dtGrid.getFastQueryParameters().put("eq_refundType", RefundState.REFUND_TYPE_RETURN);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
            for (String key : dtGrid.getNcColumnsType().keySet()) {
                for (int i = 0; i < dtGrid.getNcColumnsType().get(key).size(); i++) {
                    hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                }
                dtGrid.setNcColumnsTypeList(hashMap);
            }
        }
        QueryUtils.parseDtGridHql(dtGrid);
        dtGrid = refundDao.getDtGridList(dtGrid, Refund.class);

        List<Object> refunds = dtGrid.getExhibitDatas();
        List<Object> refundAdminLists = new ArrayList<Object>();
        for (int j = 0; j < refunds.size(); j++) {
            Refund refund = (Refund) refunds.get(j);
            RefundItemVo refundItemVo = new RefundItemVo(refund);
            refundAdminLists.add(refundItemVo);
        }
        dtGrid.setExhibitDatas(refundAdminLists);
        return dtGrid;
    }
    /**
     * 平台确认退款
     * @param refund
     * @throws ShopException
     */
    public void     editOrderRefund(Refund refund) throws ShopException {
        //获取订单信息
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ordersId", refund.getOrdersId());
        Orders orders = ordersService.getOrdersInfo(params);

        //获取退款详情页
        RefundDetail refundDetail = refundDetailDao.get(RefundDetail.class, refund.getRefundId());

        //验证
        if (refund.getRefundState() != RefundState.REFUND_STATE_CHECK) {
            throw new ShopException("保存失败");
        }
        //最终退款的金额
        BigDecimal finalAmount = refund.getRefundAmount();
        //在线退款金额
        BigDecimal payAmount = refundDetail.getPayTime() != null ? refundDetail.getPayAmount() : BigDecimal.ZERO;
        // 可退预存款金额,订单金额减去订单已经退款金额
        BigDecimal predepositAmount = PriceHelper.sub(orders.getOrdersAmount(), orders.getRefundAmount());

        //如果可退预存款大于0
        if (PriceHelper.isGreaterThan(predepositAmount, BigDecimal.ZERO)) {
            //判断退款金额是否超出可推金额
            if (PriceHelper.isGreaterThan(finalAmount, predepositAmount)) {
                finalAmount = predepositAmount;
            }
            //如果在线退款了，就减去在线退款金额
            if (PriceHelper.isGreaterThan(payAmount, BigDecimal.ZERO)) {
                finalAmount = PriceHelper.sub(finalAmount, payAmount);
            }
            //增加预存款
            if (PriceHelper.isGreaterThan(finalAmount, BigDecimal.ZERO)) {
                refundDetail.setPdAmount(finalAmount);
                predepositService.addLogForRefund(finalAmount, refund.getMemberId(), String.valueOf(refund.getRefundSn()));
            }


            //修改退款详情
            refundDetail.setRefundState(RefundState.REFUND_DETAIL_FINISH);

            //修改订单中的退款
            orders.setRefundAmount(PriceHelper.add(orders.getRefundAmount(), finalAmount));

            // 订单金额减去运费
            BigDecimal b = PriceHelper.sub(orders.getOrdersAmount(), orders.getFreightAmount());
            BigDecimal a = PriceHelper.sub(b, finalAmount);
            orders.setRefundState(PriceHelper.isGreaterThan(a, BigDecimal.ZERO) ? 1 : 2);

            //订单解锁
            if (orders.getLockState() != 0) {
                orders.setLockState(0);
            }
            //如果商品单退就不取消订单
            if (refund.getGoodsId() == 0) {
                ordersDao.update(orders);
                //取消订单
                adminOrdersService.cancelOrdersForRefund(refund.getOrdersId());
            }

            refundDetailDao.update(refundDetail);
            //修改refund 状态
            refund.setRefundState(RefundState.REFUND_STATE_FINISH);
            refund.setAdminTime(ShopHelper.getCurrentTimestamp());

            refundDao.update(refund);
            HashMap<String, Object> hashMapMsg = new HashMap<>();
            hashMapMsg.put("refundSn", refund.getRefundSn());
            //会员消息
            if (refund.getRefundType() == RefundState.REFUND_TYPE_REFUND) {
                sendMemberMessage(MEMBER_REFUND_MESSAGE_CODE, refund.getMemberId(), hashMapMsg, Long.toString(refund.getRefundId()));
            } else {
                sendMemberMessage(MEMBER_RETURN_MESSAGE_CODE, refund.getMemberId(), hashMapMsg, Long.toString(refund.getRefundId()));
            }

        }

    }
    /**
     * 获取待管理员处理的退款单
     * @return
     */
    public Long getHandleRefundCount() {

        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("refundType=:refundType");
        values.put("refundType", RefundState.REFUND_TYPE_REFUND);
        conditions.add("refundState=:refundState");
        values.put("refundState", RefundState.REFUND_STATE_CHECK);
        return refundDao.getRefundListCount(conditions, values);
    }

    /**
     * 获取待管理员处理的退货单
     *
     * @return
     */
    public Long getHandleReturnCount() {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("refundType=:refundType");
        values.put("refundType", RefundState.REFUND_TYPE_RETURN);
        conditions.add("refundState=:refundState");
        values.put("refundState", RefundState.REFUND_STATE_CHECK);
        return  refundDao.getRefundListCount(conditions, values);

    }
    /**
     *  根据退款详情获取退款金额
     *  @param refundDetail 退款详情
     */
    public BigDecimal getPayRefundAmount(RefundDetail refundDetail) throws ShopException{
        //获取订单 详情
        OrdersVo ordersVo = adminOrdersService.getOrdersVoInfo(refundDetail.getOrdersId());
        //本次退款金额
        BigDecimal refundAmount = refundDetail.getRefundAmount();
        //计算此订单在线支付的金额，在线支付金额=订单总价格-预存款支付金额
        BigDecimal ordersPayAmount = PriceHelper.sub(ordersVo.getOrdersAmount(), ordersVo.getPredepositAmount());
        //计算可在线退款金额
        BigDecimal outAmount = PriceHelper.sub(ordersPayAmount,ordersVo.getRefundAmount());
        //如果本次退款金额大于 在线可退金额就 直接用在线可退金额
        return PriceHelper.isGreaterThan(refundAmount , outAmount) ? outAmount: refundAmount;
    }

}
