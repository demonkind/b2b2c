package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2015-11-3.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class MemberDao extends BaseDaoHibernate4<Member> {

    /**
     * 会员总数
     * @param where 查询条件
     * @param params HQL参数值
     * @return 会员总数
     */
    public Long findMemberCount(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "select count(*) from Member where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findCount(hql, params);
    }

    /**
     * 查询用户名否存在
     * @param memberName 会员名称
     * @return boolean true为存在，false为不存在
     */
    public boolean memberNameIsExist(String memberName){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("memberName", "memberName = :memberName");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("memberName", memberName);
        Long count = this.findMemberCount(where,params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询用户名是否存在并排除指定会员ID的会员
     * @param memberName 会员名称
     * @param memberId 会员编号
     * @return boolean true为存在，false为不存在
     */
    public boolean memberNameIsExist(String memberName,int memberId){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("memberName", "memberName = :memberName");
        where.put("memberId", "memberId <> :memberId");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("memberName", memberName);
        params.put("memberId", memberId);
        Long count = this.findMemberCount(where,params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询邮箱是否存在
     * @param email 邮箱
     * @return boolean true为存在，false为不存在
     */
    public boolean emailIsExist(String email){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("email", "email = :email");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("email", email);
        Long count = this.findMemberCount(where,params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 查询邮箱是否存在并排除指定会员ID的会员
     * @param email 邮箱
     * @param memberId 会员编号
     * @return boolean true为存在，false为不存在
     */
    public boolean emailIsExist(String email,int memberId){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("email", "email = :email");
        where.put("memberId", "memberId <> :memberId");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("email", email);
        params.put("memberId", memberId);
        Long count = this.findMemberCount(where,params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询手机是否存在
     * @param mobile 手机号
     * @return boolean true为存在，false为不存在
     */
    public boolean mobileIsExist(String mobile){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("mobile", "mobile = :mobile");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("mobile", mobile);
        Long count = this.findMemberCount(where,params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询手机是否存在并排除指定会员ID的会员
     * @param mobile 手机号
     * @param memberId 会员编号
     * @return boolean true为存在，false为不存在
     */
    public boolean mobileIsExist(String mobile,int memberId){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("mobile", "mobile = :mobile");
        where.put("memberId", "memberId <> :memberId");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("mobile", mobile);
        params.put("memberId", memberId);
        Long count = this.findMemberCount(where,params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 按照规则生成会员名
     * @return 会员名
     */
    public String createMemberName(){
        String prefix = "u_";
        //时间精确到天
        String timeStr = ((Long)System.currentTimeMillis()).toString().substring(0,6);
        //查询最大ID
        String hql = "from Member order by memberId desc";
        List<Member> memberList = super.find(hql,1);
        int memberId = 0;
        if (!memberList.isEmpty()) {
            memberId = memberList.get(0).getMemberId();
        }
        String number;
        if (memberId == 0) {
            number = "0001";
        }else if (memberId < 10000){
            number = String.format("%04d", memberId);
        }else{
            String tmp = ((Integer)memberId).toString();
            number = tmp.substring(0,4);
        }
        String memberName = prefix + number + timeStr;
        return memberName;
    }

    /**
     * 查询会员详情
     * @param where 查询条件
     * @param params HQL参数值
     * @return 会员实体
     */
    public Member getMemberInfo(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "from Member where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        List<Member> list = super.find(hql, params);
        Member member = null;
        if (list.size() > 0) {
            member = list.get(0);
        }
        return member;
    }

    /**
     * 通过会员名查询会员信息
     * @param memberName 会员名称
     * @return 会员实体
     */
    public Member getMemberInfoByMemberName(String memberName){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("memberName", "memberName = :memberName");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("memberName", memberName);
        return this.getMemberInfo(where,params);
    }

    /**
     * 通过手机号查询会员信息
     * @param mobile 手机号
     * @return 会员实体
     */
    public Member getMemberInfoByMobile(String mobile){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("mobile", "mobile = :mobile");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("mobile", mobile);
        return this.getMemberInfo(where,params);
    }

    /**
     * 根据会员ID编辑会员信息
     * @param updateMap 更新参数及值
     * @param memberId 会员编号
     * @return 更新结果true为成功，false为失败
     */
    public Boolean updateByMemberId(HashMap<String,Object> updateMap, int memberId){
        String hql = "update Member set ";
        HashMap<String,Object> params = new HashMap<String, Object>();
        //update
        int i=0;
        for (String key : updateMap.keySet()) {
            if (i > 0) {
                hql += (" ," + key + "=:" + key);
            }else{
                hql += (key + "=:" + key);
            }
            params.put(key,updateMap.get(key));
            i++;
        }
        //where
        hql +=" where memberId = :memberId";
        params.put("memberId", memberId);
        try{
            super.update(hql,params);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获得会员列表
     * @param where 查询条件
     * @param params HQL参数值
     * @return 会员列表
     */
    public List<Member> getMemberList(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "from Member where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.find(hql, params);
    }

    /**
     * 取得带锁的会员信息
     * @param memberId 会员编号
     * @return 会员实体
     */
    public Member getLockModelMemberInfo(int memberId) {
        String hql = "from Member Lmember where memberId = :memberId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("memberId", memberId);
        query.setLockMode("Lmember", LockMode.PESSIMISTIC_WRITE);
        List<Member> memberList = query.list();
        return memberList.size()>0 ? memberList.get(0) : null;
    }
}
