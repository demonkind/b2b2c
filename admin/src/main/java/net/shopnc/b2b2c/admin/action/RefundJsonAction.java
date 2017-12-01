package net.shopnc.b2b2c.admin.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.refund.RefundDao;
import net.shopnc.b2b2c.domain.refund.Refund;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.orders.MemberOrdersService;
import net.shopnc.b2b2c.service.refund.AdminRefundService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 退款退货原因放到
 * Created by cj on 2016/2/1.
 */
@Controller
public class RefundJsonAction extends BaseJsonAction {
    @Autowired
    AdminRefundService adminRefundService;
    @Autowired
    RefundDao refundDao;
    @Autowired
    MemberOrdersService memberOrdersService;


    /**
     * 分类列表JSON数据
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "refund/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            ObjectMapper mapper = new ObjectMapper();
            dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
            dtGrid = adminRefundService.getRefundDtGridList(dtGrid);
        } catch (Exception e) {
            logger.error("whereHql:" + dtGrid.getWhereHql());
            logger.error("sortHql:" + dtGrid.getSortHql());
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }


    /**
     * 退款审核保存
     */
    @ResponseBody
    @RequestMapping(value = "refund/save", method = RequestMethod.POST)
    public ResultEntity save(@RequestParam(value = "refundId", required = true) int refundId,
                             @RequestParam(value = "adminMessage", required = true) String adminMessage
    ) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            Refund refund = refundDao.get(Refund.class, refundId);
            refund.setAdminMessage(adminMessage);
            adminRefundService.editOrderRefund(refund);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
            resultEntity.setUrl(ShopConfig.getAdminRoot() + "refund/list");
            return resultEntity;
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
            return resultEntity;
        }
    }
}
