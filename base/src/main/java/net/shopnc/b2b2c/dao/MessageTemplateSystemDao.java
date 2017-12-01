package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.domain.MessageTemplateSystem;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息模板
 * Created by shopnc on 2015/11/16.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class MessageTemplateSystemDao extends BaseDaoHibernate4<MessageTemplateSystem> {
}
