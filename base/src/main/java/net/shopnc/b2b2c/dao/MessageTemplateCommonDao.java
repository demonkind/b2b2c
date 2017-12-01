package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.constant.MessageTemplateCommonTplType;
import net.shopnc.b2b2c.domain.MessageTemplateCommon;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc on 2015/11/16.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class MessageTemplateCommonDao extends BaseDaoHibernate4<MessageTemplateCommon> {
    /**
     * 查询商家消息模板
     * @return
     */
    public List<MessageTemplateCommon> findMessageTemplateSeller() {
        String hql = "from MessageTemplateCommon where tplType = :tplType";
        HashMap<String, Object> map = new HashMap<>();
        map.put("tplType", MessageTemplateCommonTplType.SELLER);
        return find(hql, map);
    }

    /**
     * 根据模板分类查询会员消息模板
     * @param tplClass
     * @return
     */
    public List<MessageTemplateCommon> findMessageTemplateMemberByTplClass(int tplClass) {
        String hql = "from MessageTemplateCommon where tplType = :tplType and tplClass = :tplClass";
        HashMap<String, Object> map = new HashMap<>();
        map.put("tplType", MessageTemplateCommonTplType.MEMBER);
        map.put("tplClass", tplClass);
        return find(hql, map);
    }
}
