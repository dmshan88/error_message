package com.example.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.common.NotifySubject;
import com.example.service.NotifySubjectService;

@Service
public class NotifySubjectServiceImpl implements NotifySubjectService {
    
    @Value("${app.phone_list}")
    private String phoneList;
    
    @Value("${app.e0210_list}")
    private String e0210List;
    
    @Value("${app.maintain_list}")
    private String maintainList;

    @Override
    public NotifySubject<String> getErrorMessageGroup() {
        NotifySubject<String> notifySubject = new NotifySubject<>();
        notifySubject.setList(Arrays.asList(phoneList.split(",")));
        return notifySubject;
    }

    @Override
    public NotifySubject<String> get0210Group() {
        NotifySubject<String> notifySubject = new NotifySubject<>();
        notifySubject.setList(Arrays.asList(e0210List.split(",")));
        return notifySubject;
    }

    @Override
    public NotifySubject<String> getMaintainGroup() {
        NotifySubject<String> notifySubject = new NotifySubject<>();
        notifySubject.setList(Arrays.asList(maintainList.split(",")));
        return notifySubject;
    }

}
