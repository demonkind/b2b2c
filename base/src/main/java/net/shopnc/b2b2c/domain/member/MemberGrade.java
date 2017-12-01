package net.shopnc.b2b2c.domain.member;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 会员等级<br>
 * Created by zxy on 2015-11-26
 */
@Entity
@Table(name = "member_grade")
public class MemberGrade implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="grade_id")
    private int gradeId;
    /**
     * 等级值
     */
    @Column(name="grade_level")
    private int gradeLevel = 0;
    /**
     * 等级名称
     */
    @Column(name="grade_name")
    private String gradeName = "";
    /**
     * 经验值
     */
    @Column(name="exp_points")
    private int expPoints = 0;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getExpPoints() {
        return expPoints;
    }

    public void setExpPoints(int expPoints) {
        this.expPoints = expPoints;
    }

    @Override
    public String toString() {
        return "MemberGrade{" +
                "gradeId=" + gradeId +
                ", gradeLevel='" + gradeLevel + '\'' +
                ", gradeName=" + gradeName +
                ", expPoints=" + expPoints +
                '}';
    }
}
