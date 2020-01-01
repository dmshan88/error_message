package com.example.service;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class MqttSubscriberService {

    @Value("${app.mqtt_broker}")
    private String broker;
    
    @Value("${app.mqtt_client_id}")
    private String clientId;
    
    @Value("${app.mqtt_username}")
    private String username;
    
    @Value("${app.mqtt_password}")
    private String password;
    
    @Value("${app.debug_app}")
    private boolean debug;
    
    @Autowired
    private MessageAnalyseService messageAnalyseService;
    
    @Getter
    private boolean start = false;
    
    @Getter
    private MqttAsyncClient mqttClient = null;
    
    private final String subscribedTopic = "dispather/#";
    private final Integer subscribedQos = 2;
    private final String appControlErrorTopic = "error_app/control/error";
    private final String appControlErrorTopicDebug = "error_app/control_debug/error";
    
    public void start() {
        log.info("MqttSubscriber start");
        MqttConnectOptions connectOptions = new MqttConnectOptions(); 
        connectOptions.setAutomaticReconnect(true);
        connectOptions.setUserName(username);
        connectOptions.setPassword(password.toCharArray());
        connectOptions.setCleanSession(false);
        connectOptions.setKeepAliveInterval(30);
        connectOptions.setWill(debug ? appControlErrorTopicDebug: appControlErrorTopic,
                new String("off").getBytes(), 2, true);
        try {
            mqttClient = new MqttAsyncClient(broker, clientId);
            mqttClient.setCallback(new MqttCallbackExtended() {
                
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    log.info("messageArrived! topic: " + topic 
                            + "; message: " + message.toString());
                    messageAnalyseService.AnalyzeMessage(topic, new String(message.getPayload()));
                }
                
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    log.info("deliveryComplete " + token.getMessageId());
                }
                
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("connectionLost " + cause.getMessage());
                }
                
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    log.info("connectComplete: " + serverURI  + ", is reconnect:" + reconnect);
                    if (!reconnect) {
                        try {
                            mqttClient.subscribe(subscribedTopic, subscribedQos);
                        } catch (MqttException e) {
                            log.error("subscribe error" + e.getMessage());
                        }
                    }
                }
            });
            mqttClient.connect(connectOptions, null, new IMqttActionListener() {
                
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    log.info("connect success");
                }
                
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    log.error("connect error" + exception.getMessage());
                }
            });
            System.out.println("to connect");
            start = true;
        } catch (MqttException e) {
            log.error("MqttException e" + e.getMessage());
        }
    }

}
