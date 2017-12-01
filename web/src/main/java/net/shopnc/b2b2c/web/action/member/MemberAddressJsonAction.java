package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.AddressService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by zxy on 2016-03-14.
 */
@Controller
public class MemberAddressJsonAction extends MemberBaseJsonAction {
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private AddressService addressService;

    /**
     * 新增保存收货地址
     * @param address
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "address/add", method = RequestMethod.POST)
    public ResultEntity add(@Valid Address address,BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                address.setMemberId(SessionEntity.getMemberId());
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
        }
        return resultEntity;
    }
    /**
     * 编辑收货地址
     * @param addressId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "address/edit/{addressId}", method = RequestMethod.GET)
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
    @RequestMapping(value = "address/save", method = RequestMethod.POST)
    public ResultEntity save(@Valid Address address, BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        } else {
            try {
                address.setMemberId(SessionEntity.getMemberId());
                addressService.editAddress(address);
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("保存成功");
            } catch (ShopException e) {
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage(e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("保存失败");
            }
        }
        return resultEntity;
    }
    /**
     * 删除收货地址
     * @param addressId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "address/del", method = RequestMethod.POST)
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
