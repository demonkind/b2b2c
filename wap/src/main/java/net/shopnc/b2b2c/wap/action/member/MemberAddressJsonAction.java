package net.shopnc.b2b2c.wap.action.member;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.AddressService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;

/**
 * Created by zxy on 2016-03-14.
 */
@Controller
@RequestMapping("address")
public class MemberAddressJsonAction extends MemberBaseJsonAction {
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private AddressService addressService;
    
    /**
     * 跳转并获取到列表
     * @param modelMap
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultEntity list(HttpServletRequest request) {
    	ResultEntity resultEntity = new ResultEntity();
    	List<Address> addressList = addressDao.getAddressList(SessionEntity.getMemberId());
    	Integer address_id = 0;
    	for(Address address : addressList){
    		if (address.getIsDefault() == 1){
    			address_id = address.getAddressId();
    			break;
    		}
    	}
    	HashMap<String, Object> resultMap = new HashMap<>();
    	resultMap.put("address_list", addressList);
    	resultMap.put("address_id", address_id.intValue());
    	resultMap.put("login", SessionEntity.getIsLogin());
    	resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setData(resultMap);
        resultEntity.setMessage("获取地址列表成功");
        return resultEntity;
    }
    
    /**
     * 新增保存收货地址
     * @param address
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultEntity add(HttpServletRequest request) {
        ResultEntity resultEntity = new ResultEntity();
        Address address = new Address();
	    
	    address.setMemberId(SessionEntity.getMemberId());
        address.setRealName(request.getParameter("trueName"));
        address.setMobPhone(request.getParameter("mobPhone"));
        address.setAreaId(Integer.parseInt(request.getParameter("areaId")));
        address.setAreaId1(Integer.parseInt(request.getParameter("cityId")));
        address.setAddress(request.getParameter("address"));
        address.setAreaInfo(request.getParameter("areaInfo"));
        address.setIsDefault(Integer.parseInt(request.getParameter("isDefault")));
        
        try {
            Address address1 = addressService.addAddress(address);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(address1);
            resultEntity.setMessage("保存成功");
        } catch (ShopException e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            logger.error(e.getMessage());
            resultEntity.setMessage("保存失败");
        }
        
        return resultEntity;
    }
    /**
     * 编辑收货地址
     * @param addressId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ResultEntity edit(@PathVariable(value = "addressId") Integer addressId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            Address address = addressDao.getAddressInfo(addressId,SessionEntity.getMemberId());
            resultEntity.setData(address);
            resultEntity.setCode(ResultEntity.SUCCESS);
        }catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("数据读取失败");
        }
        return resultEntity;
    }
    /**
     * 编辑保存收货地址
     * @param address
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResultEntity save(HttpServletRequest request) {
    	ResultEntity resultEntity = new ResultEntity();
        Address address = new Address();
	    
	    address.setMemberId(SessionEntity.getMemberId());
        address.setRealName(request.getParameter("true_name"));
        address.setMobPhone(request.getParameter("mob_phone"));
        address.setAreaId1(Integer.parseInt(request.getParameter("city_id")));
        address.setAreaId(Integer.parseInt(request.getParameter("area_id")));
        address.setAddress(request.getParameter("address"));
        address.setAreaInfo(request.getParameter("area_info"));
        address.setIsDefault(Integer.parseInt(request.getParameter("is_default")));
        address.setAddressId(Integer.parseInt(request.getParameter("address_id")));
        try {
            addressService.editAddress(address);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("保存成功");
        } catch (ShopException e) {
        	e.printStackTrace();
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("保存失败");
        }
        
        return resultEntity;
    }
    /**
     * 删除收货地址
     * @param addressId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResultEntity del(@RequestParam(value = "addressId", required = true) Integer addressId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            addressDao.delAddress(addressId, SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("删除失败");
        }
        return resultEntity;
    }
}
