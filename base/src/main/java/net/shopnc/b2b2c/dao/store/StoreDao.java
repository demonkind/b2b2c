package net.shopnc.b2b2c.dao.store;

import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.common.dao.BaseDaoHibernate4;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lzp on 2015/10/26.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class StoreDao extends BaseDaoHibernate4<Store>  {
    /**
     * 获得店铺列表
     */
    public List<Store> getStoreList(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "from Store where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.find(hql, params);
    }

    /**
     * 跟据主键更新店铺指定字段
     * @param setItems
     * @param params
     * @param storeId
     */
    public void editStore (List<Object> setItems, HashMap<String,Object> params,int storeId) {
        String setHql = "";
        String hql = "";
        for (int i=0; i<setItems.size(); i++) {
            setHql += (setItems.get(i) + ",");
        }
        Pattern pattern = Pattern.compile("^,+|,+$");
        setHql = pattern.matcher(setHql).replaceAll("");

        hql = "update Store set " + setHql + " where storeId = :storeId";
        params.put("storeId",storeId);
        super.update(hql,params);
    }
    
    /**
     * 根据店铺分类编号查询描述评价平均值、服务评价平均值、发货速度评价平均值
     * @param classId
     * @return
     */
    public List<Object> getAvgEvaluateByClassId(int classId){
    	String hql="select avg(storeDesccredit),avg(storeServicecredit),avg(storeDeliverycredit) from Store where classId=:classId";
    	HashMap<String,Object> params=new HashMap<String, Object>();
		params.put("classId", classId);
		List<Object> list=super.findObject(hql, params);
		return list;
    }

    /**
     * 取得有限的店铺列表
     * @param pageNo
     * @param pageSize
     */
    public List<Store> getStoreLimitList(int pageNo, int pageSize) {
        String hql = "from Store order by storeId asc";
        return super.findByPage(hql, pageNo, pageSize);
    }

    /**
     * 查询商品SPU总数
     * @param whereList
     * @param params
     * @return
     */
    public long findStoreCount(List<String> whereList, HashMap<String, Object> params) {
        String hql = "select count(*) from Store where 1=1 ";
        for (String item : whereList) {
            hql += (" and " + item);
        }
        return super.findCount(hql, params);
    }
}
