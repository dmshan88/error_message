package com.example.mqtt.errmsg.domain;

public class PanelError {
	private Long chkdatetime;
	private Integer errcode;
	private String hardware1version;
	private String hardware2version;
	private String machineid;
	private Integer panelid;
	private Integer panelindex;
	private String panellot;
	private String sampletype;
	private String softversion;
	private String summary;
	
	public Long getChkdatetime() {
		return chkdatetime;
	}
	public void setChkdatetime(Long chkdatetime) {
		this.chkdatetime = chkdatetime;
	}
	public Integer getErrcode() {
		return errcode;
	}
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	public String getHardware1version() {
		return hardware1version;
	}
	public void setHardware1version(String hardware1version) {
		this.hardware1version = hardware1version;
	}
	public String getHardware2version() {
		return hardware2version;
	}
	public void setHardware2version(String hardware2version) {
		this.hardware2version = hardware2version;
	}
	public String getMachineid() {
		return machineid;
	}
	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}
	public Integer getPanelid() {
		return panelid;
	}
	public void setPanelid(Integer panelid) {
		this.panelid = panelid;
	}
	public Integer getPanelindex() {
		return panelindex;
	}
	public void setPanelindex(Integer panelindex) {
		this.panelindex = panelindex;
	}
	public String getPanellot() {
		return panellot;
	}
	public void setPanellot(String panellot) {
		this.panellot = panellot;
	}
	public String getSampletype() {
		return sampletype;
	}
	public void setSampletype(String sampletype) {
		this.sampletype = sampletype;
	}
	public String getSoftversion() {
		return softversion;
	}
	public void setSoftversion(String softversion) {
		this.softversion = softversion;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}		
}
