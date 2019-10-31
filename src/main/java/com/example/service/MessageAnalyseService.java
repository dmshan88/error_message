package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.message.OnlineMessage;
import com.example.message.PositionMessage;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class MessageAnalyseService {
    
    @Autowired
    private MessageStoreService messageStoreService;
    
    private final String errorTopic = "dispather/panel_error_message";
    private final String onlineTopic = "dispather/online_message";
    private final String offlineTopic = "dispather/offline_message";
    private final String positionTopic = "dispather/position_message";
    private final String fatalTopic = "dispather/retained_online_state";
    
    void AnalyzeMessage(String topic, String message) {
        if (topic.equals(fatalTopic)) {
            messageStoreService.putFatalMessage(message);
        } else if (topic.equals(errorTopic)) {
            messageStoreService.putErrorMessage(message);
        } else if (topic.equals(onlineTopic) || topic.equals(offlineTopic)) {
            OnlineMessage obj = JSON.parseObject(message, OnlineMessage.class);
            String str = String.format("machineid:%s;",obj.getMachineId());
            log.info("online message parse;" + str);
        } else if (topic.equals(positionTopic)) {
            PositionMessage obj = JSON.parseObject(message, PositionMessage.class);
            Long[] position = obj.getPosition();
            String str;
            if (position.length == 4) {
                str = String.format("machineid:%s; mcc:%d; mnc:%d; lac:%d ci:%d;", obj.getMachineId(),
                        position[0], position[1], position[2], position[3]);
            } else {
                str = "error (position.length != 4)";
            }
            log.info("position message parse;" + str);
        }
    }

}
