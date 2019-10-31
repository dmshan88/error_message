package com.example.service;

import java.util.Vector;

import org.springframework.stereotype.Service;

@Service
public class MessageStoreService {
    
    private Vector<String> errorMessageVector = new Vector<String>();
    private Vector<String> fatalMessageVector = new Vector<String>();
    
    public void putFatalMessage(String message) {
        this.fatalMessageVector.add(message);
    }
    
    public void putErrorMessage(String message) {
        this.errorMessageVector.add(message);
    }
    
    public String getErrorMessage() {
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
    
}
