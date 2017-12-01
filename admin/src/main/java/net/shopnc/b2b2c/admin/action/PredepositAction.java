package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.PaymentDao;
import net.shopnc.b2b2c.dao.member.PredepositRechargeDao;
import net.shopnc.b2b2c.service.member.PredepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 预存款管理
 * Created by zxy on 2015-12-25.
 */
@Controller
public class PredepositAction extends BaseAction {
    @Autowired
    PredepositService predepositService;
    @Autowired
    PredepositRechargeDao predepositRechargeDao;
    @Autowired
    PaymentDao paymentDao;
    /**
     * 预存款明细
     * @return
     */
    @RequestMapping(value = "predeposit/log", method = RequestMethod.GET)
    public String logList() {
        return getAdminTemplate("member/predeposit/log");
    }
    /**
     * 充值列表
     * @return
     */
    @RequestMapping(value = "predeposit/recharge", method = RequestMethod.GET)
    public String rechargeList() {
        return getAdminTemplate("member/predeposit/recharge");
    }
    /**
     * 提现列表
     * @return
     */
    @RequestMapping(value = "predeposit/cash", method = RequestMethod.GET)
    public String cashList() {
        return getAdminTemplate("member/predeposit/cash");
    }
}
