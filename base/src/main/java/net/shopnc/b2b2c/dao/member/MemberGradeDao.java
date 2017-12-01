package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.domain.member.MemberGrade;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by zxy on 2015-11-26.
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class MemberGradeDao extends BaseDaoHibernate4<MemberGrade> {

    /**
     * 查询会员等级列表
     * @return 会员等级列表
     */
    public List<MemberGrade> getGradeList(){
        return super.find("from MemberGrade order by gradeLevel asc");
    }
}