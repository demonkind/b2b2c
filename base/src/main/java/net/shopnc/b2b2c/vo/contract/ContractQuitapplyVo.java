package net.shopnc.b2b2c.vo.contract;



public class ContractQuitapplyVo {
	
	private int ctqId;
	private int ctqItemid;
	private int ctqStoreid;
	private String ctqStorename;
	private String ctqAddtime;
	private int ctqAuditstate;
	private String ctqItemname;
	private String ctqState;
	private String cause;
	
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public int getCtqId() {
		return ctqId;
	}
	public void setCtqId(int ctqId) {
		this.ctqId = ctqId;
	}
	public int getCtqItemid() {
		return ctqItemid;
	}
	public void setCtqItemid(int ctqItemid) {
		this.ctqItemid = ctqItemid;
	}
	public int getCtqStoreid() {
		return ctqStoreid;
	}
	public void setCtqStoreid(int ctqStoreid) {
		this.ctqStoreid = ctqStoreid;
	}
	public String getCtqStorename() {
		return ctqStorename;
	}
	public void setCtqStorename(String ctqStorename) {
		this.ctqStorename = ctqStorename;
	}
	public String getCtqAddtime() {
		return ctqAddtime;
	}
	public void setCtqAddtime(String ctqAddtime) {
		this.ctqAddtime = ctqAddtime;
	}
	public int getCtqAuditstate() {
		return ctqAuditstate;
	}
	public void setCtqAuditstate(int ctqAuditstate) {
		this.ctqAuditstate = ctqAuditstate;
	}
	public String getCtqItemname() {
		return ctqItemname;
	}
	public void setCtqItemname(String ctqItemname) {
		this.ctqItemname = ctqItemname;
	}
	public String getCtqState() {
		return ctqState;
	}
	public void setCtqState(String ctqState) {
		this.ctqState = ctqState;
	}
}