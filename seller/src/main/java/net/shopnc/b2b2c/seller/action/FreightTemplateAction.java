package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.FreightTemplateCalcType;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.orders.FreightAreaDao;
import net.shopnc.b2b2c.dao.orders.FreightTemplateDao;
import net.shopnc.b2b2c.domain.FreightArea;
import net.shopnc.b2b2c.domain.FreightTemplate;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.FreightTemplateService;
import net.shopnc.common.entity.PageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 运费模板
 * Created by hbj on 2016/1/20.
 */
@Controller
public class FreightTemplateAction extends BaseAction {
    @Autowired
    private FreightTemplateDao freightTemplateDao;
    @Autowired
    private FreightTemplateService freightTemplateService;
    @Autowired
    private FreightAreaDao freightAreaDao;

    public FreightTemplateAction() {
        setMenuPath("freight/template/list");
    }

    /**
     * 运费模板列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "freight/template/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       ModelMap modelMap) {
        subNav(modelMap);
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(freightTemplateDao.getFreightTemplateCountByStoreId(SellerSessionHelper.getStoreId()));
        pageEntity.setPageNo(page);
        List<FreightTemplate> freightTemplateList = freightTemplateDao.getFreightTemplateListByStoreId(SellerSessionHelper.getStoreId(),page, pageEntity.getPageSize());

        //取得详细地区及价格列表
        HashMap<Integer,List<FreightArea>> freightAreaList = freightTemplateService.getFreightAreaListGroupByFreightId(freightTemplateList);
        modelMap.put("freightTemplateList",freightTemplateList);
        modelMap.put("freightAreaList",freightAreaList);
        modelMap.put("showPage", pageEntity.getPageHtml());
        return getSellerTemplate("freight/template/list");
    }

    /**
     * 新增/编辑运费模板
     * @return
     */
    @RequestMapping(value = "freight/template/add", method = RequestMethod.GET)
    public String add(@RequestParam(value = "calcType", required = false) String calcType,
                      @RequestParam(value = "freightId", required = false) Integer freightId, ModelMap modelMap) {

        if (freightId != null) {
            FreightTemplate freightTemplate = freightTemplateDao.getFreightTemplateInfo(freightId, SellerSessionHelper.getStoreId());
            if (freightTemplate == null) {
                return "redirect:/404";
            }
            List<FreightArea> freightAreaList = freightAreaDao.getFreightAreaByFreightId(freightId);
            Pattern pattern = Pattern.compile("^_+|_+$");
            for (int i=0; i<freightAreaList.size(); i++) {
                freightAreaList.get(i).setAreaId(pattern.matcher(freightAreaList.get(i).getAreaId()).replaceAll(""));
            }
            modelMap.put("freightTemplate",freightTemplate);
            modelMap.put("freightAreaList",freightAreaList);
            modelMap.put("calcType",freightTemplate.getCalcType());
            modelMap.put("freightFree",freightTemplate.getFreightFree());

            HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
            tabMenuMap.put("freight/template/list", "物流配送");
            tabMenuMap.put("freight/template/add", "编辑");
            modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);

        } else {
            if (calcType != null) {
                if (!Arrays.asList(FreightTemplateCalcType.NUMBER,FreightTemplateCalcType.VOLUME,FreightTemplateCalcType.WEIGHT).contains(calcType)) {
                    calcType = FreightTemplateCalcType.NUMBER;
                }
            } else {
                calcType = FreightTemplateCalcType.NUMBER;
            }
            modelMap.put("calcType", calcType);
            modelMap.put("freightFree", State.NO);

            HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
            tabMenuMap.put("freight/template/list", "物流配送");
            tabMenuMap.put("freight/template/add", "新增");
            modelMap.addAttribute("sellerTabMenuMap", tabMenuMap);
        }

        HashMap<String,Object> hashMap = freightTemplateService.getAreaList();
        modelMap.put("nameList",hashMap.get("nameList"));
        modelMap.put("childrenList", hashMap.get("childrenList"));
        modelMap.put("regionList", hashMap.get("regionList"));

        return getSellerTemplate("freight/template/add");
    }

    private void subNav(ModelMap modelMap) {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("freight/template/list", "物流配送");
        modelMap.put("sellerTabMenuMap", tabMenuMap);
    }
}
