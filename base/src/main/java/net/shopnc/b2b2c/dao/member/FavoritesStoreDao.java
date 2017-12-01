package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.FavoritesStore;
import net.shopnc.b2b2c.vo.favorites.FavoritesStoreVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-20
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class FavoritesStoreDao extends BaseDaoHibernate4<FavoritesStore> {
    /**
     * 店铺关注总数
     * @param where 查询条件
     * @param params HQL参数
     * @param group 分组条件
     * @return 店铺关注总数
     */
    public Long findFavoritesStoreCount(HashMap<String,String> where, HashMap<String,Object> params, String group)
    {
        String hql = "select count(*) from FavoritesStore where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        if (group!=null && group.length()>0) {
            hql += (" group by " + group);
        }
        return super.findCount(hql, params);
    }

    /**
     * 店铺关注详细列表分页
     * @param where 查询条件
     * @param params HQL参数
     * @param pageNo 当前分页
     * @param pageSize 分页条数
     * @param sort 排序条件
     * @param group 分组条件
     * @return 店铺关注详细列表分页
     */
    public List<FavoritesStoreVo> getFavoritesStoreListByPage(HashMap<String,String> where, HashMap<String,Object> params,int pageNo, int pageSize, String sort, String group) {
        String hql = "select new net.shopnc.b2b2c.vo.favorites.FavoritesStoreVo(favStore, store) from FavoritesStore favStore, Store store where favStore.storeId=store.storeId ";
        //where
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        //group
        if (group!=null && group.length()>0) {
            hql += (" group by " + group);
        }
        //sort
        if (sort!=null && sort.length()>0) {
            hql += (" order by " + sort);
        } else {
            hql += " order by favStore.favoritesId desc";
        }
        List<Object> favList = (List)super.findObjectByPage(hql, pageNo, pageSize, params);
        List<FavoritesStoreVo> favListNew = new ArrayList<FavoritesStoreVo>();
        if (favList!=null && favList.size()>0) {
            for (int j = 0; j < favList.size(); j++) {
                favListNew.add((FavoritesStoreVo)favList.get(j));
            }
        }
        return favListNew;
    }

    /**
     * 删除关注
     * @param where 查询条件
     * @param params HQL参数
     */
    public void deleteFavoritesStore(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "delete FavoritesStore where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        super.delete(hql, params);
    }

}