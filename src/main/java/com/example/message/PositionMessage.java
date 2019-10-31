package com.example.message;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionMessage {
	
    @JSONField(name = "machine_id")
    private String machineId;
    
    private Long[] position;

}
