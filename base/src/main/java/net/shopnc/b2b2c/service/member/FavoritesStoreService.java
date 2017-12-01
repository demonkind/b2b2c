package net.shopnc.b2b2c.service.member;


import net.shopnc.b2b2c.dao.member.FavoritesStoreDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.member.FavoritesStore;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.favorites.FavoritesStoreVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-20
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class FavoritesStoreService extends BaseService {
    @Autowired
    private FavoritesStoreDao favoritesStoreDao;
    @Autowired
    private StoreDao storeDao;

    /**
     * 关注店铺
     * @param storeId
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean addFavoritesStore(Integer storeId,Integer memberId) throws ShopException {
        if (storeId==null || storeId<=0 || memberId==null || memberId<=0) {
            throw new ParameterErrorException();
        }
        //判断是否已经关注
        HashMap<String,String> where = new HashMap<String, String>();
        where.put("storeId", "storeId = :storeId");
        where.put("memberId", "memberId = :memberId");

        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        params.put("memberId", memberId);
        Long count = favoritesStoreDao.findFavoritesStoreCount(where, params, "");
        if (count > 0) {
            throw new ShopException("您已关注过该店铺");
        }
        //查询店铺详情
        Store storeInfo = storeDao.get(Store.class, storeId);

        //添加关注
        FavoritesStore favoritesStore = new FavoritesStore();
        favoritesStore.setStoreId(storeId);
        favoritesStore.setMemberId(memberId);
        favoritesStore.setAddTime(ShopHelper.getCurrentTimestamp());
        Integer favId = (Integer)favoritesStoreDao.save(favoritesStore);
        //累计关注次数
        if (favId > 0) {
            List<Object> setItems = new ArrayList<Object>();
            setItems.add("storeCollect = :storeCollect");
            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("storeCollect", storeInfo.getStoreCollect() + 1);
            storeDao.editStore(setItems, updateMap, storeId);
        }
        return true;
    }
    /**
     * 店铺关注列表分页
     * @param params
     * @param page
     * @param pageSize
     * @param sort
     * @param group
     * @return
     */
    public HashMap<String,Object> getFavoritesStoreListByPage(HashMap<String,Object> params, int page, int pageSize, String sort, String group) {
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(favoritesStoreDao.findFavoritesStoreCount(where, params, ""));
        pageEntity.setPageNo(page);
        if (pageSize > 0) {
            pageEntity.setPageSize(pageSize);
        }
        HashMap<String,String> listWhere = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                listWhere.put("memberId", "favStore.memberId = :memberId");
            }
        }
        List<FavoritesStoreVo> favList = favoritesStoreDao.getFavoritesStoreListByPage(listWhere, params, pageEntity.getPageNo(), pageEntity.getPageSize(), sort, group);

        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("list", favList);
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    /**
     * 店铺关注列表
     * @param params
     * @param num
     * @param sort
     * @param group
     * @return
     */
    public List<FavoritesStoreVo> getFavoritesStoreList(HashMap<String,Object> params, int num, String sort, String group) {
        HashMap<String,String> listWhere = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("memberId")) {
                listWhere.put("memberId", "favStore.memberId = :memberId");
            }
        }
        return favoritesStoreDao.getFavoritesStoreListByPage(listWhere, params, 1, num, sort, group);
    }
    /**
     * 删除关注
     * @param favoritesId
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean delFavoritesStore(Integer favoritesId, int memberId) throws ShopException {
        if (favoritesId==null || favoritesId<=0 || memberId<=0) {
            throw new ParameterErrorException();
        }
        //查询关注信息
        FavoritesStore favoritesStoreInfo = favoritesStoreDao.get(FavoritesStore.class, favoritesId);
        if (favoritesStoreInfo == null || favoritesStoreInfo.getMemberId() != memberId) {
            throw new ParameterErrorException();
        }
        HashMap<String, String> where = new HashMap<String, String>();
        where.put("favoritesId", "favoritesId = :favoritesId");
        where.put("memberId", "memberId = :memberId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("favoritesId", favoritesId);
        params.put("memberId", memberId);
        favoritesStoreDao.deleteFavoritesStore(where, params);
        //查询店铺信息
        Store storeInfo = storeDao.get(Store.class, favoritesStoreInfo.getStoreId());
        if (storeInfo != null) {
            //更新关注数量
            List<Object> setItems = new ArrayList<Object>();
            setItems.add("storeCollect = :storeCollect");
            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("storeCollect", storeInfo.getStoreCollect() - 1);
            storeDao.editStore(setItems, updateMap, storeInfo.getStoreId());
        }
        return true;
    }
}