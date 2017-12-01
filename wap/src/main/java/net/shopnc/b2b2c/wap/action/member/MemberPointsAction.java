package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.service.member.PointsService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberPointsAction extends MemberBaseAction {

    @Autowired
    PointsService pointsService;

    /**
     * 会员积分列表
     * @param addTimeStart
     * @param addTimeEnd
     * @param operationStage
     * @param descriptionLike
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "pointslog", method = RequestMethod.GET)
    public String index(@RequestParam(value = "addTimeStart", required = false) Timestamp addTimeStart,
                        @RequestParam(value = "addTimeEnd", required = false) Timestamp addTimeEnd,
                        @RequestParam(value = "operationStage", required = false) String operationStage,
                        @RequestParam(value = "description", required = false) String descriptionLike,
                        @RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                        ModelMap modelMap) {
        //会员信息
        modelMap.put("member",memberDao.get(Member.class, SessionEntity.getMemberId()));
        //积分日志
        HashMap<String,Object> params = new HashMap<String, Object>();
        if (addTimeStart != null && !addTimeStart.equals("")) {
            params.put("addTimeGt", addTimeStart);
        }
        if (addTimeEnd != null && !addTimeEnd.equals("")) {
            params.put("addTimeLt", addTimeEnd);
        }
        if (operationStage != null && !operationStage.equals("")) {
            params.put("operationStage", operationStage);
        }
        if (descriptionLike != null && !descriptionLike.equals("")) {
            params.put("descriptionLike", "%"+descriptionLike+"%");
        }
        params.put("memberId", SessionEntity.getMemberId());
        HashMap<String,Object> result = pointsService.getPointsLogListByPage(params, page);
        modelMap.put("logList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //menuKey
        modelMap.put("menuKey", "pointslog");
        return getMemberTemplate("points_log");
    }
}