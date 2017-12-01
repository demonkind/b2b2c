package net.shopnc.b2b2c.service.member;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.exception.ShopException;
import org.hibernate.dialect.Ingres10Dialect;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

/**
 * Created by hbj on 2015/12/24.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AddressService {
    @Autowired
    private AddressDao addressDao;

    /**
     * 添加收货地址
     * @param address
     * @return
     * @throws ShopException
     */
    public Address addAddress(Address address)throws ShopException {
        if (address.getMemberId() <= 0) {
            throw new ShopException("登录账号不正确");
        }
        //判断保存地址是否超过上限
        long addressCount = addressDao.getAddressCount(address.getMemberId());
        if (addressCount >= Common.ADDRESS_MAX_NUM) {
            throw new ShopException(String.format("最多可以添加%d个收货地址", Common.ADDRESS_MAX_NUM));
        }
        //判断是否为默认地址
        address.setIsDefault(address.getIsDefault() == State.YES ? State.YES : State.NO);
        if (address.getIsDefault() == State.YES) {
            addressDao.editAddressIsDefault(State.NO, address.getMemberId());
        }
//    	address.setAddress(Pattern.compile("\"|'").matcher(address.getAddress()).replaceAll(""));
//    	address.setAreaInfo(Pattern.compile("\"|'").matcher(address.getAreaInfo()).replaceAll(""));
//    	address.setRealName(Pattern.compile("\"|'").matcher(address.getRealName()).replaceAll(""));
//        if(!address.getTelPhone().isEmpty()){
//        	address.setTelPhone(Pattern.compile("\"|'").matcher(address.getTelPhone()).replaceAll(""));
//        }
//        if(address.getMobPhone().isEmpty() || "".equals(address.getMobPhone())){
//        	address.setMobPhone(address.getMobPhone());
//        }else{
//        	address.setMobPhone(Pattern.compile("\"|'").matcher(address.getMobPhone()).replaceAll(""));
//        }
        int addressId = (Integer)addressDao.save(address);
        if (addressId > 0) {
            address.setAddressId(addressId);
        } else {
            throw new ShopException("保存失败");
        }

        return address;
    }
    /**
     * 编辑收货地址
     * @param address
     * @throws ShopException
     */
    public void editAddress(Address address) throws ShopException {
        if (address.getMemberId() <= 0) {
            throw new ShopException("会员不存在");
        }
        address.setIsDefault(address.getIsDefault() == State.YES ? State.YES : State.NO);
        address.setAddress(Pattern.compile("\"|'").matcher(address.getAddress()).replaceAll(""));
        address.setAreaInfo(Pattern.compile("\"|'").matcher(address.getAreaInfo()).replaceAll(""));
        address.setRealName(Pattern.compile("\"|'").matcher(address.getRealName()).replaceAll(""));
        if(!"".equals(address.getTelPhone()) && address.getTelPhone() != null){
        	address.setTelPhone(Pattern.compile("\"|'").matcher(address.getTelPhone()).replaceAll(""));
        }
        if(!"".equals(address.getMobPhone().isEmpty())){
        	address.setMobPhone(Pattern.compile("\"|'").matcher(address.getMobPhone()).replaceAll(""));
        }
        if (address.getIsDefault() == State.YES) {
            addressDao.editAddressIsDefault(State.NO, address.getMemberId());
        }
        addressDao.update(address);
    }
}
