package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zxy on 2016-03-15.
 */
@Controller
public class CommonJsonAction extends BaseJsonAction {
    @Autowired
    MemberDao memberDao;

    @ResponseBody
    @RequestMapping(value = "common/member/getid", method = RequestMethod.GET)
    public ResultEntity getMemberIdByMemberName(@RequestParam(value = "memberName") String memberName) {
        ResultEntity resultEntity = new ResultEntity();
        Member memberInfo = memberDao.getMemberInfoByMemberName(memberName);
        if (memberInfo == null) {
            resultEntity.setCode(ResultEntity.FAIL);
        }else{
            resultEntity.setData(memberInfo.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        }
        return resultEntity;
    }
}
