package com.example.mqtt.errmsg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.example.mqtt.errmsg.domain.OnlineMessage;
import com.example.mqtt.errmsg.domain.PanelErrorMessage;
import com.example.mqtt.errmsg.domain.PositionMessage;
import com.example.mqtt.errmsg.utils.ISmsSender;
import com.example.mqtt.errmsg.utils.PanelInfo;

@Component
@Configurable
@EnableScheduling
public class MqttSubscriber {

	@Value("${app.mqtt_broker}")
	private String broker;
	
	@Value("${app.mqtt_client_id}")
	private String clientId;
	
	@Value("${app.mqtt_username}")
	private String username;
	
	@Value("${app.mqtt_password}")
	private String password;
	
	@Value("${app.phone_list}")
	private String phoneList;
	
	@Value("${app.debug_app}")
	private boolean isDebug;
	
	@Autowired
	private ISmsSender smsSender;
	
//	@Autowired
//	private ErrorCounter errorCounter;

	private boolean isStart = false;
	private MqttAsyncClient mqttClient = null;
	private Vector<String> errorMessageVector = new Vector<String>();
	private Vector<String> fatalMessageVector = new Vector<String>();
	private Logger logger = LoggerFactory.getLogger(Application.class);
	
	private final String subscribedTopic = "dispather/#";
	private final Integer subscribedQos = 2;
	private final Integer publishQos = 2;
	private final String errorTopic = "dispather/panel_error_message";
	private final String onlineTopic = "dispather/online_message";
	private final String offlineTopic = "dispather/offline_message";
	private final String positionTopic = "dispather/position_message";
	private final String fatalTopic = "dispather/retained_online_state";
	private final String appMessageTopic = "error_app/error_message";
	private final String appMessageTopicDebug = "error_app/error_message_debug";
	private final String appControlErrorTopic = "error_app/control/error";
	private final String appControlErrorTopicDebug = "error_app/control_debug/error";
	private final String appControlDispatherTopic = "error_app/control/dispather";
	private final String appControlDispatherTopicDebug = "error_app/control_debug/dispather";
//	private final String errorServiceOff = "stat_error_off";
//	private final String errorServiceOn = "stat_error_on";
//	private final String dispatherServiceOff = "stat_dispather_off";
//	private final String dispatherServiceOn = "stat_dispather_on";
		
	MqttSubscriber() {}

	public void start() {
		logger.info("MqttSubscriber start");
		MqttConnectOptions connectOptions = new MqttConnectOptions(); 
		connectOptions.setAutomaticReconnect(true);
		connectOptions.setUserName(username);
		connectOptions.setPassword(password.toCharArray());
		connectOptions.setCleanSession(false);
		connectOptions.setKeepAliveInterval(30);
		connectOptions.setWill(isDebug ? appControlErrorTopicDebug: appControlErrorTopic,
				new String("off").getBytes(), 2, true);
	
		try {
			mqttClient = new MqttAsyncClient(broker, clientId);
			mqttClient.setCallback(new MqttCallbackExtended() {
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					logger.info("messageArrived! topic: " + topic 
							+ "; message: " + message.toString());
					AnalyzeMessage(topic, new String(message.getPayload()));
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					logger.info("deliveryComplete " + token.getMessageId());
				}
				
				@Override
				public void connectionLost(Throwable cause) {
					logger.error("connectionLost " + cause.getMessage());				
				}
				
				@Override
				public void connectComplete(boolean reconnect, String serverURI) {
					logger.info("connectComplete: " + serverURI  + ", is reconnect:" + reconnect);
					sendAppControlMessage(isDebug ? appControlErrorTopicDebug : appControlErrorTopic, "on", true);
					if (!reconnect) {
						try {
							mqttClient.subscribe(subscribedTopic, subscribedQos);
						} catch (MqttException e) {
							logger.error("subscribe error" + e.getMessage());
						}						
					}					
				}
			});
			mqttClient.connect(connectOptions, null, new IMqttActionListener() {
				
				@Override
				public void onSuccess(IMqttToken asyncActionToken) {
					logger.info("connect success");		
				}
				
				@Override
				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					logger.error("connect error" + exception.getMessage());					
				}
			});
			System.out.println("to connect");
			isStart = true;
		} catch (MqttException e) {
			logger.error("MqttException e");
			e.printStackTrace();
		}

	}
	
