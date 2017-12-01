package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.member.ConsultClassDao;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.member.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by zxy on 2016-01-14
 */
@Controller
public class ConsultAction extends BaseAction {
    private static int PAGESIZE = 20;

    @Autowired
    private ConsultService consultService;
    @Autowired
    private ConsultClassDao consultClassDao;

    public ConsultAction() {
        setMenuPath("consult/list/all");
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("consult/list/all", "全部咨询");
        tabMenuMap.put("consult/list/noreply", "未回复咨询");
        tabMenuMap.put("consult/list/replied", "已回复咨询");
        setSellerTabMenu(tabMenuMap);
    }
    /**
     * 咨询列表
     * @param type
     * @param page
     * @param classId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "consult/list/{type}", method = RequestMethod.GET)
    public String list(@PathVariable String type,
                       @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value = "cid", required = false) Integer classId,
                       ModelMap modelMap) {
        //查询咨询列表
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("storeId", SellerSessionHelper.getStoreId());
        if (classId!=null && classId>0) {
            params.put("classId", classId);
        }
        if (type!=null && type.equals("noreply")) {
            params.put("consultReplyEq", "");
        }else if(type!=null && type.equals("replied")){
            params.put("consultReplyNeq","");
        }
        HashMap<String, Object> result = consultService.getConsultListByPage(params, page, PAGESIZE);
        modelMap.put("consultList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //查询咨询类型
        modelMap.put("consultClassList", consultClassDao.getConsultClassList(""));
        return getSellerTemplate("aftersales/consult_list");
    }
}
