package net.shopnc.b2b2c.service;

import net.shopnc.b2b2c.dao.ShipCompanyDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.ShipCompany;
import net.shopnc.b2b2c.domain.store.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 快递公司
 * Created by hbj on 2016/1/13.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ShipCompanyService {
    @Autowired
    private ShipCompanyDao shipCompanyDao;
    @Autowired
    private StoreDao storeDao;

    /**
     * 取得店铺使用的物流公司列表
     * @param storeId
     * @return
     */
    public List<ShipCompany> getStoreShipCompanyList(int storeId) {
        //如果店铺未设置自己的物流公司，则应用平台设置快递公司

        //店铺已选择的快递公司
        List<Integer> shipIdList = new ArrayList<Integer>();
        Store store = storeDao.get(Store.class, storeId);
        if (store.getShipCompany() != null && !store.getShipCompany().equals("")) {
            String[] shipArray = store.getShipCompany().split(",");
            for(int i=0; i<shipArray.length; i++) {
                shipIdList.add(Integer.parseInt(shipArray[i]));
            }
        }
        if (shipIdList.size()>0) {
            return shipCompanyDao.getShipCompanyOnlineListByShipId(shipIdList);
        } else {
            return shipCompanyDao.getShipCompanyActivityList();
        }
    }

    /**
     * 更新店铺设置的快递公司
     * @param storeId
     * @param shipIdList
     */
    public void updateStoreShipCompany(int storeId, List<Integer> shipIdList) {
        List<Object> setItems = new ArrayList<Object>();
        setItems.add("shipCompany = :shipCompany");
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        if (shipIdList != null && shipIdList.size() > 0) {
            StringBuffer shipString = new StringBuffer();
            for(int i=0; i<shipIdList.size(); i++){
                if (i == 0) {
                    shipString.append(shipIdList.get(i));
                } else {
                    shipString.append(",").append(shipIdList.get(i));
                }
            }
            hashMap.put("shipCompany", shipString.toString());
        } else {
            hashMap.put("shipCompany", "");
        }
        storeDao.editStore(setItems,hashMap,storeId);

    }

}
