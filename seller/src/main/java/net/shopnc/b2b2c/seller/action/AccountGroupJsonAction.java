package net.shopnc.b2b2c.seller.action;

import com.fasterxml.jackson.core.type.TypeReference;
import net.shopnc.b2b2c.constant.UrlSeller;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.store.SellerGroupService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商家账号组管理
 */
@Controller
public class AccountGroupJsonAction extends BaseJsonAction {

    @Autowired
    private SellerGroupService sellerGroupService;

    /**
     * 商家账号组保存
     * @param groupId
     * @param groupName
     * @param groupPermission
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "account_group/save.json", method = RequestMethod.POST)
    public ResultEntity saveJson(@RequestParam(defaultValue = "0") int groupId,
                                 @RequestParam String groupName,
                                 @RequestParam String groupPermission) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            List<String> menuIdList = JsonHelper.toGenericObject(groupPermission, new TypeReference<List<String>>() {
            });
            sellerGroupService.saveSellerGroup(groupId, groupName, SellerSessionHelper.getStoreId(), menuIdList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(UrlSeller.ACCOUNT_GROUP_LIST);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

    /**
     * 商家账号组删除
     * @param groupId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "account_group/del.json", method = RequestMethod.POST)
    public ResultEntity delJson(@RequestParam int groupId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            sellerGroupService.delSellerGroup(groupId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setUrl(UrlSeller.ACCOUNT_GROUP_LIST);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }

}