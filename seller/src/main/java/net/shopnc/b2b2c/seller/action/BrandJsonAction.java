package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.goods.BrandApplyDao;
import net.shopnc.b2b2c.domain.goods.Brand;
import net.shopnc.b2b2c.domain.goods.BrandApply;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.goods.BrandService;
import net.shopnc.b2b2c.vo.BrandAndLabelVo;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * 商家品牌申请
 * Created by shopnc.feng on 2016-01-25.
 */
@Controller
public class BrandJsonAction extends BaseJsonAction {
    @Autowired
    BrandService brandService;
    @Autowired
    BrandApplyDao brandApplyDao;

    /**
     * 品牌保存
     * @param brand
     * @param bindingResult
     * @param brandLabelIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/save.json", method = RequestMethod.POST)
    public ResultEntity save(@Valid Brand brand,
                             BindingResult bindingResult,
                             @RequestParam(value = "brandLabelId", required = false) int[] brandLabelIds,
                             String registerNumber,
                             String image1,
                             String image2,
                             String owner
    ) {
        ResultEntity resultEntity = new ResultEntity();

        // 定义返回结果对象
        resultEntity.setUrl(UrlSeller.BRAND_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("保存失败");
            return resultEntity;
        }

        try {
            // 保存品牌和品牌标签关系
            Integer brandId = brandService.saveBrandForStore(brand, brandLabelIds, SellerSessionHelper.getSellerId());
            BrandApply brandApply = new BrandApply();
            brandApply.setBrandId(brandId);
            brandApply.setRegisterNumber(registerNumber);
            brandApply.setImage1(image1);
            brandApply.setImage2(image2);
            brandApply.setOwner(owner);
            brandApplyDao.save(brandApply);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("保存成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("保存失败");
        }

        return resultEntity;
    }

    /**
     * 编辑品牌
     * @param brandId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/edit.json", method = RequestMethod.POST)
    public ResultEntity edit(int brandId) {
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
     * 品牌更新
     * @param brand
     * @param bindingResult
     * @param brandLabelIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/update.json", method = RequestMethod.POST)
    public ResultEntity update(@Valid Brand brand,
                               BindingResult bindingResult,
                               @RequestParam(value = "brandLabelId", required = false) int[] brandLabelIds,
                               String registerNumber,
                               String image1,
                               String image2,
                               String owner
    ){
        // 定义返回结果对象
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.BRAND_LIST);
        // 格式验证失败
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("编辑失败");
            return resultEntity;
        }

        try {
            // 更新品牌和品牌标签关系
            brandService.updateBrandForStore(brand, brandLabelIds, SellerSessionHelper.getSellerId());
            BrandApply brandApply = brandApplyDao.get(BrandApply.class, brand.getBrandId());
            brandApply.setRegisterNumber(registerNumber);
            brandApply.setImage1(image1);
            brandApply.setImage2(image2);
            brandApply.setOwner(owner);
            brandApplyDao.update(brandApply);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("编辑成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("编辑失败");
        }
        return resultEntity;
    }


    /**
     * 品牌删除
     * @param brandId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "brand/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(int brandId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.BRAND_LIST);
        try {
            brandService.deleteByBrandIdForStore(brandId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
}
