package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.refund.RefundReasonDao;
import net.shopnc.b2b2c.domain.refund.RefundReason;
import net.shopnc.b2b2c.service.refund.RefundReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 退款退货原因放到
 * Created by cj on 2016/2/1.
 */
@Controller
public class RefundReasonAction extends BaseAction {
    @Autowired
    RefundReasonService refundReasonService;
    @Autowired
    RefundReasonDao refundReasonDao;

    /**
     * 退款退货原因列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "refund/reason/list", method = RequestMethod.GET)
    public String list(ModelMap model) {
        List<RefundReason> refundReasons = refundReasonDao.findAll(RefundReason.class);
        model.put("refundReasons", refundReasons);

        String path = getAdminTemplate("refund/reason/list");
        return path;
    }
}
