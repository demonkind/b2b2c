package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.goods.BrandApplyDao;
import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.dao.goods.BrandLabelDao;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.b2b2c.domain.goods.BrandApply;
import net.shopnc.b2b2c.domain.goods.BrandLabel;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.BrandService;
import net.shopnc.b2b2c.vo.BrandAndLabelVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2016-01-25.
 */
@Controller
public class BrandAction extends BaseAction {
    @Autowired
    BrandDao brandDao;
    @Autowired
    BrandLabelDao brandLabelDao;
    @Autowired
    BrandService brandService;
    @Autowired
    BrandApplyDao brandApplyDao;

    public BrandAction() {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("brand/list", "品牌申请");
        setSellerTabMenu(tabMenuMap);
        setMenuPath("brand/list");
    }

    /**
     * 品牌申请列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "brand/list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                       ModelMap modelMap) {
        List<BrandLabel> brandLabelList = brandLabelDao.findAll(BrandLabel.class);
        modelMap.put("brandLabelList", brandLabelList);

        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(brandDao.findCountByStoreId(SellerSessionHelper.getStoreId()));
        pageEntity.setPageNo(page);

        List<Brand> brandList = brandDao.findByStoreId(SellerSessionHelper.getStoreId(), pageEntity.getPageNo(), pageEntity.getPageSize());
        modelMap.put("brandList", brandList);
        modelMap.put("showPage", pageEntity.getPageHtml());
        return getSellerTemplate("brand/list");
    }
}
