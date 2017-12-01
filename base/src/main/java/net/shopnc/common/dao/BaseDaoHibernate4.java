package net.shopnc.common.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.entity.dtgrid.QueryUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Transactional(rollbackFor = {Exception.class})
public class BaseDaoHibernate4<T> {
    protected final Logger logger = Logger.getLogger(getClass());

    @Autowired
    protected SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public T get(Class<T> entityClazz, Serializable id) {
        return (T) sessionFactory.getCurrentSession().get(entityClazz, id);
    }

    public Serializable save(T entity) {
        return sessionFactory.getCurrentSession().save(entity);
    }

    public void update(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public void update(String hql, HashMap<String,Object> params) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        for (String key : params.keySet()) {
            if (params.get(key) instanceof List) {
                query.setParameterList(key,(List)params.get(key));
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        query.executeUpdate();
    }

    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void delete(Class<T> entityClazz, Serializable id) {
        sessionFactory.getCurrentSession()
                .createQuery("delete " + entityClazz.getSimpleName()
                        + " en where en.id = ?0")
                .setParameter("0", id)
                .executeUpdate();
    }

    public void delete(String hql, HashMap<String, Object> params) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery(hql);
        for (String key : params.keySet()) {
            if (params.get(key) instanceof List) {
                query.setParameterList(key,(List)params.get(key));
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        query.executeUpdate();
    }

    public List<T> findAll(Class<T> entityClazz) {
        return find("select en from "
                + entityClazz.getSimpleName() + " en");
    }

    public long findCount(Class<T> entityClazz) {
        List<?> l = find("select count(*) from "
                + entityClazz.getSimpleName());

        if (l != null && l.size() == 1) {
            return (Long) l.get(0);
        }
        return 0;
    }

    @Deprecated
    public long findCount(String hql, List<Object>params) {
        List<?> l = find(hql, params);
        if (l != null && l.size() == 1) {
            return (Long) l.get(0);
        }
        return 0;
    }

    public long findCount(String hql, HashMap<String,Object> params) {
        List<?> l = find(hql, params);
        if (l != null && l.size() == 1) {
            return Long.valueOf(l.get(0).toString());
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public List<T> find(String hql) {
        return (List<T>) sessionFactory.getCurrentSession()
                .createQuery(hql)
                .list();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(String hql,int maxCount) {
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setMaxResults(maxCount);
        return query.list();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public List<T> find(String hql, List<Object> params) {

        Query query = sessionFactory.getCurrentSession()
                .createQuery(hql);

        for (int i = 0, len = params.size(); i < len; i++) {
            query.setParameter(i + "", params.get(i));
        }
        return (List<T>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(String hql, HashMap<String,Object> params) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery(hql);
        for (String key : params.keySet()) {
            if (params.get(key) instanceof List) {
                query.setParameterList(key,(List)params.get(key));
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        query.toString();
        return (List<T>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object> findObject(String hql, HashMap<String,Object> params) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery(hql);
        if(params!=null){
	        for (String key : params.keySet()) {
	            if (params.get(key) instanceof List) {
	                query.setParameterList(key,(List)params.get(key));
	            } else {
	                query.setParameter(key, params.get(key));
	            }
	        }
        }
        query.toString();
        return (List<Object>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByPage(String hql,
                                 int pageNo, int pageSize) {

        return sessionFactory.getCurrentSession()
                .createQuery(hql)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public List<T> findByPage(String hql, int pageNo, int pageSize
            , List<Object> params) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery(hql);
        for (int i = 0, len = params.size(); i < len; i++) {
            query.setParameter(i + "", params.get(i));
        }
        return query.setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByPage(String hql, int pageNo, int pageSize
            , HashMap<String,Object> params) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery(hql);
        for (String key : params.keySet()) {
            if (params.get(key) instanceof List) {
                query.setParameterList(key,(List)params.get(key));
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        return query.setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @SuppressWarnings("unchecked")
    public List<Object> findObjectByPage(String hql, int pageNo, int pageSize
            , HashMap<String,Object> params) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery(hql);
        if(params!=null){
	        for (String key : params.keySet()) {
	            if (params.get(key) instanceof List) {
	                query.setParameterList(key,(List)params.get(key));
	            } else {
	                query.setParameter(key, params.get(key));
	            }
	        }
        }
        return query.setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    /**
     * 批量保存
     * @param entityList
     * @return
     */
    public List<Serializable> saveAll(List<T> entityList) {
        List<Serializable> integerList = new ArrayList<Serializable>();
        for (T entity : entityList) {
            Serializable id = save(entity);
            integerList.add(id);
        }
        return integerList;
    }

    /**
     * 批量更新
     * @param entityList
     * @return
     */
    public void updateAll(List<T> entityList) {
        for (T entity : entityList) {
            update(entity);
        }
    }

    /**
     * 批量删除
     * @param entityList
     * @return
     */
    public void deleteAll(List<T> entityList) {
        for (T entity : entityList) {
            delete(entity);
        }
    }

    @SuppressWarnings("unchecked")
    public DtGrid getDtGridList(String dtGridPager, Class<T> entityClazz) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
            for (String key : dtGrid.getNcColumnsType().keySet()) {
                for (int i = 0; i< dtGrid.getNcColumnsType().get(key).size(); i++) {
                    hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                }
                dtGrid.setNcColumnsTypeList(hashMap);
            }
        }
        QueryUtils.parseDtGridHql(dtGrid);
        //定义参数值列表
        List<Object> arguments = dtGrid.getArguments();
        //生成带占位符的hql
        String hql = dtGrid.getWhereHql();
        //生成排序hql
        String sortHql = dtGrid.getSortHql();

        long recordCount = findCount("select count(*) from " + entityClazz.getSimpleName() + hql, arguments);
        dtGrid.setRecordCount(recordCount);
        int pageCount = (int)recordCount/dtGrid.getPageSize()+(recordCount%dtGrid.getPageSize() > 0 ?1:0);
        dtGrid.setPageCount(pageCount);
        List<Object> list = (List<Object>) findByPage("from " + entityClazz.getSimpleName() + hql + sortHql, dtGrid.getNowPage(), dtGrid.getPageSize(), arguments);
        dtGrid.setExhibitDatas(list);
        return dtGrid;
    }

    @SuppressWarnings("unchecked")
    public DtGrid getDtGridList(DtGrid dtGrid, Class<T> entityClazz) throws Exception {
        //定义参数值列表
        List<Object> arguments = dtGrid.getArguments();
        //生成带占位符的hql
        String hql = dtGrid.getWhereHql();
        //生成排序hql
        String sortHql = dtGrid.getSortHql();
        long recordCount = findCount("select count(*) from " + entityClazz.getSimpleName() + hql, arguments);
        dtGrid.setRecordCount(recordCount);
        int pageCount = (int)recordCount/dtGrid.getPageSize()+(recordCount%dtGrid.getPageSize() > 0 ?1:0);
        dtGrid.setPageCount(pageCount);
        List<Object> list = (List<Object>) findByPage("from " + entityClazz.getSimpleName() + hql + sortHql, dtGrid.getNowPage(), dtGrid.getPageSize(), arguments);
        dtGrid.setExhibitDatas(list);
        return dtGrid;
    }
}