//	@Scheduled(fixedRate=5000)
	public void test() {
//		MostError mostError = errorCounter.getMostError();
//		String str =  String.format("mosterror machine:%s;count:%d;panellot:%s;count:%d", 
//				mostError.getMachineError(), mostError.getMachineErrorCount(),
//				mostError.getPanelLotError(), mostError.getPanelLotErrorCount());
//		logger.info(str);
	}
	
	@Scheduled(fixedRate=5000)
	public void checkMessage() {
		if (!isStart) {
			start();
			return;	
		}
		if (!mqttClient.isConnected()) {
			System.out.println("client not connected");
			return;
		}
		String message = getFatalMessage();
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
			String[] phoneNumbers = phoneList.split(",");
			if (stat.equals("off")) {
				param[1] = "停止服务!!!";
				smsSender.sendSms(phoneNumbers, param);	
				logger.info("dispather service off;");
				
				sendAppControlMessage(isDebug ? appControlDispatherTopicDebug : appControlDispatherTopic, "off" ,true);
				
			} else if (stat.equals("on")){
				param[1] = "开始服务";
				smsSender.sendSms(phoneNumbers, param);				
				sendAppControlMessage(isDebug ? appControlDispatherTopicDebug : appControlDispatherTopic, "on" ,true);
				logger.info("dispather service on;");
			} else {
				logger.info("unknown service;");
			}			
		}
		
		message = getTopMessage();
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
				obj.getPanelError().getSummary());
		logger.info("panel error message parse;" + str);
		String[] param = new String[5];
		param[0] = simpleName;
		param[1] = errorMessage.substring(0, Math.min(10,errorMessage.length()));
		param[2] = panelName;
		param[3] = panellot;
		param[4] = dateString;
		String[] phoneNumbers = phoneList.split(",");
		smsSender.sendSms(phoneNumbers, param);	
		String error_message = String.format("设备:%s 报错:%s 盘片:%s 批号%s:时间:%s", 
				simpleName, errorMessage, panelName, panellot,dateString);
		sendAppMessage(error_message, false);
//		errorCounter.AddMachineError(simpleName);
//		errorCounter.AddPanellotError(panelName + panellot);	
	}
	
	public  String getTopMessage() {
		if (this.errorMessageVector.size() == 0) {
			return null;
		}
		String message = this.errorMessageVector.firstElement();
		this.errorMessageVector.remove(0);
		return message;
	}
	
	public  String getFatalMessage() {
		if (this.fatalMessageVector.size() == 0) {
			return null;
		}
		String message = this.fatalMessageVector.firstElement();
		this.fatalMessageVector.remove(0);
		return message;
	}
	
	private void sendAppMessage (String message, boolean retained) {
		String topic = isDebug ? appMessageTopicDebug : appMessageTopic;
		logger.info("send app message:" + message);
		try {
			IMqttDeliveryToken token =  mqttClient.publish(topic, message.getBytes(), publishQos, retained);
			logger.info("publish topic:" + topic + token.getMessageId());
		} catch (MqttPersistenceException e) {			
			logger.info("send app message MqttPersistenceException:" + e.getMessage());
		} catch (MqttException e) {
			logger.info("send app message MqttException:" + e.getMessage());
		}
	}
	private void sendAppControlMessage (String topic, String message, boolean retained) {
		logger.info("send app control message:" + message);
		try {
			IMqttDeliveryToken token =  mqttClient.publish(topic, message.getBytes(), publishQos, retained);
			logger.info("publish topic:" + topic + token.getMessageId());
		} catch (MqttPersistenceException e) {			
			logger.info("send app control message MqttPersistenceException:" + e.getMessage());
		} catch (MqttException e) {
			logger.info("send app control message MqttException:" + e.getMessage());
		}
	}
	
	void AnalyzeMessage(String topic, String message) {
		if (topic.equals(fatalTopic)) {
			this.fatalMessageVector.add(message);
		} else if (topic.equals(errorTopic)) {
			this.errorMessageVector.add(message);
		} else if (topic.equals(onlineTopic) || topic.equals(offlineTopic)) {
			OnlineMessage obj = JSON.parseObject(message, OnlineMessage.class);
			String str = String.format("machineid:%s;",obj.getMachineId());
			logger.info("online message parse;" + str);		
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
			logger.info("position message parse;" + str);			
		}
	}
}
