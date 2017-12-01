package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.constant.StoreJoininState;
import net.shopnc.b2b2c.constant.StoreState;
import net.shopnc.b2b2c.dao.goods.StoreBindCategoryDao;
import net.shopnc.b2b2c.dao.store.*;
import net.shopnc.b2b2c.domain.goods.StoreBindCategory;
import net.shopnc.b2b2c.domain.store.*;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dqw on 2015/12/23.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class StoreJoininService extends BaseService {

    @Autowired
    private StoreCertificateDao storeCertificateDao;

    @Autowired
    private StoreBindCategoryDao storeBindCategoryDao;

    @Autowired
    private StoreJoininDao storeJoininDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private StoreGradeDao storeGradeDao;

    @Autowired
    private StoreClassDao storeClassDao;

    /**
     * 商家提交入驻申请
     *
     * @param sellerId              商家编号
     * @param sellerName            商家用户名
     * @param storeCertificate      商家资质对象
     * @param storeJoinin           商家入驻对象
     * @param storeBindCategoryList 商家选择的经营类目列表
     */
    public void submit(int sellerId, String sellerName, StoreCertificate storeCertificate, StoreJoinin storeJoinin, List<StoreBindCategory> storeBindCategoryList) {
        //保存店铺资质信息
        storeCertificate.setSellerId(sellerId);
        storeCertificate.setSellerName(sellerName);
        storeCertificateDao.update(storeCertificate);

        //保存开店信息
        storeJoinin.setSellerId(sellerId);
        storeJoinin.setSellerName(sellerName);
        storeJoinin.setState(StoreJoininState.SUBMIT);
        storeJoinin.setJoininSubmitTime(ShopHelper.getCurrentTimestamp());
        storeJoininDao.update(storeJoinin);

        //保存绑定分类
        storeBindCategoryDao.deleteByStoreId(sellerId);
        for (StoreBindCategory storeBindCategory : storeBindCategoryList) {
            storeBindCategory.setStoreId(sellerId);
            storeBindCategoryDao.save(storeBindCategory);
        }
    }

    /**
     * 商家上传付款凭证
     *
     * @param sellerId             商家编号
     * @param payingCertificate
     * @param payingCertificateExp
     */
    public void pay(int sellerId, String payingCertificate, String payingCertificateExp) {
        StoreJoinin storeJoinin = storeJoininDao.get(StoreJoinin.class, sellerId);
        storeJoinin.setPayingCertificate(payingCertificate);
        storeJoinin.setPayingCertificateExp(payingCertificateExp);
        storeJoinin.setState(StoreJoininState.PAYED);
        storeJoininDao.update(storeJoinin);
    }

    /**
     * 后台管理员审核入驻申请
     * @param sellerId
     * @param payingAmount
     * @param joininMessage
     * @param type
     * @param storeBindCategoryList
     */
    public void verify(int sellerId, int payingAmount, String joininMessage, String type, List<StoreBindCategory> storeBindCategoryList) {
        //更新入驻申请信息
        StoreJoinin storeJoinin = storeJoininDao.get(StoreJoinin.class, sellerId);
        storeJoinin.setPayingAmount(payingAmount);
        storeJoinin.setJoininMessage(joininMessage);
        if (type.equals("success")) {
            storeJoinin.setState(StoreJoininState.VERIFY_SUCCESS);
        } else {
            storeJoinin.setState(StoreJoininState.VERIFY_FAIL);
        }
        storeJoininDao.update(storeJoinin);

        //更新分佣比例
        for (StoreBindCategory storeBindCategory : storeBindCategoryList) {
            storeBindCategoryDao.update(storeBindCategory);
        }
    }

    /**
     * 后台管理员审核付款
     * @param sellerId
     * @param joininMessage
     * @param type
     */
    public void verifyPay(int sellerId, String joininMessage, String type) {
        //更新入驻申请信息
        StoreJoinin storeJoinin = storeJoininDao.get(StoreJoinin.class, sellerId);
        storeJoinin.setJoininMessage(joininMessage);
        if (type.equals("success")) {
            //查询店铺等级
            StoreGrade storeGrade = storeGradeDao.get(StoreGrade.class, storeJoinin.getGradeId());

            //查询店铺分类
            StoreClass storeClass = storeClassDao.get(StoreClass.class, storeJoinin.getClassId());

            //开店
            Store store = new Store();
            store.setStoreId(storeJoinin.getSellerId());
            store.setSellerId(storeJoinin.getSellerId());
            store.setSellerName(storeJoinin.getSellerName());
            store.setStoreName(storeJoinin.getStoreName());
            store.setGradeId(storeJoinin.getGradeId());
            store.setGradeName(storeGrade.getName());
            store.setClassId(storeJoinin.getClassId());
            store.setClassName(storeClass.getName());
            store.setStoreCreateTime(ShopHelper.getCurrentTimestamp());
            store.setStoreEndTime(ShopHelper.getFutureTimestamp(Calendar.YEAR, storeJoinin.getJoininYear()));
            store.setState(StoreState.OPEN);
            store.setIsOwnShop(State.NO);
            storeDao.update(store);
            //更新商家信息
            Seller seller = sellerDao.get(Seller.class, sellerId);
            seller.setStoreId(storeJoinin.getSellerId());
            seller.setStoreName(storeJoinin.getStoreName());
            seller.setIsStoreOwner(State.YES);
            seller.setGroupId(0);
            seller.setGroupName("店主");
            //修改注入申请状态
            storeJoinin.setState(StoreJoininState.SUCCESS);
        } else {
            storeJoinin.setState(StoreJoininState.PAY_VERIFY_FAIL);
        }
        storeJoininDao.update(storeJoinin);
    }



    /**
     * 后台管理员删除入驻申请
     * @param sellerId
     * @throws Exception
     */
    public void delete(int sellerId) throws Exception {
        StoreJoinin storeJoinin = storeJoininDao.get(StoreJoinin.class, sellerId);
        if(storeJoinin.getState() == StoreJoininState.SUCCESS) {
            throw new Exception("不能删除已经成功的入驻申请");
        }
        storeJoininDao.delete(StoreJoinin.class, sellerId);
        storeCertificateDao.delete(StoreCertificate.class, sellerId);
        storeBindCategoryDao.deleteByStoreId(sellerId);
    }
}

