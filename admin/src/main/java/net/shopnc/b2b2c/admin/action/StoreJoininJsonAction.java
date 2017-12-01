package net.shopnc.b2b2c.admin.action;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.StoreJoininState;
import net.shopnc.b2b2c.dao.goods.StoreBindCategoryDao;
import net.shopnc.b2b2c.dao.store.StoreCertificateDao;
import net.shopnc.b2b2c.dao.store.StoreClassDao;
import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.dao.store.StoreJoininDao;
import net.shopnc.b2b2c.domain.goods.StoreBindCategory;
import net.shopnc.b2b2c.domain.store.StoreCertificate;
import net.shopnc.b2b2c.domain.store.StoreClass;
import net.shopnc.b2b2c.domain.store.StoreGrade;
import net.shopnc.b2b2c.domain.store.StoreJoinin;
import net.shopnc.b2b2c.service.store.StoreJoininService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺入驻管理
 * Created by dqw on 2015-12-21.
 */
@Controller
public class StoreJoininJsonAction extends BaseJsonAction {

    @Autowired
    private StoreJoininDao storeJoininDao;

    @Autowired
    private StoreJoininService storeJoininService;

    /**
     * 商家入驻列表JSON数据
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_joinin/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = storeJoininDao.getDtGridList(dtGridPager, StoreJoinin.class);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 商家入驻申请审批
     *
     * @param sellerId
     * @param payingAmount
     * @param joininMessage
     * @param type
     * @param storeBindCategoryListJson
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_joinin/verify.json", method = RequestMethod.POST)
    public ResultEntity verify(@RequestParam int sellerId,
                               @RequestParam int payingAmount,
                               @RequestParam String joininMessage,
                               @RequestParam String type,
                               @RequestParam String storeBindCategoryListJson) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            List<StoreBindCategory> storeBindCategoryList = JsonHelper.toGenericObject(storeBindCategoryListJson, new TypeReference<List<StoreBindCategory>>() {
            });
            storeJoininService.verify(sellerId, payingAmount, joininMessage, type, storeBindCategoryList);

            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(ShopConfig.getAdminRoot() + "store_joinin/list");
            resultEntity.setMessage("审核成功");
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("审核失败");
        }

        return resultEntity;
    }

    /**
     * 商家入驻申请审批
     *
     * @param sellerId
     * @param joininMessage
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_joinin/verify_pay.json", method = RequestMethod.POST)
    public ResultEntity verifyPay(@RequestParam int sellerId,
                                  @RequestParam String joininMessage,
                                  @RequestParam String type) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            storeJoininService.verifyPay(sellerId, joininMessage, type);

            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(ShopConfig.getAdminRoot() + "store_joinin/list");
            resultEntity.setMessage("审核成功");
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("审核失败");
        }

        return resultEntity;
    }

    /**
     * 删除商家入驻申请
     *
     * @param sellerId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "store_joinin/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(@RequestParam int sellerId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            storeJoininService.delete(sellerId);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("删除失败");
        }
        return resultEntity;
    }
}


