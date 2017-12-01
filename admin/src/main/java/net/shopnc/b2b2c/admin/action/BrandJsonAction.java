package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.UrlAdmin;
import net.shopnc.b2b2c.dao.goods.BrandApplyDao;
import net.shopnc.b2b2c.dao.goods.BrandDao;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.b2b2c.domain.goods.BrandApply;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.goods.BrandService;
import net.shopnc.b2b2c.vo.BrandAndLabelVo;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * 品牌管理
 * Created by shopnc.feng on 2015-11-02.
 */
@Controller
public class BrandJsonAction extends BaseJsonAction {
    @Autowired
    BrandDao brandDao;
    @Autowired
    BrandService brandService;
    @Autowired
    BrandApplyDao brandApplyDao;

    /**
     * 品牌列表JSON数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "brand/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        try {
            return brandService.getBrandDtGridListForAdmin(dtGridPager);
        } catch (Exception e) {
            logger.error(e.toString());
            return new DtGrid();
        }
    }

    /**
     * 审核列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/verify/list.json", method = RequestMethod.POST)
    public DtGrid verifyListJson(String dtGridPager) {
        try {
            return brandService.getBrandDtGridVerifyListForAdmin(dtGridPager);
        } catch (Exception e) {
            return new DtGrid();
        }
    }

    /**
     * 新增品牌保存
     * @param brand
     * @param bindingResult
     * @param brandLabelIds
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @ResponseBody
    @RequestMapping(value = "brand/save.json", method = RequestMethod.POST)
    public ResultEntity save(@Valid Brand brand,
                             BindingResult bindingResult,
                             @RequestParam(value = "brandLabelId", required = false) int[] brandLabelIds
    ) {

        // 定义返回结果对象
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.BRAND_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
            return resultEntity;
        }

        // 保存品牌和品牌标签关系
        brandService.save(brand, brandLabelIds);

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("操作成功");
        return resultEntity;
    }

    /**
     * 更新品牌
     * @param brand
     * @param bindingResult
     * @param brandLabelIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/update.json", method = RequestMethod.POST)
    public ResultEntity update(@Valid Brand brand,
                               BindingResult bindingResult,
                               @RequestParam(value = "brandLabelId", required = false) int[] brandLabelIds
    ){
        // 定义返回结果对象
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.BRAND_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
            return resultEntity;
        }

        // 更新品牌和品牌标签关系
        brandService.update(brand, brandLabelIds);

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("操作成功");
        return resultEntity;
    }

    /**
     * 品牌删除
     * @param brandId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(@RequestParam(value = "brandId") int brandId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.BRAND_LIST);
        if (brandId > 0) {
            brandService.deleteByBrandId(brandId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } else {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 推荐品牌
     * @param brandId
     * @param isRecommend
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/is_recommend/update.json", method = RequestMethod.POST)
    public ResultEntity isRecommendUpdate(@RequestParam(value = "brandId") int brandId,
                                        @RequestParam(value = "isRecommend") int isRecommend) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setUrl(UrlAdmin.BRAND_LIST);

        Brand brand = brandDao.get(Brand.class, brandId);
        brand.setIsRecommend(isRecommend);
        brandDao.update(brand);

        return resultEntity;
    }

    /**
     * 审核品牌
     * @param brandId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/verify.json", method = RequestMethod.POST)
    public ResultEntity verify(int brandId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            BrandAndLabelVo brandAndLabel = brandService.getBrandAndLabelForBrandId(brandId);
            BrandApply brandApply = brandApplyDao.get(BrandApply.class, brandId);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("brandAndLabel", brandAndLabel);
            map.put("brandApply", brandApply);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(map);
        } catch (ShopException e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }


    /**
     * 更新品牌
     * @param brand
     * @param bindingResult
     * @param brandLabelIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/verify/save.json", method = RequestMethod.POST)
    public ResultEntity verifySave(@Valid Brand brand,
                               BindingResult bindingResult,
                               @RequestParam(value = "brandLabelId", required = false) int[] brandLabelIds,
                               @RequestParam(value = "stateRemark", required = false, defaultValue = "") String stateRemark
    ){
        // 定义返回结果对象
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlAdmin.BRAND_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("审核失败");
            return resultEntity;
        }

        // 更新品牌和品牌标签关系
        try {
            brandService.verify(brand, brandLabelIds, stateRemark);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("审核成功");
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("审核失败");
        }
        return resultEntity;
    }
}
