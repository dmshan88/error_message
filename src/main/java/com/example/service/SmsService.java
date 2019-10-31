package com.example.service;

public interface SmsService {

    /**发送短信*/
     void sendSms(String[] phoneNumbers, String[] params);
}
