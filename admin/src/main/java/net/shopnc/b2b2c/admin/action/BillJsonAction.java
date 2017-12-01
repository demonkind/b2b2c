package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.orders.BillDao;
import net.shopnc.b2b2c.domain.orders.Bill;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.bill.BillService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 结算
 * Created by hou on 2016/3/14.
 */
@Controller
public class BillJsonAction extends BaseJsonAction {
    @Autowired
    private BillDao billDao;
    @Autowired
    private BillService billService;

    /**
     * 结算列表异步
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bill/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = billDao.getDtGridList(dtGridPager,Bill.class);
        } catch (Exception e) {
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 平台审核结算单
     * @param billId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bill/access", method = RequestMethod.POST)
    public ResultEntity cancel(@RequestParam(value = "billId", required = true) Integer billId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "bill/list");
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            billService.accessBill(billId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.equals(e.toString());
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 平台支付结算单
     * @param billId
     * @param paymentTime
     * @param paymentNote
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bill/pay", method = RequestMethod.POST)
    public ResultEntity cancel(@RequestParam(value = "billId", required = true) Integer billId,
                               @RequestParam(value = "paymentTime", required = true) String paymentTime,
                               @RequestParam(value = "paymentNote", required = false) String paymentNote) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "bill/list");
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            billService.payBill(billId, paymentTime, paymentNote);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.equals(e.toString());
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

}
