package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.domain.Address;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 收货地址
 * Created by shopnc on 2015/10/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class AddressDao extends BaseDaoHibernate4<Address> {

    /**
     * 收货地址列表
     * @param memberId
     * @return
     */
    public List<Address> getAddressList(int memberId) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        String hql = "from Address where memberId = :memberId order by addressId desc";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId", memberId);
        List<Address> list = super.find(hql, hashMap);
        return list;
    }

    /**
     * 收货地址详细信息
     * @param addressId
     * @param memberId
     * @return
     */
    public Address getAddressInfo(int addressId, int memberId) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        String hql = "from Address where addressId = :addressId and memberId = :memberId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("addressId",addressId);
        hashMap.put("memberId", memberId);
        List<Address> list = super.find(hql, hashMap);
        return list.size()>0 ? list.get(0) : null;
    }

    /**
     * 删除收货地址
     * @param addressId
     * @param memberId
     */
    public void delAddress(int addressId, int memberId) {
        String hql = "delete from Address where addressId = :addressId and memberId = :memberId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("addressId",addressId);
        hashMap.put("memberId", memberId);
        super.delete(hql,hashMap);
    }

    /**
     * 编辑默认收货地址
     * @param isDefault
     * @param memberId
     */
    public void editAddressIsDefault(int isDefault,int memberId) {
        String hql = "update Address set isDefault = :isDefault where memberId = :memberId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("isDefault",isDefault);
        hashMap.put("memberId",memberId);
        super.update(hql, hashMap);
    }

    /**
     * 取得收货地址数量
     * @param memberId
     * @return
     */
    public long getAddressCount(int memberId) {
        String hql = "select count(*) from Address where memberId = :memberId";
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("memberId",memberId);
        return super.findCount(hql,hashMap);
    }
}
