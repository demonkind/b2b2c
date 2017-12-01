package net.shopnc.b2b2c.domain.contract;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 店铺消费者保障服务日志表
 */
@Entity
@Table(name = "contract_log")
public class ContractLog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "log_id")
	private Integer logId;
	
	/**
	 * 店铺ID
	 */
	@Column(name = "log_storeid", nullable = false)
	private Integer logStoreid;
	
	/**
	 * 店铺名称
	 */
	@Column(name = "log_storename", nullable = false, length = 500)
	private String logStorename;
	
	/**
	 * 服务项目ID
	 */
	@Column(name = "log_itemid", nullable = false)
	private Integer logItemid;
	
	/**
	 * 服务项目名称
	 */
	@Column(name = "log_itemname", nullable = false, length = 100)
	private String logItemname;
	
	/**
	 * 操作描述
	 */
	@Column(name = "log_msg", nullable = false, length = 1000)
	private String logMsg;
	
	/**
	 * 添加时间
	 */
	@Column(name = "log_addtime", nullable = false)
	private Timestamp logAddtime;
	
	/**
	 * 操作者角色 管理员为admin 商家为seller
	 */
	@Column(name = "log_role", nullable = false, length = 100)
	private String logRole;
	
	/**
	 * 操作者ID
	 */
	@Column(name = "log_userid", nullable = false)
	private Integer logUserid;
	
	/**
	 * 操作者名称
	 */
	@Column(name = "log_username", nullable = false, length = 200)
	private String logUsername;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getLogStoreid() {
		return logStoreid;
	}

	public void setLogStoreid(Integer logStoreid) {
		this.logStoreid = logStoreid;
	}

	public String getLogStorename() {
		return logStorename;
	}

	public void setLogStorename(String logStorename) {
		this.logStorename = logStorename;
	}

	public Integer getLogItemid() {
		return logItemid;
	}

	public void setLogItemid(Integer logItemid) {
		this.logItemid = logItemid;
	}

	public String getLogItemname() {
		return logItemname;
	}

	public void setLogItemname(String logItemname) {
		this.logItemname = logItemname;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}

	public Timestamp getLogAddtime() {
		return logAddtime;
	}

	public void setLogAddtime(Timestamp logAddtime) {
		this.logAddtime = logAddtime;
	}

	public String getLogRole() {
		return logRole;
	}

	public void setLogRole(String logRole) {
		this.logRole = logRole;
	}

	public Integer getLogUserid() {
		return logUserid;
	}

	public void setLogUserid(Integer logUserid) {
		this.logUserid = logUserid;
	}

	public String getLogUsername() {
		return logUsername;
	}

	public void setLogUsername(String logUsername) {
		this.logUsername = logUsername;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}