package net.shopnc.b2b2c.wap.action.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

/**
 * 收货地址
 * Created by hbj on 2015/12/23.
 */
@Controller
@RequestMapping("address")
public class MemberAddressAction extends MemberBaseAction {
    @Autowired
    private AddressDao addressDao;

    /**
     * 跳转并获取到列表
     * @param modelMap
     * @return
     */
    @RequestMapping("index")
    public String index(ModelMap map) {
    	List<Address> addressList = addressDao.getAddressList(SessionEntity.getMemberId());
    	map.put("address_list", addressList);
    	if(SessionEntity.getIsLogin()){
    		return getMemberTemplate("address_list");
    	}else{
    		return getMemberTemplate("login");
    	}
    }
    /**
     * 跳入新增地址页面
     * @param modelMap
     * @return
     */
    @RequestMapping("addAddress")
    public String addAddress() {
        return getMemberTemplate("address_opera");
    }
    /**
     * 跳入编辑地址页面
     * @param modelMap
     * @return
     */
    @RequestMapping("editAddress")
    public String editAddress(@RequestParam(value = "addressId", required = true) Integer addressId,ModelMap map) {
    	Address anAddress = addressDao.getAddressList(SessionEntity.getMemberId()).get(0);
    	map.put("anAddress", anAddress);
        return getMemberTemplate("address_opera_edit");
    }
    
}
