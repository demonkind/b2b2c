package net.shopnc.b2b2c.dao;

import net.shopnc.b2b2c.domain.Navigation;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * 菜单导航
 * Created by hbj on 2015/12/7.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class NavigationDao extends BaseDaoHibernate4<Navigation> {

    public List<Navigation> getNavigationListByPositionId(int positionId) {
        String hql = "from Navigation where positionId = :positionId order by sort";
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("positionId",positionId);
        return super.find(hql,params);
    }
}
