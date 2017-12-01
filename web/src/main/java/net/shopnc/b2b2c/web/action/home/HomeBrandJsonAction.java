package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 品牌
 * Created by shopnc.feng on 2016-01-21.
 */
@Controller
public class HomeBrandJsonAction extends HomeBaseJsonAction {
    @Autowired
    BrandDao brandDao;

    /**
     * 品牌数据
     * @param brandLabelId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand.json", method = RequestMethod.POST)
    public ResultEntity brand(int brandLabelId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            List<Brand> brandList = brandDao.findByBrandLabelId(brandLabelId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(brandList);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
        }
        return resultEntity;
    }
}
