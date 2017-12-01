package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.goods.CategoryDao;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.vo.CrumbsVo;
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
public class HomeCategoryJsonAction extends HomeBaseJsonAction {
    @Autowired
    CategoryDao categoryDao;

    /**
     * 分类三级联动接口
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "category/list.json/{parentId}", method = RequestMethod.GET)
    public ResultEntity listJson(@PathVariable int parentId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            List<Category> categoryList = categoryDao.findByParentId(parentId);
            map.put("categoryList", categoryList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(map);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

}
