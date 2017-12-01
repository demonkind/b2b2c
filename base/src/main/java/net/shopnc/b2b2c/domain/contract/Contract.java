package net.shopnc.b2b2c.domain.contract;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 店铺消费者保障服务加入情况表
 */
@Entity
@Table(name = "contract")
public class Contract implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
    @Id
    @GeneratedValue
    @Column(name = "ct_id")
    private Integer ctId;
    
    /**
     * 店铺ID
     */
    @Column(name = "ct_storeid")
    private Integer ctStoreid;
    
    /**
     * 店铺名称
     */
    @Column(name = "ct_storename")
    private String ctStorename;
    
    /**
     * 服务项目ID
     */
    @Column(name = "ct_itemid")
    private Integer ctItemid;
    
    /**
     * 申请审核状态0未审核1审核通过2审核失败3已支付保证金4保证金审核通过5保证金审核失败
     */
    @Column(name = "ct_auditstate")
    private Integer ctAuditstate;
    
    /**
     * 加入状态 0未申请 1已申请 2已加入
     */
    @Column(name = "ct_joinstate")
    private Integer ctJoinstate;
    
    /**
     * 保证金余额
     */
    @Column(name = "ct_cost")
    private BigDecimal ctCost;
    
    /**
     * 永久关闭 0永久关闭 1开启
     */
    @Column(name = "ct_closestate")
    private Integer ctClosestate;
    
    /**
     * 退出申请状态0未申请 1已申请 2申请失败
     */
    @Column(name = "ct_quitstate")
    private Integer ctQuitstate;

	public Integer getCtId() {
		return ctId;
	}

	public void setCtId(Integer ctId) {
		this.ctId = ctId;
	}

	public Integer getCtStoreid() {
		return ctStoreid;
	}

	public void setCtStoreid(Integer ctStoreid) {
		this.ctStoreid = ctStoreid;
	}

	public String getCtStorename() {
		return ctStorename;
	}

	public void setCtStorename(String ctStorename) {
		this.ctStorename = ctStorename;
	}

	public Integer getCtItemid() {
		return ctItemid;
	}

	public void setCtItemid(Integer ctItemid) {
		this.ctItemid = ctItemid;
	}

	public Integer getCtAuditstate() {
		return ctAuditstate;
	}

	public void setCtAuditstate(Integer ctAuditstate) {
		this.ctAuditstate = ctAuditstate;
	}

	public Integer getCtJoinstate() {
		return ctJoinstate;
	}

	public void setCtJoinstate(Integer ctJoinstate) {
		this.ctJoinstate = ctJoinstate;
	}

	public BigDecimal getCtCost() {
		return ctCost;
	}

	public void setCtCost(BigDecimal ctCost) {
		this.ctCost = ctCost;
	}

	public Integer getCtClosestate() {
		return ctClosestate;
	}

	public void setCtClosestate(Integer ctClosestate) {
		this.ctClosestate = ctClosestate;
	}

	public Integer getCtQuitstate() {
		return ctQuitstate;
	}

	public void setCtQuitstate(Integer ctQuitstate) {
		this.ctQuitstate = ctQuitstate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
