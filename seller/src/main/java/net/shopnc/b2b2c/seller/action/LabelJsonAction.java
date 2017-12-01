package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.dao.store.StoreLabelDao;
import net.shopnc.b2b2c.domain.store.StoreLabel;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.StoreLabelService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 店内商品分类
 * Created by shopnc.feng on 2015-12-01.
 */
@Controller
public class LabelJsonAction extends BaseJsonAction {
    @Autowired
    StoreLabelDao storeLabelDao;
    @Autowired
    StoreLabelService storeLabelService;

    /**
     * 保存店内分类
     * @param storeLabel
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "label/save", method = RequestMethod.POST)
    public ResultEntity save(StoreLabel storeLabel,
                             BindingResult bindingResult) {

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.LABEL_LIST);
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
        }

        storeLabel.setStoreId(SellerSessionHelper.getStoreId());
        storeLabelDao.save(storeLabel);

        resultEntity.setMessage("添加成功");
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑店内分类
     * @param storeLabel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "label/edit/save", method = RequestMethod.POST)
    public ResultEntity edit(StoreLabel storeLabel) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.LABEL_LIST);

        try {
            storeLabelService.edit(storeLabel, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }

        resultEntity.setMessage("编辑成功");
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 删除店内分类
     *
     * @param storeLabelId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "label/delete", method = RequestMethod.POST)
    public ResultEntity delete(int storeLabelId) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(UrlSeller.LABEL_LIST);

        try {
            storeLabelService.deleteStoreLabel(storeLabelId, SellerSessionHelper.getStoreId());
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
}
