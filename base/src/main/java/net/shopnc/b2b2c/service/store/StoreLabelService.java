package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.dao.store.StoreLabelDao;
import net.shopnc.b2b2c.domain.store.StoreLabel;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-12-02.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class StoreLabelService extends BaseService {
    @Autowired
     StoreLabelDao storeLabelDao;

    /**
     * 店内分类类别
     * @param storeId
     * @return
     */
    public List<StoreLabel> findByStoreId(int storeId) {
        List<String> where = new ArrayList<String>();
        where.add("storeId = :storeId");
        where.add("parentId = :parentId");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("storeId", storeId);
        params.put("parentId", 0);
        List<StoreLabel> storeLabelList = storeLabelDao.findStoreLabel(where, params);
        for (StoreLabel storeLabel : storeLabelList) {
            List<StoreLabel> storeLabelList1 = storeLabelDao.findByParentId(storeLabel.getStoreLabelId());
            storeLabel.setStoreLabelList(storeLabelList1);
        }
        return storeLabelList;
    }

    /**
     * 删除店内分类
     * @param storeLabelId
     * @param storeId
     * @throws ShopException
     */
    public void deleteStoreLabel(int storeLabelId, int storeId) throws ShopException{
        // 验证标签是否使用店铺
        StoreLabel storeLabel = storeLabelDao.get(StoreLabel.class, storeLabelId);
        if (storeLabel.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        // 删除标签
        storeLabelDao.delete(StoreLabel.class, storeLabel.getStoreLabelId());
        // 删除下级标签
        deleteByParentId(storeLabel.getParentId());
    }

    /**
     * 根据上级编号删除店内分类
     * @param parentId
     */
    public void deleteByParentId(int parentId) {
        if (parentId == 0) {
            return;
        }
        List<StoreLabel> storeLabelList = storeLabelDao.findByParentId(parentId);
        storeLabelDao.deleteAll(storeLabelList);
    }

    /**
     * 编辑电脑分类
     * @param storeLabelNew
     * @param storeId
     * @throws ShopException
     */
    public void edit(StoreLabel storeLabelNew, int storeId) throws ShopException{
        StoreLabel storeLabel = storeLabelDao.get(StoreLabel.class, storeLabelNew.getStoreLabelId());
        if (storeLabel.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }

        storeLabel.setStoreLabelSort(storeLabelNew.getStoreLabelSort());
        storeLabel.setStoreLabelName(storeLabelNew.getStoreLabelName());
        storeLabel.setParentId(storeLabelNew.getParentId());
        storeLabel.setIsFold(storeLabelNew.getIsFold());
        storeLabel.setImage(storeLabelNew.getImage());

        storeLabelDao.update(storeLabel);
    }
}
