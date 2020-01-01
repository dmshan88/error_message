package com.example.service.impl;

import com.example.common.NotifySubject;
import com.example.service.SmsService;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import lombok.extern.log4j.Log4j;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Log4j
@Service
public class TencentSmsServiceImpl implements SmsService {
    
    @Value("${app.enable_sms}")
    int enable;
    
    @Value("${app.sms_appid}")
    int appid;
    
    @Value("${app.sms_appkey}")
    String appkey;
    
    @Value("${app.sms_template_id}")
    int templateId; 
    // 签名
//    String smsSign = "腾讯云";
    
    @Override
    public void sendSms(String[] phoneNumbers, String[] params) {
        if (enable <= 0) {
            log.info("disable sms");
            return;
        }
        try {
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            SmsMultiSenderResult result =  msender.sendWithParam("86", phoneNumbers,
              templateId, params, "", "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
          log.info(result.toString());
        } catch (HTTPException e) {
          // HTTP 响应码错误
            log.error(e.getMessage());
        } catch (JSONException e) {
          // JSON 解析错误
            log.error(e.getMessage());
        } catch (IOException e) {
          // 网络 IO 错误
            log.error(e.getMessage());
        }
    }

    @Override
    public void sendSms(NotifySubject<String> subject, String[] params) {
        if (subject == null || subject.getList() == null || subject.getList().isEmpty()) {
            log.error("subject empty");
        }
        List<String> list = subject.getList();
        sendSms(list.toArray(new String[list.size()]), params);
        
    }
}
