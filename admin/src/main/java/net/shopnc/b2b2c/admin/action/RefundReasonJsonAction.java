package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.UrlAdmin;
import net.shopnc.b2b2c.dao.refund.RefundReasonDao;
import net.shopnc.b2b2c.domain.refund.RefundReason;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.refund.RefundReasonService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 退款退货原因放到
 * Created by cj on 2016/2/1.
 */
@Controller
public class RefundReasonJsonAction extends BaseJsonAction {
    @Autowired
    RefundReasonService refundReasonService;
    @Autowired
    RefundReasonDao refundReasonDao;

    /**
     * 分类列表JSON数据
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "refund/reason/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        try {
            DtGrid dtGrid = refundReasonService.getRefundReasonDtGridListForAdmin(dtGridPager);
            return dtGrid;
        } catch (Exception e) {
            return new DtGrid();
        }
    }

    /**
     * 添加一个退款理由
     * @param refundReason
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "refund/reason/add", method = RequestMethod.POST)

    public ResultEntity add(
            RefundReason refundReason,
            BindingResult bindingResult
    ) {
        ResultEntity resultEntity = new ResultEntity();
        // 定义返回结果对象
        resultEntity.setUrl(UrlAdmin.REFUND_REASON_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("错误消息");
            return resultEntity;
        }
        refundReason.setUpdateTime(ShopHelper.getCurrentTimestamp());
        refundReasonService.addRefundReason(refundReason);
        resultEntity.setMessage("添加成功");
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 保存退款理由
     *
     * @param refundReason
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "refund/reason/save", method = RequestMethod.POST)
    public ResultEntity save(
            RefundReason refundReason,
            BindingResult bindingResult
    ) {
        ResultEntity resultEntity = new ResultEntity();
        // 定义返回结果对象
        resultEntity.setUrl(UrlAdmin.REFUND_REASON_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("错误消息");
            return resultEntity;
        }
        refundReason.setUpdateTime(ShopHelper.getCurrentTimestamp());
        refundReasonService.saveRefundReason(refundReason);
        resultEntity.setMessage("添加成功");
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
    /**
     * 删除记录
     * @param reasonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "refund/reason/del", method = RequestMethod.POST)
    public ResultEntity cashDel(@RequestParam(value = "reasonId") Integer reasonId) {
        ResultEntity resultEntity = new ResultEntity();
        if (reasonId <= 5) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        try{
            refundReasonService.delRefundReason(reasonId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
            return resultEntity;
        }catch (ShopException e){
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("删除失败");
            return resultEntity;
        }
    }

}
