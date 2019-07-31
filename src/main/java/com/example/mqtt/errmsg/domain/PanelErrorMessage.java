package com.example.mqtt.errmsg.domain;

public class PanelErrorMessage {
	private String machine_id;
	private PanelError panel_error;
	
	public String getMachineId() {
		return machine_id;
	}
	public void setMachineId(String machine_id) {
		this.machine_id = machine_id;
	}
	public PanelError getPanelError() {
		return panel_error;
	}
	public void setPanelError(PanelError panel_error) {
		this.panel_error = panel_error;
	}

}
