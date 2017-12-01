package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.ShipCompanyDao;
import net.shopnc.b2b2c.domain.ShipCompany;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 快递公司
 * Created by hou on 2016/3/15.
 */
@Controller
public class ShipCompnayJsonAction  extends BaseJsonAction{
    @Autowired
    private ShipCompanyDao shipCompanyDao;
    /**
     * 列表
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "ship_company/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = shipCompanyDao.getDtGridList(dtGridPager, ShipCompany.class);
        } catch (Exception e) {
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 编辑
     * @param shipCompany
     * @param bindingResult
     * @param shipType
     * @param shipState
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "ship_company/edit", method = RequestMethod.POST)
    public ResultEntity save1(@Valid ShipCompany shipCompany,
                              BindingResult bindingResult,
                              String shipType, String shipState) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "ship_company/list");
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                ShipCompany shipCompany1 = shipCompanyDao.get(ShipCompany.class, shipCompany.getShipId());
                if (shipState != null) {
                    shipCompany1.setShipState(Integer.parseInt(shipState));
                }
                if (shipType != null) {
                    shipCompany1.setShipType(Integer.parseInt(shipType));
                }
                shipCompanyDao.update(shipCompany1);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("操作成功");
            } catch (Exception e) {
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("操作失败");
            }
        }
        return resultEntity;
    }
}
