package net.shopnc.b2b2c.wap.action.home;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.goods.CategoryDao;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.service.goods.CategoryService;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.b2b2c.vo.goods.CategoryCommissionVo;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 商品分类
 * Created by shopnc.feng on 2015-11-02.
 */
@Controller
public class HomeCategoryAction extends HomeBaseAction {
    @Autowired
    CategoryDao categoryDao;

    public HomeCategoryAction() {
        List<CrumbsVo> crumbsVoList = new ArrayList<CrumbsVo>();
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName("全部商品分类");
        crumbsVo.setUrl(ShopConfig.getWebRoot() + "category");
        crumbsVoList.add(crumbsVo);
        setCrumbsList(crumbsVoList);
    }

    /**
     * 全部商品分类
     * @return
     */
    @RequestMapping(value = "category", method = RequestMethod.GET)
    public String list() {
        return getHomeTemplate("category");
    }

}
