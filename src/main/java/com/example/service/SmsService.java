package com.example.service;

import com.example.common.NotifySubject;

public interface SmsService {

    /**发送短信*/
     void sendSms(String[] phoneNumbers, String[] params);
     
     void sendSms(NotifySubject<String> subject, String[] params);
}
