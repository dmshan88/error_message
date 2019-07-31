package com.example.mqtt.errmsg.utils;

import com.example.mqtt.errmsg.Application;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TencentSms implements ISmsSender {
	@Value("${app.enable_sms}")
	int enable;
	
	@Value("${app.sms_appid}")
	int appid;
	
	@Value("${app.sms_appkey}")
	String appkey;
	
	@Value("${app.sms_template_id}")
	int templateId; 
	// 签名
//	String smsSign = "腾讯云";
	private Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Override
	public void sendSms(String[] phoneNumbers, String[] params) {
		if (enable <= 0) {
			logger.info("disable sms");
			return;
		}
		
		try {
//			  String[] params = {"5678",};
		  SmsMultiSender msender = new SmsMultiSender(appid, appkey);
		  SmsMultiSenderResult result =  msender.sendWithParam("86", phoneNumbers,
		      templateId, params, "", "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
		  logger.info(result.toString());
		  
		} catch (HTTPException e) {
		  // HTTP 响应码错误
		  e.printStackTrace();
		} catch (JSONException e) {
		  // JSON 解析错误
		  e.printStackTrace();
		} catch (IOException e) {
		  // 网络 IO 错误
		  e.printStackTrace();
		}
	}
}
