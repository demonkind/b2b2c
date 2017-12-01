package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by shopnc.feng on 2015-11-30.
 */
@Controller
public class GoodsJsonAction extends BaseJsonAction {
    @Autowired
    SiteService siteService;
    @Autowired
    GoodsService goodsService;

    /**
     * 商品列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        try {
            return goodsService.getGoodsDtGridListForAdmin(dtGridPager);
        } catch (Exception e) {
            return new DtGrid();
        }
    }

    /**
     * 商品删除
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/delete.json", method = RequestMethod.POST)
    public ResultEntity deleteJson(int commonId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            goodsService.deleteByCommonId(commonId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 商品禁售
     * @param commonId
     * @param stateRemark
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/ban.json", method = RequestMethod.POST)
    public ResultEntity banJson(int commonId,
                            String stateRemark) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            goodsService.ban(commonId, stateRemark);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 通过审核
     * @param commonId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/pass.json", method = RequestMethod.POST)
    public ResultEntity passJson(int commonId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            goodsService.pass(commonId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 审核失败
     * @param commonId
     * @param verifyRemark
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/fail.json", method = RequestMethod.POST)
    public ResultEntity failJson(int commonId,
                             String verifyRemark) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            goodsService.fail(commonId, verifyRemark);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (ShopException e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }

    /**
     * 商品设置保存
     * @param goodsVerify
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/site/update.json", method = RequestMethod.POST)
    public ResultEntity siteUpdateJson(@RequestParam(value = "goodsVerify", required = false, defaultValue = "0") String goodsVerify) {
        HashMap<String, String> siteList = new HashMap<String, String>();
        ResultEntity resultEntity = new ResultEntity();
        try {
            siteList.put(SiteTitle.GOODSVERIFY, goodsVerify);
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("数据库保存失败");
        }

        return resultEntity;
    }

}
