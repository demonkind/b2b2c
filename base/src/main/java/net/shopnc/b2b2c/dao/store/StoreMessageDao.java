package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.constant.MessageTemplateCommonTplClass;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.store.StoreMessage;
import net.shopnc.b2b2c.vo.message.MessageClassVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2016-02-05.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreMessageDao extends BaseDaoHibernate4<StoreMessage> {
    /**
     * 根据卖家查询消息列表
     * @param sellerId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<StoreMessage> findBySellerId(int sellerId, int pageNo, int pageSize) {
        String hql = "from StoreMessage where sellerId = :sellerId order by isRead asc,messageId desc";
        HashMap<String, Object> map = new HashMap<>();
        map.put("sellerId", sellerId);
        return findByPage(hql, pageNo, pageSize, map);
    }
    /**
     * 根据卖家查询消息列表
     * @param sellerId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<StoreMessage> findBySellerId(int sellerId, int tplClass, int pageNo, int pageSize) {
        String hql = "from StoreMessage where sellerId = :sellerId and tplClass = :tplClass order by isRead asc,messageId desc";
        HashMap<String, Object> map = new HashMap<>();
        map.put("sellerId", sellerId);
        map.put("tplClass", tplClass);
        return findByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 根据卖家编号查询消息列表
     * @param sellerId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<StoreMessage> findUnreadBySellerId(int sellerId, int pageNo, int pageSize) {
        String hql = "from StoreMessage where sellerId = :sellerId and isRead = :isRead order by messageId desc";
        HashMap<String, Object> map = new HashMap<>();
        map.put("sellerId", sellerId);
        map.put("isRead", State.NO);
        return findByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 根据卖家编号查询消息数量
     * @param sellerId
     * @return
     */
    public long findCountBySellerId(int sellerId) {
        String hql = "select count(*) from StoreMessage where sellerId = :sellerId";
        HashMap<String, Object> map = new HashMap<>();
        map.put("sellerId", sellerId);
        return findCount(hql, map);
    }

    /**
     * 根据卖家编号查询消息数量
     * @param sellerId
     * @return
     */
    public long findCountBySellerId(int sellerId, int tplClass) {
        String hql = "select count(*) from StoreMessage where sellerId = :sellerId and tplClass = :tplClass";
        HashMap<String, Object> map = new HashMap<>();
        map.put("sellerId", sellerId);
        map.put("tplClass", tplClass);
        return findCount(hql, map);
    }

    /**
     * 根据卖家编号查询未读消息数量
     * @param sellerId
     * @return
     */
    public long findUnreadCountBySellerId(int sellerId) {
        String hql = "select count(*) from StoreMessage where sellerId = :sellerId and isRead = :isRead";
        HashMap<String, Object> map = new HashMap<>();
        map.put("sellerId", sellerId);
        map.put("isRead", State.NO);
        return findCount(hql, map);
    }

    /**
     * 根据商户编号更好已读状态
     * @param isRead
     * @param sellerId
     */
    public void updateIsReadBySellerId(int isRead, int sellerId, Integer[] messageId) {
        String hql = "update StoreMessage set isRead = :isRead where messageId in (:messageId) and sellerId = :sellerId";
        HashMap<String, Object> map = new HashMap<>();
        map.put("isRead", isRead);
        map.put("sellerId", sellerId);
        map.put("messageId", Arrays.asList(messageId));
        update(hql, map);
    }
}
