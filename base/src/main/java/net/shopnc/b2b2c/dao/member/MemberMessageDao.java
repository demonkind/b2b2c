package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.member.MemberMessage;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 会员消息
 * Created by shopnc.feng on 2016-02-18.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class MemberMessageDao extends BaseDaoHibernate4<MemberMessage> {
    /**
     * 根据会员编号查询会员消息
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<MemberMessage> findByMemberId(int memberId, int pageNo, int pageSize) {
        String hql = "from MemberMessage where memberId = :memberId order by isRead asc,messageId desc";
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        return findByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 根据会员编号查询会员消息
     * @param memberId
     * @param tplClass
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<MemberMessage> findByMemberId(int memberId, int tplClass, int pageNo, int pageSize) {
        String hql = "from MemberMessage where memberId = :memberId and tplClass = :tplClass order by isRead asc,messageId desc";
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("tplClass", tplClass);
        return findByPage(hql, pageNo, pageSize, map);
    }

    /**
     * 根据会员编号查询消息数量
     * @param memberId
     * @return
     */
    public long findCountByMemberId(int memberId) {
        String hql = "select count(*) from MemberMessage where memberId = :memberId";
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        return findCount(hql, map);
    }

    /**
     * 根据会员编号查询消息数量
     * @param memberId
     * @param tplClass
     * @return
     */
    public long findCountByMemberId(int memberId, int tplClass) {
        String hql = "select count(*) from MemberMessage where memberId = :memberId and tplClass = :tplClass";
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("tplClass", tplClass);
        return findCount(hql, map);
    }

    /**
     * 根据会员编号查询未读消息数量
     * @param memberId
     * @return
     */
    public long findUnreadCountByMemberId(int memberId) {
        String hql = "select count(*) from MemberMessage where memberId = :memberId and isRead = :isRead";
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("isRead", State.NO);
        return findCount(hql, map);
    }


    /**
     * 根据会员编号更改已读状态
     * @param isRead
     * @param memberId
     */
    public void updateIsReadByMemberId(int isRead, int memberId, Integer[] messageId) {
        String hql = "update MemberMessage set isRead = :isRead where memberId = :memberId and messageId in (:messageId)";
        HashMap<String, Object> map = new HashMap<>();
        map.put("isRead", isRead);
        map.put("memberId", memberId);
        map.put("messageId", Arrays.asList(messageId));
        update(hql, map);
    }
}
