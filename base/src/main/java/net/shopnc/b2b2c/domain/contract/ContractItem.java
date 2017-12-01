package net.shopnc.b2b2c.domain.contract;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 消费者保障服务项目表
 */
@Entity
@Table(name = "contract_item")
public class ContractItem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "cti_id")
	private Integer ctiId;
	
	/**
	 * 保障项目名称
	 */
	@Column(name = "cti_name", nullable = false, length = 100)
	private String ctiName;
	
	/**
	 * 保障项目描述
	 */
	@Column(name = "cti_describe", nullable = false, length = 2000)
	private String ctiDescribe;
	
	/**
	 * 保证金
	 */
	@Column(name = "cti_cost", nullable = false)
	private BigDecimal ctiCost;
	
	/**
	 * 图标
	 */
	@Column(name = "cti_icon", nullable = false, length = 500)
	private String ctiIcon;
	
	/**
	 * 内容介绍文章地址
	 */
	@Column(name = "cti_descurl", length = 500)
	private String ctiDescurl;
	
	/**
	 * 状态 0关闭 1开启
	 */
	@Column(name = "cti_state", nullable = false)
	private Integer ctiState=1;
	
	/**
	 * 排序
	 */
	@Column(name = "cti_sort")
	private Integer ctiSort;

	public Integer getCtiId() {
		return ctiId;
	}

	public void setCtiId(Integer ctiId) {
		this.ctiId = ctiId;
	}

	public String getCtiName() {
		return ctiName;
	}

	public void setCtiName(String ctiName) {
		this.ctiName = ctiName;
	}

	public String getCtiDescribe() {
		return ctiDescribe;
	}

	public void setCtiDescribe(String ctiDescribe) {
		this.ctiDescribe = ctiDescribe;
	}

	public BigDecimal getCtiCost() {
		return ctiCost;
	}

	public void setCtiCost(BigDecimal ctiCost) {
		this.ctiCost = ctiCost;
	}

	public String getCtiIcon() {
		return ctiIcon;
	}

	public void setCtiIcon(String ctiIcon) {
		this.ctiIcon = ctiIcon;
	}

	public String getCtiDescurl() {
		return ctiDescurl;
	}

	public void setCtiDescurl(String ctiDescurl) {
		this.ctiDescurl = ctiDescurl;
	}

	public Integer getCtiState() {
		return ctiState;
	}

	public void setCtiState(Integer ctiState) {
		this.ctiState = ctiState;
	}

	public Integer getCtiSort() {
		return ctiSort;
	}

	public void setCtiSort(Integer ctiSort) {
		this.ctiSort = ctiSort;
	}
}