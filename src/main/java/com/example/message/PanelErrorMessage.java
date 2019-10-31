package com.example.message;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PanelErrorMessage {
    
    @Getter
    @Setter
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
        
    }
    
    @JSONField(name = "machine_id")
    private String machineId;

    @JSONField(name = "panel_error")
    private PanelError panelError;
    
}
