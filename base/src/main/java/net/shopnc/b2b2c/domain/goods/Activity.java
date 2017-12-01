package net.shopnc.b2b2c.domain.goods;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
  
    @Column(name = "activity_id")
    private String activityId;

    @Column(name = "activity_name")
    private String activityName;
    
    @Column(name = "member_id")
    private String memberId;
    
	@Column(name = "is_flag")
    private String isFlag;

  
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getActivityId() {
		return activityId;
	}



	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}



	public String getActivityName() {
		return activityName;
	}



	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}



	public String getMemberId() {
		return memberId;
	}



	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}



	public String getIsFlag() {
		return isFlag;
	}



	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}



	@Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", memberId='" + memberId + '\'' +
                ", isFlag='" + isFlag + '\'' +
                '}';
    }
}
