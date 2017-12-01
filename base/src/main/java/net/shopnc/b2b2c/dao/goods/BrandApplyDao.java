package net.shopnc.b2b2c.dao.goods;

import net.shopnc.b2b2c.domain.goods.BrandApply;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shopnc.feng on 2016-01-26.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class BrandApplyDao extends BaseDaoHibernate4<BrandApply> {

}
