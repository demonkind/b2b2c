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
 * 店铺加入消费者保障服务申请表
 */
@Entity
@Table(name = "contract_apply")
public class ContractApply implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "cta_id")
	private Integer ctaId;
	
	/**
	 * 保障项目ID
	 */
	@Column(name = "cta_itemid", nullable = false)
	private Integer ctaItemid;
	
	/**
	 * 店铺ID
	 */
	@Column(name = "cta_storeid", nullable = false)
	private Integer ctaStoreid;
	
	/**
	 * 店铺名称
	 */
	@Column(name = "cta_storename", nullable = false, length = 500)
	private String ctaStorename;
	
	/**
	 * 申请时间
	 */
	@Column(name = "cta_addtime", nullable = false)
	private Timestamp ctaAddtime;
	
	/**
	 * 审核状态 0未审核 1审核通过 2审核失败 3保证金待审核 4保证金审核通过 5保证金审核失败
	 */
	@Column(name = "cta_auditstate", nullable = false)
	private Integer ctaAuditstate=0;
	
	/**
	 * 保证金金额
	 */
	@Column(name = "cta_cost")
	private BigDecimal ctaCost;
	
	/**
	 * 保证金付款凭证图片
	 */
	@Column(name = "cta_costimg", length = 500)
	private String ctaCostimg;

	public Integer getCtaId() {
		return ctaId;
	}

	public void setCtaId(Integer ctaId) {
		this.ctaId = ctaId;
	}

	public Integer getCtaItemid() {
		return ctaItemid;
	}

	public void setCtaItemid(Integer ctaItemid) {
		this.ctaItemid = ctaItemid;
	}

	public Integer getCtaStoreid() {
		return ctaStoreid;
	}

	public void setCtaStoreid(Integer ctaStoreid) {
		this.ctaStoreid = ctaStoreid;
	}

	public String getCtaStorename() {
		return ctaStorename;
	}

	public void setCtaStorename(String ctaStorename) {
		this.ctaStorename = ctaStorename;
	}

	public Timestamp getCtaAddtime() {
		return ctaAddtime;
	}

	public void setCtaAddtime(Timestamp ctaAddtime) {
		this.ctaAddtime = ctaAddtime;
	}

	public Integer getCtaAuditstate() {
		return ctaAuditstate;
	}

	public void setCtaAuditstate(Integer ctaAuditstate) {
		this.ctaAuditstate = ctaAuditstate;
	}

	public BigDecimal getCtaCost() {
		return ctaCost;
	}

	public void setCtaCost(BigDecimal ctaCost) {
		this.ctaCost = ctaCost;
	}

	public String getCtaCostimg() {
		return ctaCostimg;
	}

	public void setCtaCostimg(String ctaCostimg) {
		this.ctaCostimg = ctaCostimg;
	}
}