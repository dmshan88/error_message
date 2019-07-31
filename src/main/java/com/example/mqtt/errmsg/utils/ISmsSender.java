package com.example.mqtt.errmsg.utils;

public interface ISmsSender {
	public abstract void sendSms(String[] phoneNumbers, String[] params);
}
