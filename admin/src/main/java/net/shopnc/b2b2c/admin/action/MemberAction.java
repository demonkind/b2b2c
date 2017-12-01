package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.service.member.MemberService;
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

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2015-11-25.
 */
@Controller
public class MemberAction extends BaseAction {
    /**
     * 会员列表页
     * @return
     */
    @RequestMapping(value = "member/list", method = RequestMethod.GET)
    public String list() {
        return getAdminTemplate("member/list");
    }
}
