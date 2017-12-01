package net.shopnc.b2b2c.dao.store;

import org.springframework.stereotype.Repository;
import net.shopnc.b2b2c.domain.store.StoreClass;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lzp on 2015/10/26.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreClassDao extends BaseDaoHibernate4<StoreClass> {
}
