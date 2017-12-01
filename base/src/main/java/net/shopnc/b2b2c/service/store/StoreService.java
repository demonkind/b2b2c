package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.constant.StoreState;
import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.dao.store.StoreClassDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.domain.store.StoreClass;
import net.shopnc.b2b2c.domain.store.StoreGrade;
import net.shopnc.b2b2c.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;

/**
 * Created by dqw on 2015/12/29.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class StoreService extends BaseService {

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private StoreGradeDao storeGradeDao;

    @Autowired
    private StoreClassDao storeClassDao;

    @Autowired
    private GoodsCommonDao goodsCommonDao;

    /**
     * 后台编辑店铺
     * @param storeId
     * @param storeName
     * @param gradeId
     * @param classId
     * @param storeEndTime
     * @param state
     * @param billCycle
     * @param billCycleType
     */
    public void adminUpdateStore(int storeId, String storeName, int gradeId, int classId, Timestamp storeEndTime, int state, int billCycle, int billCycleType) {
        Store store = storeDao.get(Store.class, storeId);
        StoreGrade storeGrade = storeGradeDao.get(StoreGrade.class, gradeId);
        StoreClass storeClass = storeClassDao.get(StoreClass.class, classId);

        store.setStoreName(storeName);
        store.setGradeId(gradeId);
        store.setGradeName(storeGrade.getName());
        store.setClassId(classId);
        store.setClassName(storeClass.getName());
        store.setStoreEndTime(storeEndTime);
        store.setState(state);

        // by hbj 2016/02/16
        store.setBillCycle(billCycle);
        store.setBillCycleType(billCycleType);

        storeDao.update(store);

        //如果店铺被关闭下架该店铺所有商品
        if(state == StoreState.CLOSE) {
           goodsCommonDao.editOfflineByStoreId(storeId);
        }

    }

    /**
     * 商家保存店铺设置
     * @param storeId
     * @param storeSetting
     */
    public void updateStoreSetting(int storeId, Store storeSetting) {
        Store store = storeDao.get(Store.class, storeId);
        store.setStoreZy(storeSetting.getStoreZy());
        store.setStoreLogo(storeSetting.getStoreLogo());
        store.setStoreBanner(storeSetting.getStoreBanner());
        store.setStoreAvatar(storeSetting.getStoreAvatar());
        store.setStoreQq(storeSetting.getStoreQq());
        store.setStoreWw(storeSetting.getStoreWw());
        store.setStoreWeixin(storeSetting.getStoreWeixin());
        store.setStorePhone(storeSetting.getStorePhone());
        store.setStoreSeoKeywords(storeSetting.getStoreSeoKeywords());
        store.setStoreSeoDescription(storeSetting.getStoreSeoDescription());
        store.setCompanyName(storeSetting.getCompanyName());
        store.setStorePhone(storeSetting.getStorePhone());
        store.setCompanyArea(storeSetting.getCompanyArea());
        storeDao.update(store);
    }
}
