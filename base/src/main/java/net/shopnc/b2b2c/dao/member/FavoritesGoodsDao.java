package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.FavoritesGoods;
import net.shopnc.b2b2c.vo.favorites.FavoritesGoodsVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-18
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class FavoritesGoodsDao extends BaseDaoHibernate4<FavoritesGoods> {
    /**
     * 商品关注总数
     * @param where 查询条件
     * @param params HQL参数值
     * @param group 分组条件
     * @return 商品关注总数
     */
    public Long findFavoritesGoodsCount(HashMap<String,String> where, HashMap<String,Object> params, String group)
    {
        String hql = "select count(*) from FavoritesGoods where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        if (group!=null && group.length()>0) {
            hql += (" group by " + group);
        }
        return super.findCount(hql, params);
    }

    /**
     * 商品关注详细列表分页
     * @param where 查询条件
     * @param params HQL参数值
     * @param pageNo 当前页数
     * @param pageSize 分页条数
     * @param sort 排序条件
     * @param group 分组条件
     * @return 关注详细列表
     */
    public List<FavoritesGoodsVo> getFavoritesGoodsListByPage(HashMap<String,String> where, HashMap<String,Object> params,int pageNo, int pageSize, String sort, String group) {
        String hql = "select new net.shopnc.b2b2c.vo.favorites.FavoritesGoodsVo(fg, g, gc) from FavoritesGoods fg, Goods g, GoodsCommon gc where fg.goodsId=g.goodsId and g.commonId=gc.commonId ";
        //where
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        //group
        if (group!=null && group.length()>0) {
            hql += (" group by " + group);
        }else{
            hql += " group by g.goodsId";
        }
        //sort
        if (sort!=null && sort.length()>0) {
            hql += (" order by " + sort);
        } else {
            hql += " order by fg.favoritesId desc";
        }
        List<Object> favList = (List)super.findObjectByPage(hql, pageNo, pageSize, params);
        List<FavoritesGoodsVo> favListNew = new ArrayList<FavoritesGoodsVo>();
        if (favList!=null && favList.size()>0) {
            for (int j = 0; j < favList.size(); j++) {
                favListNew.add((FavoritesGoodsVo)favList.get(j));
            }
        }
        return favListNew;
    }
    /**
     * 删除商品关注
     * @param where 查询条件
     * @param params HQL参数值
     */
    public void deleteFavoritesGoods(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "delete FavoritesGoods where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        super.delete(hql, params);
    }
    
    public FavoritesGoods findFavoritesById(HashMap<String,Object> params){
    	String hql="from FavoritesGoods where goodsId=:goodsId and memberId=:memberId";
    	List<FavoritesGoods> objList=super.find(hql, params);
    	if(objList!=null&&objList.size()>0){
    		return objList.get(0);
    	}else{
    		return null;
    	}
    }

}