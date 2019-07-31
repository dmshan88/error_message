package com.example.mqtt.errmsg.domain;

public class PositionMessage {
	private String machine_id;
	private Long[] position;
	
	public String getMachineId() {
		return machine_id;
	}
	public void setMachineId(String machine_id) {
		this.machine_id = machine_id;
	}
	public Long[] getPosition() {
		return position;
	}
	public void setPosition(Long[] position) {
		this.position = position;
	}

}
