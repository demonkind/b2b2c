package net.shopnc.b2b2c.wap.action.member;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.constant.RefundState;
import net.shopnc.b2b2c.dao.refund.RefundDetailDao;
import net.shopnc.b2b2c.dao.refund.RefundReasonDao;
import net.shopnc.b2b2c.domain.refund.RefundDetail;
import net.shopnc.b2b2c.domain.refund.RefundReason;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.service.refund.MemberRefundService;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.b2b2c.vo.orders.OrdersVo;
import net.shopnc.b2b2c.vo.refund.RefundDetailVo;
import net.shopnc.b2b2c.vo.refund.RefundItemVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.util.PriceHelper;

/**
 * Created by cj on 2016/2/2.
 */
@Controller
@RequestMapping("/member/refund")
public class MemberRefundAction extends MemberBaseAction {
    @Autowired
    private MemberRefundService memberRefundService;

    @Autowired
    private RefundReasonDao refundReasonDao;
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private MemberOrdersService memberOrdersService;

    @Autowired
    private RefundDetailDao refundDetailDao;
    private static int PAGESIZE = 20;

    /**
     * 退款退货选择页面
     * @param ordersId
     * @param orderGoodsId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/choose", method = RequestMethod.GET)
    public String chooseRefund(@RequestParam(value = "ordersId")int ordersId, @RequestParam(value = "orderGoodsId")int orderGoodsId, ModelMap modelMap) {
        if (ordersId <= 0 || orderGoodsId <= 0) {
            return "redirect:/member/orders/list";
        }
        //订单详情
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        params.put("ordersId", ordersId);
        OrdersVo ordersVo = memberRefundService.getOrdersVoInfo(params, orderGoodsId);
        modelMap.put("ordersVo", ordersVo);

        //商品详情
        OrdersGoodsVo ordersGoodsVo = new OrdersGoodsVo();
        for (int i = 0; i < ordersVo.getOrdersGoodsVoList().size(); i++) {
            OrdersGoodsVo _ordersGoodsVo = (OrdersGoodsVo) ordersVo.getOrdersGoodsVoList().get(i);
            if (_ordersGoodsVo.getOrdersGoodsId() == orderGoodsId) {
                ordersGoodsVo = _ordersGoodsVo;
            }
        }
        /*验证是否可以单品退款*/
        //是否有退款记录
        if (memberRefundService.getRefundWaitingCount(ordersId, orderGoodsId, SessionEntity.getMemberId()) > 0) {
            return "redirect:/member/refund/list";
        }
        //是否
        if (!memberRefundService.allowRefundByOrders(ordersVo)) {
            return "redirect:/member/refund/list";
        }
        //修正商品成交价格（可退金额）
        ordersGoodsVo.setGoodsPayAmount(memberRefundService.memberReviseGoodsPayAmount(ordersVo.getOrdersAmount(), ordersGoodsVo.getGoodsPayAmount(), ordersVo.getRefundAmount()));
        modelMap.put("ordersGoodsVo", ordersGoodsVo);

