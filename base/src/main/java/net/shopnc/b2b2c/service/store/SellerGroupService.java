package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.dao.store.SellerGroupDao;
import net.shopnc.b2b2c.dao.store.SellerGroupMenuDao;
import net.shopnc.b2b2c.domain.store.SellerGroup;
import net.shopnc.b2b2c.domain.store.SellerGroupMenu;
import net.shopnc.b2b2c.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dqw on 2015/11/25.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SellerGroupService extends BaseService {

    @Autowired
    private SellerGroupDao sellerGroupDao;

    @Autowired
    private SellerGroupMenuDao sellerGroupMenuDao;

    /**
     * 添加商家组
     * @param groupId
     * @param groupName
     * @param storeId
     */
    public void saveSellerGroup(int groupId, String groupName, int storeId, List<String> menuIdList) throws Exception {
        if(groupId > 0) {
            SellerGroup sellerGroup = sellerGroupDao.get(SellerGroup.class, groupId);
            if(sellerGroup.getStoreId() != storeId) {
                throw new Exception("修改失败");
            }
            sellerGroup.setGroupName(groupName);
            sellerGroupDao.update(sellerGroup);
        } else {
            SellerGroup sellerGroup = new SellerGroup();
            sellerGroup.setGroupName(groupName);
            sellerGroup.setStoreId(storeId);
            groupId = (Integer) sellerGroupDao.save(sellerGroup);
        }

        sellerGroupMenuDao.delByGroupId(groupId);

        List<SellerGroupMenu>  sellerGroupMenuList = new LinkedList<SellerGroupMenu>();
        for(String menuId : menuIdList) {
            SellerGroupMenu sellerGroupMenu = new SellerGroupMenu();
            sellerGroupMenu.setGroupId(groupId);
            sellerGroupMenu.setMenuId(Integer.valueOf(menuId));
            sellerGroupMenuList.add(sellerGroupMenu);
        }
        sellerGroupMenuDao.saveAll(sellerGroupMenuList);
    }

    /**
     * 删除商家组
     * @param groupId
     * @param storeId
     * @throws Exception
     */
    public void delSellerGroup(int groupId, int storeId) throws Exception {
        SellerGroup sellerGroup = sellerGroupDao.get(SellerGroup.class, groupId);
        if(sellerGroup != null && sellerGroup.getStoreId() == storeId) {
            sellerGroupDao.delete(sellerGroup);
        } else {
           throw new Exception("删除失败");
        }

        sellerGroupMenuDao.delByGroupId(groupId);
    }

}
