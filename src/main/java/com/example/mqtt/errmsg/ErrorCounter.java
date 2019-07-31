package com.example.mqtt.errmsg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.mqtt.errmsg.domain.MostError;

@Component
public class ErrorCounter {

	private HashMap<String, Integer> machineError = new HashMap<String, Integer>();
//	private HashMap<String, Integer> codeError = new HashMap<String, Integer>();
	private HashMap<String, Integer> panelLotError = new HashMap<String, Integer>();
	private MostError mostError = new MostError();

	public void AddMachineError(String machine) {
		int count = 0;
		if (machineError.containsKey(machine)) {
			count =  machineError.get(machine);
		} 
		machineError.put(machine, count + 1);
	}
	
	public void AddPanellotError(String panellot) {
		int count = 0;
		if (panelLotError.containsKey(panellot)) {
			count =  panelLotError.get(panellot);
		} 
		panelLotError.put(panellot, count + 1);
	}
	
	public void SetError(String machine, String panellot) {
		
	}

	public void test() {
		calcMostError();
//		for (Map.Entry<String, Integer> entry : mostError.entrySet()) {
//			System.out.println(entry.getKey() + entry.getValue());
//		}
	}
	public MostError getMostError() {
		calcMostError();
		return mostError;
	}
	
	private void calcMostError() {
		int max = 0;
		String error = "";
		for (Map.Entry<String, Integer> entry : machineError.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				error = entry.getKey();
			}
		}
		if (max > 0) {
			mostError.setMachineError(error);
			mostError.setMachineErrorCount(max);
		}
		max = 0;
		error = "";
		for (Map.Entry<String, Integer> entry : panelLotError.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				error = entry.getKey();
			}
		}
		if (max > 0) {
			mostError.setPanelLotError(error);
			mostError.setPanelLotErrorCount(max);
		}
	}
}