        return getMemberTemplate("refund/choose");
    }
    /**
     * 退款列表
     * @param page
     * @param searchType
     * @param addTimeStart
     * @param addTimeEnd
     * @param keyword
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value = "searchType", required = false) String searchType,
                       @RequestParam(value = "addTimeStart", required = false) Timestamp addTimeStart,
                       @RequestParam(value = "addTimeEnd", required = false) Timestamp addTimeEnd,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       ModelMap modelMap) {


        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        if (searchType != null && !searchType.equals("")) {
            params.put("searchType", searchType);
        }
        if (addTimeStart != null) {
            params.put("addTimeStart", addTimeStart);
        }
        if (addTimeEnd != null) {
            params.put("addTimeEnd", addTimeEnd);
        }
        if (keyword != null && !keyword.equals("")) {
            params.put("keyword", keyword);
        }
        params.put("refundType", RefundState.REFUND_TYPE_REFUND);
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(memberRefundService.getRefundListCount(params));
        List<RefundItemVo> refundItemVoList = memberRefundService.getRefundVoList(params, page, PAGESIZE);
        modelMap.put("refundItemVoList", refundItemVoList);

        modelMap.put("showPage", pageEntity.getPageHtml());
        modelMap.put("searchType", searchType);
        modelMap.put("addTimeStart", addTimeStart);
        modelMap.put("addTimeEnd", addTimeEnd);
        modelMap.put("keyword", keyword);
        //menuKey
        modelMap.put("menuKey", "refund");
        return getMemberTemplate("member_refund");
    }

    /**
     * 退款详情
     * @param refundId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(@RequestParam(value = "refundId") int refundId, ModelMap modelMap) {
        if (refundId <= 0) {
            return "redirect:/member/refund/list";
        }
        RefundItemVo refundItemVo = memberRefundService.getRefundInfo(refundId);
        modelMap.put("refundItemVo", refundItemVo);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        params.put("ordersId", refundItemVo.getOrdersId());
        OrdersVo ordersVo = memberRefundService.getOrdersVoInfo(params, refundItemVo.getOrdersGoodsId());
        modelMap.put("ordersVo", ordersVo);
        //menuKey
        modelMap.put("menuKey", "refund");

        RefundDetail refundDetail = refundDetailDao.get(RefundDetail.class, refundItemVo.getRefundId());
        RefundDetailVo refundDetailVo = refundDetail != null ? new RefundDetailVo(refundDetail) : null;
        modelMap.put("refundDetailVo", refundDetailVo);
        return getMemberTemplate("member_refund_info");
    }

    /**
     * 整单退款申请页面
     *
     * @param ordersId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/addall", method = RequestMethod.GET)
    public String addAll(@RequestParam(value = "ordersId")int ordersId, ModelMap modelMap) {
    	try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("memberId", SessionEntity.getMemberId());
            params.put("ordersId", ordersId);
            OrdersVo ordersVo = memberRefundService.getOrdersVoInfo(params, 0);
            modelMap.put("ordersVo", ordersVo);
            //判断0元订单
            if (PriceHelper.isLessThanOrEquals(ordersVo.getOrdersAmount(), BigDecimal.ZERO)) {
                throw new ShopException("退款申请失败[0元订单不能退款]");
            }
            if (memberRefundService.allowRefundAll(ordersVo.getOrdersId(), ordersVo.getOrdersState(), ordersVo.getPaymentTypeCode()) == false) {
                throw new ShopException("退款申请失败[参数错误]");
            }
        } catch (ShopException e) {
            modelMap.put("errorMessage", e.getMessage());
            return "redirect:/member/refund/list";
        }
        modelMap.put("menuKey", "refund");
        return getMemberTemplate("refund_all");
    }

    /**
     * 商品单件退款退货
     *
     * @param ordersId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/addgoods", method = RequestMethod.GET)
    public String addGoods(@RequestParam(value = "ordersId") int ordersId, @RequestParam(value = "orderGoodsId")  int orderGoodsId, ModelMap modelMap) {

        if (ordersId <= 0 || orderGoodsId <= 0) {
            return "redirect:/member/orders/list";
        }
        //订单详情
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        params.put("ordersId", ordersId);
        OrdersVo ordersVo = memberRefundService.getOrdersVoInfo(params, orderGoodsId);
        modelMap.put("ordersVo", ordersVo);

        //商品详情
        OrdersGoodsVo ordersGoodsVo = new OrdersGoodsVo();
        for (int i = 0; i < ordersVo.getOrdersGoodsVoList().size(); i++) {
            OrdersGoodsVo _ordersGoodsVo = (OrdersGoodsVo) ordersVo.getOrdersGoodsVoList().get(i);
            if (_ordersGoodsVo.getOrdersGoodsId() == orderGoodsId) {
                ordersGoodsVo = _ordersGoodsVo;
            }
        }
        /*验证是否可以单品退款*/
        //是否有退款记录
        if (memberRefundService.getRefundWaitingCount(ordersId, orderGoodsId, SessionEntity.getMemberId()) > 0) {
            return "redirect:/member/refund/list";
        }
        //是否
        if (!memberRefundService.allowRefundByOrders(ordersVo)) {
            return "redirect:/member/refund/list";
        }
        //修正商品成交价格（可退金额）
        ordersGoodsVo.setGoodsPayAmount(memberRefundService.memberReviseGoodsPayAmount(ordersVo.getOrdersAmount(), ordersGoodsVo.getGoodsPayAmount(), ordersVo.getRefundAmount()));
        modelMap.put("ordersGoodsVo", ordersGoodsVo);
        //退款退货原因
        List<RefundReason> refundReasonList = refundReasonDao.findAll(RefundReason.class);
        modelMap.put("refundReasonList", refundReasonList);
        //menuKey
        modelMap.put("menuKey", "refund");
        return getMemberTemplate("refund");
    }
    
    
}
