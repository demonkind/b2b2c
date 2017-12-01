package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 收货地址
 * Created by hbj on 2015/12/23.
 */
@Controller
public class MemberAddressAction extends MemberBaseAction {
    @Autowired
    private AddressDao addressDao;

    /**
     * 列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "address", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        List<Address> addressList = addressDao.getAddressList(SessionEntity.getMemberId());
        modelMap.put("addressList",addressList);
        modelMap.put("addressMaxNum", Common.ADDRESS_MAX_NUM);
        //menuKey
        modelMap.put("menuKey", "address");
        return getMemberTemplate("address/address_list");
    }
}
