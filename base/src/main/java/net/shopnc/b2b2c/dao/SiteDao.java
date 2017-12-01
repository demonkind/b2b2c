package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.domain.Site;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 站点设置
 * Created by shopnc on 2015/11/6.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class SiteDao extends BaseDaoHibernate4<Site> {
}
