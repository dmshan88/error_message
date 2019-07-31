package com.example.mqtt.errmsg.domain;

public class MostError {
	private String machineError;
	private Integer machineErrorCount;
	
	public String getMachineError() {
		return machineError;
	}
	public void setMachineError(String machineError) {
		this.machineError = machineError;
	}
	public Integer getMachineErrorCount() {
		return machineErrorCount;
	}
	public void setMachineErrorCount(Integer machineErrorCount) {
		this.machineErrorCount = machineErrorCount;
	}
	public String getPanelLotError() {
		return panelLotError;
	}
	public void setPanelLotError(String panelLotError) {
		this.panelLotError = panelLotError;
	}
	public Integer getPanelLotErrorCount() {
		return panelLotErrorCount;
	}
	public void setPanelLotErrorCount(Integer panelLotErrorCount) {
		this.panelLotErrorCount = panelLotErrorCount;
	}
	private String panelLotError;
	private Integer panelLotErrorCount;

}
