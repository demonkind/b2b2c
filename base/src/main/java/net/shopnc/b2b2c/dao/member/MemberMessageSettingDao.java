package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.MemberMessageSetting;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Created by shopnc.feng on 2016-02-18.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class MemberMessageSettingDao extends BaseDaoHibernate4<MemberMessageSetting> {
    /**
     * 根据消息模板编号和会员编号删除消息接收设置
     * @param tplCode
     * @param memberId
     */
    public void deleteByTplCodeAndMemberId(String tplCode, int memberId) {
        String hql = "delete MemberMessageSetting where memberId = :memberId and tplCode = :tplCode";
        HashMap<String, Object> map = new HashMap<>();
        map.put("tplCode", tplCode);
        map.put("memberId", memberId);
        super.delete(hql, map);
    }

    /**
     * 根据消息模板编号和会员编号查询消息接收设置
     * @param tplCode
     * @param memberId
     * @return
     */
    public MemberMessageSetting getByTplCodeAndMemberId(String tplCode, int memberId) {
        String hql = "from MemberMessageSetting where memberId = :memberId and tplCode = :tplCode";
        MemberMessageSetting MemberMessageSetting = (MemberMessageSetting) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setParameter("memberId", memberId)
                .setParameter("tplCode", tplCode)
                .uniqueResult();
        return MemberMessageSetting;
    }
}
