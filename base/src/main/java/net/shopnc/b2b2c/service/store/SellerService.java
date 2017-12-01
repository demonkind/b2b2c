package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.dao.store.SellerDao;
import net.shopnc.b2b2c.dao.store.SellerGroupDao;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.SellerGroup;
import net.shopnc.b2b2c.domain.store.SellerMenu;
import net.shopnc.b2b2c.exception.SellerPasswordErrorException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by dqw on 2015/11/25.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SellerService extends BaseService {

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private SellerGroupDao sellerGroupDao;

    @Autowired
    private SellerMenuService sellerMenuService;

    /**
     * 添加卖家
     * @param seller
     */
    public void addSeller(Seller seller) {
        sellerDao.save(seller);
    }

    /**
     * 添加商家子帐号
     * @param sellerName
     * @param password
     * @param storeId
     * @param storeName
     * @param sellerEmail
     * @param sellerMobile
     * @param groupId
     * @throws Exception
     */
    public void addSubSeller(String sellerName, String password, int storeId, String storeName,String sellerEmail,String sellerMobile, int groupId) throws Exception {
        SellerGroup sellerGroup = sellerGroupDao.get(SellerGroup.class, groupId);
        if(sellerGroup == null) {
            throw new Exception("账号组不存在");
        }
        Seller seller = new Seller();
        seller.setSellerName(sellerName);
        seller.setSellerPassword(ShopHelper.getMd5(password));
        seller.setStoreId(storeId);
        seller.setStoreName(storeName);
        seller.setSellerEmail(sellerEmail);
        seller.setSellerMobile(sellerMobile);
        seller.setGroupId(groupId);
        seller.setGroupName(sellerGroup.getGroupName());
        seller.setJoinDate(ShopHelper.getCurrentTimestamp());
        seller.setLastLoginTime(ShopHelper.getCurrentTimestamp());
        sellerDao.save(seller);
    }

    /**
     * 添加商家子帐号
     * @param sellerId
     * @param password
     * @param storeId
     * @param sellerEmail
     * @param sellerMobile
     * @param groupId
     * @throws Exception
     */
    public void updateSubSeller(int sellerId, String password, int storeId,String sellerEmail,String sellerMobile, int groupId) throws Exception {
        SellerGroup sellerGroup = sellerGroupDao.get(SellerGroup.class, groupId);
        if(sellerGroup == null) {
            throw new Exception("账号组不存在");
        }

        Seller seller = sellerDao.get(Seller.class, sellerId);
        if(seller == null || seller.getStoreId() != storeId) {
            throw new Exception("账号不存在");
        }

        if(!password.equals("")) {
            seller.setSellerPassword(ShopHelper.getMd5(password));
        }

        seller.setSellerEmail(sellerEmail);
        seller.setSellerMobile(sellerMobile);
        seller.setGroupId(groupId);
        seller.setGroupName(sellerGroup.getGroupName());

        sellerDao.update(seller);
    }

    /**
     * 添加商家帐号
     * @param sellerId
     * @param storeId
     * @param sellerEmail
     * @param sellerMobile
     * @throws Exception
     */
    public void updateSeller(int sellerId, int storeId, String sellerEmail,String sellerMobile) throws Exception {
        Seller seller = sellerDao.get(Seller.class, sellerId);
        if(seller == null || seller.getStoreId() != storeId) {
            throw new Exception("账号不存在");
        }

        seller.setSellerEmail(sellerEmail);
        seller.setSellerMobile(sellerMobile);

        sellerDao.update(seller);
    }

    /**
     * 添加商家子帐号
     * @param sellerId
     * @param storeId
     * @throws Exception
     */
    public void delSubSeller(int sellerId, int storeId) throws Exception {
        Seller seller = sellerDao.get(Seller.class, sellerId);
        if(seller == null || seller.getStoreId() != storeId) {
            throw new Exception("账号不存在");
        }

        sellerDao.delete(seller);
    }

    /**
     * 保存商家
     * @param seller
     */
    public void updateSeller(Seller seller) {
        sellerDao.update(seller);
    }

     /**
     * 根据编号查找商家
     * @param sellerId 商家编号
     * @return
     */
    public Seller findSellerById(int sellerId) {
        return sellerDao.get(Seller.class, sellerId);
    }

    /**
     * 根据用户名查找用户
     * @param sellerName 商家用户名
     * @return
     */
    public Seller findSellerByName(String sellerName) {
        return sellerDao.findSellerByName(sellerName);
    }

    /**
     * 判断邮箱是否已经被注册
     * @param sellerEmail 商家邮箱
     * @return
     */
    public Boolean isSellerEmailExist(String sellerEmail) {
        Seller seller = sellerDao.findSellerByEmail(sellerEmail);
        if(seller == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据商家用户名密码查找用户
     * @param sellerName 商家用户名
     * @param sellerPassword 商家密码
     * @return
     */
    public Seller findSellerByNameAndPassword(String sellerName, String sellerPassword) {
        return sellerDao.findSellerByNameAndPassword(sellerName, sellerPassword);
    }

    /**
     * 根据商家用户名查找用户组
     * @param sellerName 商家用户名
     * @return
     */
    public SellerGroup findSellerGroupByName(String sellerName) {
        Seller seller = sellerDao.findSellerByName(sellerName);
        return sellerGroupDao.get(SellerGroup.class, seller.getGroupId());
    }

    /**
     * 获取商家会员列表表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getMemberDtGridList(String dtGridPager) throws Exception {
        return sellerDao.getDtGridList(dtGridPager, Seller.class);
    }

    /**
     * 修改商家密码
     * @param sellerName
     * @param oldPassword
     * @param newPassword
     */
    public void updateProfile(String sellerName, String oldPassword, String newPassword, String avatar) throws SellerPasswordErrorException {
        Seller seller = sellerDao.findSellerByName(sellerName);

        if(!newPassword.equals("")) {
            if (!seller.getSellerPassword().equals(ShopHelper.getMd5(oldPassword))) {
                throw new SellerPasswordErrorException();
            }
            seller.setSellerPassword(ShopHelper.getMd5(newPassword));
        }
        if(!avatar.equals("")) {
            seller.setAvatar(avatar);
        }
        sellerDao.update(seller);
    }

    /**
     * 根据店铺编号查找商家列表
     *
     * @param storeId
     * @return
     */
    public List<Seller> findSellerListByStoreId(int storeId) {
        return sellerDao.findSellerListByStoreId(storeId);
    }

    /**
     * 获取管理员有权限的列表
     */
    public Map<String, List<SellerMenu>> findSellerMenuPermission(int sellerId) {
        Seller seller = sellerDao.get(Seller.class, sellerId);

        Set<String> permissionList = sellerMenuService.findPremissionsByGroupId(seller.getGroupId());

        Map<String, List<SellerMenu>> menuList = sellerMenuService.getSellerMenu();

        Map<String, List<SellerMenu>> newMenuList = new LinkedHashMap<String, List<SellerMenu>>();


        for (String key : menuList.keySet()) {
            List<SellerMenu> subMenu = new LinkedList<SellerMenu>();
            for (SellerMenu sellerMenu : menuList.get(key)) {
                if (permissionList.contains(sellerMenu.getUrl())) {
                    subMenu.add(sellerMenu);
                }
            }
            if (subMenu.size() > 0) {
                newMenuList.put(key,subMenu);
            }
        }

        return newMenuList;
    }

    /**
     * 查询需要发送消息的商家账户
     * @param tplCode
     * @param storeId
     * @return
     */
    public List<Seller> findSellerForMessage(String tplCode, int storeId) {
        Seller seller = sellerDao.getSellerOwnerByStoreId(storeId);
        if (seller == null) {
            return new ArrayList<>();
        }
        List<Seller> sellerList = sellerDao.findSellerByTplCodeAndStoreId(tplCode, storeId);
        List<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        if (sellerList != null) {
            sellers.addAll(sellerList);
        }
        return sellers;
    }
}
