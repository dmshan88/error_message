package com.example.service;

import com.example.common.NotifySubject;

/**通知主体服务*/
public interface NotifySubjectService {
    
    /**报错组*/
    NotifySubject<String> getErrorMessageGroup();
    
    /**0210组*/
    NotifySubject<String> get0210Group();
    
    /**维护组*/
    NotifySubject<String> getMaintainGroup();

}
