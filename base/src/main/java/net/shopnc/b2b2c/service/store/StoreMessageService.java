package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.constant.MessageTemplateCommonTplClass;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.MessageTemplateCommonDao;
import net.shopnc.b2b2c.dao.store.StoreMessageDao;
import net.shopnc.b2b2c.dao.store.StoreMessageSellerDao;
import net.shopnc.b2b2c.dao.store.StoreMessageSettingDao;
import net.shopnc.b2b2c.domain.MessageTemplateCommon;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.StoreMessage;
import net.shopnc.b2b2c.domain.store.StoreMessageSeller;
import net.shopnc.b2b2c.domain.store.StoreMessageSetting;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.message.MessageClassVo;
import net.shopnc.common.entity.PageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2016-02-14.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class StoreMessageService extends BaseService {
    @Autowired
    StoreMessageDao storeMessageDao;
    @Autowired
    StoreMessageSettingDao storeMessageSettingDao;
    @Autowired
    StoreMessageSellerDao storeMessageSellerDao;
    @Autowired
    MessageTemplateCommonDao messageTemplateCommonDao;
    @Autowired
    SellerService sellerService;

    /**
     * 商家消息列表
     * @param sellerId
     * @param tplClass
     * @param page
     * @return
     */
    public HashMap<String, Object> findList(int sellerId, int tplClass, int page) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageNo(page);
        List<StoreMessage> storeMessageList;
        if (tplClass == 1) {
            pageEntity.setTotal(storeMessageDao.findCountBySellerId(sellerId));
            storeMessageList = storeMessageDao.findBySellerId(sellerId, pageEntity.getPageNo(), pageEntity.getPageSize());
        } else {
            pageEntity.setTotal(storeMessageDao.findCountBySellerId(sellerId, tplClass));
            storeMessageList = storeMessageDao.findBySellerId(sellerId, tplClass, pageEntity.getPageNo(), pageEntity.getPageSize());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("storeMessageList", storeMessageList);
        map.put("showPage", pageEntity.getPageHtml());
        return map;
    }

    /**
     * 删除消息
     * @param messageId
     * @param sellerId
     * @throws Exception
     */
    public void deleteMessage(Integer[] messageId, int sellerId) throws Exception {
        if (messageId.length <= 0) {
            return;
        }
        for (int i = 0; i<messageId.length; i++) {
            StoreMessage storeMessage = storeMessageDao.get(StoreMessage.class, messageId[i]);
            if (storeMessage != null && storeMessage.getSellerId() != sellerId) {
                throw new Exception("参数错误");
            }
            storeMessageDao.delete(storeMessage);
        }
    }

    /**
     * 根据商户账号标记已读
     * @param sellerId
     */
    public void markReadBySellerId(int sellerId, Integer[] messageId) {
        storeMessageDao.updateIsReadBySellerId(State.YES, sellerId, messageId);
    }

    /**
     * 保存店铺消息接收设置
     * @param tplCode
     * @param isReceive
     * @param sellerIds
     * @param storeId
     */
    public void saveSetting(String tplCode,
                     int isReceive,
                     Integer[] sellerIds,
                     int storeId) {
        // 删除原有是否接收设置
        storeMessageSettingDao.deleteByTplCodeAndStoreId(tplCode, storeId);
        // 删除原有接收消息子账户数据
        storeMessageSellerDao.deleteByTplCodeAndStoreId(tplCode, storeId);

        // 插入接收记录
        StoreMessageSetting storeMessageSetting = new StoreMessageSetting();
        storeMessageSetting.setTplCode(tplCode);
        storeMessageSetting.setIsReceive(isReceive);
        storeMessageSetting.setStoreId(storeId);
        storeMessageSettingDao.save(storeMessageSetting);

        // 如果设置不接收直接返回
        if (sellerIds == null || sellerIds.length == 0 || isReceive == State.NO) {
            return;
        }

        for (int i = 0; i < sellerIds.length; i++) {
            StoreMessageSeller storeMessageSeller = new StoreMessageSeller();
            storeMessageSeller.setSellerId(sellerIds[i]);
            storeMessageSeller.setStoreId(storeId);
            storeMessageSeller.setTplCode(tplCode);
            storeMessageSellerDao.save(storeMessageSeller);
        }
    }

    /**
     * 商家消息模板
     * @param storeId
     * @return
     */
    public List<MessageTemplateCommon> findMessageTemplateSeller(int storeId) {
        List<MessageTemplateCommon> messageTemplateCommonList = messageTemplateCommonDao.findMessageTemplateSeller();
        HashMap<Integer, String> map = new MessageTemplateCommonTplClass().getStoreClassMap();
        for (MessageTemplateCommon messageTemplateCommon : messageTemplateCommonList) {
            StoreMessageSetting storeMessageSetting = storeMessageSettingDao.getByTplCodeAndStoreId(messageTemplateCommon.getTplCode(), storeId);
            if (storeMessageSetting == null || storeMessageSetting.getIsReceive() == State.YES) {
                messageTemplateCommon.setIsReceive(State.YES);
            }
            messageTemplateCommon.setTplClassName(map.get(messageTemplateCommon.getTplClass()));
        }
        return messageTemplateCommonList;
    }

    /**
     * 商家接收消息账号
     * @param tplCode
     * @param storeId
     * @return
     */
    public List<Seller> getSellerList(String tplCode, int storeId) {
        List<Seller> sellerList = sellerService.findSellerListByStoreId(storeId);
        for (Seller seller : sellerList) {
            StoreMessageSeller storeMessageSeller = storeMessageSellerDao.getByTplCodeAndSellerId(tplCode, seller.getSellerId());
            if (storeMessageSeller != null) {
                seller.setIsReceive(State.YES);
            }
        }
        return sellerList;
    }

    /**
     * 查询未读消息列表
     * @param sellerId
     * @param limit
     * @return
     */
    public List<StoreMessage> getUnreadStoreMessageBySellerId(int sellerId, int limit) {
        List<StoreMessage> storeMessageList = storeMessageDao.findUnreadBySellerId(sellerId, 1, limit);
        return storeMessageList;
    }

    /**
     * 查询未读消息数量
     * @param sellerId
     * @return
     */
    public long getUnreadStoreMessageCountBySellerId(int sellerId) {
        return storeMessageDao.findUnreadCountBySellerId(sellerId);
    }
}
