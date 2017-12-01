package net.shopnc.b2b2c.admin.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.shopnc.b2b2c.service.EvaluateService;

/**
 * 评价action
 * @author sjz
 *
 */
@Controller
@RequestMapping("/evaluate")
public class EvaluateAction extends BaseAction {
    @Autowired
    private EvaluateService evaluateService;

    @RequestMapping(value="",method=RequestMethod.GET)
    public String initEvaluate(ModelMap model) {
        return getAdminTemplate("evaluate/evaluate");
    }
    
    @RequestMapping(value="store",method=RequestMethod.GET)
    public String initEvaluateStore(ModelMap model) {
        return getAdminTemplate("evaluate/evaluate_store");
    }
    
}