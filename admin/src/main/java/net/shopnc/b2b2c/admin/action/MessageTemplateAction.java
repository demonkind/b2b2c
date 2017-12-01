package net.shopnc.b2b2c.admin.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 消息模板
 * Created by shopnc.feng on 2016-01-29.
 */
@Controller
public class MessageTemplateAction extends BaseAction {
    /**
     * 其他消息
     * @return
     */
    @RequestMapping(value = "message_template/list", method = RequestMethod.GET)
    public String list() {
        return getAdminTemplate("message_template/list");
    }

    /**
     * 会员消息
     * @return
     */
    @RequestMapping(value = "message_template/member/list", method = RequestMethod.GET)
    public String memberList() {
        return getAdminTemplate("message_template/member_list");
    }

    /**
     * 店铺消息
     * @return
     */
    @RequestMapping(value = "message_template/store/list", method = RequestMethod.GET)
    public String storeList() {
        return getAdminTemplate("message_template/store_list");
    }
}
