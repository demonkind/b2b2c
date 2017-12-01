package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.UploadService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * Created by zxy on 2016-03-14.
 */
@Controller
public class MemberInformationAction extends MemberBaseAction {

    @Autowired
    MemberService memberService;
    @Autowired
    UploadService uploadService;

    /**
     * 会员基本信息
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "information", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member",member);
        //menuKey
        modelMap.put("menuKey", "information");
        return getMemberTemplate("information/base");
    }
    /**
     * 会员更换头像
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "information/avatar", method = RequestMethod.GET)
    public String uploadAvatar(ModelMap modelMap){
        Member member = memberDao.get(Member.class, SessionEntity.getMemberId());
        modelMap.put("member", member);
        //menuKey
        modelMap.put("menuKey", "information");
        return getMemberTemplate("information/avatar");
    }
}