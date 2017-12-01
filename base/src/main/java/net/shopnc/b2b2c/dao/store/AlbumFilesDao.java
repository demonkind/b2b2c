package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.AlbumFiles;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-02.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class AlbumFilesDao extends BaseDaoHibernate4<AlbumFiles> {
    /**
     * 查询图片数量
     * @param storeId
     * @return
     */
    public long findCountByStoreId(int storeId) {
        String hql = "select count(*) from AlbumFiles where storeId = :storeId";
        HashMap<String, Object> map = new HashMap<>();
        map.put("storeId", storeId);
        return super.findCount(hql, map);
    }

    /**
     * 根据店铺编号查询图片数量
     * @param storeId
     * @param filesType
     * @return
     */
    public long findCountByStoreId(int storeId, int filesType) {
        String hql = "select count(*) from AlbumFiles where storeId = :storeId and filesType = :filesType";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        map.put("filesType", filesType);
        return super.findCount(hql, map);
    }

    /**
     * 根据店铺编号查询店铺列表
     * @param storeId
     * @param filesType
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<AlbumFiles> findListByStoreId(int storeId, int filesType, int pageNo, int pageSize) {
        String hql = "from AlbumFiles where storeId = :storeId and filesType = :filesType";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("storeId", storeId);
        map.put("filesType", filesType);
        return super.findByPage(hql, pageNo, pageSize, map);
    }
}
