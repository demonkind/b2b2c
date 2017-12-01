package net.shopnc.b2b2c.domain.contract;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 店铺消费者保障服务退出申请表
 */
@Entity
@Table(name = "contract_quitapply")
public class ContractQuitapply implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "ctq_id")
	private Integer ctqId;
	
	/**
	 * 项目ID
	 */
	@Column(name = "ctq_itemid", nullable = false)
	private Integer ctqItemid;
	
	/**
	 * 项目名称
	 */
	@Column(name = "ctq_itemname", nullable = false, length = 200)
	private String ctqItemname;
	
	/**
	 * 店铺ID
	 */
	@Column(name = "ctq_storeid", nullable = false)
	private Integer ctqStoreid;
	
	/**
	 * 店铺名称
	 */
	@Column(name = "ctq_storename", nullable = false, length = 500)
	private String ctqStorename;
	
	/**
	 * 添加时间
	 */
	@Column(name = "ctq_addtime", nullable = false)
	private Timestamp ctqAddtime;
	
	/**
	 * 审核状态0未审核1审核通过2审核失败
	 */
	@Column(name = "ctq_auditstate", nullable = false)
	private Integer ctqAuditstate;

	public Integer getCtqId() {
		return ctqId;
	}

	public void setCtqId(Integer ctqId) {
		this.ctqId = ctqId;
	}

	public Integer getCtqItemid() {
		return ctqItemid;
	}

	public void setCtqItemid(Integer ctqItemid) {
		this.ctqItemid = ctqItemid;
	}

	public String getCtqItemname() {
		return ctqItemname;
	}

	public void setCtqItemname(String ctqItemname) {
		this.ctqItemname = ctqItemname;
	}

	public Integer getCtqStoreid() {
		return ctqStoreid;
	}

	public void setCtqStoreid(Integer ctqStoreid) {
		this.ctqStoreid = ctqStoreid;
	}

	public String getCtqStorename() {
		return ctqStorename;
	}

	public void setCtqStorename(String ctqStorename) {
		this.ctqStorename = ctqStorename;
	}

	public Timestamp getCtqAddtime() {
		return ctqAddtime;
	}

	public void setCtqAddtime(Timestamp ctqAddtime) {
		this.ctqAddtime = ctqAddtime;
	}

	public Integer getCtqAuditstate() {
		return ctqAuditstate;
	}

	public void setCtqAuditstate(Integer ctqAuditstate) {
		this.ctqAuditstate = ctqAuditstate;
	}
}