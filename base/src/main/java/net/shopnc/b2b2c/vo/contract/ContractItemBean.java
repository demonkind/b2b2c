package net.shopnc.b2b2c.vo.contract;

import java.math.BigDecimal;


public class ContractItemBean implements java.io.Serializable {
	private static final long serialVersionUID = 1L;


	private Integer ctiId;
	

	private String ctiName;
	

	private String ctiDescribe;
	

	private BigDecimal ctiCost;
	

	private String ctiIcon;
	

	private String ctiDescurl;
	

	private Integer ctiState=1;
	

	private Integer ctiSort;
	
	private String ctiIconUrl;

	public String getCtiIconUrl() {
		return ctiIconUrl;
	}

	public void setCtiIconUrl(String ctiIconUrl) {
		this.ctiIconUrl = ctiIconUrl;
	}

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