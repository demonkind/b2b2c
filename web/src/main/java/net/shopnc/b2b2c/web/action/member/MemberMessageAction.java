package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.constant.MessageTemplateCommonTplClass;
import net.shopnc.b2b2c.service.SendMessageService;
import net.shopnc.b2b2c.service.member.MemberMessageService;
import net.shopnc.b2b2c.vo.message.MessageClassVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 会员消息
 * Created by shopnc.feng on 2016-02-18.
 */
@Controller
public class MemberMessageAction extends MemberBaseAction {
    @Autowired
    MemberMessageService memberMessageService;
    @Autowired
    SendMessageService sendMessageService;

    @ModelAttribute("messageClassVoList")
    private List<MessageClassVo> messageClassList() {
        return new MessageTemplateCommonTplClass().getMemberMessageClassVoList();
    }

    /**
     * 消息列表页面
     * @param tplClass
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "message/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "tplClass", required = false, defaultValue = "1") int tplClass,
                       @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                       ModelMap modelMap) {

        HashMap<String, Object> map = memberMessageService.findList(SessionEntity.getMemberId(), tplClass, page);

        modelMap.addAllAttributes(map);
        modelMap.put("tplClass", tplClass);
        //menuKey
        modelMap.put("menuKey", "message");
        return getMemberTemplate("message/list");
    }

    /**
     * 消息接收设置页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "message/setting", method = RequestMethod.GET)
    public String setting(ModelMap modelMap) {


        HashMap<String, Object> map = new HashMap<>();
        sendMessageService.sendMember("memberRefundUpdate", 3, map, "ssssssss");



        List<MessageClassVo> messageClassList = memberMessageService.findMessageTemplateMember(SessionEntity.getMemberId());
        modelMap.put("messageClassList", messageClassList);
        //menuKey
        modelMap.put("menuKey", "message");
        return getMemberTemplate("message/setting");
    }
}
