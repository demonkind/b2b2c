package net.shopnc.b2b2c.domain.contract;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 店铺消费者保障服务保证金日志表
 */
@Entity
@Table(name = "contract_costlog")
public class ContractCostlog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "clog_id")
	private Integer clogId;
	
	/**
	 * 保障项目ID
	 */
	@Column(name = "clog_itemid", nullable = false)
	private Integer clogItemid;
	
	/**
	 * 保障项目名称
	 */
	@Column(name = "clog_itemname", nullable = false, length = 100)
	private String clogItemname;
	
	/**
	 * 店铺ID
	 */
	@Column(name = "clog_storeid", nullable = false)
	private Integer clogStoreid;
	
	/**
	 * 店铺名称
	 */
	@Column(name = "clog_storename", nullable = false, length = 500)
	private String clogStorename;
	
	/**
	 * 操作管理员ID
	 */
	@Column(name = "clog_adminid")
	private Integer clogAdminid;
	
	/**
	 * 操作管理员名称
	 */
	@Column(name = "clog_adminname", length = 200)
	private String clogAdminname;
	
	/**
	 * 金额
	 */
	@Column(name = "clog_price", nullable = false)
	private BigDecimal clogPrice;
	
	/**
	 * 添加时间
	 */
	@Column(name = "clog_addtime", nullable = false)
	private Timestamp clogAddtime;
	
	/**
	 * 描述
	 */
	@Column(name = "clog_desc", nullable = false, length = 2000)
	private String clogDesc;

	public Integer getClogId() {
		return clogId;
	}

	public void setClogId(Integer clogId) {
		this.clogId = clogId;
	}

	public Integer getClogItemid() {
		return clogItemid;
	}

	public void setClogItemid(Integer clogItemid) {
		this.clogItemid = clogItemid;
	}

	public String getClogItemname() {
		return clogItemname;
	}

	public void setClogItemname(String clogItemname) {
		this.clogItemname = clogItemname;
	}

	public Integer getClogStoreid() {
		return clogStoreid;
	}

	public void setClogStoreid(Integer clogStoreid) {
		this.clogStoreid = clogStoreid;
	}

	public String getClogStorename() {
		return clogStorename;
	}

	public void setClogStorename(String clogStorename) {
		this.clogStorename = clogStorename;
	}

	public Integer getClogAdminid() {
		return clogAdminid;
	}

	public void setClogAdminid(Integer clogAdminid) {
		this.clogAdminid = clogAdminid;
	}

	public String getClogAdminname() {
		return clogAdminname;
	}

	public void setClogAdminname(String clogAdminname) {
		this.clogAdminname = clogAdminname;
	}

	public BigDecimal getClogPrice() {
		return clogPrice;
	}

	public void setClogPrice(BigDecimal clogPrice) {
		this.clogPrice = clogPrice;
	}

	public Timestamp getClogAddtime() {
		return clogAddtime;
	}

	public void setClogAddtime(Timestamp clogAddtime) {
		this.clogAddtime = clogAddtime;
	}

	public String getClogDesc() {
		return clogDesc;
	}

	public void setClogDesc(String clogDesc) {
		this.clogDesc = clogDesc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}