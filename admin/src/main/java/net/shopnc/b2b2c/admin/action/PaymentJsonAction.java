package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.PaymentService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class PaymentJsonAction extends BaseJsonAction {
    @Autowired
    private PaymentService paymentService;
    /**
     * 支付方式列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "payment/list.json", method = RequestMethod.POST)
    public DtGrid pointslogJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = paymentService.getPaymentDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
    /**
     * 编辑支付方式
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "payment/edit", method = RequestMethod.POST)
    public ResultEntity saveEdit(@RequestParam HashMap<String,String> params) {
        ResultEntity resultEntity = new ResultEntity();
        try{
            paymentService.editPayment(params);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("编辑成功");
            return resultEntity;
        }catch (ShopException e){
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("编辑失败");
            return resultEntity;
        }
    }
}