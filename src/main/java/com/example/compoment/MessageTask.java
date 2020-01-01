package com.example.compoment;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.example.common.PanelInfo;
import com.example.message.PanelErrorMessage;
import com.example.service.MessageStoreService;
import com.example.service.MqttSubscriberService;
import com.example.service.NotifySubjectService;
import com.example.service.SmsService;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class MessageTask  implements Runnable {
    
    @Autowired
    private NotifySubjectService notifySubjectService;
    
    @Autowired
    private SmsService smsSender;
    
    @Autowired
    private MessageStoreService messageStoreService;
    
    @Autowired
    private MqttSubscriberService mqttSubscriberService;

    @Override
    public void run() {
        if (!mqttSubscriberService.isStart()) {
            mqttSubscriberService.start();
            return;
        }
        if (mqttSubscriberService.getMqttClient() == null || !mqttSubscriberService.getMqttClient().isConnected()) {
            System.out.println("client not connected");
            return;
        }
        String message = messageStoreService.getFatalMessage();
        if (message == null) {
            System.out.println("check no fatal message:");
        } else {
            String stat = message;
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
            String dateString = sdf.format(new Date());
            String[] param = new String[5];
            param[0] = "报错提醒软件";
            param[1] = "。。";
            param[2] = "..";
            param[3] = "..";
            param[4] = dateString;
//            String[] phoneNumbers = phoneList.split(",");
            if (stat.equals("off")) {
                param[1] = "停止服务!!!";
                smsSender.sendSms(notifySubjectService.getMaintainGroup(), param);
                log.info("dispather service off;");
            } else if (stat.equals("on")){
                param[1] = "开始服务";
                smsSender.sendSms(notifySubjectService.getMaintainGroup(), param);
                log.info("dispather service on;");
            } else {
                log.info("unknown service;");
            }
        }
        
        message = messageStoreService.getErrorMessage();
        if (message == null) {
            System.out.println("check no error message:");
            return;
        }
        PanelErrorMessage obj = JSON.parseObject(message, PanelErrorMessage.class);
        String errorMessage = PanelInfo.parseError(obj.getPanelError().getErrcode());
        String panelName = PanelInfo.getPanelName(obj.getPanelError().getPanelid());
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
        String dateString = sdf.format(new Date(obj.getPanelError().getChkdatetime() * 1000));
        String simpleName = PanelInfo.simpleMachineId(obj.getMachineId());
        String panellot = obj.getPanelError().getPanellot();
        String summary = obj.getPanelError().getSummary();
        String str = String.format("timestamp:%d;datetime:%s;machineid:%s;simplename:%s;panelid:%d;panename:%s;panellot:%s;errcode:%d;errmessage:%s;summary:%s;",
                obj.getPanelError().getChkdatetime(),
                dateString,
                obj.getMachineId(), 
                simpleName,
                obj.getPanelError().getPanelid(),
                panelName,
                panellot,
                obj.getPanelError().getErrcode(),
                errorMessage,
                summary);
        log.info("panel error message parse;" + str);
        String[] param = new String[5];
        param[0] = simpleName;
        param[1] = summary.contains("0208") ? summary : errorMessage.substring(0, Math.min(10,errorMessage.length()));
//        param[1] = errorMessage.substring(0, Math.min(10,errorMessage.length()));
        param[2] = panelName;
        param[3] = panellot;
        param[4] = dateString;
//        String[] phoneNumbers = phoneList.split(",");
        smsSender.sendSms(notifySubjectService.getErrorMessageGroup(), param);
        if (summary.contains("0210") || summary.contains("0211")) {
            smsSender.sendSms(notifySubjectService.get0210Group(), param);
        }
    }

}
