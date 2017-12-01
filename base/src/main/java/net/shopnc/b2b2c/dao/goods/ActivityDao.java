package net.shopnc.b2b2c.dao.goods;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.domain.goods.Activity;
import net.shopnc.b2b2c.domain.goods.Attribute;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.util.ShopHelper;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class ActivityDao extends BaseDaoHibernate4<Activity> {
	
	/**
     * 获取用户自己的活动信息列表
     * @param memberId
     * @param acrivityId
     * @param activityName
     */
	public List<Object> findActivityList( HashMap<String, Object> map,int pageNo,int pageSize,String hqlCondition) {
        String hql = "from Activity where memberId = :memberId and isFlag=0"+hqlCondition;
        return super.findObjectByPage(hql, pageNo, pageSize, map);
    }
	
	
	/**
     * 修改活动信息
     * @param memberId
     * @param acrivityId
     * @param activityName
     */
    public void updateActivity(HashMap<String,Object> hashMap) {
        String hql = "update Activity set activityName = :activityName where activityId = :activityId ";
        super.update(hql,hashMap);
    }
    
    /**
     * 删除活动信息
     * @param memberId
     * @param acrivityId
     * @param activityName
     */
    public void delActivity(HashMap<String,Object> hashMap) {
        String hql = "update Activity set isFlag = 1 where activityId = :activityId ";
        super.update(hql,hashMap);
    }
    
    
    public int getCount(String condition,HashMap<String,Object> params){
    	String hql="select count(*) from Activity where 1=1 "+condition;
    	long count=super.findCount(hql, params);
    	if(count>0){
    		return 1;
    	}else{
    		return 0;
    	}
    } 
    
}
