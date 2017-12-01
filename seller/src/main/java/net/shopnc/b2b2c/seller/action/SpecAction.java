package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.goods.SpecDao;
import net.shopnc.b2b2c.dao.goods.SpecValueDao;
import net.shopnc.b2b2c.domain.goods.Spec;
import net.shopnc.b2b2c.domain.goods.SpecValue;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.SpecService;
import net.shopnc.b2b2c.vo.SpecAndValueVo;
import net.shopnc.common.entity.ResultEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 商品规格
 * Created by shopnc.feng on 2015-12-07.
 */
@Controller
public class SpecAction extends BaseAction {
    @Autowired
    SpecService specService;

    public SpecAction() {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("spec/list", "规格列表");
        setSellerTabMenu(tabMenuMap);
        setMenuPath("spec/list");
    }

    /**
     * 商家规格列表
     * @return
     */
    @RequestMapping(value = "spec/list")
    public String list(ModelMap modelMap) {
        List<SpecAndValueVo> specAndValueList = specService.findSpecAndValueVoByStoreId(SellerSessionHelper.getStoreId());
        modelMap.put("specAndValueList", specAndValueList);
        return getSellerTemplate("goods/spec/list");
    }
}
