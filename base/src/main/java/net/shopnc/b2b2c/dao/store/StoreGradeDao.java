package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.StoreGrade;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dqw on 2015/12/11.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreGradeDao extends BaseDaoHibernate4<StoreGrade> {
}
