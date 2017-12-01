package net.shopnc.b2b2c.service.refund;

import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cj on 2016/2/1.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SellerRefundService extends RefundService {

    @Autowired
    private MemberDao memberDao;
    /**
     * 商家退款处理
     */
    public void saveSellerRefundHandle(int refundId, int sellerState, String sellerMessage, int storeId) throws ShopException {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("refundId = :refundId");
        values.put("refundId", refundId);
        conditions.add("storeId = :storeId");
        values.put("storeId", storeId);
        Refund refund = refundDao.getRefundByParams(conditions, values);
        if (refund == null || refund.getSellerState() != RefundState.SELLER_STATE_WAITING) {
            throw new ShopException("没有此条信息");
        }
        refund.setSellerTime(ShopHelper.getCurrentTimestamp());
        refund.setSellerState(sellerState);
        refund.setSellerMessage(sellerMessage);
        if (sellerState == RefundState.SELLER_STATE_DISAGREE) {
            refund.setRefundState(RefundState.REFUND_STATE_FINISH);
            //订单解锁
            if (refund.getOrderLock() == RefundState.ORDER_LOCK){
                lockAndUnlockOrdersById(refund.getOrdersId(), ORDERS_UNLOCK);
            }
        } else {
            refund.setRefundState(RefundState.REFUND_STATE_CHECK);
        }
        refundDao.update(refund);
        //发送卖家信息
        sendMemberMessage(MEMBER_REFUND_MESSAGE_CODE, refund.getMemberId(), new HashMap<>(), Long.toString(refund.getRefundId()));

    }

    /**
     * 商家退货处理
     *
     * @param refundId
     * @param sellerState
     * @param sellerMessage
     * @param returnType
     * @param storeId
     */
    public void saveSellerReturnHandle(int refundId, int sellerState, String sellerMessage, int returnType, int storeId) throws ShopException {
        List<Object> conditions = new ArrayList<Object>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        conditions.add("refundId = :refundId");
        values.put("refundId", refundId);
        conditions.add("storeId = :storeId");
        values.put("storeId", storeId);
        Refund refund = refundDao.getRefundByParams(conditions, values);
        if (refund == null || refund.getSellerState() != RefundState.SELLER_STATE_WAITING) {
            throw new ShopException("没有此条信息");
        }
        refund.setSellerTime(ShopHelper.getCurrentTimestamp());
        refund.setSellerState(sellerState);
        refund.setSellerMessage(sellerMessage);

        if (sellerState == RefundState.SELLER_STATE_AGREE && returnType != 1) {
            refund.setReturnType(2);
        } else if (sellerState == RefundState.SELLER_STATE_DISAGREE) {
            refund.setRefundState(RefundState.REFUND_STATE_FINISH);
        } else {
            refund.setSellerState(RefundState.SELLER_STATE_AGREE);
            refund.setRefundState(RefundState.REFUND_STATE_CHECK);
        }
        refundDao.update(refund);
        //订单解锁
        if (sellerState == RefundState.SELLER_STATE_DISAGREE && refund.getOrderLock() == RefundState.ORDER_LOCK) {
            lockAndUnlockOrdersById(refund.getOrdersId(), ORDERS_UNLOCK);
        }
        sendMessageService.sendMember(MEMBER_REFUND_MESSAGE_CODE, refund.getMemberId(), new HashMap<>(), Long.toString(refund.getRefundId()));
    }

    /**
     * 收货
     *
     * @param refundId
     * @param storeId
     */
    public void saveSellerReceive(int refundId, int storeId, int goodsState) throws ShopException {
        Refund refund = getRefundByIds(refundId, storeId);
        if (refund == null) {
            throw new ShopException("收货失败[参数错误]");
        }
        //获取几个条件
        RefundItemVo refundItemVo = new RefundItemVo(refund);
        if (refundItemVo.getSellerState() != RefundState.SELLER_STATE_AGREE || refundItemVo.getGoodsState() != RefundState.RETURN_SHIP_STATE_SEND) {
            throw new ShopException("收货失败[退货单状态错误]");
        }
        if (goodsState == RefundState.RETURN_SHIP_STATE_UNRECEIVED && refundItemVo.getShowStoreReturnReceive() == 1) {
            refund.setGoodsState(RefundState.RETURN_SHIP_STATE_UNRECEIVED);
        } else {
            refund.setReceiveTime(ShopHelper.getCurrentTimestamp());
            refund.setReceiveMessage(RefundState.RECEIVE_MESSAGE);
            refund.setRefundState(RefundState.REFUND_STATE_CHECK);
            refund.setGoodsState(RefundState.RETURN_SHIP_STATE_FINISH);
        }
        sendMessageService.sendMember(MEMBER_RETURN_MESSAGE_CODE, refund.getMemberId(), new HashMap<>(), Long.toString(refund.getRefundId()));
    }


    /**
     * 获取卖家同意的退款单
     * 用于显示退款的价格
     * @param refundItemVoList
     * @return
     * refundItemVo.getRefundState() == RefundState.REFUND_STATE_FINISH && refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE
     */
    public RefundItemVo getSellerAgreeRefund(List<RefundItemVo> refundItemVoList){
        if (refundItemVoList.size() == 0)
        {
            return null;
        }
        for (RefundItemVo refundItemVo:refundItemVoList){
            if (refundItemVo.getRefundState() == RefundState.REFUND_STATE_FINISH && refundItemVo.getSellerState() == RefundState.SELLER_STATE_AGREE){
                return refundItemVo;
            }
        }
        return null;
    }

    /**
     * 获取等待商家审核的退款单
     * @param refundItemVoList
     * @return
     */
    public RefundItemVo filterSellerWaitingRefundVo(List<RefundItemVo> refundItemVoList)
    {
        if (refundItemVoList.size() != 0 ){
            for (RefundItemVo refundItemVo:refundItemVoList){
                if ( refundItemVo.getRefundState() == RefundState.SELLER_STATE_WAITING){
                    return refundItemVo;
                }
            }
        }
        return null;
    }
    /**
     * 获取正在处理的退款单
     * 商家同意，且退款单未完成
     * @return
     */
    public RefundItemVo getSellerDisagreeRefund(List<RefundItemVo> refundItemVoList){
        if (refundItemVoList.size() == 0 ){
            return null;
        }
        for (RefundItemVo refundItemVo:refundItemVoList){
            if ( refundItemVo.getRefundState() < RefundState.REFUND_STATE_FINISH){
                return refundItemVo;
            }
        }
        return null;
    }

    /**
     * 根据goodsid 筛选退款单
     * @param refundItemVos
     * @param goodsId
     * @return
     */
    public List<RefundItemVo> filterRefundItemVoByGoodsId(List<RefundItemVo> refundItemVos, int goodsId){
        List<RefundItemVo> result = new ArrayList<RefundItemVo>();
        for (RefundItemVo refundItemVo:refundItemVos){
            if ( refundItemVo.getGoodsId()  ==  goodsId){
                result.add(refundItemVo);
            }
        }
        return result;
    }

    /**
     * 筛选未完成的退款单
     * @param refundItemVos
     * @return
     */
    public RefundItemVo filterUnfinishedRefundItemVo(List<RefundItemVo> refundItemVos){
        if (refundItemVos.size() != 0 )
        {
            for (RefundItemVo refundItemVo:refundItemVos){
                if ( refundItemVo.getRefundState()  !=   RefundState.REFUND_STATE_FINISH){
                    return refundItemVo;
                }
            }
        }
        return null;
    }
    /**
     * 获取会员信息
     * 退款退货的右侧会员详情中会调用
     * @param memberId
     * @return
     */
    public Member getMemberInfo(int memberId){
        HashMap<String,String> where = new HashMap<String,String>();
        HashMap<String, Object> values = new HashMap<String, Object>();
        where.put("memberId", "memberId = :memberId");
        values.put("memberId", memberId);
        return  memberDao.getMemberInfo(where, values);
    }
}
