package net.shopnc.b2b2c.dao.admin;

import net.shopnc.b2b2c.domain.admin.AdminGroup;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dqw on 2015/12/29.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class AdminGroupDao extends BaseDaoHibernate4<AdminGroup> {

}

