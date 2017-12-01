package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.service.member.ConsultService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberConsultAction extends MemberBaseAction {

    @Autowired
    private ConsultService consultService;

    /**
     * 咨询列表
     * @param type
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "consult/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                       ModelMap modelMap) {
        //查询咨询列表
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("memberId", SessionEntity.getMemberId());
        if (type!=null && type.equals("noreply")) {
            params.put("consultReplyEq","");
        }else if(type!=null && type.equals("replied")){
            params.put("consultReplyNeq","");
        }
        HashMap<String, Object> result = consultService.getConsultListByPage(params, page, 10);
        modelMap.put("consultList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //更新已回复未读咨询为已读
        consultService.consultToReadByMemberId(SessionEntity.getMemberId());
        //menuKey
        modelMap.put("menuKey", "consult");
        return getMemberTemplate("consult/consult_list");
    }


}