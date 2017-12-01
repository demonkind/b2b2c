package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.ShipCompanyDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.ShipCompany;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 快递公司
 * Created by hbj on 2016/1/13.
 */
@Controller
public class ShipCompanyAction extends BaseAction {
    @Autowired
    private ShipCompanyDao shipCompanyDao;
    @Autowired
    private StoreDao storeDao;

    public ShipCompanyAction() {
        setMenuPath("ship/company/list");
    }

    /**
     * 店铺设置快递公司列表
     * @return
     */
    @RequestMapping(value = "ship/company/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("ship/company/list", "快递公司");
        modelMap.put("sellerTabMenuMap", tabMenuMap);

        //店铺已选择的快递公司
        List<Integer> shipIdList = new ArrayList<Integer>();
        Store store = storeDao.get(Store.class, SellerSessionHelper.getStoreId());
        if (store.getShipCompany() != null && !store.getShipCompany().equals("")) {
            String[] shipArray = store.getShipCompany().split(",");
            for(int i=0; i<shipArray.length; i++) {
                shipIdList.add(Integer.parseInt(shipArray[i]));
            }
        }
        modelMap.put("shipIdList",shipIdList);

        //平台所有快递公司列表
        List<ShipCompany> shipCompanyList = shipCompanyDao.getShipCompanyOnlineList();
        modelMap.put("shipCompanyList", shipCompanyList);
        return getSellerTemplate("orders/ship_company/list");
    }


}
