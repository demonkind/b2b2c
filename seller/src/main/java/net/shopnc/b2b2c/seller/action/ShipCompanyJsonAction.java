package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.domain.ShipCompany;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.ShipCompanyService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 快递公司
 * Created by hou on 2016/3/14.
 */
@Controller
public class ShipCompanyJsonAction extends BaseJsonAction {
    @Autowired
    private ShipCompanyService shipCompanyService;
    /**
     * 保存店铺快递公司设置
     * @param shipIdList
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "ship/company/list/save",method = RequestMethod.POST)
    public ResultEntity save(@RequestParam(value = "shipId", required = false) List<Integer> shipIdList) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            shipCompanyService.updateStoreShipCompany(SellerSessionHelper.getStoreId(),shipIdList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("保存成功");
        }catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setMessage("保存失败");
        }
        return resultEntity;
    }

    /**
     * 异步读取店铺设置的物流公司列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "ship/company/list/json",method = RequestMethod.GET)
    public ResultEntity listJson() {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
            List<ShipCompany> shipCompanyList = shipCompanyService.getStoreShipCompanyList(SellerSessionHelper.getStoreId());
            resultEntity.setData(shipCompanyList);
            resultEntity.setCode(ResultEntity.SUCCESS);
        }catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setMessage("读取失败");
        }
        return resultEntity;
    }
}
