package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.store.StoreMessageSettingDao;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.StoreMessageSetting;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.StoreMessageService;
import net.shopnc.common.entity.ResultEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * 商家消息
 * Created by shopnc.feng on 2016-02-04.
 */
@Controller
public class MessageJsonAction extends BaseJsonAction {
    @Autowired
    StoreMessageService storeMessageService;
    @Autowired
    StoreMessageSettingDao storeMessageSettingDao;

    /**
     * 删除消息记录
     * @param messageId
     * @return
     */
    @RequiresPermissions("message/list/1")
    @ResponseBody
    @RequestMapping(value = "message/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(Integer[] messageId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            storeMessageService.deleteMessage(messageId, SellerSessionHelper.getSellerId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("删除失败");
        }
        return resultEntity;
    }

    @RequiresPermissions("message/list/1")
    @ResponseBody
    @RequestMapping(value = "message/mark_read.json", method = RequestMethod.POST)
    public ResultEntity markRead(Integer[] messageId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            storeMessageService.markReadBySellerId(SellerSessionHelper.getSellerId(), messageId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }

        return resultEntity;
    }

    /**
     * 消息设置保存
     * @param sellerId
     * @return
     */
    @RequiresPermissions("message/list/1")
    @ResponseBody
    @RequestMapping(value = "message/setting/save.json", method = RequestMethod.POST)
    public ResultEntity settingSave(
            String tplCode,
            int isReceive,
            @RequestParam(name = "sellerId", required = false) Integer[] sellerId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            storeMessageService.saveSetting(tplCode, isReceive, sellerId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("设置成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("设置失败");
        }
        return resultEntity;
    }

    /**
     * 查询商家账户
     * @param tplCode
     * @return
     */
    @RequiresPermissions("message/list/1")
    @ResponseBody
    @RequestMapping(value = "message/get/seller/list.json", method = RequestMethod.POST)
    public ResultEntity getSellerList(String tplCode) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            resultEntity.setCode(ResultEntity.SUCCESS);
            HashMap<String, Object> map = new HashMap<>();

            List<Seller> sellerList = storeMessageService.getSellerList(tplCode, SellerSessionHelper.getStoreId());
            map.put("sellerList", sellerList);

            StoreMessageSetting storeMessageSetting = storeMessageSettingDao.getByTplCodeAndStoreId(tplCode, SellerSessionHelper.getStoreId());
            map.put("isReceive", (storeMessageSetting == null || storeMessageSetting.getIsReceive() == 1) ? 1 : 0);

            resultEntity.setData(map);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
        }
        return resultEntity;
    }
}
