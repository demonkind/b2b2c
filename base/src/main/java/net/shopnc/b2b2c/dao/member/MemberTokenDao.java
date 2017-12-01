package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.MemberToken;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by zxy on 2015-12-01.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class MemberTokenDao extends BaseDaoHibernate4<MemberToken> {

    /**
     * 按照一定规则生成会员令牌
     * @return 会员令牌
     */
    public String getTokenCode(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 查询会员令牌记录
     * @param where 查询条件
     * @param params HQL参数值
     * @return 会员令牌实体
     */
    public MemberToken getMemberTokenInfo(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "from MemberToken where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        List<MemberToken> list = super.find(hql, params);
        MemberToken memberToken = null;
        if (list.size() > 0) {
            memberToken = list.get(0);
        }
        return memberToken;
    }

    /**
     * 通过令牌获得会员令牌记录
     * @param token 会员令牌
     * @return 会员令牌实体
     */
    public MemberToken getMemberTokenByToken(String token){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("token", "token = :token");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("token", token);
        return this.getMemberTokenInfo(where, params);
    }

    /**
     * 删除会员令牌记录
     * @param where 查询条件
     * @param params HQL参数值
     */
    public void deleteMemberToken(HashMap<String,String> where,HashMap<String,Object> params)
    {
        String hql = "delete MemberToken where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        super.delete(hql, params);
    }

    /**
     * 删除会员令牌记录
     * @param memberId 会员编号
     * @param clientType 客户端类型
     */
    public void deleteMemberTokenByMemberId(int memberId, String clientType){
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("memberId", "memberId = :memberId");
        where.put("clientType", "clientType = :clientType");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("memberId", memberId);
        params.put("clientType", clientType);
        this.deleteMemberToken(where, params);
    }
}