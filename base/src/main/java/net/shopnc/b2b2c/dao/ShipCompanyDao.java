package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.ShipCompany;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 快递公司
 * Created by shopnc on 2015/10/22.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class ShipCompanyDao extends BaseDaoHibernate4<ShipCompany> {
    /**
     * 取得平台常用快递公司
     * @return
     */
    public List<ShipCompany> getShipCompanyActivityList() {
        String hql = "from ShipCompany where shipType = :shipType and shipState = :shipState";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("shipType", State.YES);
        hashMap.put("shipState", State.YES);
        return super.find(hql,hashMap);
    }

    /**
     * 跟据快递公司编码取得快递公司信息
     * @param shipCode
     * @return
     */
    public ShipCompany getShipCompanyInfoByShipCode(String shipCode) {
        if (shipCode == null) {
            return null;
        }
        String hql = "from ShipCompany where shipCode = :shipCode and shipState = :shipState";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("shipCode", shipCode);
        hashMap.put("shipState", State.YES);
        List<ShipCompany> shipCompanyList = super.find(hql, hashMap);
        return shipCompanyList.size()>0 ? shipCompanyList.get(0) : null;
    }

    /**
     * 获取快递公司信息
     * @param shipId
     * @return
     */
    public ShipCompany getShipCompany(int shipId){
        String hql = "from ShipCompany where shipId = :shipId and shipState = :shipState";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("shipId", shipId);
        hashMap.put("shipState", State.YES);
        List<ShipCompany> shipCompanyList = super.find(hql, hashMap);
        return shipCompanyList.size()>0 ? shipCompanyList.get(0) : null;
    }
    /**
     * 取得平台所有开户状态的快递公司列表，常用的排前面
     * @return
     */
    public List<ShipCompany> getShipCompanyOnlineList() {
        String hql = "from ShipCompany where shipState = :shipState order by shipType desc";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("shipState", State.YES);
        return super.find(hql,hashMap);
    }

    /**
     * 取得快递公司列表
     * @param shipIdList
     * @return
     */
    public List<ShipCompany> getShipCompanyOnlineListByShipId(List<Integer> shipIdList) {
        String hql = "from ShipCompany where shipId in (:shipIdList) order by shipType desc";
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("shipIdList",shipIdList);
        return super.find(hql,hashMap);
    }
}
