package net.shopnc.b2b2c.wap.action.home;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.dao.goods.BrandLabelDao;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.b2b2c.domain.goods.BrandLabel;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 品牌
 * Created by shopnc.feng on 2016-01-21.
 */
@Controller
public class HomeBrandAction extends HomeBaseAction {
    @Autowired
    BrandDao brandDao;
    @Autowired
    BrandLabelDao brandLabelDao;

    public HomeBrandAction() {
        List<CrumbsVo> crumbsVoList = new ArrayList<CrumbsVo>();
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName("全部品牌");
        crumbsVo.setUrl(ShopConfig.getWebRoot() + "brand");
        crumbsVoList.add(crumbsVo);
        setCrumbsList(crumbsVoList);
    }

    /**
     * 品牌页
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "brand", method = RequestMethod.GET)
    public String brand(ModelMap modelMap) {
        // 48个推荐品牌
        List<Brand> brandList = brandDao.findRecommendForHome();
        modelMap.put("brandList", brandList);
        List<BrandLabel> brandLabelList = brandLabelDao.findAll(BrandLabel.class);
        modelMap.put("brandLabelList", brandLabelList);
        return getHomeTemplate("brand");
    }
}
