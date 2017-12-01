package net.shopnc.b2b2c.service.member;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.InvoiceInvoiceType;
import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.domain.orders.Invoice;
import net.shopnc.b2b2c.exception.ShopException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hbj on 2015/12/28.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    /**
     * 增加发票
     * @param invoice
     * @return
     * @throws ShopException
     */
    public Invoice addInvoice(Invoice invoice) throws ShopException {
        long count = invoiceDao.getInvoiceCount(invoice.getMemberId());
        if (count >= Common.INVOICE_MAX_NUM) {
            throw new ShopException(String.format("最多允许添加%d条发票信息",Common.INVOICE_MAX_NUM));
        }
        if (invoice.getInvoiceType() != InvoiceInvoiceType.NORMAL) {
            //增值税发票
            if (invoice.getCompany().equals("") || invoice.getCodeSn().equals("") || invoice.getRegisterAddress().equals("")) {
                throw new ShopException("增值税发票内容错误");
            }
        }
        int invoiceId = (Integer) invoiceDao.save(invoice);
        if (invoiceId <= 0) {
            throw new ShopException("保存失败");
        }
        invoice.setInvoiceId(invoiceId);
        return invoice;
    }

    /**
     * 取得保存订单所用的发票格式
     * @param invoice
     * @return
     */
    public String getOrderFormatInvoiceInfo(Invoice invoice) {
        String string = "";
        List<String> list = new ArrayList<String>();
        if (invoice != null) {
            if (invoice.getInvoiceType() == InvoiceInvoiceType.NORMAL) {
                list.add("类型:普通发票");
                list.add("抬头:" + invoice.getTitle().replace(","," "));
                list.add("内容:" + invoice.getContent().replace(",", " "));
            } else {
                list.add("类型:增值税发票");
                list.add("单位名称:" + invoice.getCompany().replace(",", " "));
                list.add("纳税人识别号:" + invoice.getCodeSn().replace(",", " "));
                list.add("注册地址:" + invoice.getRegisterAddress().replace(",", " "));
                list.add("注册电话:" + invoice.getRegisterPhone().replace(",", " "));
                list.add("开户银行:" + invoice.getBankName().replace(",", " "));
                list.add("银行账户:" + invoice.getBankAccount().replace(",", " "));
                list.add("收票人姓名:" + invoice.getReceiverName().replace(",", " "));
                list.add("收票人手机号:" + invoice.getRegisterPhone().replace(",", " "));
                list.add("收票人省份:" + invoice.getReceiverArea().replace(",", " "));
                list.add("送票地址:" + invoice.getReceiverAddress().replace(",", " "));
            }
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            StringBuffer invoiceString = new StringBuffer();
            for(int i=0; i<list.size(); i++){
                if (i == 0) {
                    invoiceString.append(list.get(i));
                } else {
                    invoiceString.append(",").append(list.get(i));
                }
            }
            string = invoiceString.toString();
        }
        return string;
    }
}
