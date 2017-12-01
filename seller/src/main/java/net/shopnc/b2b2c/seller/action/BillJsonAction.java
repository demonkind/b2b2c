package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.bill.BillService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hou on 2016/3/14.
 */
@Controller
public class BillJsonAction extends BaseJsonAction {
    @Autowired
    private BillService billService;
    /**
     * 商家确认结算单
     * @param billId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bill/confirm", method = RequestMethod.POST)
    public ResultEntity confirm(@RequestParam(value = "billId", required = true) Integer billId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "bill/list");
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            billService.confirmBill(billId);
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
