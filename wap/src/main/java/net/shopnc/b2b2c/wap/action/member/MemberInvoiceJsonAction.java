package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.domain.orders.Invoice;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.InvoiceService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 发票
 * Created by hou on 2016/3/14.
 */
@Controller
@RequestMapping(value = "invoice")
public class MemberInvoiceJsonAction extends MemberBaseJsonAction {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceDao invoiceDao;

    /**
     * 发票列表
     * @return
     * */
    @ResponseBody
    @RequestMapping(value = "list/json" , method = RequestMethod.POST)
    public ResultEntity invoiceListJson(HttpServletRequest request) {
    	String key=request.getParameter("key");
        ResultEntity resultEntity = new ResultEntity();
        List<Invoice> invoiceList = invoiceDao.getInvoiceList(Integer.valueOf(key));
        resultEntity.setData(invoiceList);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 保存新增发票
     * @param invoice
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public ResultEntity add(@Valid Invoice invoice,BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                invoice.setMemberId(SessionEntity.getMemberId()) ;
                Invoice invoice1 = invoiceService.addInvoice(invoice);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setData(invoice1);
            } catch (ShopException e) {
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage(e.getMessage());
            } catch (Exception e) {
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("保存失败");
                logger.error(e.getMessage());
            }
        }
        return resultEntity;
    }

    /**
     * 删除发票
     * @param invoiceId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "del",method = RequestMethod.POST)
    public ResultEntity del(HttpServletRequest request) {
    	String invoiceId=request.getParameter("invoiceId");
    	String key=request.getParameter("key");
        ResultEntity resultEntity = new ResultEntity();
        try {
            invoiceDao.delInvoice(Integer.valueOf(invoiceId),Integer.valueOf(key));
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("删除失败");
        }
        return resultEntity;
    }
}
