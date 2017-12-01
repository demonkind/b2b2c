package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.service.store.SellerService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商家管理
 * Created by dqw on 2015-12-01.
 */
@Controller
public class SellerJsonAction extends BaseJsonAction {

    @Autowired
    private SellerService sellerService;

    /**
     * 会员列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "seller/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = sellerService.getMemberDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 编辑商家会员
     * @param sellerId
     * @param sellerPassword
     * @param sellerPassword2
     * @param sellerEmail
     * @param allowLogin
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "seller/edit", method = RequestMethod.POST)
    public ResultEntity edit(@RequestParam("sellerId") int sellerId,
                             @RequestParam(value = "sellerPassword", required = false) String sellerPassword,
                             @RequestParam(value = "sellerPassword2", required = false) String sellerPassword2,
                             @RequestParam("sellerEmail") String sellerEmail,
                             @RequestParam("allowLogin") int allowLogin) {

        ResultEntity resultEntity = new ResultEntity();

        try {
            Seller seller = sellerService.findSellerById(sellerId);
            seller.setSellerPassword(ShopHelper.getMd5(sellerPassword));
            seller.setSellerEmail(sellerEmail);
            seller.setAllowLogin(allowLogin);
            sellerService.updateSeller(seller);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("保存失败");
        }

        return resultEntity;
    }

}
